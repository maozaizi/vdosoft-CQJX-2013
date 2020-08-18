package com.u2a.framework.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.u2a.framework.commons.Constants;

/**
 * 数据库取值赋值类
 * 日期型，日期时间性，数字型，整数型，状态型（varchar2），字符型，text大字段
 * @author 吴明强 
 * @date 2010-08-16
 *
 */
@SuppressWarnings("unchecked")
public class HelperDB {
	/**
	 * 为修改数据赋值
	 * @param userName
	 * @param dataSet 
	 */
	public static void setModifyInfo(String userName, Map dataSet) {
		Date date =  new Date();
		HelperDB.setDateTime("modifyDate", dataSet, date);
		dataSet.put("modifyUser", userName);
	}
	/**
	 * 为创建数据赋值
	 * @param userName
	 * @param dataSet
	 */
	public static void setCreateInfo(String userName, Map dataSet) {
		Date date =  new Date();
		HelperDB.setDateTime("createDate", dataSet, date);
		HelperDB.setDateTime("modifyDate", dataSet, date);
		dataSet.put("createUser", userName);
		dataSet.put("modifyUser", userName);
	}
	/**
	 * 为创建数据赋值,带有效标志的
	 * @param userName
	 * @param dataSet
	 */
	public static void setCreate4isValid(String userName, Map dataSet) {
		setCreateInfo(userName,dataSet);
		dataSet.put("isValid", Constants.ISVALID);
	}
	/**
	 * 为创建数据赋值,带有效标志的
	 * @param userName
	 * @param dataSet
	 */
	public static void setCreate(String userName, Map dataSet) {
		setCreateInfo(userName,dataSet);
	}

	/**
	 * 获取str型日期
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @return 返回日期字符串
	 */
	public static String getStrDate(String fieldName, Map dataSet) {
		return (String) dataSet.get(fieldName);
	}

