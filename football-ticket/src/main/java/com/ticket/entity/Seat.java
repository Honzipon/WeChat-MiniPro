package com.ticket.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Seat {
    private Long id;
    private Long venueId;
    private String area;
    private Integer rowNum;
    private Integer seatNum;
    private String position; // JSON格式存储三维坐标 {x,y,z}
    private BigDecimal price;
    private Integer status; // 0可售 1锁定 2已售 3预留
    private Long lockOrderId; // 锁定订单ID
    private Date lockTime;
    private Integer viewScore; // 视角评分
}