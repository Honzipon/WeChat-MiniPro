package com.ticket.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order {
    private Long id;
    private String orderNo;
    private Long userId;
    private Long matchId;
    private String seatIds; // 逗号分隔
    private BigDecimal totalAmount;
    private Integer status; // 0待支付 1已支付 2已完成 3已取消 4退款中 5已退款
    private String contactName;
    private String contactPhone;
    private String note;
    private Date createTime;
    private Date payTime;
    private Date expireTime;
    private Date updateTime;
}
