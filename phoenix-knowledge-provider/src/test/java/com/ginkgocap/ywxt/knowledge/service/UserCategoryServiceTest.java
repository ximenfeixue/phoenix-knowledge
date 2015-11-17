package com.ginkgocap.ywxt.knowledge.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.user.model.User;

public class UserCategoryServiceTest extends TestBase {
	@Autowired
	private UserCategoryService userCategoryService;

	@Test
	public void deleteKnowledgeNew() {
		
		List<Long> idtype = new ArrayList<Long>(); 
		idtype.add(0l); 
		idtype.add(1l); 
		userCategoryService.checkNogroup(5555l, idtype); 
	}
}
