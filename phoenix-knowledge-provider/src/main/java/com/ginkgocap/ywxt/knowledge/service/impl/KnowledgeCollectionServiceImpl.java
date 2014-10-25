package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledgecollection.KnowledgeCollectionDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollection;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollectionExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollectionExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCollectionMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCollectionValueMapper;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCollectionService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("knowledgeCollectionService")
public class KnowledgeCollectionServiceImpl implements
		KnowledgeCollectionService {

	@Autowired
	private KnowledgeCollectionDAO knowledgeCollectionDAO;
	@Resource
    private KnowledgeCollectionValueMapper knowledgeCollectionValueMapper;


	@Override
	public Map<String, Object> insertKnowledgeCollection(long kid,
			long columnid, String type, String source, long categoryid) {
		Map<String, Object> result = new HashMap<String, Object>();
		int collectSource = 0;//knowledge's six sort of source
		try{
		    collectSource =Integer.parseInt(source);
		}catch(NumberFormatException e){
		    e.printStackTrace();
		}
		
		if (knowledgeCollectionDAO.isExsitInCollection(kid, categoryid)) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.alreadyCollection.c());
			return result;
		}

		KnowledgeCollection coll = new KnowledgeCollection();
		coll.setKnowledgeId(kid);
		coll.setSource(collectSource);
		coll.setCreatetime(new Date());
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

    @SuppressWarnings("rawtypes")
    @Override
    public List selectKnowledgeAll(String source, String knowledgeType, long collectionUserId, int pageno, int pagesize) {
//        return knowledgeCollectionValueMapper.selectKnowledgeAll(source, knowledgeType, collectionUserId, pageno, pagesize);
        return null;
    }

    @Override
    public long countKnowledgeAll(String source, String knowledgeType, long collectionUserId) {
//        return knowledgeCollectionValueMapper.countKnowledgeAll(source, knowledgeType, collectionUserId);
        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> queryKnowledgeAll(String source, String knowledgeType, long collectionUserId,String sortId,String keyword,
            int pageno, int pagesize) {
        Integer start = (pageno - 1) * pagesize;
        Map<String,Object> m =new HashMap<String,Object>();
        long count = knowledgeCollectionValueMapper.countKnowledgeAll(source, knowledgeType, collectionUserId,sortId,keyword);
        List<Map<String,Object>> list = knowledgeCollectionValueMapper.selectKnowledgeAll(source, knowledgeType, collectionUserId, sortId,keyword,start, pagesize);
        PageUtil p=new PageUtil((int) count, pageno, pagesize);
        m.put("page", p);
        m.put("list", list);
        return m;
    }

}
