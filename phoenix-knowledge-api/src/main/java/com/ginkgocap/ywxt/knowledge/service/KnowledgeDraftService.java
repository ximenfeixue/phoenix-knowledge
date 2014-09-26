package com.ginkgocap.ywxt.knowledge.service;

import java.util.Date;

/**
 * 知识相关的关系表
 * 
 * @author caihe
 * 
 */
public interface KnowledgeDraftService {

	/**
	 * 知识存入草稿箱
	 * 
	 * @author caihe
	 * @return
	 */
	int insertKnowledgeDraft(long id, String title, long userid, String uname,
			long cid, String cname, String pic, String desc, long content,
			int essence, Date createtime, String taskid, String tags);

}
