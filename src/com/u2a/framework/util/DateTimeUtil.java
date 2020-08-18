package com.u2a.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class DateTimeUtil {
	private static int qiShiXingQi = Calendar.SATURDAY;
	
	public static int dataToYear(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		return ca.get(Calendar.YEAR);
	}
	public static int dataToMonth(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		return  ca.get(Calendar.MONTH) + 1;
	}
	public static int dataToDay(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);

		return  ca.get(Calendar.DAY_OF_MONTH);
	}
	/**
	 * 取得当前日期是多少周
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(qiShiXingQi);
		c.setMinimalDaysInFirstWeek(7);
		c.setTime(date);
		return c.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 得到某一年周的总数
	 * 
	 * @param year
	 * @return
	 */
	public static int getMaxWeekNumOfYear(int year) {
		Calendar c = new GregorianCalendar();
		c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);

		return getWeekOfYear(c.getTime());
	}

	/**
	 * 取得指定日期所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(qiShiXingQi);
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime();
	}

	/**
	 * 取得指定日期所在周的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(qiShiXingQi);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();
	}

	/**
	 * 取得当前日期所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek() {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(qiShiXingQi);
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime();
	}

	/**
	 * 取得当前日期所在周的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek() {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(qiShiXingQi);
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();
	}

	public static Date nextdate(Date date, int a) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(cal.DATE, a);
		return cal.getTime();
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/**
	 * 得到几天前的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 获取某月第1天
	 * 
	 * @param someDate
	 *            "yyyy-MM"
	 * @return
	 */
	public static Date getMonthFirstDay(Date someDate) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(someDate);
		ca.set(Calendar.DAY_OF_MONTH, 1);
		return ca.getTime();
	}

	/**
	 * 获取某月最后1天
	 * 
	 * @param someDate
	 *            "yyyy-MM"
	 * @return
	 */
	public static Date getMonthLastDay(Date someDate) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(someDate);
		final int lastDay = ca.getActualMaximum(Calendar.DAY_OF_MONTH);
		Date lastDate = ca.getTime();
		lastDate.setDate(lastDay);
		return lastDate;
	}

	/**
	 * 获取下一个月第一天
	 * 
	 * @param someDate
	 * @return
	 */
	public static Date getNextMonth(Date someDate) {
		int year = Integer.valueOf(date2str("yyyy", someDate));
		int Month = Integer.valueOf(date2str("MM", someDate));
		Calendar cal = Calendar.getInstance();
		cal.set(year, Month, 1);
		cal.add(GregorianCalendar.MONTH, 0);
		return cal.getTime();
	}
	/**
	 * 获取上一个月的第几天
	 * 
	 * @param someDate
	 * @return
	 */
	public static Date getBeforeMonth(Date someDate,int day) {
		int year = Integer.valueOf(date2str("yyyy", someDate));
		int Month = Integer.valueOf(date2str("MM", someDate));
		Calendar cal = Calendar.getInstance();
		cal.set(year, Month,day);
		cal.add(GregorianCalendar.MONTH,-2);
		return cal.getTime();
	}

	/**
	 * 获取某年的第几周的开始日期
	 * 
	 * @param year
	 *            要获得的年
	 * @param week
	 *            第几个星期
	 * @param flag
	 *            当为true时返回第一天,false则返回最后一天
	 * @return java.uilt.Date 类型日期
	 * 
	 */
	public static Date getDayByWeek(int year, int week, boolean falg) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);

		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);
		if (falg) {
			return getFirstDayOfWeek(cal.getTime());
		} else {
			return getLastDayOfWeek(cal.getTime());
		}
	}

	/**
	 * 日期转换为字符串
	 * 
	 * @param d
	 *            日期值
	 * @param pattern
	 *            格式化类型
	 * @return 转换后日期
	 */
	public static String date2str(String pattern, Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(d);
	}

	/**
	 * 字符串转换为日期时间
	 * 
	 * @param value
	 *            字符串型日期值时间
	 * @param pattern
	 *            格式化类型
	 * @return 转换后日期时间
	 */
	public static Date str2Date(String pattern, String value) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 取得给定日期加上一定天数后的日期对象.
	 */
	public static Date getDateAmountDays(Date date, int amount) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(GregorianCalendar.DATE, amount);
		return cal.getTime();
	}

	/**
	 * 得到当前日期
	 */

	/*
	 * 字符串
	 */

	// 格林尼治时间
	public static String getGMTDateStr(String parton) {
		if (parton == null || parton.trim().equals("")) {
			parton = "yyyy-MM-dd HH:mm:ss";
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(parton);
		return sdf.format(date);
	}

	// 北京时间
	public static String getLocalDateStr(String parton) {
		if (parton == null || parton.trim().equals("")) {
			parton = "yyyy-MM-dd HH:mm:ss";
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(parton);
		sdf.setTimeZone(new SimpleTimeZone(28800000, "Asia/Shanghai"));// 设置时区
		// 北京时间
		return sdf.format(date);
	}

	/*
	 * Date
	 */
	// 北京时间
	public static Date getLocalDate(String parton) {
		if (parton == null || parton.trim().equals("")) {
			parton = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(parton);
		try {
			return sdf.parse(getLocalDateStr(null));
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}
	public static Date getLocalDate() {
		String parton = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(parton);
		try {
			return sdf.parse(getLocalDateStr(null));
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	/**
	 * String 转Date
	 */
	public static Date toDate(String date, String pattern) {
		Date date2;

		if (Util.checkNull(pattern)) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		try {
			if (date != null && !date.equals("")) {
				SimpleDateFormat sdf = new SimpleDateFormat(pattern);
				date2 = sdf.parse(date);
			} else {
				date2 = toLocalDate(new Date(), pattern);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
		return date2;
	}

	/**
	 * Date 转LocalDate
	 */
	public static Date toLocalDate(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		TimeZone zone = new SimpleTimeZone(28800000, "Asia/Shanghai");// +8区
		sdf.setTimeZone(zone);
		String sdate = sdf.format(date);

		SimpleDateFormat sdf2 = new SimpleDateFormat(pattern);
		try {
			return sdf2.parse(sdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	/*
	 * 北京时间
	 */
	@SuppressWarnings("deprecation")
	public static int getLocalYear() {
		return getLocalDate().getYear() + 1900;

	}

	@SuppressWarnings("deprecation")
	public static int getLocalMonth() {
		return getLocalDate().getMonth() + 1;

	}

	@SuppressWarnings("deprecation")
	public static int getLocalDay() {
		return getLocalDate().getDate();

	}

	@SuppressWarnings("deprecation")
	public static int getLocalHours() {
		return getLocalDate().getHours();

	}

	@SuppressWarnings("deprecation")
	public static int getLocalWeekDay() {
		return getLocalDate().getDay();

	}

}