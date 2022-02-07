package com.amane.seckill.service;

import com.amane.seckill.vo.AdminVo;
import com.amane.seckill.vo.GoodsVo;
import com.amane.seckill.vo.RespBean;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public interface AdminService {
    RespBean doLogin(AdminVo adminVo, HttpServletRequest request, HttpServletResponse response);
    RespBean addGoods(GoodsVo goodsVo);
}
