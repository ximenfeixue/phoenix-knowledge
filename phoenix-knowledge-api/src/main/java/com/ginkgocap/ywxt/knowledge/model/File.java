package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

public class File  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6239299762529342059L;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getArticleId() {
		return articleId;
	}
	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getUptime() {
		return uptime;
	}
	public void setUptime(String uptime) {
		this.uptime = uptime;
	}
	/** 主键 **/
	private long id;
	/** phoenix_knowledge.tb_article.id **/
	private long articleId;
	/** 文件名(文件原始名称，需在前端显示) **/
	private String showName;
	/** 上传后生成的新的文件名称 **/
	private String fileName;
	/** 文件扩展名 **/
	private String extension;
	/** 附件路径 **/
	private String filePath;
	/** 上传时间 **/
	private String uptime;
}
