package com.amane.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public enum RespBeanEnum {
    //通用
    SUCCESS(200, "登录成功"),
    ERROR(500,"登录失败"),
    //登录失败
    LOGIN_ERROR(501,"用户名或密码不正确！"),
    PHONE_ERROR(502,"手机号格式错误！"),
    PASSWORD_ERROR(503,"密码不得少于6位！"),
    EMPTY_ERROR(504,"账号密码不能为空！"),
    BIND_ERROR(505,"参数校验失败！" ),
    STORK_EMPTY(506,"库存为空"),
    REPEATE_ERROR(507,"每人限购一件商品"),
    USER_NOT_EXSIT(508,"用户不存在"),
    ORDER_NOT_EXSIT(510,"订单不存在")
    ;

    private Integer code;
    private String message;
}
