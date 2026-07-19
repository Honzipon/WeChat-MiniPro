package com.ticket.mapper;

import com.ticket.entity.Venue;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VenueMapper {
    Venue selectById(Long id);

    List<Venue> selectList();

    void insert(Venue venue);

    void updateById(Venue venue);

    void deleteById(Long id);
}