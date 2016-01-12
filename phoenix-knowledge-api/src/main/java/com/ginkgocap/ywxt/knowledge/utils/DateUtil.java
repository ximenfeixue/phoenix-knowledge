package com.ginkgocap.ywxt.knowledge.utils;

import java.text.ParseException;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

public class DateUtil {

    public static final String YYYYMMDD = "yyyy-MM-dd";

    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm:ss";

    
    public static Date parseWithYYYYMMDDHHMMSS(String dateStr){
    	
    	Date date = null;
    	
    	if(!StringUtils.isEmpty(dateStr)){
    		try {
    			date = DateUtils.parseDate(dateStr, new String[]{YYYYMMDDHHMMSS});
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	return date;
    	
    }
    
    public static Date parseWithYYYYMMDD(String dateStr){
    	
    	Date date = null;
    	
    	if(!StringUtils.isEmpty(dateStr)){
    		try {
    			date = DateUtils.parseDate(dateStr, new String[]{YYYYMMDD});
    		} catch (ParseException e) {
    		}
    	}
    	
    	return date;
    	
    }
    
    public static String formatWithYYYYMMDD(Date date){
    	if(date == null){
    		return null;
    	}
    	return DateFormatUtils.format(date, YYYYMMDD);
    }
    
    public static String formatWithYYYYMMDDHHMMSS(Date date){
    	if(date == null){
    		return null;
    	}
    	return DateFormatUtils.format(date, YYYYMMDDHHMMSS);
    }
    
    /**
     * 查询昨天开始时间 00:00:00
     * @return
     */
    public static Date getYesterdayBegin() {
        Date date = new Date();
        date = DateUtils.addDays(date, -1);
        date = DateUtils.setHours(date, 0);
        date = DateUtils.setMinutes(date, 0);
        date = DateUtils.setSeconds(date, 0);
        return date;
    }

    /**
     * 查询当天开始时间 00:00:00
     * @return
     */
    public static Date getTodayBegin() {
        Date date = new Date();
        date = DateUtils.setHours(date, 0);
        date = DateUtils.setMinutes(date, 0);
        date = DateUtils.setSeconds(date, 0);
        return date;
    }

    public static Date getTomorrowBegin() {
        Date date = new Date();
        date = DateUtils.addDays(date, 1);
        date = DateUtils.setHours(date, 0);
        date = DateUtils.setMinutes(date, 0);
        date = DateUtils.setSeconds(date, 0);
        return date;
    }
    
    public static Date subDays(Date date,int days) {
    	if(date == null){
    		return null;
    	}
        date = DateUtils.addDays(date, -days);
        return date;
    }

	public final static long getDateString(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 20);
		return cal.getTimeInMillis();
	}
	
	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		java.util.Date beginDate;
		java.util.Date endDate;
		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
			day = (endDate.getTime() - beginDate.getTime())
					/ (24 * 60 * 60 * 1000);
			// System.out.println("相隔的天数="+day);
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return day/365;
	}

}
