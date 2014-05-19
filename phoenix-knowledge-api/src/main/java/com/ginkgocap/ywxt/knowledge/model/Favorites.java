package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

/**收藏夹
 * @author liuyang
 *
 */
public class Favorites implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String type;//类型模块
	private long moduleId;//类型模块的id
	private long userId;//收藏人
	private Object target;//存储的对象
	private String title;//标题
	private String content;//内容
	private String ctime;//创建时间
	private String fbrName;
	private long fbrId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getModuleId() {
		return moduleId;
	}
	public void setModuleId(long moduleId) {
		this.moduleId = moduleId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public Object getTarget() {
		return target;
	}
	public void setTarget(Object target) {
		this.target = target;
	}
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFbrName() {
		return fbrName;
	}
	public void setFbrName(String fbrName) {
		this.fbrName = fbrName;
	}
	public long getFbrId() {
		return fbrId;
	}
	public void setFbrId(long fbrId) {
		this.fbrId = fbrId;
	}
	
}
