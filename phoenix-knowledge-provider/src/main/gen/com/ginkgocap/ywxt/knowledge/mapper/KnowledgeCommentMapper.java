package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCommentExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCommentQuery;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface KnowledgeCommentMapper {
    int countByExample(KnowledgeCommentExample example);

    int deleteByExample(KnowledgeCommentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(KnowledgeComment record);

    int insertSelective(KnowledgeComment record);

    List<KnowledgeComment> selectByExample(KnowledgeCommentExample example);

    KnowledgeComment selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") KnowledgeComment record, @Param("example") KnowledgeCommentExample example);

    int updateByExample(@Param("record") KnowledgeComment record, @Param("example") KnowledgeCommentExample example);

    int updateByPrimaryKeySelective(KnowledgeComment record);

    int updateByPrimaryKey(KnowledgeComment record);

    List<KnowledgeCommentQuery> getCommentByPage(Map<String, String> param);
    /**
	 * 查询评论数量
	 * @param param
	 * @return
	 */
	public int getCommentCount(Map<String, String> param);
	/**
	 * 删除评论 设置标志位
	 * @param idList
	 */
	public void deleteComment(List<Long> idList, Long userId, String userName);
	/**
	 * 彻底删除评论 物理删除
	 * @param idList
	 */
	public void deleteCommentCompletely(List<Long> idList);
	/**
	 * 恢复已删除的评论
	 * @param idList
	 */
	public void recoverDeletedComment(List<Long> idList);
	/**
	 * 屏蔽评论内容
	 * @param idList
	 */
	public void invisibleComment(List<Long> idList);
	/**
	 * 恢复屏蔽内容
	 * @param idList
	 */
	public void recoverInvisibleComment(List<Long> idList);
}