package com.amane.seckill.mapper;

import com.amane.seckill.pojo.Goods;
import com.amane.seckill.vo.GoodsVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoById(Long goodsId);

    @Select("select * from t_goods where goods_name = #{name}")
    Goods findGoodByName(String name);
}
