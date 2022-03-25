package com.amane.seckill.mapper;

import com.amane.seckill.pojo.Order;
import com.amane.seckill.vo.ResultVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    @Select("select * from t_order where goods_Id = #{goodsId} ")
    List<ResultVo> findResultByGoodsID(Long goodsId);
    @Select("select * from t_order where user_id = #{userId}")
    List<ResultVo> findResultByUserId(Long userId);
}
