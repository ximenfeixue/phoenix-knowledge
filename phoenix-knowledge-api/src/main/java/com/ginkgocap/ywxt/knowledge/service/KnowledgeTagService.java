package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;

//import com.ginkgocap.ywxt.knowledge.entity.KnowledgeTag;

/**
 * 知识标签关系
 * 
 * @author
 * 
 */
public interface KnowledgeTagService {

	/**
	 * 新增知识，把知识ID，栏目ID，存入到知识标签中间表中
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	//int insertKnowledgeTag(KnowledgeTag knowledgeTag);

	
	/** 知识标签更改方法 */
	boolean updateKnowledgeTag(long kid,int type,String tags);
	
    /**
     * 保存标签
     * @param id null:新增 >0：修改
     * @param tag 名称
     * @param userId 用户id
     * @return<pre> {"resultType":0,//0:成功 1：失败
     *  "resultMessage":"名称重复",//返回文本
     *  "obj":{userTag1..}//{@linkplain com.ginkgocap.ywxt.demand.entity.UserTag usertag}</pre>
     */
    public Map<String, Object> saveOrUpdateUserTag(Long id, String tag, long userId,Long knowledgeid,Long webTagId);
    
    /**
     * 查询自己的所有标签
     * @param userId 用户id
     * @param page 当前页
     * @param size 每页大小
     * @return <pre>{"list":{userTag1..{userTag1..},{@linkplain com.ginkgocap.ywxt.demand.entity.UserTag //usertag},
     *  "page":{}}{@linkplain com.ginkgocap.ywxt.util.PageUtil //pageUtil}</pre>
     */
    public Map<String, Object> queryUserTag(long userId, int page, int size);
}
