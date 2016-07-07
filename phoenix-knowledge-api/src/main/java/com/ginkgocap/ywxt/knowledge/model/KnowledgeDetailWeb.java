package com.ginkgocap.ywxt.knowledge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created by gintong on 2016/7/7.
 */
public class KnowledgeDetailWeb extends KnowledgeDetail
{
    public Pair getColumn() {
        return column;
    }

    public void setColumn(Pair column) {
        this.column = column;
    }

    public List<Pair> getDirectory() {
        return directory;
    }

    public void setDirectory(List<Pair> directory) {
        this.directory = directory;
    }

    public List<Pair> getTagList() {
        return tagList;
    }

    public void setTagList(List<Pair> tagList) {
        this.tagList = tagList;
    }

    @JsonIgnore
    public void setTags(List<Long> tags){}

    @JsonIgnore
    public void setCategoryIds(List<Long> categoryIds){}

    @JsonIgnore
    public void setColumnId(short columnId) {}

    private Pair column;
    private List<Pair> tagList;
    private List<Pair> directory;
}
