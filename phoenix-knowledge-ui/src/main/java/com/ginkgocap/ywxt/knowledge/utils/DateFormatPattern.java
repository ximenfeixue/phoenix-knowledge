/*
 * ----------------------------------------------------------
 * FILE         : DatePattern
 * CREATEUSER   : Timothy
 * CREATEDATE   : 2007-11-7
 * FILENAME     : DatePattern.java
 * DESCRIPTION  : 
 * MODIFIES     : 
 * MODIFIER     : 
 * MODIFIEDDATE : 
 * COMMENT      : 
 * ----------------------------------------------------------
 */

package com.ginkgocap.ywxt.knowledge.utils;


public enum DateFormatPattern {

    /**
     * default date : "yyyy-M-d"
     */
    DEFAULT_DATE("yyyy-M-d"),

    /**
     * default date : "yyyy-M-d"
     */
    SIMPLE_DATE("yyyy-M-d"),

    /**
     * default full date : "yyyy-MM-dd"
     */
    DEFAULT_FULL_DATE("yyyy-MM-dd"),

    /**
     * default full date : "yyyyMMdd"
     */
    DEFAULT_FULL_DATE_NO_PARTITION("yyyyMMdd"),

    /**
     * default full date : "YYYYMMDD"
     */
    DEFAULT_FULL_DATE_NO_PARTITION_DB("YYYYMMDD"),
    
    /**
     * default full time : "HH24:MI:ss"
     */
    DEFAULT_FULL_TIME("HH24:MI:ss"),

    /**
     * default full timestamp : "yyyy-MM-dd HH:mm:ss"
     */
    DEFAULT_FULL_TIMESTAMP("yyyy-MM-dd HH:mm:ss"),
    /**
     * default full timestamp : "yyyy年MM月dd日HH:mm"
     */
    DEFAULT_TIMESTAMP("yyyy年MM月dd日HH:mm"),
    /**
     * default full timestamp : "yyyy-MM-dd HHmmss"
     */
    DEFAULT_FULL_TIMESTAMP_HALF_PARTITION("yyyy-MM-dd HHmmss"),

    /**
     * default full timestamp : "YYYY-MM-DD HH24:MI:SS"
     */
    DEFAULT_FULL_TIMESTAMP_DB("YYYY-MM-DD HH24:MI:SS"),
    
    /**
     * default full timestamp : "yyyyMMddHHmmss"
     */
    DEFAULT_FULL_TIMESTAMP_NO_PARTITION("yyyyMMddHHmmss"),

    /**
     * default full timestamp : "YYYYMMDDHH24MISS"
     */
    DEFAULT_FULL_TIMESTAMP_NO_PARTITION_DB("YYYYMMDDHH24MISS"),
    
    /**
     * default year : "yyyy"
     */
    DEFAULT_YEAR("yyyy"),

    /**
     * default month : "MM"
     */
    DEFAULT_MONTH("MM"),
    
    /**
     * formate ： "yyyyMM"
     */
    DEFAULT_YEAR_MONTH("yyyyMM"),

    /**
     * formate ： "yyyy-MM"
     */
    DEFAULT_YEAR_MONTH_PARTITION("yyyy-MM"),
    
    /**
     * year 后两位 : "yy"
     */
    YEAR_LAST2("yy");

    private String pattern;

    private DateFormatPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

}
