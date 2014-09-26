package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledgecollection.KnowledgeCollectionDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollection;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCollectionService;
import com.ginkgocap.ywxt.knowledge.util.Constants;

@Service("knowledgeCollectionService")
public class KnowledgeCollectionServiceImpl implements
		KnowledgeCollectionService {

	@Autowired
	private KnowledgeCollectionDAO knowledgeCollectionDAO;

	@Override
	public Map<String, Object> insertKnowledgeCollection(long kid,
			long columnid, String type, String source, long categoryid) {
		Map<String, Object> result = new HashMap<String, Object>();
		KnowledgeCollection coll = new KnowledgeCollection();
		coll.setKnowledgeId(kid);
		coll.setColumnId(columnid);
		coll.setKnowledgetype(type);
		coll.setSource(source);
		coll.setTimestamp(new Date());
		coll.setCategoryId(categoryid);
		int v = knowledgeCollectionDAO.insertKnowledgeCollection(coll);
		if (v == 0) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.addCollFail.c());
		} else {
			result.put(Constants.status, Constants.ResultType.success.v());
		}
		return result;

	}

	@Override
	public int deleteKnowledgeCollection(long[] knowledgeids, long categoryid) {

		return knowledgeCollectionDAO.deleteKnowledgeCollection(knowledgeids,
				categoryid);
	}

	@Override
	public List<Long> selectKnowledgeCollection(long column_id,
			String knowledgeType, long category_id, int pageno, int pagesize) {

		return knowledgeCollectionDAO.selectKnowledgeCollection(column_id,
				knowledgeType, category_id, pageno, pagesize);
	}

}
