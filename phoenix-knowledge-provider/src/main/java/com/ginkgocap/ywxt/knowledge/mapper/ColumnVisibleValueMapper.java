package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.ColumnVisible;

public interface ColumnVisibleValueMapper {

    public void update(Map<String, Object> map);
    public void init(List<ColumnVisible> list);
    public void updateChild(Map<String, Object> map);
    public List<Long> selectNotinIds(Map<String, Object> map);
}