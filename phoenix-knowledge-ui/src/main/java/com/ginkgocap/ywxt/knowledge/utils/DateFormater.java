package com.ginkgocap.ywxt.knowledge.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Created by gintong on 2016/7/24.
 */
public class DateFormater {

    private static Logger logger = Logger.getLogger(DateFormater.class);

    /**
     * 格式化日期 为 yyyy/MM/dd 的样式
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return formatDate(date, DateFormatPattern.DEFAULT_FULL_DATE);
    }

    /**
     * 格式化日期 为 yyyy/MM/dd HH:mm:ss的样式
     *
     * @param date
     * @return
     */
    public static String getStrTimestamp(Date date) {
        return formatDate(date, DateFormatPattern.DEFAULT_FULL_TIMESTAMP);
    }

    /**
     * 將系統當前格式化日期 为 yyyy/MM/dd 的样式
     *
     * @return
     */
    public static String getSystemCurrentDate() {
        return formatDate(Calendar.getInstance().getTime(), DateFormatPattern.DEFAULT_FULL_DATE);
    }

    /**
     * 將系統當前格式化日期 为 yyyyMMdd 的样式
     *
     * @return
     */
    public static String getSystemCurrentDateSimple() {
        return formatDate(Calendar.getInstance().getTime(), DateFormatPattern.DEFAULT_FULL_DATE_NO_PARTITION);
    }

    /**
     * 將系統前一天格式化日期 为 yyyyMMdd 的样式
     *
     * @return
     */
    public static String getSystemBeforeDayDateSimple() {
        // 取得前一天的日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return formatDate(calendar.getTime(), DateFormatPattern.DEFAULT_FULL_DATE_NO_PARTITION);
    }

    /**
     * 格式化日期 为 DateFormatPattern 的样式
     *
     * @param pattern
     * @param date
     * @return
     */
    public static String formatDate(Date date, DateFormatPattern pattern) {

        if (date != null) {
            return new SimpleDateFormat(pattern.getPattern()).format(date);
        } else {
            return "";
        }
    }


    /**
     * 将传入的日期转成制定格式的字串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        if (date != null) {
            return new SimpleDateFormat(pattern).format(date);
        } else {
            return "";
        }
    }

    /**
     * 将当前系统日期转成制定格式的字串
     *
     * @param pattern
     * @return
     */
    public static String formatDate(String pattern) {
        return formatDate(Calendar.getInstance().getTime(), pattern);
    }

    /**
     * 格式化时间 为 hh:mm:ss 的样式
     *
     * @param date
     * @return
     */
    public static String formatTime(Date date, String pattern) {

        if (date != null) {
            return new SimpleDateFormat(pattern).format(date);
        } else {
            return "";
        }
    }

    /**
     * 计算 日期 calendar1 与 calendar2 之间的所查的天数 ,注：return calendar1 - calendar2
     *
     * @param calendar1 日期1
     * @param calendar2 日期2
     * @return 相差天数 = 日期1 - 日期2
     */
    public static int dateSubtraction(Calendar calendar1, Calendar calendar2) {
        long daterange = calendar1.getTime().getTime()
                - calendar2.getTime().getTime();
        long time = 1000 * 3600 * 24;

        return (int) (daterange / time);
    }


    /**
     * 计算 日期 date1 与 date2 之间的所相差的天数 ,
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 相差天数 = 日期1 - 日期2
     */
    public static String dateSubtraction(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return "";
        }

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        return String.valueOf(dateSubtraction(calendar1, calendar2));

    }

    /**
     * 將 yyyy/MM/dd 格式的日期字串，轉成Calendar物件
     *
     * @param strDate
     * @return
     */
    public static Calendar transDate(String strDate) {
        if (strDate != null && strDate.length() >= 8) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(java.sql.Date.valueOf(strDate.replace('/', '-')));

            return calendar;
        } else {
            return null;
        }
    }

    /**
     * 计算两个日期相差月数
     *
     * @param calendarDate1
     * @param calendarDate2
     * @return
     */
    public static int computeMonth(Calendar calendarDate1, Calendar calendarDate2) {
        int iMonth = 0;
        int flag = 0;
        try {

            logger.debug("calendarDate1:" + calendarDate1.getTime());
            logger.debug("calendarDate2:" + calendarDate2.getTime());

            if (calendarDate2.equals(calendarDate1)) {
                return 0;
            }

            if (calendarDate1.after(calendarDate2)) {
                Calendar temp = calendarDate1;
                calendarDate1 = calendarDate2;
                calendarDate2 = temp;
            }

            logger.debug("calendarDate1:" + calendarDate1.getTime());
            logger.debug("calendarDate2:" + calendarDate2.getTime());

            if (calendarDate2.get(Calendar.DAY_OF_MONTH) < calendarDate1
                    .get(Calendar.DAY_OF_MONTH)) {
                flag = 1;
            }

            if (calendarDate2.get(Calendar.YEAR) > calendarDate1
                    .get(Calendar.YEAR)) {
                iMonth = ((calendarDate2.get(Calendar.YEAR) - calendarDate1
                        .get(Calendar.YEAR))
                        * 12 + calendarDate2.get(Calendar.MONTH) - flag)
                        - calendarDate1.get(Calendar.MONTH);
            } else {
                iMonth = calendarDate2.get(Calendar.MONTH)
                        - calendarDate1.get(Calendar.MONTH) - flag;
            }
        } catch (Exception e) {
            logger.error("月份计算处理异常:" + e.getMessage());
        }
        logger.debug("iMonth:" + iMonth);

        return iMonth;
    }


    public static Date ConvertDate(Date date) {
        if (date == null) return null;
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dat = simple.format(date);
        Date result = null;
        try {
            result = simple.parse(dat);
        } catch (ParseException e) {
            logger.warn("日期转换异常");
        }
        return result;
    }


    public static void main(String[] args) {
        System.out.println("dispersionMonth:");
    }
}
