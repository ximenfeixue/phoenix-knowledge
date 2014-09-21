package com.ginkgocap.ywxt.knowledge.util;

import org.apache.commons.lang3.StringUtils;

import com.ginkgocap.ywxt.knowledge.util.Constants.Relation;

public class TypeUtils {

	public String getRelationNameByType(String v) {
		if (StringUtils.isBlank(v))
			return null;
		Relation[] relation = Relation.values();
		for (Relation r : relation) {
			if (r.v() == Integer.parseInt(v)) {
				return r.c();
			}
		}
		return null;
	}
}
