package com.ticket.controller;

import com.ticket.entity.Order;
import com.ticket.service.OrderService;
import com.ticket.service.PayService;
import com.ticket.util.JwtUtil;
import com.ticket.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class PayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;



    @PostMapping("/pay")
    public Result pay(@RequestBody Map<String, String> params, HttpServletRequest request) {
        String orderNo = params.get("orderNo");
        Long userId = JwtUtil.getUserId(request.getHeader("Authorization"));

        Order order = orderService.getByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(userId)) {
            return Result.error("订单不存在");
        }
        if (order.getStatus() != 0) {
            return Result.error("订单状态不正确");
        }

        // 调用微信支付统一下单
        Map<String, String> payParams = payService.unifiedOrder(order);
        return Result.success(payParams);
    }

    @PostMapping("/pay/notify")
    public String payNotify(@RequestBody String xmlData) {
        // 微信支付回调通知处理
        boolean success = payService.handleNotify(xmlData);
        if (success) {
            return "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>";
        } else {
            return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        }
    }
}
