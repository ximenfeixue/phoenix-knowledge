package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;

import java.util.List;

/**
 * Created by Chen Peifeng on 2016/5/24.
 */
public interface KnowledgeCountService {

    public KnowledgeCount updateClickCount(long userId,long knowledgeId,short type);

    public KnowledgeCount updateShareCount(long userId,long knowledgeId,short type);

    public KnowledgeCount updateCollectCount(long userId,long knowledgeId,short type);

    public KnowledgeCount updateCommentCount(long userId,long knowledgeId,short type);

    public KnowledgeCount getKnowledgeCount(long userId, long knowledgeId, short type);

    public KnowledgeCount getKnowledgeCount(long knowledgeId);

    public List<KnowledgeCount> getHotKnowledge(int size);

    public List<KnowledgeCount> getHotKnowledgeByPage(int start,int size);
}
