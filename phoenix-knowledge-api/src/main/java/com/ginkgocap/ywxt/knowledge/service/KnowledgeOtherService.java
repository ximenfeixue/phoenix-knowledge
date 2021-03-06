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
    InterfaceResult collectKnowledge(long userId,long knowledgeId, int typeId, long shareId, short privated);

    InterfaceResult deleteCollectedKnowledge(long userId,long knowledgeId, int typeId);

    boolean updateCollectedKnowledge(KnowledgeCollect collect);

    boolean updateCollectedKnowledgePrivate(long knowledgeId, int typeId, short privated);

    boolean isCollectedKnowledge(long userId,long knowledgeId, int typeId);

    boolean isCollectedKnowledge(long knowledgeId, int typeId);

    List<KnowledgeCollect> myCollectedKnowledgeByPage(long userId, long total, int typeId, int page, int size,String keyword);

    List<KnowledgeCollect> myCollectedKnowledgeByIndex(final long userId, final int typeId, int index, int size, final String keyword);

    long myCollectKnowledgeCount(long userId);

    List<KnowledgeCollect> getAllCollectKnowledge(int page, int size);

    InterfaceResult reportKnowledge(KnowledgeReport report);

}
