package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

/**
 * Created by gintong on 2016/7/9.
 */
public class KnowledgeUnique implements Serializable
{
	private static final long serialVersionUID = -4214697426857729388L;

	//KnowledgeId
    private long id;

    //columnId
    private int columnId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }
}
