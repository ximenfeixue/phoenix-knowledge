package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.parasol.common.service.exception.BaseServiceException;
import com.ginkgocap.parasol.common.service.impl.BaseService;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeCountDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Admin on 2016/5/24.
 */
@Repository("knowledgeCountDao")
public class KnowledgeCountDaoImpl extends BaseService<KnowledgeCount> implements KnowledgeCountDao
{

    @Override
    public boolean updateClickCount(long knowledgeId)
    {
        KnowledgeCount knowledgeCount = getKnowledgeCount(knowledgeId);
        if (knowledgeCount != null) {
            knowledgeCount.setClickNum(knowledgeCount.getClickNum()+1);
            knowledgeCount.setHotNum(knowledgeCount.getHotNum()+1);
            this.saveKnowledgeCount(knowledgeCount);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateShareCount(long knowledgeId) {
        KnowledgeCount knowledgeCount = getKnowledgeCount(knowledgeId);
        if (knowledgeCount != null) {
            knowledgeCount.setShareNum(knowledgeCount.getShareNum()+1);
            knowledgeCount.setHotNum(knowledgeCount.getHotNum()+1);
            this.saveKnowledgeCount(knowledgeCount);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCollectCount(long knowledgeId) {
        KnowledgeCount knowledgeCount = getKnowledgeCount(knowledgeId);
        if (knowledgeCount != null) {
            knowledgeCount.setCollectNum(knowledgeCount.getCollectNum()+1);
            knowledgeCount.setHotNum(knowledgeCount.getHotNum()+1);
            this.saveKnowledgeCount(knowledgeCount);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCommentCount(long knowledgeId) {
        KnowledgeCount knowledgeCount = getKnowledgeCount(knowledgeId);
        if (knowledgeCount != null) {
            knowledgeCount.setCommentNum(knowledgeCount.getCommentNum()+1);
            knowledgeCount.setHotNum(knowledgeCount.getHotNum()+1);
            this.saveKnowledgeCount(knowledgeCount);
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
        List<KnowledgeCount> knowledgeCounts = null;
        try {
            knowledgeCounts = this.getEntitys("get_knowledge_count_by_id", new Object[]{knowledgeId});
        } catch (BaseServiceException e) {
            e.printStackTrace();
        }

        if (knowledgeCounts ==null || knowledgeCounts.size() <= 0) {
            KnowledgeCount knowledgeCount = new KnowledgeCount();
            knowledgeCount.setKnowledgeId(knowledgeId);
            try {
                this.saveEntity(knowledgeCount);
            } catch (BaseServiceException e) {
                e.printStackTrace();
            }
            return knowledgeCount;
        }
        return knowledgeCounts.get(0);
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