	/**
	 * 获取str型日期时间
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @return 返回日期时间字符串
	 */
	public static String getStrDateTime(String fieldName, Map dataSet) {
		return (String) dataSet.get(fieldName);
	}
	/**
	 * 获取日期
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @return 返回日期字符串
	 */
	public static String getDate(String fieldName, Map dataSet) {
		Object obj = dataSet.get(fieldName);
		return date2String((Date)obj);
	}
	/**
	 * 获取日期时间
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @return 返回日期时间字符串
	 */
	public static String getDateTime(String fieldName, Map dataSet) {
		Object obj = dataSet.get(fieldName);
		return dateTime2String((Date)obj);
	}
	/**
	 * 获取数字
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @return 返回数字
	 */
	public static Double getNumber(String fieldName, Map dataSet) {
		return (Double) dataSet.get(fieldName);
	}
	/**
	 * 获取整数
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @return 返回整数
	 */
	public static Integer getInt(String fieldName, Map dataSet) {
		Object obj = dataSet.get(fieldName);
		if (obj instanceof BigDecimal) {
			BigDecimal bigDecimal = (BigDecimal) obj;
			return bigDecimal.intValue();
		}
		return (Integer) obj;
	}
	/**
	 * 获取状态varchar2(2)
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @return 返回状态
	 */
	public static String getVarState(String fieldName, Map dataSet) {
		return (String) dataSet.get(fieldName);
	}
	/**
	 * 获取字符串
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @return 返回字符串
	 */
	public static String getString(String fieldName, Map dataSet) {
		return (String) dataSet.get(fieldName);
	}
	/**
	 * 获取大文本
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @return 返回大文本
	 */
	public static String getText(String fieldName, Map dataSet) {
		return (String) dataSet.get(fieldName);
	}
	/**
	 * 日期转换为字符
	 * @param value 日期值
	 * @return 转换后字符串
	 */
	public static String date2String(Date value) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(value);
	}
	/**
	 * 日期转换为字符
	 * @param value 日期值
	 * @return 转换后字符串
	 */
	public static String yeartoString(Date value) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(value);
	}
	/**
	 * 日期时间转换为字符
	 * @param value 日期时间值
	 * @return 转换后字符串
	 */
	public static String dateTime2String(Date value) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(value);
	}
	/**
	 * 字符串转换为日期
	 * @param value 字符串型日期值
	 * @return 转换后日期
	 */
	public static Date str2Date(String value) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 字符串转换为日期时间
	 * @param value 字符串型日期值时间
	 * @return 转换后日期时间
	 */
	public static Date str2DateTime(String value) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据工作日、开始日期计算截止日期
	 * @param startDate 开始日期
	 * @param days 工作日
	 * @return 截止日期
	 */
	public static String getEndDate(String startDate, int days) {
		Date str2Date = str2Date(startDate);
		Date endDate = str2Date;
		Calendar cal = Calendar.getInstance();
		cal.setTime(str2Date);
		int count = 0;
		while (count < days) {
			int day = cal.get(Calendar.DAY_OF_WEEK);
			if (day < 7 && day > 1)
				count++;
			cal.add(Calendar.DATE, 1);
			endDate = cal.getTime();
		}
		return date2String(endDate);
	}
	/**
	 * 根据工作日、开始日期计算截止日期
	 * @param startDate 开始日期
	 * @param days 工作日
	 * @return 截止日期
	 */
	public static String getOverDate(String startDate, int days) {
		Date str2Date = str2Date(startDate);
		Date endDate = str2Date;
		Calendar cal = Calendar.getInstance();
		cal.setTime(str2Date);
		int count = 0;
		while (count < days) {
			int day = cal.get(Calendar.DAY_OF_WEEK);
			if (day < 7 && day > 1)
				count--;
			cal.add(Calendar.DATE, 1);
			endDate = cal.getTime();
		}
		return date2String(endDate);
	}
	/**
	 * 设置字符串型日期
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @param value 日期值
	 */
	public static void setStrDate(String fieldName, Map dataSet, Date value) {
		dataSet.put(fieldName, date2String(value));
	}
	/**
	/**
	 * 设置字符串型日期
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @param value 日期值
	 */
	public static void setStrYear(String fieldName, Map dataSet, Date value) {
		dataSet.put(fieldName, yeartoString(value));
	}
	/**
	 * 设置字符串型日期
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @param value 日期值
	 */
	public static void setStrDate(String fieldName, Map dataSet, String value) {
		dataSet.put(fieldName, value);
	}
	/**
	 * 设置字符串型日期时间
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @param value 日期值
	 */
	public static void setStrDateTime(String fieldName, Map dataSet, Date value) {
		dataSet.put(fieldName, dateTime2String(value));
	}
	/**
	 * 设置字符串型日期时间
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @param value 日期值
	 */
	public static void setStrDateTime(String fieldName, Map dataSet, String value) {
		dataSet.put(fieldName, value);
	}
	/**
	 * 设置日期
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @param value 日期值
	 */
	public static void setDate(String fieldName, Map dataSet, Date value) {
		java.sql.Timestamp t=new Timestamp(value.getTime());
		dataSet.put(fieldName, t);
	}

	/**
	 * 设置日期
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @param value 日期值
	 */
	public static void setDate(String fieldName, Map dataSet, String value) {
		setDate(fieldName,dataSet,str2Date(value));
	}
	/**
	 * 设置日期时间
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @param value 日期值
	 */
	public static void setDateTime(String fieldName, Map dataSet, Date value) {
		if (value!=null){
			java.sql.Timestamp t=new Timestamp(value.getTime());
			dataSet.put(fieldName, t);
		}
		
	}
	/**
	 * 设置日期时间
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @param value 日期值
	 */
	public static void setDateTime(String fieldName, Map dataSet, String value) {
		setDateTime(fieldName,dataSet,str2DateTime(value));
	}

	/**
	 * 设置数值
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @param value 数值
	 */
	public static void setNumber(String fieldName, Map dataSet, Double value) {
		dataSet.put(fieldName, value);
	}

	/**
	 * 设置整数
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @param value 整数值
	 */
	public static void setInt(String fieldName, Map dataSet, Integer value) {
		dataSet.put(fieldName, value);
	}
	/**
	 * 设置状态
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @param value 状态值
	 */
	public static void setVarState(String fieldName, Map dataSet, String value) {
		dataSet.put(fieldName, value);
	}
	/**
	 * 设置字符串
	 * @param fieldName 字段key
	 * @param dataSet 数据集
	 * @param value 字符串
	 */
	public static void setString(String fieldName, Map dataSet, String value) {
		dataSet.put(fieldName, value);
	}

	/**
	 * 设置大文本
	 * @param fieldName
	 * @param dataSet
	 * @param value
	 */
	public static void setText(String fieldName, Map dataSet, String value) {
		dataSet.put(fieldName, value);
	}
	/**
	 * 根据当前日期获取多少个工作日以后日期
	 * @Description 
	 * @param @param workDay    
	 * @return void   
	 * @author gaoy
	 * @date Jul 9, 2012 4:24:30 PM
	 */
	public static void getworkday(int workDay ) {
		long beginTime=new Date().getTime();
		long stepTime=24*60*60*1000;
		Date dt;
		int dy;
		Long endTime=beginTime;
		int k=0;
		while(k<workDay){
		endTime-=stepTime;
		dt=new Date(endTime);
		dy=dt.getDay();
		if(dy!=0&&dy!=6){
		k++;
		}
		}
		}
	/**
	 * 日期转换为字符
	 * @param value 日期值
	 * @return 转换后字符串
	 */
	public static String dateToString(Date value) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd");
		return sdf.format(value);
	}
}
