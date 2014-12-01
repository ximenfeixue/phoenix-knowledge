package com.ginkgocap.ywxt.knowledge.util;

public class HTTPUrlConfig {
	
	/**搜索地址**/
	private String searchUrl;
	
	/**解析地址**/
	private String parseUrl;
	
	/**分享到金桐脑推送地址**/
	private String pushUrl;
	
	/**最热排序URL**/
	private String hotSortUrl;
	
	/**评论排序URL**/
	private String commentSortUrl;
	
	
	public String getHotSortUrl() {
		return hotSortUrl;
	}
	public void setHotSortUrl(String hotSortUrl) {
		this.hotSortUrl = hotSortUrl;
	}
	public String getCommentSortUrl() {
		return commentSortUrl;
	}
	public void setCommentSortUrl(String commentSortUrl) {
		this.commentSortUrl = commentSortUrl;
	}
	public String getSearchUrl() {
		return searchUrl;
	}
	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}
	public String getParseUrl() {
		return parseUrl;
	}
	public void setParseUrl(String parseUrl) {
		this.parseUrl = parseUrl;
	}
	public String getPushUrl() {
		return pushUrl;
	}
	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
	}

}
