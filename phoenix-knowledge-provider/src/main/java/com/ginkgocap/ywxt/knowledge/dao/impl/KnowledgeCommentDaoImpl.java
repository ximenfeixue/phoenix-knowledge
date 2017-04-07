package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeCommentDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
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
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by oem on 4/7/17.
 */
@Repository("knowledgeCommentDao")
public class KnowledgeCommentDaoImpl implements KnowledgeCommentDao
{
    private final Logger logger = LoggerFactory.getLogger(KnowledgeCommentDaoImpl.class);
    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private KnowledgeCommonService knowledgeCommonService;

    @Override
    public long create(KnowledgeComment knowledgeComment) {
        if (knowledgeComment != null) {
            knowledgeComment.setId(knowledgeCommonService.getKnowledgeSequenceId());
            if (knowledgeComment.getCreateTime() <= 0) {
                knowledgeComment.setCreateTime(new Date().getTime());
            }
            mongoTemplate.save(knowledgeComment, Constant.Collection.KnowledgeComment);
            return knowledgeComment.getId();
        }
        return -1L;
    }

    @Override
    public boolean update(final long commentId, final long ownerId, final String comment)
    {
        if(commentId <= 0 || ownerId <= 0 || comment == null || comment.trim().length() <= 0) {
            return false;
        }

        Query query = commentIdAndOwnerId(commentId, ownerId);

        Update update = new Update();
        update.set(Constant.Content, comment);
        update.set(Constant.createTime, new Date().getTime());
        KnowledgeComment knowledgeComment = mongoTemplate.findAndModify(query, update, KnowledgeComment.class, Constant.Collection.KnowledgeComment);
        if (knowledgeComment == null) {
            logger.error("Update Knowledge Comment error, no this comment or no permission to update, ownerId:" + ownerId + " knowledgeId: " + commentId);
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(final long commentId, final long ownerId)
    {
        if(commentId <= 0 || ownerId <= 0){
            return false;
        }

        Query query = commentIdAndOwnerId(commentId, ownerId);
        WriteResult result = mongoTemplate.remove(query, KnowledgeComment.class, Constant.Collection.KnowledgeComment);
        if (result.getN() <= 0) {
            logger.error("delete knowledge comment error, ownerId: " + ownerId + " commentId: " + commentId);
            return false;
        }
        return true;
    }

    @Override
    public List<KnowledgeComment> getKnowledgeCommentList(final long knowledgeId)
    {
        if(knowledgeId <= 0){
            return null;
        }

        Criteria c = Criteria.where(Constant.KnowledgeId).is(knowledgeId);
        Query query = new Query(c);
        query.with(new Sort(Sort.Direction.DESC, Constant.createTime));

        return mongoTemplate.find(query, KnowledgeComment.class, Constant.Collection.KnowledgeComment);
    }

    @Override
    public Long getKnowledgeCommentCount(final long knowledgeId)
    {
        if(knowledgeId <= 0){
            return null;
        }

        Criteria c = Criteria.where(Constant.KnowledgeId).is(knowledgeId);
        Query query = new Query(c);

        return mongoTemplate.count(query, KnowledgeComment.class, Constant.Collection.KnowledgeComment);
    }

    private Query commentIdAndOwnerId(final long commentId, final long ownerId)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant._ID).is(commentId));
        query.addCriteria(Criteria.where(Constant.OwnerId).is(ownerId));
        return query;
    }
}
