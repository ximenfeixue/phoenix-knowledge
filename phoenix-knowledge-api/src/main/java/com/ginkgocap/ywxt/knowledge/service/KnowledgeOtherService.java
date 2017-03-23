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
    public InterfaceResult collectKnowledge(long userId,long knowledgeId, int typeId, short privated);

    public InterfaceResult deleteCollectedKnowledge(long userId,long knowledgeId, int typeId);

    public boolean updateCollectedKnowledge(final KnowledgeCollect collect);

    public boolean isCollectedKnowledge(long userId,long knowledgeId, int typeId);

    public List<KnowledgeCollect> myCollectKnowledge(long userId, int typeId, int page, int size,String keyword);

    public long myCollectKnowledgeCount(long userId);

    public List<KnowledgeCollect> getAllCollectKnowledge(final int page, final int size);

    public InterfaceResult reportKnowledge(KnowledgeReport report);

}
