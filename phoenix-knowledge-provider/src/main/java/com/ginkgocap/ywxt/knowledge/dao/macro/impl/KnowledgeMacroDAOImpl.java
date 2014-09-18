package com.ginkgocap.ywxt.knowledge.dao.macro.impl;

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

import com.ginkgocap.ywxt.knowledge.dao.macro.KnowledgeMacroDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeMacro;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 内容的DAO接口
 * 
 * @author caihe
 * @创建时间：2014-08-27 16:11
 */

@Component("knowledgeMacroDAO")
public class KnowledgeMacroDAOImpl extends SqlMapClientDaoSupport implements
		KnowledgeMacroDAO {

	@Autowired
	SqlMapClient sqlMapClient;

	@Resource
	private MongoTemplate mongoTemplate;

	@PostConstruct
	public void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}

	@Override
	public KnowledgeMacro insertknowledge(KnowledgeMacro knowledge) {
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
			mongoTemplate.updateFirst(query, update, KnowledgeMacro.class);
		}
	}

	@Override
	public void updateKnowledge(KnowledgeMacro knowledge) {

		Criteria criteria = Criteria.where("_id").is(knowledge.getId());
		Query query = new Query(criteria);
		KnowledgeMacro kdnews = mongoTemplate.findOne(query,
				KnowledgeMacro.class);
		if (kdnews != null) {

			Update update = new Update();
			update.set("status", Constants.Status.checked.v());
			update.set("title", knowledge.getTitle());
			update.set("uid", knowledge.getUid());
			update.set("uname", knowledge.getUname());
			update.set("cid", knowledge.getCid());
			update.set("cname", knowledge.getCname());
			update.set("source", knowledge.getSource());
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
			mongoTemplate.updateFirst(query, update, KnowledgeMacro.class);
		}

	}

	@Override
	public KnowledgeMacro selectKnowledge(long knowledgeid) {

		Criteria criteria = Criteria.where("_id").is(knowledgeid);
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, KnowledgeMacro.class);
	}

}
