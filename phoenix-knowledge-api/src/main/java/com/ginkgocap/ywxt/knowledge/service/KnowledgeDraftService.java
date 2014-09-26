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
			long cid, String cname, String pic, String desc, String content,
			int essence,  String taskid, String tags);

}
