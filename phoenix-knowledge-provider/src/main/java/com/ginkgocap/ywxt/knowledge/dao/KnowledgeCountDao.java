package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;

import java.util.List;

/**
 * Created by Chen Peifeng on 2016/5/24.
 */
public interface KnowledgeCountDao {
    public boolean updateClickCount(long knowledgeId);

    public boolean updateShareCount(long knowledgeId);

    public boolean updateCollectCount(long knowledgeId);

    public boolean updateCommentCount(long knowledgeId);

    public List<KnowledgeCount> getHotKnowledge(int limit);
}
