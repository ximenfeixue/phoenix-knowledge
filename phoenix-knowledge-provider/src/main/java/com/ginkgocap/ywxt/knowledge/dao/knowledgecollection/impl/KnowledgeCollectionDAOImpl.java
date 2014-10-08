package com.ginkgocap.ywxt.knowledge.dao.knowledgecollection.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.knowledgecollection.KnowledgeCollectionDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollection;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollectionExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollectionExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCollectionMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCollectionValueMapper;

/**
 * @author caihe
 * 
 */
@Component("knowledgeCollectionDAO")
public class KnowledgeCollectionDAOImpl implements KnowledgeCollectionDAO {

	@Resource
	private KnowledgeCollectionMapper knowledgeCollectionMapper;

	@Resource
	private KnowledgeCollectionValueMapper knowledgeCollectionValueMapper;

	@Override
	public int insertKnowledgeCollection(KnowledgeCollection knowledgeCollection) {
		return knowledgeCollectionMapper.insertSelective(knowledgeCollection);
	}

	@Override
	public int deleteKnowledgeCollection(long[] knowledgeids, long categoryid) {

		return knowledgeCollectionValueMapper.deleteKnowledge(knowledgeids,
				categoryid);
	}

	@Override
	public List<Long> selectKnowledgeCollection(long column_id,
			String knowledgeType, long category_id, int pageno, int pagesize) {

		List<Long> list = new ArrayList<Long>();
		List<Map<String, Object>> maplist = knowledgeCollectionValueMapper
				.selectKnowledgeCollection(column_id, knowledgeType,
						category_id, pageno, pagesize);
		if (maplist != null && maplist.size() > 0) {
			for (int i = 0; i < maplist.size(); i++) {
				Map<String, Object> map = maplist.get(i);
				list.add((Long) map.get("knowledge_id"));
			}
		}
		return list;
	}

	@Override
	public boolean isExsitInCollection(long kid, long categoryid) {
		KnowledgeCollectionExample example = new KnowledgeCollectionExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(kid);
		criteria.andCategoryIdEqualTo(categoryid);

		return knowledgeCollectionMapper.countByExample(example) > 0 ? true
				: false;
	}
}
