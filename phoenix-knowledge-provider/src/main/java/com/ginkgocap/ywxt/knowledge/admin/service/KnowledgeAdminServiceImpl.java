/**
 * 
 */
package com.ginkgocap.ywxt.knowledge.admin.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeAdminDao;
import com.ginkgocap.ywxt.user.form.DataGridModel;

import net.sf.json.JSONObject;

/**
 * @author liubang
 *
 */
@Service("knowledgeAdminService")
public class KnowledgeAdminServiceImpl implements KnowledgeAdminService {
	@Resource
	private KnowledgeAdminDao knowledgeAdminDao;
	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.service.KnowledgeAdminService#selectKnowledgeNewsList(java.lang.Integer, java.lang.Integer, java.util.Map)
	 */
	@Override
	public Map<String, Object> selectByParam(DataGridModel dgm, Map<String, String> map) {
		return knowledgeAdminDao.selectKnowledgeNewsList(dgm.getPage(), dgm.getRows(), map);
	}

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.service.KnowledgeAdminService#selectKnowledgeNewsById(long, java.lang.String)
	 */
	@Override
	public Object selectById(long id, String collectionName) {
		return knowledgeAdminDao.selectKnowledgeNewsById(id, collectionName);
	}


	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.service.KnowledgeAdminService#checkStatusById(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void checkStatusById(long id, int status, String collectionNames) {
		knowledgeAdminDao.checkStatusById(id, status, collectionNames);
	}

	@Override
	public void update(long id, String title, String cpathid, String content,
			String tags, String collectionName) {
		knowledgeAdminDao.update(id,title, cpathid, content,
				tags, collectionName);
		
	}

}
