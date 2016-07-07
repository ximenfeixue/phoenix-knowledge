package com.ginkgocap.ywxt.knowledge.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by gintong on 2016/7/7.
 */
public class KnowledgeDetailWeb extends KnowledgeDetail
{
	private static final long serialVersionUID = 8481118906779887261L;

	//private IdName column;
    private List<IdName> minTags;

    private List<IdNameType> minDirectorys;

    public KnowledgeDetailWeb(KnowledgeDetail detail,List<IdName> minTags,List<IdNameType> minDirectory)
    {
        this.setId(detail.getId());
        this.setColumnId(detail.getColumnId());

        this.setOwnerId(detail.getOwnerId());
        this.setOwnerName(detail.getOwnerName());
        this.setCid(detail.getCid());
        this.setCname(detail.getCname());
        this.setTitle(detail.getTitle());
        this.setContent(detail.getContent());
        this.setMultiUrls(detail.getMultiUrls());
        this.setAttachmentUrls(detail.getAttachmentUrls());
        this.setModifyUserId(detail.getModifyUserId());
        this.setCreateTime(detail.getCreateTime());
        this.setModifyTime(detail.getModifyTime());
        this.setS_addr(detail.getS_addr());
        this.setCategoryIds(null);
        this.setTags(null);
        this.setVirtual(detail.getVirtual());
        this.setPic(detail.getPic());
        this.setHcontent(detail.getHcontent());
        this.setCollected(detail.getCollected());
        this.setCPath(detail.getCPath());
        this.setDesc(detail.getDesc());
        this.setSource(detail.getSource());

        //Web data
        this.setMinTags(minTags);
        this.setMinDirectorys(minDirectory);
    }

    /*
    public IdName getColumn() {
        return column;
    }

    public void setColumn(IdName column) {
        this.column = column;
    }*/



    public List<IdName> getMinTags() {
        return minTags;
    }

    public void setMinTags(List<IdName> minTags) {
        this.minTags = minTags;
    }

    public List<IdNameType> getMinDirectorys() {
        return minDirectorys;
    }

    public void setMinDirectorys(List<IdNameType> minDirectorys) {
        this.minDirectorys = minDirectorys;
    }

    @JsonIgnore
    public void setTags(List<Long> tags){}

    @JsonIgnore
    public void setCategoryIds(List<Long> categoryIds){}

    @JsonIgnore
    public void setColumnId(short columnId) {}


}
