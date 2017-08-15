package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeCommentDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Chen Peifeng on 2016/4/7.
 */
@Service("knowledgeCommentService")
public class KnowledgeCommentServiceImpl implements KnowledgeCommentService
{
    @Autowired
    private KnowledgeCommentDao knowledgeCommentDao;

    @Override
    public long create(KnowledgeComment comment) {
        return knowledgeCommentDao.create(comment);
    }

    @Override
    public boolean update(long commentId, long ownerId, String comment)
    {
        return knowledgeCommentDao.update(commentId, ownerId, comment);
    }

    @Override
    public boolean delete(long commentId,long ownerId)
    {
        return knowledgeCommentDao.delete(commentId, ownerId);
    }

    @Override
    public boolean cleanComment(long knowledgeId) {
        return knowledgeCommentDao.cleanComment(knowledgeId);
    }

    @Override
    public List<KnowledgeComment> getKnowledgeCommentList(long knowledgeId)
    {
        return knowledgeCommentDao.getKnowledgeCommentList(knowledgeId);
    }

    @Override
    public long getKnowledgeCommentCount(long knowledgeId)
    {
        return knowledgeCommentDao.getKnowledgeCommentCount(knowledgeId);
    }

}
