package com.ticket.service;

import com.ticket.dto.OrderSubmitDTO;
import com.ticket.entity.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 分页条件查询订单
     * @param params 查询参数（可包含 page, size, status, userId）
     */
    Map<String, Object> list(Map<String, Object> params);

    /**
     * 提交订单
     */
    @Transactional
    Order submitOrder(Long userId, OrderSubmitDTO dto);

    /**
     * 取消订单
     */
    void cancelOrder(String orderNo, Long userId);

    /**
     * 根据订单号查询
     */
    Order getByOrderNo(String orderNo);
}