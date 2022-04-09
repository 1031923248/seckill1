package com.amane.seckill.controller;

import com.amane.seckill.pojo.User;
import com.amane.seckill.service.GoodService;
import com.amane.seckill.service.UserService;
import com.amane.seckill.vo.DetailVo;
import com.amane.seckill.vo.GoodsVo;
import com.amane.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(value = "/goods",produces = "text/html;charset=utf-8")
public class GoodController {
    @Autowired
    UserService userService;
    @Autowired
    GoodService goodService;
    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping( value = "/toDetail/{goodsId}",produces = "application/json")
    @ResponseBody
    public RespBean toDetail(User user, @PathVariable Long goodsId){

        GoodsVo goodsVo = goodService.getGoodsVoById(goodsId);
        Date startTime = goodsVo.getStartDate();
        Date endTime = goodsVo.getEndDate();
        Date nowTime = new Date();
        int remainSeconds = 0;
        int msStatus = 0;
        if(nowTime.after(endTime)){
            remainSeconds = -1;
            msStatus = 2;
        }
        else if (nowTime.before(startTime)){
            remainSeconds = (int) ((startTime.getTime() - nowTime.getTime()) / 1000);
        }
        else {
            msStatus = 1;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSeckillStatus(msStatus);
        detailVo.setRemainSeconds(remainSeconds);
        RespBean respBean = RespBean.success(detailVo);
        System.out.println(respBean);
        return respBean;
    }

    @RequestMapping(value = "/getGoods",produces = "application/json")
    @ResponseBody
    public List<GoodsVo> getGoods(){
        return goodService.getGoodsVo();
    }

    @RequestMapping("/goodList")
    public String toGood(User user){
        if (user == null){
            return "redirect:/login/toLogin";
        }
        return "forward:/goods.html";
    }

    @RequestMapping("/goodDetail/{goodID}")
    public String toGoodDetail(User user, @PathVariable("goodID") Long id){
        if (user == null){
            return "redirect:/login/toLogin";
        }else if (user.getPhone() == 0L){
            return "forward:/fail.html";
        }
        return "forward:/goodsDetail.htm?goodsId="+id;
    }

}
