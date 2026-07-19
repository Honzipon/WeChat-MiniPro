package com.ticket.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Matches {
    private Long id;
    private String name;
    private String homeTeam;
    private String awayTeam;
    private String homeLogo;
    private String awayLogo;
    private Date matchTime;
    private Date saleTime; // 开票时间
    private Long venueId;
    private String venueName;
    private String poster;
    private Integer status; // 0未开始 1售票中 2已售罄 3已结束
    private BigDecimal minPrice; // 最低票价
    private BigDecimal maxPrice; // 最高票价
    private Date createTime;
    private Date updateTime;
}