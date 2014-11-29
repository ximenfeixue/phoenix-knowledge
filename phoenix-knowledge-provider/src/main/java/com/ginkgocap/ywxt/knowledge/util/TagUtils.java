package com.ginkgocap.ywxt.knowledge.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class TagUtils {

	private final static String split = ",";

	public String formatTag(String tags) {

		if (StringUtils.isBlank(tags))
			return null;

		return tags.replaceAll("，", split);

	}

	public String[] getTagListByTags(String tags) {
		String currTags = formatTag(tags);
		if (StringUtils.isBlank(currTags))
			return null;
		return currTags.split(split);
	}
}
