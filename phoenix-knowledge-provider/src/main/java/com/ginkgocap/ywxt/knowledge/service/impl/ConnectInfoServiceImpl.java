package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ginkgocap.ywxt.knowledge.entity.ConnectionInfo;
import com.ginkgocap.ywxt.knowledge.entity.ConnectionInfoExample;
import com.ginkgocap.ywxt.knowledge.entity.ConnectionInfoExample.Criteria;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.mapper.ConnectionInfoMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ConnectionInfoValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeBaseMapper;
import com.ginkgocap.ywxt.knowledge.service.ConnectInfoService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeHomeService;
import com.ginkgocap.ywxt.people.domain.modelnew.PeopleName;
import com.ginkgocap.ywxt.people.domain.modelnew.PeopleSimple;
import com.ginkgocap.ywxt.people.domain.modelnew.PeopleTemp;
import com.ginkgocap.ywxt.people.service.PeopleMongoService;
import com.ginkgocap.ywxt.personalcustomer.service.PersonalCustomerService;
import com.ginkgocap.ywxt.requirement.model.Requirement;
import com.ginkgocap.ywxt.requirement.service.RequirementService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.FriendsRelationService;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("connectInfoService")
public class ConnectInfoServiceImpl implements ConnectInfoService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private KnowledgeBaseMapper knowledgeBaseMapper;
	@Autowired
	private ConnectionInfoMapper connectInfoMapper;
	@Resource
	private PeopleMongoService peopleMongoService;

	@Resource
	private FriendsRelationService friendsRelationService;
	@Resource
	private KnowledgeHomeService knowledgeHomeService;

	@Resource
	private PersonalCustomerService personalCustomerService;

	@Resource
	private ConnectionInfoValueMapper connectionInfoValueMapper;

	@Resource
	private RequirementService requirementService;

	@Override
	public Map<String, Object> findConnectInfo(Long kid, Integer connType,
			String tag, String pType, Integer pOff, int page, int size) {
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.ConnectInfoServiceImpl.findConnectInfo:{},",
				kid);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.ConnectInfoServiceImpl.findConnectInfo:{},",
				connType);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.ConnectInfoServiceImpl.findConnectInfo:{},",
				tag);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.ConnectInfoServiceImpl.findConnectInfo:{},",
				page);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.ConnectInfoServiceImpl.findConnectInfo:{},",
				size);
		Map<String, Object> m = new HashMap<String, Object>();
		if (connType != null && connType > 0) {
			List<String> tags = connectionInfoValueMapper.selectTags(kid,
					connType);
			if ("".equals(tag)) {//默认筛第一个标签
				Object tagMap= tags.get(0);
				Map<String,Object> tm =(Map<String,Object>)tagMap;
				tag =(String) tm.get("name");
			}
			m.put("tags", tags);
			List<ConnectionInfo> l = new ArrayList<ConnectionInfo>();
			ConnectionInfoExample example = buildCriteria(
					new ConnectionInfoExample(), kid, connType, tag, 0, page,
					size);
			ConnectionInfo c = new ConnectionInfo();
			l = getResultList(example, page, size);
			Long userid = 0l;
			if (l != null && l.size() > 0) {// 1.按照tag筛选时，先判断是否为-1
				c = l.get(0);
				KnowledgeBase kb = knowledgeBaseMapper.selectByPrimaryKey(c
						.getKnowledgeid());
				if (kb != null && kb.getUserId() != null) {
					userid = kb.getUserId();
				}
			}
			int allasso = 0;
			if (c.getAllasso() != null && c.getAllasso() == -1) {
				allasso = -1;
			}
			example.clear();

			if (!tag.equals("") && allasso == -1) {// 2.按照tag筛选时，为-1，走接口
				m = getMapForAllasso(connType, tag, userid, pType, pOff, page,
						size);
			} else {
				example = buildCriteria(example, kid, connType, tag, 0, page,
						size);
				m = getResultMap(example, page, size);
			}
		} else {
			List<ConnectionInfo> kcl = new ArrayList<ConnectionInfo>();
			kcl = getList(kid, 0l, page, size);
			m.put("page", "");
			m.put("list", kcl);
		}
		return m;
	}

	private ConnectionInfoExample buildCriteria(ConnectionInfoExample example,
			Long kid, Integer connType, String tag, int allasso, int page,
			int size) {
		example.clear();
		int start = (page - 1) * size;
		if ("".equals(tag)) {
			if (allasso == -1) {
				example.createCriteria().andKnowledgeidEqualTo(kid)
						.andConntypeEqualTo(connType)
						.andAllassoEqualTo(allasso);
			} else {
				example.createCriteria().andKnowledgeidEqualTo(kid)
						.andConntypeEqualTo(connType);
			}
		} else {
			if (allasso == -1) {
				example.createCriteria().andKnowledgeidEqualTo(kid)
						.andConntypeEqualTo(connType)
						.andAllassoEqualTo(allasso).andTagEqualTo(tag);
			} else {
				example.createCriteria().andKnowledgeidEqualTo(kid)
						.andConntypeEqualTo(connType).andTagEqualTo(tag);
			}
		}
		example.setOrderByClause("connName ");
		example.setLimitStart(start);
		example.setLimitEnd(size);
		return example;
	}

	private Map<String, Object> getMapForAllasso(int connType, String tag,
			Long userid, String pType, int pOff, int page, int size) {
		Map<String, Object> lc = new HashMap<String, Object>();
		int start = (page - 1) * size;
		if (connType == 2) {
			if (!"".equals(pType)) {
				List<User> list = friendsRelationService.findAllFriends(userid,
						0, "", "", start + pOff, size); // 好友
				lc = convertPFToConnectionInfoMap(list);
				lc.put("pType", "hy");
				lc.put("pOff", pOff);
			} else {
				List<PeopleSimple> peoples = peopleMongoService
						.getPeopleSimplelist(0, null, null, page, size, userid,
								1); // 人脉
				int resize = peoples.size();
				lc = convertPToConnectionInfoMap(peoples);
				if (resize != size) {
					Map<String, Object> lct = new HashMap<String, Object>();
					List<User> list = friendsRelationService.findAllFriends(
							userid, 0, "", "", 0, size - resize); // 好友
					lct = convertPFToConnectionInfoMap(list);
					lc = addAll(lc, lct);
					lc.put("pType", "hy");
					lc.put("pOff", size - resize);
				}
			}
		} else if (connType == 1) {
			Map<String, Object> events = requirementService.selectMy(userid,
					start, size, -1, ""); // 事件
			lc = convertEToConnectionInfoMap(events);
		} else if (connType == 5) {
			List<Map<String, Object>> orgs = personalCustomerService.list(
					userid, "", "", "", "", 0, start, size);// 组织
			lc = convertOToConnectionInfoMap(orgs);
		} else if (connType == 6) {
			Map<String, Object> knowledges = knowledgeHomeService
					.selectAllKnowledgeCategoryByParam("", "", 0, "", userid,
							"", page, size);// 知识
			lc = convertKToConnectionInfoMap(knowledges);
		}
		return lc;
	}

	private Map<String, Object> addAll(Map<String, Object> lc,
			Map<String, Object> lct) {
		Map<String, Object> cml = new HashMap<String, Object>();
		lc.get("list");
		List<ConnectionInfo> cl1 = (List<ConnectionInfo>) lc.get("list");
		lct.get("list");
		List<ConnectionInfo> cl2 = (List<ConnectionInfo>) lct.get("list");
		cl1.addAll(cl2);
		cml.put("page", "");
		cml.put("list", cl1);
		return cml;
	}

	private Map<String, Object> convertPFToConnectionInfoMap(List<User> list) {
		Map<String, Object> cml = new HashMap<String, Object>();
		List<ConnectionInfo> cl = new ArrayList<ConnectionInfo>();
		for (User u : list) {
			ConnectionInfo c = new ConnectionInfo();
			c.setConnid(u.getId());
			c.setConnname(u.getName());
			c.setUrl("/member/view/?id=" + u.getId());
			c.setPicpath(u.getPicPath());
			cl.add(c);
		}
		cml.put("page", "");
		cml.put("list", cl);
		return cml;
	}

	private Map<String, Object> convertPToConnectionInfoMap(
			List<PeopleSimple> peoples) {
		Map<String, Object> cml = new HashMap<String, Object>();
		List<ConnectionInfo> cl = new ArrayList<ConnectionInfo>();
		for (PeopleSimple p : peoples) {
			ConnectionInfo c = new ConnectionInfo();
			c.setPicpath(p.getPortrait());
			c.setConnid(Long.parseLong(p.getId()));
			List<PeopleName> lpn = p.getPeopleNameList();
			for (PeopleName pt : lpn) {
				c.setConnname(pt.getName());
				break;
			}
			c.setUrl("/people/" + Long.parseLong(p.getId()) + "/");
			cl.add(c);
		}
		cml.put("page", "");
		cml.put("list", cl);
		return cml;
	}

	private Map<String, Object> convertEToConnectionInfoMap(
			Map<String, Object> events) {
		Map<String, Object> cml = new HashMap<String, Object>();
		List<ConnectionInfo> cl = new ArrayList<ConnectionInfo>();
		List<Requirement> el = (List<Requirement>) events.get("results");
		for (Requirement p : el) {
			ConnectionInfo c = new ConnectionInfo();
			c.setPicpath("");
			c.setConnid(p.getId());
			c.setConnname(p.getTitle());
			c.setUrl("/requirement/detail/" + p.getRequirementType() + "/"
					+ p.getId() + "/");
			cl.add(c);
		}
		cml.put("page", "");
		cml.put("list", cl);
		return cml;
	}

	private Map<String, Object> convertOToConnectionInfoMap(
			List<Map<String, Object>> orgs) {
		Map<String, Object> cml = new HashMap<String, Object>();
		List<ConnectionInfo> cl = new ArrayList<ConnectionInfo>();
		for (Map<String, Object> p : orgs) {
			ConnectionInfo c = new ConnectionInfo();
			c.setPicpath("/corporation/avatar/user/image/?orgid=" + p.get("id"));
			c.setConnid((Long) p.get("id"));
			c.setConnname((String) p.get("name"));
			c.setUrl("");
			cl.add(c);
		}
		cml.put("page", "");
		cml.put("list", cl);
		return cml;
	}

	private Map<String, Object> convertKToConnectionInfoMap(
			Map<String, Object> kmap) {
		Map<String, Object> cml = new HashMap<String, Object>();
		List<ConnectionInfo> cl = new ArrayList<ConnectionInfo>();
		List<Map<String, Object>> rl = (List<Map<String, Object>>) kmap
				.get("list");
		for (Map<String, Object> k : rl) {
			ConnectionInfo c = new ConnectionInfo();
			c.setConnid((Long) k.get("knowledge_id"));
			c.setConnname((String) k.get("title"));
			c.setUrl((String) k.get("/knowledge/reader?type="
					+ k.get("column_type") + "&kid=" + k.get("knowledge_id")));
			c.setPicpath((String) k.get("pic_path"));
			cl.add(c);
		}
		cml.put("page", kmap.get("page"));
		cml.put("list", cl);
		return cml;
	}

	private Map<String, Object> getResultMap(ConnectionInfoExample example,
			int page, int size) {
		int count = connectInfoMapper.countByExample(example);
		List<ConnectionInfo> kcl = connectInfoMapper.selectByExample(example);
		PageUtil p = new PageUtil(count, page, size);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("page", p);
		m.put("list", kcl);
		return m;
	}

	private List<ConnectionInfo> getResultList(ConnectionInfoExample example,
			int page, int size) {
		List<ConnectionInfo> kcl = connectInfoMapper.selectByExample(example);
		return kcl;
	}

	private List<ConnectionInfo> getList(Long kid, long userid, int page,
			int size) {
		List<ConnectionInfo> k = getListByParam(kid, 1, userid, page, size);
		k.addAll(getListByParam(kid, 2, userid, page, size));
		k.addAll(getListByParam(kid, 5, userid, page, size));
		k.addAll(getListByParam(kid, 6, userid, page, size));
		return k;
	}

	private List<ConnectionInfo> getListByParam(Long kid, int connType,
			long userid, int page, int size) {
		int start = (page - 1) * size;
		ConnectionInfoExample example = new ConnectionInfoExample();
		example.createCriteria().andKnowledgeidEqualTo(kid)
				.andConntypeEqualTo(connType);
		example.setOrderByClause("connName ");
		example.setLimitStart(start);
		example.setLimitEnd(size);
		List<ConnectionInfo> cl = connectInfoMapper.selectByExample(example);
		// 插入6条
		// if (cl.size() <= 5) {
		// if (connType == 1) {// 知识
		// Map<String, Object> knowledges = knowledgeHomeService
		// .selectAllKnowledgeCategoryByParam("", "", 0, "",
		// userid, "", page, size);
		// List<ConnectionInfo> clo = (List<ConnectionInfo>)
		// convertKToConnectionInfoMap(
		// knowledges).get("list");
		// cl.addAll(clo);
		// } else if (connType == 2) {
		// //
		// }
		// }
		return cl;

	}

}
