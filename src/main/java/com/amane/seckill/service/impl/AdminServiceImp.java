package com.amane.seckill.service.impl;

import com.amane.seckill.controller.SeckillController;
import com.amane.seckill.controller.SeckillGoodsController;
import com.amane.seckill.mapper.AdminMapper;
import com.amane.seckill.mapper.GoodsMapper;
import com.amane.seckill.mapper.SeckillGoodsMapper;
import com.amane.seckill.pojo.Goods;
import com.amane.seckill.pojo.SeckillGoods;
import com.amane.seckill.service.AdminService;
import com.amane.seckill.service.GoodService;
import com.amane.seckill.utils.CookieUtil;
import com.amane.seckill.utils.UUIDUtil;
import com.amane.seckill.vo.AdminVo;
import com.amane.seckill.vo.GoodsVo;
import com.amane.seckill.vo.RespBean;
import com.amane.seckill.vo.RespBeanEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImp extends ServiceImpl<AdminMapper, AdminVo> implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Override
    public RespBean doLogin(AdminVo adminVo, HttpServletRequest request, HttpServletResponse response) {
        String id = adminVo.getId();
        String password = adminVo.getPassword();

        if(StringUtils.isEmpty(password) || StringUtils.isEmpty(id)){
            return RespBean.error(RespBeanEnum.EMPTY_ERROR);
        }
        AdminVo admin = adminMapper.selectById(id);
        if(admin == null){
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        if(!admin.getPassword().equals(password)){
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }

        String ticket = UUIDUtil.uuid();
        redisTemplate.opsForValue().set("user:"+ticket,admin);
        CookieUtil.setCookie(request,response,"adminTicket",ticket);
        return RespBean.success(ticket);
    }

    @Override
    public RespBean addGoods(GoodsVo goodsVo) {
        String name = goodsVo.getGoodsName();
        String detail = goodsVo.getGoodsDetail();
        BigDecimal price = goodsVo.getGoodsPrice();
        BigDecimal seckillPrice = goodsVo.getSeckillPrice();
        Integer stockCount = goodsVo.getStockCount();
        Date startDate = goodsVo.getStartDate();
        if (goodsMapper.findGoodByName(name) != null){
            return RespBean.error(RespBeanEnum.REPEAT_GOOD);
        }
        Date endDate = goodsVo.getEndDate();
        Goods good = new Goods();
        SeckillGoods seckillGoods = new SeckillGoods();
        good.setGoodsDetail(detail);
        good.setGoodsName(name);
        good.setGoodsStock(stockCount);
        good.setGoodsPrice(price);
        goodsMapper.insert(good);
        Goods goods = goodsMapper.findGoodByName(name);
        Long id = goods.getId();
        seckillGoods.setGoodsId(id);
        seckillGoods.setStockCount(stockCount);
        seckillGoods.setSeckillPrice(seckillPrice);
        seckillGoods.setEndDate(endDate);
        seckillGoods.setStartDate(startDate);
        seckillGoodsMapper.insert(seckillGoods);
        SeckillController.emptyStock.put(id,false);
        redisTemplate.opsForValue().set("msGoods:"+id,stockCount);
        return RespBean.success();
    }
}
