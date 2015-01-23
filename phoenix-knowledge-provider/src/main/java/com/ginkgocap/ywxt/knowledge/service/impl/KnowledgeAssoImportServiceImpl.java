package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeAssoImportService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeConnectInfoService;
import com.ginkgocap.ywxt.knowledge.service.UserPermissionService;
import com.ginkgocap.ywxt.knowledge.thread.NoticeThreadPool;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.JsonUtil;

@Service("knowledgeAssoImportService")
public class KnowledgeAssoImportServiceImpl implements
		KnowledgeAssoImportService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private MongoTemplate mongoTemplate;

	@Resource
	private KnowledgeConnectInfoService knowledgeConnectInfoService;

	@Resource
	private UserPermissionService userPermissionService;
	
	@Resource
	private NoticeThreadPool noticeThreadPool;

	@Override
	public void importasso() {

		// 类型（,1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规，11：文章）
		String type[] = { "1", "2", "3", "4", "6", "7", "8", "10", "11" };
		for (int i = 0; i < type.length; i++) {
			String obj = Constants.getTableName(type[i]);
			Criteria criteria = Criteria.where("asso").ne("").and("uid").ne(0);
			Query query = new Query(criteria);
			Knowledge knowledge = null;
			try {
				knowledge = (Knowledge) Class.forName(obj).newInstance();

			} catch (Exception e) {

				e.printStackTrace();
			}
			List<Knowledge> knowledgelist = mongoTemplate.find(query,
					Knowledge.class,
					obj.substring(obj.lastIndexOf(".") + 1, obj.length()));
			for (Knowledge knowledge2 : knowledgelist) {
				if (knowledge2.getAsso() != null
						&& !StringUtils.equals(knowledge2.getAsso(), "{}")) {

					knowledgeConnectInfoService.insertKnowledgeConnectInfo(knowledge2.getAsso(), knowledge2.getId(), 0l);
				}
				if (StringUtils.isNotBlank(knowledge2.getSelectedIds())) {
					try{
						Boolean dule = JsonUtil.checkKnowledgePermission(knowledge2.getSelectedIds());
						if (!dule) {
							Map<String, Object> result = userPermissionService.importUserPermission(knowledge2.getSelectedIds());
							JSONObject json = JSONObject.fromObject(result);
							userPermissionService.updateUserPermission(knowledge2.getId(), json.toString(), type[i]);
						}
					}catch (Exception e) {

						e.printStackTrace();
					}
				}
			}
		}

	}

	@Override
	public void threadimpotdata() {
	
		noticeThreadPool.importData();
		
	}
}
