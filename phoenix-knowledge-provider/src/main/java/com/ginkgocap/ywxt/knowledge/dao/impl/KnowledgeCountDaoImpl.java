package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.parasol.common.service.exception.BaseServiceException;
import com.ginkgocap.parasol.common.service.impl.BaseService;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeCountDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Admin on 2016/5/24.
 */
@Repository("knowledgeCountDao")
public class KnowledgeCountDaoImpl extends BaseService<KnowledgeCount> implements KnowledgeCountDao
{


    @Override
    public List<KnowledgeCount> getHotKnowledge(int size) {
        List<KnowledgeCount> knowledgeCounts = null;
        try {
            knowledgeCounts = this.getEntitys("get_knowledge_count_order_desc", size);
        } catch (BaseServiceException e) {
            e.printStackTrace();
        }
        return knowledgeCounts;
    }


    @Override
    public KnowledgeCount getKnowledgeCount(long knowledgeId) throws Exception
    {
        return this.getEntity(knowledgeId);
    }

    @Override
    public void saveKnowledgeCount(KnowledgeCount knowledgeCount) throws Exception
    {
        this.saveEntity(knowledgeCount);
    }

    @Override
    public void saveKnowledgeCountList(List<KnowledgeCount> knowledgeCountList) throws Exception
    {
        this.updateEntitys(knowledgeCountList);
    }
}
