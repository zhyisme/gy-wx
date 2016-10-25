package org.gy.framework.util.convert;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期类型与字符串类型相互转换
 * 
 * @author 牛绍刚
 */
public class DateUtil {
    /** 记录日志 */
    private static final Logger  logger              = LoggerFactory.getLogger(DateUtil.class);

    /** 24小时制 yyyy-MM-dd HH:mm:ss */
    public static final String   DATETIMEPATTERN24H  = "yyyy-MM-dd HH:mm:ss";

    /** 24小时制 yyyyMMddHHmmss */
    public static final String   YYYYMMDDHHMMSS      = "yyyyMMddHHmmss";

    /** Base ISO 8601 Date format yyyyMMdd i.e., 20021225 for the 25th day of December in the year 2002 */
    public static final String   ISO_DATE_FORMAT     = "yyyyMMdd";

    public static final String   DATE_FORMAT         = "yyyy-MM-dd";

    /** Default lenient setting for getDate. */
    private static final boolean LENIENTDATE         = false;

    /** 一天的秒数 **/
    private static final int     DAYSERCOND          = 24 * 60 * 60;
    /** timestamp最小值 1970-01-01 00:00:00 **/
    private static final long    TIMESTAMP_RANGE_MIN = -28800000L;
    /** timestamp最大值 2037-12-31 23:59:59 **/
    private static final long    TIMESTAMP_RANGE_MAX = 2145887999000L;

    /**
     * 功能描述: 验证时间戳是否在指定范围，避免保存到数据库报错<br>
     * timestamp最小值 1970-01-01 00:00:00，最大值 2037-12-31 23:59:59
     * 
     * @param longTime 待验证时间戳
     * @return true在指定范围，false不在
     * @version 2.0.0
     * @author guanyang/14050360
     */
    public static boolean validateTimestampRange(long longTime) {
        if (longTime >= TIMESTAMP_RANGE_MIN && longTime <= TIMESTAMP_RANGE_MAX) {
            return true;
        }
        return false;
    }

    /**
     * 返回当前日期字符串
     * 
     * @param pattern 日期字符串样式
     * @return
     */
    public static String getCurrentDateString(String pattern) {
        return dateToString(getCurrentDateTime(), pattern);
    }

    /**
     * 字符串转换为日期java.util.Date
     * 
     * @param dateText 字符串
     * @param format 日期格式
     * @return
     */
    public static Date stringToDate(String dateString,
                                    String format) {
        return stringToDate(dateString, format, LENIENTDATE);
    }

    /**
     * 字符串转换为日期java.sql.Date
     * 
     * @param dateText 字符串
     * @param format 日期格式
     * @return 数据库中的Date类型，只有年月日
     */
    public static java.sql.Date stringToSqlDate(String dateString,
                                                String format) {
        return new java.sql.Date(stringToDate(dateString, format, LENIENTDATE).getTime());
    }

    /**
     * 字符串转换为日期java.sql.Timestamp
     * 
     * @param dateText 字符串
     * @param format 日期格式
     * @return 数据库中的Date类型，只有年月日
     */
    public static java.sql.Timestamp stringToSqlTimestamp(String dateString,
                                                          String format) {
        return new java.sql.Timestamp(stringToDate(dateString, format, LENIENTDATE).getTime());
    }

