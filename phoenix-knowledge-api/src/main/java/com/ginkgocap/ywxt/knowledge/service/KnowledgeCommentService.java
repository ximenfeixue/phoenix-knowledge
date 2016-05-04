package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;

import java.util.List;

/**
 * Created by Chen Peifeng on 2016/4/7.
 */
public interface KnowledgeCommentService {
    /**
     * des:新建知识评论
     * @param knowledgeComment
     * @return 状态（大于0成功，-1失败）
     */
    public long create(KnowledgeComment knowledgeComment);

    /**
     * des: 更新评论
     * @param commentId	用户id
     * @return
     */
    public boolean update(Long commentId,Long ownerId,String comment);

    /**
     * des:根据id删除评论
     * @param commentId
     * @param ownerId
     * @return 操作的记录数(0无记录，>0操作条数)
     */
    public boolean delete(Long commentId,Long ownerId);

    /**
     * des:根据knowledgeId查询知识评论集合
     * @param knowledgeId	知识id
     * @param knowledgeId
     * @return
     */
    public List<KnowledgeComment> getKnowledgeCommentList(Long knowledgeId);

    /**
     * des:根据knowledgeId查询知识评论数量
     * @param knowledgeId
     * @return
     */
    public Long getKnowledgeCommentCount(Long knowledgeId);
}
