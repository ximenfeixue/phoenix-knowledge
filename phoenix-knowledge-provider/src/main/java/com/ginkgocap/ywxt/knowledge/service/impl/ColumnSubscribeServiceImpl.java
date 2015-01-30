package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.ColumnExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeColumnSubscribe;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeColumnSubscribeExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeColumnSubscribeExample.Criteria;
import com.ginkgocap.ywxt.knowledge.form.KnowledgeSimpleMerge;
import com.ginkgocap.ywxt.knowledge.form.SubcribeNode;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnKnowledgeMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeColumnSubscribeMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserPermissionValueMapper;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.service.ColumnSubscribeService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.HtmlToText;
import com.ginkgocap.ywxt.knowledge.util.Constants.Ids;
import com.ginkgocap.ywxt.knowledge.util.KCHelper;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("columnSubscribeService")
public class ColumnSubscribeServiceImpl implements ColumnSubscribeService {

	@Autowired
	KnowledgeColumnSubscribeMapper kcsm;

	@Autowired
	ColumnMapper cm;

	@Autowired
	ColumnService columnService;

	@Resource
	private KnowledgeColumnSubscribeMapper knowledgeColumnSubscribeMapper;

	@Resource
	private UserPermissionValueMapper userPermissionValueMapper;

	@Resource
	private MongoTemplate mongoTemplate;

	@Resource
	private ColumnKnowledgeMapper columnKnowledgeMapper;

	private static final Logger logger = LoggerFactory
			.getLogger(KnowledgeCollectionServiceImpl.class);

	@Override
	public KnowledgeColumnSubscribe add(KnowledgeColumnSubscribe kcs) {

		if (this.isExist(kcs.getUserId(), kcs.getColumnId())) {
			// 应该查询
			return kcs;
		}

		if (kcs.getColumnType() == null) {

			Column kc = cm.selectByPrimaryKey(kcs.getColumnId());

			// 栏目类型分析，只能按照一级父id查询，根据columnlevelpath路径无法分析其类型
			Integer type = KCHelper.resolveKCType(kc.getId(), kc.getParentId());
			kcs.setColumnType(KCHelper.getMysqlkcType(type));
		}

		Date date = new Date();
		kcs.setSubDate(date);

		// 怎么查询id
		kcsm.insert(kcs);

		return kcs;
	}

	@Override
	public boolean isExist(long userId, long columnId) {
		KnowledgeColumnSubscribeExample kcseExample = new KnowledgeColumnSubscribeExample();
		kcseExample.createCriteria().andUserIdEqualTo(userId)
				.andColumnIdEqualTo(columnId);
		return kcsm.countByExample(kcseExample) > 0 ? true : false;
	}

	@Override
	public boolean isExistType(long userId, short type) {
		KnowledgeColumnSubscribeExample kcseExample = new KnowledgeColumnSubscribeExample();
		kcseExample.createCriteria().andUserIdEqualTo(userId)
				.andColumnTypeEqualTo(type);
		return kcsm.countByExample(kcseExample) > 0 ? true : false;
	}

