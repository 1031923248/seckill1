package com.amane.seckill.service;

import com.amane.seckill.pojo.Order;
import com.amane.seckill.pojo.User;
import com.amane.seckill.vo.GoodsVo;
import com.amane.seckill.vo.OrderDetailVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderService extends IService<Order> {
    Order seckill(User user, GoodsVo goods);

    OrderDetailVo detail(Long orderId);
}
