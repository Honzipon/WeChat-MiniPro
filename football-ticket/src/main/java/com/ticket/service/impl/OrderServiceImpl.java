package com.ticket.service.impl;

import com.ticket.dto.OrderSubmitDTO;
import com.ticket.entity.Order;
import com.ticket.entity.Seat;
import com.ticket.mapper.OrderMapper;
import com.ticket.mapper.SeatMapper;
import com.ticket.service.OrderService;
import com.ticket.util.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SeatMapper seatMapper;

    @Autowired
    private RedisLock redisLock;

    @Override
    public Map<String, Object> list(Map<String, Object> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1").toString());
        int size = Integer.parseInt(params.getOrDefault("size", "10").toString());
        int offset = (page - 1) * size;
        params.put("offset", offset);
        params.put("size", size);

        List<Order> list = orderMapper.selectByCondition(params);
        int total = orderMapper.countByCondition(params);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return result;
    }

    @Transactional
    @Override
    public Order submitOrder(Long userId, OrderSubmitDTO dto) {
        Long matchId = dto.getMatchId();
        List<Long> seatIds = dto.getSeatIds();
        String contactName = dto.getContactName();
        String contactPhone = dto.getContactPhone();
        String note = dto.getNote();

        // 以下为原有业务逻辑（保持不变）
        String orderNo = generateOrderNo();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 锁定座位
        for (Long seatId : seatIds) {
            String lockKey = "seat:lock:" + seatId;
            boolean locked = redisLock.tryLock(lockKey, 3000, 10000);
            if (!locked) {
                throw new RuntimeException("座位已被锁定，请重试");
            }
            try {
                Seat seat = seatMapper.selectById(seatId);
                if (seat.getStatus() != 0) {
                    throw new RuntimeException("座位不可售");
                }
                seat.setStatus(1);
                seat.setLockOrderId(null);
                seat.setLockTime(new Date());
                seatMapper.updateById(seat);
                totalAmount = totalAmount.add(seat.getPrice());
            } finally {
                redisLock.unlock(lockKey);
            }
        }

        // 创建订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setMatchId(matchId);
        order.setSeatIds(String.join(",", seatIds.stream().map(String::valueOf).toArray(String[]::new)));
        order.setTotalAmount(totalAmount);
        order.setStatus(0);
        order.setContactName(contactName);
        order.setContactPhone(contactPhone);
        order.setNote(note);
        order.setCreateTime(new Date());
        order.setExpireTime(new Date(System.currentTimeMillis() + 15 * 60 * 1000));
        orderMapper.insert(order);

        // 更新座位关联订单号
        for (Long seatId : seatIds) {
            Seat seat = seatMapper.selectById(seatId);
            seat.setLockOrderId(order.getId());
            seatMapper.updateById(seat);
        }

        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(String orderNo, Long userId) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != 0) {
            throw new RuntimeException("订单状态不可取消");
        }

        // 释放座位
        String[] seatIdArr = order.getSeatIds().split(",");
        for (String seatIdStr : seatIdArr) {
            Long seatId = Long.parseLong(seatIdStr);
            Seat seat = seatMapper.selectById(seatId);
            if (seat != null && seat.getStatus() == 1 && seat.getLockOrderId() != null && seat.getLockOrderId().equals(order.getId())) {
                seat.setStatus(0);
                seat.setLockOrderId(null);
                seat.setLockTime(null);
                seatMapper.updateById(seat);
            }
        }

        order.setStatus(3);
        order.setUpdateTime(new Date());
        orderMapper.updateById(order);
    }

    @Override
    public Order getByOrderNo(String orderNo) {
        return orderMapper.selectByOrderNo(orderNo);
    }

    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4);
    }
}