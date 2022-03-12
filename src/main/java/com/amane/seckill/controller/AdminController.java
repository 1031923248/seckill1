package com.amane.seckill.controller;

import com.amane.seckill.service.AdminService;
import com.amane.seckill.utils.CookieUtil;
import com.amane.seckill.vo.AdminVo;
import com.amane.seckill.vo.GoodsVo;
import com.amane.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.jws.Oneway;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/admin")
@Controller
@Slf4j
public class AdminController {
    @Autowired
    private AdminService adminService;
    @RequestMapping(value = "/addGoods",produces = "text/html;charset=utf-8")
    public String toAdd(HttpServletResponse response,HttpServletRequest request){
        String ticket = CookieUtil.getCookieValue(request,"adminTicket");
        if (StringUtils.isEmpty(ticket)){
            return "redirect:/login.html";
        }
        return "redirect:/addGoods.html";
    }
    @RequestMapping(value = "/deleteGoods")
    public String toDelete(){
        return "redirect:/goodsManager.html";
    }
    @RequestMapping("/adminLogin")
    @ResponseBody
    public RespBean adminLogin(AdminVo adminVo, HttpServletRequest request, HttpServletResponse response){
        log.info("{}",adminVo);
        return adminService.doLogin(adminVo,request,response);
    }
    @RequestMapping("/doAddGoods")
    @ResponseBody
    public RespBean doAddGoods(GoodsVo goodsVo){
        log.info("{}",goodsVo);
        return adminService.addGoods(goodsVo);
    }
    @RequestMapping("/deleteGood/{id}")
    @ResponseBody
    public RespBean doDeleteGood(@PathVariable("id") Long id){
        return adminService.deleteGood(id);
    }
}
