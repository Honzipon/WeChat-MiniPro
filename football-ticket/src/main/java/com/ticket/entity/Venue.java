package com.ticket.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Venue {
    private Long id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private Integer totalSeats;
    private String facilities; // 设施，JSON
    private String images; // 图片，JSON
    private String modelUrl; // 3D模型URL
    private Date createTime;
    private Date updateTime;
}
