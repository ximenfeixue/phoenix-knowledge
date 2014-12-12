package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeHomeDao;
import com.ginkgocap.ywxt.knowledge.dao.news.KnowledgeNewsDAO;
import com.ginkgocap.ywxt.knowledge.entity.Attachment;
import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStaticsExample;
import com.ginkgocap.ywxt.knowledge.entity.UserPermissionExample;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnKnowledgeMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnKnowledgeValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCategoryValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserPermissionMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserPermissionValueMapper;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeVO;
import com.ginkgocap.ywxt.knowledge.service.AttachmentService;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeHomeService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeReaderService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.tree.Branch;
import com.ginkgocap.ywxt.knowledge.util.tree.ConvertUtil;
import com.ginkgocap.ywxt.knowledge.util.tree.Node;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("knowledgeHomeService")
public class KnowledgeHomeServiceImpl implements KnowledgeHomeService {

	@Autowired
	KnowledgeStaticsMapper knowledgeStaticsMapper;
	@Autowired
	ColumnValueMapper columnValueMapper;
	@Autowired
	ColumnService columnService;
	@Autowired
	KnowledgeCategoryValueMapper knowledgeCategoryValueMapper;
	@Autowired
	UserPermissionMapper userPermissionMapper;
	@Autowired
	ColumnMapper columnMapper;
	@Autowired
	ColumnKnowledgeValueMapper columnKnowledgeValueMapper;
	@Autowired
	KnowledgeHomeDao knowledgeHomeDao;
	@Autowired
	KnowledgeNewsDAO knowledgeNewsDao;
	@Resource
	private AttachmentService attachmentService;

	@Resource
	private MongoTemplate mongoTemplate;

