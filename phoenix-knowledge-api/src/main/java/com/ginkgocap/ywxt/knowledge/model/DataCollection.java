package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

import com.ginkgocap.ywxt.asso.model.Asso;

/**
 * @Title: 知识数据的大集合对象
 * @Description: 将涉及到知识的所有对象统一集合到此大集合对象中，以便统一管理以及统一标准
 * @author 周仕奇
 * @date 2016年1月15日 上午10:31:31
 * @version V1.0.0
 */
public class DataCollection implements Serializable {

	
	private static final long serialVersionUID = -424912985959502809L;
	
	/**知识信息*/
	private KnowledgeBase knowledge;

	/**知识来源*/
	private KnowledgeReference reference;

	/**栏目*/
	private ColumnCollection column;

	/**关联*/
	private Asso asso;

	public KnowledgeBase getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(KnowledgeBase knowledge) {
		this.knowledge = knowledge;
	}

	public KnowledgeReference getReference() {
		return reference;
	}

	public void setReference(KnowledgeReference reference) {
		this.reference = reference;
	}

	public ColumnCollection getColumn() {
		return column;
	}

	public void setColumn(ColumnCollection column) {
		this.column = column;
	}

	public Asso getAsso() {
		return asso;
	}

	public void setAsso(Asso asso) {
		this.asso = asso;
	}
	
	
}