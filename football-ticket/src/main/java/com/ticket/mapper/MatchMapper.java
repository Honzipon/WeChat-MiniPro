package com.ticket.mapper;

import com.ticket.entity.Matches;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MatchMapper {
    Matches selectById(Long id);
    List<Matches> selectUpcoming(@Param("limit") Integer limit);
    List<Matches> selectHot(@Param("limit") Integer limit);
    List<Matches> selectByCondition(Map<String, Object> params);
    int countByCondition(Map<String, Object> params);

    void insert(Matches match);

    void updateById(Matches match);

    void deleteById(Long id);
}