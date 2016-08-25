package com.ginkgocap.ywxt.knowledge.model.common;

import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.utils.HtmlToText;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.common.phoenix.permission.entity.Permission;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by gintong on 2016/7/18.
 */
public class DataCollect implements Serializable {

    // 知识简略信息
    private KnowledgeBase knowledge;

    // 知识详细信息
    private Knowledge knowledgeDetail;

    // 知识来源
    private KnowledgeReference reference;

    // 关联
    private List<Associate> asso;

    // 权限
    private Permission permission;

    //同步到动态
    private short updateDynamic;

    public KnowledgeBase getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(KnowledgeBase knowledge) {
        this.knowledge = knowledge;
    }

    public Knowledge getKnowledgeDetail() {
        return knowledgeDetail;
    }

    public void setKnowledgeDetail(Knowledge knowledgeDetail) {
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


    public DataCollect() {
    }

    public DataCollect(KnowledgeBase knowledgeBase, KnowledgeReference reference) {
        this.knowledge = knowledgeBase;
        this.reference = reference;
    }

    public DataCollect(KnowledgeBase knowledgeBase, Knowledge knowledgeDetail) {
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

    public void serUserId(long userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User is null");
        }
        if (this.knowledgeDetail != null) {
            this.knowledgeDetail.setCid(userId);
        }
    }

    public void serUserInfo(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }
        if (this.knowledgeDetail != null) {
            this.knowledgeDetail.setCid(user.getId());
            this.knowledgeDetail.setCname(user.getName());
        }
    }

    public KnowledgeBase generateKnowledge() {
        if (this.knowledgeDetail != null) {
            return this.knowledge = generateKnowledge(knowledgeDetail, (short)0);
        }

        return this.knowledge;
    }

    public static KnowledgeBase generateKnowledge(Knowledge detail,short type) {
        KnowledgeBase knowledgeBase = null;
        if (detail != null) {
            knowledgeBase = new KnowledgeBase();
            knowledgeBase.setId(detail.getId());
            knowledgeBase.setKnowledgeId(detail.getId());
            knowledgeBase.setTitle(detail.getTitle());
            String knowledgeContent = HtmlToText.htmlToText(detail.getContent());
            int contentLen = knowledgeContent.length();
            int maxLen = contentLen > 250 ? 250 : contentLen;
            knowledgeBase.setContentDesc(knowledgeContent.substring(0, maxLen));
            if (detail.getMultiUrls() != null && detail.getMultiUrls().size() > 0) {
                System.out.println("save picture: " + detail.getMultiUrls().get(0));
                knowledgeBase.setCoverPic(detail.getMultiUrls().get(0));
            }
            //knowledge.setAuditStatus(auditStatus);
            if (detail.getTagList() != null && detail.getTagList().size() > 0) {
                String tags = KnowledgeUtil.convertLongListToBase(detail.getTagList());
                System.out.println("create tags for base: " + tags);
                knowledgeBase.setTags(tags);
            }
            if (type > 0) {
                knowledgeBase.setType(type);
            } else {
                knowledgeBase.setType(KnowledgeUtil.parserShortType(detail.getColumnType()));
            }
            knowledgeBase.setColumnId(KnowledgeUtil.parserColumnId(detail.getColumnid()));
            knowledgeBase.setCreateUserId(detail.getCid());
            //For reference knowledge may be different with author
            knowledgeBase.setCreateUserName(detail.getCname());
            long createTime = StringUtils.isNotBlank(detail.getCreatetime()) ? KnowledgeUtil.parserTimeToLong(detail.getCreatetime()) : System.currentTimeMillis();
            long modifyTime = StringUtils.isNotBlank(detail.getModifytime()) ? KnowledgeUtil.parserTimeToLong(detail.getModifytime()) : System.currentTimeMillis();
            long publicTime = StringUtils.isNotBlank(detail.getSubmitTime()) ? KnowledgeUtil.parserTimeToLong(detail.getSubmitTime()) : System.currentTimeMillis();;
            knowledgeBase.setCreateDate(createTime);
            knowledgeBase.setModifyDate(modifyTime);
            knowledgeBase.setPublicDate(publicTime);
            knowledgeBase.setIsOld((short) 0);
            knowledgeBase.setUserStar((short) 0);
            knowledgeBase.setStatus((short)detail.getStatus());
            //knowledge.setPublicDate(System.currentTimeMillis());
            //knowledge.setReportStatus(reportStatus);
        }
        return knowledgeBase;
    }
}
