package com.amane.seckill.service;

import com.amane.seckill.pojo.SeckillOrder;
import com.amane.seckill.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SeckillOrderService extends IService<SeckillOrder> {
    Long getResult(User user, Long goodsId);
}
