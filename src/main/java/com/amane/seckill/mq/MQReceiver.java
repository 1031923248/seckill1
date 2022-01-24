package com.amane.seckill.mq;

import com.amane.seckill.pojo.SeckillMessage;
import com.amane.seckill.pojo.SeckillOrder;
import com.amane.seckill.pojo.User;
import com.amane.seckill.service.GoodService;
import com.amane.seckill.service.OrderService;
import com.amane.seckill.utils.JsonUtil;
import com.amane.seckill.vo.GoodsVo;
import com.amane.seckill.vo.RespBean;
import com.amane.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class MQReceiver {
    @Autowired
    private GoodService goodService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderService orderService;
    @RabbitListener(queues = "msQueue")
    public void receive(String msg){
        log.info(msg);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(msg, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        GoodsVo goodsVo = goodService.getGoodsVoById(goodsId);
        if(goodsVo.getStockCount() < 1){
            return;
        }
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:"+user.getPhone()+":"+goodsId);
        if(seckillOrder != null){
            return;
        }
        orderService.seckill(user,goodsVo);
    }
}
