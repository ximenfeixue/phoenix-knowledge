package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeIndexDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;

import com.ginkgocap.ywxt.knowledge.service.KnowledgeIndexService;

/**
 * Created by gintong on 2016/7/21.
 */
@Service("knowledgeIndexService")
public class KnowledgeIndexServiceImpl implements KnowledgeIndexService {

    @Autowired
    private KnowledgeIndexDao knowledgeIndexDao;

    @Override
    public long getKnowledgeCountByUserIdAndColumnId(String[] columnId, long userId, short type) {
        return knowledgeIndexDao.getKnowledgeByUserIdAndColumnId(columnId, userId, type);
    }

    @Override
    public List<Knowledge> getKnowledgeDetailList(String[] columnId, long userIdd, short type, int start, int size) {
        return knowledgeIndexDao.getKnowledge(columnId, userIdd, type, start, size);
    }

    @Override
    public List<Knowledge> selectPlatform(short type, int columnId, String columnPath,long userId, int start, int size)
    {
        return knowledgeIndexDao.selectPlatform(type, columnId, columnPath, userId, start, size);
    }

    @Override
    public List<KnowledgeBase> getAllPublicByPage(short type, int columnId, String columnPath, long userId, int start, int size)
    {
        return knowledgeIndexDao.getAllPublicByPage(type, columnId, columnPath, start, size);
    }

    public List<KnowledgeBase> getAllByType(final long userId, final short type, final short status, final String title, final int page, int size)
    {
        return knowledgeIndexDao.getAllByType(userId, type, status, title, page, size);
    }
}
