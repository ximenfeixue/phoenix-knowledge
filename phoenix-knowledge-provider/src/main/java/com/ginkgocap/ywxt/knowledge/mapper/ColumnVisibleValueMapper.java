package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.ColumnVisible;

public interface ColumnVisibleValueMapper {

    public void update(Map<String, Object> map);
    public void updateOneLevelTrue(Map<String, Object> map);
    public void init(List<ColumnVisible> list);
    public void updateChild(Map<String, Object> map);
    public List<Long> selectNotinIds(Map<String, Object> map);
    public List<String> selectSortIds(Map<String, Object> map);
    public List<Column> initvisible(long userId);
    public long initcount(long userId);
    public List getAggregationRead(@Param("userId") Long userId,@Param("columnids")String[] columnIds,@Param("page") int page,@Param("size") int size);
}