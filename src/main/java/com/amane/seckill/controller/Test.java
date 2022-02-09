package com.amane.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/Test")
public class Test {

    @RequestMapping("/hello")
    public String hello(Model model){
        model.addAttribute("age",18);
        return "hello";
    }
    @RequestMapping("/cs")
    @ResponseBody
    public String lll(){
        return null;
    }
}
