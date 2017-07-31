package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;

import java.util.List;
import java.util.Map;

/**
 * Created by Chen Peifeng on 2016/5/24.
 */
public interface KnowledgeCountService {

    public KnowledgeCount updateClickCount(long userId, String userName, String title, long knowledgeId,short type);

    public KnowledgeCount updateShareCount(long knowledgeId,short type);

    public KnowledgeCount updateCollectCount(long knowledgeId,short type);

    public KnowledgeCount updateCommentCount(long knowledgeId,short type);

    public KnowledgeCount getKnowledgeCount(long knowledgeId);

    public KnowledgeCount getKnowledgeCountByIdType(long knowledgeId, short type);

    public Map<Long, Long> getKnowledgeClickCount(List<Long> idList);

    public Map<Long, KnowledgeCount> getKnowledgeCount(List<Long> idList);

    public boolean deleteKnowledgeCount(long knowledgeId);

    public List<KnowledgeCount> getHotKnowledge(int size);

    public List<KnowledgeCount> getHotKnowledgeByPage(int start,int size);
}
