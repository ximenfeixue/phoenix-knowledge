package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gintong on 2016/7/8.
 */
public class Page<E> implements Serializable
{
	private static final long serialVersionUID = 4313491047942049436L;

	private long totalCount;// 总数据数

    private int pageSize;// 该页内容数

    private int pageNo;// 页码 从1开始

    private List<E> list; // 存放任意实体

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }
}
