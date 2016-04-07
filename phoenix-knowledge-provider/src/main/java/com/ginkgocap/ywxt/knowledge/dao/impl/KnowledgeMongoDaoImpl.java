package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.model.ColumnSys;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.mongodb.WriteResult;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Repository("knowledgeMongoDao")
public class KnowledgeMongoDaoImpl implements KnowledgeMongoDao {
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	@Override
	public KnowledgeDetail insert(KnowledgeDetail knowledgeDetail, String... collectionName) throws Exception {
		
		if(knowledgeDetail == null || knowledgeDetail.getId() <= 0)
			return null;
		
		long currentDate = new Date().getTime();
        knowledgeDetail.setCreateTime(currentDate);
		
		String currCollectionName = getCollectionName(knowledgeDetail.getColumnId(),collectionName);
		
		mongoTemplate.insert(knowledgeDetail,currCollectionName);
		
		return this.getByIdAndColumnId(knowledgeDetail.getId(),knowledgeDetail.getColumnId(),currCollectionName);
	}
	
	@Override
	public List<KnowledgeDetail> insertList(List<KnowledgeDetail> knowledgeDetailList,  String... collectionName) throws Exception {
		
		List<KnowledgeDetail> returnList = new ArrayList();
		
		if(knowledgeDetailList != null && !knowledgeDetailList.isEmpty()) {
			Iterator<KnowledgeDetail> it = knowledgeDetailList.iterator();
			while(it.hasNext()) {
				//returnList.add(this.insert(it.next(), user, collectionName));
			}
		}
		
		return returnList;
	}

	@Override
	public KnowledgeDetail update(KnowledgeDetail knowledgeDetail, String... collectionName)
			throws Exception {

		if(knowledgeDetail == null)
			return null;
		
		long knowledgeId = knowledgeDetail.getId();
		
		if(knowledgeId <= 0) {
			return this.insert(knowledgeDetail);
		}

        long currentDate = new Date().getTime();
        knowledgeDetail.setModifyTime(currentDate);
		
		Criteria criteria = Criteria.where("_id").is(knowledgeId);
		Query query = new Query(criteria);
		
		String currCollectionName = getCollectionName(knowledgeDetail.getColumnId(),collectionName);
		
		WriteResult result = mongoTemplate.updateFirst(query, getUpdate(knowledgeDetail), currCollectionName);
		
		return this.getByIdAndColumnId(knowledgeId,knowledgeDetail.getColumnId(),currCollectionName);
	}


	@Override
	public int deleteByIdAndColumnId(long id,short columnId, String...collectionName ) throws Exception {
		
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = new Query(criteria);
		
		mongoTemplate.remove(query, getCollectionName(columnId,collectionName));
		
		return 0;
	}

	@Override
	public int deleteByIdsAndColumnId(List<Long> ids,short columnId,String... collectionName) throws Exception {
		
		Criteria criteria = Criteria.where("_id").in(ids);
		Query query = new Query(criteria);
		
		mongoTemplate.remove(query, getCollectionName(columnId,collectionName));
		
		return 0;
	}

	@Override
	public int deleteByCreateUserIdAndColumnId(long createUserId,short columnId,String... collectionName) throws Exception {
		
		Criteria criteria = Criteria.where("createUserId").is(createUserId);
		Query query = new Query(criteria);
		
		mongoTemplate.remove(query, getCollectionName(columnId,collectionName));
		
		return 0;
	}

	@Override
	public KnowledgeDetail getByIdAndColumnId(long id,short columnId,String... collectionName) throws Exception {
		
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = new Query(criteria);
		
		return mongoTemplate.findOne(query,KnowledgeDetail.class, getCollectionName(columnId,collectionName));
		
	}

    @Override
    public KnowledgeDetail insertAfterDelete(KnowledgeDetail knowledgeDetail,
                                             long knowledgeId, String... collectionName) throws Exception {

        String currCollectionName = getCollectionName(knowledgeDetail.getColumnId(),collectionName);

        if(knowledgeDetail == null || knowledgeId <= 0)
            return null;

        KnowledgeDetail oldValue = this.getByIdAndColumnId(knowledgeId,knowledgeDetail.getColumnId(),currCollectionName);

        this.deleteByIdAndColumnId(knowledgeId,knowledgeDetail.getColumnId(),currCollectionName);

        try {

            this.insert(knowledgeDetail, currCollectionName);

        } catch (Exception e) {

            if(oldValue != null)
                this.insert(oldValue, currCollectionName);

            throw e;
        }


        return this.getByIdAndColumnId(knowledgeId,knowledgeDetail.getColumnId());
    }

    @Override
	public List<KnowledgeDetail> getByIdsAndColumnId(List<Long> ids,short columnId,String... collectionName) throws Exception {
		
		Criteria criteria = Criteria.where("_id").in(ids);
		Query query = new Query(criteria);
		
		return mongoTemplate.find(query,KnowledgeDetail.class, getCollectionName(columnId,collectionName));
		
	}
	
	private String getCollectionName(long columnId) throws Exception {
		
		StringBuffer strBuf = new StringBuffer();
		
		strBuf.append(KNOWLEDGE_COLLECTION_NAME);
		
		//从缓存中获取系统栏目
		List<ColumnSys> columnSysList = new ArrayList<ColumnSys>();
		
		Iterator<ColumnSys> it = columnSysList.iterator();
		
		boolean columnCodeNotExistflag = true;
		
		while(it.hasNext()) {
			ColumnSys columnSys = it.next();
			if(columnId == columnSys.getId()) {
				if(StringUtils.isEmpty(columnSys.getColumnCode())) {
					break;
				}
				columnCodeNotExistflag = false;
				strBuf.append(columnSys.getColumnCode());
				break;
			}
		}
		
		if(columnCodeNotExistflag) {
			strBuf.append(KNOWLEDGE_COLLECTION_USERSELF_NAME);
		}
		
		return strBuf.toString();
		
	}
	
	private String getCollectionName(long columnId,String[] collectionName) throws Exception {
		return "Knowledge"; //ArrayUtils.isEmpty(collectionName) && StringUtils.isEmpty(collectionName[0]) ? getCollectionName(columnId) : collectionName[0];
	}
	
	private Update getUpdate(KnowledgeDetail knowledgeDetail) {
		
		//构建更新字段，目前默认是全字段更新
		Update update = new Update();
		
		JSONObject json = JSONObject.fromObject(knowledgeDetail);
		
		Iterator<String> it = json.keys();
		
		while(it.hasNext()) {
			String key = it.next();
			update.update(key, json.get(key));
		}
		
		return update;
		
	}
}