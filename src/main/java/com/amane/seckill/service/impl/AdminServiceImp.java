package com.amane.seckill.service.impl;

import com.amane.seckill.mapper.AdminMapper;
import com.amane.seckill.pojo.User;
import com.amane.seckill.service.AdminService;
import com.amane.seckill.utils.CookieUtil;
import com.amane.seckill.utils.UUIDUtil;
import com.amane.seckill.utils.VerifyLogin;
import com.amane.seckill.vo.AdminVo;
import com.amane.seckill.vo.RespBean;
import com.amane.seckill.vo.RespBeanEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AdminServiceImp extends ServiceImpl<AdminMapper, AdminVo> implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public RespBean doLogin(AdminVo adminVo, HttpServletRequest request, HttpServletResponse response) {
        String id = adminVo.getId();
        String password = adminVo.getPassword();

        if(StringUtils.isEmpty(password) || StringUtils.isEmpty(id)){
            return RespBean.error(RespBeanEnum.EMPTY_ERROR);
        }
        AdminVo admin = adminMapper.selectById(id);
        if(admin == null){
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        if(!admin.getPassword().equals(password)){
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }

        String ticket = UUIDUtil.uuid();
        redisTemplate.opsForValue().set("user:"+ticket,admin);
        CookieUtil.setCookie(request,response,"adminTicket",ticket);
        return RespBean.success(ticket);
    }
}
