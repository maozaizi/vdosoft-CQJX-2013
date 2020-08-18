package com.u2a.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.export.ooxml.DocxBorderHelper;

import com.brick.api.Service;
import com.brick.dao.Page;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.data.OrderBean;
import com.brick.exception.BusinessException;
import com.brick.manager.BeanFactory;
import com.u2a.framework.commons.Constants;
import com.u2a.framework.commons.OperateHelper;
import com.u2a.framework.service.sys.logrecords.LogBean;
import com.u2a.framework.service.sys.logrecords.LogRecordsService;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;
import com.u2a.framework.util.Util;

/**
 * 
 * 系统名称：长庆钻井机修公司 - 设备维修管理系统   
 * 类名称：EquipmentInfoService   
 * 类描述：   
 * 创建人：孟勃婷 
 * 创建时间：Jul 11, 2013 9:23:14 AM
 */
@SuppressWarnings("unchecked")
public class EquipmentInfoService extends Service {

	/**
	 * 
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Jul 11, 2013 9:23:31 AM
	 */
	public IMap getEquipmentInfoList(IMap in){
		// 设置结果集
		IMap result = new DataMap();
		
		List list = db.getList(in,"queryEquipmentInfoList","com.EquipmentInfo");
		List<IMap> rlist = new ArrayList<IMap>();
		//组合设备名称和编号
		if(!list.isEmpty()){
			for(int i=0;i<list.size();i++){
				IMap map = (IMap) list.get(i);
				map.put("wholename", (String)map.get("equipmentCode")+" "+(String)map.get("equipmentName"));
				rlist.add(map);
			}
		}
		
		result.put("equipmentList", rlist);
		return result;
	}
	
	
	/**
	 * 添加 设备信息
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Jul 11, 2013 1:28:49 PM
	 */
	public IMap<String, Object> addEquipmentInfo(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap equipmentInfoMap = (IMap) in.get("equipmentInfo");
		
		IMap selMap = BeanFactory.getClassBean("com.EquipmentInfo");
		
		selMap.put("equipmentCode",equipmentInfoMap.get("equipmentCode"));
		selMap.put("isValid", 1);
		List equipmentList = db.getList(selMap,null);
		if(!equipmentList.isEmpty()){
			throw new BusinessException("此序号已存在，请修改后重新添加！");
		}
		
		
		equipmentInfoMap.put("equipmentModel", ((String)equipmentInfoMap.get("equipmentCode")).replace(".", "").trim());
		
		IMap userMap = (IMap) in.get("userMap");
		String equId =HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.EquipmentInfo");
		equipmentInfoMap.put("id",equId);
		equipmentInfoMap.put("isValid",Constants.ISVALID);
		//为创建数据赋值
		HelperDB.setCreateInfo(HelperApp.getUserName(userMap),equipmentInfoMap);
		db.insert(equipmentInfoMap);
		//log日志
		String remark="用户"+userMap.get("userName")+"在"+Util.toStringDate(new Date())+"添加设备信息";
		LogBean logb=new LogBean(userMap,equId, "com.EquipmentInfo","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg", "添加设备信息成功！");
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 修改 设备信息
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Jul 11, 2013 1:40:52 PM
	 */
	public IMap<String, Object> updateEquipmentInfo(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap equipmentInfoMap = (IMap) in.get("equipmentInfo");
		IMap selMap = BeanFactory.getClassBean("com.EquipmentInfo");
		
		selMap.put("equipmentCode",equipmentInfoMap.get("equipmentCode"));
		selMap.put("isValid", 1);
		List equipmentList = db.getList(selMap,null);
		if(equipmentList.size()>1){
			throw new BusinessException("此序号已存在，请重新修改！");
		}
		equipmentInfoMap.put("equipmentModel", ((String)equipmentInfoMap.get("equipmentCode")).replace(".", "").trim());
		selMap.put("isValid", 1);
		IMap userMap = (IMap) in.get("userMap");
		//为创建数据赋值
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap),equipmentInfoMap);
		db.update(equipmentInfoMap);
		//log日志
		String remark="用户"+userMap.get("userName")+"在"+Util.toStringDate(new Date())+"修改设备信息";
		LogBean logb=new LogBean(userMap,(String)equipmentInfoMap.get("Id"), "com.EquipmentInfo","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg", "修改设备信息成功！");
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 设备修改 跳转
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Jul 11, 2013 1:25:16 PM
	 */
	public IMap<String, Object> toUpdateEquipmentInfo(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.EquipmentInfo");
		selectMap.put("id", in.get("id"));
		result.put("equipmentInfoMap", super.db.get(selectMap));
		return result;
	}
	/**
	 * 设备删除
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Jul 11, 2013 1:25:16 PM
	 */
	public IMap<String, Object> deleteEquipmentInfo(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//session登录用户
		IMap userMap = (IMap) in.get("userMap");
		IMap selectMap = BeanFactory.getClassBean("com.EquipmentInfo");
		selectMap.put("id", in.get("id"));
		//获取所有子节点
		List<IMap> childNode = db.getList(selectMap, "equipmentChildList",
				selectMap.getClassName());
		//判断如果有子节点则不能删除，否则可以删除
		if(!childNode.isEmpty()){
			result.put("method.infoMsg", "要先作废子节点，才能作废父节点！");
		}else{
			IMap equipmentMap = db.get(selectMap);
			equipmentMap.put("isValid",Constants.ISNOTVALID);
			//修改信息
			HelperDB.setModifyInfo(HelperApp.getUserName(userMap),equipmentMap);
			db.update(equipmentMap);
			//log日志
			String remark="用户"+userMap.get("userName")+"在"+Util.toStringDate(new Date())+"作废设备信息";
			LogBean logb=new LogBean(userMap,(String)in.get("id"), "com.EquipmentInfo","",remark, "0");
			LogRecordsService.saveOperationLog(logb,db);
			result.put("method.infoMsg",OperateHelper.getDelMsg());
		}
		
		result.put("method.url", in.get("url"));
		return result;
	}
}
