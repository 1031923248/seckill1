package com.amane.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Test")
public class Test {

    @RequestMapping("/hello")
    public String hello(Model model){
        model.addAttribute("age",18);
        return "hello";
    }
}
