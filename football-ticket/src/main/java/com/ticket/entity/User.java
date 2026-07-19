package com.ticket.entity;

import lombok.Data;
import java.util.Date;

@Data
public class User {
    private Long id;
    private String openId;
    private String nickName;
    private String avatarUrl;
    private String phone;
    private String realName;
    private String idCard;
    private Integer realStatus;   // 0-未实名，1-已实名
    private Integer role;         // 0-普通用户，1-管理员
    private String username;        // 登录用户名
    private String passwordHash;    // 密码哈希（新增）
    private Date createTime;
    private Date updateTime;

    // 此处暂时不写 getter/setter，后续会用 Lombok 简化
}
