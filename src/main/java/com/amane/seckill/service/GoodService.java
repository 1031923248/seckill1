package com.amane.seckill.service;

import com.amane.seckill.pojo.Goods;
import com.amane.seckill.vo.GoodsVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface GoodService extends IService<Goods> {
    List<GoodsVo> getGoodsVo();

    GoodsVo getGoodsVoById(Long goodsId);
}
