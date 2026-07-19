package com.ticket.service;

import com.ticket.entity.Seat;
import com.ticket.entity.Venue;
import java.util.List;

/**
 * 场馆服务接口
 */
public interface VenueService {

    /**
     * 查询所有场馆
     */
    List<Venue> list();

    /**
     * 新增场馆
     */
    void save(Venue venue);

    /**
     * 更新场馆
     */
    void update(Venue venue);

    /**
     * 删除场馆
     */
    void delete(Long id);

    /**
     * 根据ID查询场馆
     */
    Venue getById(Long id);

    /**
     * 根据赛事查询座位（含状态）
     */
    List<Seat> getSeatsByMatch(Long venueId, Long matchId);
}