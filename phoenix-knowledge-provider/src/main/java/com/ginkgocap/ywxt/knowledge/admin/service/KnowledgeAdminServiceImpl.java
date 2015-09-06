/**
 * 
 */
package com.ginkgocap.ywxt.knowledge.admin.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeAdminDao;
import com.ginkgocap.ywxt.knowledge.dao.news.KnowledgeNewsDAO;
import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategoryExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategoryExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.AdminUserCategoryValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCategoryMapper;
import com.ginkgocap.ywxt.knowledge.model.AdminUserCategory;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeMongoIncService;
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
	public void addNews(KnowledgeNewsVO vo,User user) {
		
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
}
