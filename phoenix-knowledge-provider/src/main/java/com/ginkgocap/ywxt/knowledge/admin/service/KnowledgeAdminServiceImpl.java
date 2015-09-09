/**
 * 
 */
package com.ginkgocap.ywxt.knowledge.admin.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeAdminDao;
import com.ginkgocap.ywxt.knowledge.dao.news.KnowledgeNewsDAO;
import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategoryExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategoryExample.Criteria;
import com.ginkgocap.ywxt.knowledge.entity.UserCategory;
import com.ginkgocap.ywxt.knowledge.mapper.AdminUserCategoryValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeBaseMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCategoryMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserCategoryMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserCategoryValueMapper;
import com.ginkgocap.ywxt.knowledge.model.AdminUserCategory;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeMongoIncService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.ginkgocap.ywxt.knowledge.service.UserCategoryService;
import com.ginkgocap.ywxt.knowledge.service.impl.CategoryHelper;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.DateUtil;
import com.ginkgocap.ywxt.knowledge.util.HtmlToText;
import com.ginkgocap.ywxt.knowledge.util.tree.ConvertUtil;
import com.ginkgocap.ywxt.knowledge.util.tree.Tree;
import com.ginkgocap.ywxt.user.form.DataGridModel;
import com.ginkgocap.ywxt.user.model.User;

/**
 * @author liubang
 *
 */
