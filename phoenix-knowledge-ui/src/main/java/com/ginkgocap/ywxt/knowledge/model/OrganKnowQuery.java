package com.ginkgocap.ywxt.knowledge.model;

/**
 * Created by wang fei on 2018/1/25.
 */
public class OrganKnowQuery {

    private int page;

    private int size;

    private Long organId;

    private String keyword;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Long getOrganId() {
        return organId;
    }

    public void setOrganId(Long organId) {
        this.organId = organId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
