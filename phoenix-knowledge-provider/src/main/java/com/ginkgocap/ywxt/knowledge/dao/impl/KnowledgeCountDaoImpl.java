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
    private ConcurrentMap<Long, KnowledgeCount> hotCountMap = new ConcurrentHashMap<Long, KnowledgeCount>();

    @Override
    @Transactional
    public boolean updateClickCount(long knowledgeId)
    {
        KnowledgeCount knowledgeCount = getKnowledgeCount(knowledgeId);
        if (knowledgeCount != null) {
            knowledgeCount.setClickCount(knowledgeCount.getClickCount() + 1);
            knowledgeCount.setHotCount(knowledgeCount.getHotCount() + 1);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateShareCount(long knowledgeId) {
        KnowledgeCount knowledgeCount = getKnowledgeCount(knowledgeId);
        if (knowledgeCount != null) {
            knowledgeCount.setShareCount(knowledgeCount.getShareCount() + 1);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateCollectCount(long knowledgeId) {
        KnowledgeCount knowledgeCount = getKnowledgeCount(knowledgeId);
        if (knowledgeCount != null) {
            knowledgeCount.setCollectCount(knowledgeCount.getCollectCount() + 1);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateCommentCount(long knowledgeId) {
        KnowledgeCount knowledgeCount = getKnowledgeCount(knowledgeId);
        if (knowledgeCount != null) {
            knowledgeCount.setCommentCount(knowledgeCount.getCommentCount() + 1);
            return true;
        }
        return false;
    }

    @Override
    public List<KnowledgeCount> getHotKnowledge(int limit) {
        List<KnowledgeCount> knowledgeCounts = null;
        try {
            knowledgeCounts = this.getEntitys("get_knowledge_count_order_desc", limit);
        } catch (BaseServiceException e) {
            e.printStackTrace();
        }
        return knowledgeCounts;
    }

    private KnowledgeCount getKnowledgeCount(long knowledgeId)
    {
        KnowledgeCount knowledgeCount = hotCountMap.get(knowledgeId);
        if (knowledgeCount != null ) {
            return knowledgeCount;
        }

        try {
            knowledgeCount = this.getEntity(knowledgeId);
        } catch (BaseServiceException e) {
            e.printStackTrace();
        }

        if (knowledgeCount == null) {
            knowledgeCount = new KnowledgeCount();
            knowledgeCount.setKnowledgeId(knowledgeId);
            try {
                this.saveEntity(knowledgeCount);
            } catch (BaseServiceException e) {
                e.printStackTrace();
            }
            return knowledgeCount;
        }

        hotCountMap.put(knowledgeId, knowledgeCount);
        return knowledgeCount;
    }

    private void saveKnowledgeCount(KnowledgeCount knowledgeCount)
    {
        try {
            this.updateEntity(knowledgeCount);
        } catch (BaseServiceException e) {
            e.printStackTrace();
        }
    }
}
