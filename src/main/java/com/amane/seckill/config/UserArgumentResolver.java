package com.amane.seckill.config;

import com.amane.seckill.pojo.User;
import com.amane.seckill.service.UserService;
import com.amane.seckill.utils.CookieUtil;
import com.amane.seckill.utils.UserUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private UserService service;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();
        return parameterType == User.class;
    }



    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        String ticket = CookieUtil.getCookieValue(request,"userTicket");
        if (StringUtils.isEmpty(ticket)){
            return null;
        }
        User user = service.getUserByCookie(ticket,response,request);
        /*
        * 开启用户准入决策系统
        * 注释掉为关闭
        * */

        /*if (!filterUser(user)){
            return new User();
        }*/

        return user;
    }

    public boolean filterUser(User user){
        Integer age = user.getAge();
        Integer jobStatus = user.getJob();
        Integer faith = user.getCredit();
        Integer isYuqi = user.getYuqi();
        if (age < 18 || jobStatus == 0 || faith == 0 || isYuqi == 1){
            return false;
        }
        return true;
    }
}
