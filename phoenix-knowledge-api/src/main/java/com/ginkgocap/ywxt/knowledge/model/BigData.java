package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

/**
 * Created by oem on 12/16/16.
 */
public class BigData implements Serializable
{
    public long getKid() {
        return kid;
    }

    public void setKid(long kid) {
        this.kid = kid;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCpathid() {
        return cpathid;
    }

    public void setCpathid(String cpathid) {
        this.cpathid = cpathid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public short getPublicFlag() {
        return publicFlag;
    }

    public void setPublicFlag(short publicFlag) {
        this.publicFlag = publicFlag;
    }

    public short getConnectFlag() {
        return connectFlag;
    }

    public void setConnectFlag(short connectFlag) {
        this.connectFlag = connectFlag;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getColumnid() {
        return columnid;
    }

    public void setColumnid(int columnid) {
        this.columnid = columnid;
    }

    public int getColumnType() {
        return columnType;
    }

    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    private long kid;
    private long cid;
    private String cname;
    private String title;
    private String cpathid;
    private String pic;
    private short publicFlag;
    private short connectFlag;
    private short status;
    private String tags;
    private int columnid;
    private int columnType;
    private String content;
    private String desc;
    private long createtime;

    public static BigData clone(KnowledgeBase base, short connectFlag) {
        BigData data = new BigData();

        data.setKid(base.getId());
        data.setCid(base.getCreateUserId());
        data.setCname(base.getCreateUserName());
        data.setTitle(base.getTitle());
        data.setCpathid(base.getCpath());
        data.setPic(base.getCoverPic());
        data.setPublicFlag(base.getPrivated() == 1 ? (short) 0 : 1);
        data.setConnectFlag(connectFlag);
        data.setStatus(base.getStatus());
        data.setTags(base.getTags());
        data.setColumnid(base.getColumnId());
        data.setColumnType(base.getType());
        data.setContent(base.getContent());
        data.setDesc(base.getContentDesc());
        data.setCreatetime(base.getCreateDate());

        return data;
    }
}
