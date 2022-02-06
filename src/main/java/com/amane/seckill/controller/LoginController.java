package com.amane.seckill.controller;

import com.amane.seckill.service.AdminService;
import com.amane.seckill.service.UserService;
import com.amane.seckill.vo.AdminVo;
import com.amane.seckill.vo.LoginVo;
import com.amane.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "redirect:/login.html";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(LoginVo loginVo,HttpServletRequest request,HttpServletResponse response){
        log.info("{}",loginVo);
        return userService.doLogin(loginVo,request,response);
    }

}
