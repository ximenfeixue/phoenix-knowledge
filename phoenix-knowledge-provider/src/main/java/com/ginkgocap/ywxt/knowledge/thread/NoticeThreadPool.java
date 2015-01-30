package com.ginkgocap.ywxt.knowledge.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.knowledge.service.DataCenterService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeAssoImportService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeSearchService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.user.service.DynamicNewsService;

@Service
public class NoticeThreadPool implements InitializingBean, DisposableBean {

	@Resource
	private DataCenterService dataCenterService;
	@Resource
	private KnowledgeSearchService knowledgeSearchService;
	@Resource
	private KnowledgeAssoImportService knowledgeAssoImportService;
	@Resource
	private DynamicNewsService dynamicNewsService;

	private static ExecutorService executor = null;

	private static int threadCount = 5;

	private static Logger logger = LoggerFactory
			.getLogger(NoticeThreadPool.class);

	static {
		executor = Executors.newFixedThreadPool(threadCount);
	}

	public void noticeDataCenter(final Integer type,
			final Map<String, Object> params) {
		logger.info("----进入通知数据中心线程,通知类型为：{}----", type);
		if (params == null || type == null)
			return;

		executor.execute(new Runnable() {
			@Override
			public void run() {
				if (type == Constants.noticeType.column.v()) { // 栏目通知
					String columnId = params.get("columnId") + "";
					if (StringUtils.isBlank(columnId))
						return;
					dataCenterService.noticeDataCenterWhileColumnChange(Integer
							.parseInt(columnId));

				} else if (type == Constants.noticeType.knowledge.v()) { // 知识通知
					// kId 知识ID
					// oper 操作(add del upd)
					// type 知识类型(11种类型)
					String kId = params.get("kId") + "";
					String oper = params.get("oper") + "";
					String type = params.get("type") + "";
					if (StringUtils.isBlank(kId) || StringUtils.isBlank(oper)
							|| StringUtils.isBlank(type))
						return;
					dataCenterService.noticeDataCenterWhileKnowledgeChange(kId,
							oper, type);

				} else if (type == Constants.noticeType.cases.v()) { // 经典案例通知
					dataCenterService.noticeDataCenterWhileFileChange(params);
				} else if (type == Constants.noticeType.shareToJinTN.v()) { // 分享到金桐脑通知
					String userId = params.get("userId") + "";
					KnowledgeNewsVO vo = (KnowledgeNewsVO) params.get("vo");
					knowledgeSearchService.shareToJinTN(Long.parseLong(userId),
							vo);
				} else if (type == Constants.noticeType.dynamic.v()) {
					// 创建知识，生成动态
					String userId = params.get("userId") + "";
					String userName = params.get("userName") + "";
					String userPic = params.get("userPic") + "";
					List<String> permList = (List<String>) params.get("permList");
					KnowledgeNewsVO vo = (KnowledgeNewsVO) params.get("vo");
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("type", "10");
					param.put("lowType", vo.getColumnType());
					param.put("createrId", userId);
					param.put("title", vo.getTitle());
					param.put("content", vo.getDesc());
					param.put("createrName", userName);
					param.put("targetId", vo.getkId() + "");
					param.put("imgPath", vo.getPic());
					param.put("receiverIds", permList);
					param.put("picPath", userPic);
					param.put("forwardingContent", "");
					dynamicNewsService.insertNewsAndRelationByParam(param);
				}
			}
		});
	}

	public void importData() {
		logger.info("----进入导入关联和权限数据----");
		executor.execute(new Runnable() {

			@Override
			public void run() {

				knowledgeAssoImportService.importasso();
			}
		});
	}

	@Override
	public void destroy() throws Exception {
		if (executor != null) {
			executor.shutdown();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}
