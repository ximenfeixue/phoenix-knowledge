package com.ginkgocap.ywxt.knowledge.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.ginkgocap.ywxt.knowledge.base.TestBase;

public class DataCenterServiceTest extends TestBase{
	
	@Resource
	private DataCenterService dataCenterService;
	@Test
	public void parseTest(){
		dataCenterService.getCaseDataFromDataCenter("http://192.168.101.14:81/cloud/case/0051190063.pdf");
	}
}
