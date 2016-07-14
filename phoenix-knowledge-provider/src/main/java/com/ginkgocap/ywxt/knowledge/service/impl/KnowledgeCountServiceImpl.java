package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeCountDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Chen Peifeng on 2016/5/24.
 */
@Service("knowledgeCountService")
public class KnowledgeCountServiceImpl implements KnowledgeCountService {

     private final int defaultLimit = 10;
    /**知识简表*/
    @Autowired
    private KnowledgeCountDao knowledgeCountDao;

    @Override
    public boolean updateClickCount(long knowledgeId)
    {
        return knowledgeCountDao.updateClickCount(knowledgeId);
    }

    @Override
    public boolean updateShareCount(long knowledgeId)
    {
        return knowledgeCountDao.updateShareCount(knowledgeId);
    }

    @Override
    public boolean updateCollectCount(long knowledgeId)
    {
        return knowledgeCountDao.updateCollectCount(knowledgeId);
    }

    @Override
    public boolean updateCommentCount(long knowledgeId)
    {
        return knowledgeCountDao.updateCommentCount(knowledgeId);
    }

    public List<KnowledgeCount> getHotKnowledge(int limit)
    {
        limit = limit > 0 ? limit : defaultLimit;
        return knowledgeCountDao.getHotKnowledge(limit);
    }
}
