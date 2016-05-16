package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2016/5/16.
 */
public class TagItems implements Serializable {

    public long getKnowlegeId() {
        return knowlegeId;
    }

    public void setKnowlegeId(long knowlegeId) {
        this.knowlegeId = knowlegeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    private long knowlegeId;
    private String title;
    List<Long> tagIds;
}
