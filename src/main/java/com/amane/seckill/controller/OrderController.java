package com.amane.seckill.controller;

import com.amane.seckill.pojo.User;
import com.amane.seckill.service.OrderService;
import com.amane.seckill.vo.OrderDetailVo;
import com.amane.seckill.vo.RespBean;
import com.amane.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @RequestMapping(value = "/detail",produces = "application/json")
    @ResponseBody
    public RespBean getDetail(User user, Long orderId){
        if (user == null){
            return RespBean.error(RespBeanEnum.USER_NOT_EXIST);
        }
        OrderDetailVo detailVo = orderService.detail(orderId);
        return RespBean.success(detailVo);
    }

}
