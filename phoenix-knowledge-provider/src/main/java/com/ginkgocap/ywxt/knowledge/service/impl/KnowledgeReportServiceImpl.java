package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeReport;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeReportMapper;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeReportService;
import com.ginkgocap.ywxt.knowledge.util.Constants;

@Service("knowledgeReportService")
public class KnowledgeReportServiceImpl implements KnowledgeReportService {

	@Resource
	private KnowledgeReportMapper knowledgeReportMapper;

	@Override
	public Map<String, Object> addReport(long kid, String type, String desc,
			long userid) {
		Map<String, Object> result = new HashMap<String, Object>();
		KnowledgeReport report = new KnowledgeReport();
		report.setKnowledgeId(kid);
		report.setType(type);
		report.setRepDesc(desc);
		report.setUserId(userid);
		report.setCreatetime(new Date());
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

}
