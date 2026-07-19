package com.ticket.controller;

import com.ticket.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/banner")
public class BannerController {
    @GetMapping("/list")
    public Result list() {
        List<Map<String, String>> banners = new ArrayList<>();
        // 模拟三条轮播图数据，图片可使用网络图片或本地图片（需在小程序端存在）
        Map<String, String> item1 = new HashMap<>();
        item1.put("image", "/images/banner1.jpg");  // 路径需对应小程序内的图片
        item1.put("url", "");
        banners.add(item1);
        Map<String, String> item2 = new HashMap<>();
        item2.put("image", "/images/banner2.jpg");
        item2.put("url", "");
        banners.add(item2);
        Map<String, String> item3 = new HashMap<>();
        item3.put("image", "/images/banner3.jpg");
        item3.put("url", "");
        banners.add(item3);
        return Result.success(banners);
    }
}