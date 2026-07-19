package com.ticket.service;

import com.ticket.entity.Matches;
import java.util.List;
import java.util.Map;

/**
 * 赛事服务接口
 */
public interface MatchService {

    /**
     * 新增赛事
     */
    void save(Matches match);

    /**
     * 即将开始的赛事
     */
    List<Matches> getUpcoming(Integer limit);

    /**
     * 热门赛事
     */
    List<Matches> getHot(Integer limit);

    /**
     * 分页条件查询
     */
    Map<String, Object> list(Map<String, Object> params);

    /**
     * 根据ID查询
     */
    Matches getById(Long id);

    /**
     * 更新赛事
     */
    void update(Matches match);

    /**
     * 删除赛事
     */
    void delete(Long id);
}