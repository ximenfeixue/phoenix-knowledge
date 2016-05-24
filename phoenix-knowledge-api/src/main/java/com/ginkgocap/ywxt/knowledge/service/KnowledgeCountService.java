package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;

import java.util.List;

/**
 * Created by Admin on 2016/5/24.
 */
public interface KnowledgeCountService {

    public boolean updateClickCount(long knowledgeId);

    public boolean updateShareCount(long knowledgeId);

    public boolean updateCollectCount(long knowledgeId);

    public boolean updateCommentCount(long knowledgeId);

    public List<KnowledgeCount> getHotKnowledge(int limit);
}
