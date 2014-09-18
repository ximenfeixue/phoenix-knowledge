package com.ginkgocap.ywxt.knowledge.util;

import org.apache.commons.lang3.StringUtils;

import com.ginkgocap.ywxt.knowledge.util.Constants.Type;

public class MongoUtils {

	public String getTableName(String v) {
		if (StringUtils.isBlank(v))
			return null;
		Type[] type = Type.values();
		for (Type t : type) {
			if (t.v() == Integer.parseInt(v)) {
				return t.obj();
			}
		}
		return null;
	}

	public String getCollectionName(String className) {
		return className.substring(className.lastIndexOf(".") + 1,
				className.length());
	}
}
