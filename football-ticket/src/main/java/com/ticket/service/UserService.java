package com.ticket.service;

import com.ticket.entity.User;
import com.ticket.mapper.UserMapper;
import com.ticket.util.HttpUtil;
import com.ticket.util.JwtUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Value("${wx.pay.app-id}")
    private String appId;

    @Value("${wx.pay.app-secret:}")
    private String appSecret;

    @Autowired
    private UserMapper userMapper;

    public Map<String, Object> login(String code) {
        // 打印接收到的 code
        System.out.println("收到 code: " + code);

        // 调用微信接口获取openId
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> params = new HashMap<>();
        params.put("appid", appId);
        params.put("secret", appSecret);
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");

        String response = HttpUtil.get(url, params);
        System.out.println("微信接口返回: " + response);  // 打印原始响应

        JSONObject json = JSONObject.parseObject(response);
        String openId = json.getString("openid");
        String sessionKey = json.getString("session_key");
        System.out.println("解析到的 openId: " + openId);

        if (openId == null || openId.isEmpty()) {
            throw new RuntimeException("获取 openId 失败: " + response);
        }

        // 查询或创建用户
        User user = userMapper.selectByOpenId(openId);
        if (user == null) {
            user = new User();
            user.setOpenId(openId);
            user.setCreateTime(new Date());
            user.setRole(0);
            userMapper.insert(user);
        }

        // 生成JWT
        String token = JwtUtil.generateToken(user.getId(), user.getRole());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", user);
        return result;
    }

    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    public void update(User user) {
        user.setUpdateTime(new Date());
        userMapper.updateById(user);
    }

    public Map<String, Object> getStats(Long userId) {
        // 查询订单数、收藏数、优惠券数等
        Map<String, Object> stats = new HashMap<>();
        stats.put("orderCount", userMapper.countOrders(userId));
        stats.put("favoriteCount", userMapper.countFavorites(userId));
        stats.put("couponCount", userMapper.countCoupons(userId));
        return stats;
    }
}