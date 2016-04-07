package com.ginkgocap.ywxt.knowledge.model;

import com.ginkgocap.ywxt.knowledge.utils.HtmlToText;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

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
	
	public void createContendDesc() {
		
		String testContent = HtmlToText.html2Text(this.getContent());
		
		if(StringUtils.isNotBlank(testContent) && testContent.length() > CONTENT_DESC_LENGTH )
			this.setContentDesc(testContent.substring(0, CONTENT_DESC_LENGTH));
		else 
			this.setContentDesc(testContent);
	}

    public static KnowledgeMongo clone(KnowledgeBase knowledgeBase)
    {
        KnowledgeMongo knowledgeMongo = new KnowledgeMongo();
        knowledgeMongo.setId(knowledgeBase.getId());
        knowledgeMongo.setColumnId(knowledgeBase.getColumnId());
        knowledgeMongo.setType(knowledgeBase.getType());
        knowledgeMongo.setAuthor(knowledgeBase.getAuthor());
        knowledgeMongo.setTitle(knowledgeBase.getTitle());
        knowledgeMongo.setContentDesc(knowledgeBase.getContentDesc());
        knowledgeMongo.setPictureId(knowledgeBase.getPictureId());
        knowledgeMongo.setAttachmentId(knowledgeBase.getAttachmentId());
        knowledgeMongo.setAuditStatus(knowledgeBase.getAuditStatus());
        knowledgeMongo.setCreateUserId(knowledgeBase.getCreateUserId());
        knowledgeMongo.setCreateUserName(knowledgeBase.getCreateUserName());
        knowledgeMongo.setModifyDate(knowledgeBase.getModifyDate());
        knowledgeMongo.setModifyUserId(knowledgeBase.getModifyUserId());
        knowledgeMongo.setModifyUserName(knowledgeBase.getModifyUserName());
        knowledgeMongo.setCreateDate(knowledgeBase.getCreateDate());
        knowledgeMongo.setPublicDate(knowledgeBase.getPublicDate());
        knowledgeMongo.setReportStatus(knowledgeBase.getReportStatus());

        return knowledgeMongo;
    }

}