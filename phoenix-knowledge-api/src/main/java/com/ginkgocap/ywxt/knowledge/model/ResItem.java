package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chen Peifeng on 2016/5/16.
 */
public class ResItem implements Serializable {
    private long resId; //knowledgeId
    List<Long> ids;
    
    public long getResId() {
		return resId;
	}
	public void setResId(long resId) {
		this.resId = resId;
	}
	public List<Long> getIds() {
		return ids;
	}
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
	
}
