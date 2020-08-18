package com.u2a.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.u2a.framework.util.DateTimeUtil;
import com.u2a.framework.util.FireNetHelper;
import com.u2a.framework.util.FreemarkUtil;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;
import com.u2a.framework.util.PDFUtil;

/**
 * 
 *  系统 ：长庆机修管理平台
 *  类名 : BusToolswfService类
 *  劳保工具申请审批流程
 *  作者 : duch
 *  时间 : Jun 17, 2014
 *  版本 : 1.0
 */
@SuppressWarnings("unchecked")
public class BusToolswfService extends Service {
	/**
	 * 
	 *  描述:获取劳保工具申请列表 
	 *  方法: getToolsPlanList方法
	 *  作者: duch
	 *  时间: Jul 14, 2014
	 *  版本: 1.0
	 */
	public IMap getToolsPlanList(IMap in){
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
		Page page = db.pageQuery(in,"getToolsPlanList","com.BusToolswf","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		
		return result;
	}
	
	
	/**
	 * 
	 *  描述: 跳转到材料员调度入口
	 *  方法: toToolsPlan方法
	 *  作者: duch
	 *  时间: Jun 5, 2014
	 *  版本: 1.0
	 */
	public IMap toToolsPlan(IMap in){
		IMap result = new DataMap();
		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) userMap.get("orgid");
		
		IMap orgMap = BeanFactory.getClassBean("com.baseorganization");
		orgMap.put("orgCode", orgId);
		orgMap = db.get(orgMap);
		int serialnum = (Integer) orgMap.get("serialnum");
		serialnum = serialnum+1;
		String serialStr = String.valueOf(serialnum);
		int len = serialStr.length();
		while(len < 4) {  
			serialStr = "0" + serialStr;  
        	len++;  
    	}
		
		int year = DateTimeUtil.dataToYear(new Date());
		//拼接2014+YZ+四位顺序码
		String dept = "";
		//页面显示的车间名称
		String deptname = "";
		//判断是哪个厂的材料员提交的申请
		if(orgId.indexOf("YC")>-1){
			//判断是哪个车间的材料员提交的申请
			//如果是机修车间 1
			if(orgId.indexOf("JX01")>-1){
				dept = year+"YJ";
				deptname = "银川厂机修车间";
			}
			//电修车间 2
			else if(orgId.indexOf("DX")>-1){
				dept = year+"YD";
				deptname = "银川厂电修车间";
			}
			//钻修车间 3
			else if(orgId.indexOf("ZX")>-1){
				dept = year+"YZ";
				deptname = "银川厂钻修车间";
			}
			//铆焊车间 4
			else if(orgId.indexOf("MH")>-1){
				dept = year+"YM";
				deptname = "银川厂铆焊车间";
			}
			//机加车间 5
			else if(orgId.indexOf("JJ")>-1){
				dept = year+"YG";
				deptname = "银川厂机加车间";
			}
			//厂机关
			else{
				dept = year+"YC";
				deptname = "银川厂机关";
			}
		}else if(orgId.indexOf("QY")>-1){
			//判断是哪个车间的材料员提交的申请
			//如果是机修车间 1
			if(orgId.indexOf("JX")>-1){
				dept = year+"QJ";
				deptname = "庆阳厂机修车间";
			}
			//电修车间 2
			else if(orgId.indexOf("DX")>-1){
				dept = year+"QD";
				deptname = "庆阳厂电修车间";
			}
			//钻修车间 3
			else if(orgId.indexOf("ZX")>-1){
				dept = year+"QZ";
				deptname = "庆阳厂钻修车间";
			}
			//机加车间 5
			else if(orgId.indexOf("JJ")>-1){
				dept = year+"QG";
				deptname = "庆阳厂机加车间";
			}
			//厂机关
			else{
				dept = year+"QC";
				deptname = "庆阳厂机关";
			}
		}
		result.put("dept", dept+serialStr+"F");
		result.put("deptname", deptname);
		result.put("serialnum", serialnum);
		result.put("orgId", orgMap.get("id"));
		return result;
	}
	
