package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledgecollection.KnowledgeCollectionDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollection;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollectionExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollectionExample.Criteria;
import com.ginkgocap.ywxt.knowledge.entity.UserCategory;
import com.ginkgocap.ywxt.knowledge.entity.UserCategoryExample;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeBaseMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCollectionMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCollectionMapperManual;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCollectionValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserCategoryMapper;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBaseVO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollectionVO;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCollectionService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.util.MongoUtils;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("knowledgeCollectionService")
public class KnowledgeCollectionServiceImpl implements
		KnowledgeCollectionService {

	private static final Logger logger = LoggerFactory
			.getLogger(KnowledgeCollectionServiceImpl.class);
	@Autowired
	private KnowledgeCollectionDAO knowledgeCollectionDAO;
	@Resource
	private KnowledgeCollectionValueMapper knowledgeCollectionValueMapper;
	@Resource
	private UserCategoryMapper userCategoryMapper;
	@Resource
	private MongoTemplate mongoTemplate;
	@Resource
	private KnowledgeCollectionMapperManual knowledgeCollectionMapperManual;
	@Resource
	private KnowledgeBaseMapper knowledgeBaseMapper;
	@Resource
	private KnowledgeCollectionMapper knowledgeCollectionMapper;
	@Resource
	private KnowledgeCategoryService knowledgeCategoryService;


	@Override
	public Map<String, Object> insertKnowledgeCollection(long kid,
			long columnid, String type, String source, long categoryid) {
		Map<String, Object> result = new HashMap<String, Object>();
		int collectSource = 0;//knowledge's six sort of source
		try{
		    collectSource =Integer.parseInt(source);
		}catch(NumberFormatException e){
		    e.printStackTrace();
		}
		
		if (knowledgeCollectionDAO.isExsitInCollection(kid, categoryid)) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.alreadyCollection.c());
			return result;
		}

		KnowledgeCollection coll = new KnowledgeCollection();
		coll.setKnowledgeId(kid);
		coll.setSource(collectSource);
		coll.setCreatetime(new Date());
		coll.setCategoryId(categoryid);
		int v = knowledgeCollectionDAO.insertKnowledgeCollection(coll);
		if (v == 0) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.addCollFail.c());
		} else {
			result.put(Constants.status, Constants.ResultType.success.v());
		}
		return result;

	}

	@Override
	public int deleteKnowledgeCollection(long[] knowledgeids, long categoryid) {

		return knowledgeCollectionDAO.deleteKnowledgeCollection(knowledgeids,
				categoryid);
	}

	@Override
	public List<Long> selectKnowledgeCollection(long column_id,
			String knowledgeType, long category_id, int pageno, int pagesize) {

		return knowledgeCollectionDAO.selectKnowledgeCollection(column_id,
				knowledgeType, category_id, pageno, pagesize);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List selectKnowledgeAll(String source, String knowledgeType,
			long collectionUserId, int pageno, int pagesize) {
		// return knowledgeCollectionValueMapper.selectKnowledgeAll(source,
		// knowledgeType, collectionUserId, pageno, pagesize);
		return null;
	}

	@Override
	public long countKnowledgeAll(String source, String knowledgeType,
			long collectionUserId) {
		// return knowledgeCollectionValueMapper.countKnowledgeAll(source,
		// knowledgeType, collectionUserId);
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryKnowledgeAll(String source,
			String knowledgeType, long collectionUserId, String sortId,
			String keyword, int pageno, int pagesize) {
		Integer start = (pageno - 1) * pagesize;
		Map<String, Object> m = new HashMap<String, Object>();
		long count = knowledgeCollectionValueMapper.countKnowledgeAll(source,
				knowledgeType, collectionUserId, sortId, keyword);
		List<Map<String, Object>> list = knowledgeCollectionValueMapper
				.selectKnowledgeAll(source, knowledgeType, collectionUserId,
						sortId, keyword, start, pagesize);
		PageUtil p = new PageUtil((int) count, pageno, pagesize);
		m.put("page", p);
		m.put("list", list);
		return m;
	}

	@Override
	public Map<String, Object> insertKnowledgeCollection(
			KnowledgeCollectionVO vo, User user) {
		logger.info("进入添加知识收藏,知识ID:{},用户ID:{}", vo.getkId(), user.getId());
		Map<String, Object> result = new HashMap<String, Object>();

		try {

			List<KnowledgeCollection> list = new ArrayList<KnowledgeCollection>();
			MongoUtils util = new MongoUtils();
			String c = util.getTableName(vo.getColumType());
			if (StringUtils.isBlank(c))
				return null;
			Knowledge knowledge = (Knowledge) mongoTemplate.findById(
					vo.getkId(), Class.forName(c), util.getCollectionName(c));

			// 判断是否已收藏过该文章，若已收藏过，删除全部收藏
			if (isCollection(user.getId(), vo.getkId())) {
				// 删除
				if (delCollection(user.getId(), vo.getkId())) {
					logger.error("删除已收藏数据失败!,知识ID:{},用户ID:{}", vo.getkId(),
							user.getId());
					result.put(Constants.status, Constants.ResultType.fail.v());
					result.put(Constants.errormessage,
							Constants.ErrorMessage.addCollFail.c());
					return result;
				}
			}
			// 格式化目录
			long[] cIds = knowledgeCategoryService.getCurrentCategoryArray(
					vo.getCategoryIds(), Constants.CategoryType.collection.v(),
					user.getId());
			// 组装收藏目录ID集合
			KnowledgeCollection kc = null;
			for (long cId : cIds) {
				kc = new KnowledgeCollection();
				kc.setCategoryId(cId);
				kc.setCollectionComment(vo.getComment());
				kc.setKnowledgeId(vo.getkId());
				kc.setCreatetime(new Date());
				kc.setCollectionTags(vo.getTags());
				kc.setSource(getSource(user.getId(), knowledge.getUid()));
				kc.setUserid(user.getId());
				list.add(kc);
			}
			// 添加到收藏表
			int cV = knowledgeCollectionMapperManual
					.batchInsertCollection(list);
			if (cV == 0) {
				logger.error("添加知识收藏表失败!,知识ID:{},用户ID:{}", vo.getkId(),
						user.getId());
				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage,
						Constants.ErrorMessage.addCollFail.c());
				return result;
			}
			// 添加基本信息表
			KnowledgeBaseVO bVo = new KnowledgeBaseVO();
			bVo.setColumType(vo.getColumType());
			bVo.setKnowledge(knowledge);
			if (!addBaseInfo(bVo, user)) {
				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage,
						Constants.ErrorMessage.addCollFail.c());
				return result;
			}

			result.put(Constants.status, Constants.ResultType.success.v());

		} catch (ClassNotFoundException e) {
			logger.error("添加知识收藏异常!没有根据类型:" + vo.getColumType()
					+ "找到对应类,知识ID:{},用户ID:{}", vo.getkId(), user.getId());
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.addCollFail.c());
			return result;
		}
		return result;
	}

	private Integer getSource(long loginUserId, long uId) {
		Integer relation = null;
		if (uId == loginUserId) {
			relation = Constants.Relation.self.v();
		} else if (uId == -1) {
			relation = Constants.Relation.platform.v();
		} else if (uId == 0) {
			relation = Constants.Relation.jinTN.v();
		} else {
			relation = Constants.Relation.friends.v();
		}
		return relation;
	}

	@Override
	public boolean isCollection(long userId, long kId) {
		KnowledgeCollectionExample example = new KnowledgeCollectionExample();
		Criteria criteria = example.createCriteria();
		criteria.andUseridEqualTo(userId);
		criteria.andKnowledgeIdEqualTo(kId);

		int v = knowledgeCollectionMapper.countByExample(example);
		return v > 0;
	}

	@Override
	public boolean delCollection(long userId, long kId) {
		KnowledgeCollectionExample example = new KnowledgeCollectionExample();
		Criteria criteria = example.createCriteria();
		criteria.andUseridEqualTo(userId);
		criteria.andKnowledgeIdEqualTo(kId);

		int v = knowledgeCollectionMapper.deleteByExample(example);

		return v > 0;

	}

	@Override
	public boolean addBaseInfo(KnowledgeBaseVO vo, User user) {
		logger.error("进入添加知识基本表!,知识ID:{},用户ID:{}", vo.getKnowledge().getId(),
				user.getId());
		if (vo.getKnowledge() == null) {
			logger.error("知识对象为空!,知识ID:{},用户ID:{}", vo.getKnowledge().getId(),
					user.getId());
			return false;
		}
		int bV = 1;
		// 查询知识在base表中是否存在
		KnowledgeBase b = knowledgeBaseMapper.selectByPrimaryKey(vo
				.getKnowledge().getId());
		if (b == null) {
			// 添加基本表
			KnowledgeBase knowledgeBase = new KnowledgeBase();
			knowledgeBase.setKnowledgeId(vo.getKnowledge().getId());
			knowledgeBase.setAuthor(user.getName());
			knowledgeBase
					.setcDesc(vo.getKnowledge().getContent().length() > 50 ? vo
							.getKnowledge().getContent().substring(0, 50) : vo
							.getKnowledge().getContent());
			knowledgeBase.setColumnId(Long.parseLong(vo.getKnowledge()
					.getColumnid()));
			knowledgeBase.setColumnType(Short.parseShort(vo.getColumType()));
			knowledgeBase.setEssence((short) vo.getKnowledge().getEssence());
			knowledgeBase.setCreatetime(vo.getKnowledge().getCreatetime());
			knowledgeBase.setPath(vo.getKnowledge().getCpathid());
			knowledgeBase.setPicPath(vo.getKnowledge().getPic());
			knowledgeBase.setTag(vo.getKnowledge().getTags());
			knowledgeBase.setTitle(vo.getKnowledge().getTitle());
			knowledgeBase.setUserId(user.getId());

			bV = knowledgeBaseMapper.insertSelective(knowledgeBase);
			if (bV == 0) {
				logger.error("添加知识基本表失败!,知识ID:{},用户ID:{}", vo.getKnowledge()
						.getId(), user.getId());
			}
		}
		return bV > 0;
	}

	@Override
	public List<KnowledgeCollection> queryCollCategoryIds(long userId, long kId) {
		KnowledgeCollectionExample example = new KnowledgeCollectionExample();
		Criteria criteria = example.createCriteria();
		criteria.andUseridEqualTo(userId);
		criteria.andKnowledgeIdEqualTo(kId);

		return knowledgeCollectionMapper.selectByExample(example);
	}

}
