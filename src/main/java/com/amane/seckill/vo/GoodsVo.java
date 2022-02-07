package com.amane.seckill.vo;

import com.amane.seckill.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsVo extends Goods {
    private BigDecimal seckillPrice;
    private Integer stockCount;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
}
