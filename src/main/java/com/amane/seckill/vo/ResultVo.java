package com.amane.seckill.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_order")
public class ResultVo {
    @TableId("user_id")
    private String userId;

    @TableId("goods_name")
    private String goodsName;

    @TableId("create_date")
    private String createDate;
}
