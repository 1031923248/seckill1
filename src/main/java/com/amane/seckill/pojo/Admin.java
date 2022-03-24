package com.amane.seckill.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@TableName("t_admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Admin implements Serializable {

    @TableId("id")
    private String id;

    @TableId("password")
    private String password;
}
