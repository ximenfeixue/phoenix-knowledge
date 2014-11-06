package com.ginkgocap.ywxt.knowledge.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollectionVO;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCollectionService;
import com.ginkgocap.ywxt.user.model.User;

/**
 * 知识测试类
 * 
 * @author caihe
 * 
 */

public class KnowledgeCollectionServiceTest extends TestBase {

	public KnowledgeCollectionServiceTest() {
		System.out.println(123456);
	}

	@Autowired
	private KnowledgeCollectionService knowledgeCollectionService;

	@Test
	public void testInserCollection() {
		KnowledgeCollectionVO vo = new KnowledgeCollectionVO();
		vo.setCategoryIds("1,2,3,4");
		vo.setColumType("1");
		vo.setComment("我要收藏这篇文章");
		vo.setkId(22805l);
		vo.setTags("标签1111，标签2222，标签3333");
		User user =  new User();
		user.setId(1);
		user.setName("jintongnao");
		knowledgeCollectionService.insertKnowledgeCollection(vo, user);
	}
	
	//返回收藏夹下知识的分页信息
	@Test
	public void queryKnowledgeAll() {
	    knowledgeCollectionService.queryKnowledgeAll("1", "1", 10132l, "000000001", "", 1, 10);
	    knowledgeCollectionService.queryKnowledgeAll("1","1", 10132l, "000000007","",1, 10);
	}
	//移动
	@Test
	@Rollback(false)
	public void move() {
	    
	    List<Long> knowledgeids=new ArrayList<Long>();
	    knowledgeids.add(111l);
	    knowledgeids.add(112l);
	    List<Long> categoryids=new ArrayList<Long>();
	    categoryids.add(221l);
	    categoryids.add(222l);
        knowledgeCollectionService.move(10132l, knowledgeids, categoryids, 5);
	}
}
