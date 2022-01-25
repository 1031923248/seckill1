package com.amane.seckill.service;

import com.amane.seckill.pojo.User;
import com.amane.seckill.vo.LoginVo;
import com.amane.seckill.vo.RegisterVo;
import com.amane.seckill.vo.RespBean;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Service
public interface UserService {
    RespBean doLogin(LoginVo loginVo, HttpServletRequest request,HttpServletResponse response);

    User getUserByCookie(String userTicket,HttpServletResponse response,HttpServletRequest request);

    RespBean doRegister(RegisterVo registerVo,HttpServletResponse response,HttpServletRequest request);
}
