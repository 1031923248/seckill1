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
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

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
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;
    @RequestMapping("/goodList")
    @ResponseBody
    public String toGood(Model model, User user, HttpServletResponse response, HttpServletRequest request){
//        if (user == null){
//            return "login";
//        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goods");
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("goodsList",goodService.getGoodsVo());
        model.addAttribute( "user",user);
        WebContext webContext = new WebContext(request,response,request.getServletContext(),request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods",webContext);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("goods",html,60, TimeUnit.SECONDS);
        }
        return html;
    }

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
    /*@RequestMapping(value = "/toDetail/{goodsId}", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2(Model model, User user, @PathVariable Long goodsId,HttpServletRequest request,HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail:"+goodsId);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
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
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("user",user);
        model.addAttribute("goods",goodsVo);
        model.addAttribute("msStatus",msStatus);
        WebContext webContext = new WebContext(request,response,request.getServletContext(),request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail",webContext);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsDetail:"+goodsId,html,60,TimeUnit.SECONDS);
        }
        return html;
    }*/

//    @RequestMapping("/goodList")
//    public String toGood(HttpServletRequest request, HttpServletResponse response, Model model, @CookieValue("userTicket") String ticket){
//        if(StringUtils.isEmpty(ticket)){
//            return "login";
//        }
//        //User user = (User) session.getAttribute(ticket);
//        User user = userService.getUserByCookie(ticket,response,request);
//        if(null == user){
//            return "login";
//        }
//        model.addAttribute("user",user);
//        return "goods";
//    }
}
