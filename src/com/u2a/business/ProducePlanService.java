package com.u2a.business;

import java.util.ArrayList;
import java.util.List;

import com.brick.api.Service;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.manager.BeanFactory;
import com.brick.util.Util;
import com.u2a.framework.commons.OperateHelper;
import com.u2a.framework.util.DateTimeUtil;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;

public class ProducePlanService extends Service{
	/**
	 * 
	 * @Description 跳转到生产计划列表页面
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Sep 26, 2013 3:58:09 PM
	 */
	public IMap<String, Object> toSearchPlan(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();
		
		int year = DateTimeUtil.getLocalYear();
		int month = DateTimeUtil.getLocalMonth();
		
		result.put("year", year);
		result.put("month", month);
		return result;
	}
	/**
	 * 
	 * @Description 跳转到制定生产计划页面
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Sep 26, 2013 5:15:51 PM
	 */
	public IMap<String, Object> toAddplan(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();
		IMap<String, Object> userMap = (IMap<String, Object>) in.get("userMap");
		String orgCode =  (String) userMap.get("orgId");
		
		IMap<String, Object> map = (IMap<String, Object>) in.get("produceplan");
		map.put("orgCode", orgCode);
		
		List<IMap> maplist = db.getList(map,"getproduceplanList","com.produceplan");
		
		result.put("maplist", maplist);
		result.put("year", map.get("year"));
		result.put("month", map.get("month"));
		
		return result;
	}
	/**
	 * 
	 * @Description 选择本车间待修和在修的设备列入工作计划
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author duch
	 * @date Oct 9, 2013 11:10:50 AM
	 */
	public IMap<String, Object> toChooseplan(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();
		
		IMap<String,Object> userMap = (IMap<String, Object>) in.get("userMap"); 
		String orgCode = (String) userMap.get("orgId");
		in.put("orgCode", orgCode);
		if(orgCode.indexOf("JX")>0){
			in.put("tableNames", "bus_mechanic");
		}else if(orgCode.indexOf("ZX")>0){
			in.put("tableNames", "bus_drilling");
		}else if(orgCode.indexOf("DX")>0){
			in.put("tableNames", "bus_electrical");
		}else if(orgCode.indexOf("MH")>0){
			in.put("tableNames", "bus_rivetweld");
		}
		
		List<IMap> buslist = db.getList(in, "getProplanrepairList", "");
		
		result.put("repairlist", buslist);
		result.put("year", in.get("year"));
		result.put("month", in.get("month"));
		result.put("closeFlag", "0");
		return result;
	}
	
	public IMap<String, Object> savePlan(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();
		
		IMap<String,Object> userMap = (IMap<String, Object>) in.get("userMap"); 
		String orgCode = (String) userMap.get("orgId");
		in.put("orgCode", orgCode);
		if(orgCode.indexOf("JX")>0){
			in.put("tableNames", "bus_mechanic");
		}else if(orgCode.indexOf("ZX")>0){
			in.put("tableNames", "bus_drilling");
		}else if(orgCode.indexOf("DX")>0){
			in.put("tableNames", "bus_electrical");
		}else if(orgCode.indexOf("MH")>0){
			in.put("tableNames", "bus_rivetweld");
		}
		String[] workCards = (String[]) in.get("checkbox");
		List templist = new ArrayList();
		for(int j=0;j<workCards.length;j++){
			String workcard = workCards[j];
			IMap monthplan = BeanFactory.getClassBean("com.produceplan");
			monthplan.put("workCard", workcard);
			monthplan.put("isValid", "1");
			List<IMap> list = db.getList(monthplan, null);
			if(list.isEmpty()){
				templist.add(workcard);
			}
		}
		
		if(!templist.isEmpty()){
			in.put("workCards", templist);
			List<IMap> buslist = db.getList(in, "getProplanrepairList", "");
			
			if(!buslist.isEmpty()){
				for(int i=0;i<buslist.size();i++){
					IMap map = buslist.get(i);
					IMap proplan = BeanFactory.getClassBean("com.produceplan");
					proplan.put("id", HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(), "com.produceplan"));
					proplan.put("year", in.get("year"));
					proplan.put("month", in.get("month"));
					proplan.put("workCard", map.get("workCard"));
					proplan.put("equipmentName", map.get("equipmentName"));
					proplan.put("equipmentModel", map.get("equipmentModel"));
					proplan.put("unity", map.get("unity"));
					proplan.put("numbers", map.get("numbers"));
					proplan.put("equipmentValue", map.get("equipmentValue"));
					proplan.put("teamNumber", map.get("deptFrom"));
					proplan.put("orgCode", map.get("orgCode"));
					proplan.put("repairType", map.get("repairType"));
					
					HelperDB.setCreate4isValid(HelperApp.getUserName(userMap), proplan);
					
					db.insert(proplan);
				}
				
			}
		}
		
		result.put("closeFlag", "1");
		return result;
	}
	
	
	public IMap<String, Object> deletePlan(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();
		IMap<String, Object> userMap = (IMap<String, Object>) in.get("userMap");
		
		IMap monthplan = BeanFactory.getClassBean("com.produceplan");
		
		monthplan.put("id", in.get("delId"));
		monthplan.put("isValid", "0");
		
		db.update(monthplan);
		
		String year = (String) in.get("year");
		String month = (String) in.get("month");
		
		result.put("method.infoMsg", OperateHelper.getDeleteMsg());
		result.put("method.url", in.get("url")+"?year="+year+"&month="+month);
		
		return result;
	}
	
}
