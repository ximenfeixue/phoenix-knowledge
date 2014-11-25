package com.ginkgocap.ywxt.knowledge.dao.news.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.news.KnowledgeNewsDAO;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeInvestment;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.HtmlToText;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.util.PageUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 内容的DAO接口
 * 
 * @author caihe
 * @创建时间：2014-08-27 16:11
 */

@Component("knowledgeNewsDAO")
public class KnowledgeNewsDAOImpl implements KnowledgeNewsDAO {

	@Autowired
	SqlMapClient sqlMapClient;

	@Resource
	private MongoTemplate mongoTemplate;

	@Override
	public Knowledge insertknowledge(KnowledgeNewsVO vo, User user) {
		String obj = Constants.getTableName(vo.getColumnType());

		Knowledge knowledge = null;
		try {
			knowledge = (Knowledge) Class.forName(obj).newInstance();
			Knowledge currK = knowledge.setValue(vo, user);
			mongoTemplate.save(currK,
					obj.substring(obj.lastIndexOf(".") + 1, obj.length()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return knowledge;
	}

	@Override
	public Knowledge insertknowledgeDraft(KnowledgeNewsVO vo, User user) {
		String obj = Constants.getTableName(vo.getColumnType());

		Knowledge knowledge = null;
		try {
			knowledge = (Knowledge) Class.forName(obj).newInstance();
			Knowledge currK = knowledge.setDraftValue(vo, user);
			mongoTemplate.save(currK,
					obj.substring(obj.lastIndexOf(".") + 1, obj.length()));

		} catch (Exception e) {
			e.printStackTrace();
		}

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
	public void updateKnowledge(KnowledgeNewsVO vo, User user) {

		String obj = Constants.getTableName(vo.getColumnType());
		try {
			Criteria criteria = Criteria.where("_id").is(vo.getkId());
			Query query = new Query(criteria);
			Update update = new Update();
			update.set("status", vo.getKnowledgestatus());
			update.set("title", vo.getTitle());
			update.set("uid", user.getId());
			update.set("uname", user.getName());
			update.set("cpathid", vo.getColumnPath());
			update.set("pic", vo.getPic());
			update.set("desc", HtmlToText.htmlTotest(vo.getContent()));
			update.set("content", vo.getContent());
			update.set("essence", vo.getEssence());
			update.set("modifytime", vo.getCreatetime());
			update.set("taskid", vo.getTaskId());
			update.set("columnid", vo.getColumnid());
			update.set("postUnit", vo.getPostUnit());
			update.set("titanic", vo.getTitanic());
			update.set("submitTime", vo.getSubmitTime());
			update.set("performTime", vo.getPerformTime());
			update.set("selectedIds", vo.getSelectedIds());
			update.set("asso", vo.getAsso());
			update.set("tags", vo.getTags());

			mongoTemplate.updateFirst(query, update,
					obj.substring(obj.lastIndexOf(".") + 1, obj.length()));

		} catch (Exception e) {
			e.printStackTrace();
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

	@Override
	public void restoreKnowledgeByid(long knowledgeid) {
		Criteria criteria = Criteria.where("_id").is(knowledgeid);
		Query query = new Query(criteria);
		KnowledgeNews kdnews = mongoTemplate.findOne(query,
				KnowledgeNews.class, "KnowledgeNews");
		if (kdnews != null) {
			Update update = new Update();
			update.set("status", Constants.Status.checked.v());
			mongoTemplate.updateFirst(query, update, "KnowledgeNews");
		}
	}

	@Override
	public Knowledge getDraftByMainIdAndUser(Long knowledgeId, String type,
			Long userId) {
		String obj = Constants.getTableName(type);

		Knowledge knowledge = null;
		try {
			Criteria criteria = Criteria.where("knowledgeMainId").is(
					knowledgeId);
			if (userId != null) {
				criteria.and("uid").is(userId);
			}
			Query query = new Query(criteria);
			knowledge = (Knowledge) Class.forName(obj).newInstance();
			return (Knowledge) mongoTemplate.findOne(query, Class.forName(obj),
					obj.substring(obj.lastIndexOf(".") + 1, obj.length()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return knowledge;

	}

	@Override
	public void updateKnowledgeDraft(KnowledgeNewsVO vo, User user) {
		String type = vo.getColumnType();
		String obj = Constants.getTableName(vo.getColumnType());
		try {
			Criteria criteria = Criteria.where("_id").is(vo.getkId());
			Query query = new Query(criteria);
			Update update = new Update();
			update.set("status", Constants.Status.draft.v());
			update.set("title", vo.getTitle());
			update.set("uid", user.getId());
			update.set("uname", user.getName());
			update.set("cpathid", vo.getColumnPath());
			update.set("pic", vo.getPic());
			if (type.equals(Constants.Type.Investment.v() + "")
					|| type.equals(Constants.Type.Industry.v() + "")
					|| type.equals(Constants.Type.Case.v() + "")) {
				update.set("desc", vo.getDesc());
			} else {
				update.set("desc", vo.getContent().length() > 50 ? vo
						.getContent().substring(0, 50) : vo.getContent());
			}
			update.set("content", vo.getContent());
			update.set("essence", vo.getEssence());
			update.set("modifytime", vo.getCreatetime());
			update.set("taskid", vo.getTaskId());
			update.set("columnid", vo.getColumnid());
			update.set("postUnit", vo.getPostUnit());
			update.set("titanic", vo.getTitanic());
			update.set("submitTime", vo.getSubmitTime());
			update.set("performTime", vo.getPerformTime());
			update.set("selectedIds", vo.getSelectedIds());
			update.set("asso", vo.getAsso());
			update.set("tags", vo.getTags());

			mongoTemplate.updateFirst(query, update,
					obj.substring(obj.lastIndexOf(".") + 1, obj.length()));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteKnowledgeById(long knowledgeid, String type) {
		String obj = Constants.getTableName(type);
		Criteria criteria = Criteria.where("_id").is(knowledgeid);
		Query query = new Query(criteria);
		mongoTemplate.remove(query,
				obj.substring(obj.lastIndexOf(".") + 1, obj.length()));

	}

}
