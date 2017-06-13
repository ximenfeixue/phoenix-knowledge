package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeCommentDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCommentService;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeCommonService;
import com.mongodb.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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
