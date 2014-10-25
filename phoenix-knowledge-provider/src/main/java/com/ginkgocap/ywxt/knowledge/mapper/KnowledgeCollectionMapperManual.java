package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollection;
import com.ginkgocap.ywxt.knowledge.entity.UserPermission;

public interface KnowledgeCollectionMapperManual {

    int batchInsertCollection(List<KnowledgeCollection> list);
    
}