	/**
	 * 
	 *  描述:各个车间发起流程 
	 *  方法: toToolsPlan方法
	 *  作者: duch
	 *  时间: Jun 6, 2014
	 *  版本: 1.0
	 */
	public IMap saveToolsPlan(IMap in){
		IMap result = new DataMap();
		IMap userMap = (IMap) in.get("userMap");
		String deptId = (String) userMap.get("orgId");
		//将页面传回来的信息保存到数据库
		IMap busToolsInfoMap = (IMap) in.get("busToolsInfo");
		//获取基础组织编码
		String orgCode = (String) userMap.get("orgId");
		String bustoolswfId= HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.BusToolswf");
		busToolsInfoMap.put("id", bustoolswfId);
		busToolsInfoMap.put("orgId", orgCode);
		busToolsInfoMap.put("assistCard", busToolsInfoMap.get("workCard"));
		
		HelperDB.setCreate4isValid(HelperApp.getUserName(userMap), busToolsInfoMap);
		
		db.insert(busToolsInfoMap);
		//判断是那个车间发起的流程
		String flowCode = "toolswf";
		String busId = bustoolswfId;
		String busName = "com.BusToolswf";
		String flowDesc = "工具劳保申请流程";
		String flowUrl = "${pageContext.request.contextPath}/api/businrepairinfo/viewToolsPlan";
		
		// 发起工作流
		IMap wfmap = new DataMap();
		IMap condition = new DataMap();
		//获取基础组织编码
		condition.put("orgId", deptId);
		if("|CQ|.|JX|.|QY|".equals(deptId)||"|CQ|.|JX|.|YC|".equals(deptId)){
			condition.put("postCode", "厂材料员");
		}else{
			condition.put("postCode", "车间材料员");
		}
		
		List<IMap> userList = db.getList(condition, "get_assign_user", "com.UserInfo");
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
		
		//发起劳保工具流程 用以保存各个车间的劳保申请信息
		FireNetHelper.wf_start(db,userMap,flowCode,busId,busName,
				busToolsInfoMap.get("workCard")+""+busToolsInfoMap.get("equipmentName") + "的"+flowDesc,flowUrl,0,wfmap);
		
		SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String remark="材料员："+userMap.get("name")+"在"+dftow.format(new Date())+"添加劳保工具申请";
		LogBean logb=new LogBean(userMap,busId,busName,"",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		
		//更新本年度的编码
		//保存成功 则更新baseOrg表中的序列号
		IMap baseMap = BeanFactory.getClassBean("com.baseorganization");
		baseMap.put("id", in.get("orgId"));
		baseMap.put("serialnum", in.get("serialnum"));
		
		db.update(baseMap);
		
		
		result.put("method.url", in.get("url"));
		result.put("method.infoMsg", "提交完成!");
		
		return result;
	}
	
	/**
	 * 
	 *  描述: 跳转到制定材料计划页面
	 *  方法: toMaterialPlan方法
	 *  作者: duch
	 *  时间: Jul 3, 2014
	 *  版本: 1.0
	 */
	public IMap<String, Object> toMaterialPlan(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.BusToolswf");
		selectMap.put("id", in.get("id"));
		//进修单信息
		IMap busToolswfMap = db.get(selectMap);
		result.put("busToolswfMap",busToolswfMap);
		//工作流信息
		result.put("stepId", in.get("stepId"));
		result.put("nodeId", in.get("nodeId"));
		result.put("instanceId", in.get("instanceId"));
		//当前审批计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
		busPlanInfoMap.put("busId",busToolswfMap.get("id"));
		busPlanInfoMap.put("busName","com.BusToolswf");
		busPlanInfoMap.put("isValid",1);
		//第几份材料申请计划
		List list = db.getList(busPlanInfoMap,null);
		/*******
		 * ======需要改动
		 */
		result.put("num", list.size()+1);
		//auditState - 0  审核不通过 1 审核通过 2未审
		busPlanInfoMap.put("auditState",0);
		busPlanInfoMap = db.get(busPlanInfoMap);
		/*******
		 * ======需要改动
		 */
		//材料申请信息
		IMap busMaterialPlanMap =BeanFactory.getClassBean("com.BusMaterialPlan");
		//判断当前是否已有材料申请
		if(busPlanInfoMap!=null){
			busMaterialPlanMap.put("planId",busPlanInfoMap.get("id"));
			//获取当前审批的材料申请列表
			List busMaterialPlanList = db.getList(busMaterialPlanMap, null);
			//回写 材料申请列表
			result.put("busMaterialPlanList",busMaterialPlanList);
		}
		//业务表
		IMap condition = new DataMap();
		condition.put("busIdOne",busToolswfMap.get("id"));
		condition.put("busNameOne","com.BusToolswf");
		//审批详情列表
		List<IMap> logRecordsList = db.getList(condition, "getLogRecordsList", "com.logRecords");
		result.put("logRecordsList", logRecordsList);
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = busToolswfMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", busToolswfMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		result.put("groupName", baseOrganization.get("orgName"));
		
		result.put("method.url", in.get("url"));
		result.put("method.infoMsg", "提交完成!");
		
		return result;
	}
	
	/**
	 * 
	 *  描述: 保存材料计划
	 *  方法: saveMaterialPlan方法
	 *  作者: duch
	 *  时间: Jun 19, 2014
	 *  版本: 1.0
	 */
	public IMap<String, Object> saveMaterialPlan(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//获取材料计划列表
		List busMaterialPlanList = (List) in.get("busMaterialPlan");
		//当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		//业务ID
		String busId = (String)in.get("busId");
		String planRemark=(String)in.get("planRemark");
		IMap busToolsMap =BeanFactory.getClassBean("com.BusToolswf");
		busToolsMap.put("id", busId);
		busToolsMap.put("isValid",1);
		busToolsMap = db.get(busToolsMap);
		//物流计划备注信息
		busToolsMap.put("planRemark", planRemark);
		db.update(busToolsMap);
		// 发起工作流
		IMap wfmap = new DataMap();
		IMap condition = new DataMap();
		//获取基础组织编码
		String orgCode = (String) userMap.get("orgId");
		condition.put("orgId", orgCode);
		//当前审批计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
		busPlanInfoMap.put("busId",in.get("busId"));
		busPlanInfoMap.put("busName","com.BusToolswf");
		//auditState - 0  审核不通过 1 审核通过 2未审
		busPlanInfoMap.put("auditState",0);
		busPlanInfoMap.put("isValid",1);
		//当前审批计划
		busPlanInfoMap = db.get(busPlanInfoMap);
		//作废审核未通过信息
		if(busPlanInfoMap!=null){
			busPlanInfoMap.put("isValid",0);
			db.update(busPlanInfoMap);
		}
		//重新添加循环添加材料计划
		if(!busMaterialPlanList.isEmpty()){
			//添加计划主信息
			IMap newBusPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
			//关联机修流程
			newBusPlanInfoMap.put("busId", busId);
			newBusPlanInfoMap.put("busName", "com.BusToolswf");
			newBusPlanInfoMap.put("planCode", (String)in.get("planCode"));
			//计划ID
			String busPlanInfoId =HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(), "com.BusPlanInfo");
			newBusPlanInfoMap.put("id", busPlanInfoId);
			//工卡号
			newBusPlanInfoMap.put("workCard",busToolsMap.get("workCard"));
			//orgId
			newBusPlanInfoMap.put("orgId", busToolsMap.get("orgId"));
			//物料计划大件材料申请原因
			newBusPlanInfoMap.put("bigRemark", (String)in.get("bigRemark"));
			//为创建对象赋值
			HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),newBusPlanInfoMap);
			for (int i = 0; i < busMaterialPlanList.size();i++) {
				IMap busMaterialPlan = (IMap)busMaterialPlanList.get(i);

				String planId =  HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(), "com.BusMaterialPlan");
				busMaterialPlan.put("id",planId);
				busMaterialPlan.put("planId", busPlanInfoId);
				busMaterialPlan.put("ratifyNum", busMaterialPlan.get("materialNum"));
				HelperDB.setCreateInfo(HelperApp.getUserName(userMap), busMaterialPlan);
				db.insert(busMaterialPlan);
			}
			SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
			String remark="材料员："+userMap.get("name")+"在"+dftow.format(new Date())+"添加了材料及加工件领用申请信息。";
			LogBean logb=new LogBean(userMap,busId, "com.BusToolswf","",remark, "0");
			LogRecordsService.saveOperationLog(logb,db);
			//判断如果是厂材料员提交的申请 则直接跳转到厂领导审批
			String orgcode = (String) busToolsMap.get("orgId");
			if("|CQ|.|JX|.|QY|".equals(orgcode)||"|CQ|.|JX|.|YC|".equals(orgcode)){
				wfmap.put("result", "2");
				condition.put("postCode", "厂区领导");
				newBusPlanInfoMap.put("auditState",2);
			}else{
				wfmap.put("result", "1");
				condition.put("postCode", "车间主任");
				newBusPlanInfoMap.put("auditState",2);
			}
			db.insert(newBusPlanInfoMap);
		}else{
			throw new BusinessException("请制定物料申请!");
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
	 * 
	 *  描述: 跳转到各个审批页面
	 *  方法: toAuditPage方法
	 *  作者: duch
	 *  时间: Jul 11, 2014
	 *  版本: 1.0
	 */
	public IMap<String, Object> toAuditPage(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.BusToolswf");
		selectMap.put("id", in.get("id"));
		//业务表基本信息
		IMap busToolswfMap = db.get(selectMap);
		//当前审批计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
		busPlanInfoMap.put("busId",busToolswfMap.get("id"));
		busPlanInfoMap.put("busName","com.BusToolswf");
		//auditState - 0 不通过 1 审核通过 2 待审
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
		logRecordsMap.put("busId",busToolswfMap.get("id"));
		logRecordsMap.put("busName","com.BusToolswf");
		//logRecordsMap.put("logType","1");
		OrderBean orderBean = new OrderBean();
		orderBean.put("operationDate",OrderBean.DESC);
		List logRecordsList = db.getList(logRecordsMap, orderBean);
		
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = busToolswfMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", busToolswfMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);

		result.put("logRecordsList", logRecordsList);
		result.put("busToolswfMap", busToolswfMap);
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
	 * 
	 *  描述: 保存审批流程各个步骤的结果
	 *  方法: saveAudit方法
	 *  作者: duch
	 *  时间: Jul 11, 2014
	 *  版本: 1.0
	 */
	public IMap saveAudit(IMap in){
		IMap result = new DataMap();
		IMap busMap = (IMap) in.get("busTools");
		IMap userMap = (IMap) in.get("userMap");
		String remark="";
		String docId="";
		String str="";
		String sk = (String) in.get("sk");
		String cq = (String) in.get("cq");
		Date date=new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String date_str=df.format(date);
		db.update(busMap);
		IMap busToolsMap = BeanFactory.getClassBean("com.BusToolswf");
		busToolsMap.put("id",busMap.get("id"));
		busToolsMap = db.get(busToolsMap);
		//当前审批计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
		busPlanInfoMap.put("busId",busToolsMap.get("id"));
		busPlanInfoMap.put("busName","com.BusToolswf");
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
			busPlanInfoMap.put("auditState",busToolsMap.get("jlResult"));
			//上传审批文件
			IMap attachmentInfoMap=(IMap) in.get("attachmentInfo");
		 
			str=busMap.get("jlResult").equals("0")?"不同意":"同意";
			remark="经理："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+busMap.get("jlRemark")+"；结果为："+str+"。";
			if("0".equals(busMap.get("jlResult"))){
				orgId = (String) busToolsMap.get("orgId");
				postCode="车间材料员";
				spresult = "0";
			}else if("1".equals(busMap.get("jlResult"))&&"1".equals(sk)){
				orgId =orgCodes[0];
				postCode="总公司领导";
				spresult = "1";
			}else if("1".equals(busMap.get("jlResult"))&&!"1".equals(sk)){
				//厂材料员确认
				orgId = (String) busToolsMap.get("orgId");
				String[] orgids = orgId.replace(".", ",").split(",");
				orgId =orgids[0]+"."+orgids[1]+"."+orgids[2];
				postCode="厂材料员";
				spresult = "2";
			}
		}else if(in.get("type").toString().equals("ccl")){//新增厂区领导审批前-厂材料员审批
			busPlanInfoMap.put("auditState",busMap.get("cclResult"));
			str="0".equals(busMap.get("cclResult"))?"不同意":"同意";
			remark="厂材料员："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+busMap.get("cclRemark")+"；结果为："+str+"。";		
			//总公司
			if("0".equals(busMap.get("cclResult"))){
				orgId = (String) busToolsMap.get("orgId");
				postCode="车间材料员";
				spresult = "0";
			}else{
				orgId =orgCodes[0];
				postCode="厂区领导";
				spresult = "1";
			}
		}else if(in.get("type").toString().equals("cq")){
			busPlanInfoMap.put("auditState",busMap.get("cqResult"));
			str=busMap.get("cqResult").equals("0")?"不同意":"同意";
			remark="厂区领导："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+busMap.get("cqRemark")+"；结果为："+str+"。";		
			//总公司
			if("0".equals(busMap.get("cqResult"))){
				orgId = (String) busToolsMap.get("orgId");
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
			busPlanInfoMap.put("auditState",busMap.get("zrResult"));
			str=busMap.get("zrResult").equals("0")?"不同意":"同意";
			remark="车间主任："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+busMap.get("zrRemark")+"；结果为："+str+"。";	
			if("0".equals(busMap.get("zrResult"))){
				orgId = (String) busToolsMap.get("orgId");
				postCode="车间材料员";
				spresult = "0";
			}else{
				orgId =orgCodes[0]+"."+orgCodes[1]+"."+orgCodes[2];
				postCode="厂材料员";
				spresult = "1";
			}
		}else if(in.get("type").toString().equals("sc")){
			busPlanInfoMap.put("auditState",busMap.get("scResult"));
			str=busMap.get("scResult").equals("0")?"不同意":"同意";
			remark="生产主管："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+busMap.get("scRemark")+"；结果为："+str+"。";		
			//总公司
			if("0".equals(busMap.get("scResult"))){
				orgId = (String) busToolsMap.get("orgId");
				postCode="车间材料员";
				spresult = "0";
			}else{
				orgId =orgCodes[0];
				postCode="经营主管";
				spresult = "1";
			}
		}else if(in.get("type").toString().equals("jy")){
			busPlanInfoMap.put("auditState",busMap.get("jyResult"));
			str=busMap.get("jyResult").equals("0")?"不同意":"同意";
			remark="经营主管："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+busMap.get("jyRemark")+"；结果为："+str+"。";		
			//总公司
			if("0".equals(busMap.get("jyResult"))){
				orgId = (String) busToolsMap.get("orgId");
				postCode="车间材料员";
				spresult = "0";
			}else{
				orgId =orgCodes[0];
				postCode="经理";
				spresult = "1";
			}
		}else if(in.get("type").toString().equals("zgs")){
			busPlanInfoMap.put("auditState",busMap.get("zgsResult"));
			str=busMap.get("zgsResult").equals("0")?"不同意":"同意";
			//总公司
			remark="总公司领导："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+busMap.get("zgsRemark")+"；结果为："+str+"。";	
			if("0".equals(busMap.get("zgsResult"))){
				orgId = (String) busToolsMap.get("orgId");
				postCode="车间材料员";
				spresult = "0";
			}else{
				//厂材料员确认
				orgId = (String) busToolsMap.get("orgId");
				String[] orgids = orgId.replace(".", ",").split(",");
				orgId =orgids[0]+"."+orgids[1]+"."+orgids[2];
				postCode="厂材料员";
				spresult = "1";
			}
		}
		//审核物料计划
		db.update(busPlanInfoMap);
		//获取材料计划列表
		List busMaterialPlanList = (List) in.get("busMaterialPlan");
		for (int i = 0; i < busMaterialPlanList.size(); i++) {
			IMap busMaterialPlan = (IMap)busMaterialPlanList.get(i);
			db.update(busMaterialPlan);
		}
		LogBean logb=null;
		if(!docId.equals("")){
		 logb=new LogBean(userMap,busToolsMap.get("Id").toString(), "com.BusToolswf","",remark,"1",docId);
		}else{
		 logb=new LogBean(userMap,busToolsMap.get("Id").toString(), "com.BusToolswf","",remark,"1");
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
	 * 
	 *  描述: 打印大件审批表
	 *  方法: printBigAccpet方法
	 *  作者: duch
	 *  时间: Jul 14, 2014
	 *  版本: 1.0
	 */
	public IMap<String, Object> printBigAccpet(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		HttpServletResponse response = (HttpServletResponse) in.get("response");
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		String path = request.getSession().getServletContext().getRealPath(
		"/");
		//当前审批计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
		busPlanInfoMap.put("busId",in.get("busid"));
		busPlanInfoMap.put("busName",in.get("tablename"));
		//auditState - 0 不通过 1 审核通过 2 待审
		busPlanInfoMap.put("isValid",1);
		busPlanInfoMap = db.get(busPlanInfoMap);
		//材料申请信息
		IMap BusMaterialPlanMap =BeanFactory.getClassBean("com.BusMaterialPlan");
		BusMaterialPlanMap.put("planId",busPlanInfoMap.get("id"));
	    //type 1：领用 2：加工 3维修
		//非主任审批，只显示领用件
		//获取当前审批的材料申请列表
		List busMaterialPlanList = db.getList(BusMaterialPlanMap,null);
		List resultList = new ArrayList();
		if(!busMaterialPlanList.isEmpty()){
			for(int i=0;i<busMaterialPlanList.size();i++){
				IMap tempmap = (IMap) busMaterialPlanList.get(i);
				Double price = (Double)tempmap.get("estimateprice");
				if(price>10000){
					resultList.add(tempmap);
				}
			}
		}
		
		IMap freemap = new DataMap();
		freemap.put("materiallist", resultList);
		freemap.put("deptname", in.get("deptname"));
		freemap.put("bigRemark", busPlanInfoMap.get("bigRemark"));
		//业务信息
		IMap busMap = BeanFactory.getClassBean((String)busPlanInfoMap.get("busName"));
		busMap.put("id", (String)busPlanInfoMap.get("busId"));
		busMap = db.get(busMap);
		freemap.put("jlRemark",busMap.get("jlRemark"));
		freemap.put("jyRemark",busMap.get("jyRemark"));
		freemap.put("scRemark",busMap.get("scRemark"));
		freemap.put("cardNo",busMap.get("assistCard")+"/"+busMap.get("assistCard"));
		freemap.put("printDate", DateTimeUtil.getLocalDateStr("yyyy-MM-dd"));
		String body = FreemarkUtil.transform(path+"temp","bigaccpet.ftl",freemap);
		PDFUtil.getPdf("大件审批表", body, request, response);
		return result;
	}
	
	/**
	 * 
	 *  描述: 厂材料员确认
	 *  方法: saveMaterialConfirm方法
	 *  作者: duch
	 *  时间: Jul 14, 2014
	 *  版本: 1.0
	 */
	public IMap saveMaterialConfirm(IMap in){
		IMap result = new DataMap();
		IMap userMap = (IMap) in.get("userMap");
		
		IMap busToolsMap = db.get((IMap) in.get("busTools"));
		
		String instanceId = (String)in.get("instanceId");
		String stepId = (String)in.get("stepId");
		String nodeId = (String)in.get("nodeId");
		
		String orgId = (String) busToolsMap.get("orgId");
		String postCode="车间材料员";
		String spresult = "1";
		
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
		wfmap.put("result",spresult);
		
		FireNetHelper.wf_next(db, userMap, instanceId, stepId, nodeId,wfmap);
		result.put("method.infoMsg","确认完成!");
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 
	 *  描述:跳转到出库页面 
	 *  方法: toOutStore方法
	 *  作者: duch
	 *  时间: Jul 14, 2014
	 *  版本: 1.0
	 */
	public IMap<String, Object> toOutStore(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap selectMap = BeanFactory.getClassBean("com.BusToolswf");
		selectMap.put("id", in.get("id"));
		//获取基本信息
		IMap busToolsMap = db.get(selectMap);
		//当前审批计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
		busPlanInfoMap.put("busId",in.get("id"));
		busPlanInfoMap.put("busName","com.BusToolswf");
		busPlanInfoMap.put("isValid",1);
		busPlanInfoMap = db.get(busPlanInfoMap);
		if(busPlanInfoMap!=null){
			//材料申请信息
			IMap busMaterialPlanMap =BeanFactory.getClassBean("com.BusMaterialPlan");
			busMaterialPlanMap.put("planId",busPlanInfoMap.get("id"));
			//type 1：领用 2：加工 3维修
			busMaterialPlanMap.put("type","1,4");
			//获取当前审批的材料申请列表
			List busMaterialPlanList = db.getList(busMaterialPlanMap,"getBusMaterialPlanList", "com.BusMaterialPlan");
			
			result.put("materialList", busMaterialPlanList);
		}
		
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = busToolsMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", busToolsMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		//返回值
		// 工作流信息
		String instanceId = (String) in.get("instanceId");
		String nodeId = (String) in.get("nodeId");
		String stepId = (String) in.get("stepId");
		result.put("busToolswfMap",busToolsMap );
		result.put("instanceId", instanceId);
		result.put("nodeId", nodeId);
		result.put("stepId", stepId);
		return result;
	}
	/**
	 * 
	 *  描述: 出库方法
	 *  方法: saveOutStore方法
	 *  作者: duch
	 *  时间: Jul 14, 2014
	 *  版本: 1.0
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
		
		String orgName = (String) orgInfo.get("orgName");
		//获取页面上填写的出库数量
		List<IMap> outList = (List<IMap>) in.get("outStoreInfo");
		//按照先进先出的原则，进行材料的增减
		//循环页面返回的值
		//判断物料编码相同的，则取数量进行对比，
		//如果第一条数量大于页面填写数量，剩余数量为第一条数量减去填写数量
		//如果第一条数量等于页面填写数量，剩余数量为0 并将其置为无效
		//依次递归，直到页面填写数量减为0为止
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
			busPlanInfoMap.put("busName","com.BusToolswf");
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
	
		// 工作流信息
		String instanceId = (String) in.get("instanceId");
		
		String nodeId = (String) in.get("nodeId");
		String stepId = (String) in.get("stepId");
		FireNetHelper.wf_next(db, userMap, instanceId, stepId, nodeId,
				wfmap);
		SimpleDateFormat dftow=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String remark="材料员："+userMap.get("name")+"在"+dftow.format(new Date())+"将物料出库。";
		LogBean logb=new LogBean(userMap,busId, "com.BusToolswf","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		
		result.put("method.url", (String)in.get("url"));
		result.put("method.infoMsg", OperateHelper.getSaveMsg());
		return result;
	}
	
	
	
	/**
	 * 
	 *  描述: 出库
	 *  方法: saveRatioStore方法
	 *  作者: duch
	 *  时间: Jul 14, 2014
	 *  版本: 1.0
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
	 * 
	 *  描述:保存出库的方法  
	 *  方法: updateRatifyNum方法
	 *  作者: duch
	 *  时间: Jul 14, 2014
	 *  版本: 1.0
	 */
	public IMap updateRatifyNum(IMap in){
		IMap result = new DataMap();
		//获取材料计划列表
		List busMaterialPlanList = (List) in.get("busMaterialPlan");
		for (int i = 0; i < busMaterialPlanList.size(); i++) {
			IMap busMaterialPlan = (IMap)busMaterialPlanList.get(i);
			db.update(busMaterialPlan);
		}
		result.put("method.url", in.get("url"));
		result.put("method.infoMsg", "保存成功！");
		return result;
	}
	
	/**
	 * 
	 *  描述: 跳转到劳保工具详情
	 *  方法: toBustoolswfDetail方法
	 *  作者: duch
	 *  时间: Jul 14, 2014
	 *  版本: 1.0
	 */
	public IMap toBustoolswfDetail(IMap in){
		IMap result = new DataMap();
		//根据ID 查询劳保工具基本信息
		IMap busMap = (IMap) in.get("busTools");
		busMap = db.get(busMap);
		//查询材料计划信息
		//当前审批计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfo");
		busPlanInfoMap.put("busId",busMap.get("id"));
		busPlanInfoMap.put("busName","com.BusToolswf");
		//auditState - 0 不通过 1 审核通过 2 待审
		busPlanInfoMap.put("isValid",1);
		busPlanInfoMap = db.get(busPlanInfoMap);
		result.put("busPlanInfoMap", busPlanInfoMap);
		//材料申请信息
		IMap busMaterialPlanMap =BeanFactory.getClassBean("com.BusMaterialPlan");
		busMaterialPlanMap.put("planId",busPlanInfoMap.get("id"));
	    //type 1：领用 2：加工 3维修
//		//非主任审批，只显示领用件
//		busMaterialPlanMap.put("type","1,4");
		//获取当前审批的材料申请列表
		List busMaterialPlanList = db.getList(busMaterialPlanMap,"getBusMaterialPlanList", "com.BusMaterialPlan");
		//查询材料出库信息
		IMap outMap =BeanFactory.getClassBean("com.outStoreDetail");
		outMap.put("busId",busMap.get("id"));
		outMap.put("busName","com.BusToolswf");
		
		List outList = db.getList(outMap, null);
		//查询日志信息
		//审批详情列表
		IMap logRecordsMap =BeanFactory.getClassBean("com.logRecords");
		logRecordsMap.put("busId",busMap.get("id"));
		logRecordsMap.put("busName","com.BusToolswf");
		OrderBean orderBean = new OrderBean();
		orderBean.put("operationDate",OrderBean.DESC);
		List logRecordsList = db.getList(logRecordsMap, orderBean);
		
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = busMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车间
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", busMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		
		result.put("groupName", baseOrganization.get("orgName"));
		result.put("busToolswfMap", busMap);
		result.put("outList", outList);
		result.put("materialPlanList", busMaterialPlanList);
		result.put("logRecordsList", logRecordsList);
		return result;
	}
	
	
	
	
	
}