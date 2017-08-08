package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.mongodb.WriteResult;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;
import java.util.List;

/**
 * Created by oem on 4/7/17.
 */
public interface KnowledgeCommentDao
{
    long create(KnowledgeComment knowledgeComment);

    boolean update(long commentId,long ownerId,String comment);

    boolean delete(long commentId,long ownerId);

    boolean cleanComment(long knowledgeId);

    List<KnowledgeComment> getKnowledgeCommentList(long knowledgeId);

    Long getKnowledgeCommentCount(long knowledgeId);
}
