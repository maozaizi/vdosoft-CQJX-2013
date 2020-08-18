package com.u2a.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

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
import com.u2a.framework.util.FireNetHelper;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;
import com.u2a.framework.util.Util;

/**
 * 
 * 系统名称：机加流程业务处理类
 * 类名称：MechTreatmentSevice   
 * 类描述：   
 * 创建人：duch 
 * 创建时间：Aug 5, 2013 2:51:05 PM
 */
@SuppressWarnings("unchecked")
public class MechTreatmentService extends Service{
	
	//1、外部件登记
	public IMap<String,Object> saveRegister(IMap in){
		
		
		return null;
	}
	//2、内部件登记
	//3、调度
	//4、接收确认 派工
	/**
	 * 机加列表
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Aug 7, 2013 3:04:17 PM
	 */
	public IMap<String,Object> getMechTreatmentList(IMap<String,Object> in){
		// 设置结果集
		IMap result = new DataMap();
		//session当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?0:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?0:Integer.parseInt((String) in.get("perCount"));
		//按照创建日期进行排序
		OrderBean orderBean = new OrderBean();
		orderBean.put("createDate",OrderBean.DESC);
		//查询限制，当前用户orgId
		in.put("orgId", userMap.get("orgId"));
		//查询分页结果
		Page page = db.pageQuery(in,"queryMachiningList","com.MachiningMessage","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}
	/**
	 * 进修单信息
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Aug 8, 2013 9:40:25 AM
	 */
	public IMap<String, Object> getMechTreatmentDetails(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.MechTreatment");
		selectMap.put("id", in.get("id"));
		//进修单信息
		IMap mechTreatmentMap = db.get(selectMap);
		result.put("mechTreatmentMap",mechTreatmentMap );
		//机加单信息
		IMap machiningMap =BeanFactory.getClassBean("com.MachiningMessage");
		machiningMap.put("id",mechTreatmentMap.get("inrepairId"));
		machiningMap =db.get(machiningMap);
		result.put("machiningMap",machiningMap);
		//附件列表
		IMap condition = new DataMap();
		condition.put("busIdOne",mechTreatmentMap.get("id"));
		condition.put("busNameOne","com.MechTreatment");
		//进修单
		condition.put("busIdTow",in.get("id"));
		condition.put("busNameTow","com.MachiningMessage");
		//附件列表
		List<IMap> fileList = db.getList(condition, "getAllFileList", "com.AttachmentInfo");
		//工作流信息
		result.put("fileList", fileList);
		result.put("stepId", in.get("stepId"));
		result.put("nodeId", in.get("nodeId"));
		result.put("instanceId", in.get("instanceId"));
		//操作记录
		List<IMap> logRecordsList = db.getList(condition, "getLogRecordsList", "com.logRecords");
		result.put("logRecordsList",logRecordsList);
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = mechTreatmentMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", mechTreatmentMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		result.put("groupName", baseOrganization.get("orgName"));
		return result;
	}
	
	/**
	 * 领料计划申请
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Aug 8, 2013 9:40:25 AM
	 */
	@SuppressWarnings("unchecked")
	public IMap<String, Object> modifyBusMaterialPlan(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//获取材料计划列表
		List busMaterialPlanList = (List) in.get("busMaterialPlan");
		//当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		//业务ID
		String busId = (String)in.get("busId");
		String planRemark=(String)in.get("planRemark");
		IMap mechTreatmentMap =BeanFactory.getClassBean("com.MechTreatment");
		mechTreatmentMap.put("id", busId);
		mechTreatmentMap = db.get(mechTreatmentMap);
		//物流计划备注信息
		mechTreatmentMap.put("planRemark", planRemark);
		db.update(mechTreatmentMap);
		// 发起工作流
		IMap wfmap = new DataMap();
		IMap condition = new DataMap();
		//获取基础组织编码
		String orgCode = (String) userMap.get("orgId");
		condition.put("orgId", orgCode);
		//当前审批计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
		busPlanInfoMap.put("busId",in.get("busId"));
		busPlanInfoMap.put("busName","com.MechTreatment");
		busPlanInfoMap.put("isValid",1);
		//auditState - 0  审核不通过 1 审核通过 2未审
		busPlanInfoMap.put("auditState",0);
		//当前审批计划
		busPlanInfoMap = db.get(busPlanInfoMap);
		//作废审核未通过信息
		if(busPlanInfoMap!=null){
			busPlanInfoMap.put("isValid",0);
			db.update(busPlanInfoMap);
		}
		//重新添加循环添加材料计划
		if(busMaterialPlanList !=null && busMaterialPlanList.size()>0){
			//添加计划主信息
			IMap newBusPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
			//关联机加流程
			newBusPlanInfoMap.put("busId", busId);
			newBusPlanInfoMap.put("busName", "com.MechTreatment");
			newBusPlanInfoMap.put("planCode", (String)in.get("planCode"));
			//计划ID
			String busPlanInfoId =HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(), "com.BusPlanInfo");
			newBusPlanInfoMap.put("id", busPlanInfoId);
			//工卡号
			newBusPlanInfoMap.put("workCard",mechTreatmentMap.get("workCard"));
			//orgId
			newBusPlanInfoMap.put("orgId", userMap.get("orgId"));
			//物料计划大件材料申请原因
			newBusPlanInfoMap.put("bigRemark", (String)in.get("bigRemark"));
			//为创建对象赋值、
//			boolean flag = true;
			HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),newBusPlanInfoMap);
			for (int i = 0; i < busMaterialPlanList.size();i++) {
				IMap busMaterialPlan = (IMap)busMaterialPlanList.get(i);
				String planId =  HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(), "com.BusMaterialPlan");
				busMaterialPlan.put("id",planId);
				busMaterialPlan.put("planId", busPlanInfoId);
				busMaterialPlan.put("ratifyNum", busMaterialPlan.get("materialNum"));
//				if("1".equals(busMaterialPlan.get("type"))){
//					flag= false;
//				}
				HelperDB.setCreateInfo(HelperApp.getUserName(userMap), busMaterialPlan);
				db.insert(busMaterialPlan);
			}
			String remark="材料员："+userMap.get("name")+"在"+Util.toStringDate(new Date())+"添加了材料及加工件领用申请信息。";
			LogBean logb=new LogBean(userMap,busId, "com.MechTreatment","",remark, "0");
			LogRecordsService.saveOperationLog(logb,db);
			//auditState - 0  审核不通过 1 审核通过 2未审
//			if(flag==true){ //无领用材料，默认材料计划为 审核通过
//				wfmap.put("result","0");
//				condition.put("postCode", "车间材料员");
//				newBusPlanInfoMap.put("auditState",1);
//			}else{
				wfmap.put("result", "1");
				condition.put("postCode", "车间主任");
				newBusPlanInfoMap.put("auditState",2);
//			}
			db.insert(newBusPlanInfoMap);
		}else{
			wfmap.put("result","0");
			condition.put("postCode", "车间材料员");
		}
		//wfmap.put("result", flag);// 判断是否受理
		//------------查询当前组织下的厂检验人员，没有则查询所有的车间检验人员
		List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
		String cbids = "";
		for(int i=0;i<userList.size();i++){
			IMap usersMap = BeanFactory.getClassBean("com.UserInfo");
			usersMap = userList.get(i);
			cbids = cbids+ (String)usersMap.get("userId")+",";
		}
		String[] nextUserIds = cbids.split(",");
		IMap wf_users = new DataMap();
		wf_users.put("u", nextUserIds);
		JSONObject str_wf_user = JSONObject.fromObject(wf_users);
		wfmap.put("assignedbyuser", str_wf_user.toString());
		String instanceId = (String)in.get("instanceId");
		String stepId = (String)in.get("stepId");
		String nodeId = (String)in.get("nodeId");
		FireNetHelper.wf_next(db,userMap, instanceId, stepId, nodeId, wfmap);
		result.put("method.url", in.get("url"));
		result.put("method.infoMsg", "提交完成!");
		return result;
	}
	/**
	 * 添加试车记录
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Aug 8, 2013 9:43:25 AM
	 */
	@SuppressWarnings("unchecked")
	public IMap<String, Object> addBusTestRecord(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap busTestRecord = (IMap) in.get("busTestRecord");
		IMap userMap = (IMap) in.get("userMap");
		String busId = (String) in.get("busId");
		//获取试车记录主键ID
		String busTestRecordId= HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.BusTestRecord");
		busTestRecord.put("id",busTestRecordId);
		//关联 机加流程
		busTestRecord.put("busId", busId);
		busTestRecord.put("busName","com.MechTreatment");
		//为创建数据赋值
		HelperDB.setCreateInfo(HelperApp.getUserName(userMap),busTestRecord);
		db.insert(busTestRecord);
		String remark="检验员："+userMap.get("name")+"在"+Util.toStringDate(new Date())+"添加了试车记录信息";
		LogBean logb=new LogBean(userMap,busId, "com.MechTreatment","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		//工作流信息
		IMap wfmap = new DataMap();
		//wfmap.put("result", flag);// 判断是否受理
		//------------查询当前组织下的厂检验人员，没有则查询所有的车间检验人员
		IMap condition = new DataMap();
		//获取基础组织编码
		String orgCode = (String) userMap.get("orgId");
		condition.put("orgId", orgCode);
		condition.put("postCode", "车间检验员");
		List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
		String cbids = "";
		for(int i=0;i<userList.size();i++){
			IMap usersMap = BeanFactory.getClassBean("com.UserInfo");
			usersMap = userList.get(i);
			cbids = cbids+ (String)usersMap.get("userId")+",";
		}
		String[] nextUserIds = cbids.split(",");
		IMap wf_users = new DataMap();
		wf_users.put("u", nextUserIds);
		JSONObject str_wf_user = JSONObject.fromObject(wf_users);
		// 发起工作流
		wfmap.put("assignedbyuser", str_wf_user.toString());
		String instanceId = (String)in.get("instanceId");
		String stepId = (String)in.get("stepId");
		String nodeId = (String)in.get("nodeId");
		FireNetHelper.wf_next(db,userMap, instanceId, stepId, nodeId, wfmap);
		result.put("method.infoMsg",OperateHelper.getAddMsg());
		result.put("method.url", in.get("url"));
		return result;
	}

	/**
	 * 跳转验收检验
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Aug 8, 2013 9:46:44 AM
	 */
	@SuppressWarnings("unchecked")
	public IMap<String, Object> getBusTestRecordDetails(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.MechTreatment");
		selectMap.put("id", in.get("id"));
		//进修单信息
		selectMap = db.get(selectMap);
		//需要加工的零件信息
		IMap machiningMap =BeanFactory.getClassBean("com.mechdept");
		machiningMap.put("busid",selectMap.get("id"));
		List<IMap> machiningList =db.getList(machiningMap,"getmechdeptlist", "com.mechdept");
		
		
		result.put("machiningList", machiningList);
		
		IMap condition = new DataMap();
		//业务表
		condition.put("busIdOne",selectMap.get("id"));
		condition.put("busNameOne","com.MechTreatment");
		//进修单
		//操作记录
		List<IMap> logRecordsList = db.getList(condition, "getLogRecordsList", "com.logRecords");
		result.put("logRecordsList", logRecordsList);
		//附件列表
//		IMap attachmentMap =BeanFactory.getClassBean("com.AttachmentInfo");
//		attachmentMap.put("busId",machiningMap.get("id"));
//		attachmentMap.put("busName","com.MachiningMessage");
//		attachmentMap.put("busType","1");	
//		attachmentMap.put("isValid",1);	
//		OrderBean orderBean = new OrderBean();
//		orderBean.put("createDate",OrderBean.DESC);
//		List fileList = db.getList(attachmentMap, orderBean);
//		result.put("fileList", fileList);
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = selectMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", selectMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		result.put("groupName", baseOrganization.get("orgName"));
		//工卡号信息
		IMap busWorkMap = BeanFactory.getClassBean("com.BusWorkCard");
		busWorkMap.put("workCard", selectMap.get("workCard"));
		List workHardMapList = db.getList(busWorkMap,null);
		if(!workHardMapList.isEmpty()){
			busWorkMap = (IMap)workHardMapList.get(0);
		}
		//回写 进修单信息、试车记录
		result.put("mechTreatmentMap",selectMap);
		result.put("stepId", in.get("stepId"));
		result.put("nodeId", in.get("nodeId"));
		result.put("instanceId", in.get("instanceId"));
		return result;
	}
	
	/**
	 * 验收检验
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Aug 8, 2013 9:48:37 AM
	 */
	public IMap modifyMechTreatmentOpinionCheck(IMap in){
		IMap result = new DataMap();
		IMap selectMap = BeanFactory.getClassBean("com.MechTreatment");
		selectMap.put("id", in.get("busId"));
		//进修单信息
		selectMap = db.get(selectMap);
		IMap userMap = (IMap) in.get("userMap");
		//检验人
		selectMap.put("checker",HelperApp.getUserName(userMap));
		Date date=new Date();	
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date_str=df.format(date);
		//检验时间
		selectMap.put("checkDate",date_str);
		//检验意见
		selectMap.put("opinionChecker",in.get("opinionChecker"));
		//完成时间，实耗工时
		selectMap.put("completeDate",in.get("completeDate"));
		selectMap.put("useManHours",in.get("useManHours"));
		//为修改数据赋值
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap),selectMap);
		db.update(selectMap);
		//修改工卡信息，完成时间，实耗工时
		IMap workHardMap = BeanFactory.getClassBean("com.BusWorkCard");
		workHardMap.put("workCard", selectMap.get("mainCard"));
		workHardMap = db.get(workHardMap);
		workHardMap.put("completeDate",in.get("completeDate"));
		workHardMap.put("useManHours",in.get("useManHours"));
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap),workHardMap);
		db.update(workHardMap);
		//工时信息
		List busRepairerHoursList = (List)in.get("busRepairerHours");
		if(!busRepairerHoursList.isEmpty()){
			for(int i=0;i<busRepairerHoursList.size();i++){
				IMap busRepairerHours =(IMap)busRepairerHoursList.get(i);
				busRepairerHours.put("id",HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.BusRepairerHours"));
				HelperDB.setCreate4isValid(HelperApp.getUserName(userMap), busRepairerHours);
				db.insert(busRepairerHours);
			}
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		//工作流信息
		IMap wfmap = new DataMap();
		//wfmap.put("result", flag);// 判断是否受理
		//------------查询当前组织下的厂检验人员，没有则查询所有的车间检验人员
		IMap condition = new DataMap();
		//获取基础组织编码
		String orgCode = (String) userMap.get("orgId");
		String [] orgCodes = orgCode.replace(".", ",").split(",");
		//试车检验结果
		String checkRes =(String)in.get("result");
		wfmap.put("result",checkRes);
		if("1".equals(checkRes)){
			//获取当前用户所属厂区编码
			condition.put("orgId", orgCodes[0]+"."+orgCodes[1]);
			condition.put("postCode", "调度");
			
		}else{
			condition.put("orgId",orgCode);
			condition.put("postCode", "车间材料员");
		}
		List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
		String cbids = "";
		for(int i=0;i<userList.size();i++){
			IMap usersMap = BeanFactory.getClassBean("com.UserInfo");
			usersMap = userList.get(i);
			cbids = cbids+ (String)usersMap.get("userId")+",";
		}
		String[] nextUserIds = cbids.split(",");
		IMap wf_users = new DataMap();
		wf_users.put("u", nextUserIds);
		JSONObject str_wf_user = JSONObject.fromObject(wf_users);
		// 发起工作流
		wfmap.put("assignedbyuser", str_wf_user.toString());
		String instanceId = (String)in.get("instanceId");
		String stepId = (String)in.get("stepId");
		String nodeId = (String)in.get("nodeId");
		FireNetHelper.wf_next(db,userMap, instanceId, stepId, nodeId, wfmap);
		result.put("method.infoMsg",OperateHelper.getSaveMsg());
		result.put("method.url", in.get("url"));
		//log
		String resCheck ="1".equals(checkRes)?"通过。":"不通过。";
		String remark="检验员："+userMap.get("name")+"在"+sdf.format(new Date())+"对设备进行了完工检验，意见为："+in.get("opinionChecker")+"；结果为："+resCheck;
		LogBean logb=new LogBean(userMap,(String)in.get("busId"), "com.MechTreatment","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		return result;
	}
	
	/**
	 * 设备出厂
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Aug 8, 2013 9:48:55 AM
	 */
	public IMap modifyMechTreatmentComplete(IMap in){
		IMap result = new DataMap();
		IMap selectMap = BeanFactory.getClassBean("com.MechTreatment");
		selectMap.put("id", in.get("busId"));
		//进修单信息
		selectMap = db.get(selectMap);
		IMap userMap = (IMap) in.get("userMap");
		//设备出厂状态
		selectMap.put("repairState", in.get("repairState"));
		//为修改数据赋值
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap),selectMap);
		selectMap.put("factoryWhere", in.get("factoryWhere"));
		selectMap.put("factoryDate", in.get("factoryDate"));
		db.update(selectMap);
		//log日志
		String stateStr = (String)in.get("repairState")=="3"?"到装备库":"出厂";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String res ="";
		if("2".equals(in.get("repairState"))){
			res ="退回";
		}else if("3".equals(in.get("repairState"))){
			res ="到装备库";
		}else if("4".equals(in.get("repairState"))){
			res ="出厂";
		}
		String remark="监造："+userMap.get("name")+"在"+sdf.format(new Date())+"进行设备出厂，结果为："+res+"。";
		LogBean logb=new LogBean(userMap,(String)in.get("busId"), "com.MechTreatment","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		//工作流信息
		IMap wfmap = new DataMap();
		String instanceId = (String)in.get("instanceId");
		String stepId = (String)in.get("stepId");
		String nodeId = (String)in.get("nodeId");
		String actionId = "";
		//是否退回
		if("2".equals(in.get("repairState"))){
			actionId ="142";
		}else{
			actionId ="141";
		}
		FireNetHelper.wf_next(db,userMap, instanceId, stepId, nodeId,actionId, wfmap);
		result.put("method.infoMsg",OperateHelper.getSaveMsg());
		result.put("method.url", in.get("url"));
		return result;
	}
	
	/**
	 * 材料计划LIST
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Aug 8, 2013 9:50:13 AM
	 */
	public IMap getBusMaterialPlanList(IMap in){
		IMap result = new DataMap();
		//当前计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
		busPlanInfoMap.put("busId",in.get("id"));
		busPlanInfoMap.put("busName","com.MechTreatment");
		busPlanInfoMap.put("isValid", 1);
		busPlanInfoMap = db.get(busPlanInfoMap);
		//材料申请信息
		IMap busMaterialPlanMap =BeanFactory.getClassBean("com.BusMaterialPlan");
		busMaterialPlanMap.put("planId",busPlanInfoMap.get("id"));
		//type 1：领用 2：加工 3维修 4 计划加领用
		busMaterialPlanMap.put("type","1,4");
		//获取当前审批的材料申请列表
		List busMaterialPlanList = db.getList(busMaterialPlanMap,"getBusMaterialPlanList", "com.BusMaterialPlan");
		//查询该材料计划所属的车间
		IMap selectMap = BeanFactory.getClassBean("com.MechTreatment");
		selectMap.put("id", in.get("id"));
		//业务信息
		selectMap = db.get(selectMap);
		result.put("mechTreatmentMap",selectMap);
		//需要加工的零件信息
		IMap machiningMap =BeanFactory.getClassBean("com.mechdept");
		machiningMap.put("busid",selectMap.get("id"));
		List<IMap> machiningList =db.getList(machiningMap,"getmechdeptlist", "com.mechdept");
		
		
		result.put("machiningList", machiningList);
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = selectMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", selectMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		result.put("groupName", baseOrganization.get("orgName"));
		//返回busMetarialPlanList,busId
		result.put("busMaterialPlanList",busMaterialPlanList);
		result.put("busId",in.get("id"));
		result.put("orgCode",selectMap.get("orgId"));
		result.put("stepId", in.get("stepId"));
		result.put("nodeId", in.get("nodeId"));
		result.put("instanceId", in.get("instanceId"));
		return result;
	}
	
	/**
	 * 上报器材库
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Aug 8, 2013 9:51:31 AM
	 */
	public IMap modifyMechTreatmentReport(IMap in){
		IMap result = new DataMap();
		IMap userMap = (IMap) in.get("userMap");
		List busMaterialPlanList = (List) in.get("busMaterialPlan");
		for (int i = 0; i < busMaterialPlanList.size(); i++) {
			//保存busMaterialPlan
			IMap busMaterialPlan = BeanFactory.getClassBean("com.BusMaterialPlan");
			//修改busMaterialPlan
			IMap updatePlan = (IMap) busMaterialPlanList.get(i);
			busMaterialPlan.put("id", updatePlan.get("id"));
			busMaterialPlan = db.get(busMaterialPlan);
			busMaterialPlan.put("realNum", updatePlan.get("realNum"));
			HelperDB.setModifyInfo(HelperApp.getUserName(userMap),busMaterialPlan);
			db.update(busMaterialPlan);
		}
		// 发起工作流
		IMap wfmap = new DataMap();
		//wfmap.put("result", flag);// 判断是否受理
		//------------查询当前组织下的厂检验人员，没有则查询所有的车间检验人员
		IMap condition = new DataMap();
		//获取基础组织编码
		String [] orgCodes =in.get("orgCode").toString().replace(".",",").split(",");
		condition.put("orgId", orgCodes[0]);
		condition.put("postCode", "器材公司");
		List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
		String cbids = "";
		for(int i=0;i<userList.size();i++){
			IMap usersMap = BeanFactory.getClassBean("com.UserInfo");
			usersMap = userList.get(i);
			cbids = cbids+ (String)usersMap.get("userId")+",";
		}
		String[] nextUserIds = cbids.split(",");
		IMap wf_users = new DataMap();
		wf_users.put("u", nextUserIds);
		JSONObject str_wf_user = JSONObject.fromObject(wf_users);
		wfmap.put("assignedbyuser", str_wf_user.toString());
		String instanceId = (String) in.get("instanceId");
		String nodeId = (String) in.get("nodeId");
		String stepId = (String) in.get("stepId");
		FireNetHelper.wf_next(db, userMap, instanceId, stepId, nodeId,wfmap);
		//log日志
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String remark="材料员："+userMap.get("name")+"在"+sdf.format(new Date())+"上报器材库。";
		LogBean logb=new LogBean(userMap,(String)in.get("id"), "com.MechTreatment","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg", "确认成功！");
		result.put("method.url", in.get("url"));
		return result;
	}

	/**
	 * 跳转调度确认
	 * @param @param in
	 * @param @return  
	 * @author 孟勃婷  
	 * @return IMap<String,Object>
	 * logType:0-普通，1-审批
	 * @date Aug 8, 2013 9:51:59 AM
	 */
	public IMap<String, Object> toConfirmMechTreatment(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.MechTreatment");
		selectMap.put("id", in.get("id"));
		//业务表基本信息
		IMap mechTreatmentMap = db.get(selectMap);
		result.put("mechTreatmentMap",mechTreatmentMap);
		IMap condition = new DataMap();
		//业务表
		condition.put("busIdOne",mechTreatmentMap.get("id"));
		condition.put("busNameOne","com.MechTreatment");
		//进修单
		condition.put("busIdTow",mechTreatmentMap.get("inrepairId"));
		condition.put("busNameTow","com.MachiningMessage");
		//附件列表
		List<IMap> fileList = db.getList(condition, "getAllFileList", "com.AttachmentInfo");
		//操作记录
		List<IMap> logRecordsList = db.getList(condition, "getLogRecordsList", "com.logRecords");
		result.put("logRecordsList", logRecordsList);
		//机加单信息
		IMap machiningMap =BeanFactory.getClassBean("com.MachiningMessage");
		machiningMap.put("id",mechTreatmentMap.get("inrepairId"));
		machiningMap =db.get(machiningMap);
		result.put("machiningMap",machiningMap);
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = mechTreatmentMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", mechTreatmentMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		result.put("groupName", baseOrganization.get("orgName"));
		//派工单信息
		IMap busWorkCardMap =BeanFactory.getClassBean("com.BusWorkCard");
		busWorkCardMap.put("busId",mechTreatmentMap.get("id"));
		busWorkCardMap.put("busName","com.MechTreatment");
		busWorkCardMap = db.get(busWorkCardMap);
		result.put("busWorkCard", busWorkCardMap);
		//派工单备注信息
		IMap workTypeMap=BeanFactory.getClassBean("com.WorkTypeInfo");
		workTypeMap.put("workId", busWorkCardMap.get("id"));
		List workTypeList=db.getList(workTypeMap, null);
		result.put("workTypeList", workTypeList);
		result.put("logRecordsList", logRecordsList);
		result.put("fileList", fileList);
		result.put("busId",in.get("id"));
		result.put("orgCode",selectMap.get("orgId"));
		result.put("stepId", in.get("stepId"));
		result.put("nodeId", in.get("nodeId"));
		result.put("instanceId", in.get("instanceId"));
		return result;
	}
	/**
	 * 调度确认
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Aug 8, 2013 9:54:59 AM
	 */
	public IMap saveMechTreatmentConfirm(IMap in){
		IMap result = new DataMap();
		IMap userMap = (IMap) in.get("userMap");
		// 发起工作流
		IMap wfmap = new DataMap();
		//wfmap.put("result", flag);// 判断是否受理
		//------------查询当前组织下的厂检验人员，没有则查询所有的车间检验人员
		IMap condition = new DataMap();
		//获取基础组织编码
		IMap mechTreatmentMap = BeanFactory.getClassBean("com.MechTreatment");
		mechTreatmentMap.put("id", in.get("id"));
		mechTreatmentMap = db.get(mechTreatmentMap);
		mechTreatmentMap.put("factoryWhere", in.get("factoryWhere"));
		mechTreatmentMap.put("factoryDate", in.get("factoryDate"));
		db.update(mechTreatmentMap);
		String rem="";
		String orgId="";
		String [] orgCodes =mechTreatmentMap.get("orgId").toString().replace(".",",").split(",");
		String postCode="";
		if("1".equals(in.get("result"))){
			orgId = orgCodes[0]+"."+orgCodes[1];
			postCode="监造主管";
			rem="把设备发给"+postCode+"进行出厂。";
		}else{
			orgId =orgCodes[0]+"."+orgCodes[1];;
			postCode ="出厂";
			rem="把设备出厂到"+in.get("factoryWhere")+"。";
			IMap factoryMap = BeanFactory.getClassBean("com.Factory");
			String factoryId =HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(), "com.Factory");
			factoryMap.put("id", factoryId);
			factoryMap.put("busId", mechTreatmentMap.get("id"));
			factoryMap.put("busName", "com.MechTreatment");
			factoryMap.put("affirmType",1);//状态为1调度直接出厂
			factoryMap.put("orgId",userMap.get("orgId"));
			factoryMap.put("workCard",mechTreatmentMap.get("workCard"));
			factoryMap.put("equipmentName",mechTreatmentMap.get("equipmentName"));
			factoryMap.put("equipmentModel",mechTreatmentMap.get("equipmentModel"));
			factoryMap.put("factoryDate", in.get("factoryDate"));
			db.insert(factoryMap);
		}
		condition.put("orgId", orgId);
		condition.put("postCode", postCode);
		List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
		String cbids = "";
		for(int i=0;i<userList.size();i++){
			IMap usersMap = BeanFactory.getClassBean("com.UserInfo");
			usersMap = userList.get(i);
			cbids = cbids+ (String)usersMap.get("userId")+",";
		}
		String[] nextUserIds = cbids.split(",");
		IMap wf_users = new DataMap();
		wf_users.put("u", nextUserIds);
		JSONObject str_wf_user = JSONObject.fromObject(wf_users);
		wfmap.put("assignedbyuser", str_wf_user.toString());
		//创收状态 type 0 内部创收  1 外部创收
		String instanceId = (String) in.get("instanceId");
		String nodeId = (String) in.get("nodeId");
		String stepId = (String) in.get("stepId");
		
		String actionId = "";
		String revenueType = (String)mechTreatmentMap.get("revenueType");
		if("1".equals(revenueType)){
			actionId = "131";
		}else{
			actionId = "141";
		}
		FireNetHelper.wf_next(db, userMap, instanceId, stepId, nodeId, actionId,wfmap);
		//log日志
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String remark="调度："+userMap.get("name")+"在"+sdf.format(new Date())+rem;
		LogBean logb=new LogBean(userMap,(String)in.get("id"), "com.MechTreatment","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg", "确认成功！");
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 调度管理
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @date Jul 19, 2013 9:38:03 AM
	 */
	public IMap doSchedulingManage(IMap in){
		IMap result = new DataMap();
		IMap userMap = (IMap) in.get("userMap");
		//wfmap.put("result", flag);// 判断是否受理
		//获取基础组织编码
		IMap mechTreatmentMap = BeanFactory.getClassBean("com.MechTreatment");
		mechTreatmentMap.put("id", in.get("id"));
		mechTreatmentMap = db.get(mechTreatmentMap);
		mechTreatmentMap.put("factoryWhere", in.get("factoryWhere"));
		mechTreatmentMap.put("factoryDate", in.get("factoryDate"));
		db.update(mechTreatmentMap);
		String rem="";
		IMap factoryMap = BeanFactory.getClassBean("com.Factory");
		factoryMap.put("busId", mechTreatmentMap.get("id"));
		factoryMap.put("busName", "com.MechTreatment");
		factoryMap=db.get(factoryMap);
		if("1".equals(in.get("result"))){
			rem="把设备发给监造主管进行出厂。";
			factoryMap.put("affirmType",2);//状态为2调度发现有错误然后提交给监造出厂
		}else{
			rem="确认把设备出厂到"+in.get("factoryWhere")+"。";
			factoryMap.put("affirmType",0);//状态为0调度确认自己出厂
		}
		factoryMap.put("factoryDate", in.get("factoryDate"));
		db.update(factoryMap);
		//log日志
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String remark="调度："+userMap.get("name")+"在"+sdf.format(new Date())+rem;
		LogBean logb=new LogBean(userMap,(String)in.get("id"), "com.MechTreatment","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg", "操作成功！");
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 厂材料员确认
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Aug 8, 2013 9:56:37 AM
	 */
	public IMap saveMaterialConfirm(IMap in){
		IMap result = new DataMap();
		IMap userMap = (IMap) in.get("userMap");
		// 发起工作流
		IMap wfmap = new DataMap();
		//wfmap.put("result", flag);// 判断是否受理
		//------------查询当前组织下的厂检验人员，没有则查询所有的车间检验人员
		IMap condition = new DataMap();
		//获取基础组织编码
		IMap mechTreatmentMap = BeanFactory.getClassBean("com.MechTreatment");
		mechTreatmentMap.put("id", in.get("id"));
		mechTreatmentMap = db.get(mechTreatmentMap);
		String orgId= (String) mechTreatmentMap.get("orgId");
		String postCode="";
		postCode="车间材料员";
		condition.put("orgId", orgId);
		condition.put("postCode", postCode);
		List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
		String cbids = "";
		for(int i=0;i<userList.size();i++){
			IMap usersMap = BeanFactory.getClassBean("com.UserInfo");
			usersMap = userList.get(i);
			cbids = cbids+ (String)usersMap.get("userId")+",";
		}
		String[] nextUserIds = cbids.split(",");
		IMap wf_users = new DataMap();
		wf_users.put("u", nextUserIds);
		JSONObject str_wf_user = JSONObject.fromObject(wf_users);
		wfmap.put("assignedbyuser", str_wf_user.toString());
		wfmap.put("result","1");
		String instanceId = (String) in.get("instanceId");
		String nodeId = (String) in.get("nodeId");
		String stepId = (String) in.get("stepId");
		FireNetHelper.wf_next(db, userMap, instanceId, stepId, nodeId,wfmap);
		//log日志
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String remark="厂材料员："+userMap.get("name")+"在"+sdf.format(new Date())+"进行了器材库确认。";
		LogBean logb=new LogBean(userMap,(String)in.get("id"), "com.MechTreatment","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg", "确认成功！");
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 跳转材料申请页面
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Aug 8, 2013 10:01:21 AM
	 */
	public IMap<String, Object> toMaterialPlan(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		
		IMap selectMap = BeanFactory.getClassBean("com.MechTreatment");
		selectMap.put("id", in.get("id"));
		//进修单信息
		IMap mechTreatmentMap = db.get(selectMap);
		result.put("mechTreatmentMap",mechTreatmentMap);
		//机加单信息
		//需要加工的零件信息
		IMap machiningMap =BeanFactory.getClassBean("com.mechdept");
		machiningMap.put("busid",mechTreatmentMap.get("id"));
		List<IMap> machiningList =db.getList(machiningMap,"getmechdeptlist", "com.mechdept");
		
		
		result.put("machiningList", machiningList);
		
		//工作流信息
		result.put("stepId", in.get("stepId"));
		result.put("nodeId", in.get("nodeId"));
		result.put("instanceId", in.get("instanceId"));
		//当前审批计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
		busPlanInfoMap.put("busId",mechTreatmentMap.get("id"));
		busPlanInfoMap.put("busName","com.MechTreatment");
		busPlanInfoMap.put("isValid",1);
		//第几份材料申请计划
		List list = db.getList(busPlanInfoMap,null);
		result.put("num", list.size()+1);
		//auditState - 0  审核不通过 1 审核通过 2未审
		busPlanInfoMap.put("auditState",0);
		busPlanInfoMap = db.get(busPlanInfoMap);
		//材料申请信息
		IMap busMaterialPlanMap =BeanFactory.getClassBean("com.BusMaterialPlan");
		//判断当前是否已有材料申请
		if(busPlanInfoMap!=null){
			busMaterialPlanMap.put("planId",busPlanInfoMap.get("id"));
			//获取当前审批的材料申请列表
			List busMaterialPlanList = db.getList(busMaterialPlanMap, null);
			//回写 材料申请列表
			result.put("busMaterialPlanList",busMaterialPlanList);
			result.put("num", list.size());
		}
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = mechTreatmentMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", mechTreatmentMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		result.put("groupName", baseOrganization.get("orgName"));
		//业务表
		IMap condition = new DataMap();
		condition.put("busIdOne",mechTreatmentMap.get("id"));
		condition.put("busNameOne","com.MechTreatment");
		
		//附件列表
//		IMap attachmentMap =BeanFactory.getClassBean("com.AttachmentInfo");
//		attachmentMap.put("busId",machiningMap.get("id"));
//		attachmentMap.put("busName","com.MachiningMessage");
//		attachmentMap.put("busType","1");	
//		attachmentMap.put("isValid",1);	
//		OrderBean orderBean = new OrderBean();
//		orderBean.put("createDate",OrderBean.DESC);
//		List fileList = db.getList(attachmentMap, orderBean);
//		result.put("fileList", fileList);
		//操作记录
		List<IMap> logRecordsList = db.getList(condition, "getLogRecordsList", "com.logRecords");
		result.put("logRecordsList", logRecordsList);
		return result;
	}
	/**
	 * 派工单信息
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Aug 8, 2013 10:10:38 AM
	 */
	public IMap<String, Object> getbusWorkAndMechTreatmentDetails(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//IMap userMap = (IMap) in.get("userMap");
		//String orgId = (String) userMap.get("orgid");
		
		IMap selectMap = BeanFactory.getClassBean("com.MechTreatment");
		selectMap.put("id", in.get("id"));
		//进修单信息
		IMap mechTreatmentMap = db.get(selectMap);
		//施工卡信息
		IMap busWorkCardMap =BeanFactory.getClassBean("com.BusWorkCard");
		busWorkCardMap.put("busId",mechTreatmentMap.get("id"));
		busWorkCardMap.put("busName","com.MechTreatment");
		//需要加工的零件信息
		IMap machiningMap =BeanFactory.getClassBean("com.mechdept");
		machiningMap.put("busid",mechTreatmentMap.get("id"));
		List<IMap> machiningList =db.getList(machiningMap,"getmechdeptlist", "com.mechdept");
		
		
		result.put("machiningList", machiningList);
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = mechTreatmentMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", mechTreatmentMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		result.put("groupName", baseOrganization.get("orgName"));
		//根据业务表Id的到施工卡列表
		List busWorkCardList = db.getList(busWorkCardMap, null);
		IMap condition = new DataMap();
		//业务表
		condition.put("busIdOne",mechTreatmentMap.get("id"));
		condition.put("busNameOne","com.MechTreatment");
		//附件列表
		
		List<IMap> fileList = db.getList(condition, "getAllFileList", "com.AttachmentInfo");
		result.put("fileList", fileList);
		result.put("mechTreatmentMap",mechTreatmentMap);
		result.put("busWorkCardList", busWorkCardList);
		result.put("stepId", in.get("stepId"));
		result.put("instanceId",in.get("instanceId"));
		result.put("nodeId", in.get("nodeId"));
		return result;
	}
	/**
	 * 添加施工卡
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Aug 8, 2013 10:12:12 AM
	 */
	public IMap saveBusWorkCard(IMap in){
		IMap result = new DataMap();
		IMap busWorkCardMap = (IMap) in.get("busworkcard");
		IMap userMap = (IMap) in.get("userMap");
		Date date=new Date();	
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date_str=df.format(date);
		IMap busWorkCard =BeanFactory.getClassBean("com.BusWorkCard");
		busWorkCard.put("id", busWorkCardMap.get("id"));
		busWorkCard=db.get(busWorkCard);
		//派工日期
		busWorkCard.put("appointDate", busWorkCardMap.get("appointDate"));
		//开工日期
		busWorkCard.put("startDate", busWorkCardMap.get("startDate"));
		//指定完成日期
		busWorkCard.put("orderEndDate", busWorkCardMap.get("orderEndDate"));
		//修改人
		busWorkCard.put("modifyUser",userMap.get("userName")+"/"+userMap.get("name"));
		//修改时间
		busWorkCard.put("modifyDate",date_str);
		db.update(busWorkCard);
		List workTypeList = (List) in.get("workType");
		for(int a=0;a<workTypeList.size();a++){
		  IMap workTypeMap = (IMap)workTypeList.get(a);
		  workTypeMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.WorkTypeInfo"));
		  workTypeMap.put("workId", busWorkCard.get("id"));
		  String[] strs=workTypeMap.get("workType").toString().split("@");
		  workTypeMap.put("workType", strs[1]);
		  workTypeMap.put("typeCode", strs[0]);
		  db.insert(workTypeMap);
		}
		SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String remark="派工员："+userMap.get("name")+"在"+dftow.format(date)+"添加了施工卡信息。";
		LogBean logb=new LogBean(userMap,in.get("busId").toString(), "com.MechTreatment","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		//插入业务表信息
		IMap selectMap = BeanFactory.getClassBean("com.MechTreatment");
		selectMap.put("id", in.get("busId"));
		
		IMap mechTreatmentMap = db.get(selectMap);
		if(Util.checkNull(mechTreatmentMap.get("workCard"))){
			mechTreatmentMap.put("assistCard", in.get("assistCard"));
			mechTreatmentMap.put("workCard", (String)in.get("assistCard")+"/"+(String)in.get("mainCard"));
		}
		mechTreatmentMap.put("dispatchingRemark", in.get("dispatchingRemark"));
		//将开工时间添加到主表中
		mechTreatmentMap.put("startDate", busWorkCardMap.get("startDate"));
		db.update(mechTreatmentMap);
		
		
		// 发起工作流
		IMap wfmap = new DataMap();
		//wfmap.put("result", flag);// 判断是否受理
		//------------查询当前组织下的厂检验人员，没有则查询所有的车间检验人员
		IMap condition = new DataMap();
		//获取基础组织编码
		String orgCode = (String) userMap.get("orgId");
		condition.put("orgId", orgCode);
		condition.put("postCode", "车间材料员");
		List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
		String cbids = "";
		for(int i=0;i<userList.size();i++){
			IMap usersMap = BeanFactory.getClassBean("com.UserInfo");
			usersMap = userList.get(i);
			cbids = cbids+ (String)usersMap.get("userId")+",";
		}
		String[] nextUserIds = cbids.split(",");
		IMap wf_users = new DataMap();
		wf_users.put("u", nextUserIds);
		JSONObject str_wf_user = JSONObject.fromObject(wf_users);
		wfmap.put("assignedbyuser", str_wf_user.toString());
		String instanceId = (String) in.get("instanceId");
		String nodeId = (String) in.get("nodeId");
		String stepId = (String) in.get("stepId");
		//double makeNum = Double.parseDouble((String)mechMap.get("makeNum"));
		//if(makeNum==0){
		//	wfmap.put("result","2");
		//}
		FireNetHelper.wf_next(db, userMap, instanceId, stepId, nodeId,wfmap);
		result.put("method.url", in.get("url"));
		result.put("method.infoMsg", "提交成功！");
		return result;
	}

	/**
	 * 材料申请审批信息
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Aug 8, 2013 10:12:56 AM
	 */
	public IMap<String, Object> getMechTreatmentAndMaterialPlanDetails(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.MechTreatment");
		selectMap.put("id", in.get("id"));
		//业务表基本信息
		IMap mechTreatmentMap = db.get(selectMap);
		//当前审批计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
		busPlanInfoMap.put("busId",mechTreatmentMap.get("id"));
		busPlanInfoMap.put("busName","com.MechTreatment");
		//auditState - 0  审核不通过 1 审核通过 2未审
		busPlanInfoMap.put("isValid",1);
		busPlanInfoMap = db.get(busPlanInfoMap);
		result.put("busPlanInfoMap", busPlanInfoMap);
		//材料申请信息
		IMap BusMaterialPlanMap =BeanFactory.getClassBean("com.BusMaterialPlan");
		BusMaterialPlanMap.put("planId",busPlanInfoMap.get("id"));
	    //type 1：领用 2：加工 3维修
		//非主任审批，只显示领用件
		if(!"zr".equals(in.get("auditPerson"))){
			BusMaterialPlanMap.put("type","1");
		}
		//获取当前审批的材料申请列表
		List busMaterialPlanList = db.getList(BusMaterialPlanMap, null);
		String sk="0";
		String cq="0";
		for(int i=0;i<busMaterialPlanList.size();i++){
			IMap busMaterialPlan=(IMap) busMaterialPlanList.get(i);
			Float money=Float.parseFloat(busMaterialPlan.get("estimatePrice").toString());
			 if(5000<=money){
			    	cq="1";
			    	if(money>=10000){
				    	sk="1";
				    	break;
				    }
			    	break;
			    }
		}
		//审批详情列表
		IMap logRecordsMap =BeanFactory.getClassBean("com.logRecords");
		logRecordsMap.put("busId",mechTreatmentMap.get("id"));
		logRecordsMap.put("busName","com.MechTreatment");
		logRecordsMap.put("logType","1");
		OrderBean orderBean = new OrderBean();
		orderBean.put("operationDate",OrderBean.DESC);
		List logRecordsList = db.getList(logRecordsMap, orderBean);
		//派工单信息
		IMap busWorkCardMap =BeanFactory.getClassBean("com.BusWorkCard");
		busWorkCardMap.put("busId",mechTreatmentMap.get("id"));
		busWorkCardMap.put("busName","com.MechTreatment");
		//根据业务表Id的到施工卡列表
		List busWorkCardList = db.getList(busWorkCardMap, null);
//		//附件列表
//		IMap attachmentMap =BeanFactory.getClassBean("com.AttachmentInfo");
//		attachmentMap.put("busId",mechTreatmentMap.get("id"));
//		attachmentMap.put("busName","com.MechTreatment");
//		attachmentMap.put("busType","2");	
//		attachmentMap.put("isValid",1);	
//		orderBean = new OrderBean();
//		orderBean.put("createDate",OrderBean.DESC);
//		List fileList = db.getList(attachmentMap, orderBean);
		//需要加工的零件信息
		IMap machiningMap =BeanFactory.getClassBean("com.mechdept");
		machiningMap.put("busid",mechTreatmentMap.get("id"));
		List<IMap> machiningList =db.getList(machiningMap,"getmechdeptlist", "com.mechdept");
		
		
		result.put("machiningList", machiningList);
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = mechTreatmentMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", mechTreatmentMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		result.put("machiningList", machiningList);
		result.put("busWorkCardList", busWorkCardList);
		result.put("logRecordsList", logRecordsList);
		result.put("mechTreatmentMap",mechTreatmentMap);
		result.put("sk",sk);
		result.put("cq",cq);
		result.put("busMaterialPlanList", busMaterialPlanList);
		result.put("stepId", in.get("stepId"));
		result.put("instanceId",in.get("instanceId") );
		result.put("nodeId", in.get("nodeId"));
		result.put("companyName", baseOrganizationMap.get("orgName"));
		result.put("groupName", baseOrganization.get("orgName"));
		return result;
	}
	/**
	 * 审批
	 * @param in
	 * @return
	 */
	public IMap updateleadershipAudit(IMap in){
		IMap result = new DataMap();
		IMap bussMap = (IMap) in.get("mechTreatment");
		IMap userMap = (IMap) in.get("userMap");
		String remark="";
		String docId="";
		String str="";
		String sk = (String) in.get("sk");
		String cq = (String) in.get("cq");
		Date date=new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String date_str=df.format(date);
		db.update(bussMap);
		
		IMap mechTreatmentMap = BeanFactory.getClassBean("com.MechTreatment");
		mechTreatmentMap.put("id",bussMap.get("id"));
		mechTreatmentMap = db.get(mechTreatmentMap);
		//当前审批计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
		busPlanInfoMap.put("busId",mechTreatmentMap.get("id"));
		busPlanInfoMap.put("busName","com.MechTreatment");
		busPlanInfoMap.put("isValid",1);
		busPlanInfoMap = db.get(busPlanInfoMap);
		//下一步审批人
		String postCode ="";
		//获取基础组织编码
		String orgCode = (String) userMap.get("orgId");
		String [] orgCodes = orgCode.replace(".", ",").split(",");
		String orgId ="";
		String spresult = "";
		//只有是经理审批才进上传方法
		if(in.get("type").toString().equals("jl")){
			busPlanInfoMap.put("auditState",bussMap.get("jlResult"));
			//上传审批文件
			IMap attachmentInfoMap=(IMap) in.get("attachmentInfo");
			if(attachmentInfoMap.get("docUrl")!=null){
				attachmentInfoMap.put("busId", mechTreatmentMap.get("id"));
				attachmentInfoMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.AttachmentInfo"));
				attachmentInfoMap.put("isValid",Constants.ISVALID);
				attachmentInfoMap.put("busName","com.MechTreatment");
				//状态2为审批上传文件
				attachmentInfoMap.put("busType","2");	
				//为创建数据赋值
				HelperDB.setCreateInfo(HelperApp.getUserName(userMap),attachmentInfoMap);
				db.insert(attachmentInfoMap);
				docId=attachmentInfoMap.get("id").toString();
			}
			str=bussMap.get("jlResult").equals("0")?"不同意":"同意";
			remark="经理："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+bussMap.get("jlRemark")+"；结果为："+str+"";
			if("0".equals(bussMap.get("jlResult"))){
				orgId = (String) mechTreatmentMap.get("orgId");
				postCode="车间材料员";
				spresult = "0";
			}else if("1".equals(bussMap.get("jlResult"))&&"1".equals(sk)){
				orgId =orgCodes[0];
				postCode="总公司领导";
				spresult = "1";
			}else if("1".equals(bussMap.get("jlResult"))&&!"1".equals(sk)){
				//厂材料员确认
				orgId = (String) mechTreatmentMap.get("orgId");
				String [] orgIds = orgId.replace(".", ",").split(",");
				orgId = orgIds[0]+"."+orgIds[1]+"."+orgIds[2];
				postCode="厂材料员";
				spresult = "2";
			}
		}else if(in.get("type").toString().equals("ccl")){//新增厂区领导审批前-厂材料员审批
			busPlanInfoMap.put("auditState",bussMap.get("cclResult"));
			str=bussMap.get("cclResult").equals("0")?"不同意":"同意";
			remark="厂材料员："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+bussMap.get("cclRemark")+"；结果为："+str+"。";		
			//总公司
			if("0".equals(bussMap.get("cclResult"))){
				orgId = (String) mechTreatmentMap.get("orgId");
				postCode="车间材料员";
				spresult = "0";
			}else{
				orgId =orgCodes[0];
				postCode="厂区领导";
				spresult = "1";
			}
		}else if(in.get("type").toString().equals("cq")){
			busPlanInfoMap.put("auditState",bussMap.get("cqResult"));
			str=bussMap.get("cqResult").equals("0")?"不同意":"同意";
			remark="厂区领导："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+bussMap.get("cqRemark")+"；结果为："+str+"";		
			//总公司
			if("0".equals(bussMap.get("cqResult"))){
				orgId = (String) mechTreatmentMap.get("orgId");
				postCode="车间材料员";
				spresult = "0";
			}else{
				if("1".equals(cq)){
					orgId =orgCodes[0];
					postCode="生产主管";
					spresult = "1";
				}else{
					orgId =orgCode;
					postCode="厂材料员";
					spresult = "2";
				}
			}
		}else if(in.get("type").toString().equals("zr")){
			busPlanInfoMap.put("auditState",bussMap.get("zrResult"));
			str=bussMap.get("zrResult").equals("0")?"不同意":"同意";
			remark="车间主任："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+bussMap.get("zrRemark")+"；结果为："+str+"";	
			if("0".equals(bussMap.get("zrResult"))){
				spresult = "0";
				orgId = (String) mechTreatmentMap.get("orgId");
				postCode="车间材料员";
			}else{
				//机加流程，主任审批通过后厂材料员审批
				spresult = "1";
				//orgId = (String) mechTreatmentMap.get("orgId");
				//postCode="车间材料员";
				orgId =orgCodes[0]+"."+orgCodes[1]+"."+orgCodes[2];
				postCode="厂材料员";
			}
		}else if(in.get("type").toString().equals("sc")){
			busPlanInfoMap.put("auditState",bussMap.get("scResult"));
			str=bussMap.get("scResult").equals("0")?"不同意":"同意";
			remark="生产主管："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+bussMap.get("scRemark")+"；结果为："+str+"";		
			//总公司
			if("0".equals(bussMap.get("scResult"))){
				orgId = (String) mechTreatmentMap.get("orgId");
				postCode="车间材料员";
				spresult = "0";
			}else{
				orgId =orgCodes[0];
				postCode="经营主管";
				spresult = "1";
			}
		}else if(in.get("type").toString().equals("jy")){
			busPlanInfoMap.put("auditState",bussMap.get("jyResult"));
			str=bussMap.get("jyResult").equals("0")?"不同意":"同意";
			remark="经营主管："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+bussMap.get("jyRemark")+"；结果为："+str+"";		
			//总公司
			if("0".equals(bussMap.get("jyResult"))){
				orgId = (String) mechTreatmentMap.get("orgId");
				postCode="车间材料员";
				spresult = "0";
			}else{
				orgId =orgCodes[0];
				postCode="经理";
				spresult = "1";
			}
		}else if(in.get("type").toString().equals("zgs")){
			busPlanInfoMap.put("auditState",bussMap.get("zgsResult"));
			str=bussMap.get("zgsResult").equals("0")?"不同意":"同意";
			//总公司
			remark="总公司领导："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+bussMap.get("zgsRemark")+"；结果为："+str+"";	
			if("0".equals(bussMap.get("zgsResult"))){
				//删除不通过的附件信息
				IMap attachmentMap =BeanFactory.getClassBean("com.AttachmentInfo");
				attachmentMap.put("busId",mechTreatmentMap.get("id"));
				attachmentMap.put("busName","com.MechTreatment");
				//状态2为审批上传文件
				attachmentMap.put("busType","2");
				attachmentMap.put("isValid","1");
				List fileList = db.getList(attachmentMap, null);
				for(int a=0;a<fileList.size();a++){
				IMap  attachmentInfo= (IMap)fileList.get(a);
				attachmentInfo.put("isValid",Constants.ISNOTVALID);
				db.update(attachmentInfo);
				};
				orgId = (String) mechTreatmentMap.get("orgId");
				postCode="车间材料员";
				spresult = "0";
			}else{
				orgId = (String) mechTreatmentMap.get("orgId");
				//厂材料员确认 
				String[] orgids = orgId.replace(".", ",").split(",");
				orgId =orgids[0]+"."+orgids[1]+"."+orgids[2];
				postCode="厂材料员";
				spresult = "1";
			}
			//上传附件
			List attachmentInfoList=(List) in.get("attachmentInfo");
			for(int i=0;i<attachmentInfoList.size();i++){
				IMap  attachmentInfoMap= (IMap)attachmentInfoList.get(i);
				if(attachmentInfoMap.get("docUrl")!=null&!"".equals(attachmentInfoMap.get("docUrl"))){
					attachmentInfoMap.put("busId",mechTreatmentMap.get("id"));
					attachmentInfoMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
							.getPackageName(), "com.AttachmentInfo"));
					attachmentInfoMap.put("isValid",Constants.ISVALID);
					attachmentInfoMap.put("busName","com.MechTreatment");
					attachmentInfoMap.put("busType",1);
					//为创建数据赋值
					HelperDB.setCreateInfo(HelperApp.getUserName(userMap),attachmentInfoMap);
					db.insert(attachmentInfoMap);
				}
			}
		}
		//审核物料计划，修改物料计划状态
		db.update(busPlanInfoMap);
		//获取材料计划列表
		List busMaterialPlanList = (List) in.get("busMaterialPlan");
		for (int i = 0; i < busMaterialPlanList.size(); i++) {
			IMap busMaterialPlan = (IMap)busMaterialPlanList.get(i);
			db.update(busMaterialPlan);
		}
		LogBean logb=null;
		if(!docId.equals("")){
		 logb=new LogBean(userMap,mechTreatmentMap.get("Id").toString(), "com.MechTreatment","",remark,"1",docId);
		}else{
		 logb=new LogBean(userMap,mechTreatmentMap.get("Id").toString(), "com.MechTreatment","",remark,"1");
		}
		LogRecordsService.saveOperationLog(logb,db);
		//工作流信息
		IMap wfmap = new DataMap();
		//wfmap.put("result", flag);// 判断是否受理
		//------------查询当前组织下的厂检验人员，没有则查询所有的车间检验人员
		IMap condition = new DataMap();
		condition.put("orgId", orgId);
		condition.put("postCode",postCode);
		List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
		String cbids = "";
		for(int i=0;i<userList.size();i++){
			IMap usersMap = BeanFactory.getClassBean("com.UserInfo");
			usersMap = userList.get(i);
			cbids = cbids+ (String)usersMap.get("userId")+",";
		}
		String[] nextUserIds = cbids.split(",");
		IMap wf_users = new DataMap();
		wf_users.put("u", nextUserIds);
		JSONObject str_wf_user = JSONObject.fromObject(wf_users);
		// 发起工作流
		wfmap.put("assignedbyuser", str_wf_user.toString());
		String instanceId = (String) in.get("instanceId");
		String nodeId = (String) in.get("nodeId");
		String stepId = (String) in.get("stepId");
		wfmap.put("result",spresult);
		
		FireNetHelper.wf_next(db, userMap, instanceId, stepId, nodeId,wfmap);
		result.put("method.infoMsg", "审批成功！");
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 导向机加详情信息页面
	 * @Description 
	 * @param @param in
	 * @param @return  
	 * @author 孟勃婷  
	 * @return IMap<String,Object>
	 * logType:0-普通，1-审批
	 * @date 2013-08-08 10:19:22
	 */
	public IMap<String, Object> toMechTreatmentDetailsPage(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.MechTreatment");
		selectMap.put("inrepairId", in.get("id"));
		selectMap.put("isValid",1);
		//业务表基本信息
		IMap mechTreatmentMap = db.get(selectMap);
		if(mechTreatmentMap==null){
			IMap selectMaps = BeanFactory.getClassBean("com.MechTreatment");
			selectMaps.put("id", in.get("id"));
		    mechTreatmentMap = db.get(selectMaps);
		}
		//机加单信息
		IMap machiningMap =BeanFactory.getClassBean("com.MachiningMessage");
		machiningMap.put("id",mechTreatmentMap.get("inrepairId"));
		machiningMap =db.get(machiningMap);
		result.put("machiningMap",machiningMap);
		//根据派工单得到申请过planId
		IMap con = new DataMap();
		con.put("busId", mechTreatmentMap.get("id"));
		con.put("busName","com.MechTreatment");
		List list = db.getList(con, "getBusPlanInfoList",null);
		result.put("busPlanInfoList",list);
		//派工单信息
		IMap busWorkCardMap =BeanFactory.getClassBean("com.BusWorkCard");
		busWorkCardMap.put("busId",mechTreatmentMap.get("id"));
		busWorkCardMap.put("busName","com.MechTreatment");
		busWorkCardMap = db.get(busWorkCardMap);
		result.put("busWorkCard", busWorkCardMap);
		//派工单备注信息
		IMap workTypeMap=BeanFactory.getClassBean("com.WorkTypeInfo");
		workTypeMap.put("workId", busWorkCardMap.get("id"));
		OrderBean orderBean = new OrderBean();
		orderBean.put("ranking",OrderBean.ASC);
		List workTypeList=db.getList(workTypeMap, orderBean);
		result.put("workTypeList", workTypeList);
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = mechTreatmentMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", mechTreatmentMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		result.put("groupName", baseOrganization.get("orgName"));
		IMap condition = new DataMap();
		//业务表
		condition.put("busIdOne",mechTreatmentMap.get("id"));
		condition.put("busNameOne","com.MechTreatment");
		//进修单
		condition.put("busIdTow",mechTreatmentMap.get("inrepairId"));
		condition.put("busNameTow","com.MachiningMessage");
		//附件列表
		List<IMap> fileList = db.getList(condition, "getAllFileList", "com.AttachmentInfo");
		result.put("fileList", fileList);
		//操作记录
		List<IMap> logRecordsList = db.getList(condition, "getLogRecordsList", "com.logRecords");
		result.put("logRecordsList", logRecordsList);
		result.put("mechTreatmentMap",mechTreatmentMap);
		return result;
	}
	/**
	 * 跳转出库页面
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Aug 8, 2013 10:25:10 AM
	 */
	public IMap<String, Object> toOutStore(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.MechTreatment");
		selectMap.put("id", in.get("id"));
		//获取基本信息
		IMap mechTreatmentMap = db.get(selectMap);
		//当前审批计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
		busPlanInfoMap.put("busId",in.get("id"));
		busPlanInfoMap.put("busName","com.MechTreatment");
		busPlanInfoMap.put("isValid",1);
		busPlanInfoMap = db.get(busPlanInfoMap);
		if(busPlanInfoMap!=null){
			//材料申请信息
			IMap busMaterialPlanMap =BeanFactory.getClassBean("com.BusMaterialPlan");
			busMaterialPlanMap.put("planId",busPlanInfoMap.get("id"));
		    //type 1：领用 2：加工 3维修
			//获取领用材料计划列表
			busMaterialPlanMap.put("type","1");
			List materialList = db.getList(busMaterialPlanMap, null);
			//获取加工材料计划列表
			busMaterialPlanMap.put("type","2");
			List materialList2 = db.getList(busMaterialPlanMap, null);
			//获取维修材料计划列表
			busMaterialPlanMap.put("type","3");
			List materialList3 = db.getList(busMaterialPlanMap, null);
			result.put("materialList", materialList);
			result.put("materialList2", materialList2);
			result.put("materialList3", materialList3);
		}
		//机加单信息
		//需要加工的零件信息
		IMap machiningMap =BeanFactory.getClassBean("com.mechdept");
		machiningMap.put("busid",mechTreatmentMap.get("id"));
		List<IMap> machiningList =db.getList(machiningMap,"getmechdeptlist", "com.mechdept");
		
		
		result.put("machiningList", machiningList);
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = mechTreatmentMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", mechTreatmentMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		result.put("groupName", baseOrganization.get("orgName"));
		//返回值
		// 工作流信息
		String instanceId = (String) in.get("instanceId");
		String nodeId = (String) in.get("nodeId");
		String stepId = (String) in.get("stepId");
		result.put("mechTreatmentMap",mechTreatmentMap);
		result.put("instanceId", instanceId);
		result.put("nodeId", nodeId);
		result.put("stepId", stepId);
		return result;
	}
	/**
	 * 保存入库结果
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Aug 8, 2013 10:26:03 AM
	 */
	public IMap<String, Object> saveOutStore(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap userMap = (IMap) in.get("userMap");
		
		String orgId = (String) userMap.get("orgid");
		IMap orgInfo =BeanFactory.getClassBean("com.baseorganization");
		orgInfo.put("orgCode", orgId);
		orgInfo = db.get(orgInfo);
		
		String busId = (String) in.get("busId");
		String busName = (String) in.get("busName");
		String workCard = (String) in.get("workCard");
		IMap selectMap =BeanFactory.getClassBean("com.BusWorkCard");
		selectMap.put("busId", busId);
		selectMap.put("busName", busName);
		String orgName = (String) orgInfo.get("orgName");
		//获取页面上填写的出库数量
		List<IMap> outList = (List<IMap>) in.get("outStoreInfo");
		//按照先进先出的原则，进行材料的增减
		//循环页面返回的值
		//判断物料编码相同的，则取数量进行对比，
		//如果第一条数量大于页面填写数量，剩余数量为第一条数量减去填写数量
		//如果第一条数量等于页面填写数量，剩余数量为0 并将其置为无效
		//依次递归，知道页面填写数量减为0为止
		String outStr = "";
		for(int i=0;i<outList.size();i++){
			//获取库存中某一种材料的数量
			IMap tempout = outList.get(i);
			String outString = "";
			tempout.put("busId", busId);
			tempout.put("busName", busName);
			tempout.put("workCard", workCard);
			
			String code = (String) tempout.get("materialCode");
			//获取材料库存材料
			IMap selMap =BeanFactory.getClassBean("com.StoreDetail");
			selMap.put("materialCode", code);
			selMap.put("orgId", orgId);
			selMap.put("isValid", Constants.ISVALID);
			OrderBean ob = new OrderBean();
			ob.put("createDate", OrderBean.DESC);
			List<IMap> storeList = db.getList(selMap, ob);
			//判断如果库存为空则提示此材料库存不足
			if(storeList.isEmpty()){
				outStr += (String)tempout.get("materialDetail")+",";
			}else{
				//获取页面填写的出库数量 转换为int
				Double outNum = Double.valueOf((String) tempout.get("storeNum"));
				//循环库存结果，对比数量
				int j = 0;
				double store = 0;
				double amount = 0;
				//对库存结果进行递归直到出库数量为0为止
				//插入到出库信息表
				tempout.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.outStoreInfo"));
				tempout.put("workCard", in.get("workCard"));
				tempout.put("orgId", orgId);
				tempout.put("deptName", orgName);
				HelperDB.setCreate(HelperApp.getUserName(userMap),tempout);
				
				db.insert(tempout);
				
				//对比库存数量 从库存中减除
				outStr += saveRatioStore(storeList,j,outNum,store,outString,tempout,userMap,amount);
			}
		}
		if(!"".equals(outStr)&&outStr!=null){
			outStr = outStr.substring(0,outStr.length()-1);
			throw new BusinessException("材料"+outStr+"库存不足!");
		}else{
			//成功出库，当前出库计划，修改状态
			IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
			busPlanInfoMap.put("busId",busId);
			busPlanInfoMap.put("busName","com.MechTreatment");
			busPlanInfoMap.put("isValid", 1);
			List busPlanInfoMapList = db.getList(busPlanInfoMap,null);
			if(busPlanInfoMapList!=null&&busPlanInfoMapList.size()>0){
				busPlanInfoMap = (IMap)busPlanInfoMapList.get(0);
				busPlanInfoMap.put("isValid", 0);
				db.update(busPlanInfoMap);
			}
		}
		//工作流信息
		IMap wfmap = new DataMap();
		//wfmap.put("result", flag);// 判断是否受理
		//------------查询当前组织下的厂检验人员，没有则查询所有的车间检验人员
		IMap condition = new DataMap();
		//获取基础组织编码
		String orgCode = (String) userMap.get("orgId");
		condition.put("orgId", orgCode);
		condition.put("postCode", "车间检验员");
		List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
		String cbids = "";
		for(int i=0;i<userList.size();i++){
			IMap usersMap = BeanFactory.getClassBean("com.UserInfo");
			usersMap = userList.get(i);
			cbids = cbids+ (String)usersMap.get("userId")+",";
		}
		String[] nextUserIds = cbids.split(",");
		IMap wf_users = new DataMap();
		wf_users.put("u", nextUserIds);
		JSONObject str_wf_user = JSONObject.fromObject(wf_users);
		// 发起工作流
		wfmap.put("assignedbyuser", str_wf_user.toString());
		// 工作流信息
		String instanceId = (String) in.get("instanceId");
		
		String nodeId = (String) in.get("nodeId");
		String stepId = (String) in.get("stepId");
		FireNetHelper.wf_next(db, userMap, instanceId, stepId, nodeId,
				wfmap);
		//log 日志
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String remark="材料员："+userMap.get("name")+"在"+sdf.format(new Date())+"将物料出库。";
		LogBean logb=new LogBean(userMap,busId, "com.MechTreatment","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		
		result.put("method.url", (String)in.get("url"));
		result.put("method.infoMsg", OperateHelper.getSaveMsg());
		return result;
	}
	/**
	 * 
	 * @Description 在库存数量中减去单机应用的出库数量,将材料对应到单机
	 * @param @param storeList
	 * @param @param material    
	 * @return void   
	 * @author duch
	 * @date Jul 24, 2013 9:32:27 AM
	 */
	private String saveRatioStore(List<IMap> storeList,int j,Double out,Double store,String outString,IMap outStore,IMap userMap,double amount) {
		IMap storeMap =BeanFactory.getClassBean("com.StoreDetail");
		IMap outstoreMap =BeanFactory.getClassBean("com.outStoreDetail");
		//如果J小于storeList的长度 则进行循环
		if(j<storeList.size()){
			storeMap = storeList.get(j);
			Double jstore = (Double)storeMap.get("num");
			store = store + jstore;
			//如果最近的库存数量大于出库数量，则减去库存数量，并更新数据库
			if(store > out){
				storeMap.put("num", store-out);
				db.update(storeMap);
			//在出库表中插入一条数据
				outstoreMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.outStoreDetail"));
				outstoreMap.put("materialClass", outStore.get("materialClass"));
				outstoreMap.put("materialGroup", outStore.get("materialGroup"));
				outstoreMap.put("materialDetail", outStore.get("materialDetail"));
				outstoreMap.put("materialCode", outStore.get("materialCode"));
				outstoreMap.put("workCard", outStore.get("workCard"));
				outstoreMap.put("unity", outStore.get("unity"));
				outstoreMap.put("unitPrice", storeMap.get("unitPrice"));
				outstoreMap.put("amount", (jstore-(store-out))*((Double)storeMap.get("unitPrice")));
				outstoreMap.put("materialNum", jstore-(store-out));
				outstoreMap.put("orgId", outStore.get("orgId"));
				outstoreMap.put("deptName", outStore.get("deptName"));
				outstoreMap.put("busId", outStore.get("busId"));
				outstoreMap.put("busName", outStore.get("busName"));
				outstoreMap.put("outId", outStore.get("id"));
				HelperDB.setCreate(HelperApp.getUserName(userMap),outstoreMap);
				
				amount = amount +out*((Double)storeMap.get("unitPrice"));
				
				db.insert(outstoreMap);
				
			}else if(store == out){
			//如果最近库存数量等于出库数量，置为0，并将数据置为无效
				storeMap.put("num", store-out);
				storeMap.put("isValid", Constants.ISNOTVALID);
				db.update(storeMap);
				
				outstoreMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.outStoreDetail"));
				outstoreMap.put("materialClass", outStore.get("materialClass"));
				outstoreMap.put("materialGroup", outStore.get("materialGroup"));
				outstoreMap.put("materialDetail", outStore.get("materialDetail"));
				outstoreMap.put("materialCode", outStore.get("materialCode"));
				outstoreMap.put("workCard", outStore.get("workCard"));
				outstoreMap.put("unity", outStore.get("unity"));
				outstoreMap.put("materialNum", jstore-(store-out));
				outstoreMap.put("unitPrice", storeMap.get("unitPrice"));
				outstoreMap.put("amount", (jstore-(store-out))*((Double)storeMap.get("unitPrice")));
				outstoreMap.put("orgId", outStore.get("orgId"));
				outstoreMap.put("deptName", outStore.get("deptName"));
				outstoreMap.put("busId", outStore.get("busId"));
				outstoreMap.put("busName", outStore.get("busName"));
				outstoreMap.put("outId", outStore.get("id"));
				HelperDB.setCreate(HelperApp.getUserName(userMap),outstoreMap);
				
				amount = amount + out*((Double)storeMap.get("unitPrice"));
				
				db.insert(outstoreMap);
			}else if(store < out){
			//如果最近库存数量小于出库数量，则将最近库存置为0，并将数据置为无效。且进行下次递归
				outstoreMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.outStoreDetail"));
				outstoreMap.put("materialClass", outStore.get("materialClass"));
				outstoreMap.put("materialGroup", outStore.get("materialGroup"));
				outstoreMap.put("materialDetail", outStore.get("materialDetail"));
				outstoreMap.put("materialCode", outStore.get("materialCode"));
				outstoreMap.put("workCard", outStore.get("workCard"));
				outstoreMap.put("unity", outStore.get("unity"));
				outstoreMap.put("materialNum", jstore);
				outstoreMap.put("unitPrice", storeMap.get("unitPrice"));
				outstoreMap.put("amount", jstore*((Double)storeMap.get("unitPrice")));
				outstoreMap.put("orgId", outStore.get("orgId"));
				outstoreMap.put("deptName", outStore.get("deptName"));
				outstoreMap.put("busId", outStore.get("busId"));
				outstoreMap.put("busName", outStore.get("busName"));
				outstoreMap.put("outId", outStore.get("id"));
				HelperDB.setCreate(HelperApp.getUserName(userMap),outstoreMap);
				
				db.insert(outstoreMap);
				amount = amount + jstore*((Double)storeMap.get("unitPrice"));
				
				storeMap.put("num", 0);
				storeMap.put("isValid", Constants.ISNOTVALID);
				db.update(storeMap);
				
				j=j+1;
				outString += saveRatioStore(storeList,j,out,store,outString,outStore,userMap,amount);
				System.out.print(outString);
			}
			if(amount!=0&&"".equals(outString)){
				outStore.put("amount", amount);
				db.update(outStore);
			}
		}else{
			storeMap = storeList.get(j-1);
			outString += (String)storeMap.get("materialDetail") +",";
		}
		
		
		return outString;
	}
	/**
	 * 跳转到材料确认页面
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @date Jul 17, 2013 14:07:44 AM
	 */
	public IMap<String, Object> toMaterialAffirm(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.MechTreatment");
		selectMap.put("id", in.get("id"));
		//获取基本信息
		IMap mechTreatmentMap = db.get(selectMap);
		//所有出库后的审批计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
		busPlanInfoMap.put("busId",in.get("id"));
		busPlanInfoMap.put("busName","com.MechTreatment");
		busPlanInfoMap.put("isValid",0);
		busPlanInfoMap.put("auditState",1);
		List busPlanInfoList = db.getList(busPlanInfoMap,null);
		result.put("busPlanInfoMap",busPlanInfoList);
		int planLength=0;
		for(int a=0;a<busPlanInfoList.size();a++){
			IMap busPlanInfo = (IMap) busPlanInfoList.get(a);
			IMap busMaterialPlanMap =BeanFactory.getClassBean("com.BusMaterialPlan");
			busMaterialPlanMap.put("planId",busPlanInfo.get("id"));
			List busMaterialPlanList = db.getList(busMaterialPlanMap, null);
			planLength=+busMaterialPlanList.size();
		}
		result.put("planLength",planLength);
		//需要加工的零件信息
		IMap machiningMap =BeanFactory.getClassBean("com.mechdept");
		machiningMap.put("busid",mechTreatmentMap.get("id"));
		List<IMap> machiningList =db.getList(machiningMap,"getmechdeptlist", "com.mechdept");
		
		
		result.put("machiningList", machiningList);
		
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = mechTreatmentMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", mechTreatmentMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		result.put("groupName", baseOrganization.get("orgName"));
		
		// 工作流信息
		String instanceId = (String) in.get("instanceId");
		String nodeId = (String) in.get("nodeId");
		String stepId = (String) in.get("stepId");
		result.put("mechTreatmentMap",mechTreatmentMap );
		result.put("instanceId", instanceId);
		result.put("nodeId", nodeId);
		result.put("stepId", stepId);
		return result;
	}
	/**
	 * 材料确认
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @date Jul 17, 2013 14:07:44 AM
	 */
	public IMap<String, Object> doMaterialAffirm(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		String busId = (String) in.get("busId");
		String busName = (String) in.get("busName");
		IMap userMap = (IMap) in.get("userMap");
		//工作流信息
		IMap wfmap = new DataMap();
		IMap condition = new DataMap();
		//获取基础组织编码
		String orgCode = (String) userMap.get("orgId");
		//确认结果 0：试车 1：车间材料
		wfmap.put("result",in.get("affirmtype"));
		String cbids = "";
		if("0".equals(in.get("affirmtype").toString())){
//			IMap selectMap =BeanFactory.getClassBean("com.BusWorkCard");
//			selectMap.put("busId", busId);
//			selectMap.put("busName", busName);
//			List<IMap> cardlist = db.getList(selectMap, null);
//			if(!cardlist.isEmpty()){
//				for(int i=0;i<cardlist.size();i++){
//					IMap cardMap =BeanFactory.getClassBean("com.BusWorkCard");
//					cardMap = cardlist.get(i);
//					String[] mainRepair = ((String)cardMap.get("mainId")).split(",");
//					String[] assistRepair = ((String)cardMap.get("assistId")).split(",");
//					//拼接主修人
//					for(int j=0;j<mainRepair.length;j++){
//						cbids = cbids+ mainRepair[j]+",";
//					}
//					//拼接辅修人
//					for(int k=0;k<assistRepair.length;k++){
//						cbids = cbids+ assistRepair[k]+",";
//					}
//				}
//			}
			condition.put("orgId",orgCode);
			condition.put("postCode", "车间材料员");
			List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
			for(int i=0;i<userList.size();i++){
				IMap usersMap = BeanFactory.getClassBean("com.UserInfo");
				usersMap = userList.get(i);
				cbids = cbids+ (String)usersMap.get("userId")+",";
			}
		}else{
			condition.put("orgId",orgCode);
			condition.put("postCode", "车间材料员");
			List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
			for(int i=0;i<userList.size();i++){
				IMap usersMap = BeanFactory.getClassBean("com.UserInfo");
				usersMap = userList.get(i);
				cbids = cbids+ (String)usersMap.get("userId")+",";
			}
		}
		
		String[] nextUserIds = cbids.split(",");
		IMap wf_users = new DataMap();
		wf_users.put("u", nextUserIds);
		JSONObject str_wf_user = JSONObject.fromObject(wf_users);
		// 发起工作流
		wfmap.put("assignedbyuser", str_wf_user.toString());
		String instanceId = (String)in.get("instanceId");
		String stepId = (String)in.get("stepId");
		String nodeId = (String)in.get("nodeId");
		FireNetHelper.wf_next(db,userMap, instanceId, stepId, nodeId, wfmap);
		result.put("method.infoMsg",OperateHelper.getSaveMsg());
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 监造管理
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @date Jul 18, 2013 5:35:51 PM
	 */
	public IMap doEngineerManage(IMap in){
		IMap result = new DataMap();
		IMap selectMap = BeanFactory.getClassBean("com.MachiningMessage");
		selectMap.put("id", in.get("busId"));
		//进修单信息
		selectMap = db.get(selectMap);
		IMap userMap = (IMap) in.get("userMap");
		//设备出厂状态
		selectMap.put("repairState", in.get("repairState"));
		//为修改数据赋值
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap),selectMap);
		selectMap.put("factoryWhere", in.get("factoryWhere"));
		selectMap.put("factoryDate", in.get("factoryDate"));
		db.update(selectMap);
		IMap factoryMap = BeanFactory.getClassBean("com.Factory");
		factoryMap.put("busId", selectMap.get("id"));
		factoryMap.put("busName", "com.MachiningMessage");
		factoryMap=db.get(factoryMap);
		factoryMap.put("affirmType",in.get("repairState"));
		factoryMap.put("factoryDate", in.get("factoryDate"));
		db.update(factoryMap);
		//log日志
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String res ="";
	    if("3".equals(in.get("repairState"))){
			res ="到装备库";
		}else if("4".equals(in.get("repairState"))){
			res ="出厂";
		}
		String remark="监造："+userMap.get("name")+"在"+sdf.format(new Date())+"进行设备出厂，结果为："+res+"。";
		LogBean logb=new LogBean(userMap,(String)in.get("busId"), "com.MachiningMessage","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg",OperateHelper.getSaveMsg());
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 入库存
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Jun 29, 2014 11:17:11 AM
	 */
	public IMap saveInStore(IMap in){
		IMap result = new DataMap();
		IMap mechMap = BeanFactory.getClassBean("com.MechTreatment");
		mechMap.put("id", in.get("busId"));
		mechMap = db.get(mechMap);
		IMap selectMap = BeanFactory.getClassBean("com.MachiningMessage");
		selectMap.put("id", mechMap.get("inrepairId"));
		selectMap = db.get(selectMap);
		//进修单信息
		IMap userMap = (IMap) in.get("userMap");
		//领用数量
		double out = (Double)selectMap.get("outNum");
		//加工数量
		double makeNum = (Double)selectMap.get("makeNum");
		//计划数量
		double planNum = (Double)selectMap.get("planNum");
		//发其他车间数量
		double otherNum = 0;
		if((String)in.get("otherNum")!=null&&!"".equals((String)in.get("otherNum"))){
			otherNum = Double.parseDouble((String)in.get("otherNum"));
		}
		//加工数量入库
		IMap materialInfo = BeanFactory.getClassBean("com.MaterialInfo");
		materialInfo.put("materialDetail", selectMap.get("equipmentName"));
		//加工材料
		List list = db.getList(materialInfo, null);
		if(list.size()>0){
			materialInfo = (IMap)list.get(0);
		}
		//加工件入库信息
		IMap storeDetail = BeanFactory.getClassBean("com.StoreDetail");
		storeDetail.put("id", HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(), "com.StoreDetail"));
		HelperDB.setCreate4isValid(HelperApp.getUserName(userMap), storeDetail);
		storeDetail.put("materialClass", materialInfo.get("materialClass"));
		storeDetail.put("materialGroup", materialInfo.get("materialGroup"));
		storeDetail.put("materialDetail", materialInfo.get("materialDetail"));
		storeDetail.put("materialCode", materialInfo.get("materialCode"));
		storeDetail.put("materialType",3);
		storeDetail.put("num",makeNum);
		storeDetail.put("unitPrice",materialInfo.get("unitPrice"));
		storeDetail.put("unity",materialInfo.get("unity"));
		//入库车间
		storeDetail.put("orgId",userMap.get("orgId"));
		IMap orgMap = BeanFactory.getClassBean("com.baseorganization");
		orgMap.put("orgCode", userMap.get("orgId"));
		List orgList = db.getList(orgMap, null);
		if(orgList.size()>0){
			orgMap = (IMap)orgList.get(0);
		}
		storeDetail.put("deptName",orgMap.get("orgName"));
		db.insert(storeDetail);
		//入库(领用)
		//送修单位
		IMap proDept = BeanFactory.getClassBean("com.baseorganization");
		proDept.put("orgCode", mechMap.get("proDept"));
		List proDeptList = db.getList(proDept, null);
		if(proDeptList.size()>0){
			proDept = (IMap)proDeptList.get(0);
		}
		IMap instoreDetailMap = BeanFactory.getClassBean("com.StoreDetail");
		instoreDetailMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(), "com.StoreDetail"));
		HelperDB.setCreate4isValid(HelperApp.getUserName(userMap), instoreDetailMap);
		instoreDetailMap.put("materialClass", materialInfo.get("materialClass"));
		instoreDetailMap.put("materialGroup", materialInfo.get("materialGroup"));
		instoreDetailMap.put("materialDetail", materialInfo.get("materialDetail"));
		instoreDetailMap.put("materialCode", materialInfo.get("materialCode"));
		instoreDetailMap.put("materialType",3);
		instoreDetailMap.put("num",otherNum);
		instoreDetailMap.put("unitPrice",materialInfo.get("unitPrice"));
		instoreDetailMap.put("unity",materialInfo.get("unity"));
		instoreDetailMap.put("orgId",proDept.get("orgCode"));
		instoreDetailMap.put("deptName",proDept.get("orgName"));
		db.insert(instoreDetailMap);
		//出库（领用）
		IMap outMap = BeanFactory.getClassBean("com.outStoreInfo");
		outMap.put("materialClass", materialInfo.get("materialClass"));
		outMap.put("materialGroup", materialInfo.get("materialGroup"));
		outMap.put("materialDetail", materialInfo.get("materialDetail"));
		outMap.put("materialCode", materialInfo.get("materialCode"));
		outMap.put("workCard",in.get("workCard"));
		outMap.put("unity", materialInfo.get("unity"));
		outMap.put("storeNum",planNum);
		outMap.put("orgId", proDept.get("orgCode"));
		outMap.put("deptName", proDept.get("orgName"));
		//出库（发其他车间）
		IMap otherMap = BeanFactory.getClassBean("com.outStoreInfo");
		otherMap.put("materialClass", materialInfo.get("materialClass"));
		otherMap.put("materialGroup", materialInfo.get("materialGroup"));
		otherMap.put("materialDetail", materialInfo.get("materialDetail"));
		otherMap.put("materialCode", materialInfo.get("materialCode"));
		otherMap.put("unity", materialInfo.get("unity"));
		otherMap.put("storeNum",otherNum);
		otherMap.put("orgId", orgMap.get("orgCode"));
		otherMap.put("deptName", orgMap.get("orgName"));
		//入库其他车间
		IMap storeDetailMap = BeanFactory.getClassBean("com.StoreDetail");
		storeDetailMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(), "com.StoreDetail"));
		HelperDB.setCreate4isValid(HelperApp.getUserName(userMap), storeDetailMap);
		storeDetailMap.put("materialClass", materialInfo.get("materialClass"));
		storeDetailMap.put("materialGroup", materialInfo.get("materialGroup"));
		storeDetailMap.put("materialDetail", materialInfo.get("materialDetail"));
		storeDetailMap.put("materialCode", materialInfo.get("materialCode"));
		storeDetailMap.put("materialType",3);
		storeDetailMap.put("num",otherNum);
		storeDetailMap.put("unitPrice",materialInfo.get("unitPrice"));
		storeDetailMap.put("unity",materialInfo.get("unity"));
		storeDetailMap.put("orgId",in.get("deptId"));
		storeDetailMap.put("deptName",in.get("deptName"));
		//获取页面上填写的出库数量
		List<IMap> outList = new ArrayList<IMap>();
		outList.add(outMap);
		if(otherNum!=0){
			outList.add(otherMap);
			db.insert(storeDetailMap);
		}
		//按照先进先出的原则，进行材料的增减
		//循环页面返回的值
		//判断物料编码相同的，则取数量进行对比，
		//如果第一条数量大于页面填写数量，剩余数量为第一条数量减去填写数量
		//如果第一条数量等于页面填写数量，剩余数量为0 并将其置为无效
		//依次递归，知道页面填写数量减为0为止
		String outStr = "";
		for(int i=0;i<outList.size();i++){
			//获取库存中某一种材料的数量
			IMap tempout = outList.get(i);
			String outString = "";
			tempout.put("busId", in.get("busId"));
			tempout.put("busName", in.get("busName"));
			tempout.put("workCard", in.get("workCard"));
			String code = (String) tempout.get("materialCode");
			//获取材料库存材料
			IMap selMap =BeanFactory.getClassBean("com.StoreDetail");
			selMap.put("materialCode", code);
			selMap.put("orgId", orgMap.get("orgCode"));
			selMap.put("isValid", Constants.ISVALID);
			OrderBean ob = new OrderBean();
			ob.put("createDate", OrderBean.DESC);
			List<IMap> storeList = db.getList(selMap, ob);
			//判断如果库存为空则提示此材料库存不足
			if(storeList.isEmpty()){
				outStr += (String)tempout.get("materialDetail")+",";
			}else{
				//获取页面填写的出库数量 转换为int
				Double outNum = (Double) tempout.get("storeNum");
				//循环库存结果，对比数量
				int j = 0;
				double store = 0;
				double amount = 0;
				//对库存结果进行递归直到出库数量为0为止
				//插入到出库信息表
				tempout.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.outStoreInfo"));
				tempout.put("workCard", in.get("workCard"));
				HelperDB.setCreate(HelperApp.getUserName(userMap),tempout);
				db.insert(tempout);
				//对比库存数量 从库存中减除
				outStr += saveRatioStore(storeList,j,outNum,store,outString,tempout,userMap,amount);
			}
		}
		//工作流
		IMap wfmap = new DataMap();
		String instanceId = (String)in.get("instanceId");
		String stepId = (String)in.get("stepId");
		String nodeId = (String)in.get("nodeId");
		FireNetHelper.wf_next(db,userMap, instanceId, stepId, nodeId, wfmap);
		//操作日志
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String remark="材料员："+userMap.get("name")+"在"+sdf.format(new Date())+"进行了加工件确认。";
		LogBean logb=new LogBean(userMap,(String)in.get("busId"), "com.MachiningMessage","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg",OperateHelper.getSaveMsg());
		result.put("method.url", in.get("url"));
		return result;
	}
	
	//5、材料领用计划
	//6、车间主任审批
	//7、厂领导审批
	//8、生产主管审批
	//9、经营主管审批
	//10、经理审批
	//11、总公司领导审批
	//12、上传器材库
	//13、器材库确认
	//14、领料出库
	//15、检验
	//16、调度确认
	//17、出厂
	
}
