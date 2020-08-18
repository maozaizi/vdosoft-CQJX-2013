package com.u2a.business;

import java.text.SimpleDateFormat;
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
import com.brick.util.Util;
import com.u2a.framework.commons.Constants;
import com.u2a.framework.commons.OperateHelper;
import com.u2a.framework.service.sys.logrecords.LogBean;
import com.u2a.framework.service.sys.logrecords.LogRecordsService;
import com.u2a.framework.util.FireNetHelper;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;

/**
 * 
 * 系统名称：长庆钻井机修公司 - 设备维修管理系统   
 * 类名称：BusElectricalService   
 * 类描述：   
 * 创建人：崔佳华 
 * 创建时间：Jul 31, 2013 9:00:36 AM
 */
@SuppressWarnings("unchecked")
public class BusRivetweldService extends Service {

	/**
	 * -===============================cuijiahua===============================================
	 */
	/**
	 * 铆焊列表信息
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @date Jul 12, 2013 9:00:56 AM
	 */
	public IMap getBusRivetweldList(IMap in) {
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
		in.put("tableNames","bus_rivetweld");
		//查询分页结果
		Page page = db.pageQuery(in,"BusInrepairInfoSql","","busID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}
	/**
	 * 派工单信息
	 * @Description 
	 * @param @param in
	 * @param @return  
	 * @return IMap<String,Object>   
	 * @date Jul 17, 2013 5:39:18 PM
	 */
	public IMap<String, Object> getbusWorkAndBusRivetweldDetails(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.BusRivetweld");
		selectMap.put("id", in.get("id"));
		//铆焊信息
		IMap busRivetweldMap = db.get(selectMap);
		//施工卡信息
		IMap busWorkCardMap =BeanFactory.getClassBean("com.BusWorkCard");
		busWorkCardMap.put("busId",busRivetweldMap.get("id"));
		busWorkCardMap.put("busName","com.BusRivetweld");
		//进修单基本信息
		IMap busInrepairInfoMap =BeanFactory.getClassBean("com.BusInrepairInfo");
		busInrepairInfoMap.put("id",busRivetweldMap.get("inrepairId"));
		busInrepairInfoMap =db.get(busInrepairInfoMap);
		result.put("busInrepairInfoMap",busInrepairInfoMap);
		IMap condition = new DataMap();
		//业务表
		condition.put("busIdOne",busRivetweldMap.get("id"));
		condition.put("busNameOne","com.BusRivetweld");
		//进修单
		condition.put("busIdTow",busInrepairInfoMap.get("id"));
		condition.put("busNameTow","com.BusInrepairInfo");
		//附件列表
		List<IMap> fileList = db.getList(condition, "getAllFileList", "com.AttachmentInfo");
		result.put("fileList", fileList);
		//操作记录
		List<IMap> logRecordsList = db.getList(condition, "getLogRecordsList", "com.logRecords");
		result.put("logRecordsList", logRecordsList);
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = busRivetweldMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", busRivetweldMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		result.put("groupName", baseOrganization.get("orgName"));
		//根据业务表Id的到施工卡列表
		List busWorkCardList = db.getList(busWorkCardMap, null);
		result.put("busRivetweldMap",busRivetweldMap );
		result.put("busWorkCardList", busWorkCardList);
		result.put("stepId", in.get("stepId"));
		result.put("instanceId",in.get("instanceId") );
		result.put("nodeId", in.get("nodeId"));
		return result;
	}

	/**
	 * 添加施工卡
	 * @param in
	 * @return
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
		//主修人
		busWorkCard.put("mainRepair",busWorkCardMap.get("mainRepair").toString());
		//主修人Id
		busWorkCard.put("mainId",busWorkCardMap.get("mainId").toString());
		//辅修人Id
		busWorkCard.put("assistId", busWorkCardMap.get("assistId").toString());
		//辅修人
		busWorkCard.put("assistRepair", busWorkCardMap.get("assistRepair").toString());
		//派工日期
		busWorkCard.put("appointDate", busWorkCardMap.get("appointDate"));
		//开工日期
		busWorkCard.put("startDate", busWorkCardMap.get("startDate"));
		//指定完成日期
		busWorkCard.put("orderEndDate", busWorkCardMap.get("orderEndDate"));
		//报修内容
		busWorkCard.put("repairContent",busWorkCardMap.get("repairContent") );
		//修改人
		busWorkCard.put("modifyUser",userMap.get("userName")+"/"+userMap.get("name"));
		//修改时间
		busWorkCard.put("modifyDate",date_str);
		db.update(busWorkCard);
		SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String remark="派工员："+userMap.get("name")+"在"+dftow.format(date)+"添加了施工卡信息。";
		LogBean logb=new LogBean(userMap,in.get("busId").toString(), "com.BusRivetweld","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		IMap selectMap = BeanFactory.getClassBean("com.BusRivetweld");
		selectMap.put("id", in.get("busId"));
		//进修单信息
		IMap busRivetweldMap = db.get(selectMap);
		busRivetweldMap.put("dispatchingRemark", in.get("dispatchingRemark"));
		//将开工时间添加到主表中
		busRivetweldMap.put("startDate", busWorkCardMap.get("startDate"));
		db.update(busRivetweldMap);
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
		FireNetHelper.wf_next(db, userMap, instanceId, stepId, nodeId,wfmap);
		result.put("method.url", in.get("url"));
		result.put("method.infoMsg", "提交成功！");
		return result;
	}
	/**
	 * 导向铆焊详情信息页面
	 * @Description 
	 * @param @param in
	 * @param @return  
	 * @return IMap<String,Object>
	 * logType:0-普通，1-审批
	 * @date Jul 17, 2013 5:39:18 PM
	 */
	public IMap<String, Object> toBusRivetweldDetailsPage(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.BusRivetweld");
		selectMap.put("id", in.get("id"));
		selectMap.put("isValid", 1);
		//业务表基本信息
		IMap busRivetweldMap = db.get(selectMap);
		if(busRivetweldMap==null){
			IMap selectMaps = BeanFactory.getClassBean("com.BusRivetweld");
			selectMaps.put("id", in.get("id"));
			busRivetweldMap = db.get(selectMaps);
		}
		
		//根据派工单得到申请过planId
		IMap con = new DataMap();
		con.put("busId", busRivetweldMap.get("id"));
		con.put("busName","com.BusRivetweld");
		List list = db.getList(con, "getBusPlanInfoList",null);
		result.put("busPlanInfoList",list);
		//派工单信息
		IMap busWorkCardMap =BeanFactory.getClassBean("com.BusWorkCard");
		busWorkCardMap.put("busId",busRivetweldMap.get("id"));
		busWorkCardMap.put("busName","com.BusRivetweld");
		//根据业务表Id的到施工卡列表
		List busWorkCardList = db.getList(busWorkCardMap, null);
		result.put("busWorkCardList", busWorkCardList);
		//进修单信息
		IMap busInrepairInfo =BeanFactory.getClassBean("com.BusInrepairInfo");
		busInrepairInfo.put("id", busRivetweldMap.get("inrepairId"));
		IMap busInrepairInfoMap = db.get(busInrepairInfo);
		result.put("busInrepairInfoMap", busInrepairInfoMap);
		IMap condition = new DataMap();
		//业务表
		condition.put("busIdOne",busRivetweldMap.get("id"));
		condition.put("busNameOne","com.BusRivetweld");
		//进修单
		condition.put("busIdTow",busRivetweldMap.get("inrepairId"));
		condition.put("busNameTow","com.BusInrepairInfo");
		//附件列表
		List<IMap> fileList = db.getList(condition, "getAllFileList", "com.AttachmentInfo");
		result.put("fileList", fileList);
		//操作记录
		List<IMap> logRecordsList = db.getList(condition, "getLogRecordsList", "com.logRecords");
		
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = busRivetweldMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", busRivetweldMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		//试车记录信息
		IMap busTestRecordMap = BeanFactory.getClassBean("com.BusTestRecord");
		busTestRecordMap.put("busId",busRivetweldMap.get("id"));
		busTestRecordMap.put("busName","com.BusRivetweld");
		OrderBean orderBean = new OrderBean();
		orderBean.put("createDate",orderBean.ASC);
		List busTestRecordList = db.getList(busTestRecordMap,orderBean);
		
		result.put("busTestRecordList",busTestRecordList);
		result.put("logRecordsList", logRecordsList);
		result.put("fileList", fileList);
		result.put("busRivetweldMap",busRivetweldMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		result.put("groupName", baseOrganization.get("orgName"));
		return result;
	}
	/**
	 * 进修单信息
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @date Jul 16, 2013 5:39:18 PM
	 */
	public IMap<String, Object> getBusRivetweldDetails(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.BusRivetweld");
		selectMap.put("id", in.get("id"));
		//铆焊业务表信息
		IMap busRivetweldMap = db.get(selectMap);
		result.put("busRivetweldMap",busRivetweldMap );
		//进修单基本信息
		IMap busInrepairInfoMap =BeanFactory.getClassBean("com.BusInrepairInfo");
		busInrepairInfoMap.put("id",busRivetweldMap.get("inrepairId"));
		busInrepairInfoMap =db.get(busInrepairInfoMap);
		result.put("busInrepairInfoMap",busInrepairInfoMap);
		//设备信息
//		IMap selectEqp =BeanFactory.getClassBean("com.EquipmentInfo");
//		selectEqp.put("id", busMechanicMap.get("equipmentId"));
//		IMap equipmentMap = db.get(selectEqp);
		IMap condition = new DataMap();
		//业务表
		condition.put("busIdOne",busRivetweldMap.get("id"));
		condition.put("busNameOne","com.BusRivetweld");
		condition.put("busIdTow",busInrepairInfoMap.get("id"));
		condition.put("busNameTow","com.BusInrepairInfo");
		//附件列表
		List<IMap> fileList = db.getList(condition, "getAllFileList", "com.AttachmentInfo");
		result.put("fileList", fileList);
		//操作记录
		List<IMap> logRecordsList = db.getList(condition, "getLogRecordsList", "com.logRecords");
		result.put("logRecordsList",logRecordsList);
		//工作流信息
		result.put("stepId", in.get("stepId"));
		result.put("nodeId", in.get("nodeId"));
		result.put("instanceId", in.get("instanceId"));
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = busRivetweldMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", busRivetweldMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		result.put("groupName", baseOrganization.get("orgName"));
		return result;
	}
	/**
	 * 跳转材料申请页面
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @date Jul 16, 2013 5:39:18 PM
	 */
	public IMap<String, Object> toMaterialPlan(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.BusRivetweld");
		selectMap.put("id", in.get("id"));
		//进修单信息
		IMap busRivetweldMap = db.get(selectMap);
		result.put("busRivetweldMap",busRivetweldMap);
		//工作流信息
		result.put("stepId", in.get("stepId"));
		result.put("nodeId", in.get("nodeId"));
		result.put("instanceId", in.get("instanceId"));
		//当前审批计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
		busPlanInfoMap.put("busId",busRivetweldMap.get("id"));
		busPlanInfoMap.put("busName","com.BusRivetweld");
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
		//业务表
		IMap condition = new DataMap();
		condition.put("busIdOne",busRivetweldMap.get("id"));
		condition.put("busNameOne","com.BusRivetweld");
		//进修单
		condition.put("busIdTow",busRivetweldMap.get("inrepairId"));
		condition.put("busNameTow","com.BusInrepairInfo");
		//操作记录
		List<IMap> logRecordsList = db.getList(condition, "getLogRecordsList", "com.logRecords");
		result.put("logRecordsList", logRecordsList);
		//进修单基本信息
		IMap busInrepairInfoMap =BeanFactory.getClassBean("com.BusInrepairInfo");
		busInrepairInfoMap.put("id",busRivetweldMap.get("inrepairId"));
		busInrepairInfoMap =db.get(busInrepairInfoMap);
		result.put("busInrepairInfoMap",busInrepairInfoMap);
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = busRivetweldMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", busRivetweldMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		result.put("groupName", baseOrganization.get("orgName"));
		return result;
	}
	/**
	 * 添加物料计划
	 */
	public IMap<String, Object> modifyBusMaterialPlan(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//获取材料计划列表
		List busMaterialPlanList = (List) in.get("busMaterialPlan");
		//当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		//业务ID
		String busId = (String)in.get("busId");
		String planRemark=(String)in.get("planRemark");
		IMap busRivetweldMap =BeanFactory.getClassBean("com.BusRivetweld");
		busRivetweldMap.put("id", busId);
		busRivetweldMap = db.get(busRivetweldMap);
		//物流计划备注信息
		busRivetweldMap.put("planRemark", planRemark);
		db.update(busRivetweldMap);
		// 发起工作流
		IMap wfmap = new DataMap();
		IMap condition = new DataMap();
		//获取基础组织编码
		String orgCode = (String) userMap.get("orgId");
		condition.put("orgId", orgCode);
		//当前审批计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
		busPlanInfoMap.put("busId",in.get("busId"));
		busPlanInfoMap.put("busName","com.BusRivetweld");
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
			//关联机修流程
			newBusPlanInfoMap.put("busId", busId);
			newBusPlanInfoMap.put("busName", "com.BusRivetweld");
			newBusPlanInfoMap.put("planCode", (String)in.get("planCode"));
			//计划ID
			String busPlanInfoId =HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(), "com.BusPlanInfo");
			newBusPlanInfoMap.put("id", busPlanInfoId);
			//工卡号
			newBusPlanInfoMap.put("workCard",busRivetweldMap.get("workCard"));
			//orgId
			newBusPlanInfoMap.put("orgId", userMap.get("orgId"));
			//物料计划大件材料申请原因
			newBusPlanInfoMap.put("bigRemark", (String)in.get("bigRemark"));
			//为创建对象赋值
//			boolean flag = true;
			HelperDB.setCreate4isValid(HelperApp.getUserName(userMap), newBusPlanInfoMap);
			for (int i = 0; i < busMaterialPlanList.size(); i++) {
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
			SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			String remark="材料员"+userMap.get("name")+"在"+dftow.format(new Date())+"添加了材料及加工件领用申请信息。";
			LogBean logb=new LogBean(userMap,busId, "com.BusRivetweld","",remark, "0");
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
	 * 材料申请审批信息
	 * @Description 
	 * @param @param in
	 * @param @return  
	 * @return IMap<String,Object>   
	 * @date Jul 17, 2013 5:39:18 PM
	 */
	public IMap<String, Object> getBusRivetweldAndMaterialPlanDetails(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.BusRivetweld");
		selectMap.put("id", in.get("id"));
		//业务表基本信息
		IMap busRivetweldMap = db.get(selectMap);
		//当前审批计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
		busPlanInfoMap.put("busId",busRivetweldMap.get("id"));
		busPlanInfoMap.put("busName","com.BusRivetweld");
		busPlanInfoMap.put("isValid",1);
		busPlanInfoMap = db.get(busPlanInfoMap);
		result.put("busPlanInfoMap", busPlanInfoMap);
		//材料申请信息
		IMap BusMaterialPlanMap =BeanFactory.getClassBean("com.BusMaterialPlan");
		BusMaterialPlanMap.put("planId",busPlanInfoMap.get("id"));
	    //type 1：领用 2：加工 3维修
		//非主任审批，只显示领用件
		if(!"zr".equals(in.get("auditPerson"))){
			BusMaterialPlanMap.put("type","1,4");
		}
		//if("gs".equals(in.get("auditPerson"))){
			//如果是公司机关审批 则材料列表只显示5000以上的材料
		//	BusMaterialPlanMap.put("flag","1");
		//}else 
		if("zgs".equals(in.get("auditPerson"))){
			//如果是总公司，则只显示10000以上的材料
			BusMaterialPlanMap.put("flag","2");
		}
		
		//=================================2015年6月修改=====================================/
		//主任审批，配置加工件和修理
		if("zr".equals(in.get("auditPerson"))){
			IMap jiagongPlanMap =BeanFactory.getClassBean("com.BusMaterialPlan");
			jiagongPlanMap.put("planId",busPlanInfoMap.get("id"));
			jiagongPlanMap.put("type","2");
			List jiagongPlanList = db.getList(jiagongPlanMap,"getBusMaterialPlanList", "com.BusMaterialPlan");
			
			IMap weixiuPlanMap =BeanFactory.getClassBean("com.BusMaterialPlan");
			weixiuPlanMap.put("planId",busPlanInfoMap.get("id"));
			weixiuPlanMap.put("type","3");
			List weixiuPlanList = db.getList(weixiuPlanMap,"getBusMaterialPlanList", "com.BusMaterialPlan");
			
			result.put("jiagongPlanList", jiagongPlanList);
			result.put("weixiuPlanList", weixiuPlanList);
			
			//主任审批页面的车间列表，获取本厂的所有车间
			//获取当前登录用户信息
			IMap userMap = (IMap) in.get("userMap");
			//获取当前登录用户所在的组织ID
			String orgCode = (String) userMap.get("orgId");
			//查询当前登录用户所在组织所在厂区
			//查询厂区下所有的车间，并封装成LIST传到页面供选择
			IMap condition = new DataMap();
			String codes[] = orgCode.replace(".",",").split(",");
			condition.put("orgCode", codes[0]+"."+codes[1]+"."+codes[2]);
			condition.put("deptcode", orgCode);
			//获取车间列表 同时获取车间的当前工卡号
			List<IMap> deptList = db.getList(condition, "getselforgdeptList",null);
			result.put("deptList", deptList);
		}
		
		//=================================2015年6月修改=====================================/
		
		//获取当前审批的材料申请列表
		List busMaterialPlanList = db.getList(BusMaterialPlanMap,"getBusMaterialPlanList", "com.BusMaterialPlan");
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
		logRecordsMap.put("busId",busRivetweldMap.get("id"));
		logRecordsMap.put("busName","com.BusRivetweld");
		logRecordsMap.put("logType","1");
		OrderBean orderBean = new OrderBean();
		orderBean.put("operationDate",OrderBean.DESC);
		List logRecordsList = db.getList(logRecordsMap, orderBean);
		//派工单信息
		IMap busWorkCardMap =BeanFactory.getClassBean("com.BusWorkCard");
		busWorkCardMap.put("busId",busRivetweldMap.get("id"));
		busWorkCardMap.put("busName","com.BusRivetweld");
		//根据业务表Id的到施工卡列表
		List busWorkCardList = db.getList(busWorkCardMap, null);
		//附件列表
		IMap attachmentMap =BeanFactory.getClassBean("com.AttachmentInfo");
		attachmentMap.put("busId",busRivetweldMap.get("id"));
		attachmentMap.put("busName","com.BusRivetweld");
		attachmentMap.put("busType","2");	
		attachmentMap.put("isValid",1);	
		orderBean = new OrderBean();
		orderBean.put("createDate",OrderBean.DESC);
		List fileList = db.getList(attachmentMap, orderBean);
		//进修单信息
		IMap busInrepairInfoMap =BeanFactory.getClassBean("com.BusInrepairInfo");
		busInrepairInfoMap.put("id",busRivetweldMap.get("inrepairId"));
		busInrepairInfoMap =db.get(busInrepairInfoMap);
		result.put("busInrepairInfoMap",busInrepairInfoMap);
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = busRivetweldMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", busRivetweldMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		result.put("fileList", fileList);
		result.put("busWorkCardList", busWorkCardList);
		result.put("logRecordsList", logRecordsList);
		result.put("busRivetweldMap",busRivetweldMap );
		result.put("sk",sk);
		result.put("cq",cq);
		result.put("busMaterialPlanList", busMaterialPlanList);
		result.put("stepId", in.get("stepId"));
		result.put("instanceId",in.get("instanceId") );
		result.put("nodeId", in.get("nodeId"));
		result.put("busInrepairInfoMap",busInrepairInfoMap);
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
			IMap bussMap = (IMap) in.get("busRivetweld");
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
			IMap busRivetweldMap = BeanFactory.getClassBean("com.BusRivetweld");
			busRivetweldMap.put("id", bussMap.get("id"));
			busRivetweldMap=db.get(busRivetweldMap);
			//当前审批计划
			IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
			busPlanInfoMap.put("busId",busRivetweldMap.get("id"));
			busPlanInfoMap.put("busName","com.BusRivetweld");
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
				attachmentInfoMap.put("busId", busRivetweldMap.get("id"));
				attachmentInfoMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.AttachmentInfo"));
				attachmentInfoMap.put("isValid",Constants.ISVALID);
				attachmentInfoMap.put("busName","com.BusRivetweld");
				//状态2为审批上传文件
				attachmentInfoMap.put("busType","2");	
				//为创建数据赋值
				HelperDB.setCreateInfo(HelperApp.getUserName(userMap),attachmentInfoMap);
				db.insert(attachmentInfoMap);
				docId=attachmentInfoMap.get("id").toString();
				}
				str=bussMap.get("jlResult").equals("0")?"不同意":"同意";
				remark="经理："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+bussMap.get("jlRemark")+"；结果为："+str+"。";
				if("0".equals(bussMap.get("jlResult"))){
					orgId = (String) busRivetweldMap.get("orgId");
					postCode="车间材料员";
					spresult = "0";
				}else if("1".equals(bussMap.get("jlResult"))&&"1".equals(sk)){
					orgId =orgCodes[0];
					postCode="总公司领导";
					spresult = "1";
				}else if("1".equals(bussMap.get("jlResult"))&&!"1".equals(sk)){
					orgId = (String) busRivetweldMap.get("orgId");
					String[] orgids = orgId.replace(".", ",").split(",");
					orgId =orgids[0]+"."+orgids[1]+"."+orgids[2];
					//厂材料员确认
					postCode="厂材料员";
					spresult = "2";
				}
			}else if(in.get("type").toString().equals("ccl")){//新增厂区领导审批前-厂材料员审批
				busPlanInfoMap.put("auditState",bussMap.get("cclResult"));
				str=bussMap.get("cclResult").equals("0")?"不同意":"同意";
				remark="厂材料员："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+bussMap.get("cclRemark")+"；结果为："+str+"。";		
				//总公司
				if("0".equals(bussMap.get("cclResult"))){
					orgId = (String) busRivetweldMap.get("orgId");
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
				remark="厂区领导："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+bussMap.get("cqRemark")+"；结果为："+str+"。";		
				//总公司
				if("0".equals(bussMap.get("cqResult"))){
					orgId = (String) busRivetweldMap.get("orgId");
					postCode="车间材料员";
					spresult = "0";
				}else{
					//if("1".equals(cq)){
						orgId =orgCodes[0];
						postCode="生产主管";
						spresult = "1";
//					}else{
//						orgId =orgCode;
//						postCode="厂材料员";
//						spresult = "2";
//					}
					/****************************************************2015年6月修改********************************************************
					String conOrgId =orgCodes[0]+"."+orgCodes[1]+"."+orgCodes[2];
					IMap condition = new DataMap();
					condition.put("orgCode", conOrgId);
					List<IMap> deptList = db.getList(condition, "getMachiningList",null);
					String deptId = deptList.get(0).get("org_code").toString();
					//审批通过，判断材料计划，是否需要发起维修、加工的机加流程
					IMap busMaterialPlanMap = BeanFactory.getClassBean("com.BusMaterialPlan");
					busMaterialPlanMap.put("planId", busPlanInfoMap.get("id"));
					List materialPlanList = db.getList(busMaterialPlanMap, "getMachiningPlanList","com.BusMaterialPlan");
					//进修单信息
					IMap busInrepairInfoMap =BeanFactory.getClassBean("com.BusInrepairInfo");
					//业务信息
					IMap selectMap = BeanFactory.getClassBean("com.BusRivetweld");
					selectMap.put("id", busRivetweldMap.get("id"));
					selectMap = db.get(selectMap);
					busInrepairInfoMap.put("id", selectMap.get("inrepairId"));
					busInrepairInfoMap = db.get(busInrepairInfoMap);
					//循环发起流程
					for(int i=0;i<materialPlanList.size();i++){
						//需要发起机加流程的计划
						IMap machiningPlan = (IMap)materialPlanList.get(i); 
						//机加登记信息单
						IMap machiningMessage = BeanFactory.getClassBean("com.MachiningMessage");
						String machiningid = HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(), 
						"com.MachiningMessage");
						machiningMessage.put("id", machiningid);
						//名称
						machiningMessage.put("equipmentName", machiningPlan.get("materialDetail"));
						//审批通过的 机加数量
						machiningMessage.put("planNum", machiningPlan.get("ratifyNum"));
						//主卡号
						String workCard = (String)busPlanInfoMap.get("workCard");
						if(workCard!=null&&workCard!=""){
							machiningMessage.put("mainCard",workCard.substring(workCard.indexOf("/")+1,workCard.length()));
						}
						//送修单位
						//machiningMessage.put("userName", busInrepairInfoMap.get("deptFrom"));
						//项目部
						machiningMessage.put("proDept","铆焊车间");
						//orgId
						machiningMessage.put("orgId", busInrepairInfoMap.get("orgId"));
						machiningMessage.put("repairStep",2);
						machiningMessage.put("isValid",1);
						HelperDB.setCreate4isValid((String)userMap.get("userName"),machiningMessage);
						db.insert(machiningMessage);
						
						//插入机加基本信息表
						//获取当前登录用户所在的组织ID
						//查询当前登录用户所在组织所在厂区
						//查询厂区下的机加车间
						IMap workCardMap = BeanFactory.getClassBean("com.BusWorkCard");
						workCardMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
								.getPackageName(), "com.BusWorkCard"));
						workCardMap.put("deptName", "机加车间");
						workCardMap.put("orgId", deptId);
						//业务Id
						String busId = "";
						//业务名称
						String busName ="";
						//流程描述
						String flowDesc ="";
						//流程编号 mechanic
						String flowCode ="";
						//查看流程URL
						String flowUrl="";
						//分别判断五个车间
						IMap busMap = new DataMap();
						busMap = BeanFactory.getClassBean("com.MechTreatment");
						busId =HelperApp.getAutoIncrementPKID(HelperApp
								.getPackageName(), "com.MechTreatment");
						busName ="com.MechTreatment";
						flowDesc = "机加";
						flowCode = "mechtreatment";
						flowUrl = "/api/mechtreatment/toMechTreatmentDetailsPage";
						//工卡信息
						workCardMap.put("busId", busId);
						workCardMap.put("busName",busName);
						HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),workCardMap);
						db.insert(workCardMap);
						//插入业务信息
						busMap.put("id", busId);
						busMap.put("inrepairId",machiningid);
						busMap.put("orgId", deptId);
						busMap.put("isValid",1);
						busMap.put("repairState",1);
						busMap.put("mainCard",workCard.substring(workCard.indexOf("/")+1,workCard.length()));
						//收益类型0：内部创收  1：外部创收
						busMap.put("revenueType",0);
						HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),busMap);
						db.insert(busMap);
						// 发起工作流
						IMap wfmap = new DataMap();
						//wfmap.put("result", flag);// 判断是否受理
						//------------查询当前组织下的厂检验人员，没有则查询所有的车间检验人员
					    condition = new DataMap();
						//获取基础组织编码
						condition.put("orgId", deptId);
						condition.put("postCode", "车间派工");
						List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
						if(userList.isEmpty()){
							throw new BusinessException("请联系管理员指派机加车间的车间派工人员！");
						}
						String cbids = "";
						for(int j=0;j<userList.size();j++){
							IMap usersMap = BeanFactory.getClassBean("com.UserInfo");
							usersMap = userList.get(j);
							cbids = cbids+ (String)usersMap.get("userId")+",";
						}
						String[] nextUserIds = cbids.split(",");
						IMap wf_users = new DataMap();
						wf_users.put("u", nextUserIds);
						JSONObject str_wf_user = JSONObject.fromObject(wf_users);
						// 发起工作流
						wfmap.put("assignedbyuser", str_wf_user.toString());
						
						// 工作流信息
						FireNetHelper.wf_start(db,userMap,flowCode,busId,busName,
								(String)busMap.get("mainCard")+machiningPlan.get("materialDetail") + "加工件的"+flowDesc,flowUrl,0,wfmap);
						SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
						remark="厂区领导："+userMap.get("name")+"在"+dftow.format(new Date())+"调派此业务的加工件到机加车间。";
						LogBean logb=new LogBean(userMap,busId,busName,"",remark, "0");
						LogRecordsService.saveOperationLog(logb,db);
						
					}
					***********************************************************************************************************/
				}
			}else if(in.get("type").toString().equals("zr")){
				busPlanInfoMap.put("auditState",bussMap.get("zrResult"));
				str=bussMap.get("zrResult").equals("0")?"不同意":"同意";
				remark="车间主任："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+bussMap.get("zrRemark")+"；结果为："+str+"。";	
				orgId = (String) busRivetweldMap.get("orgId");
				postCode="车间材料员";
				if("0".equals(bussMap.get("zrResult"))){					
					spresult = "0";
				}else{
					orgId = (String) busRivetweldMap.get("orgId");
					String[] orgids = orgId.replace(".", ",").split(",");
					orgId =orgids[0]+"."+orgids[1]+"."+orgids[2];
					postCode="厂材料员";
					spresult = "1";
					
					/***********************************************2015年6月15日***********************************************************/
					List jqbusMaterialPlan = (List) in.get("jqbusMaterialPlan");
					if(!jqbusMaterialPlan.isEmpty()){
						for(int jq=0;jq<jqbusMaterialPlan.size();jq++){
							IMap busMaterialPlan = (IMap)jqbusMaterialPlan.get(jq);
							db.update(busMaterialPlan);
						}
					}
					
					List wxbusMaterialPlan = (List) in.get("wxbusMaterialPlan");
					if(!wxbusMaterialPlan.isEmpty()){
						for(int wx=0;wx<wxbusMaterialPlan.size();wx++){
							IMap busMaterialPlanwx = (IMap)wxbusMaterialPlan.get(wx);
							db.update(busMaterialPlanwx);
						}
					}
					
					addbusworkflow(busPlanInfoMap,busRivetweldMap,userMap);
				}
			}else if(in.get("type").toString().equals("sc")){
				busPlanInfoMap.put("auditState",bussMap.get("scResult"));
				str=bussMap.get("scResult").equals("0")?"不同意":"同意";
				remark="生产主管："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+bussMap.get("scRemark")+"；结果为："+str+"。";		
				//总公司
				if("0".equals(bussMap.get("scResult"))){
					orgId = (String) busRivetweldMap.get("orgId");
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
				remark="经营主管："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+bussMap.get("jyRemark")+"；结果为："+str+"。";		
				//总公司
				if("0".equals(bussMap.get("jyResult"))){
					orgId = (String) busRivetweldMap.get("orgId");
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
				remark="总公司领导："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+busRivetweldMap.get("zgsRemark")+"；结果为："+str+"。";	
				if("0".equals(bussMap.get("zgsResult"))){
					//删除不通过的附件信息
					IMap attachmentMap =BeanFactory.getClassBean("com.AttachmentInfo");
					attachmentMap.put("busId",busRivetweldMap.get("id"));
					attachmentMap.put("busName","com.BusRivetweld");
					//状态2为审批上传文件
					attachmentMap.put("busType","2");
					attachmentMap.put("isValid","1");
					List fileList = db.getList(attachmentMap, null);
					for(int a=0;a<fileList.size();a++){
					IMap  attachmentInfo= (IMap)fileList.get(a);
					attachmentInfo.put("isValid",Constants.ISNOTVALID);
					db.update(attachmentInfo);
					};
					orgId = (String) busRivetweldMap.get("orgId");
					postCode="车间材料员";
					spresult = "0";
				}else{
					orgId = (String) busRivetweldMap.get("orgId");
					//厂材料员
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
						attachmentInfoMap.put("busId",busRivetweldMap.get("id"));
						attachmentInfoMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
								.getPackageName(), "com.AttachmentInfo"));
						attachmentInfoMap.put("isValid",Constants.ISVALID);
						attachmentInfoMap.put("busName","com.BusRivetweld");
						attachmentInfoMap.put("busType",1);
						//为创建数据赋值
						HelperDB.setCreateInfo(HelperApp.getUserName(userMap),attachmentInfoMap);
						db.insert(attachmentInfoMap);
					}
				}
			}
			//审核物料计划，更改物料计划状态
			db.update(busPlanInfoMap);
			//获取材料计划列表
			List busMaterialPlanList = (List) in.get("busMaterialPlan");
			for (int i = 0; i < busMaterialPlanList.size(); i++) {
				IMap busMaterialPlan = (IMap)busMaterialPlanList.get(i);
				db.update(busMaterialPlan);
			}
			LogBean logb=null;
			if(!docId.equals("")){
			 logb=new LogBean(userMap,busRivetweldMap.get("Id").toString(), "com.BusRivetweld","",remark,"1",docId);
			}else{
			 logb=new LogBean(userMap,busRivetweldMap.get("Id").toString(), "com.BusRivetweld","",remark,"1");
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
		 * 材料计划LIST 
		 * @param @param in 
		 * @param @return    
		 * @return IMap   
		 * @date Jul 19, 2013 9:15:54 AM
		 */
		public IMap getBusMaterialPlanList(IMap in){
			IMap result = new DataMap();
			//当前审批计划
			IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
			busPlanInfoMap.put("busId",in.get("id"));
			busPlanInfoMap.put("busName","com.BusRivetweld");
			//auditState - 0  审核不通过 1 审核通过 2未审
			busPlanInfoMap.put("auditState","1");
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
			IMap selectMap = BeanFactory.getClassBean("com.BusRivetweld");
			selectMap.put("id", in.get("id"));
			//进修单信息
			selectMap = db.get(selectMap);
			result.put("busRivetweldMap",selectMap);
			//进修单基本信息
			IMap busInrepairInfoMap =BeanFactory.getClassBean("com.BusInrepairInfo");
			busInrepairInfoMap.put("id",selectMap.get("inrepairId"));
			busInrepairInfoMap =db.get(busInrepairInfoMap);
			result.put("busInrepairInfoMap",busInrepairInfoMap);
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
		 * @date Jul 19, 2013 9:38:03 AM
		 */
		public IMap modifyBusRivetweldReport(IMap in){
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
			
			String [] orgCodes = in.get("orgCode").toString().replace(".", ",").split(",");
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
			SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			String remark="材料员："+userMap.get("name")+"在"+dftow.format(new Date())+"上报器材库。";
			LogBean logb=new LogBean(userMap,(String)in.get("id"), "com.BusRivetweld","",remark, "0");
			LogRecordsService.saveOperationLog(logb,db);
			result.put("method.infoMsg", "确认成功！");
			result.put("method.url", in.get("url"));
			return result;
		}
		/**
		 * 厂材料员确认
		 * @param @param in
		 * @param @return    
		 * @return IMap   
		 * @date Jul 19, 2013 9:38:03 AM
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
			IMap busRivetweldMap = BeanFactory.getClassBean("com.BusRivetweld");
			busRivetweldMap.put("id", in.get("id"));
			busRivetweldMap = db.get(busRivetweldMap);
			String orgId= (String) busRivetweldMap.get("orgId");
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
			SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			String remark="厂材料员："+userMap.get("name")+"在"+dftow.format(new Date())+"器材库确认。";
			LogBean logb=new LogBean(userMap,(String)in.get("id"), "com.BusRivetweld","",remark, "0");
			LogRecordsService.saveOperationLog(logb,db);
			result.put("method.infoMsg", "确认成功！");
			result.put("method.url", in.get("url"));
			return result;
		}
		/**
		 * 跳转到出库页面
		 * @Description 
		 * @param @param in
		 * @param @return    
		 * @return IMap<String,Object>   
		 * @date Jul 17, 2013 14:07:44 AM
		 */
		public IMap<String, Object> toOutStore(IMap<String, Object> in) {
			IMap<String, Object> result = new DataMap<String, Object>();// 输出map
			IMap selectMap = BeanFactory.getClassBean("com.BusRivetweld");
			selectMap.put("id", in.get("id"));
			//获取基本信息
			IMap busRivetweldMap = db.get(selectMap);
			//当前审批计划
			IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
			busPlanInfoMap.put("busId",in.get("id"));
			busPlanInfoMap.put("busName","com.BusRivetweld");
			busPlanInfoMap.put("isValid",1);
			busPlanInfoMap = db.get(busPlanInfoMap);
			if(busPlanInfoMap!=null){
				
				//材料申请信息
				IMap busMaterialPlanMap =BeanFactory.getClassBean("com.BusMaterialPlan");
				busMaterialPlanMap.put("planId",busPlanInfoMap.get("id"));
			    //type 1：领用 2：加工 3维修
				busMaterialPlanMap.put("type","1,4");
				//获取领用材料计划列表
				List materialList = db.getList(busMaterialPlanMap,"getBusMaterialPlanList", "com.BusMaterialPlan");
					//db.getList(busMaterialPlanMap, null);
				//获取加工材料计划列表
//				busMaterialPlanMap.put("type","2");
//				List materialList2 = db.getList(busMaterialPlanMap, null);
//				//获取维修材料计划列表
//				busMaterialPlanMap.put("type","3");
//				List materialList3 = db.getList(busMaterialPlanMap, null);
				result.put("materialList", materialList);
//				result.put("materialList2", materialList2);
//				result.put("materialList3", materialList3);
			}
			//进修单信息
			IMap busInrepairInfoMap =BeanFactory.getClassBean("com.BusInrepairInfo");
			busInrepairInfoMap.put("id",busRivetweldMap.get("inrepairId"));
			busInrepairInfoMap =db.get(busInrepairInfoMap);
			result.put("busInrepairInfoMap",busInrepairInfoMap);
			//厂区车间信息
			IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
			String [] orgIds = busRivetweldMap.get("orgId").toString().replace(".",",").split(",");
			//厂区
			baseOrganizationMap.put("isValid",1);
			baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
			baseOrganizationMap = db.get(baseOrganizationMap);
			result.put("companyName", baseOrganizationMap.get("orgName"));
			//车区
			//车区
			IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
			baseOrganization.put("orgCode", busRivetweldMap.get("orgId"));
			baseOrganization.put("isValid",1);
			baseOrganization = db.get(baseOrganization);
			result.put("groupName", baseOrganization.get("orgName"));
			//返回值
			// 工作流信息
			String instanceId = (String) in.get("instanceId");
			String nodeId = (String) in.get("nodeId");
			String stepId = (String) in.get("stepId");
			result.put("busRivetweldMap",busRivetweldMap );
			result.put("instanceId", instanceId);
			result.put("nodeId", nodeId);
			result.put("stepId", stepId);
			return result;
		}
		/**
		 * 
		 * @Description 保存入库结果
		 * @param @param in
		 * @param @return    
		 * @return IMap<String,Object>   
		 * @date Jul 18, 2013 5:54:55 PM
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
			
//			IMap selectMap =BeanFactory.getClassBean("com.BusWorkCard");
//			selectMap.put("busId", busId);
//			selectMap.put("busName", busName);
//			List<IMap> cardlist = db.getList(selectMap, null);
//			String cbids = "";
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
//					
//				}
//			}
			
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
					double outNum = Double.valueOf((String) tempout.get("storeNum"));
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
				busPlanInfoMap.put("busName","com.BusRivetweld");
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
			SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			String remark="材料员："+userMap.get("name")+"在"+dftow.format(new Date())+"将物料出库。";
			LogBean logb=new LogBean(userMap,busId, "com.BusRivetweld","",remark, "0");
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
		 * @date Jul 24, 2013 9:32:27 AM
		 */
		private String saveRatioStore(List<IMap> storeList,int j,double out,double store,String outString,IMap outStore,IMap userMap,double amount) {
			IMap storeMap =BeanFactory.getClassBean("com.StoreDetail");
			IMap outstoreMap =BeanFactory.getClassBean("com.outStoreDetail");
			//如果J小于storeList的长度 则进行循环
			if(j<storeList.size()){
				storeMap = storeList.get(j);
				double jstore = (Double)storeMap.get("num");
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
					outstoreMap.put("outType", storeMap.get("materialType"));
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
					outstoreMap.put("outType", storeMap.get("materialType"));
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
					outstoreMap.put("outType", storeMap.get("materialType"));
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
			IMap selectMap = BeanFactory.getClassBean("com.BusRivetweld");
			selectMap.put("id", in.get("id"));
			//获取基本信息
			IMap busRivetweldMap = db.get(selectMap);
			//所有出库后的审批计划
			IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
			busPlanInfoMap.put("busId",in.get("id"));
			busPlanInfoMap.put("busName","com.BusRivetweld");
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
			//进修单信息
			IMap busInrepairInfoMap =BeanFactory.getClassBean("com.BusInrepairInfo");
			busInrepairInfoMap.put("id",busRivetweldMap.get("inrepairId"));
			busInrepairInfoMap =db.get(busInrepairInfoMap);
			result.put("busInrepairInfoMap",busInrepairInfoMap);
			
			//厂区车间信息
			IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
			String [] orgIds = busRivetweldMap.get("orgId").toString().replace(".",",").split(",");
			//厂区
			baseOrganizationMap.put("isValid",1);
			baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
			baseOrganizationMap = db.get(baseOrganizationMap);
			result.put("companyName", baseOrganizationMap.get("orgName"));
			//车区
			IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
			baseOrganization.put("orgCode", busRivetweldMap.get("orgId"));
			baseOrganization.put("isValid",1);
			baseOrganization = db.get(baseOrganization);
			result.put("groupName", baseOrganization.get("orgName"));
			// 工作流信息
			String instanceId = (String) in.get("instanceId");
			String nodeId = (String) in.get("nodeId");
			String stepId = (String) in.get("stepId");
			result.put("busRivetweldMap",busRivetweldMap );
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
//				IMap selectMap =BeanFactory.getClassBean("com.BusWorkCard");
//				selectMap.put("busId", busId);
//				selectMap.put("busName", busName);
//				List<IMap> cardlist = db.getList(selectMap, null);
//				if(!cardlist.isEmpty()){
//					for(int i=0;i<cardlist.size();i++){
//						IMap cardMap =BeanFactory.getClassBean("com.BusWorkCard");
//						cardMap = cardlist.get(i);
//						String[] mainRepair = ((String)cardMap.get("mainId")).split(",");
//						String[] assistRepair = ((String)cardMap.get("assistId")).split(",");
//						//拼接主修人
//						for(int j=0;j<mainRepair.length;j++){
//							cbids = cbids+ mainRepair[j]+",";
//						}
//						//拼接辅修人
//						for(int k=0;k<assistRepair.length;k++){
//							cbids = cbids+ assistRepair[k]+",";
//						}
//					}
//				}
				condition.put("orgId",orgCode);
				condition.put("postCode", "车间检验员");
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
		 * 试车记录
		 * @Description 
		 * @param @param in
		 * @param @return    
		 * @return IMap<String,Object>   
		 * @date Jul 18, 2013 2:22:42 PM
		 */
		public IMap<String, Object> addBusTestRecord(IMap<String, Object> in) {
			IMap<String, Object> result = new DataMap<String, Object>();// 输出map
			IMap busTestRecord = (IMap) in.get("busTestRecord");
			IMap userMap = (IMap) in.get("userMap");
			String busId = (String) in.get("busId");
			//获取试车记录主键ID
			String busTestRecordId= HelperApp.getAutoIncrementPKID(HelperApp
					.getPackageName(), "com.BusTestRecord");
			busTestRecord.put("id",busTestRecordId);
			//关联 机修流程
			busTestRecord.put("busId", busId);
			busTestRecord.put("busName","com.BusRivetweld");
			//为创建数据赋值
			HelperDB.setCreateInfo(HelperApp.getUserName(userMap),busTestRecord);
			db.insert(busTestRecord);
			SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			String remark="检验员："+userMap.get("name")+"在"+dftow.format(new Date())+"添加了试车记录信息。";
			LogBean logb=new LogBean(userMap,busId, "com.BusRivetweld","",remark, "0");
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
		 * @date Jul 18, 2013 3:50:53 PM
		 */
		public IMap<String, Object> getBusTestRecordDetails(IMap<String, Object> in) {
			IMap<String, Object> result = new DataMap<String, Object>();// 输出map
			IMap selectMap = BeanFactory.getClassBean("com.BusRivetweld");
			selectMap.put("id", in.get("id"));
			//进修单信息
			selectMap = db.get(selectMap);
			//进修单基本信息
			IMap busInrepairInfoMap =BeanFactory.getClassBean("com.BusInrepairInfo");
			busInrepairInfoMap.put("id",selectMap.get("inrepairId"));
			busInrepairInfoMap =db.get(busInrepairInfoMap);
			result.put("busInrepairInfoMap",busInrepairInfoMap);
			IMap condition = new DataMap();
			//业务表
			condition.put("busIdOne",selectMap.get("id"));
			condition.put("busNameOne","com.BusRivetweld");
			//进修单
			condition.put("busIdTow",busInrepairInfoMap.get("id"));
			condition.put("busNameTow","com.BusInrepairInfo");
			//附件列表
			IMap attachmentMap =BeanFactory.getClassBean("com.AttachmentInfo");
			attachmentMap.put("busId",busInrepairInfoMap.get("id"));
			attachmentMap.put("busName","com.BusInrepairInfo");
			attachmentMap.put("busType","1");	
			attachmentMap.put("isValid",1);	
			OrderBean orderBean = new OrderBean();
			orderBean.put("createDate",OrderBean.DESC);
			List fileList = db.getList(attachmentMap, orderBean);
			result.put("fileList", fileList);
			//操作记录
			List<IMap> logRecordsList = db.getList(condition, "getLogRecordsList", "com.logRecords");
			result.put("logRecordsList", logRecordsList);
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
			result.put("busWorkMap",busWorkMap);
			result.put("busRivetweldMap",selectMap);
			result.put("stepId", in.get("stepId"));
			result.put("nodeId", in.get("nodeId"));
			result.put("instanceId", in.get("instanceId"));
			return result;
		}
		
		/**
		 * 验收检验 
		 * @param @param in,busId 进修单ID
		 * @param @return    
		 * @return IMap   
		 * @date Jul 18, 2013 4:04:43 PM
		 */
		public IMap modifyBusRivetweldOpinionCheck(IMap in){
			IMap result = new DataMap();
			IMap userMap = (IMap) in.get("userMap");
			//试车检验结果
			String checkRes =(String)in.get("result");
			if("1".equals(checkRes)){
				IMap selectMap = BeanFactory.getClassBean("com.BusRivetweld");
				selectMap.put("id", in.get("busId"));
				//进修单信息
				selectMap = db.get(selectMap);
				
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
				//修改工卡信息，完成时间，实耗工时
				IMap workHardMap = BeanFactory.getClassBean("com.BusWorkCard");
				workHardMap.put("workCard", selectMap.get("workCard"));
				workHardMap = db.get(workHardMap);
				workHardMap.put("completeDate",in.get("completeDate"));
				workHardMap.put("useManHours",in.get("useManHours"));
				HelperDB.setModifyInfo(HelperApp.getUserName(userMap),workHardMap);
				db.update(workHardMap);
				
				//查询与此业务进修单相同的其他业务流程是否完成
				boolean iscomplete = false;
				String inrepairid = (String) selectMap.get("inrepairId");
				//机修
				IMap mechanicMap = BeanFactory.getClassBean("com.BusMechanic");
				mechanicMap.put("inrepairId", inrepairid);
				List<IMap> mechanicList = db.getList(mechanicMap, null);
				if(!mechanicList.isEmpty()){
					mechanicMap = mechanicList.get(0);
					if(mechanicMap.get("completeDate")!=null){
						iscomplete = true;
					}else{
						iscomplete = false;
					}
				}else{
					iscomplete = true;
				}
				//钻修
				IMap drillingMap = BeanFactory.getClassBean("com.BusDrilling");
				drillingMap.put("inrepairId", inrepairid);
				List<IMap> drillingMapList = db.getList(drillingMap, null);
				if(!drillingMapList.isEmpty()){
					drillingMap = drillingMapList.get(0);
					if(drillingMap.get("completeDate")!=null){
						iscomplete = true;
					}else{
						iscomplete = false;
					}
				}else{
					iscomplete = true;
				}
				//电修
				IMap electricalMap = BeanFactory.getClassBean("com.BusElectrical");
				electricalMap.put("inrepairId", inrepairid);
				List<IMap> electricalMapList = db.getList(electricalMap, null);
				if(!electricalMapList.isEmpty()){
					electricalMap = electricalMapList.get(0);
					if(electricalMap.get("completeDate")!=null){
						iscomplete = true;
					}else{
					iscomplete = false;
				}
				}else{
					iscomplete = true;
				}
				//铆焊
//				IMap rivetweldMap = BeanFactory.getClassBean("com.BusRivetweld");
//				rivetweldMap.put("inrepairId", inrepairid);
//				List<IMap> rivetweldMapList = db.getList(rivetweldMap, null);
//				if(!rivetweldMapList.isEmpty()){
//					rivetweldMap = rivetweldMapList.get(0);
//					if(rivetweldMap.get("completeDate")!=null){
//						iscomplete = true;
//					}else{
//						iscomplete = false;
//					}
//				}else{
//					iscomplete = true;
//				}
				
				if(iscomplete){
					IMap busInrepairInfo = BeanFactory.getClassBean("com.BusInrepairInfo");
					busInrepairInfo.put("id", inrepairid);
					busInrepairInfo.put("repairStep", 5);
					db.update(busInrepairInfo);
				}
			}
			
			SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			
			String resCheck ="1".equals(checkRes)?"通过。":"不通过。";
			String remark="检验员："+userMap.get("name")+"在"+dftow.format(new Date())+"对设备进行了出厂检验，意见为："+in.get("opinionChecker")+"；结果为："+resCheck;
			LogBean logb=new LogBean(userMap,(String)in.get("busId"), "com.BusRivetweld","",remark, "0");
			LogRecordsService.saveOperationLog(logb,db);
			//工作流信息
			IMap wfmap = new DataMap();
			//wfmap.put("result", flag);// 判断是否受理
			//------------查询当前组织下的厂检验人员，没有则查询所有的车间检验人员
			IMap condition = new DataMap();
			//获取基础组织编码
			String orgCode = (String) userMap.get("orgId");
			//String [] orgCodes = orgCode.replace(".", ",").split(",");
			//试车检验结果
			wfmap.put("result",checkRes);
			if("1".equals(checkRes)){
				//获取当前用户所属厂区编码
			}else{
				condition.put("orgId",orgCode);
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
				// 发起工作流
				wfmap.put("assignedbyuser", str_wf_user.toString());
			}
			
			String instanceId = (String)in.get("instanceId");
			String stepId = (String)in.get("stepId");
			String nodeId = (String)in.get("nodeId");
			FireNetHelper.wf_next(db,userMap, instanceId, stepId, nodeId, wfmap);
			result.put("method.infoMsg",OperateHelper.getSaveMsg());
			result.put("method.url", in.get("url"));
			return result;
		}
		/**
		 * 跳转调度确认
		 * @param @param in
		 * @param @return  
		 * @return IMap<String,Object>
		 * logType:0-普通，1-审批
		 * @date Jul 17, 2013 5:39:18 PM
		 */
		public IMap<String, Object> toConfirmBusRivetweld(IMap<String, Object> in) {
			IMap<String, Object> result = new DataMap<String, Object>();// 输出map
			IMap selectMap = BeanFactory.getClassBean("com.BusRivetweld");
			selectMap.put("id", in.get("id"));
			//铆焊业务表信息
			IMap busRivetweldMap = db.get(selectMap);
			result.put("busRivetweldMap",busRivetweldMap );
			//进修单基本信息
			IMap busInrepairInfoMap =BeanFactory.getClassBean("com.BusInrepairInfo");
			busInrepairInfoMap.put("id",busRivetweldMap.get("inrepairId"));
			busInrepairInfoMap =db.get(busInrepairInfoMap);
			result.put("busInrepairInfoMap",busInrepairInfoMap);
			IMap condition = new DataMap();
			//业务表
			condition.put("busIdOne",busRivetweldMap.get("id"));
			condition.put("busNameOne","com.BusRivetweld");
			condition.put("busIdTow",busInrepairInfoMap.get("id"));
			condition.put("busNameTow","com.BusInrepairInfo");
			//附件列表
			List<IMap> fileList = db.getList(condition, "getAllFileList", "com.AttachmentInfo");
			result.put("fileList", fileList);
			//操作记录
			List<IMap> logRecordsList = db.getList(condition, "getLogRecordsList", "com.logRecords");
			result.put("logRecordsList",logRecordsList);
			//厂区车间信息
			IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
			String [] orgIds = busRivetweldMap.get("orgId").toString().replace(".",",").split(",");
			//厂区
			baseOrganizationMap.put("isValid",1);
			baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
			baseOrganizationMap = db.get(baseOrganizationMap);
			result.put("companyName", baseOrganizationMap.get("orgName"));
			//车区
			IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
			baseOrganization.put("orgCode", busRivetweldMap.get("orgId"));
			baseOrganization.put("isValid",1);
			baseOrganization = db.get(baseOrganization);
			result.put("groupName", baseOrganization.get("orgName"));
			//派工单信息
			IMap busWorkCardMap =BeanFactory.getClassBean("com.BusWorkCard");
			busWorkCardMap.put("busId",busRivetweldMap.get("id"));
			busWorkCardMap.put("busName","com.BusRivetweld");
			//根据业务表Id的到施工卡列表
			List busWorkCardList = db.getList(busWorkCardMap, null);
			result.put("busWorkCardList", busWorkCardList);
			result.put("logRecordsList", logRecordsList);
			result.put("fileList", fileList);
			result.put("busId",in.get("id"));
			result.put("stepId", in.get("stepId"));
			result.put("nodeId", in.get("nodeId"));
			result.put("instanceId", in.get("instanceId"));result.put("busRivetweldMap",busRivetweldMap);
			return result;
		}
		/**
		 * 调度确认
		 * @param @param in
		 * @param @return    
		 * @return IMap   
		 * @date Jul 19, 2013 9:38:03 AM
		 */
		public IMap saveBusRivetweldConfirm(IMap in){
			IMap result = new DataMap();
			IMap userMap = (IMap) in.get("userMap");
			// 发起工作流
			IMap wfmap = new DataMap();
			//wfmap.put("result", flag);// 判断是否受理
			//------------查询当前组织下的厂检验人员，没有则查询所有的车间检验人员
			IMap condition = new DataMap();
			//获取基础组织编码
			IMap busRivetweldMap = BeanFactory.getClassBean("com.BusRivetweld");
			busRivetweldMap.put("id", in.get("id"));
			busRivetweldMap = db.get(busRivetweldMap);
			busRivetweldMap.put("factoryWhere", in.get("factoryWhere"));
			busRivetweldMap.put("factoryDate", in.get("factoryDate"));
			
			String rem="";
			String orgId="";
			String [] orgCodes =busRivetweldMap.get("orgId").toString().replace(".",",").split(",");
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
				factoryMap.put("busId", busRivetweldMap.get("id"));
				factoryMap.put("busName", "com.BusRivetweld");
				factoryMap.put("affirmType",1);//状态为1调度直接出厂
				factoryMap.put("orgId",userMap.get("orgId"));
				factoryMap.put("workCard",busRivetweldMap.get("workCard"));
				factoryMap.put("equipmentName",busRivetweldMap.get("equipmentName"));
				factoryMap.put("equipmentModel",busRivetweldMap.get("equipmentModel"));
				factoryMap.put("factoryDate", in.get("factoryDate"));
				db.insert(factoryMap);
				
				IMap busRepairSettlementMap = BeanFactory.getClassBean("com.BusRepairSettlement");
				busRepairSettlementMap.put("id",HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.BusRepairSettlement"));
				busRepairSettlementMap.put("workCard",busRivetweldMap.get("workCard"));
				busRepairSettlementMap.put("mainCard",busRivetweldMap.get("mainCard"));
				busRepairSettlementMap.put("assistCard",busRivetweldMap.get("assistCard"));
				HelperDB.setCreate4isValid(HelperApp.getUserName(userMap), busRepairSettlementMap);
				db.insert(busRepairSettlementMap);
				
				busRivetweldMap.put("repairState", "4");
				//将进修单信息改为出厂
				IMap inrepairMap = BeanFactory.getClassBean("com.BusInrepairInfo");
				inrepairMap.put("repairState", "4");
				inrepairMap.put("completeDate", in.get("factoryDate"));
				inrepairMap.put("id", busRivetweldMap.get("inrepairId"));
				
				db.update(inrepairMap);
			}
			
			db.update(busRivetweldMap);
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
			String revenueType = (String)busRivetweldMap.get("revenueType");
			if("1".equals(revenueType)){
				actionId = "171";
			}else{
				actionId = "172";
			}
			FireNetHelper.wf_next(db, userMap, instanceId, stepId, nodeId, actionId,wfmap);
			//log日志
			SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			String remark="调度："+userMap.get("name")+"在"+dftow.format(new Date())+rem;
			LogBean logb=new LogBean(userMap,(String)in.get("id"), "com.BusRivetweld","",remark, "0");
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
			IMap busRivetweldMap = BeanFactory.getClassBean("com.BusRivetweld");
			busRivetweldMap.put("id", in.get("id"));
			busRivetweldMap = db.get(busRivetweldMap);
			busRivetweldMap.put("factoryWhere", in.get("factoryWhere"));
			busRivetweldMap.put("factoryDate", in.get("factoryDate"));
			db.update(busRivetweldMap);
			String rem="";
			IMap factoryMap = BeanFactory.getClassBean("com.Factory");
			factoryMap.put("busId", busRivetweldMap.get("id"));
			factoryMap.put("busName", "com.BusRivetweld");
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
			LogBean logb=new LogBean(userMap,(String)in.get("id"), "com.BusRivetweld","",remark, "0");
			LogRecordsService.saveOperationLog(logb,db);
			result.put("method.infoMsg", "操作成功！");
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
			IMap selectMap = BeanFactory.getClassBean("com.BusRivetweld");
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
			factoryMap.put("busName", "com.BusRivetweld");
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
			LogBean logb=new LogBean(userMap,(String)in.get("busId"), "com.BusRivetweld","",remark, "0");
			LogRecordsService.saveOperationLog(logb,db);
			result.put("method.infoMsg",OperateHelper.getSaveMsg());
			result.put("method.url", in.get("url"));
			return result;
		}
		/**
		 * 设备出厂 
		 * @param @param in
		 * @param @return    
		 * @return IMap   
		 * @date Jul 18, 2013 5:35:51 PM
		 */
		public IMap modifyBusRivetweldComplete(IMap in){
			IMap result = new DataMap();
			IMap selectMap = BeanFactory.getClassBean("com.BusRivetweld");
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
			//将进修单信息改为出厂
			IMap inrepairMap = BeanFactory.getClassBean("com.BusInrepairInfo");
			inrepairMap.put("repairState", in.get("repairState"));
			inrepairMap.put("completeDate", in.get("factoryDate"));
			inrepairMap.put("id", selectMap.get("inrepairId"));
			db.update(inrepairMap);
			
			//log日志
			SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			String res ="";
			if("2".equals(in.get("repairState"))){
				res ="退回";
			}else if("3".equals(in.get("repairState"))){
				res ="到装备库";
			}else if("4".equals(in.get("repairState"))){
				res ="出厂";
			}
			String remark="监造："+userMap.get("name")+"在"+dftow.format(new Date())+"进行设备出厂，结果为："+res+"。";
			LogBean logb=new LogBean(userMap,(String)in.get("busId"), "com.BusRivetweld","",remark, "0");
			LogRecordsService.saveOperationLog(logb,db);
			//工作流信息
			IMap wfmap = new DataMap();
			String instanceId = (String)in.get("instanceId");
			String stepId = (String)in.get("stepId");
			String nodeId = (String)in.get("nodeId");
			String actionId = "";
			//是否退回
			if("2".equals(in.get("repairState"))){
				actionId ="182";
			}else{
				actionId ="181";
			}
			FireNetHelper.wf_next(db,userMap, instanceId, stepId, nodeId,actionId, wfmap);
			result.put("method.infoMsg",OperateHelper.getSaveMsg());
			result.put("method.url", in.get("url"));
			return result;
		}
	/**
	 * -==================================cuijiahua============================================
	 */
		/**
		 * 
		 * @Description 跳转到编辑生产日报页面
		 * @param @param in
		 * @param @return    
		 * @return IMap<String,Object>   
		 * @author duch
		 * @date Sep 12, 2013 2:39:54 PM
		 */
		public IMap<String, Object> toaddNote(IMap<String, Object> in) {
			IMap<String, Object> result = new DataMap<String, Object>();// 输出map
			
			String flag = (String) in.get("flag");
			if (!com.brick.util.Util.checkNull(flag)) {
				result.put("flag", flag);
			} else {
				result.put("flag", "-1");
				
				IMap<String, Object> busMap = (IMap) in.get("busRivetweld");
				busMap = db.get(busMap);
				//根据井队号查询井队名称
				String teamNumber = (String) busMap.get("teamNumber");
				IMap crewMap = BeanFactory.getClassBean("com.DrillingCrew");
				crewMap.put("teamCode", teamNumber);
				crewMap = db.get(crewMap);
				
				busMap.put("teamNumber", crewMap.get("teamName"));
				//判断是否当天已经书写日志，如果有，则为编辑 如果没有则为新增
				IMap noteMap = BeanFactory.getClassBean("com.BusWorkNote");
				noteMap.put("busId", busMap.get("id"));
				noteMap.put("busName", busMap.getClassName());
				Date date=new Date();	
				SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				String date_str=df.format(date);
				
				noteMap.put("noteDate", date_str);
				List<IMap> list = db.getList(noteMap, "getworknotelist", "com.BusWorkNote");
				if(!list.isEmpty()){
					noteMap = list.get(0);
				}
				result.put("noteMap", noteMap);
				result.put("busRivetweldMap", busMap);
			}
			return result;
		}
		/**
		 * 
		 * @Description 保存日报结果
		 * @param @param in
		 * @param @return    
		 * @return IMap<String,Object>   
		 * @author duch
		 * @date Sep 16, 2013 9:47:15 AM
		 */
		public IMap<String, Object> addNote(IMap<String, Object> in) {
			IMap<String, Object> result = new DataMap<String, Object>();// 输出map
			IMap busWorkNote = (IMap) in.get("busWorkNote");	
			String id = (String) in.get("id");	
			busWorkNote.put("busName", "com.BusRivetweld");
			Date date=new Date();	
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			String date_str=df.format(date);
			
			String busId = (String) busWorkNote.get("busId");
			IMap<String, Object> busMap = BeanFactory.getClassBean("com.BusRivetweld");
			busMap.put("id", busId);
			
			busMap = db.get(busMap);
			
			busWorkNote.put("orgCode",busMap.get("orgId"));
			busWorkNote.put("deptName", busMap.get("deptName"));
			
			busWorkNote.put("noteDate", date_str);
			IMap<String, Object> userMap = (IMap<String, Object>) in.get("userMap");
			if(Util.checkNotNull(id)){
				busWorkNote.put("id",id);
				HelperDB.setModifyInfo(HelperApp.getUserName(userMap), busWorkNote);
				
				db.update(busWorkNote);
			}else{
				busWorkNote.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.BusWorkNote"));
				HelperDB.setCreate4isValid(HelperApp.getUserName(userMap), busWorkNote);
				db.insert(busWorkNote);
			}
			result.put("flag", "1");
			return result;
		}
		
		/**
		 * 
		 * @Description 根据组织结构代码，发起对应的流程
		 * @param @param in
		 * @param @return    
		 * @return IMap<String,Object>   
		 * @author duch
		 * @date Sep 16, 2013 9:47:15 AM
		 */
		public IMap<String, Object> addbusworkflow(IMap<String, Object> planMap,IMap<String, Object> busMap,IMap<String, Object> userMap) {
			IMap<String, Object> result = new DataMap<String, Object>();
			String planid = (String) planMap.get("id");
			
			IMap busMaterialPlanMap = BeanFactory.getClassBean("com.BusMaterialPlan");
			busMaterialPlanMap.put("planId", planid);
			List materialPlanList = db.getList(busMaterialPlanMap, "getMachiningPlanList","com.BusMaterialPlan");
			//如果加工件和修理件不为空，则将其保存到调度分配表中
			if(!materialPlanList.isEmpty()){
				for(int i=0;i<materialPlanList.size();i++){
					IMap materialPlan = (IMap)materialPlanList.get(i); 
					
					IMap mechdeptMap = BeanFactory.getClassBean("com.mechdept");
					mechdeptMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
							.getPackageName(), "com.mechdept"));
					mechdeptMap.put("equipmentName",materialPlan.get("materialDetail"));
					mechdeptMap.put("equipmentCode",materialPlan.get("materialCode"));
					mechdeptMap.put("planNum",materialPlan.get("ratifyNum"));
					mechdeptMap.put("deptfrom",userMap.get("orgid"));
					
					//获取车间名
					IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
					baseOrganization.put("orgCode", userMap.get("orgid"));
					baseOrganization.put("isValid",1);
					baseOrganization = db.get(baseOrganization);
					mechdeptMap.put("deptfromname", baseOrganization.get("orgName"));
					mechdeptMap.put("isappoint","0");
					mechdeptMap.put("repairType",materialPlan.get("type"));
					String workCard = (String)busMap.get("workCard");
					if(workCard!=null&&workCard!=""){
						mechdeptMap.put("mainCard",workCard.substring(0,workCard.indexOf("/")));
					}
					HelperDB.setCreate4isValid(HelperApp.getUserName(userMap), mechdeptMap);
					db.insert(mechdeptMap);
				}
			}
			
			return result;
		}

		
}