    /**
     * 字符串转换为日期java.util.Date
     * 
     * @param dateText 字符串
     * @param format 日期格式
     * @param lenient 日期越界标志
     * @return
     */
    public static Date stringToDate(String dateText,
                                    String format,
                                    boolean lenient) {
        if (dateText == null) {
            return null;
        }
        DateFormat df = null;

        try {
            if (format == null) {
                df = new SimpleDateFormat();
            } else {
                df = new SimpleDateFormat(format);
            }
            df.setLenient(false);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            return df.parse(dateText);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 返回当前时间
     * 
     * @return 返回当前时间
     */
    public static Date getCurrentDateTime() {
        java.util.Calendar calNow = java.util.Calendar.getInstance();
        java.util.Date dtNow = calNow.getTime();

        return dtNow;
    }

    /**
     * 根据时间变量返回时间字符串
     * 
     * @return 返回时间字符串
     * @param pattern 时间字符串样式
     * @param date 时间变量
     */
    public static String dateToString(Object date,
                                      String pattern) {
        if (date == null) {
            return null;
        }
        try {
            SimpleDateFormat sfDate = new SimpleDateFormat(pattern);
            sfDate.setLenient(false);
            return sfDate.format(date);
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据时间变量返回时间字符串
     * 
     * @return 返回时间字符串
     * @param pattern 时间字符串样式
     * @param date 时间变量
     */
    public static Date dateFormate(Date date,
                                   String pattern) {
        if (date == null) {
            return null;
        }
        try {
            SimpleDateFormat sfDate = new SimpleDateFormat(pattern);
            sfDate.setLenient(false);
            try {
                return sfDate.parse(sfDate.format(date));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage(), e);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取几天的秒数
     * 
     * @param day
     * @return
     */
    public static int transformDayToSecond(int day) {
        return day * DAYSERCOND;
    }

    /**
     * 取得当前年
     * 
     * @return
     */
    public static int getCurrentYear() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        return Integer.parseInt(df.format(new Date()));
    }

    /**
     * 字符串转换成Timestamp类型 格式 :yyyy-MM-dd HH:mm:ss 张静萌
     * 
     * @param timeString
     * @return
     * @throws ParseException
     */
    public static Timestamp stringToTimestamp(String timeString) throws ParseException {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp result = null;

        if (timeString != null && !"".equals(timeString.trim())) {
            // 去掉尾部的.0
            if (timeString.contains(".")) {
                int index = timeString.indexOf(".");
                timeString = timeString.substring(0, index);
            }
            result = new Timestamp(dataFormat.parse(timeString).getTime());
        }
        return result;
    }

    /**
     * 校验某一个时间是否在某一个时间范围之内
     * 
     * @param time
     * @param startTime
     * @param endTime
     * @return 两个时间点差的秒数
     */
    public static boolean isInRange(Date time,
                                    Date startTime,
                                    Date endTime) {
        if (time == null || startTime == null || endTime == null) {
            return false;
        }

        if (time.after(startTime) && time.before(endTime)) {
            return true;
        }

        return false;
    }

    /**
     * 获取两个时间点的差，返回秒数
     * 
     * @param startTime
     * @param endTime
     * @return 两个时间点差的秒数
     */
    public static int getTimeDiff(Date startTime,
                                  Date endTime) {
        if (startTime == null || endTime == null) {
            return 0;
        }
        long time = endTime.getTime() - startTime.getTime();
        int totalS = Long.valueOf(time / 1000).intValue();
        return totalS;
    }

    /**
     * 时间加减
     * 
     * @param date
     * @param partten Calendar.DATE
     * @param count
     * @return
     */
    public static Date addTime(Date date,
                               int partten,
                               int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(partten, count);
        return calendar.getTime();
    }

    /**
     * 
     * 计算两个日期之间相差的天数
     * 
     * @param date1
     * 
     * @param date2
     * 
     * @return
     */

    public static int daysBetween(Date date1,
                                  Date date2)

    {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));

    }

    /**
     * 功能描述: 获取当日剩余秒数
     * 
     * @return
     */
    public static int getDayRemaindSeconds() {
        Calendar current = Calendar.getInstance();
        current.set(Calendar.MILLISECOND, 0);
        long currentTimes = current.getTimeInMillis();
        current.set(Calendar.HOUR_OF_DAY, 23);
        current.set(Calendar.MINUTE, 59);
        current.set(Calendar.SECOND, 59);
        long endTimes = current.getTimeInMillis();
        return (int) ((endTimes - currentTimes) / 1000);
    }

	/**
     * 将时间戳转化成date 北京时间
     * @param timestamp
     * @return
     * @throws ParseException 
     */
    public static Date timestampToDate(String timestamp){
    	SimpleDateFormat sdf =  new SimpleDateFormat("yyyyMMddHHmmss");
    	sdf.setLenient(false);
    	sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    	Date date = null;
		try {
			date = sdf.parse(timestamp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return date;
    }
    
    /**
     * 功能描述: 日期计算
     */
    public static String addDay(String date, String pattern, int days) {
    	Calendar c = Calendar.getInstance(); 
    	String result = "";
    	Date afterDate = null; 
        if (date != null && !"".equals(date.trim())) {
            SimpleDateFormat fsdf = new SimpleDateFormat(pattern);
            try {
                Date d = fsdf.parse(date);
                c.setTime(d);
                int day = c.get(Calendar.DATE); 
                c.set(Calendar.DATE, day + days); 
                afterDate = c.getTime();
                result = fsdf.format(afterDate);
            } catch (ParseException e) {
            	logger.error("addDay exception",e);
            }
        }
        return result;
    }

    public static Date addDay(Date date, int days) {
        Calendar c = Calendar.getInstance();
        Date afterDate = null;
        if (date != null ) {
            c.setTime(date);
            int day = c.get(Calendar.DATE);
            c.set(Calendar.DATE, day + days);
            afterDate = c.getTime();
        }
        return afterDate;
    }

    /**
     * 测试方法
     * 
     * @param args
     */
    public static void main(String[] args) {
        int num = getDayRemaindSeconds();
        System.out.println(num);
        // System.out.println(getCurrentYear());
        // System.out.println(stringToDate("2014-07-08", "yyyy-MM-dd"));
        // System.out.println(stringToSqlDate("2014-07-08 03:05:02", DATETIMEPATTERN24H));
        // System.out.println(stringToSqlTimestamp("2014-07-08 03:05:02", DATETIMEPATTERN24H));
        //
        // Date startTime = stringToSqlDate("2015-04-02 16:35:00", DATETIMEPATTERN24H);
        // Date endTime = stringToSqlDate("2015-04-02 16:36:30", DATETIMEPATTERN24H);
        // System.out.println(getTimeDiff(startTime, endTime));
        // Date a = addTime(DateUtil.getCurrentDateTime(), Calendar.DATE, 1);
        // System.out.println(DateUtil.getCurrentDateString(DateUtil.YYYYMMDDHHMMSS));
    }

}