	@Override
	public Long countSubNumber(long userId, long columnId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(KnowledgeColumnSubscribe kcs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public KnowledgeColumnSubscribe merge(KnowledgeColumnSubscribe kcs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteByUIdAndKCId(long userId, long columnId) {
		KnowledgeColumnSubscribeExample kcseExample = new KnowledgeColumnSubscribeExample();
		kcseExample.createCriteria().andUserIdEqualTo(userId)
				.andColumnIdEqualTo(columnId);
		kcsm.deleteByExample(kcseExample);
	}

	@Override
	public void deleteByPK(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<KnowledgeColumnSubscribe> selectByUserId(long userId) {
		KnowledgeColumnSubscribeExample kcseExample = new KnowledgeColumnSubscribeExample();
		kcseExample.createCriteria().andUserIdEqualTo(userId);
		List<KnowledgeColumnSubscribe> list = kcsm.selectByExample(kcseExample);
		return list;
	}

	@Override
	public List<Column> selectKCListByUserId(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long> selectUserIdListByKc(long columnId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KnowledgeColumnSubscribe> selectByKCId(long columnId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countByKC(long columnId) {
		KnowledgeColumnSubscribeExample kcseExample = new KnowledgeColumnSubscribeExample();
		kcseExample.createCriteria().andColumnIdEqualTo(columnId);
		return kcsm.countByExample(kcseExample);
	}

	@Override
	public List<Integer> countAllByKC(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countByUserId(long userId) {
		KnowledgeColumnSubscribeExample kcseExample = new KnowledgeColumnSubscribeExample();
		kcseExample.createCriteria().andUserIdEqualTo(userId);
		return kcsm.countByExample(kcseExample);
	}

	@Override
	public List<KnowledgeSimpleMerge> selectSubKnowByUserId(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KnowledgeSimpleMerge> selectSubKnowByUserId(long userId,
			int type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KnowledgeSimpleMerge> selectSubKnowByKCList(List<Column> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long updateSubscribeCount(long columnid) {
		long count = this.countByKC(columnid);
		Column cc = new Column();
		cc.setId(columnid);
		cc.setSubscribeCount(count);
		cm.updateByPrimaryKeySelective(cc);
		return columnService.queryById(columnid).getSubscribeCount();
	}

	// @Override
	// public KnowledgeColumnSubscribe add(KnowledgeColumnSubscribe kcs) {
	//
	// //TODO 判断是否已存在
	//
	// // this.isExist(kcs.getUserId(), kcs.getColumnId());
	//
	// if (StringUtils.isEmpty(kcs.getColumnType())) {
	// KnowledgeColumn kc=knowledgeColumnDao.queryById(kcs.getColumnId());
	//
	// //栏目类型分析，只能按照一级父id查询，根据columnlevelpath路径无法分析其类型
	// kc=KCHelper.setKCType(kc);
	// String columnType=KCHelper.getMysqlkcType(kc.getKcType());
	//
	// kcs.setColumnType(columnType);
	// }
	//
	//
	// Date date=new Date();
	// kcs.setSubDate(date);
	//
	// return kcsDao.insert(kcs);
	// }
	@Override
	public List<KnowledgeSimpleMerge> selectSubKnowByKCList(List<Column> list,
			int type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> selectRankList(int count, long userid) {
		ColumnExample ce = new ColumnExample();
		ce.createCriteria().andColumnLevelPathLike("__________________")
				.andDelStatusEqualTo((byte) 0);
		ce.setLimitStart(0);
		ce.setLimitEnd(count);
		ce.setOrderByClause("subscribe_count desc");
		List<Column> cl = cm.selectByExample(ce);
		Map<String, Object> hm = new HashMap<String, Object>();
		Map<String, Object> rm = new HashMap<String, Object>();
		for (Column c : cl) {
			if (this.isExist(userid, c.getId())) {
				// 表示存在 ：显示取消按钮
				hm.put(c.getId() + "", 0);
			} else {
				hm.put(c.getId() + "", 1);
			}
		}
		rm.put("list", cl);
		rm.put("map", hm);
		return rm;
	}

	@Override
	public List<SubcribeNode> selectAllList(Long userid, short t) {
		List<SubcribeNode> rccnl = new ArrayList<com.ginkgocap.ywxt.knowledge.form.SubcribeNode>();
		for (long id : Constants.homeColumnIds) {
			if (t == 1) {
				if (!this.isExistType(userid, (short) id)) {
					continue;
				}
			}
			Column c = columnService.queryById(id);
			SubcribeNode sn = new SubcribeNode();
			sn.setId(c.getId());
			sn.setName(c.getColumnname());
			// query child
			List<Column> ccl = columnService.queryByParentId(c.getId(),
					Constants.gtnid);
			List<SubcribeNode> ccnl = new ArrayList<SubcribeNode>();
			for (Column xc : ccl) {
				SubcribeNode sxn = new SubcribeNode();
				sxn.setId(xc.getId());
				sxn.setName(xc.getColumnname());
				sxn.setCount(xc.getSubscribeCount());
				if (this.isExist(userid, xc.getId())) {
					// 表示存在 ：显示取消按钮
					sxn.setState("0");
				} else {
					sxn.setState("1");
				}
				if (t == 0) {
					ccnl.add(sxn);
				} else {
					if (!this.isExist(userid, xc.getId())) {
						continue;
					} else {
						ccnl.add(sxn);
					}
				}
			}
			sn.setList(ccnl);
			rccnl.add(sn);
		}
		return rccnl;
	}

	@Override
	public Map<String, Object> selectMySubscribe(long id, String type,
			Integer source, Integer pno, Integer psize) {
		return searchKnowledgeFromSub(id, null, type, source, pno, psize);
	}

	private org.springframework.data.mongodb.core.query.Criteria createQueryCriteria(
			long uid, List<String> columnList, Integer source, String keywords) {
		org.springframework.data.mongodb.core.query.Criteria c = null;
		if (source == Constants.Relation.jinTN.v()) {
			c = org.springframework.data.mongodb.core.query.Criteria
					.where("columnid").in(columnList).and("uid")
					.is(Ids.jinTN.v()).and("status").is(4);
		} else if (source == Constants.Relation.self.v()) {
			logger.info("进入查询我的订阅自己的知识Id列表,类型:{},栏目列表{}", source, columnList);
			// 订阅下自己的知识ID，knowledgeIds
			List<Long> knowledgeIds = userPermissionValueMapper
					.selectKnowledgeIdsByParams(null, uid, columnList);
			logger.info("结束查询我的订阅自己的知识Id列表,类型:{},知识ID列表{}", source,
					knowledgeIds);
			c = org.springframework.data.mongodb.core.query.Criteria.where(
					"_id").in(knowledgeIds).and("status").is(4);

		} else if (source == Constants.Relation.friends.v()) {
			logger.info("进入查询我的订阅分享给我的中乐，大乐的知识Id列表,类型:{},栏目列表{}", source,
					columnList);
			// 获取所有大乐，中乐分享给我的knowledgeIds
			List<Long> knowledgeIds = userPermissionValueMapper
					.selectKnowledgeIdsByParams(uid, null, columnList);
			logger.info("结束查询我的订阅分享到我的中乐，大乐的知识Id列表,类型:{},知识ID列表{}", source,
					knowledgeIds);
			c = org.springframework.data.mongodb.core.query.Criteria.where(
					"_id").in(knowledgeIds).and("status").is(4);
		} else if (source == Constants.Relation.platform.v()) {
			logger.info("进入查询我的订阅分享到全平台的中乐，大乐的知识Id列表,类型:{},栏目列表{}", source,
					columnList);
			// 获取所有大乐，中乐分享到全平台的knowledgeIds
			List<Long> knowledgeIds = userPermissionValueMapper
					.selectKnowledgeIdsByParams(-1l, null, columnList);
			logger.info("结束查询我的订阅分享到全平台的中乐，大乐的知识Id列表,类型:{},知识ID列表{}", source,
					knowledgeIds);
			c = org.springframework.data.mongodb.core.query.Criteria.where(
					"_id").in(knowledgeIds).and("status").is(4);
		}
		if (StringUtils.isNotBlank(keywords)) {
			Pattern pattern = Pattern.compile("^.*" + keywords + ".*$",
					Pattern.CASE_INSENSITIVE);
			c.and("title").regex(pattern);
		}
		return c;
	}

	@Override
	public Map<String, Object> searchKnowledgeFromSub(long id, String keywords,
			String type, Integer source, Integer pno, Integer psize) {
		logger.info("进入查询我的订阅请求,用户:{},类型{}", id, type);
		Map<String, Object> result = new HashMap<String, Object>();
		KnowledgeColumnSubscribeExample example = new KnowledgeColumnSubscribeExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(id);
		criteria.andColumnTypeEqualTo(Short.parseShort(type));
		criteria.andColumnIdGreaterThan(11l);
		example.setOrderByClause("sub_date desc");
		List<KnowledgeColumnSubscribe> list = knowledgeColumnSubscribeMapper
				.selectByExample(example);
		if (list == null || list.size() == 0) {
			result.put("results", list);
			result.put("count", 0);
		} else {
			// 查询订阅栏目下的知识Id集合
			List<String> columnList = new ArrayList<String>();
			for (KnowledgeColumnSubscribe sub : list) {
				columnList.add(sub.getColumnId() + "");

			}
			try {
				String obj = Constants.getTableName(type + "");
				// 根据ID集合查询Mongodb数据
				Query query = new Query();
				org.springframework.data.mongodb.core.query.Criteria c = createQueryCriteria(
						id, columnList, source, keywords);
				query.addCriteria(c);

				long v = mongoTemplate.count(query,
						obj.substring(obj.lastIndexOf(".") + 1, obj.length()));
				query.limit(psize);
				query.skip((pno - 1) * psize);
				query.sort().on("createtime", Order.DESCENDING);
				if (StringUtils.isBlank(obj)) {
					result.put(Constants.status, Constants.ResultType.fail.v());
					result.put(Constants.errormessage, "没有找到类型为" + type
							+ "的对象.");
					return result;
				}
				List<Knowledge> subList = mongoTemplate.find(query,
						Knowledge.class,
						obj.substring(obj.lastIndexOf(".") + 1, obj.length()));

				for (Knowledge k : subList) {
					if (StringUtils.isNotBlank(k.getDesc())) {
						String desc = HtmlToText.html2Text(k.getDesc());
						if (StringUtils.isNotBlank(desc)) {
							desc = desc.length() > 50 ? desc.substring(0, 50)
									+ "..." : desc;
						}
						k.setDesc(desc);
					} else {
						String content = HtmlToText.html2Text(k.getContent());
						if (StringUtils.isNotBlank(content)) {
							content = content.length() > 50 ? content
									.substring(0, 50) + "..." : content;
						}
						k.setDesc(content);
					}
					k.setColumnType(type);
				}
				result.put("results", subList);

				PageUtil p = new PageUtil((int) v, pno, psize);

				result.put("page", p);

				result.put("totalPage", v % psize == 0 ? v / psize : v / psize
						+ 1);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("查询我的订阅请求异常,用户:{},类型{}", id, type);
			}
		}
		return result;
	}

	@Override
	@Transactional
	public void mobileSubscribeColumn(int type, long userId, long columnId) {

		if (type == 1) {
			KnowledgeColumnSubscribe kcs = new KnowledgeColumnSubscribe();
			kcs.setUserId(userId);
			kcs.setColumnId(columnId);
			kcs.setSubDate(new Date());
			add(kcs);
		} else {
			deleteByUIdAndKCId(userId, columnId);
		}

	}

}
