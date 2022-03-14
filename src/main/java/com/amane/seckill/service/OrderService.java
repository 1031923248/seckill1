package com.amane.seckill.service;

import com.amane.seckill.pojo.Order;
import com.amane.seckill.pojo.User;
import com.amane.seckill.vo.GoodsVo;
import com.amane.seckill.vo.OrderDetailVo;
import com.amane.seckill.vo.ResultVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface OrderService extends IService<Order> {
    Order seckill(User user, GoodsVo goods);

    OrderDetailVo detail(Long orderId);

    List<ResultVo> getResult(Long goodsId);

    String createPath(User user, Long goodsId);

    boolean verifyPath(User user, Long goodsId,String path);
}
