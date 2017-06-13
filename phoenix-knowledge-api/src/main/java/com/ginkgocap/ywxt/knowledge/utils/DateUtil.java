package com.ginkgocap.ywxt.knowledge.utils;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

public class DateUtil {

    public static final String YYYYMMDD = "yyyy-MM-dd";

    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm:ss";

	public static final TimeZone GTM8TimeZone = TimeZone.getTimeZone("GMT+8");
	//private static Log log = LogFactory.getLog(DateUtil.class);
	private static final String dateFormat = "yyyy-MM-dd";
	private static final String dateTimeFormat = "MM/dd/yyyy HH:mm:ss";
	private static final String dateTimeFormatForChina = "yyyy-MM-dd HH:mm:ss";
	private static final String dateFormatForTeamCode = "yyyyMMdd";
    
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

	public static String currentTime()
	{
		return String.valueOf(System.currentTimeMillis());
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

	public static Date convertStringToDate(String time) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setTimeZone(GTM8TimeZone);
		return sdf.parse(time);
	}
	public static Date convertStringToDate(String time, String format) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(GTM8TimeZone);
		return sdf.parse(time);
	}

	public static Date convertStringToDateTime(String time) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
		sdf.setTimeZone(GTM8TimeZone);
		return sdf.parse(time);
	}

	/*
	public static Date convertStringToDateTimeForChina(String time) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormatForChina);
		sdf.setTimeZone(GTM8TimeZone);
		Date date = sdf.parse(time);
		return date;
	}*/

	public static Date convertStringToDateTimeForChina(String time){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormatForChina);
			sdf.setTimeZone(GTM8TimeZone);
			Date date = sdf.parse(time);
			return date;
		}catch(Exception e){
			return new Date(0);//默认为2010年1月1日
		}
	}

	public static String convertDateToStringForChina(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormatForChina);
		sdf.setTimeZone(GTM8TimeZone);
		return sdf.format(date);
	}

	public static String convertDateToString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setTimeZone(GTM8TimeZone);
		return sdf.format(date);
	}

	public static String convertDateToStringForTeamCode(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormatForTeamCode);
		sdf.setTimeZone(GTM8TimeZone);
		return sdf.format(date);
	}

	public static Date getSearchMonthFromDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(GTM8TimeZone);
		calendar.setTime(date);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getSearchMonthToDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(GTM8TimeZone);
		calendar.setTime(date);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getSearchYearFromDate(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(GTM8TimeZone);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getSearchYearToDate(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(GTM8TimeZone);
		calendar.set(Calendar.YEAR, year+1);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static String convertDateToString(Date date,String partten){
		SimpleDateFormat sdf = new SimpleDateFormat(partten);
		sdf.setTimeZone(GTM8TimeZone);
		return sdf.format(date);
	}

	public static Date getSearchFromDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(GTM8TimeZone);
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getSearchToDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(GTM8TimeZone);
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date makeDayByHourAndMinute(int hour, int minute){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		return calendar.getTime();
	}

	public static int getHourByDate(Date date){
		if(date == null){
			return 0;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinuteByDate(Date date){
		if(date == null){
			return 0;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 得到当前日期，只保留年、月、日的信息
	 * @return
	 */
	public static Date getCurrentDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(GTM8TimeZone);
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取当前日期 yyyy-MM-dd
	 * @return
	 */
	public String refFormatNowDate() {
		Date nowTime = new Date(System.currentTimeMillis());
		SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String retStrFormatNowDate = sdFormatter.format(nowTime);

		return retStrFormatNowDate;
	}
}
