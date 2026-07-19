package com.ticket.controller;

import com.ticket.entity.Seat;
import com.ticket.entity.Venue;
import com.ticket.service.VenueService;
import com.ticket.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venue")
public class VenueController {

    @Autowired
    private VenueService venueService;

    @GetMapping("/{id}")
    public Result getVenue(@PathVariable Long id) {
        return Result.success(venueService.getById(id));
    }

    @GetMapping("/{id}/seats")
    public Result getSeats(@PathVariable Long id, @RequestParam Long matchId) {
        List<Seat> seats = venueService.getSeatsByMatch(id, matchId);
        return Result.success(seats);
    }

    // 查询所有场馆
    @GetMapping("/list")
    public Result list() {
        return Result.success(venueService.list());
    }

    // 添加场馆
    @PostMapping("/add")
    public Result add(@RequestBody Venue venue) {
        venueService.save(venue);
        return Result.success("添加成功");
    }

    @PutMapping("/update")
    public Result update(@RequestBody Venue venue) {
        venueService.update(venue);       // 改为 update
        return Result.success("更新成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        venueService.delete(id);          // 改为 delete
        return Result.success("删除成功");
    }
}
