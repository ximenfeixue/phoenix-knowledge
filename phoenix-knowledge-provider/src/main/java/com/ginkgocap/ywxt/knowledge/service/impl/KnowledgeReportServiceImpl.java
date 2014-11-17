package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeReport;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeReportExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeReportExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeReportMapper;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeReportService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.DateUtil;
import com.ginkgocap.ywxt.user.form.DataGridModel;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("knowledgeReportService")
public class KnowledgeReportServiceImpl implements KnowledgeReportService {

	@Resource
	private KnowledgeReportMapper knowledgeReportMapper;
	@Resource
	private UserService userService;

	@Override
	public Map<String, Object> addReport(long kid, String type, String desc,
			User user, String title, String columnType) {
		Map<String, Object> result = new HashMap<String, Object>();
		KnowledgeReport report = new KnowledgeReport();
		report.setKnowledgeId(kid);
		report.setType(type);
		report.setRepDesc(desc);
		report.setUserId(user.getId());
		report.setCreatetime(new Date());
		report.setUsername(user.getName());
		report.setKnowledgetitle(title);
		report.setUpdatetime(new Date());
		report.setResults("");
		report.setStatus(Constants.ReportResolveStatus.unresolve.v());
		report.setColumntype(Short.parseShort(columnType));
		int v = knowledgeReportMapper.insertSelective(report);
		if (v == 0) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.addReportFail.c());
		} else {
			result.put(Constants.status, Constants.ResultType.success.v());
		}
		return result;
	}

	@Override
	public Map<String, Object> selectByParam(DataGridModel dgm,
			Map<String, String> searchMap) {
		Map<String, Object> result = new HashMap<String, Object>();
		KnowledgeReportExample example = new KnowledgeReportExample();
		int page = dgm.getPage();
		int row = dgm.getRows();
		Criteria criteria = example.createCriteria();
		String cname = searchMap.get("cname");
		String title = searchMap.get("title");
		Date submitBeginCTime = DateUtil.parseWithYYYYMMDDHHMMSS(StringUtils
				.isNotBlank(searchMap.get("submitBeginCTime")) ? searchMap
				.get("submitBeginCTime") + " 00:00:00" : "");
		Date submitEndCTime = DateUtil.parseWithYYYYMMDDHHMMSS(StringUtils
				.isNotBlank(searchMap.get("submitEndCTime")) ? searchMap
				.get("submitEndCTime") + " 23:59:59" : "");
		Date approveBeginCTime = DateUtil.parseWithYYYYMMDDHHMMSS(StringUtils
				.isNotBlank(searchMap.get("approveBeginCTime")) ? searchMap
				.get("approveBeginCTime") + " 00:00:00" : "");
		Date approveEndCTime = DateUtil.parseWithYYYYMMDDHHMMSS(StringUtils
				.isNotBlank(searchMap.get("approveEndCTime")) ? searchMap
				.get("approveEndCTime") + " 23:59:59" : "");

		int status = Integer.parseInt(searchMap.get("status"));
		// 状态值为-1时，查找状态为3：审核中；4：审核通过；5：未通过 数据
		if (status == 0) {
			criteria.andStatusEqualTo(0);
		} else if (status == 1) {
			criteria.andStatusNotEqualTo(0);
		}
		// 按举报人查询
		if (StringUtils.isNotBlank(cname)) {
			criteria.andUsernameLike("%" + cname + "%");
		}
		// 按知识标题查询
		if (StringUtils.isNotBlank(title)) {
			criteria.andKnowledgetitleLike("%" + title + "%");
		}
		String type = searchMap.get("type");
		if (StringUtils.isNotBlank(type)) {
			criteria.andTypeEqualTo(type);
		}
		// 按创建时间查询条件
		if (submitBeginCTime != null && submitEndCTime != null) {
			criteria.andCreatetimeBetween(submitBeginCTime, submitEndCTime);
		} else if (submitBeginCTime != null && submitEndCTime == null) {
			criteria.andCreatetimeGreaterThan(submitBeginCTime);
		} else if (submitBeginCTime == null && submitEndCTime != null) {
			criteria.andCreatetimeLessThan(submitEndCTime);
		}
		// 按修改时间查询条件
		if (approveBeginCTime != null && approveEndCTime != null) {
			criteria.andUpdatetimeBetween(approveBeginCTime, approveEndCTime);
		} else if (approveBeginCTime != null && approveEndCTime == null) {
			criteria.andUpdatetimeGreaterThan(approveBeginCTime);
		} else if (approveBeginCTime == null && approveEndCTime != null) {
			criteria.andUpdatetimeLessThan(approveEndCTime);
		}
		int count = knowledgeReportMapper.countByExample(example);
		PageUtil p = new PageUtil(count, page, row);
		example.setLimitStart(p.getPageStartRow() - 1);
		example.setLimitEnd(row);
		List<KnowledgeReport> list = knowledgeReportMapper
				.selectByExample(example);
		result.put("rows", list);
		result.put("page", p);
		result.put("total", count);
		return result;
	}

	@Override
	public int updateStatus(long id, int status, String info) {
		KnowledgeReport report = knowledgeReportMapper.selectByPrimaryKey(id);
		report.setStatus(status);
		report.setResults(info);
		return knowledgeReportMapper.updateByPrimaryKey(report);
	}

	@Override
	public int deleteByKnowledgeId(long id) {
		return knowledgeReportMapper.deleteByPrimaryKey(id);
	}

}
