package com.ginkgocap.ywxt.knowledge.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Chen Peifeng on 2016/5/24.
 */
public class KnowledgeCount implements Serializable {

    private static final long serialVersionUID = 2655583530973124686L;

    private long knowledgeId;

    private long clickNum;

    private long shareNum;

    private long collectionNum;

    private long commentNum;

    private long hotNum;

    private String source;

    private short type;

    @Id
    @GeneratedValue(generator = "knowledgeId")
    @GenericGenerator(name = "knowledgeId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "knowledgeId") })
    @Column(name = "knowledgeId", unique = true, nullable = false)
    public long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    @Column(name = "clickNum")
    public long getClickNum() {
        return clickNum;
    }

    public void setClickNum(long clickNum) {
        this.clickNum = clickNum;
    }

    @Column(name = "shareNum")
    public long getShareNum() {
        return shareNum;
    }

    public void setShareNum(long shareNum) {
        this.shareNum = shareNum;
    }

    @Column(name = "collectionNum")
    public long getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(long collectionNum) {
        this.collectionNum = collectionNum;
    }

    @Column(name = "commentNum")
    public long getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(long commentNum) {
        this.commentNum = commentNum;
    }

    @Column(name = "hotNum")
    public long getHotNum() {
        return hotNum;
    }

    public void setHotNum(long hotNum) {
        this.hotNum = hotNum;
    }

    @Column(name = "source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Column(name = "type")
    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }
}
