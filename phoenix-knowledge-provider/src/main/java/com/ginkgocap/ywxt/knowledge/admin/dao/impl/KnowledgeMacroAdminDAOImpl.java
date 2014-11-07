package com.ginkgocap.ywxt.knowledge.admin.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeMacroAdminDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeMacro;
import com.ginkgocap.ywxt.knowledge.util.DateUtil;
import com.ginkgocap.ywxt.util.PageUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 宏观后台的DAO接口
 * 
 * @author fuliwen
 * @创建时间：2014-11-05 16:11
 */

@Component("knowledgeMacroAdminDAO")
public class KnowledgeMacroAdminDAOImpl implements KnowledgeMacroAdminDAO {

	@Autowired
	SqlMapClient sqlMapClient;

	@Resource
	private MongoTemplate mongoTemplate;

	@Override
	public List<KnowledgeMacro> findAll() {
		return mongoTemplate.findAll(KnowledgeMacro.class, "KnowledgeMacro");
	}

	@Override
	public Map<String,Object> selectKnowledgeMacroList(Integer page,
			Integer size, Map<String,String> searchMap) {
		Map<String ,Object> result =new HashMap<String ,Object> (); 
		// 查询所有非金桐脑抓取数据，即cid不为1或被举报的数据
		Criteria criteria = new Criteria().orOperator(Criteria.where("report_status").is(1),Criteria.where("cid").ne(1));
		
		String cname = searchMap.get("cname");
		String title = searchMap.get("title");
		Date submitBeginCTime = DateUtil.parseWithYYYYMMDDHHMMSS(searchMap.get("submitBeginCTime"));
		Date submitEndCTime = DateUtil.parseWithYYYYMMDDHHMMSS(searchMap.get("submitEndCTime"));
		Date approveBeginCTime = DateUtil.parseWithYYYYMMDDHHMMSS(searchMap.get("approveBeginCTime"));
		Date approveEndCTime = DateUtil.parseWithYYYYMMDDHHMMSS(searchMap.get("approveEndCTime"));
		int status = Integer.parseInt(searchMap.get("status"));
		// 状态值为-1时，查找状态为3：审核中；4：审核通过；5：未通过 数据
		if(status== -1) {
			List<Integer> list = new ArrayList<Integer>();
			list.add(3);
			list.add(4);
			list.add(5);
			criteria.and("status").in(list);
		}else {
			criteria.and("status").is(status);
		}
		// 按创建者查询
		if(cname.trim()!="" && cname!=null) {
			criteria.and("cname").regex(".*"+cname+".*$", "i");
		}
		// 按资讯标题查询
		if(title.trim()!="" && title!=null) {
			criteria.and("title").regex(".*"+title+".*$","i");
		}
		// 按创建时间查询条件
		if(submitBeginCTime != null && submitEndCTime != null) {
			criteria.andOperator(Criteria.where("createtime").gte(submitBeginCTime),
					Criteria.where("createtime").lte(submitEndCTime));
		}else if(submitBeginCTime != null && submitEndCTime == null) {
			criteria.and("createtime").gte(submitBeginCTime);
		}else if(submitBeginCTime == null && submitEndCTime != null) {
			criteria.and("createtime").lte(submitEndCTime);
		}
		// 按修改时间查询条件
		if(approveBeginCTime != null && approveEndCTime != null) {
			criteria.andOperator(Criteria.where("modifytime").gte(approveBeginCTime),
					Criteria.where("modifytime").lte(approveEndCTime));
		}else if(approveBeginCTime != null && approveEndCTime == null) {
			criteria.and("modifytime").gte(approveBeginCTime);
		}else if(approveBeginCTime == null && approveEndCTime != null) {
			criteria.and("modifytime").lte(approveEndCTime);
		}
		
		Query query = new Query(criteria);
		query.sort().on("createtime", Order.DESCENDING);
		long count = mongoTemplate.count(query, "KnowledgeMacro");
		PageUtil p = new PageUtil((int) count, page, size);
		query.limit(size);
		query.skip(p.getPageStartRow() - 1);
		List<KnowledgeMacro> list = mongoTemplate.find(query, KnowledgeMacro.class, "KnowledgeMacro");
		result.put("total", count);
		result.put("rows", list);
		result.put("page",page);
		result.put("size", size);
		result.put("start", p.getPageStartRow() - 1);
		result.put("params", searchMap);
		result.put("title", title);
		result.put("submitBeginCTime", submitBeginCTime);
		result.put("submitEndCTime", submitEndCTime);
		result.put("approveBeginCTime", approveBeginCTime);
		result.put("approveEndCTime", approveEndCTime);
		result.put("status", status);
		return result;
	}

	@Override
	public long selectKnowledgeMacroListCount() {
		Criteria criteria = Criteria.where("cid").is(1);
		Query query = new Query(criteria);
		long count = mongoTemplate.count(query, KnowledgeMacro.class);
		return count;
	}

	@Override
	public KnowledgeMacro selectKnowledgeMacroById(long id) {
		KnowledgeMacro news = mongoTemplate.findById(id, KnowledgeMacro.class, "KnowledgeMacro");
		return news;
	}

	@Override
	public void deleteKnowledgeMacroById(long id) {
		KnowledgeMacro news = mongoTemplate.findById(id, KnowledgeMacro.class, "KnowledgeMacro");
		mongoTemplate.remove(news, "KnowledgeMacro");
	}

	@Override
	public void checkStatusById(long id, int status) {
		KnowledgeMacro news = mongoTemplate.findById(id, KnowledgeMacro.class, "KnowledgeMacro");
		news.setStatus(status);
		mongoTemplate.save(news, "KnowledgeMacro");
	}
}
