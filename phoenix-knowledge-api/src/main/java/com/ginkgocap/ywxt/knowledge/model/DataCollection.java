package com.ginkgocap.ywxt.knowledge.model;

import com.ginkgocap.parasol.associate.model.Associate;
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
	//private ColumnCollection column;

	/**关联*/
	private Associate asso;

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

    /*
	public ColumnCollection getColumn() {
		return column;
	}

	public void setColumn(ColumnCollection column) {
		this.column = column;
	}*/

	public Associate getAsso() {
		return asso;
	}

	public void setAsso(Associate asso) {
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
        if (this.knowledgeDetail != null) {
            this.knowledge = new KnowledgeBase();
            this.knowledge.setKnowledgeId(this.knowledgeDetail.getId());
            this.knowledge.setAuthor(this.knowledgeDetail.getOwnerName());
            this.knowledge.setTitle(this.knowledgeDetail.getTitle());
            this.knowledge.setContentDesc(this.knowledgeDetail.getContent());
            if (this.knowledgeDetail.getMultiUrls() != null && this.knowledgeDetail.getMultiUrls().size()>0) {
                this.knowledge.setPictureId(1111L);
            }
            if (this.knowledgeDetail.getAttachmentUrls() != null && this.knowledgeDetail.getAttachmentUrls().size()>0) {
                this.knowledge.setAttachmentId(123456L);
            }
            //knowledge.setAuditStatus(auditStatus);
            this.knowledge.setColumnId(this.knowledgeDetail.getColumnId());
            this.knowledge.setCreateUserId(this.knowledgeDetail.getOwnerId());
            //For reference knowledge may be different with author
            this.knowledge.setCreateUserName(this.knowledgeDetail.getOwnerName());
            this.knowledge.setCreateDate(this.knowledgeDetail.getCreateTime());
            this.knowledge.setModifyDate(this.knowledgeDetail.getModifyTime());
            //knowledge.setPublicDate(System.currentTimeMillis());
            //knowledge.setReportStatus(reportStatus);
        }

        return this.knowledge;
    }
}