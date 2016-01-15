package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

/**
 * @Title: 栏目数据的集合对象
 * @Description: TODO
 * @author 周仕奇
 * @date 2016年1月15日 上午10:31:31
 * @version V1.0.0
 */
public class ColumnCollection implements Serializable {

	
	private static final long serialVersionUID = -424912985959502809L;
	
	/**系统栏目*/
	private ColumnSys sys;
	
	/**用户自定义栏目*/
	private ColumnSelf self;
	
	/**用户定制栏目*/
	private ColumnCustom custom;

	public ColumnSys getSys() {
		return sys;
	}

	public void setSys(ColumnSys sys) {
		this.sys = sys;
	}

	public ColumnSelf getSelf() {
		return self;
	}

	public void setSelf(ColumnSelf self) {
		this.self = self;
	}

	public ColumnCustom getCustom() {
		return custom;
	}

	public void setCustom(ColumnCustom custom) {
		this.custom = custom;
	}
	
	
}