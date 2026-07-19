package com.ticket.controller;

import com.ticket.entity.Matches;
import com.ticket.service.MatchService;
import com.ticket.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    // 新增赛事
    @PostMapping("/add")
    public Result add(@RequestBody Matches match) {
        matchService.save(match);
        return Result.success("添加成功");
    }

    @GetMapping("/upcoming")
    public Result getUpcoming(@RequestParam(defaultValue = "3") Integer limit) {
        List<Matches> list = matchService.getUpcoming(limit);
        return Result.success(list);
    }

    @GetMapping("/hot")
    public Result getHot(@RequestParam(defaultValue = "4") Integer limit) {
        List<Matches> list = matchService.getHot(limit);
        return Result.success(list);
    }

    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params) {
        // 参数：date, page, size
        Map<String, Object> data = matchService.list(params);
        return Result.success(data);
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Matches match = matchService.getById(id);
        return Result.success(match);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        matchService.delete(id);
        return Result.success("删除成功");
    }

    //管理员后端赛事编辑
    @PutMapping("/update")
    public Result update(@RequestBody Matches match) {
        matchService.update(match);
        return Result.success("更新成功");
    }
}
