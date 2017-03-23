package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollect;
import com.gintong.frame.util.dto.InterfaceResult;

import java.util.List;

/**
 * Created by gintong on 2016/8/17.
 */
public interface KnowledgeCollectDao {
    public InterfaceResult collectKnowledge(long userId,long knowledgeId, int typeId, short privated);

    public InterfaceResult deleteCollectedKnowledge(long ownerId,long knowledgeId,int typeId);

    public boolean updateCollectedKnowledge(KnowledgeCollect collect);

    public KnowledgeCollect getCollectedKnowledge(long userId,long knowledgeId, int typeId);

    public boolean isCollectedKnowledge(long userId,long knowledgeId, int typeId);

    public List<KnowledgeCollect> myCollectKnowledge(long userId,int typeId,int page, int size, String keyword);

    public List<KnowledgeCollect> getAllCollectKnowledge(final int page, final int size);

    public long myCollectKnowledgeCount(long userId);
}