@Service("knowledgeAdminService")
public class KnowledgeAdminServiceImpl implements KnowledgeAdminService {
	
	
	private static final Logger logger = LoggerFactory
			.getLogger(KnowledgeAdminServiceImpl.class);
	private CategoryHelper helper = new CategoryHelper();
	@Resource
	private KnowledgeAdminDao knowledgeAdminDao;
	@Resource
	private KnowledgeCategoryMapper knowledgeCategoryMapper;
	@Resource
	private KnowledgeMongoIncService knowledgeMongoIncService;
	@Resource
	private ColumnService columnService;
	@Resource
	private KnowledgeNewsDAO knowledgeNewsDAO;
	@Resource
	private AdminUserCategoryValueMapper adminUserCategoryValueMapper;
	@Resource
	private UserCategoryService userCategoryService;
	@Resource
	private UserCategoryMapper userCategoryMapper;
	@Resource
	private UserCategoryValueMapper userCategoryValueMapper;
	@Resource
	private MongoTemplate mongoTemplate;
	@Resource
	private KnowledgeBaseMapper knowledgeBaseMapper;
	@Resource
	private KnowledgeService knowledgeService;
	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.service.KnowledgeAdminService#selectKnowledgeNewsList(java.lang.Integer, java.lang.Integer, java.util.Map)
	 */
	@Override
	public Map<String, Object> selectByParam(DataGridModel dgm, Map<String, String> map) {
		return knowledgeAdminDao.selectKnowledgeNewsList(dgm.getPage(), dgm.getRows(), map);
	}

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.service.KnowledgeAdminService#selectKnowledgeNewsById(long, java.lang.String)
	 */
	@Override
	public Object selectById(long id, String collectionName) {
		return knowledgeAdminDao.selectKnowledgeNewsById(id, collectionName);
	}


	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.service.KnowledgeAdminService#checkStatusById(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void checkStatusById(long id, int status, String collectionNames) {
		knowledgeAdminDao.checkStatusById(id, status, collectionNames);
		if(status==4){//通过
			KnowledgeCategory knowledgecategory = new KnowledgeCategory();
			knowledgecategory.setStatus(Constants.ReportStatus.report.v() + "");
			KnowledgeCategoryExample example = new KnowledgeCategoryExample();
			Criteria criteria = example.createCriteria();
			criteria.andKnowledgeIdEqualTo(id);
			knowledgeCategoryMapper.updateByExampleSelective(
					knowledgecategory, example);
		}else{
			KnowledgeCategory knowledgecategory = new KnowledgeCategory();
			knowledgecategory.setStatus(Constants.ReportStatus.unreport.v() + "");
			KnowledgeCategoryExample example = new KnowledgeCategoryExample();
			Criteria criteria = example.createCriteria();
			criteria.andKnowledgeIdEqualTo(id);
			knowledgeCategoryMapper.updateByExampleSelective(
					knowledgecategory, example);
		}
		
	}

	@Override
	public void update(long id, String title, String cpathid, String content,String desc,
			String tags, String collectionName) {
		knowledgeAdminDao.update(id,title, cpathid, content,desc,
				tags, collectionName);
		
	}

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.service.KnowledgeAdminService#addNews(com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO)
	 * Administrator
	 */
	@Override
	public Map<String,Object> addNews(KnowledgeNewsVO vo,User user) {
		
		Map<String,Object> result = new HashMap<String, Object>();
				// 获取Session用户值
				long userId = user.getId();
				long kId = knowledgeMongoIncService.getKnowledgeIncreaseId();
				String columnid = StringUtils.isBlank(vo.getColumnid()) ? "0" : vo.getColumnid();
				// 判断用户是否选择栏目
				String columnPath = null;
				Column column = null;
				if (Long.parseLong(columnid) != 0) {
					columnPath = columnService.getColumnPathById(Long.parseLong(columnid));
				} else {
					column = columnService.getUnGroupColumnIdBySortId(user.getId());
					if (column == null) {
						// 没有未没分组栏目，添加
						columnService.checkNogroup(userId);
					} else {
						columnid = column.getId() + "";
					}
					columnPath = Constants.unGroupSortName;
				}
				setVO(vo, kId, columnid, columnPath);
				knowledgeNewsDAO.insertknowledge(vo, user);
				// 添加知识到目录知识表
				result = knowledgeService.insertCatalogueIds(vo, user);
				Integer status = Integer.parseInt(result.get(Constants.status) + "");
				if (status != 1) {
					result.put(Constants.errormessage,
							Constants.ErrorMessage.addKnowledgeCatalogueIds.c());
					return result;
				}
				result.put("result", "success");
				return result;
	}

	/**
	 * @param vo
	 * @param kId
	 * @param columnid
	 * @param columnPath
	 */
	private void setVO(KnowledgeNewsVO vo, long kId, String columnid,String columnPath) {
		vo.setAsso("");
		vo.setkId(kId);
		vo.setColumnPath(columnPath);
		vo.setColumnid(columnid);
		vo.setDesc(getDesc(vo.getColumnType(),vo));
		vo.setSelectedIds("\"dule\":true,\"xiaoles\":[],\"zhongles\":[],\"dales\":[]");
		vo.setCreatetime(DateUtil.formatWithYYYYMMDDHHMMSS(new Date()));
		vo.setEssence("0");
		vo.setKnowledgestatus(Constants.Status.checked.v());
	}
	
	/**
	 *  如果是行业，投融工具，经典案例，不从内容取简介
	 * @param columnType
	 * @param vo
	 * @return
	 */
	public String getDesc(String columnType, KnowledgeNewsVO vo){
		
		String content="";
		if (Integer.parseInt(columnType) == Constants.Type.Investment.v()
				|| Integer.parseInt(columnType) == Constants.Type.Industry.v()
				|| Integer.parseInt(columnType) == Constants.Type.Case.v()) {
			if (StringUtils.isNotBlank(vo.getDesc())) {
				 content = HtmlToText.html2Text(vo.getDesc());
				if (StringUtils.isNotBlank(content)) {
					content = content.length() > 50 ? content.substring(0, 50)+ "..." : content;
				}
			} else {
				 content = HtmlToText.html2Text(vo.getContent());
				if (StringUtils.isNotBlank(content)) {
					content = content.length() > 50 ? content.substring(0, 50)+ "..." : content;
				}
			}
		} else {
			if (StringUtils.isBlank(vo.getDesc())) {
				 content = HtmlToText.html2Text(vo.getContent());
				if (StringUtils.isNotBlank(content)) {
					content = content.length() > 50 ? content.substring(0, 50)+ "..." : content;
				}
			}
		}
		return content;
	}

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.service.KnowledgeAdminService#listUserCategory(long, java.lang.String, java.lang.Byte)
	 * Administrator
	 */
	@Override
	public String listUserCategory(long userId, String sortId,Byte type) {
		
		List<AdminUserCategory> cl = adminUserCategoryValueMapper.selectChildBySortId(userId, sortId, type);
		if (cl != null && cl.size() > 0) {
			return JSONArray.fromObject(
					Tree.buildAdmin(ConvertUtil.convert2AdminNode(cl, "userId", "id",
							"categoryname", "parentId", "sortid","usetype","desc","createName"))).toString();
		}
		return "";
	}

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.service.KnowledgeAdminService#insert(com.ginkgocap.ywxt.knowledge.model.AdminUserCategory)
	 * Administrator
	 */
	@Override
	public String insert(AdminUserCategory category) {
	
		// 得到要添加的分类的父类parentId
				try {
					long userid = category.getUserId();
					long parentId = category.getParentId();
					String cname = category.getCategoryname();
					Short ct = category.getCategoryType();
					List<UserCategory> lu = userCategoryService.selectUserCategoryByParams(userid,parentId, ct, cname);
					if (lu != null && lu.size() > 0) {
						return "false";
					}
					// 得到要添加的分类的父类sortId
					String parentSortId = parentId > 0 ? userCategoryMapper.selectByPrimaryKey(parentId).getSortid() : "";
					// 通过parentSortId得到子类最大已添加的sortId
					String childMaxSortId = userCategoryValueMapper.selectMaxSortId(category.getUserId(), parentSortId,category.getCategoryType());
					if (StringUtils.isBlank(category.getSortid())) {
						// 如果用户第一次添加，将childMaxSortId赋值
						String newSortId = new String("");
						if (childMaxSortId == null || "null".equals(childMaxSortId)|| "".equals(childMaxSortId)) {newSortId = parentSortId + "000000001";
						} else {
							newSortId = helper.generateSortId(childMaxSortId);
						}
						// 通过已添加的最大的SortId生成新的SortId
						// 设置最新的sortId
						category.setSortid(newSortId);
					}
					// 返回存入的对象
					adminUserCategoryValueMapper.insertUserCategory(category);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "success";
	}
	
	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.service.AdminUserCategoryService#update(com.ginkgocap.ywxt.knowledge.model.AdminUserCategory)
	 * Administrator
	 */
	@Override
	public void update(AdminUserCategory category) {
		
		adminUserCategoryValueMapper.updateByPrimaryKey(category);
		
	}

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.service.AdminUserCategoryService#selectByPrimaryKey(long)
	 * Administrator
	 */
	@Override
	public AdminUserCategory selectByPrimaryKey(long id) {
		
		return adminUserCategoryValueMapper.selectByPrimaryKey(id);
	}

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.service.KnowledgeAdminService#updateUseType(long, int)
	 * Administrator
	 */
	@Override
	public void updateUseType(long id, int userType) {
		
		  UserCategory u = userCategoryMapper.selectByPrimaryKey(id);
		 if(u !=null){
			 adminUserCategoryValueMapper.updateUseType(u.getUserId(), u.getCategoryType(),u.getSortid(),userType);
		 }
		
	}

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.admin.service.KnowledgeAdminService#batchDeleteKnowledge(java.lang.String, java.lang.String)
	 * Administrator
	 */
	@Override
	public Map<String, Object> batchDeleteKnowledge(String knowledgeids) {
		
		return knowledgeAdminDao.batchDeleteKnowledge(knowledgeids);
	}
}
