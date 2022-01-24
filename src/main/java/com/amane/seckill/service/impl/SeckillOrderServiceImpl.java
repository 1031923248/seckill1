package com.amane.seckill.service.impl;

import com.amane.seckill.mapper.SeckillOrderMapper;
import com.amane.seckill.pojo.SeckillOrder;
import com.amane.seckill.pojo.User;
import com.amane.seckill.service.SeckillOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements SeckillOrderService{
    @Autowired
    private SeckillOrderService seckillOrderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id",user.getPhone()).
                eq("goods_id",goodsId));
        if (seckillOrder != null){
            return seckillOrder.getOrderId();
        }else if (redisTemplate.hasKey("isEmpty:"+goodsId)){
            return -1L;
        }else
            return 0L;
    }
}
