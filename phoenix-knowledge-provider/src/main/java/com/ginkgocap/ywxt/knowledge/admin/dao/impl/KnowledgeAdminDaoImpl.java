/**
 * 
 */
package com.ginkgocap.ywxt.knowledge.admin.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeAdminDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeAsset;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeIndustry;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeInvestment;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeLaw;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeMacro;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.util.DateUtil;
import com.ginkgocap.ywxt.util.PageUtil;

/**
 * @author liubang
 *
 */

@Component("knowledgeAdminDao")
public class KnowledgeAdminDaoImpl implements KnowledgeAdminDao {
	@Resource
	private MongoTemplate mongoTemplate;
	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeAdminDao#selectKnowledgeNewsList(java.lang.Integer, java.lang.Integer, java.util.Map)
	 */
	@Override
	public Map<String, Object> selectKnowledgeNewsList(Integer page,
			Integer size, Map<String, String> searchMap) {
		String collectionName = searchMap.get("collectionName");
		Map<String ,Object> result =new HashMap<String ,Object> (); 
		// 查询所有非金桐脑抓取数据，即cid不为1或被举报的数据
		Criteria criteria = new Criteria().orOperator(Criteria.where("report_status").is(1),Criteria.where("cid").ne(1));
		
		String cname = searchMap.get("cname");
		String title = searchMap.get("title");
		Date submitBeginCTime = DateUtil.parseWithYYYYMMDDHHMMSS(searchMap.get("submitBeginCTime")+" 00:00:00");
		Date submitEndCTime = DateUtil.parseWithYYYYMMDDHHMMSS(searchMap.get("submitEndCTime"+" 23:59:59"));
		Date approveBeginCTime = DateUtil.parseWithYYYYMMDDHHMMSS(searchMap.get("approveBeginCTime")+" 00:00:00");
		Date approveEndCTime = DateUtil.parseWithYYYYMMDDHHMMSS(searchMap.get("approveEndCTime"+" 23:59:59"));
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
		if(StringUtils.isNotBlank(cname)) {
			criteria.and("cname").regex(".*"+cname+".*$", "i");
		}
		// 按资讯标题查询
		if(StringUtils.isNotBlank(title)) {
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
		System.out.println(""+JSONObject.fromObject(criteria));
		long count = mongoTemplate.count(query, collectionName);
		PageUtil p = new PageUtil((int) count, page, size);
		query.limit(size);
		query.skip(p.getPageStartRow() - 1);
		query.sort().on("createtime", Order.DESCENDING);
		if(StringUtils.equals(collectionName, "KnowledgeNews")){//咨询
			List<KnowledgeNews> list = mongoTemplate.find(query, KnowledgeNews.class,collectionName);
			result.put("rows", list);
		}else if(StringUtils.equals(collectionName, "KnowledgeIndustry")){//行业
			List<KnowledgeIndustry> list = mongoTemplate.find(query, KnowledgeIndustry.class, collectionName);
			result.put("rows", list);
		}else if(StringUtils.equals(collectionName, "KnowledgeAsset")){//资产管理
			List<KnowledgeAsset> list = mongoTemplate.find(query, KnowledgeAsset.class, collectionName);
			result.put("rows", list);
		}else if(StringUtils.equals(collectionName, "KnowledgeCase")){//经典案例
			List<KnowledgeCase> list = mongoTemplate.find(query, KnowledgeCase.class, collectionName);
			result.put("rows", list);
		}else if(StringUtils.equals(collectionName, "KnowledgeLaw")){//法律法规
			List<KnowledgeLaw> list = mongoTemplate.find(query, KnowledgeLaw.class, collectionName);
			result.put("rows", list);
		}else if(StringUtils.equals(collectionName, "KnowledgeMacro")){//宏观
			List<KnowledgeMacro> list = mongoTemplate.find(query, KnowledgeMacro.class, collectionName);
			result.put("rows", list);
		}else if(StringUtils.equals(collectionName, "KnowledgeInvestment")){//投融工具
			List<KnowledgeInvestment> list = mongoTemplate.find(query, KnowledgeInvestment.class, collectionName);
			result.put("rows", list);
		}else{//
			result.put("rows", null);
		}
		result.put("page",p);
		result.put("total", count);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeAdminDao#selectKnowledgeNewsById(long, java.lang.String)
	 */
	@Override
	public Object selectKnowledgeNewsById(long id, String collectionName) {
		if(StringUtils.equals(collectionName, "KnowledgeNews")){//咨询
			KnowledgeNews news = mongoTemplate.findById(id, KnowledgeNews.class, collectionName);
			return news;
		}else if(StringUtils.equals(collectionName, "KnowledgeIndustry")){//行业
			KnowledgeIndustry news = mongoTemplate.findById(id, KnowledgeIndustry.class, collectionName);
			return news;
		}else if(StringUtils.equals(collectionName, "KnowledgeAsset")){//资产管理
			KnowledgeAsset news = mongoTemplate.findById(id, KnowledgeAsset.class, collectionName);
			return news;
		}else if(StringUtils.equals(collectionName, "KnowledgeCase")){//经典案例
			KnowledgeCase news = mongoTemplate.findById(id, KnowledgeCase.class, collectionName);
			return news;
		}else if(StringUtils.equals(collectionName, "KnowledgeLaw")){//法律法规
			KnowledgeLaw news = mongoTemplate.findById(id, KnowledgeLaw.class, collectionName);
			return news;
		}else if(StringUtils.equals(collectionName, "KnowledgeMacro")){//宏观
			KnowledgeMacro news = mongoTemplate.findById(id, KnowledgeMacro.class, collectionName);
			return news;
		}else if(StringUtils.equals(collectionName, "KnowledgeInvestment")){//投融工具
			KnowledgeInvestment news = mongoTemplate.findById(id, KnowledgeInvestment.class, collectionName);
			return news;
		}else{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeAdminDao#deleteKnowledgeIndustryById(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteKnowledgeIndustryById(long id, String collectionName) {
		Criteria criteria = new Criteria().and("_id").is(id);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, collectionName);
	}

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeAdminDao#checkStatusById(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void checkStatusById(long id, int status, String collectionNames) {
		Criteria criteria = new Criteria().and("_id").is(id);
		Query query = new Query(criteria);
		Update update = new Update().set("status",status).set("modifytime", new Date());
		mongoTemplate.updateFirst(query, update, collectionNames);
	}

}
