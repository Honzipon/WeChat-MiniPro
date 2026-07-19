package com.ticket.controller;

import com.ticket.dto.OrderSubmitDTO;
import com.ticket.entity.Order;
import com.ticket.service.OrderService;
import com.ticket.util.JwtUtil;
import com.ticket.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public Result submit(@RequestBody OrderSubmitDTO dto, HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null) {
            return Result.error("未授权，请重新登录");
        }
        Long userId;
        try {
            userId = JwtUtil.getUserId(token);
        } catch (Exception e) {
            return Result.error("token 无效，请重新登录");
        }
        Order order = orderService.submitOrder(userId, dto);
        return Result.success(Collections.singletonMap("orderNo", order.getOrderNo()));
    }

    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null) {
            return Result.error("未授权，请重新登录");
        }
        Long userId;
        Integer role;
        try {
            userId = JwtUtil.getUserId(token);
            role = JwtUtil.getUserRole(token);
        } catch (Exception e) {
            return Result.error("token 无效或已过期，请重新登录");
        }

        // 处理空字符串参数
        if (params.containsKey("status") && "".equals(params.get("status"))) {
            params.remove("status");
        }

        // 分页参数处理
        int page = Integer.parseInt(params.getOrDefault("page", "1").toString());
        int size = Integer.parseInt(params.getOrDefault("size", "10").toString());
        params.put("offset", (page - 1) * size);
        params.put("size", size);

        // 普通用户只能查自己的订单，管理员可查所有（不添加 userId 条件）
        if (role == null || role != 1) {
            params.put("userId", userId);
        }

        Map<String, Object> data = orderService.list(params);
        return Result.success(data);
    }

    // 提取 token 辅助方法
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);
        return token.isEmpty() ? null : token;
    }

    // 其他方法（如 detail、cancel 等）保持不变
}