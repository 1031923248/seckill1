package com.amane.seckill.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("t_user")
public class User implements Serializable {
    private long phone;
    private String password;
    private String name;
    private String identity;
    private String email;
    private int job;
    private int credit;
    private int age;
    private int yuqi;
    private BigDecimal balance;
}
