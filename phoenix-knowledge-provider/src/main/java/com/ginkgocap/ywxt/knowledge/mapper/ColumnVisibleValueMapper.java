package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ginkgocap.ywxt.knowledge.entity.Column;

public interface ColumnVisibleValueMapper {

    public void update(Map<String, Object> map);
    public void updateChild(Map<String, Object> map);
    public List<Long> selectNotinIds(Map<String, Object> map);
}