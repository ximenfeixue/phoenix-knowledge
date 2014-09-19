package com.ginkgocap.ywxt.knowledge.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledge;
import com.ginkgocap.ywxt.knowledge.service.knowledge.ColumnKnowledgeService;

/**
 * <p>
 * 知识栏目操作测试接口
 * </p>
 * <p>
 * 于2014-8-19 由 bianzhiwei 创建
 * </p>
 * 
 * @author <p>
 *         当前负责人 bianzhiwei
 *         </p>
 * @since <p>
 *        1.2.1-SNAPSHO
 *        </p>
 */

public class ColumnKnowledgeTest extends TestBase {
	@Autowired
	private ColumnKnowledgeService columnKnowledgeService;

	@Test
	public void insertcolumnknowledge() throws Exception {

		ColumnKnowledge columnKnowledge = new ColumnKnowledge();
		columnKnowledge.setColumnId((long) 111);
		columnKnowledge.setKnowledgeId((long) 21);
		columnKnowledge.setType((short) 1);
		columnKnowledge.setUserId((long) 111);
		int count = columnKnowledgeService
				.insertColumnKnowledge(columnKnowledge);
		if (count > 0) {

			System.out.println("成功");
		}
	}

	@Test
	public void deletecolumnknowledge() throws Exception {

		ColumnKnowledge columnKnowledge = new ColumnKnowledge();

		long[] knowledgeids = { 21 };

		System.out.println(columnKnowledgeService.deleteColumnKnowledge(
				knowledgeids, 111));
	}

}
