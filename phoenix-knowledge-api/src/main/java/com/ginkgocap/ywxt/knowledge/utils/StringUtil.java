package com.ginkgocap.ywxt.knowledge.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by gintong on 16-10-25.
 */
public class StringUtil
{
    public static boolean inValidString(final String string)
    {
        return StringUtils.isBlank(string) || "null".equalsIgnoreCase(string);
    }
}
