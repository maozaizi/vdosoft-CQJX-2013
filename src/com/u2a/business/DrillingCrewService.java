package com.u2a.business;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.brick.api.Service;
import com.brick.dao.Page;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.data.OrderBean;
import com.brick.manager.BeanFactory;
import com.u2a.framework.commons.Constants;
import com.u2a.framework.service.sys.logrecords.LogBean;
import com.u2a.framework.service.sys.logrecords.LogRecordsService;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;
import com.u2a.framework.util.Util;

/**
 * 
 * 系统名称：长庆钻井   
 * 类名称：DrillingCrewService   
 * 类描述：井队管理处理类  
 * 创建人：duch 
 * 创建时间：Sep 6, 2013 2:40:53 PM
 */
@SuppressWarnings("unchecked")
public class DrillingCrewService extends Service{
	/**
	 * 
	 * @Description 获取井队列表
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Sep 6, 2013 2:42:35 PM
	 */
	public IMap<String,Object> getDrillingCrewList(IMap<String,Object> in){
		IMap result = new DataMap();
		//session当前登录用户
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
		//按照创建日期进行排序
		OrderBean orderBean = new OrderBean();
		orderBean.put("createDate",OrderBean.DESC);
		//查询分页结果
		Page page = db.pageQuery(in,"getDrillingCrewList","com.DrillingCrew","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}
	/**
	 * 
	 * @Description 添加井队信息
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Sep 9, 2013 2:03:03 PM
	 */
	public IMap<String, Object> addCrew(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap drillingCrewMap = (IMap) in.get("drillingCrew");
		IMap userMap = (IMap) in.get("userMap");
		String id =HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.DrillingCrew");
		String deptCode = (String) drillingCrewMap.get("projectDept");
		IMap dictMap = BeanFactory.getClassBean("com.DataItemBaseInfo");
		dictMap.put("dataItemCode", deptCode);
		dictMap.put("isValid", "1");
		dictMap = db.get(dictMap);
		
		drillingCrewMap.put("id",id);
		drillingCrewMap.put("deptName",dictMap.get("dataItemName"));
		//为创建数据赋值
		HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),drillingCrewMap);
		db.insert(drillingCrewMap);
		//log日志
		String remark="用户"+userMap.get("userName")+"在"+Util.toStringDate(new Date())+"添加设备信息";
		LogBean logb=new LogBean(userMap,id, "com.DrillingCrew","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg", "添加井队信息成功！");
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 
	 * @Description 跳转到修改页面
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Sep 9, 2013 2:33:38 PM
	 */
	public IMap<String, Object> toUpdateCrew(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.DrillingCrew");
		selectMap.put("id", in.get("id"));
		selectMap.put("isValid", "1");
		result.put("crewMap", super.db.get(selectMap));
		return result;
	}
	/**
	 * 
	 * @Description 保存修改的结果
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Sep 9, 2013 2:49:36 PM
	 */
	public IMap<String, Object> updateCrew(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap drillingCrewMap = (IMap) in.get("drillingCrew");
		IMap userMap = (IMap) in.get("userMap");
		String deptCode = (String) drillingCrewMap.get("projectDept");
		IMap dictMap = BeanFactory.getClassBean("com.DataItemBaseInfo");
		dictMap.put("dataItemCode", deptCode);
		dictMap.put("isValid", "1");
		dictMap = db.get(dictMap);
		drillingCrewMap.put("deptName",dictMap.get("dataItemName"));
		//为创建数据赋值
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap),drillingCrewMap);
		db.update(drillingCrewMap);
		//log日志
		String remark="用户"+userMap.get("userName")+"在"+Util.toStringDate(new Date())+"修改井队信息";
		LogBean logb=new LogBean(userMap,(String)drillingCrewMap.get("id"), "com.DrillingCrew","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg", "修改井队信息成功！");
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 
	 * @Description 删除
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Sep 9, 2013 3:18:08 PM
	 */
	public IMap<String, Object> deletecrew(IMap<String, Object> in) {
		IMap userMap = (IMap) in.get("userMap");
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.DrillingCrew");
		selectMap.put("id", in.get("id"));
		selectMap.put("isValid", "0");
		db.update(selectMap);
		
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap),selectMap);
		//log日志
		result.put("method.infoMsg", "删除井队信息成功！");
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 
	 * @Description 根据项目部选井队
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Sep 9, 2013 5:30:49 PM
	 */
	public IMap<String, Object> getTeamList(IMap<String, Object> in) {
		// 获取市级
		String proDept = (String)in.get("proDept");
		IMap selectMap = BeanFactory.getClassBean("com.DrillingCrew");
		selectMap.put("projectDept", proDept);
		selectMap.put("isValid", "1");
		
		List<IMap> list = db.getList(selectMap, null);
		
		IMap result = new DataMap();
		result.put("teamlist", list);
		
		return result;
	}
}
