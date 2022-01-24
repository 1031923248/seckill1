package com.amane.seckill.service.impl;

import com.amane.seckill.mapper.GoodsMapper;
import com.amane.seckill.pojo.Goods;
import com.amane.seckill.service.GoodService;
import com.amane.seckill.vo.GoodsVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodService {
    @Autowired
    GoodsMapper goodsMapper;
    @Override
    public List<GoodsVo> getGoodsVo() {
        return goodsMapper.findGoodsVo();
    }

    @Override
    public GoodsVo getGoodsVoById(Long goodsId) {
        return goodsMapper.findGoodsVoById(goodsId);
    }
}
