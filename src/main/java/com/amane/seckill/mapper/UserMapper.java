package com.amane.seckill.mapper;

import com.amane.seckill.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from t_user where phone = #{phone}")
    User getByPhone(String phone);
}
