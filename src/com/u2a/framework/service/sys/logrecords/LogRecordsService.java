package com.u2a.framework.service.sys.logrecords;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.brick.api.Service;
import com.brick.dao.IBaseDAO;
import com.brick.dao.Page;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.data.OrderBean;
import com.brick.manager.BeanFactory;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;


public class LogRecordsService extends Service {

	/** 
	 * @Description 获取日志基本信息列表
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author cuijh
	 * @date 
	 */

	@SuppressWarnings("unchecked")
	public IMap getLogRecordsList(IMap in) {
		IMap result = new DataMap();
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
		//按照创建日期进行排序
		OrderBean orderBean = new OrderBean();
		orderBean.put("operationDate",OrderBean.DESC);
		IMap logRecordsMap = new DataMap();
		//业务表
		if(!"".equals(in.get("id1"))){
			logRecordsMap.put("busIdOne",in.get("id1"));
		}else{
			logRecordsMap.put("busIdOne","1");
		}
		logRecordsMap.put("busNameOne",in.get("name1"));
		//进修单
		if(!"".equals(in.get("id2"))){
			logRecordsMap.put("busIdTow",in.get("id2"));
		}else{
			logRecordsMap.put("busIdTow","1");
		}
		logRecordsMap.put("busNameTow",in.get("name2"));
		//查询分页结果
		Page page = db.pageQuery(logRecordsMap,"logRecordsList","com.logRecords","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}

	
	/** 
	 * @Description 添加日志
	 * @param @param bean
	 * @param @param db    
	 * @return void   
	 */ 
	@SuppressWarnings("unchecked")
	public static void saveOperationLog(LogBean bean,IBaseDAO db) {
		Date date = new Date();
		IMap<String, Object> insertbean = BeanFactory
				.getClassBean("com.logRecords");
		insertbean.put("Id", HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.logRecords"));
		HelperDB.setDate("operationDate", insertbean, date);
		insertbean.put("userId", bean.getUserId());
		insertbean.put("userName", bean.getUserName());
		insertbean.put("busId", bean.getBusId());
		insertbean.put("busName", bean.getBusName());
		insertbean.put("remark", bean.getRemark());
		insertbean.put("reason", bean.getReason());
		insertbean.put("logType", bean.getLogType());
		insertbean.put("docId", bean.getDocId());
		db.insert(insertbean);
	}
	
}
