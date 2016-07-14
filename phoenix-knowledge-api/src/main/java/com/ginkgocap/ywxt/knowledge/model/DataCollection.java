package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.knowledge.utils.HtmlToText;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.common.phoenix.permission.entity.Permission;

/**
 * @Title: 知识数据的大集合对象
 * @Description: 将涉及到知识的所有对象统一集合到此大集合对象中，以便统一管理以及统一标准
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

	/**关联*/
	private List<Associate> asso;

    /**权限*/
    private Permission permission;

    private short updateDynamic;

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

    public short getUpdateDynamic() {
        return updateDynamic;
    }

    public void setUpdateDynamic(short updateDynamic) {
        this.updateDynamic = updateDynamic;
    }


    public DataCollection(){}

    public DataCollection(KnowledgeBase knowledgeBase,KnowledgeReference reference)
    {
        this.knowledge = knowledgeBase;
        this.reference = reference;
    }

    public DataCollection(KnowledgeBase knowledgeBase,KnowledgeDetail knowledgeDetail)
    {
        this.knowledge = knowledgeBase;
        this.knowledgeDetail = knowledgeDetail;
    }

	public List<Associate> getAsso() {
		return asso;
	}

	public void setAsso(List<Associate> asso) {
		this.asso = asso;
	}

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
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
            this.knowledgeDetail.setCid(user.getId());
            this.knowledgeDetail.setCname(user.getName());
        }
    }

    public KnowledgeBase generateKnowledge()
    {
        if (this.knowledgeDetail != null) {
            this.knowledge = new KnowledgeBase();
            this.knowledge.setId(this.knowledgeDetail.getId());
            this.knowledge.setKnowledgeId(this.knowledgeDetail.getId());
            this.knowledge.setTitle(this.knowledgeDetail.getTitle());
            String knowledgeContent = HtmlToText.htmlToText(this.knowledgeDetail.getContent());
            int contentLen = knowledgeContent.length();
            int maxLen = contentLen > 250 ? 250 : contentLen;
            this.knowledge.setContentDesc(knowledgeContent.substring(0,maxLen));
            if (this.knowledgeDetail.getMultiUrls() != null && this.knowledgeDetail.getMultiUrls().size()>0) {
                System.out.println("save picture: " + this.knowledgeDetail.getMultiUrls().get(0));
                this.knowledge.setPictureId(this.knowledgeDetail.getMultiUrls().get(0));
            }
            //knowledge.setAuditStatus(auditStatus);
            if (knowledgeDetail.getTags() != null && knowledgeDetail.getTags().size() > 0) {
                String tags = knowledgeDetail.getTags().toString();
                System.out.println("create tags for base: " + tags);
                knowledge.setTags(tags.substring(1, tags.length()-1));
            }
            this.knowledge.setColumnId(this.knowledgeDetail.getColumnId());
            this.knowledge.setCreateUserId(this.knowledgeDetail.getOwnerId());
            //For reference knowledge may be different with author
            this.knowledge.setCreateUserName(this.knowledgeDetail.getOwnerName());
            this.knowledge.setCreateDate(this.knowledgeDetail.getCreateTime());
            this.knowledge.setModifyDate(this.knowledgeDetail.getModifyTime());
            this.knowledge.setIsOld((short)0);
            this.knowledge.setUserStar((short)0);
            //knowledge.setPublicDate(System.currentTimeMillis());
            //knowledge.setReportStatus(reportStatus);
        }

        return this.knowledge;
    }

    public static KnowledgeBase generateKnowledge(KnowledgeDetail detail)
    {
        KnowledgeBase knowledgeBase = null;
        if (detail != null) {
            knowledgeBase = new KnowledgeBase();
            knowledgeBase.setId(detail.getId());
            knowledgeBase.setKnowledgeId(detail.getId());
            knowledgeBase.setTitle(detail.getTitle());
            String knowledgeContent = HtmlToText.htmlToText(detail.getContent());
            int contentLen = knowledgeContent.length();
            int maxLen = contentLen > 250 ? 250 : contentLen;
            knowledgeBase.setContentDesc(knowledgeContent.substring(0,maxLen));
            if (detail.getMultiUrls() != null && detail.getMultiUrls().size()>0) {
                System.out.println("save picture: " + detail.getMultiUrls().get(0));
                knowledgeBase.setPictureId(detail.getMultiUrls().get(0));
            }
            //knowledge.setAuditStatus(auditStatus);
            if (detail.getTags() != null && detail.getTags().size() > 0) {
                String tags = detail.getTags().toString();
                System.out.println("create tags for base: " + tags);
                knowledgeBase.setTags(tags.substring(1, tags.length()-1));
            }
            knowledgeBase.setColumnId(detail.getColumnId());
            knowledgeBase.setCreateUserId(detail.getOwnerId());
            //For reference knowledge may be different with author
            knowledgeBase.setCreateUserName(detail.getOwnerName());
            knowledgeBase.setCreateDate(detail.getCreateTime());
            knowledgeBase.setModifyDate(detail.getModifyTime());
            knowledgeBase.setIsOld((short)0);
            knowledgeBase.setUserStar((short)0);
            //knowledge.setPublicDate(System.currentTimeMillis());
            //knowledge.setReportStatus(reportStatus);
        }
        return knowledgeBase;
    }
}