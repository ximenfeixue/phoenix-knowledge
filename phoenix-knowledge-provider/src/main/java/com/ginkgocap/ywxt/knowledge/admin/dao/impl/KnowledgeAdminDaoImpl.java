/**
 * 
 */
package com.ginkgocap.ywxt.knowledge.admin.dao.impl;

import java.util.ArrayList;
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
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;
import com.ginkgocap.ywxt.knowledge.mapper.AdminUserCategoryValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeBaseMapper;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeArticle;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeAsset;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeIndustry;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeInvestment;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeLaw;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeMacro;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeOpinion;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCommentService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeRecycleService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeStaticsService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.KnowledgeUtil;
import com.ginkgocap.ywxt.user.service.UserFeedService;
import com.ginkgocap.ywxt.util.DateFunc;
import com.ginkgocap.ywxt.util.PageUtil;

/**
 * @author liubang
 *
 */

@Component("knowledgeAdminDao")
public class KnowledgeAdminDaoImpl implements KnowledgeAdminDao {
	@Resource
	private MongoTemplate mongoTemplate;
	@Resource
	private KnowledgeBaseMapper knowledgeBaseMapper;
	@Resource
	private UserFeedService userFeedService;
	@Resource
	private KnowledgeRecycleService knowledgeRecycleService;
	@Resource
	private KnowledgeCategoryService knowledgeCategoryService;
	@Resource
	private KnowledgeStaticsService knowledgeStaticsService;
	@Resource
	private KnowledgeCommentService knowledgeCommentService;
	@Resource
	private AdminUserCategoryValueMapper adminUserCategoryValueMapper;
	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeAdminDao#selectKnowledgeNewsList(java.lang.Integer, java.lang.Integer, java.util.Map)
	 */
	@Override
	public Map<String, Object> selectKnowledgeNewsList(Integer page,
			Integer size, Map<String, String> searchMap) {
		String collectionName = searchMap.get("collectionName");
		Map<String ,Object> result =new HashMap<String ,Object> (); 
		// 查询所有非金桐脑抓取数据，即cid不为1或被举报的数据
//		Criteria criteria = new Criteria().orOperator(Criteria.where("report_status").is(1),Criteria.where("cid").ne(1));
		Criteria criteria = Criteria.where("cid").ne(0);//暂时先这么写
		String cname = searchMap.get("cname");
		String title = searchMap.get("title");
		String submitBeginCTime = StringUtils.isNotBlank(searchMap.get("submitBeginCTime"))?searchMap.get("submitBeginCTime")+" 00:00:00":"";
		String submitEndCTime = StringUtils.isNotBlank(searchMap.get("submitEndCTime"))?searchMap.get("submitEndCTime")+" 23:59:59":"";
		String approveBeginCTime = StringUtils.isNotBlank(searchMap.get("approveBeginCTime"))?searchMap.get("approveBeginCTime")+" 00:00:00":"";
		String approveEndCTime = StringUtils.isNotBlank(searchMap.get("approveEndCTime"))?searchMap.get("approveEndCTime")+" 23:59:59":"";
		int status = Integer.parseInt(searchMap.get("status"));
		// 状态值为-1时，查找状态为3：审核中；4：审核通过；5：未通过 数据
		if(status== -1) {
			List<Integer> list = new ArrayList<Integer>();
			list.add(3);
			list.add(4);
			list.add(5);
			list.add(8);
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
		if(StringUtils.isNotBlank(submitBeginCTime) && StringUtils.isNotBlank(submitEndCTime)) {
			criteria.andOperator(Criteria.where("createtime").gte(submitBeginCTime),
					Criteria.where("createtime").lte(submitEndCTime));
		}else if(StringUtils.isNotBlank(submitBeginCTime ) && StringUtils.isBlank(submitEndCTime )) {
			criteria.and("createtime").gte(submitBeginCTime);
		}else if(StringUtils.isBlank(submitBeginCTime ) && StringUtils.isNotBlank(submitEndCTime )) {
			criteria.and("createtime").lte(submitEndCTime);
		}
		// 按修改时间查询条件
		if(StringUtils.isNotBlank(approveBeginCTime ) && StringUtils.isNotBlank(approveEndCTime )) {
			criteria.andOperator(Criteria.where("modifytime").gte(approveBeginCTime),
					Criteria.where("modifytime").lte(approveEndCTime));
		}else if(StringUtils.isNotBlank(approveBeginCTime )&& StringUtils.isBlank(approveEndCTime )) {
			criteria.and("modifytime").gte(approveBeginCTime);
		}else if(StringUtils.isBlank(approveBeginCTime) && StringUtils.isNotBlank(approveEndCTime )) {
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
		}else if(StringUtils.equals(collectionName, "KnowledgeOpinion")){//8-观点
			KnowledgeOpinion news = mongoTemplate.findById(id, KnowledgeOpinion.class, collectionName);
			return news;
		}else if(StringUtils.equals(collectionName, "KnowledgeArticle")){//11-文章
			KnowledgeArticle news = mongoTemplate.findById(id, KnowledgeArticle.class, collectionName);
			return news;
		}else{
		
			return null;
		}
}


	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeAdminDao#checkStatusById(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void checkStatusById(long id, int status, String collectionNames) {
		Criteria criteria = new Criteria().and("_id").is(id);
		Query query = new Query(criteria);
		Update update = new Update().set("status",status).set("modifytime", DateFunc.getDate());
		mongoTemplate.updateFirst(query, update, collectionNames);
	}

