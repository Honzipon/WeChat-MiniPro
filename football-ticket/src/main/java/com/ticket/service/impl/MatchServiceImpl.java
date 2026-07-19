package com.ticket.service.impl;

import com.ticket.entity.Matches;
import com.ticket.mapper.MatchMapper;
import com.ticket.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MatchMapper matchMapper;

    @Override
    public void save(Matches match) {
        match.setCreateTime(new Date());
        match.setUpdateTime(new Date());
        matchMapper.insert(match);
    }

    @Override
    public List<Matches> getUpcoming(Integer limit) {
        return matchMapper.selectUpcoming(limit);
    }

    @Override
    public List<Matches> getHot(Integer limit) {
        return matchMapper.selectHot(limit);
    }

    @Override
    public Map<String, Object> list(Map<String, Object> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1").toString());
        int size = Integer.parseInt(params.getOrDefault("size", "10").toString());
        int offset = (page - 1) * size;
        params.put("offset", offset);
        params.put("size", size);

        List<Matches> list = matchMapper.selectByCondition(params);
        int total = matchMapper.countByCondition(params);

        return Map.of("list", list, "total", total);
    }

    @Override
    public Matches getById(Long id) {
        return matchMapper.selectById(id);
    }

    @Override
    public void update(Matches match) {
        match.setUpdateTime(new Date());
        matchMapper.updateById(match);
    }

    @Override
    public void delete(Long id) {
        matchMapper.deleteById(id);
    }
}