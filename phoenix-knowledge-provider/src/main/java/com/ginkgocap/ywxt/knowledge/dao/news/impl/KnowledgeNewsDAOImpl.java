package com.ginkgocap.ywxt.knowledge.dao.news.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.news.KnowledgeNewsDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeShare;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.util.PageUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 内容的DAO接口
 * 
 * @author caihe
 * @创建时间：2014-08-27 16:11
 */

@Component("knowledgeNewsDAO")
public class KnowledgeNewsDAOImpl extends SqlMapClientDaoSupport implements
		KnowledgeNewsDAO {

	@Autowired
	SqlMapClient sqlMapClient;

	@Resource
	private MongoTemplate mongoTemplate;

	@PostConstruct
	public void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}

	@Override
	public KnowledgeNews insertknowledge(String title, long userid,
			String uname, long cid, String cname, String cpath, String content,
			String pic, String desc, String essence, String taskid,
			String tags, long knowledgeid, long columnid) {

		KnowledgeNews knowledge = new KnowledgeNews();
		knowledge.setTitle(title);
		knowledge.setUid(userid);
		knowledge.setUname(uname);
		knowledge.setCid(cid);
		knowledge.setCname(cname);
		knowledge.setCpathid(cpath);
		knowledge.setContent(content);
		knowledge.setPic(pic);
		knowledge.setDesc(desc);
		knowledge.setEssence(Integer.parseInt(essence));
		knowledge.setTaskid(taskid);
		knowledge.setTags(tags);
		knowledge.setId(knowledgeid);
		knowledge.setStatus(Constants.Status.checked.v());
		knowledge.setReport_status(Constants.ReportStatus.unreport.v());
		knowledge.setCreatetime(new Date());
		mongoTemplate.save(knowledge, "KnowledgeNews");
		return knowledge;
	}

	@Override
	public void deleteKnowledge(long[] ids) {

		for (int i = 0; i < ids.length; i++) {

			Criteria criteria = Criteria.where("_id").in(ids);
			Query query = new Query(criteria);
			Update update = new Update();
			update.set("status", Constants.Status.recycle.v());
			mongoTemplate.updateFirst(query, update, "KnowledgeNews");
		}
	}

	@Override
	public void updateKnowledge(String title, long userid, String uname,
			long cid, String cname, String cpath, String content, String pic,
			String desc, String essence, String taskid, String tags,
			long knowledgeid) {

		Criteria criteria = Criteria.where("_id").is(knowledgeid);
		Query query = new Query(criteria);
		KnowledgeNews kdnews = mongoTemplate.findOne(query,
				KnowledgeNews.class, "KnowledgeNews");
		if (kdnews != null) {
			Update update = new Update();
			update.set("status", Constants.Status.checked.v());
			update.set("title", title);
			update.set("uid", userid);
			update.set("uname", uname);
			update.set("cpathid", cpath);
			update.set("pic", pic);
			update.set("desc", desc);
			update.set("content", content);
			update.set("essence", essence);
			update.set("modifytime", new Date());
			update.set("taskid", taskid);
			mongoTemplate.updateFirst(query, update, "KnowledgeNews");
		}

	}

	@Override
	public KnowledgeNews selectKnowledge(long knowledgeid) {

		Criteria criteria = Criteria.where("_id").is(knowledgeid);
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, KnowledgeNews.class,
				"KnowledgeNews");
	}

	@Override
	public List<KnowledgeNews> selectByParam(Long columnid, long source,
			Long userid, List<Long> ids, int page, int size) {
		Criteria criteria1 = new Criteria().is(userid);
		Criteria criteria2 = new Criteria();
		if (ids != null) {
			criteria2.and("_id").in(ids);
		}
		Criteria criteriaall = new Criteria();
		criteriaall.orOperator(criteria1, criteria2);
		Query query = new Query(criteriaall);
		query.sort().on("createtime", Order.DESCENDING);
		long count = mongoTemplate.count(query, KnowledgeNews.class);
		PageUtil p = new PageUtil((int) count, page, size);
		query.limit(p.getPageStartRow() - 1);
		query.skip(size);
		return mongoTemplate.find(query, KnowledgeNews.class, "KnowledgeNews");
	}

	@Override
	public void deleteKnowledgeByid(long knowledgeid) {

		Criteria criteria = Criteria.where("_id").is(knowledgeid);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, "KnowledgeNews");
	}
}
