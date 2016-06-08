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

    //TODO: this just test interface, need to delete before deploy to online system
    public InterfaceResult createTag(long userId,short type,String tagName) throws Exception;

    public InterfaceResult createDirectory(long userId,short type,String tagName) throws Exception;
    //End

    //Common component will remove from knowledge
    public InterfaceResult batchTags(long userId,List<LinkedHashMap<String, Object>> tagItems) throws Exception;

    public InterfaceResult batchCatalogs(long userId,List<LinkedHashMap<String, Object>> tagItems) throws Exception;

    public InterfaceResult getTagListByIds(long userId,List<Long> tagIds) throws Exception;

    public InterfaceResult getTagSourceCountByIds(long userId, List<Long> tagIds) throws Exception;

    public InterfaceResult getDirectoryListByIds(long userId,List<Long> directoryIds) throws Exception;

    public InterfaceResult getDirectorySourceCountByIds(long userId,List<Long> directoryIds) throws Exception;
}
