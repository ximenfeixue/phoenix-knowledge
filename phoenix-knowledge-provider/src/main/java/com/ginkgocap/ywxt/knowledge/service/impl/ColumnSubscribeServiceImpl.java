package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

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
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.service.ColumnSubscribeService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.Constants.Ids;
import com.ginkgocap.ywxt.knowledge.util.KCHelper;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("columnSubscribeService")
public class ColumnSubscribeServiceImpl implements ColumnSubscribeService {

	// @Autowired
	// private KnowledgeColumnDao knowledgeColumnDao;
	//
	// @Autowired
	// private KnowledgeColumnSubscribeDao kcsDao;

	@Autowired
	KnowledgeColumnSubscribeMapper kcsm;

	@Autowired
	ColumnMapper cm;

	@Autowired
	ColumnService columnService;

	@Resource
	private KnowledgeColumnSubscribeMapper knowledgeColumnSubscribeMapper;

	@Resource
	private MongoTemplate mongoTemplate;

	@Resource
	private ColumnKnowledgeMapper columnKnowledgeMapper;

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
		ce.createCriteria().andColumnLevelPathLike("__________________").andDelStatusEqualTo((byte) 0);
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
		Map<String, Object> result = new HashMap<String, Object>();
		KnowledgeColumnSubscribeExample example = new KnowledgeColumnSubscribeExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(id);
		example.setOrderByClause("sub_date desc");
		List<KnowledgeColumnSubscribe> list = knowledgeColumnSubscribeMapper
				.selectByExample(example);
		if (list == null || list.size() == 0) {
			result.put("results", "");
			result.put("count", 0);
		} else {
			// 查询订阅栏目下的知识Id集合
			List<Long> columnList = new ArrayList<Long>();
			for (KnowledgeColumnSubscribe sub : list) {
				columnList.add(sub.getColumnId());

			}
			// 根据ID集合查询Mongodb数据
			Query query = new Query();
			org.springframework.data.mongodb.core.query.Criteria c = createQueryCriteria(
					id, columnList, source, pno, psize);
			query.addCriteria(c);
			query.limit(psize);
			query.skip((pno - 1) * psize);
			query.sort().on("createtime", Order.DESCENDING);
			String obj = Constants.getTableName(type + "");
			if (StringUtils.isBlank(obj)) {
				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage, "没有找到类型为" + type + "的对象.");
				return result;
			}
			try {
				@SuppressWarnings("unchecked")
				List<Knowledge> subList = (List<Knowledge>) mongoTemplate.find(
						query, Class.forName(obj),
						obj.substring(obj.lastIndexOf(".") + 1, obj.length()));

				result.put("results", subList);

				long v = mongoTemplate.count(query,
						obj.substring(obj.lastIndexOf(".") + 1, obj.length()));

				PageUtil p = new PageUtil((int) v, pno, psize);
				
				result.put("page", p);

				result.put("totalPage", v % psize == 0 ? v / psize : v / psize
						+ 1);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private org.springframework.data.mongodb.core.query.Criteria createQueryCriteria(
			long uid, List<Long> columnList, Integer source, Integer pno,
			Integer psize) {
		org.springframework.data.mongodb.core.query.Criteria c = null;
		if (source == Constants.Relation.jinTN.v()) {
			c = org.springframework.data.mongodb.core.query.Criteria
					.where("columnid").in(columnList).and("uid")
					.is(Ids.jinTN.v());
		} else if (source == Constants.Relation.self.v()) {
			c = org.springframework.data.mongodb.core.query.Criteria
					.where("columnid").in(columnList).and("uid").is(uid);

		} else if (source == Constants.Relation.friends.v()) {
			List<Long> list = new ArrayList<Long>();
			list.add(uid);
			list.add((long) Ids.jinTN.v());
			list.add((long) Constants.Ids.platform.v());
			c = org.springframework.data.mongodb.core.query.Criteria
					.where("columnid").in(columnList).and("uid").nin(list);

		} else if (source == Constants.Relation.platform.v()) {
			c = org.springframework.data.mongodb.core.query.Criteria
					.where("columnid").in(columnList).and("uid")
					.is(Constants.Ids.platform);
		}
		return c;
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

	/*
	 * 
	 * @Override public int update(KnowledgeColumnSubscribe kcs) { return
	 * kcsDao.update(kcs); }
	 * 
	 * @Override public KnowledgeColumnSubscribe merge(KnowledgeColumnSubscribe
	 * kcs) {
	 * 
	 * if (null == kcs) { return null; }
	 * 
	 * Long id =-1l;
	 * 
	 * //在不使用包装类型的情况下 try { id = kcs.getId(); } catch (NullPointerException e) {
	 * e.printStackTrace();
	 * 
	 * KnowledgeColumnSubscribe kcsi=kcsDao.insert(kcs); return kcsi; }
	 * 
	 * if (id>0) { this.update(kcs); return kcs; }
	 * 
	 * return null; }
	 * 
	 * @Override public void deleteByUIdAndKCId(long userId, long columnId) {
	 * kcsDao.deleteByUIdAndKCId(userId, columnId);
	 * 
	 * }
	 * 
	 * @Override public void deleteByPK(long id) { kcsDao.deleteByPK(id); }
	 * 
	 * @Override public List<KnowledgeColumnSubscribe> selectByUserId(long
	 * userId) { return kcsDao.selectByUserId(userId); }
	 * 
	 * @Override public List<KnowledgeColumn> selectKCListByUserId(long userId)
	 * { // TODO Auto-generated method stub return null; }
	 * 
	 * @Override public List<Long> selectUserIdListByKc(long columnId) { // TODO
	 * Auto-generated method stub return null; }
	 * 
	 * @Override public List<KnowledgeColumnSubscribe> selectByKCId(long
	 * columnId) { // TODO Auto-generated method stub return null; }
	 * 
	 * @Override public int countByKC(long columnId) { // TODO Auto-generated
	 * method stub return 0; }
	 * 
	 * @Override public List<Integer> countAllByKC(long userId) { // TODO
	 * Auto-generated method stub return null; }
	 * 
	 * @Override public int countByUserId(long userId) { // TODO Auto-generated
	 * method stub return 0; }
	 * 
	 * @Override public List<KnowledgeSimpleMerge> selectSubKnowByUserId(long
	 * userId) { // TODO Auto-generated method stub return null; }
	 * 
	 * @Override public List<KnowledgeSimpleMerge> selectSubKnowByUserId(long
	 * userId, int type) { // TODO Auto-generated method stub return null; }
	 * 
	 * @Override public List<KnowledgeSimpleMerge>
	 * selectSubKnowByKCList(List<KnowledgeColumn> list) { // TODO
	 * Auto-generated method stub return null; }
	 * 
	 * @Override public List<KnowledgeSimpleMerge>
	 * selectSubKnowByKCList(List<KnowledgeColumn> list, int type) { // TODO
	 * Auto-generated method stub return null; }
	 * 
	 * @Override public boolean isExist(long userId, long columnId) { Long
	 * num=kcsDao.countSubNumber(userId, columnId); if (num>0) { return true; }
	 * return false; }
	 * 
	 * @Override public Long countSubNumber(long userId, long columnId) { return
	 * kcsDao.countSubNumber(userId, columnId); }
	 */
}
