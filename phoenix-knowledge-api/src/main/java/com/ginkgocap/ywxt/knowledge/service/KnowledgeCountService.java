package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;

import java.util.List;

/**
 * Created by Chen Peifeng on 2016/5/24.
 */
public interface KnowledgeCountService {

    public boolean updateClickCount(long userId,long knowledgeId,short type);

    public boolean updateShareCount(long userId,long knowledgeId,short type);

    public boolean updateCollectCount(long userId,long knowledgeId,short type);

    public boolean updateCommentCount(long userId,long knowledgeId,short type);

    public KnowledgeCount getKnowledgeCount(long userId, long knowledgeId, short type);

    public List<KnowledgeCount> getHotKnowledge(int size);

    public List<KnowledgeCount> getHotKnowledgeByPage(int start,int size);
}
