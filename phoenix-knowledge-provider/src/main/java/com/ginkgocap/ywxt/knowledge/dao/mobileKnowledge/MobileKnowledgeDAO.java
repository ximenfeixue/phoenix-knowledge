package com.ginkgocap.ywxt.knowledge.dao.mobileKnowledge;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;

/**
 * 移动端需求搜索DAO接口
 * 
 * @author limengyang
 * @创建时间：2014-11-23 14:14
 */
public interface MobileKnowledgeDAO {
	
	/** 移动端需求 - 根据用户id与栏目id查询知识   单一 */
	List<Knowledge> getKnowledge(String[] columnID,long user_id,String type,int offset,int limit);
	
	/**
	 * 查询相应的数量  （金桐或自己）  单一 
	 */
	 long getKnowledgeByUserIdAndColumnID(String[] columnID,long user_id,String type) ;
	 
	 
	 /**
	  * 获取 金桐和自己  混合
	  */
	 List<Knowledge> getMixKnowledge(String columnID,long user_id,String type,int offset,int limit);
	 
	 long getMixKnowledgeCount(String columnID,long user_id,String type);
	 
	 
	 
	 
	 /**
	  * 从MySQL中查询出的knowledge_id和type  填充相应的knowledge 形成List
	  */
	 List<Knowledge>  fileKnowledge(Map<Long,Integer> map);
	 
	 
	 List<Knowledge> fetchFriendKw(long[] user_id,String column,int type,int offset,int limit);
	 
	 long fetchFriendKwCount(long[] user_id,String column,int type) ;
}