	@Override
	public void update(long id, String title, String cpathid, String content,String desc,
			String tags, String collectionName) {
		Criteria criteria = new Criteria().and("_id").is(id);
		Query query = new Query(criteria);
		Update update = new Update().set("title",title).set("cpathid", cpathid).set("tags", tags).set("modifytime", DateFunc.getDate());
		if(StringUtils.equals(collectionName, "KnowledgeCase")){
			update.set("desc", content);
		}else{
			update.set("content", content);
		}
		if(StringUtils.equals(collectionName, "KnowledgeInvestment")){
			update.set("desc", desc);
		}
		mongoTemplate.updateFirst(query, update, collectionName);
	}

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeAdminDao#batchDeleteKnowledge(java.lang.String)
	 * Administrator
	 */
	@Override
	public Map<String, Object> batchDeleteKnowledge(String knowledgeids) {

		Map<String, Object> result = new HashMap<String, Object>();
		long[] knowledgeid = null;
		// 检查是否为标准数据
		try {
			if (knowledgeids.endsWith(",")) {
				knowledgeid = KnowledgeUtil.convertionToLong(knowledgeids
						.substring(0, knowledgeids.length() - 1).split(","));
			} else {
				knowledgeid = KnowledgeUtil.convertionToLong(knowledgeids
						.split(","));
			}
		} catch (Exception e) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			return result;
		}

		// 批量删除
		for (int i = 0; i < knowledgeid.length; i++) {
			KnowledgeBase bl = knowledgeBaseMapper.selectByPrimaryKey(knowledgeid[i]);
			long ct = 0;
			if (bl != null) {
				ct = bl.getColumnType();
			} else {
				continue;
			}
			String obj = Constants.getTableName(ct + "");
			String collectionName = obj.substring(obj.lastIndexOf(".") + 1,
					obj.length());
			Criteria criteria = Criteria.where("_id").in(knowledgeid[i]);
			Query query = new Query(criteria);
			Update update = new Update();
			update.set("status", Constants.Status.recycle.v());

			// 删除动态长观点信息
			if (ct == Constants.KnowledgeType.Opinion.v()) {
				userFeedService.deleteDynamicKnowledge(knowledgeid[i]);
			}
			// 删除更新关联表
			try {
				mongoTemplate.updateFirst(query, update, collectionName);
				List<KnowledgeCategory> list = knowledgeCategoryService
						.selectKnowledgeCategory(knowledgeid[i]);
				if (list != null && list.size() > 0) {
					for (KnowledgeCategory knowledgeCategory : list) {
						knowledgeCategoryService.updateKnowledgeCategory(
								knowledgeid[i],
								knowledgeCategory.getCategoryId());
					}
				}
			} catch (Exception e) {
				result.put(Constants.status, Constants.ResultType.fail.v());
			}
			// 删除统计表信息
			knowledgeStaticsService.deleteKnowledgeStatics(knowledgeid[i]);

			// 删除该知识评论信息
			knowledgeCommentService.deleteCommentByknowledgeId(knowledgeid[i],
					bl.getUserId());
		}
		result.put("result", "success");
		return result;
	}

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeAdminDao#updateKnowledge(com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO)
	 * Administrator
	 */
	@Override
	public void updateKnowledge(KnowledgeNewsVO vo) {
		
		String obj = Constants.getTableName(vo.getColumnType());
		try {
			Criteria criteria = Criteria.where("_id").is(vo.getkId());
			Query query = new Query(criteria);
			Update update = new Update();
			update.set("title", vo.getTitle());
			update.set("columnid", vo.getColumnid());
			update.set("conent", vo.getContent());
			update.set("columnPath", vo.getColumnPath());
			update.set("desc", vo.getDesc());
			mongoTemplate.updateFirst(query, update,obj.substring(obj.lastIndexOf(".") + 1, obj.length()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeAdminDao#batchUpdate(java.util.List)
	 * Administrator
	 */
	@Override
	public Map<String,Object> batchUpdate(Map<String,Object> map) {
		
		 Map<String,Object> result = new HashMap<String,Object>();
		List<Long> ids =(List<Long>) map.get("ids");
		String collectionNames = map.get("collectionName")+"";
		int status = Integer.parseInt(map.get("status")+"");
		Criteria criteria = new Criteria().and("_id").in(ids);
		Query query = new Query(criteria);
		Update update = new Update().set("status",status);
		mongoTemplate.updateMulti(query, update, collectionNames);
		
		if(ids !=null && ids.size() >0){
			adminUserCategoryValueMapper.batchUpdateKnowledgeStatus(ids,1);
		}
		result.put("result", "success");
		return result;
	}

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeAdminDao#deleteMutiRows(java.util.Map)
	 * Administrator
	 */
	@Override
	public Map<String, Object> deleteMutiRows(Map<String, Object> map) {
		Map<String,Object> result = new HashMap<String,Object>();
		List<Long> ids =(List<Long>) map.get("ids");
		String collectionNames = map.get("collectionName")+"";
		Criteria criteria = new Criteria().and("_id").in(ids);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, collectionNames);
		
		if(ids !=null && ids.size() >0){
			adminUserCategoryValueMapper.batchUpdateKnowledgeStatus(ids,0);//  待修改
		}
		result.put("result", "success");
		return result;
	}

}
