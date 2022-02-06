package com.amane.seckill.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("t_admin")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminVo {
    private String id;
    private String password;
}
