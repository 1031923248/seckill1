package com.amane.seckill.controller;

import com.amane.seckill.service.UserService;
import com.amane.seckill.vo.RegisterVo;
import com.amane.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private UserService userService;
    @RequestMapping("/doRegister")
    @ResponseBody
    public RespBean register(RegisterVo registerVo, HttpServletRequest request, HttpServletResponse response){
        return userService.doRegister(registerVo,response,request);
    }
}
