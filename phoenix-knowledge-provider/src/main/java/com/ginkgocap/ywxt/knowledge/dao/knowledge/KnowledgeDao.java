package com.ginkgocap.ywxt.knowledge.dao.knowledge;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;

public interface KnowledgeDao {
//	int deleteByPrimaryKey(Long id);
//
//	int insert(Knowledge record);
//
//	int insertSelective(Knowledge record);
//
//	Knowledge selectByPrimaryKey(Long id);
//
//	int updateByPrimaryKeySelective(Knowledge record);
//
//	int updateByPrimaryKey(Knowledge record);

	int checkNameRepeat(int knowledgetype, String knowledgetitle);
}