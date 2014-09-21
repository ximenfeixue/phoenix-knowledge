package com.ginkgocap.ywxt.knowledge.service.knowledge;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;

/**
 * 知识Service
 * 
 * @author zhangwei
 * 
 */
public interface KnowledgeNewsService {

	/**
	 * 新增资讯知识
	 */

	KnowledgeNews insertknowledge(KnowledgeNews knowledge);

	/**
	 * 删除资讯知识
	 */

	void deleteKnowledge(long[] ids);

	/**
	 * 编辑知识
	 */

	void updateKnowledge(KnowledgeNews knowledge);

	/**
	 * 查询知识
	 */

	KnowledgeNews selectKnowledge(long knowledgeid);
	
	/**
	 * 根据条件查询资讯
	 * @param columnid 栏目类型
	 * @param source 1首页 2其他 
	 * @param userid 用户id
	 * @param ids 资讯ids
	 * @return
	 */
	List<KnowledgeNews> selectByParam(Long columnid,long source,Long userid,List<Long> ids,int page,int size);

}
