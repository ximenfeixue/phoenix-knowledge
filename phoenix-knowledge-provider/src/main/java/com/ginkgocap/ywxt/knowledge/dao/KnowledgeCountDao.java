package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;

import java.util.List;

/**
 * Created by Chen Peifeng on 2016/5/24.
 */
public interface KnowledgeCountDao {

    public KnowledgeCount getKnowledgeCount(long knowledgeId)throws Exception;

    public void saveKnowledgeCount(KnowledgeCount knowledgeCount) throws Exception;

    public void saveKnowledgeCountList(List<KnowledgeCount> knowledgeCountList)throws Exception;

    public boolean deleteKnowledgeCount(long knowledgeId)throws Exception;

    public List<KnowledgeCount> getHotKnowledge(int size);

    public List<KnowledgeCount> getHotKnowledgeByPage(int start,int size);
}
