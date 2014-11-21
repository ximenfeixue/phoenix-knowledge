/**
 * 
 */
package com.ginkgocap.ywxt.knowledge.admin.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeAdminDao;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategoryExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategoryExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCategoryMapper;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.user.form.DataGridModel;

/**
 * @author liubang
 *
 */
@Service("knowledgeAdminService")
public class KnowledgeAdminServiceImpl implements KnowledgeAdminService {
	@Resource
	private KnowledgeAdminDao knowledgeAdminDao;
	@Resource
	private KnowledgeCategoryMapper knowledgeCategoryMapper;
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
		if(status==4){//通过
			KnowledgeCategory knowledgecategory = new KnowledgeCategory();
			knowledgecategory.setStatus(Constants.ReportStatus.report.v() + "");
			KnowledgeCategoryExample example = new KnowledgeCategoryExample();
			Criteria criteria = example.createCriteria();
			criteria.andKnowledgeIdEqualTo(id);
			knowledgeCategoryMapper.updateByExampleSelective(
					knowledgecategory, example);
		}else{
			KnowledgeCategory knowledgecategory = new KnowledgeCategory();
			knowledgecategory.setStatus(Constants.ReportStatus.unreport.v() + "");
			KnowledgeCategoryExample example = new KnowledgeCategoryExample();
			Criteria criteria = example.createCriteria();
			criteria.andKnowledgeIdEqualTo(id);
			knowledgeCategoryMapper.updateByExampleSelective(
					knowledgecategory, example);
		}
		
	}

	@Override
	public void update(long id, String title, String cpathid, String content,
			String tags, String collectionName) {
		knowledgeAdminDao.update(id,title, cpathid, content,
				tags, collectionName);
		
	}

}
