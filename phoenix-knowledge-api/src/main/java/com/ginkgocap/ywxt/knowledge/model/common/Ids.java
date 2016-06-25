package com.ginkgocap.ywxt.knowledge.model.common;

/**
 * @Title: 主键（mongoDB中主键生成策略的对象）
 * @Description: TODO
 * @date 2016年1月13日 下午2:51:32
 * @version V1.0.0
 */
public class Ids {
	
	/**主键*/
	private String _id;
	
	/**自增器名称*/
	private String name;
	
	/**自增值*/
	private Long cid;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}
}
