package com.amane.seckill.service.impl;

import com.amane.seckill.Exception.GlobalException;
import com.amane.seckill.mapper.OrderMapper;
import com.amane.seckill.pojo.Order;
import com.amane.seckill.pojo.SeckillGoods;
import com.amane.seckill.pojo.SeckillOrder;
import com.amane.seckill.pojo.User;
import com.amane.seckill.service.GoodService;
import com.amane.seckill.service.OrderService;
import com.amane.seckill.service.SeckillGoodsService;
import com.amane.seckill.service.SeckillOrderService;
import com.amane.seckill.utils.MD5Util;
import com.amane.seckill.utils.UUIDUtil;
import com.amane.seckill.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    GoodService goodService;
    @Autowired
    SeckillGoodsService seckillGoodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private SeckillOrderService seckillOrderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public Order seckill(User user, GoodsVo goods) {
        SeckillGoods good = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id",goods.getId()));
//        if (good.getStockCount() < 1){
//            redisTemplate.opsForValue().set("isEmpty:"+goods.getId(),1);
//            return null;
//        }
//        good.setStockCount(good.getStockCount()-1);
        seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().eq("goods_id",
                goods.getId()).setSql("stock_count = stock_count-1"));
        //good.setStockCount(good.getStockCount()-1);
        //seckillGoodsService.updateById(good);
        //boolean res = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().gt("stock_count",0).eq("goods_id",
        //goods.getId()).setSql("stock_count = stock_count-1"));

        /*if(!res){
            return null;
        }*/
        Order order = new Order();
        order.setUserId(user.getPhone());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsPrice(good.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(order.getGoodsId());
        seckillOrder.setUserId(user.getPhone());
        seckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:"+user.getPhone()+":"+goods.getId(),seckillOrder);
        return order;
    }

    @Override
    public OrderDetailVo detail(Long orderId) {
        if(orderId == null){
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodService.getGoodsVoById(order.getGoodsId());
        OrderDetailVo detailVo = new OrderDetailVo();
        detailVo.setGoodsVo(goodsVo);
        detailVo.setOrder(order);
        return detailVo;
    }

    @Override
    public List<ResultVo> getResult(Long goodsId) {
        return orderMapper.findResultByGoodsID(goodsId);
    }

    @Override
    public String createPath(User user, Long goodsId) {
        String path = MD5Util.md5(UUIDUtil.uuid()+"13579");
        redisTemplate.opsForValue().set("seckillPath:"+user.getPhone(),path,60, TimeUnit.SECONDS);
        return path;
    }

    @Override
    public boolean verifyPath(User user, Long goodsId,String path) {
        if (user == null || StringUtils.isEmpty(path)){
            return false;
        }
        String verify = (String) redisTemplate.opsForValue().get("seckillPath:"+user.getPhone());
        return path.equals(verify);
    }

    @Override
    public List<ResultVo> checkOrder(Long userId) {
        return orderMapper.findResultByUserId(userId);
    }
}
