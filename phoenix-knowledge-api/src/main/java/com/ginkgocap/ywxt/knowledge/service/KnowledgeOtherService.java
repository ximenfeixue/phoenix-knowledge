package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollect;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReport;
import com.gintong.frame.util.dto.InterfaceResult;

import java.util.List;

/**
 * Created by Chen Peifeng on 2016/4/15.
 */
public interface KnowledgeOtherService
{
    public InterfaceResult collectKnowledge(long userId,long knowledgeId, int typeId) throws Exception;

    public InterfaceResult deleteCollectedKnowledge(long userId,long knowledgeId, int typeId) throws Exception;

    public boolean isCollectedKnowledge(long userId,long knowledgeId, int typeId);

    public List<KnowledgeCollect> myCollectKnowledge(long userId, int typeId,int start, int size,String keyword) throws Exception;

    public long myCollectKnowledgeCount(long userId) throws Exception;

    public InterfaceResult reportKnowledge(KnowledgeReport report) throws Exception;

}
