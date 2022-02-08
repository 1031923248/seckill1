package com.amane.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public enum RespBeanEnum {
    //通用
    SUCCESS(200, "成功"),
    ERROR(500,"登录失败"),
    //登录失败
    LOGIN_ERROR(501,"用户名或密码不正确！"),
    PHONE_ERROR(502,"手机号格式错误！"),
    PASSWORD_ERROR(503,"密码不得少于6位！"),
    EMPTY_ERROR(504,"账号密码不能为空！"),
    BIND_ERROR(505,"参数校验失败！" ),
    STORK_EMPTY(506,"库存为空"),
    REPEAT_ERROR(507,"每人限购一件商品"),
    USER_NOT_EXIST(508,"用户不存在"),
    ORDER_NOT_EXIST(510,"订单不存在"),
    NOT_EMPTY(520,"注册信息不能为空"),
    IDENTIFY_ERROR(521,"身份证号格式错误"),
    REPEATED_PHONE(522,"该手机号已注册"),
    REGISTER_SUCCESS(200,"注册成功"),
    ILLEGAL_REQUEST(520,"非法请求"),
    REPEAT_GOOD(530,"该商品已经存在"),
    SIZE_OVER(540,"文件必须小于4M"),
    UPLOAD_COMPLETE(200,"上传成功！")
    ;

    private Integer code;
    private String message;
}
