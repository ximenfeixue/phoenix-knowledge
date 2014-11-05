package com.ginkgocap.ywxt.knowledge.admin.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeNewsAdminDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.util.PageUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 资讯后台的DAO接口
 * 
 * @author fuliwen
 * @创建时间：2014-11-05 16:11
 */

@Component("knowledgeNewsAdminDAO")
public class KnowledgeNewsAdminDAOImpl implements KnowledgeNewsAdminDAO {

	@Autowired
	SqlMapClient sqlMapClient;

	@Resource
	private MongoTemplate mongoTemplate;

	@Override
	public List<KnowledgeNews> findAll() {
		return mongoTemplate.findAll(KnowledgeNews.class);
	}

	@Override
	public List<KnowledgeNews> selectKnowledgeNewsList(Integer start,
			Integer size) {
		Criteria criteria = Criteria.where("cid").is(1);
		Query query = new Query(criteria);
		query.sort().on("createtime", Order.DESCENDING);
		long count = mongoTemplate.count(query, KnowledgeNews.class);
		int page = start/size+1;
		PageUtil p = new PageUtil((int) count, page, size);
		query.limit(p.getPageStartRow() - 1);
		query.skip(size);
		return mongoTemplate.find(query, KnowledgeNews.class, "KnowledgeNews");
	}

	@Override
	public long selectKnowledgeNewsListCount() {
		Criteria criteria = Criteria.where("cid").is(1);
		Query query = new Query(criteria);
		long count = mongoTemplate.count(query, KnowledgeNews.class);
		return count;
	}

	@Override
	public KnowledgeNews selectKnowledgeNewsById(long id) {
		KnowledgeNews news = mongoTemplate.findById(id, KnowledgeNews.class, "KnowledgeNews");
		return news;
	}

	@Override
	public void deleteKnowledgeNewsById(long id) {
		KnowledgeNews news = mongoTemplate.findById(id, KnowledgeNews.class, "KnowledgeNews");
		mongoTemplate.remove(news, "KnowledgeNews");
	}

	@Override
	public void checkStatusById(long id, int status) {
		KnowledgeNews news = mongoTemplate.findById(id, KnowledgeNews.class, "KnowledgeNews");
		news.setStatus(status);
		mongoTemplate.save(news, "KnowledgeNews");
	}
}
