package com.u2a.framework.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.brick.dao.IBaseDAO;
import com.brick.data.DBBean;
import com.brick.data.DataConfigBean;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.exception.BusinessException;
import com.brick.manager.BeanFactory;
import com.brick.manager.ContextUtil;
/**
 * 主键获取类
 * @author 吴明强
 * @date 2010-08-27
 */
public class PKFactory {

	protected IMap map=new DataMap();


	public static void main(String[] args) {
		String random =String.valueOf((long)(Math.random()*9999+1));
		String nowTime =String.valueOf(System.currentTimeMillis()).substring(9);
		
	
	}
	/**
	 *获取主键
	 * @param packageName 标志
	 * @param className 类名
	 * @return
	 */
	public synchronized String getAutoIncrementPKID(
			String packageName, String className) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		//String nowTime =String.valueOf((long)(Math.random()*99999+1));
		//String nowTime =String.valueOf(System.currentTimeMillis()).substring(9);
		String nowTime = df.format(new Date());
		nowTime= nowTime.toLowerCase();
		Integer i= (Integer) map.get(className+nowTime);
		if (i==null){
			i=getDBKey(className,packageName,nowTime);
		}
		i++;
		String key=i.toString();
		while(key.length()<6){
			key="0"+key;
		}
		
		key=packageName+nowTime+ key;
		map.put(className+nowTime, i);
		return key;
	}
	/**
	 * 
	 * @Description 获取GUID作为主键
	 * @param @param className
	 * @param @return    
	 * @return String   
	 * @author duch
	 * @date May 16, 2012 2:04:25 PM
	 */
	public String getGuidPK(String className){
		   String id = UUID.randomUUID().toString();
		   id = id.replace("-", "");
		   return id; 
		}

	/**
	 * 获取数据库当前主键
	 * @param tableName
	 * @param packageName
	 * @param nowTime
	 * @return
	 */
	private Integer getDBKey(String className,String packageName,String nowTime) {
		DBBean bean= BeanFactory.getDbBean(className);
		if (bean==null){
			throw new BusinessException("类名称不正确！"+className);
		}
		IMap columns=bean.getProperty();
		String key="";
		for (Object o : columns.values()) {
			DataConfigBean column=(DataConfigBean) o;
			if (column.isPrimary()){				
				key=column.getColumn();
				break;
			}
		}
		if (HelperApp.isEmpty(key)){
			throw new BusinessException("表主键名获取不正确！"+className);
		}
		IMap in=new DataMap();
		in.put("key", key);
		in.put("tableName", bean.getTableName());
		in.put("id", packageName+nowTime);
		IBaseDAO db= (IBaseDAO)ContextUtil.getSpringBean(HelperApp.getDaoName());
		IMap out=db.get(in,"getMaxPrimary","");
		String maxkey=(String) out.get("maxkey");
		if (HelperApp.isEmpty(maxkey)){
			return 0;
		}
		maxkey=maxkey.replaceFirst(packageName+nowTime, "").trim();		
		Integer i=Integer.parseInt(maxkey);
		return i;
	}
}
