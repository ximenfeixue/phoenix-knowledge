package com.ginkgocap.ywxt.knowledge.model;

import com.ginkgocap.ywxt.asso.model.Asso;
import com.ginkgocap.ywxt.user.model.User;

import java.io.Serializable;

/**
 * @Title: 知识数据的大集合对象
 * @Description: 将涉及到知识的所有对象统一集合到此大集合对象中，以便统一管理以及统一标准
 * @author 周仕奇
 * @date 2016年1月15日 上午10:31:31
 * @version V1.0.0
 */
public class DataCollection implements Serializable {

	private static final long serialVersionUID = -424912985959502809L;

	/**知识简略信息*/
	private KnowledgeBase knowledge;

    /** 知识详细信息**/
    private KnowledgeDetail knowledgeDetail;

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

    public KnowledgeDetail getKnowledgeDetail() {
        return knowledgeDetail;
    }

    public void setKnowledgeDetail(KnowledgeDetail knowledgeDetail) {
        this.knowledgeDetail = knowledgeDetail;
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

    public void serUserId(long userId)
    {
        if (userId <= 0) {
            throw new IllegalArgumentException("User is null");
        }
        if (this.knowledgeDetail != null) {
            this.knowledgeDetail.setOwnerId(userId);
        }
    }

    public void serUserInfo(User user)
    {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }
        if (this.knowledgeDetail != null) {
            this.knowledgeDetail.setOwnerId(user.getId());
            this.knowledgeDetail.setOwnerName(user.getName());
        }
    }

    public KnowledgeBase generateKnowledge()
    {
        if (knowledgeDetail != null) {
            //knowledge.setType(typeId);
            this.knowledge.setKnowledgeId(knowledgeDetail.getId());
            this.knowledge.setAuthor(knowledgeDetail.getOwnerName());
            this.knowledge.setTitle(knowledgeDetail.getTitle());
            this.knowledge.setContentDesc(knowledgeDetail.getContent());
            if (knowledgeDetail.getMultiUrls() != null && knowledgeDetail.getMultiUrls().size()>0) {
                this.knowledge.setPictureId(1111L);
            }
            if (knowledgeDetail.getAttachmentUrls() != null && knowledgeDetail.getAttachmentUrls().size()>0) {
                this.knowledge.setAttachmentId(123456L);
            }
            //knowledge.setAuditStatus(auditStatus);
            this.knowledge.setColumnId(knowledgeDetail.getColumnId());
            this.knowledge.setCreateUserId(knowledgeDetail.getOwnerId());
            //For reference knowledge may be different with author
            this.knowledge.setCreateUserName(knowledgeDetail.getOwnerName());
            this.knowledge.setCreateDate(knowledgeDetail.getCreateTime());
            this.knowledge.setModifyDate(knowledgeDetail.getModifyTime());
            //knowledge.setPublicDate(System.currentTimeMillis());
            //knowledge.setReportStatus(reportStatus);
        }

        return this.knowledge;
    }
}