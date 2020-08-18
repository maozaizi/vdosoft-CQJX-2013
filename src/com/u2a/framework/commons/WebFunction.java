package com.u2a.framework.commons;

import java.util.List;

import com.brick.dao.IBaseDAO;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.manager.AppCaller;
import com.brick.manager.BeanFactory;
import com.brick.manager.ContextUtil;
import com.u2a.framework.util.HelperApp;
@SuppressWarnings("unchecked")
public class WebFunction {
	/**
	 * 
	 * @Description 
	 * @param @param userId
	 * @param @param count
	 * @param @return    
	 * @return List   
	 * @author duch
	 * @date May 29, 2012 10:19:52 AM
	 */
	public static List getMyCustom(String userId) {
		IMap p = new DataMap();
		p.put("userId", userId);
		IMap out = null;
		try {
			out = AppCaller.ExeMethod("custom.getMyCustom", p);
		} catch (Exception e) {
		}
		if (out != null) {
			return (List) out.get("list");
		}
		return null;
	}
	/** 
	 * @Description 获取工作流
	 * @param @return    
	 * @return List   
	 * @author panghaichao
	 * @date 2012年5月26日 17:06:34
	 */ 
		
	public static List getWorkFlow(IMap userMap) {
		IMap p = new DataMap();
		p.put("userMap", userMap);
		IMap out = null;
		try {
			out = AppCaller.ExeMethod("mywork.getMyWorkAllList", p);
		} catch (Exception e) {
		}
		if (out != null) {
			return (List) out.get("alreadyBizStepList");
		}
		return null;
	}
	
	/** 
	 * @Description 获取已经完成的工作流信息
	 * @param @param userMap
	 * @return List   
	 * @author panghaichao
	 * @date 2012年9月11日 11:03:18
	 */ 
	public static List getCompleteWorkFlow(IMap userMap) {
		IMap p = new DataMap();
		p.put("userMap", userMap);
		IMap out = null;
		try {
			out = AppCaller.ExeMethod("mywork.getCompleteWorkFlow", p);
		} catch (Exception e) {
		}
		if (out != null) {
			return (List) out.get("completeBizStepList");
		}
		return null;
	}
	/**
	 * @Description 根据编码获取列表
	 * @param dataItemCode编码
	 * @return IMap<String,Object>
	 * @author panghaichao
	 * @date Feb 28, 2012 3:53:37 PM
	 */

	public static List<IMap> getDataDictionary(String dataItemCode,IBaseDAO db) {
		IMap tempBean = BeanFactory.getClassBean("com.DataItemBaseInfo");
		IMap listBean = BeanFactory.getClassBean("com.DataItemBaseInfo");
		tempBean.put("dataItemCode", dataItemCode);
		List<IMap> listId = db.getList(tempBean, "getIdByCode","com.DataItemBaseInfo");
		List<IMap> list = null;
		if(listId.size()>0){
			String dataItemId=(String)listId.get(0).get("dataItemId");
			listBean.put("dataItemId", dataItemId);
			list = db.getList(listBean, "getChildsById","com.DataItemBaseInfo");
		}
		
		return list;
	}
	/**
	 * 获取数据字典项
	 * @param code 字典项code
	 * @return
	 */
	public static List getDataItem(String code) {
		IBaseDAO dao = (IBaseDAO) ContextUtil.getSpringBean(HelperApp.getDaoName());
		List<IMap> item = (List<IMap>)getDataDictionary(code,dao);		
		return item;
	}
	/**
	 * 获取计划详细信息
	 * @param planId计划ID
	 * @return
	 */
	public static List getMaterialInfoList(String planId) {
		IBaseDAO db = (IBaseDAO) ContextUtil.getSpringBean(HelperApp.getDaoName());
		IMap busMaterialPlanMap =BeanFactory.getClassBean("com.BusMaterialPlan");
		busMaterialPlanMap.put("planId",planId);
		//根据planId的到领用材料列表
		List busMaterialPlanList = db.getList(busMaterialPlanMap, null);
		return busMaterialPlanList;
	}
	/**
	 * 
	 *  描述: 根据CODE查找自定义井队信息
	 *  方法: getDeptBycode方法
	 *  作者: duch
	 *  时间: Mar 14, 2014
	 *  版本: 1.0
	 */
	public static String getDeptBycode(String code) {
		IBaseDAO db = (IBaseDAO) ContextUtil.getSpringBean(HelperApp.getDaoName());
		IMap crewMap =BeanFactory.getClassBean("com.DrillingCrew");
		crewMap.put("teamCode", code);
		crewMap.put("isvalid", "1");
		List crewMapList = db.getList(crewMap, null);
		String dept = "";
		if(!crewMapList.isEmpty()){
			crewMap = (IMap) crewMapList.get(0);
			dept =crewMap.get("deptName")+"-"+crewMap.get("teamName");
		}
		return dept;
	}
	/**
	 * 获取厂区-车间名称
	 * @param orgId
	 * @return
	 */
	public static String getDeptName(String orgId){
		IBaseDAO db = (IBaseDAO) ContextUtil.getSpringBean(HelperApp.getDaoName());
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = orgId.replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		if(orgIds.length>=3){
			baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		}
		if(orgIds.length==2){
			baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]);
		}
		if(orgIds.length==1){
			baseOrganizationMap.put("orgCode",orgIds[0]);
		}
		if(orgIds.length>0){
			baseOrganizationMap = db.get(baseOrganizationMap);
		}
		String deptName = (String)baseOrganizationMap.get("orgName");
		return deptName;
	}
	
}
