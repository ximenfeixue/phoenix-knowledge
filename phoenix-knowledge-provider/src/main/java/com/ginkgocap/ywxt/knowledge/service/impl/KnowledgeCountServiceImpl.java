package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeCountDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCountService;
import com.ginkgocap.ywxt.knowledge.task.KnowledgeCountTask;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chen Peifeng on 2016/5/24.
 */
@Service("knowledgeCountService")
public class KnowledgeCountServiceImpl implements KnowledgeCountService
{
    private final static Logger logger = LoggerFactory.getLogger(KnowledgeCountServiceImpl.class);


    /**知识简表*/
    @Autowired
    private KnowledgeCountDao knowledgeCountDao;

    @Autowired
    private KnowledgeCountTask knowledgeCountTask;

    private final static int maxSize = 10;
    private final static int defaultLimit = 50;

    @Override
    public KnowledgeCount updateClickCount(long userId, String userName, String title, long knowledgeId,short type)
    {
        KnowledgeCount knowledgeCount = getKnowledgeCountByIdType(knowledgeId, type);
        if (knowledgeCount != null) {
            knowledgeCount.setUserId(userId);
            knowledgeCount.setUserName(userName);
            knowledgeCount.setTitle(title);
            knowledgeCount.setClickCount(knowledgeCount.getClickCount() + 1);
            knowledgeCountTask.setToCache(knowledgeCount);
        }
        return knowledgeCount;
    }

    @Override
    public KnowledgeCount updateShareCount(long knowledgeId,short type)
    {
        KnowledgeCount knowledgeCount = getKnowledgeCountByIdType(knowledgeId, type);
        if (knowledgeCount != null) {
            knowledgeCount.setShareCount(knowledgeCount.getShareCount() + 1);
            knowledgeCountTask.setToCache(knowledgeCount);
        }
        return knowledgeCount;
    }

    @Override
    public KnowledgeCount updateCollectCount(long knowledgeId, short type)
    {
        KnowledgeCount knowledgeCount = getKnowledgeCountByIdType(knowledgeId, type);
        if (knowledgeCount != null) {
            knowledgeCount.setCollectCount(knowledgeCount.getCollectCount() + 1);
            knowledgeCountTask.setToCache(knowledgeCount);
        }
        return knowledgeCount;
    }

    @Override
    public KnowledgeCount updateCommentCount(long knowledgeId,short type)
    {
        KnowledgeCount knowledgeCount = getKnowledgeCountByIdType(knowledgeId, type);
        if (knowledgeCount != null) {
            knowledgeCount.setCommentCount(knowledgeCount.getCommentCount() + 1);
            knowledgeCountTask.setToCache(knowledgeCount);
        }
        return knowledgeCount;
    }

    @Override
    public List<KnowledgeCount> getHotKnowledge(int size)
    {
        size = size > 0 ? size : defaultLimit;
        return knowledgeCountDao.getHotKnowledge(size);
    }

    public List<KnowledgeCount> getHotKnowledgeByPage(int start,int size)
    {
        return knowledgeCountDao.getHotKnowledgeByPage(start, size);
    }

    @Override
    public Map<Long, Long> getKnowledgeClickCount(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return null;
        }

        if (idList.size() > maxSize) {
            idList = idList.subList(0, maxSize-1);
            logger.warn("id list size is over maxSize, so set to maxSize");
        }

        Map<Long, Long> map = new HashMap<Long, Long>(idList.size());
        for (Long id : idList) {
            if (id != null) {
                KnowledgeCount knowledgeCount = getKnowledgeCount(id);
                if (knowledgeCount != null) {
                    map.put(id, knowledgeCount.getClickCount());
                } else {
                    logger.warn("get knowlegde count object failed. knowledgeId: " + id);
                }
            }
        }
        return map;
    }

    @Override
    public Map<Long, KnowledgeCount> getKnowledgeCount(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return null;
        }

        if (idList.size() > maxSize) {
            idList = idList.subList(0, maxSize-1);
            logger.warn("id list size is over maxSize, so set to maxSize");
        }

        Map<Long, KnowledgeCount> map = new HashMap<Long, KnowledgeCount>(idList.size());
        for (Long id : idList) {
            if (id != null) {
                KnowledgeCount knowCount = getKnowledgeCount(id);
                if (knowCount != null) {
                    map.put(id, knowCount);
                } else {
                    logger.warn("get knowlegde count object failed. knowledgeId: " + id);
                }
            }
        }
        return map;
    }

    public boolean deleteKnowledgeCount(final long knowledgeId) {
        try {
            if (knowledgeCountTask.deleteFromCache(knowledgeId)) {
                logger.info("delete knowlegde count success from cache. knowledgeId: " + knowledgeId);
            }
            return knowledgeCountDao.deleteKnowledgeCount(knowledgeId);
        } catch (Exception ex) {
            logger.error("delete knowlegde count failed. knowledgeId: " + knowledgeId + " error: " + ex.getMessage());
            return false;
        }
    }

    //Only check exist or not
    @Override
    public KnowledgeCount getKnowledgeCount(long knowledgeId)
    {
        try {
            KnowledgeCount knowledgeCount = knowledgeCountTask.getFromCache(knowledgeId);
            if (knowledgeCount != null ) {
                return knowledgeCount;
            }
            knowledgeCount = knowledgeCountDao.getKnowledgeCount(knowledgeId);
            if (knowledgeCount != null) {
                knowledgeCountTask.setToCache(knowledgeCount);
            }
            return knowledgeCount;
        } catch (Exception e) {
            logger.error("get knowlegde count object failed. knowledgeId: " + knowledgeId);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public KnowledgeCount getKnowledgeCountByIdType(long knowledgeId, short type)
    {
        KnowledgeCount knowledgeCount = this.getKnowledgeCount(knowledgeId);

        if (knowledgeCount == null) {
            knowledgeCount = new KnowledgeCount();
            knowledgeCount.setId(knowledgeId);
            //knowledgeCount.setUserId(userId);
            knowledgeCount.setType(type);
            try {
                knowledgeCountDao.saveKnowledgeCount(knowledgeCount);
                knowledgeCountTask.setToCache(knowledgeCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return knowledgeCount;
    }
}