	@Autowired
	private ColumnKnowledgeMapper columnKnowledgeMapper;
	@Autowired
	private UserPermissionValueMapper userPermissionValueMapper;
	@Resource
	private KnowledgeReaderService knowledgeReaderService;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public List<KnowledgeStatics> getRankList(Long columnid) {
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.getRankList:{}",
				columnid);
		Column c = columnMapper.selectByPrimaryKey(columnid);
		if (c != null) {
			String clp = c.getColumnLevelPath().substring(0, 9);
			byte type = (byte) Integer.parseInt(clp);
			KnowledgeStaticsExample ce = new KnowledgeStaticsExample();
			ce.createCriteria().andSourceEqualTo((short) 0)
					.andTypeEqualTo((short) type);
			ce.setLimitStart(0);
			ce.setLimitEnd(10);
			ce.setOrderByClause("commentCount desc");
			return knowledgeStaticsMapper.selectByExample(ce);
		}
		return null;
	}

	@Override
	public List<Column> getTypeList(Long userId, Long columnId) {
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.getTypeList:{}",
				columnId);
		return columnValueMapper.selectByParam(columnId, Constants.gtnid,
				userId);
	}

	// 首页栏目
	@Override
	public <T> Map<String, Object> selectAllByParam(T t, int state,
			String columnid, Long userid, int page, int size) {
		long start = System.currentTimeMillis();
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllByParam:{},",
				state);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllByParam:{},",
				columnid);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllByParam:{},",
				userid);

		Map<String, Object> model = new HashMap<String, Object>();
		String[] names = t.getClass().getName().split("\\.");
		int length = names.length;
		// 栏目id
		Long cid = Long.parseLong(columnid);
		// 查询栏目类型
		Column column = columnMapper.selectByPrimaryKey(cid);

		String ty = column.getColumnLevelPath().substring(0, 9);
		// String cname = column.getColumnname();
		long type = Long.parseLong(ty);
		Criteria criteria = Criteria.where("status").is(4);
		// 权限条件过滤
		Criteria criteriaUp = null;
		// 我的知识过滤
		Criteria criteriaMy = new Criteria();
		// 金桐脑过滤
		Criteria criteriaGt = new Criteria();
		// 路径过滤
		Criteria criteriaPath = new Criteria();

		// 定义查询条件
		// 查询权限表，获取可见文章ID列表
		List<Long> ids = userPermissionValueMapper.selectByParamsSingle(userid,
				type);
		// List<String> cls = userPermissionValueMapper.selectVisble(userid,
		// type);
		List<Long> cls = userPermissionValueMapper.selectVisbleColumnid(userid,
				type);
		if (ids != null && ids.size() > 0) {
			criteriaUp = new Criteria();
			criteriaUp.and("_id").in(ids);
		}
		// 我的知识条件
		criteriaMy.and("uid").is(userid);
		// 金桐脑知识条件
		criteriaGt.and("uid").is(Constants.Ids.jinTN.v());
		// 查询栏目目录为当前分类下的所有数据
		String reful = column.getPathName();
		// 该栏目路径下的所有文章条件
		criteriaPath.and("cpathid").regex(reful + ".*$");
		// 汇总条件
		Criteria criteriaAll = new Criteria();
		if (criteriaUp == null) {

			criteriaAll.orOperator(criteriaMy, criteriaGt).andOperator(
					criteriaPath);
		} else {

			criteriaAll.orOperator(criteriaMy, criteriaGt, criteriaUp)
					.andOperator(criteriaPath);
		}
		// Criteria criteriaVisAll = new Criteria();
		if (cls != null && cls.size() > 0) {// 判断定制
			// Criteria[] criteriaArr= new Criteria[cls.size()];
			// for (int i = 0; i < cls.size(); i++) {
			// Criteria criteriav = new Criteria();
			// Pattern p = Pattern.compile(cname+"/(?!"+cls.get(i)+").*$");
			// criteriaArr[i] = criteriav.and("cpathid").regex(p);
			// }
			// criteriaVisAll.andOperator(criteriaArr);
			List<String> clstr = fillList(cls);
			criteriaAll.and("columnid").nin(clstr);
		}
		criteria.andOperator(criteriaAll);
		// 查询知识
		Query query = new Query(criteria);
		if (type == 10) {
			query.sort().on("createtime", Order.DESCENDING);
		} else {
			query.sort().on("_id", Order.DESCENDING);
		}
		query.limit(size);
		query.skip((page - 1) * size);
		model.put("list", (List) mongoTemplate.find(query, KnowledgeVO.class,
				names[length - 1]));
		logger.info("总消耗时间为  end= " + (System.currentTimeMillis() - start));
		return model;
	}

	private List<String> fillList(List<Long> cls) {
		List<String> clstr = new ArrayList<String>();
		for (Long l : cls) {
			clstr.add(l + "");
		}
		return clstr;
	}

	// 首页主页
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> selectIndexByParam(Constants.Type ty, int page, int size) {
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectIndexByParam:{},",
				ty);
		String[] names = ty.obj().split("\\.");
		int length = names.length;
		Criteria criteria = Criteria.where("status").is(4);
		Criteria criteriaPj = new Criteria();
		Criteria criteriaUp = new Criteria();
		Criteria criteriaGt = new Criteria();
		List<Long> ids = new ArrayList<Long>();

		criteriaGt.and("uid").is(Constants.gtnid);
		// 查询栏目大类下的数据：全平台
        ids = userPermissionValueMapper.selectByParamsSingle(null,
                (long) ty.v());
		// 查询资讯
		if (ids != null && ids.size()>0) {
			criteriaUp.and("_id").in(ids);
			criteriaPj.orOperator(criteriaUp, criteriaGt);
		}else{
		    criteriaPj.andOperator(criteriaGt);
		}
		criteria.andOperator(criteriaPj);
		Query query = new Query(criteria);
		String str = "" + JSONObject.fromObject(criteria);
		logger.info("MongoObject:" + ty.obj() + ",Query:" + str);
		query.sort().on("_id", Order.DESCENDING);
		long count;
		try {
//			count = mongoTemplate.count(query, names[length - 1]);
//			PageUtil p = new PageUtil((int) count, page, size);
			query.limit(size);
			query.skip(0);
			return (List<T>) mongoTemplate.find(query, KnowledgeVO.class,
					names[length - 1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public KnowledgeStatics getPl(long id) {
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.getPl:{}",
				id);
		return knowledgeStaticsMapper.selectByPrimaryKey(id);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Object> selectAllKnowledgeCategoryByParam(String tid,
			String lid, int state, String sortid, Long userid, String keyword,
			int page, int size) {
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllKnowledgeCategoryByParam:{},",
				tid);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllKnowledgeCategoryByParam:{},",
				lid);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllKnowledgeCategoryByParam:{},",
				state);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllKnowledgeCategoryByParam:{},",
				sortid);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllKnowledgeCategoryByParam:{},",
				userid);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllKnowledgeCategoryByParam:{},",
				keyword);
		int start = (page - 1) * size;
		int count = knowledgeCategoryValueMapper.countKnowledgeIds(userid,
				state, sortid, Constants.gtnid, tid, lid, keyword);
		List kcl = knowledgeCategoryValueMapper.selectKnowledgeIds(userid,
				state, sortid, Constants.gtnid, tid, lid, keyword, start, size);
		PageUtil p = new PageUtil(count, page, size);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("page", p);
		m.put("list", kcl);
		return m;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Object> selectKnowledgeCategoryForImport(Long userid,
			List<Long> groupid, int page, int size) {
		if (userid == null || groupid == null || groupid.size() == 0) {
			return null;
		}
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectKnowledgeCategoryForImport:{},",
				userid);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectKnowledgeCategoryForImport:{},",
				groupid);
		int start = (page - 1) * size;
		int count = knowledgeCategoryValueMapper.countKnowledgeId(userid,
				groupid);
		List<Map<String, Object>> kcl = knowledgeCategoryValueMapper
				.selectKnowledgeId(userid, groupid, start, size);
		if (kcl != null && kcl.size() > 0) {
			for (Map<String, Object> map : kcl) {
				int haveFile = 0;
				int fileSize = 0;
				if (map != null && map.get("taskid") != null) {
					if (StringUtils.isNotBlank(map.get("taskid").toString())) {
						Map<String, Object> attMap = attachmentService
								.queryAttachmentByTaskId(map.get("taskid")
										.toString());
						if (attMap != null && attMap.get("attList") != null) {
							List<FileIndex> attList = (List<FileIndex>) attMap
									.get("attList");
							if (attList != null && attList.size() > 0) {
								haveFile = 1;
								for (FileIndex att : attList) {
									fileSize += att.getFileSize();
								}
							}
						}
					}
				}
				map.put("haveFile", haveFile);
				map.put("fileSize", fileSize);
			}
		}
		PageUtil p = new PageUtil(count, page, size);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("page", p);
		m.put("list", kcl);
		return m;
	}

	@Override
	public List<Node> queryColumns(long cid, long userId) {
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.queryColumns:{}",
				cid);
		Column c = columnMapper.selectByPrimaryKey(cid);
		try {
			List<Column> cl = columnValueMapper.selectColumnTreeBySortId(
					userId, c.getColumnLevelPath());
			if (cl != null && cl.size() > 0) {
				return Branch.build(
						ConvertUtil.convert2Node(cl, "userId", "id",
								"columnname", "parentId", "columnLevelPath"),
						cid, c.getParentId()).getList();
			}
		} catch (Exception e) {
			logger.error("无法获取2,3级栏目数据knowledgeHomeServiceImpl.queryColumns()");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int beRelation(long id, int t, long cid, Long userId) {
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.beRelation:{}",
				cid);
		UserPermissionExample example = new UserPermissionExample();
		example.createCriteria().andKnowledgeIdEqualTo(id)
				.andSendUserIdEqualTo(userId);
		int count = countKnowledgeByMongo(id, t, cid, userId);
		if (count > 0) {
			return Constants.Relation.self.v();// 自己
		}
		example = new UserPermissionExample();
		example.createCriteria().andKnowledgeIdEqualTo(id)
				.andReceiveUserIdEqualTo(-1l);
		count = userPermissionMapper.countByExample(example);
		if (count > 0) {
			return Constants.Relation.platform.v();// 全平台
		}

		Knowledge knowledge = knowledgeReaderService.getKnowledgeById(id, t
				+ "");
		if (knowledge != null) {
			return Constants.Relation.jinTN.v();// gt
		}
		return Constants.Relation.friends.v();// 好友可见
	}

	private int countKnowledgeByMongo(long id, int t, long cid, long userId) {
		String[] names = Constants.Type.values()[t - 1].obj().split("\\.");
		int length = names.length;
		Criteria criteria = Criteria.where("status").is(4);
		criteria.and("cid").is(userId).and("_id").is(id);
		Query query = new Query(criteria);
		long count = mongoTemplate.count(query, names[length - 1]);
		return (int) count;
	}

	@Override
	public List<KnowledgeStatics> getRankHotList(Long column) {
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.getRankHotList:{}",
				column);
		Column c = columnMapper.selectByPrimaryKey(column);
		if (c != null) {
			String clp = c.getColumnLevelPath().substring(0, 9);
			byte type = (byte) Integer.parseInt(clp);
			KnowledgeStaticsExample ce = new KnowledgeStaticsExample();
			ce.createCriteria().andSourceEqualTo((short) 0)
					.andTypeEqualTo((short) type);
			ce.setLimitStart(0);
			ce.setLimitEnd(10);
			ce.setOrderByClause("(shareCount * 0.1 + commentcount * 0.2 + collectioncount * 0.3 + clickcount * 0.4)/4 DESC");
			return knowledgeStaticsMapper.selectByExample(ce);
		}
		return null;
	}
}
