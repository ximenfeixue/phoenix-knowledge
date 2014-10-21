package com.ginkgocap.ywxt.knowledge.util;

import org.apache.commons.lang3.StringUtils;

public class ValidateUtils {

	public static boolean validateStringBlank(String... str) {
		boolean flag = true;
		if (str == null || str.length == 0)
			return !flag;
		for (String s : str) {
			if (StringUtils.isBlank(s)) {
				flag = false;
				break;
			}
		}
		return flag;
	}
}
