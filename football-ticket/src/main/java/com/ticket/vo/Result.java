package com.ticket.vo;

import lombok.Data;

@Data
public class Result {
    private int code;       // 状态码：0-成功，其他-失败
    private String message; // 提示信息
    private Object data;    // 返回数据

    public static Result success(Object data) {
        Result r = new Result();
        r.setCode(0);
        r.setMessage("success");
        r.setData(data);
        return r;
    }

    public static Result error(String message) {
        Result r = new Result();
        r.setCode(500);
        r.setMessage(message);
        return r;
    }
}