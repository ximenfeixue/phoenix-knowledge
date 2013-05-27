package com.ginkgocap.ywxt.knowledge.util;


import com.ginkgocap.ywxt.knowledge.model.Article;

public class HTMLTemplate {


	public String getTemplate(Article article){
		String title = article.getArticleTitle();
		String content = article.getArticleContent();
		if (content != null){
			content = Snippet.imgFilter(content);
		}
//		System.out.println("content------------------------------------------" + content);
		StringBuffer HTML = new StringBuffer();
		HTML.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
		HTML.append("<HTML><HEAD><META HTTP-EQUIV=\"CONTENT-TYPE\" CONTENT=\"text/html; charset=UTF-8\"><TITLE>" + title + "</TITLE>");
		HTML.append("</HEAD>");
//		HTML.append("<META NAME=\"GENERATOR\" CONTENT=\"OpenOffice.org 3.4.1  (Unix)\">");
//		HTML.append("<META NAME=\"AUTHOR\" CONTENT=\"yx\">");	
//		HTML.append("<META NAME=\"CREATED\" CONTENT=\"20130403;7060000\">");	
//		HTML.append("<META NAME=\"CHANGEDBY\" CONTENT=\"yx\">");	
//		HTML.append("<META NAME=\"CHANGED\" CONTENT=\"20130408;3100000\">");
//		HTML.append("<META NAME=\"AppVersion\" CONTENT=\"14.0000\">");
//		HTML.append("<META NAME=\"Company\" CONTENT=\"yx\">");	
//		HTML.append("<META NAME=\"DocSecurity\" CONTENT=\"0\">");	
//		HTML.append("<META NAME=\"HyperlinksChanged\" CONTENT=\"false\">");
//		HTML.append("<META NAME=\"LinksUpToDate\" CONTENT=\"false\">");
//		HTML.append("<META NAME=\"ScaleCrop\" CONTENT=\"false\">");	
//		HTML.append("<META NAME=\"ShareDoc\" CONTENT=\"false\">");
		HTML.append("<BODY>");
		HTML.append(title);
		HTML.append("<HR><BR/>");
		HTML.append(content);
		HTML.append("</BODY></HTML>");
//		System.out.println("--------------------------------------------------------------------------" + HTML.toString());
		return HTML.toString();
	}
}
