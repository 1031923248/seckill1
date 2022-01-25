package com.amane.seckill.controller;

import com.amane.seckill.mq.MQSender;
import com.amane.seckill.pojo.Order;
import com.amane.seckill.pojo.SeckillMessage;
import com.amane.seckill.pojo.SeckillOrder;
import com.amane.seckill.pojo.User;
import com.amane.seckill.service.GoodService;
import com.amane.seckill.service.OrderService;
import com.amane.seckill.service.SeckillGoodsService;
import com.amane.seckill.service.SeckillOrderService;
import com.amane.seckill.utils.JsonUtil;
import com.amane.seckill.vo.GoodsVo;
import com.amane.seckill.vo.RespBean;
import com.amane.seckill.vo.RespBeanEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbitmq.tools.json.JSONUtil;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {
    @Autowired
    private GoodService goodService;
    @Autowired
    private SeckillOrderService seckillOrderService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSender mqSender;

    private Map<Long,Boolean> emptyStock = new HashMap<>();

    @RequestMapping(value = "/dokill",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public RespBean doKill( User user,Long goodsId){
        if(user == null){
            return RespBean.error(RespBeanEnum.USER_NOT_EXIST);
        }
        if (emptyStock.get(goodsId)){
            return RespBean.error(RespBeanEnum.STORK_EMPTY);
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:"+user.getPhone()+":"+goodsId);
        if(seckillOrder != null){
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        Long stock = valueOperations.decrement("msGoods:"+goodsId);
        if (stock < 0){
            emptyStock.put(goodsId,true);
            valueOperations.increment("msGoods:"+goodsId);
            return RespBean.error(RespBeanEnum.STORK_EMPTY);
        }
        SeckillMessage message = new SeckillMessage(user,goodsId);
        mqSender.sendMS(JsonUtil.object2JsonStr(message));
        return RespBean.success(0);

        /*if(user == null){
            return RespBean.error(RespBeanEnum.USER_NOT_EXSIT);
        }
        GoodsVo goodsVo = goodService.getGoodsVoById(goodsId);
        if (goodsVo.getStockCount() < 1){
            return RespBean.error(RespBeanEnum.STORK_EMPTY);
        }

        //SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id",user.getPhone()).eq("goods_id",goodsId));
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:"+user.getPhone()+":"+goodsId);
        if(seckillOrder != null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        Order order = orderService.seckill(user,goodsVo);
        return RespBean.success(order);*/
    }

    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user,Long goodsId){
        if(user == null){
            return RespBean.error(RespBeanEnum.USER_NOT_EXIST);
        }
        Long orderId = seckillOrderService.getResult(user,goodsId);
        return RespBean.success(orderId);
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goods = goodService.getGoodsVo();
        if(CollectionUtils.isEmpty(goods)){
            return;
        }
        goods.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("msGoods:"+goodsVo.getId(),goodsVo.getStockCount());
            emptyStock.put(goodsVo.getId(),false);
        });
     }

    /*@RequestMapping("/dokill")
    public String doKill(Model model, User user,Long goodsId){
        if(user == null){
            return "login";
        }
        model.addAttribute("user",user);
        GoodsVo goodsVo = goodService.getGoodsVoById(goodsId);
        if (goodsVo.getStockCount() < 1){
            model.addAttribute("msg", RespBeanEnum.STORK_EMPTY);
            return "seckillFail";
        }
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id",user.getPhone()).eq("goods_id",goodsId));
        if(seckillOrder != null){
            model.addAttribute("msg",RespBeanEnum.REPEATE_ERROR);
            return "seckillFail";
        }
        Order order = orderService.seckill(user,goodsVo);
        model.addAttribute("order",order);
        model.addAttribute("goods",goodsVo);
        return "orderDetail";
    }*/
}
