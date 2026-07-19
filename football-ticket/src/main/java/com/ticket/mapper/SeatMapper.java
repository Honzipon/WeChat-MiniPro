package com.ticket.mapper;

import com.ticket.entity.Seat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SeatMapper {
    Seat selectById(Long id);
    void updateById(Seat seat);
    List<Seat> selectByVenueAndMatch(@Param("venueId") Long venueId, @Param("matchId") Long matchId);
}