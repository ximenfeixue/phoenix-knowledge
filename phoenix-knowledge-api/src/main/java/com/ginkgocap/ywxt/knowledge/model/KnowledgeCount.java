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

    private short type;

    private long userId;

    private long clickCount;

    private long shareCount;

    private long collectCount;

    private long commentCount;

    private long hotCount;

    private String title;

    private int source;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "type")
    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    @Column(name = "userId")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    @Column(name = "clickCount")
    public long getClickCount() {
        return clickCount;
    }

    public void setClickCount(long clickCount) {
        this.clickCount = clickCount;
    }

    @Column(name = "shareCount")
    public long getShareCount() {
        return shareCount;
    }

    public void setShareCount(long shareCount) {
        this.shareCount = shareCount;
    }

    @Column(name = "collectCount")
    public long getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(long collectCount) {
        this.collectCount = collectCount;
    }

    @Column(name = "commentCount")
    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    @Column(name = "hotCount")
    public long getHotCount() {
        return hotCount;
    }

    public void setHotCount(long hotCount) {
        this.hotCount = hotCount;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "source")
    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
