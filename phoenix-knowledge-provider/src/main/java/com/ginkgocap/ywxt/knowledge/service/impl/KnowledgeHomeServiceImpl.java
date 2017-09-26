package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMysqlDao;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeHomeService;


/**
 * Created by gintong on 2016/7/13.
 */
@Service("knowledgeHomeService")
public class KnowledgeHomeServiceImpl implements KnowledgeHomeService
{
    private final static Logger logger = LoggerFactory.getLogger(KnowledgeHomeServiceImpl.class);

    @Autowired
    KnowledgeMongoDao knowledgeMongoDao;

    @Autowired
    KnowledgeMysqlDao knowledgeMysqlDao;

    @Override
    public List<KnowledgeStatics> getRankList(int columnId) {
        return null;
    }

    @Override
    public List<KnowledgeStatics> getRankHotList(int columnId) {
        return null;
    }

    @Override
    public KnowledgeStatics getPl(long id) {
        return null;
    }

    @Override
    public List<Knowledge> selectIndexByParam(short type, int page, int size)
    {
        List<Long> ids = null;
        try {
            ids = knowledgeMysqlDao.getKnowledgeIdsByType(type, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ids != null && ids.size() < 6) {
            ids = null;
        }

        return knowledgeMongoDao.selectIndexByParam(type, page, size, ids);
    }

    @Override
    public int beRelation(long id, int t, long kid, Long userId) {
        return 0;
    }

    @Override
    public Map<String, Object> selectKnowledgeCategoryForImport(long userId, List<Long> groupId, int page, int size) {
        return null;
    }

    @Override
    public int countKnowledgeIds(String tid, String lId, int state, String sortId, long userId, String keyword) {
        return 0;
    }

    @Override
    public Map<String, Object> getAggregationRead(long userId, String[] columnIds, int page, int size) {
        return null;
    }

    @Override
    public Map<String, Object> selectRecommendedKnowledge(long userId, int page, int size) {
        return null;
    }

    @Override
    public Map<String, Object> addUserStar(List<Long> knowledgeIds, long userId, int star) {
        return null;
    }
}
