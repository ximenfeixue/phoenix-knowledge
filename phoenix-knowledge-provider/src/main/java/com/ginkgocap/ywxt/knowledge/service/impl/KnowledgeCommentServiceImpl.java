package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCommentService;
import com.ginkgocap.ywxt.knowledge.service.common.IKnowledgeCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Chen Peifeng on 2016/4/7.
 */
@Service("knowledgeCommentService")
public class KnowledgeCommentServiceImpl implements KnowledgeCommentService
{
    private Logger logger = LoggerFactory.getLogger(KnowledgeCommentServiceImpl.class);
    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private IKnowledgeCommonService knowledgeCommonService;

    @Override
    public boolean create(KnowledgeComment knowledgeComment) {
        if (knowledgeComment != null) {
            knowledgeComment.setId(knowledgeCommonService.getKnowledgeSeqenceId());
            mongoTemplate.save(knowledgeComment, Constant.Collection.KnowledgeComment);
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Long knowledgeId,Long ownerId,String comment)
    {
        if(knowledgeId == null || comment == null || comment.trim().length() <= 0){
            return false;
        }

        Query query = updateAndDelQuery(knowledgeId, ownerId);

        Update update = new Update();
        update.set(Constant.Content, comment);
        KnowledgeComment knowledgeComment = mongoTemplate.findAndModify(query, update, KnowledgeComment.class, Constant.Collection.KnowledgeComment);
        if (knowledgeComment == null) {
            logger.error("Update Knowledge Comment error, no this comment or no permission to update, ownerId:" + ownerId + " knowledgeId: " + knowledgeId);
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Long knowledgeId,Long ownerId)
    {
        if(knowledgeId == null || ownerId == null){
            return false;
        }

        Query query = updateAndDelQuery(knowledgeId, ownerId);
        KnowledgeComment comment = mongoTemplate.findAndRemove(query, KnowledgeComment.class, Constant.Collection.KnowledgeComment);
        if (comment == null) {
            logger.error("Update Knowledge Comment error, no this comment or no permission to delete: ownerId:"+ ownerId+ " knowledgeId:" + knowledgeId);
            return false;
        }
        return true;
    }

    @Override
    public List<KnowledgeComment> getKnowledgeCommentList(Long knowledgeId)
    {
        if(knowledgeId == null){
            return null;
        }

        Criteria c = Criteria.where(Constant.ID).is(knowledgeId);
        Query query = new Query(c);

        return mongoTemplate.find(query, KnowledgeComment.class, Constant.Collection.KnowledgeComment);
    }

    @Override
    public Long getKnowledgeCommentCount(Long knowledgeId)
    {
        if(knowledgeId == null){
            return null;
        }

        Criteria c = Criteria.where(Constant.KnowledgeId).is(knowledgeId);
        Query query = new Query(c);

        return mongoTemplate.count(query, KnowledgeComment.class, Constant.Collection.KnowledgeComment);
    }

    private Query updateAndDelQuery(Long knowledgeId,Long ownerId)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant.ID).is(knowledgeId));
        query.addCriteria(Criteria.where(Constant.OwnerId).is(ownerId));
        return query;
    }
}
