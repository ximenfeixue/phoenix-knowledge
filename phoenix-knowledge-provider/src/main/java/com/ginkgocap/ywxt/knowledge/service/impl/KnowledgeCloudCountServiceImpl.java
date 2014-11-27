package com.ginkgocap.ywxt.knowledge.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapperManual;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCloudCountService;
import com.ginkgocap.ywxt.knowledge.util.Constants;

@Service("knowledgeCloudCountService")
public class KnowledgeCloudCountServiceImpl implements KnowledgeCloudCountService {
	
	@Resource
	private KnowledgeStaticsMapperManual knowledgeStaticsMapperManual;
	
	@Override
	public void countReader(Long kid) {
		// 添加点击数
				knowledgeStaticsMapperManual.updateStatics(kid, 0, 0, 0,
						Constants.StaticsValue.clickCount.v());
	}


}
