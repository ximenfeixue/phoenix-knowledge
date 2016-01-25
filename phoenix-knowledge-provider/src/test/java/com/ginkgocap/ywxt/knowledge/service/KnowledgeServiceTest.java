package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.user.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;

public class KnowledgeServiceTest extends TestBase {
	@Autowired
	private KnowledgeService knowledgeService;

	@Resource
	private MongoTemplate mongoTemplate;

	@Test
	public void deleteKnowledgeNew() {
		
		
		KnowledgeNewsVO  vo =new KnowledgeNewsVO();
		
		vo.setSelectedIds("{\"dales\":[{\"id\":-1,\"name\":\"全平台\"}],\"zhongles\":[],\"xiaoles\":[],\"dule\":false}");
		vo.setTitle("111");
		vo.setColumnid("1");
		vo.setContent("ss");

		User user =new User();
		user.setId(36);
		user.setName("sss");
		vo.setColumnType("1");
		vo.setAsso("{\"r\":[],\"p\":[],\"o\":[],\"k\":[]}");
	    knowledgeService.insertknowledge(vo, user);
	}

	@Test
	public void getKnowledge(){
		/*Criteria criteria = new Criteria();
		BasicQuery basicQuery = new BasicQuery("{status:4,uid:{$ne:0}}");
		basicQuery.limit(100);
		criteria.where("status").is(4).and("uid").ne(0);
		Query query = new Query(criteria).limit(10);
		//List<KnowledgeNews> list = mongoTemplate.find(basicQuery, KnowledgeNews.class,"KnowledgeNews");
		//System.out.println("--------------" + list.size());
		System.out.println("knowledge count:" + mongoTemplate.find(basicQuery,KnowledgeNews.class,"KnowledgeNews").size());*/
		knowledgeService.initOldSerach(16293L,100000L);
	}

}
