package com.amane.seckill.controller;

import com.amane.seckill.pojo.User;
import com.amane.seckill.vo.RespBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/info")
    @ResponseBody
    public RespBean getInfo(User user){
        return RespBean.success(user);
    }
}
