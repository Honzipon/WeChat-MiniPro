package com.ticket.service.impl;

import com.ticket.entity.Seat;
import com.ticket.entity.Venue;
import com.ticket.mapper.SeatMapper;
import com.ticket.mapper.VenueMapper;
import com.ticket.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VenueServiceImpl implements VenueService {

    @Autowired
    private VenueMapper venueMapper;

    @Autowired
    private SeatMapper seatMapper;

    @Override
    public List<Venue> list() {
        // 使用 MyBatis-Plus 的 selectList 方法，如果未引入 MyBatis-Plus，请自行实现
        return venueMapper.selectList();
    }

    @Override
    public void save(Venue venue) {
        venue.setCreateTime(new Date());
        venue.setUpdateTime(new Date());
        venueMapper.insert(venue);
    }

    @Override
    public void update(Venue venue) {
        venue.setUpdateTime(new Date());
        venueMapper.updateById(venue);
    }

    @Override
    public void delete(Long id) {
        venueMapper.deleteById(id);
    }

    @Override
    public Venue getById(Long id) {
        return venueMapper.selectById(id);
    }

    @Override
    public List<Seat> getSeatsByMatch(Long venueId, Long matchId) {
        return seatMapper.selectByVenueAndMatch(venueId, matchId);
    }
}