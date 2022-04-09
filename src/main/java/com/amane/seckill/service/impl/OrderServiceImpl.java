package com.amane.seckill.service.impl;

import com.amane.seckill.Exception.GlobalException;
import com.amane.seckill.mapper.AdminMapper;
import com.amane.seckill.mapper.OrderMapper;
import com.amane.seckill.mapper.UserMapper;
import com.amane.seckill.pojo.*;
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

import java.math.BigDecimal;
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
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AdminMapper adminMapper;
    @Override
    @Transactional
    public Order seckill(User user, GoodsVo goods) {
        SeckillGoods good = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id",goods.getId()));

        seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().eq("goods_id",
                goods.getId()).setSql("stock_count = stock_count-1"));

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

    @Override
    public RespBean doPay(Long orderID) {
        Order order = orderMapper.selectById(orderID);
        String uid = String.valueOf(order.getUserId());
        User user = userMapper.getByPhone(uid);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone",uid);
        BigDecimal balance = user.getBalance();
        BigDecimal orderPrice = order.getGoodsPrice();
        if (balance.compareTo(orderPrice) < 0 ){
            return RespBean.error(RespBeanEnum.BANLANCE_ERROR);
        }
        user.setBalance(balance.subtract(orderPrice));
        userMapper.update(user,wrapper);
        adminMapper.updateAccount(orderPrice);
        order.setPayDate(new Date());
        order.setStatus(1);
        orderMapper.updateById(order);
        return RespBean.success();
    }
}
