package com.ticket.mapper;

import com.ticket.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    void insert(Order order);
    void updateById(Order order);
    Order selectById(Long id);
    Order selectByOrderNo(String orderNo);
    List<Order> selectByCondition(Map<String, Object> params);
    int countByCondition(Map<String, Object> params);
}