package com.ticket.mapper;

import com.ticket.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    User selectById(Long id);
    User selectByOpenId(String openId);
    User findByUsername(String username);
    void insert(User user);
    void updateById(User user);

    @Select("SELECT COUNT(*) FROM `order` WHERE user_id = #{userId}")
    int countOrders(Long userId);

    @Select("SELECT COUNT(*) FROM favorite WHERE user_id = #{userId}")
    int countFavorites(Long userId);

    @Select("SELECT COUNT(*) FROM coupon WHERE user_id = #{userId} AND status = 0")
    int countCoupons(Long userId);
}