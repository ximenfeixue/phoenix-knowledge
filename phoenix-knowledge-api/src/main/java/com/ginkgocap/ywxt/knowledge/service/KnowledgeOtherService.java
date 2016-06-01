package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollect;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReport;
import com.gintong.frame.util.dto.InterfaceResult;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Chen Peifeng on 2016/4/15.
 */
public interface KnowledgeOtherService
{
    public InterfaceResult collectKnowledge(long userId,long knowledgeId, short columnId) throws Exception;

    public InterfaceResult deleteCollectedKnowledge(long userId,long knowledgeId, short columnId) throws Exception;

    public List<KnowledgeCollect> myCollectKnowledge(long userId,short columnId,int start, int size) throws Exception;

    public InterfaceResult reportKnowledge(KnowledgeReport report) throws Exception;

    public InterfaceResult batchTags(List<LinkedHashMap<String, Object>> tagItems,long userId) throws Exception;

    public InterfaceResult batchCatalogs(List<LinkedHashMap<String, Object>> tagItems,long userId) throws Exception;

    public InterfaceResult getTagListByIds(List<Long> tagIds,long userId) throws Exception;

    public InterfaceResult getDirectoryListByIds(List<Long> directoryIds,long userId) throws Exception;
}
