package com.ginkgocap.ywxt.knowledge.util;

import java.util.HashMap;
import java.util.Map;

public class Content {
	//导出导入的进度条被监控对象的map
	public final static Map MAP = new HashMap();
	//生成导出文件发布到tomcat下，tomcat的路径
	public final static String WEBSERVERPATH= "/root/apache-tomcat-6.0.36/webapps";
	//导出时生成的文件目录
	public final static String EXPORTDOCPATH = "/webserver/uploadfile/knowledge/GENFILE";
	//导出时附件服务器上存放附件的地址MOUNT到本地的路径
	public final static String EXPORTMOUNTPATH = "/webserver/uploadfile/knowledge";
}
