package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollect;
import com.gintong.frame.util.dto.InterfaceResult;

import java.util.List;

/**
 * Created by gintong on 2016/8/17.
 */
public interface KnowledgeCollectDao {
    InterfaceResult collectKnowledge(long userId,long knowledgeId, int typeId, short privated);

    InterfaceResult deleteCollectedKnowledge(long ownerId,long knowledgeId,int typeId);

    boolean updateCollectedKnowledge(final KnowledgeCollect collect);

    boolean updateCollectedKnowledgePrivate(long knowledgeId, int typeId, short privated);

    KnowledgeCollect getCollectedKnowledge(long userId,long knowledgeId, int typeId);

    boolean isCollectedKnowledge(long userId,long knowledgeId, int typeId);

    List<KnowledgeCollect> myCollectKnowledge(long userId,int typeId,int page, int size, String keyword);

    List<KnowledgeCollect> getAllCollectKnowledge(final int page, final int size);

    long myCollectKnowledgeCount(long userId);
}
