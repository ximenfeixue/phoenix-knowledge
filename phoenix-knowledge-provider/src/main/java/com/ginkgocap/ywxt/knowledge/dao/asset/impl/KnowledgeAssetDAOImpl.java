package com.ginkgocap.ywxt.knowledge.dao.asset.impl;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.asset.KnowledgeAssetDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeAsset;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 内容的DAO接口
 * 
 * @author caihe
 * @创建时间：2014-08-27 16:11
 */

@Component("knowledgeAssetDAO")
public class KnowledgeAssetDAOImpl extends SqlMapClientDaoSupport implements
		KnowledgeAssetDAO {

	@Autowired
	SqlMapClient sqlMapClient;

	@Resource
	private MongoTemplate mongoTemplate;

	@PostConstruct
	public void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}

	@Override
	public KnowledgeAsset insertknowledge(KnowledgeAsset knowledge) {
		mongoTemplate.save(knowledge);
		return knowledge;
	}

	@Override
	public void deleteKnowledge(long[] ids) {

		for (int i = 0; i < ids.length; i++) {

			Criteria criteria = Criteria.where("_id").is(ids[i]);
			Query query = new Query(criteria);
			Update update = new Update();
			update.set("status", Constants.Status.recycle.v());
			mongoTemplate.updateFirst(query, update, KnowledgeAsset.class);
		}
	}

	@Override
	public void updateKnowledge(KnowledgeAsset knowledge) {

		Criteria criteria = Criteria.where("_id").is(knowledge.getId());
		Query query = new Query(criteria);
		KnowledgeAsset kdnews = mongoTemplate.findOne(query,
				KnowledgeAsset.class);
		if (kdnews != null) {

			Update update = new Update();
			update.set("status", Constants.Status.checked.v());
			update.set("title", knowledge.getTitle());
			update.set("uid", knowledge.getUid());
			update.set("uname", knowledge.getUname());
			update.set("cid", knowledge.getCid());
			update.set("cname", knowledge.getCname());
			update.set("sid", knowledge.getSid());
			update.set("s_addr", knowledge.getS_addr());
			update.set("cpathid", knowledge.getCpathid());
			update.set("pic", knowledge.getPic());
			update.set("desc", knowledge.getDesc());
			update.set("content", knowledge.getContent());
			update.set("hcontent", knowledge.getHcontent());
			update.set("essence", knowledge.getEssence());
			update.set("modifytime", new Date());
			update.set("taskid", knowledge.getTaskid());
			update.set("ish", knowledge.getIsh());
			mongoTemplate.updateFirst(query, update, KnowledgeAsset.class);
		}

	}

	@Override
	public KnowledgeAsset selectKnowledge(long knowledgeid) {

		Criteria criteria = Criteria.where("_id").is(knowledgeid);
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, KnowledgeAsset.class);
	}

}
