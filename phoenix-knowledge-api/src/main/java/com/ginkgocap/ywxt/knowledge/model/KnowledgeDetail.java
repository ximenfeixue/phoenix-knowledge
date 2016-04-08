package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chen Peifeng on 2016/4/7.
 */
public class KnowledgeDetail implements Serializable {

    /** 知识id **/
    private long id;

    /** 知识类型 **/
    private short columnId;

    /** 知识标题 **/
    private String title;
    /** 知识介绍 **/
    private String content;

    private long phone;
    /** 创建者id **/
    private long ownerId;
    /** 创建人姓名 **/
    private String ownerName;

    /** 创建时间 **/
    private long createTime;

    /** modify user Id**/
    private long modifyUserId;

    /** 修改时间 **/
    private long modifyTime;

    /** 图片/视频     */
    private List<String> multiUrls;

    /** 附件 */
    private List<String> attachmentUrls;

    private String tags;

    /** 存储目录 **/
    private String categoryIds;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public short getColumnId() {
        return columnId;
    }

    public void setColumnId(short columnId) {
        this.columnId = columnId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(long modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public List<String> getMultiUrls() {
        return multiUrls;
    }

    public void setMultiUrls(List<String> multiUrls) {
        this.multiUrls = multiUrls;
    }

    public List<String> getAttachmentUrls() {
        return attachmentUrls;
    }

    public void setAttachmentUrls(List<String> attachmentUrls) {
        this.attachmentUrls = attachmentUrls;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(String categoryIds) {
        this.categoryIds = categoryIds;
    }


}
