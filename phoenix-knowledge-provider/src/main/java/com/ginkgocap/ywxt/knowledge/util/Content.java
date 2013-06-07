package com.ginkgocap.ywxt.knowledge.util;

import java.util.HashMap;
import java.util.Map;

public class Content {
	//导出导入的进度条被监控对象的map
	public final static Map MAP = new HashMap();
	//生成导出文件发布到tomcat下，tomcat的路径
	public final static String WEBSERVERPATH = Env.getInstance().getProperty("webserver.path");
	//导出时生成的文件目录
	public final static String EXPORTDOCPATH = Env.getInstance().getProperty("gen.path");
	//导出时附件服务器上存放附件的地址MOUNT到本地的路径
	public final static String EXPORTMOUNTPATH = Env.getInstance().getProperty("linux.mount.path");
	//nginx服务地址
	public final static String NGINXROOT = Env.getInstance().getProperty("nginx.root");
}
