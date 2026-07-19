package com.ticket.service;

import java.util.Date;
import com.ticket.entity.Order;
import com.ticket.mapper.OrderMapper;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class PayService {

    @Value("${wx.pay.app-id}")
    private String appId;

    @Value("${wx.pay.mch-id}")
    private String mchId;

    @Value("${wx.pay.key}")
    private String apiKey;

    @Value("${wx.pay.notify-url}")
    private String notifyUrl;

    @Autowired
    private OrderMapper orderMapper;

    // 微信支付JSAPI下单
    public Map<String, String> unifiedOrder(Order order) {
        // 使用微信支付Java SDK（简化版，实际使用时需正确配置）
        // 此处模拟返回参数
        Map<String, String> payParams = new HashMap<>();
        payParams.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        payParams.put("nonceStr", "random" + System.currentTimeMillis());
        payParams.put("package", "prepay_id=wx" + System.currentTimeMillis());
        payParams.put("signType", "RSA");
        payParams.put("paySign", "signature"); // 实际需计算签名
        return payParams;
    }

    // 支付回调处理
    public boolean handleNotify(String xmlData) {
        // 解析回调数据，验证签名，更新订单状态
        // 假设解析出订单号 out_trade_no 和交易状态
        String orderNo = "ORD123456"; // 示例
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order != null && order.getStatus() == 0) {
            order.setStatus(1);
            order.setPayTime(new Date());
            orderMapper.updateById(order);
            // 更新座位状态为已售
            // ... 省略
        }
        return true;
    }
}