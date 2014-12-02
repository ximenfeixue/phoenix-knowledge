package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;

/**
 * @Description: 移动端官方搜索接口
 * @Author: zhangzhen
 * @CreateDate: 2014-10-20
 * @Version: [v1.0]
 */

public interface MobileSearchService {

	/** 关键词搜索 */
	public Map<String, Object> searchByKeywords(Long userid, String keywords,
			String scope, String pno, String pszie);

	/** 根据标签与关键字搜索 */
	public Map<String, Object> selectKnowledgeByTagsAndkeywords(Long userid,
			String keywords, String scope, String tag, int page, int size);

	/** 根据我的收藏与关键字搜索 */
	public Map<String, Object> selectKnowledgeByMyCollectionAndkeywords(
			Long userid, String keywords, String scope, int page, int size);

	/** 分享给我的并按关键字搜索 */
	public Map<String, Object> selectShareMeByKeywords(Long userid,
			String keywords, String scope, int page, int size);

	/** 范围性搜索数据,来源&栏目版的知识数据 */
	public Map<String, Object> selectKnowledgeBySourceAndColumn(Long userid,
			long columnId, String scope, int page, int size);

	/** 我的好友的知识并按关键字过滤分页显示 */
	public Map<String, Object> selectMyFriendKnowledgeByKeywords(
			String friends, long columnId, String scope, int page, int size);

	/**
	 * 根据我的栏目和来源查询知识列表 根据栏目和来源获取知识列表 ( 0-全部;1-金桐脑;2-全平台;4-自己 )
	 * */
	public Map<String, Object> selectknowledgeByColumnIdAndSource(
			long columnId, long source, String scope, int page, int size);

	/**
	 * 查询我的所有好友的指定栏目下，所有知识 根据栏目和来源获取知识列表 ( 3-好友 )
	 * */
	public Map<String, Object> selectMyFriendknowledgeByColumnId(long columnId,
			long userId, String scope, int page, int size);

	/** 整合查询 zz join*/
	public JSONObject searchKnowledge(long userid, String keyword,
			String tag, int scope, int pno, int psize, String qf, int type,
			String sort) throws Exception;
	
	/** 查询全平台的知识 */
	public Map<String, Object> selectPermissionByAllPermission(long userId,long columnId,int start,int size);
	
	/** 查询好友的知识 */
	public Map<String, Object> selectPermissionByMyFriends(long userId,long columnId,int start,int size);
	
	/** 根据栏目ID和用户ID获取知识 */
	public List<Knowledge> getKnowledge(String[] columnID,long user_id,String type,int offset,int limit);
	
	/** 查询全平台与好友的知识 */
	public Map<Long,Integer> selectKnowledgeByPermission(long userId,long columnId,int start,int size);
	
	/** 查询全平台与好友的知识个数 */
	public int selectKnowledgeCountByPermission(long userId,long columnId);
	
	 /**
	  * 获取 金桐和自己  混合
	  */
	 List<Knowledge> getMixKnowledge(String columnID,long user_id,String type,int offset,int limit);
	 
	 long getMixKnowledgeCount(String columnID,long user_id,String type);
	 
	 
	 /**
	  * 从MySQL中查询出的knowledge_id和type  填充相应的knowledge 形成List
	  */
	 List<Knowledge>  fileKnowledge(Map<Long,Integer> map);
	 
	 long getKnowledgeCountByUserIdAndColumnID(String[] columnID,long user_id,String type);
	 
	 /** 查询好友的知识 */
	 public <T> Map<String,Object> selectAllByParam(int classType, int state, String columnid, Long userid, int page, int size);
	 
	 /** 查询首页知识 */
	 public <T> Map<String, Object> selectAllByParam(T t, int state, String columnid, Long userid, int page, int size);
}
