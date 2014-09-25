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
			long cid, String cname, String source, String s_addr,
			String cpathid, String pic, String desc, long content,
			String hcontent, int essence, Date createtime, int status,
			int report_status, String taskid, String tags);

}
