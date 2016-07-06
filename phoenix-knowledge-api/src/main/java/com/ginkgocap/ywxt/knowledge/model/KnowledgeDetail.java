package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chen Peifeng on 2016/4/7.
 */
public class KnowledgeDetail implements Serializable {

    private static final long serialVersionUID = 1564469001269897935L;
    /** 知识id **/
    private long id;

    /** 栏目类型 **/
    private short columnId;

    //1. 组织， 2. 人
    private short virtual;
    
    /** 栏目路径**/
    private String cpath;

	/** 知识标题 **/
    private String title;
    /** 知识介绍 **/
    private String content;

    private String hcontent;

    /** 知识所有者id **/
    private long ownerId;
    /** 知识所有者姓名 **/
    private String ownerName;
    
	// 创建人id
	private long cid;

	// 创建人名称
	private String cname;

    // 来源
    private String source;

    // 来源地址
    private String s_addr;

    // 封面地址
    private String pic;

    // 描述
    private String desc;

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

    private List<Long> tags;

    /** 存储目录 **/
    private List<Long> categoryIds;

    // 是否加精
    private short essence;

    // 是否收藏
    private short collected;

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

    public short getVirtual() {
        return virtual;
    }

    public void setVirtual(short virtual) {
        this.virtual = virtual;
    }


    public String getCpath() {
		return cpath;
	}

	public void setCpath(String cpath) {
		this.cpath = cpath;
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

	public long getCid() {
		return cid;
	}

	public void setCid(long cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

    public String getHcontent() {
        return hcontent;
    }

    public void setHcontent(String hcontent) {
        this.hcontent = hcontent;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getS_addr() {
        return s_addr;
    }

    public void setS_addr(String s_addr) {
        this.s_addr = s_addr;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public List<Long> getTags() {
        return tags;
    }

    public void setTags(List<Long> tags) {
        this.tags = tags;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public short getEssence() {
        return essence;
    }

    public void setEssence(short essence) {
        this.essence = essence;
    }

    public short getCollected() {
        return collected;
    }

    public void setCollected(short collected) {
        this.collected = collected;
    }
}
