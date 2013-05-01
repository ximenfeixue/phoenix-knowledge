package com.ginkgocap.ywxt.knowledge.util;

import java.io.IOException;
import java.util.Properties;

import com.ginkgocap.ywxt.util.PropertiesLoader;

public class ReadProperties {
	private static Properties props = null;

	static {
		try {
			props = PropertiesLoader.loadProperties("classpath:dubbo.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Properties getPro() {
		return props;
	}
}
