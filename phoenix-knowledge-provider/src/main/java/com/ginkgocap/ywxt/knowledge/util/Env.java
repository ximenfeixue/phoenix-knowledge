package com.ginkgocap.ywxt.knowledge.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取properties配置文件
 * 
 * @author lk
 */

public class Env extends Properties {
	private static Env instance;
	public static Env getInstance() {
		if (null != instance) {
			return instance;
		} else {
			makeInstance();
			return instance;
		}
	}

	private static synchronized void makeInstance() {
		if (instance == null) {
			instance = new Env();
		}
	}

	private Env() {
		InputStream is = getClass().getResourceAsStream("/conf/dubbo.properties");
		try {
			load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String [] args){
	}

}
