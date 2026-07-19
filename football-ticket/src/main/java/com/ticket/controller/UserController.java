package com.ticket.controller;

import com.ticket.entity.User;
import com.ticket.service.UserService;
import com.ticket.util.JwtUtil;
import com.ticket.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> params) {
        String code = params.get("code");
        Map<String, Object> data = userService.login(code);
        return Result.success(data);
    }

    @GetMapping("/info")
    public Result info(HttpServletRequest request) {
        Long userId = JwtUtil.getUserId(request.getHeader("Authorization"));
        User user = userService.getById(userId);
        return Result.success(user);
    }

    @PostMapping("/update")
    public Result update(@RequestBody User user, HttpServletRequest request) {
        Long userId = JwtUtil.getUserId(request.getHeader("Authorization"));
        user.setId(userId);
        userService.update(user);
        return Result.success(null);
    }

    @GetMapping("/stats")
    public Result stats(HttpServletRequest request) {
        Long userId = JwtUtil.getUserId(request.getHeader("Authorization"));
        Map<String, Object> stats = userService.getStats(userId);
        return Result.success(stats);
    }
}
