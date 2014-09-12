package com.ginkgocap.ywxt.knowledge.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.ColumnKnowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;
import com.ginkgocap.ywxt.knowledge.service.columnknowledge.ColumnKnowledgeService;

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
	private ColumnKnowledgeService knowledgeColumnService;

	@Test
	public void insertcolumnknowledge() throws Exception {

		ColumnKnowledge columnKnowledge = new ColumnKnowledge();
		columnKnowledge.setColumn_id(1);
		columnKnowledge.setKnowledge_id(1);
		columnKnowledge.setUser_id(111);
		columnKnowledge.setType(1);

		knowledgeColumnService.insertColumnKnowledge(columnKnowledge);
	}

	@Test
	public void deletecolumnknowledge() throws Exception {

		ColumnKnowledge columnKnowledge = new ColumnKnowledge();
		columnKnowledge.setColumn_id(1);
		columnKnowledge.setKnowledge_id(1);
		columnKnowledge.setUser_id(111);
		columnKnowledge.setType(1);

		long[] knowledgeids = { 1, 2 };

		System.out.println(knowledgeColumnService.deleteColumnKnowledge(
				knowledgeids, 1));
	}

}
