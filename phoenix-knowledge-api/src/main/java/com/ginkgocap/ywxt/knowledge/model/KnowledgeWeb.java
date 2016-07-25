package com.ginkgocap.ywxt.knowledge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ginkgocap.ywxt.knowledge.model.common.IdName;
import com.ginkgocap.ywxt.knowledge.model.common.IdNameType;
import com.ginkgocap.ywxt.knowledge.model.common.KnowledgeDetail;

import java.util.List;

/**
 * Created by gintong on 2016/7/19.
 */
public class KnowledgeWeb extends Knowledge
{
    //private IdName column;
    private IdName column;

    private List<IdName> minTags;

    private List<IdNameType> minDirectorys;

    public KnowledgeWeb(Knowledge detail,List<IdName> minTags,List<IdNameType> minDirectory,IdName column)
    {
        this.setId(detail.getId());
        this.setColumnid(detail.getColumnid());

        this.setCid(detail.getCid());
        this.setCname(detail.getCname());
        this.setCid(detail.getCid());
        this.setCname(detail.getCname());
        this.setTitle(detail.getTitle());
        this.setContent(detail.getContent());
        this.setMultiUrls(detail.getMultiUrls());
        this.setAttachUrls(detail.getAttachUrls());
        this.setModifytime(detail.getModifytime());
        this.setCreatetime(detail.getCreatetime());
        this.setS_addr(detail.getS_addr());
        this.setDirectorys(null);
        this.setTags(null);
        this.setVirtual(detail.getVirtual());
        this.setPic(detail.getPic());
        this.setHcontent(detail.getHcontent());
        this.setCollected(detail.getCollected());
        this.setCpathid(detail.getCpathid());
        this.setDesc(detail.getDesc());
        this.setSource(detail.getSource());

        //Web data
        this.setColumn(column);
        this.setMinTags(minTags);
        this.setMinDirectorys(minDirectory);
    }

    public IdName getColumn() {
        return column;
    }

    public void setColumn(IdName column) {
        this.column = column;
    }

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
    public void setTags(String tags){}

    @JsonIgnore
    public void setTagList(List<Long> tags){}

    @JsonIgnore
    public void setDirectorys(List<Long> categoryIds){}

    @JsonIgnore
    public void setColumnid(String columnId) {}
}
