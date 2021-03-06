package com.ginkgocap.ywxt.knowledge.model.common;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chen Peifeng on 2016/5/16.
 */
public class ResItem implements Serializable 
{
	private static final long serialVersionUID = 4260148152954320931L;
	
	private long resId; //knowledgeId
	private short type; //column type
    List<Long> ids;
    
    public long getResId() {
		return resId;
	}
	public void setResId(long resId) {
		this.resId = resId;
	}
	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}
	public List<Long> getIds() {
		return ids;
	}
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
}
