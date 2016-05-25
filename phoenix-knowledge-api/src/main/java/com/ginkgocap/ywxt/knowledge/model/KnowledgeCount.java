package com.ginkgocap.ywxt.knowledge.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Chen Peifeng on 2016/5/24.
 */
@Entity
@Table(name = "tb_knowledge_count", catalog = "phoenix_knowledge_new")
public class KnowledgeCount implements Serializable {

    private static final long serialVersionUID = 2655583530973124686L;

    private long id;

    private long knowledgeId;

    private long clickNum;

    private long shareNum;

    private long collectNum;

    private long commentNum;

    private long hotNum;

    private String source;

    private short type;

    @Id
    @GeneratedValue(generator = "knowledgeCountId")
    @GenericGenerator(name = "knowledgeCountId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "knowledgeCountId") })
    @Column(name = "id", unique = true, nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "knowledgeId")
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

    @Column(name = "collectNum")
    public long getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(long collectNum) {
        this.collectNum = collectNum;
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
