package com.ticket.controller;

import com.ticket.entity.User;
import com.ticket.mapper.UserMapper;
import com.ticket.util.JwtUtil;
import com.ticket.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        // 1. 查询用户（且必须是管理员 role = 1）
        User user = userMapper.findByUsername(username);
        if (user == null || user.getRole() != 1) {
            return Result.error("用户名或密码错误");
        }

        // 2. 验证密码
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            return Result.error("用户名或密码错误");
        }

        // 3. 生成 JWT token
        String token = JwtUtil.generateToken(user.getId(), user.getRole());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);
        return Result.success(data);
    }
}