package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.ginkgocap.ywxt.knowledge.utils.HtmlToText;

/**
 * @Title: 知识详细信息（mongoDB保存，保存知识的全部信息，提供给编辑、详细信息查看界面查询）
 * @author 周仕奇
 * @date 2016年1月11日 下午3:38:53
 * @version V1.0.0
 */
public class KnowledgeMongo extends KnowledgeBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4155861553088421781L;
	
	/**描述*/
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public void createContendDesc() {
		
		String testContent = HtmlToText.html2Text(this.getContent());
		
		if(StringUtils.isNotBlank(testContent) && testContent.length() > CONTENT_DESC_LENGTH )
			this.setContentDesc(testContent.substring(0, CONTENT_DESC_LENGTH));
		else 
			this.setContentDesc(testContent);
	}

}