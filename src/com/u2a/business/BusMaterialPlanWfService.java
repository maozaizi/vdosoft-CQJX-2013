package com.u2a.business;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import net.sf.json.JSONObject;

import com.brick.api.Service;
import com.brick.dao.Page;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.data.OrderBean;
import com.brick.manager.BeanFactory;
import com.u2a.framework.commons.Constants;
import com.u2a.framework.commons.OperateHelper;
import com.u2a.framework.service.sys.logrecords.LogBean;
import com.u2a.framework.service.sys.logrecords.LogRecordsService;
import com.u2a.framework.util.FileUtil;
import com.u2a.framework.util.FireNetHelper;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;
import com.u2a.framework.util.Util;

/**
 * 
 * 系统名称：长庆钻井机修公司 - 设备维修管理系统   
 * 类名称：BusMaterialPlanWfService   
 * 类描述：材料计划审批   
 * 创建人：孟勃婷 
 * 创建时间：Aug 9, 2013 4:12:44 PM
 */
@SuppressWarnings("unchecked")
public class BusMaterialPlanWfService extends Service{
	
	
	/**
	 * 跳转材料申请页面
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Aug 8, 2013 10:01:21 AM
	 */
	
	public IMap<String, Object> toMaterialPlan(IMap<String, Object> in) {
		IMap result = new DataMap();
		
		IMap userMap = (IMap) in.get("userMap");
		String code = (String) userMap.get("orgId");
		IMap orgMap = BeanFactory.getClassBean("com.baseorganization");
		orgMap.put("orgCode", code);
		orgMap = db.get(orgMap);
		int serialnum = (Integer) orgMap.get("serialnum");
		serialnum = serialnum+1;
		String serialStr = String.valueOf(serialnum);
		int len = serialStr.length();
		while(len < 4) {  
			serialStr = "0" + serialStr;  
        	len++;  
    	}  
		
		Calendar a=Calendar.getInstance();
		String str = "";
		if(code.indexOf("|YC|")>0){
			str = a.get(Calendar.YEAR)+"Y";
		} 
		if(code.indexOf("|QY|")>0){
			str = a.get(Calendar.YEAR)+"Q";
		} 
		if(code.indexOf("|ZX|")>0){
			str = str+"Z";
		} 
		if(code.indexOf("|JX01|")>0){
			str = str+"J";
		} 
		if(code.indexOf("|DX|")>0){
			str = str+"D";
		} 
		if(code.indexOf("|MH|")>0){
			str = str+"M";
		} 
		if(code.indexOf("|JJ|")>0){
			str = str+"G";
		} 
		str = str + serialStr + "PJH";
		
		result.put("planCode", str);
		result.put("orgCode", orgMap.get("id"));
		result.put("serialnum", serialnum);
		//当前审批计划
		IMap busPlanInfoWfMap = BeanFactory.getClassBean("com.BusPlanInfoWf");
		//判断当前是否已有材料申请
		if(in.get("id")!=null&&in.get("id")!=""){
			busPlanInfoWfMap.put("id", in.get("id"));
			busPlanInfoWfMap = db.get(busPlanInfoWfMap);
			result.put("busPlanInfoWfMap",busPlanInfoWfMap);
			//材料申请信息
			IMap busMaterialPlanWfMap =BeanFactory.getClassBean("com.BusMaterialPlanWf");
			busMaterialPlanWfMap.put("planId",busPlanInfoWfMap.get("id"));
			//获取当前审批的材料申请列表
			List busMaterialPlanWfList = db.getList(busMaterialPlanWfMap, null);
			//回写 材料申请列表
			result.put("busMaterialPlanWfList",busMaterialPlanWfList);
			//审批详情
			IMap logRecordsMap =BeanFactory.getClassBean("com.logRecords");
			logRecordsMap.put("busId",in.get("id"));
			logRecordsMap.put("busName","com.BusPlanInfoWf");
			//logRecordsMap.put("logType","1");
			List logRecordsList = db.getList(logRecordsMap, null);
			result.put("logRecordsList",logRecordsList);
		}
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
	public IMap<String, Object> saveBusMaterialPlanWf(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		//获取材料计划列表
		List busMaterialPlanList = (List) in.get("busMaterialPlanWf");
		//当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		// 发起工作流
		IMap wfmap = new DataMap();
		IMap condition = new DataMap();
		//获取基础组织编码
		String orgCode = (String) userMap.get("orgId");
		condition.put("orgId", orgCode);
		//当前审批计划
		IMap busPlanInfoWfMap = BeanFactory.getClassBean("com.BusPlanInfoWf");
		busPlanInfoWfMap.put("id",in.get("id"));
		//当前审批计划
		busPlanInfoWfMap = db.get(busPlanInfoWfMap);
		//当前是否有审批计划，作废审核未通过信息
		if(busPlanInfoWfMap!=null){
			busPlanInfoWfMap.put("isValid",0);
			db.update(busPlanInfoWfMap);
		}
		String busPlanInfoId="";
		//重新添加循环添加材料计划
		if(busMaterialPlanList !=null && busMaterialPlanList.size()>0){
			//添加计划主信息
			IMap newBusPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfoWf");
			//计划ID
			busPlanInfoId =HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(), "com.BusPlanInfoWf");
			newBusPlanInfoMap.put("id", busPlanInfoId);
			//auditState - 0  审核不通过 1 审核通过 2未审
			newBusPlanInfoMap.put("auditState",2);
			newBusPlanInfoMap.put("orgId", orgCode);
			newBusPlanInfoMap.put("planRemark",in.get("planRemark"));
			newBusPlanInfoMap.put("planCode",in.get("planCode"));
			//为创建对象赋值
			HelperDB.setCreate4isValid(HelperApp.getUserName(userMap),newBusPlanInfoMap);
			db.insert(newBusPlanInfoMap);
			for (int i = 0; i < busMaterialPlanList.size();i++) {
				IMap busMaterialPlan = (IMap)busMaterialPlanList.get(i);
				String planId =  HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(), "com.BusMaterialPlanWf");
				busMaterialPlan.put("id",planId);
				busMaterialPlan.put("planId", busPlanInfoId);
				busMaterialPlan.put("ratifyNum", busMaterialPlan.get("materialNum"));
				HelperDB.setCreateInfo(HelperApp.getUserName(userMap), busMaterialPlan);
				db.insert(busMaterialPlan);
			}
			String remark="材料员"+userMap.get("name")+"在"+Util.toStringDate(new Date())+"添加了材料及加工件领用申请信息。";
			LogBean logb=new LogBean(userMap,busPlanInfoId, "com.BusPlanInfoWf","",remark, "0");
			LogRecordsService.saveOperationLog(logb,db);
			wfmap.put("result", "1");
			condition.put("postCode", "车间主任");
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
		wfmap.put("assignedbyuser", str_wf_user.toString());
		FireNetHelper.wf_start(db,userMap,"materialwf",busPlanInfoId,"com.BusPlanInfoWf",(String)in.get("planCode")+"物料领用申请","api/busplanwf/toBusPlanInfoWfDetailPage",0,wfmap);
		//更新本年度的编码
		//保存成功 则更新baseOrg表中的序列号
		IMap baseMap = BeanFactory.getClassBean("com.baseorganization");
		baseMap.put("id", in.get("orgCode"));
		baseMap.put("serialnum", in.get("serialnum"));
		
		db.update(baseMap);
		
		result.put("method.url", in.get("url"));
		result.put("method.infoMsg", "提交完成!");
		return result;
	}
	/**
	 * 跳转审批页面，获取物料计划审批信息 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Aug 12, 2013 10:17:37 AM
	 */
	public IMap<String, Object> getBusPlanInfoWfDetails(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap busPlanInfoWfMap = BeanFactory.getClassBean("com.BusPlanInfoWf");
		busPlanInfoWfMap.put("id", in.get("id"));
		//物料计划信息
		busPlanInfoWfMap = db.get(busPlanInfoWfMap);
		result.put("busPlanInfoWfMap",busPlanInfoWfMap);
		result.put("busPlanInfoMap", busPlanInfoWfMap);
		//厂区车间信息
		IMap baseOrganizationMap = BeanFactory.getClassBean("com.baseorganization");
		String [] orgIds = busPlanInfoWfMap.get("orgId").toString().replace(".",",").split(",");
		//厂区
		baseOrganizationMap.put("isValid",1);
		baseOrganizationMap.put("orgCode",orgIds[0]+"."+orgIds[1]+"."+orgIds[2]);
		baseOrganizationMap = db.get(baseOrganizationMap);
		result.put("companyName", baseOrganizationMap.get("orgName"));
		//车区
		IMap baseOrganization = BeanFactory.getClassBean("com.baseorganization");
		baseOrganization.put("orgCode", busPlanInfoWfMap.get("orgId"));
		baseOrganization.put("isValid",1);
		baseOrganization = db.get(baseOrganization);
		result.put("groupName", baseOrganization.get("orgName"));
		//物料计划详细列表
		IMap busMaterialPlanWfMap = BeanFactory.getClassBean("com.BusMaterialPlanWf");
		busMaterialPlanWfMap.put("planId",busPlanInfoWfMap.get("id"));
		List busMaterialPlanWfList = db.getList(busMaterialPlanWfMap,null);
		result.put("busMaterialPlanWfList",busMaterialPlanWfList);
		//审批详情列表
		IMap logRecordsMap =BeanFactory.getClassBean("com.logRecords");
		logRecordsMap.put("busId",in.get("id"));
		logRecordsMap.put("busName","com.BusPlanInfoWf");
		logRecordsMap.put("logType","1");
		List logRecordsList = db.getList(logRecordsMap, null);
		result.put("logRecordsList",logRecordsList);
		//附件信息
		IMap attachmentInfoIMap =BeanFactory.getClassBean("com.AttachmentInfo");
		attachmentInfoIMap.put("busId",in.get("id"));
		attachmentInfoIMap.put("busName","com.BusPlanInfoWf");
		attachmentInfoIMap.put("busType","2");
		attachmentInfoIMap.put("isValid",1);
		List fileList = db.getList(attachmentInfoIMap, null);
		result.put("fileList",fileList);
		//工作流
		result.put("stepId", in.get("stepId"));
		result.put("instanceId",in.get("instanceId") );
		result.put("nodeId", in.get("nodeId"));
		return result;
	}
	/**
	 * 物料计划审批 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Aug 12, 2013 10:29:49 AM
	 */
	public IMap updateLeaderShipAudit(IMap in){
		IMap result = new DataMap();
		IMap busPlanInfoWfMap = (IMap) in.get("busPlanInfoWf");
		IMap userMap = (IMap) in.get("userMap");
		String remark="";
		String docId="";
		String str="";
		String re="";
		String sk = (String) in.get("sk");
		Date date=new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String date_str=df.format(date);
		db.update(busPlanInfoWfMap);
		//当前审批计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfoWf");
		busPlanInfoMap.put("id",busPlanInfoWfMap.get("id"));
		busPlanInfoMap = db.get(busPlanInfoMap);
		//下一步审批人
		String postCode ="";
		//获取基础组织编码
		//String orgCode = (String) userMap.get("orgId");
		String orgCode = (String) busPlanInfoMap.get("orgId");
		String [] orgCodes = orgCode.replace(".", ",").split(",");
		String orgId ="";
		String spresult = "";
		String actionId = "";
		//只有是经理审批才进上传方法
		if(in.get("type").toString().equals("jl")){
			busPlanInfoMap.put("auditState",busPlanInfoWfMap.get("jlResult"));
			//上传审批文件
			IMap attachmentInfoMap=(IMap) in.get("attachmentInfo");
			if(attachmentInfoMap.get("docUrl")!=null){
				attachmentInfoMap.put("busId", busPlanInfoWfMap.get("id"));
				attachmentInfoMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.AttachmentInfo"));
				attachmentInfoMap.put("isValid",Constants.ISVALID);
				attachmentInfoMap.put("busName","com.BusPlanInfoWf");
				//状态2为审批上传文件
				attachmentInfoMap.put("busType","2");	
				//为创建数据赋值
				HelperDB.setCreateInfo(HelperApp.getUserName(userMap),attachmentInfoMap);
				db.insert(attachmentInfoMap);
				docId=attachmentInfoMap.get("id").toString();
			}
			str=busPlanInfoWfMap.get("jlResult").equals("0")?"不同意":"同意";
			remark="经理："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+busPlanInfoWfMap.get("jlRemark")+"；结果为："+str+"";
			if("0".equals(busPlanInfoWfMap.get("jlResult"))){
				orgId = orgCode;
				postCode="车间材料员";
				spresult = "0";
			}else if("1".equals(busPlanInfoWfMap.get("jlResult"))&&"1".equals(sk)){
				orgId =orgCodes[0];
				postCode="总公司领导";
				spresult = "1";
			}else if("1".equals(busPlanInfoWfMap.get("jlResult"))&&!"1".equals(sk)){
				orgId =orgCode;
				postCode="车间材料员";
				spresult = "2";
			}
		}else if(in.get("type").toString().equals("cq")){
			busPlanInfoMap.put("auditState",busPlanInfoWfMap.get("cqResult"));
			str=busPlanInfoWfMap.get("cqResult").equals("0")?"不同意":"同意";
			remark="厂区领导："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+busPlanInfoWfMap.get("cqRemark")+"；结果为："+str+"";		
			//总公司
			if("0".equals(busPlanInfoWfMap.get("cqResult"))){
				orgId = orgCode;
				postCode="车间材料员";
				spresult = "0";
			}else{
				orgId =orgCodes[0];
				postCode="生产主管";
				spresult = "1";
			}
		}else if(in.get("type").toString().equals("zr")){
			busPlanInfoMap.put("auditState",busPlanInfoWfMap.get("zrResult"));
			str=busPlanInfoWfMap.get("zrResult").equals("0")?"不同意":"同意";
			remark="车间主任："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+busPlanInfoWfMap.get("zrRemark")+"；结果为："+str+"";	
			if("0".equals(busPlanInfoWfMap.get("zrResult"))){
				orgId = orgCode;
				postCode="车间材料员";
				spresult = "0";
			}else{
				orgId =orgCodes[0]+"."+orgCodes[1]+"."+orgCodes[2];
				postCode="厂材料员";
				spresult = "1";
			}
		}else if(in.get("type").toString().equals("ccl")){//新增厂区领导审批前-厂材料员审批
			busPlanInfoMap.put("auditState",busPlanInfoWfMap.get("cclResult"));
			str=busPlanInfoWfMap.get("cclResult").equals("0")?"不同意":"同意";
			remark="厂材料员："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+busPlanInfoWfMap.get("cclRemark")+"；结果为："+str+"。";		
			//总公司
			if("0".equals(busPlanInfoWfMap.get("cclResult"))){
				orgId = orgCode;
				postCode="车间材料员";
				spresult = "0";
			}else{
				orgId =orgCodes[0];
				postCode="厂区领导";
				spresult = "1";
			}
		}else if(in.get("type").toString().equals("sc")){
			busPlanInfoMap.put("auditState",busPlanInfoWfMap.get("scResult"));
			str=busPlanInfoWfMap.get("scResult").equals("0")?"不同意":"同意";
			remark="生产主管："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+busPlanInfoWfMap.get("scRemark")+"；结果为："+str+"";		
			//总公司
			if("0".equals(busPlanInfoWfMap.get("scResult"))){
				orgId =orgCode;
				postCode="车间材料员";
				spresult = "0";
			}else{
				orgId =orgCodes[0];
				postCode="经营主管";
				spresult = "1";
			}
		}else if(in.get("type").toString().equals("jy")){
			busPlanInfoMap.put("auditState",busPlanInfoWfMap.get("jyResult"));
			str=busPlanInfoWfMap.get("jyResult").equals("0")?"不同意":"同意";
			remark="经营主管："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+busPlanInfoWfMap.get("jyRemark")+"；结果为："+str+"";		
			//总公司
			if("0".equals(busPlanInfoWfMap.get("jyResult"))){
				orgId = orgCode;
				postCode="车间材料员";
				spresult = "0";
			}else{
				orgId =orgCodes[0];
				postCode="经理";
				spresult = "1";
			}
		}else if(in.get("type").toString().equals("zgs")){
			busPlanInfoMap.put("auditState",busPlanInfoWfMap.get("zgsResult"));
			str=busPlanInfoWfMap.get("zgsResult").equals("0")?"不同意":"同意";
			//总公司
			remark="总公司领导："+userMap.get("name")+"在"+date_str+"审批了物料计划，意见为："+busPlanInfoWfMap.get("zgsRemark")+"；结果为："+str+"";	
			if("0".equals(busPlanInfoWfMap.get("zgsResult"))){
				//删除不通过的附件信息
				IMap attachmentMap =BeanFactory.getClassBean("com.AttachmentInfo");
				attachmentMap.put("busId",busPlanInfoWfMap.get("id"));
				attachmentMap.put("busName","com.BusPlanInfoWf");
				//状态2为审批上传文件
				attachmentMap.put("busType","2");
				attachmentMap.put("isValid","1");
				List fileList = db.getList(attachmentMap, null);
				for(int a=0;a<fileList.size();a++){
				IMap  attachmentInfo= (IMap)fileList.get(a);
				attachmentInfo.put("isValid",Constants.ISNOTVALID);
				db.update(attachmentInfo);
				};
				orgId = orgCode;
				postCode="车间材料员";
				spresult = "0";
			}else{
				//厂材料员确认
				orgId = orgCode;
				postCode="车间材料员";
				spresult = "1";
			}
			//上传附件
			List attachmentInfoList=(List) in.get("attachmentInfo");
			for(int i=0;i<attachmentInfoList.size();i++){
				IMap  attachmentInfoMap= (IMap)attachmentInfoList.get(i);
				if(attachmentInfoMap.get("docUrl")!=null&!"".equals(attachmentInfoMap.get("docUrl"))){
					attachmentInfoMap.put("busId",busPlanInfoWfMap.get("id"));
					attachmentInfoMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
							.getPackageName(), "com.AttachmentInfo"));
					attachmentInfoMap.put("isValid",Constants.ISVALID);
					attachmentInfoMap.put("busName","com.BusPlanInfoWf");
					attachmentInfoMap.put("busType",1);
					//为创建数据赋值
					HelperDB.setCreateInfo(HelperApp.getUserName(userMap),attachmentInfoMap);
					db.insert(attachmentInfoMap);
				}
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
		 logb=new LogBean(userMap,busPlanInfoWfMap.get("Id").toString(), "com.BusPlanInfoWf","",remark,"1",docId);
		}else{
		 logb=new LogBean(userMap,busPlanInfoWfMap.get("Id").toString(), "com.BusPlanInfoWf","",remark,"1");
		}
		LogRecordsService.saveOperationLog(logb,db);
		//工作流信息
		IMap wfmap = new DataMap();
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
		if(!"".equals(actionId)&&actionId!=null){
			FireNetHelper.wf_next(db, userMap, instanceId, stepId, nodeId,actionId,wfmap);
		}else{
			FireNetHelper.wf_next(db, userMap, instanceId, stepId, nodeId,wfmap);
		}
		
		result.put("method.infoMsg", "审批成功！");
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 获取审批物料计划信息，跳转上报器材库
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Aug 12, 2013 2:11:40 PM
	 */
	public IMap getBusPlanInfoDetailList(IMap in){
		IMap result = new DataMap();
		//当前计划
		IMap busPlanInfoMap = BeanFactory.getClassBean("com.BusPlanInfoWf");
		busPlanInfoMap.put("id",in.get("id"));
		busPlanInfoMap = db.get(busPlanInfoMap);
		//材料申请信息
		IMap busMaterialPlanMap =BeanFactory.getClassBean("com.BusMaterialPlanWf");
		busMaterialPlanMap.put("planId",busPlanInfoMap.get("id"));
	    //type 1：领用 2：加工 3维修
		busMaterialPlanMap.put("type","1");
		//获取当前审批的材料申请列表
		List busMaterialPlanList = db.getList(busMaterialPlanMap, null);
		//返回busMetarialPlanList,busId
		result.put("busMaterialPlanList",busMaterialPlanList);
		result.put("busId",in.get("id"));
		result.put("stepId", in.get("stepId"));
		result.put("nodeId", in.get("nodeId"));
		result.put("orgCode", busPlanInfoMap.get("orgId"));
		result.put("instanceId", in.get("instanceId"));
		return result;
	}
	/**
	 * 上报器材库
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Aug 12, 2013 2:12:12 PM
	 */
	public IMap modifyBusPlanInfoWfReport(IMap in){
		IMap result = new DataMap();
		IMap userMap = (IMap) in.get("userMap");
		List busMaterialPlanList = (List) in.get("busMaterialPlanWf");
		for (int i = 0; i < busMaterialPlanList.size(); i++) {
			//保存busMaterialPlan
			IMap busMaterialPlan = BeanFactory.getClassBean("com.BusMaterialPlanWf");
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
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String remark="材料员："+userMap.get("name")+"在"+sdf.format(new Date())+"上报器材库。";
		LogBean logb=new LogBean(userMap,(String)in.get("id"), "com.BusPlanInfoWf","",remark, "0");
		LogRecordsService.saveOperationLog(logb,db);
		result.put("method.infoMsg", "确认成功！");
		result.put("method.url", in.get("url"));
		return result;
	}
	
	/**
	 * 器材库确认
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Jul 19, 2013 9:38:03 AM
	 */
	public IMap saveMaterialConfirm(IMap in){
		IMap result = new DataMap();
		IMap userMap = (IMap) in.get("userMap");
		
		IMap wfmap = new DataMap();
		String instanceId = (String)in.get("instanceId");
		String stepId = (String)in.get("stepId");
		String nodeId = (String)in.get("nodeId");
		
		FireNetHelper.wf_next(db,userMap, instanceId, stepId, nodeId, wfmap);
		result.put("method.infoMsg","确认完成!");
		result.put("method.url", in.get("url"));
		return result;
	}
	
	/**
	 * 材料申请列表页面
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author 孟勃婷
	 * @date Aug 8, 2013 10:01:21 AM
	 */
	public IMap<String, Object> getBusPlanInfoWfList(IMap<String, Object> in) {
		// 设置结果集
		IMap result = new DataMap();
		//session当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		//分页参数
		Integer currentPageNO = (String) in.get("currentPageNO")==null?1:Integer.parseInt((String) in.get("currentPageNO"));
		Integer perCount =(String) in.get("perCount")==null?10:Integer.parseInt((String) in.get("perCount"));
		//按照创建日期进行排序
		OrderBean orderBean = new OrderBean();
		orderBean.put("createDate",OrderBean.DESC);
		//查询限制，当前用户orgId
		in.put("orgId", userMap.get("orgId"));
		//未通过审核
		//in.put("auditState",0);
		//查询分页结果
		Page page = db.pageQuery(in,"queryBusPlanInfoWfList","com.BusPlanInfoWf","ID",currentPageNO,perCount,orderBean);
		//回写
		page.setAction(request);
		result.put("page", page);
		return result;
	}
	
	/**
	 * 物料申请报表
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date 2013-9-26 下午01:33:14
	 */
	public IMap queryMaterialWfReport(IMap in){
		IMap result = new DataMap();
		String factory = (String)in.get("factory");
		result.put("factory",factory);
		result.put("searchDate",in.get("searchDate"));
		if("1".equals(factory)){
			factory ="|CQ|.|JX|.|YC|";
		}else{
			factory ="|CQ|.|JX|.|QY|";
		}
		//分页参数
		IMap con = new DataMap();
		con.put("searchDate",((String)in.get("searchDate")).replace("-",""));
		con.put("orgId",factory);
		result.put("orgId", factory);
		List materialWfReportList = db.getList(con,"queryMaterialWfReport",null);
		result.put("materialWfReportList", materialWfReportList);
		return result;
	}
	/**
	 * 大件物料申请报表
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date 2013-9-26 下午01:33:14
	 */
	public IMap queryMaterialDjReport(IMap in){
		IMap result = new DataMap();
		String factory = (String)in.get("factory");
		result.put("factory",factory);
		result.put("searchDate",in.get("searchDate"));
		if("1".equals(factory)){
			factory ="|CQ|.|JX|.|YC|";
		}else{
			factory ="|CQ|.|JX|.|QY|";
		}
		//分页参数
		IMap con = new DataMap();
		con.put("searchDate",((String)in.get("searchDate")).replace("-",""));
		con.put("orgId",factory);
		result.put("orgId", factory);
		List materialWfReportList = db.getList(con,"queryMaterialDjReport",null);
		result.put("materialWfReportList", materialWfReportList);
		return result;
	}
	/**
	 * 导出物料申请报表
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date 2013-9-26 下午05:41:04
	 */
	public IMap importMaterialWfReport(IMap in){
		IMap result = new DataMap();

		result.put("searchDate",in.get("searchDate"));
		String orgName = "";

		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) userMap.get("orgid");
		String factory = (String) in.get("factory");
		IMap con = new DataMap();
		String factoryName = "";
		if(orgId.indexOf(".")>1){
			//如果大于1 则为厂区及以下查询 orgCode直接采用登录人Code
			con.put("orgId", orgId);
		}else{
			//否则根据查询条件不同，值不同
			if("1".equals(factory)){
				//银川
				con.put("orgId", "|CQ|.|JX|.|YC|");
				factoryName="银川厂";
			}else if("2".equals(factory)){
				//庆阳
				con.put("orgId", "|CQ|.|JX|.|QY|");
				factoryName="庆阳厂";
			}else{
				//所有
				con.put("orgId", orgId);
			}
		}
		
		
		con.put("searchDate",((String)in.get("searchDate")).replace("-",""));
		
		List<IMap> materialWfReportList = db.getList(con,"queryMaterialWfReport",null);
		

		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = "机修公司"+factoryName+"材料计划表.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(os);
			
			WritableSheet ws  =  book.createSheet("机修公司"+factoryName+"材料计划表" ,  0);
			
			WritableCellFormat format1 = new WritableCellFormat();
			WritableCellFormat format2 = new WritableCellFormat();
			WritableCellFormat format3 = new WritableCellFormat();
			//设置数字格式
			jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00");    //设置数字格式
			jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf);
			wcfN.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			jxl.write.NumberFormat nf1 = new jxl.write.NumberFormat("0");    //设置数字格式
			jxl.write.WritableCellFormat wcfNint = new jxl.write.WritableCellFormat(nf1);
			wcfNint.setAlignment(jxl.format.Alignment.CENTRE);
			wcfNint.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			// 单元格居中
			format1.setAlignment(jxl.format.Alignment.CENTRE);
			format3.setAlignment(jxl.format.Alignment.LEFT);
			format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			format1.setWrap(true);
			//设置表头
			 WritableFont font = new WritableFont(WritableFont.createFont("宋体"),  
                     12,   
                     WritableFont.BOLD,   
                     false,  
                     UnderlineStyle.NO_UNDERLINE); 
			format2.setAlignment(jxl.format.Alignment.CENTRE);
			format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format2.setFont(font);
			// 设置字体
			jxl.write.WritableFont wf = new jxl.write.WritableFont(
					WritableFont.COURIER, 30, WritableFont.BOLD, false);
			jxl.write.WritableCellFormat wcfF = new jxl.write.WritableCellFormat(
					wf);
			jxl.write.WritableFont redWord = new jxl.write.WritableFont(
					WritableFont.COURIER, 10, WritableFont.BOLD, false,
					jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.RED);
			jxl.write.WritableCellFormat redWordRow = new jxl.write.WritableCellFormat(
					redWord);
			redWordRow.setAlignment(jxl.format.Alignment.CENTRE);
			wcfF.setAlignment(jxl.format.Alignment.CENTRE);
			
			//日期
			 jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-dd-MM"); 
		     jxl.write.WritableCellFormat wcfdate = new jxl.write.WritableCellFormat(df); 
			
			//添加标题行
			ws.mergeCells(0, 0, 9, 1);
			
			
			//列宽
			ws.setColumnView(0, 15);
			ws.setColumnView(1, 15);
			ws.setColumnView(2, 10);
			ws.setColumnView(3, 10);
			ws.setColumnView(4, 10);
			ws.setColumnView(5, 10);
			ws.setColumnView(6, 10);
			ws.setColumnView(7, 10);
			ws.setColumnView(8, 10);
			ws.setColumnView(9, 10);
			ws.setColumnView(10, 10);
			ws.setColumnView(11, 10);
			ws.setColumnView(12, 10);
			ws.setColumnView(13, 10);
			ws.setColumnView(14, 10);
			ws.setColumnView(15, 10);
			ws.setColumnView(16, 10);
			ws.setColumnView(17, 10);
			ws.setColumnView(18, 10);
			ws.setColumnView(19, 10);
			ws.setColumnView(20, 10);
			ws.setColumnView(21, 10);
			ws.setColumnView(22, 10);
			ws.setColumnView(23, 10);
			ws.setColumnView(24, 10);
			ws.setColumnView(25, 10);
			ws.setColumnView(26, 10);
			ws.setColumnView(27, 10);
			ws.setColumnView(28, 10);
			ws.setColumnView(38, 23);
			ws.setColumnView(39, 23);
			
			
			ws.addCell(new jxl.write.Label(0, 0, "机修公司"+factoryName+"材料计划表", format2));
			ws.addCell(new jxl.write.Label(0, 2, "厂区", format1));
			ws.addCell(new jxl.write.Label(1, 2, "车间", format1));
			ws.addCell(new jxl.write.Label(2, 2, "物资编码", format1));
			ws.addCell(new jxl.write.Label(3, 2, "规格名称及型号", format1));
			ws.addCell(new jxl.write.Label(4, 2, "单位", format1));
			ws.addCell(new jxl.write.Label(5, 2, "单价", format1));
			ws.addCell(new jxl.write.Label(6, 2, "申请数量", format1));
			ws.addCell(new jxl.write.Label(7, 2, "领用数量", format1));
			ws.addCell(new jxl.write.Label(8, 2, "金额", format1));
			ws.addCell(new jxl.write.Label(9, 2, "备注", format1));
			
			
			for (int i = 0; i < materialWfReportList.size(); i++) {
				IMap map =  materialWfReportList.get(i);
				
				ws.addCell(new jxl.write.Label(0, i+3, (String)map.get("parentOrgName"), format1));
				ws.addCell(new jxl.write.Label(1, i+3, (String)map.get("orgName"), format1));
				ws.addCell(new jxl.write.Label(2, i+3, (String)map.get("materialCode"), format1));
				ws.addCell(new jxl.write.Label(3, i+3, (String)map.get("materialDetail"), format1));
				ws.addCell(new jxl.write.Label(4, i+3, (String)map.get("unity"), format1));
				ws.addCell(new jxl.write.Number(5, i+3, (Double)map.get("estimatePrice"), wcfN));
				ws.addCell(new jxl.write.Number(6, i+3, (Double)map.get("materialNum"), wcfNint));
				ws.addCell(new jxl.write.Number(7, i+3, (Double)map.get("realNum"), wcfNint));
				ws.addCell(new jxl.write.Number(8, i+3, (Double)map.get("totalPrice"), wcfN));
				ws.addCell(new jxl.write.Label(9, i+3, (String)map.get("remark"), format1));
			}

			
			book.write();
            book.close(); 
            os.close();
            response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return result;
	}
	/**
	 * 导出大件物料申请报表
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date 2013-9-26 下午05:41:04
	 */
	public IMap importMaterialDjReport(IMap in){
		IMap result = new DataMap();

		result.put("searchDate",in.get("searchDate"));
		String orgName = "";

		IMap userMap = (IMap) in.get("userMap");
		String orgId = (String) userMap.get("orgid");
		String factory = (String) in.get("factory");
		IMap con = new DataMap();
		String factoryName = "";
		if(orgId.indexOf(".")>1){
			//如果大于1 则为厂区及以下查询 orgCode直接采用登录人Code
			con.put("orgId", orgId);
		}else{
			//否则根据查询条件不同，值不同
			if("1".equals(factory)){
				//银川
				con.put("orgId", "|CQ|.|JX|.|YC|");
				factoryName="银川厂";
			}else if("2".equals(factory)){
				//庆阳
				con.put("orgId", "|CQ|.|JX|.|QY|");
				factoryName="庆阳厂";
			}else{
				//所有
				con.put("orgId", orgId);
			}
		}
		
		
		con.put("searchDate",((String)in.get("searchDate")).replace("-",""));
		
		List<IMap> materialWfReportList = db.getList(con,"queryMaterialDjReport",null);
		

		try {
			HttpServletResponse response = (HttpServletResponse) in.get("response"); 
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String filename = "机修公司"+factoryName+"大件审批表.xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""+ new String(filename.getBytes(),"iso8859-1")+   "\"");
			
			OutputStream os = response.getOutputStream();
			WritableWorkbook book = Workbook.createWorkbook(os);
			
			WritableSheet ws  =  book.createSheet("机修公司"+factoryName+"大件审批表" ,  0);
			
			WritableCellFormat format1 = new WritableCellFormat();
			WritableCellFormat format2 = new WritableCellFormat();
			WritableCellFormat format3 = new WritableCellFormat();
			//设置数字格式
			jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00");    //设置数字格式
			jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf);
			wcfN.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			jxl.write.NumberFormat nf1 = new jxl.write.NumberFormat("0");    //设置数字格式
			jxl.write.WritableCellFormat wcfNint = new jxl.write.WritableCellFormat(nf1);
			wcfNint.setAlignment(jxl.format.Alignment.CENTRE);
			wcfNint.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			// 单元格居中
			format1.setAlignment(jxl.format.Alignment.CENTRE);
			format3.setAlignment(jxl.format.Alignment.LEFT);
			format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format1.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			format1.setWrap(true);
			//设置表头
			 WritableFont font = new WritableFont(WritableFont.createFont("宋体"),  
                     12,   
                     WritableFont.BOLD,   
                     false,  
                     UnderlineStyle.NO_UNDERLINE); 
			format2.setAlignment(jxl.format.Alignment.CENTRE);
			format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			format2.setFont(font);
			// 设置字体
			jxl.write.WritableFont wf = new jxl.write.WritableFont(
					WritableFont.COURIER, 30, WritableFont.BOLD, false);
			jxl.write.WritableCellFormat wcfF = new jxl.write.WritableCellFormat(
					wf);
			jxl.write.WritableFont redWord = new jxl.write.WritableFont(
					WritableFont.COURIER, 10, WritableFont.BOLD, false,
					jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.RED);
			jxl.write.WritableCellFormat redWordRow = new jxl.write.WritableCellFormat(
					redWord);
			redWordRow.setAlignment(jxl.format.Alignment.CENTRE);
			wcfF.setAlignment(jxl.format.Alignment.CENTRE);
			
			//日期
			 jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-dd-MM"); 
		     jxl.write.WritableCellFormat wcfdate = new jxl.write.WritableCellFormat(df); 
			
			//添加标题行
			ws.mergeCells(0, 0, 9, 1);
			
			
			//列宽
			ws.setColumnView(0, 15);
			ws.setColumnView(1, 15);
			ws.setColumnView(2, 10);
			ws.setColumnView(3, 10);
			ws.setColumnView(4, 10);
			ws.setColumnView(5, 10);
			ws.setColumnView(6, 10);
			ws.setColumnView(7, 10);
			ws.setColumnView(8, 10);
			ws.setColumnView(9, 10);
			ws.setColumnView(10, 10);
			ws.setColumnView(11, 10);
			ws.setColumnView(12, 10);
			ws.setColumnView(13, 10);
			ws.setColumnView(14, 10);
			ws.setColumnView(15, 10);
			ws.setColumnView(16, 10);
			ws.setColumnView(17, 10);
			ws.setColumnView(18, 10);
			ws.setColumnView(19, 10);
			ws.setColumnView(20, 10);
			ws.setColumnView(21, 10);
			ws.setColumnView(22, 10);
			ws.setColumnView(23, 10);
			ws.setColumnView(24, 10);
			ws.setColumnView(25, 10);
			ws.setColumnView(26, 10);
			ws.setColumnView(27, 10);
			ws.setColumnView(28, 10);
			ws.setColumnView(38, 23);
			ws.setColumnView(39, 23);
			
			
			ws.addCell(new jxl.write.Label(0, 0, "机修公司"+factoryName+"大件审批表", format2));
			ws.addCell(new jxl.write.Label(0, 2, "厂区", format1));
			ws.addCell(new jxl.write.Label(1, 2, "车间", format1));
			ws.addCell(new jxl.write.Label(2, 2, "物资编码", format1));
			ws.addCell(new jxl.write.Label(3, 2, "规格名称及型号", format1));
			ws.addCell(new jxl.write.Label(4, 2, "单位", format1));
			ws.addCell(new jxl.write.Label(5, 2, "单价", format1));
			ws.addCell(new jxl.write.Label(6, 2, "申请数量", format1));
			ws.addCell(new jxl.write.Label(7, 2, "领用数量", format1));
			ws.addCell(new jxl.write.Label(8, 2, "金额", format1));
			ws.addCell(new jxl.write.Label(9, 2, "备注", format1));
			
			
			for (int i = 0; i < materialWfReportList.size(); i++) {
				IMap map =  materialWfReportList.get(i);
				
				ws.addCell(new jxl.write.Label(0, i+3, (String)map.get("parentOrgName"), format1));
				ws.addCell(new jxl.write.Label(1, i+3, (String)map.get("orgName"), format1));
				ws.addCell(new jxl.write.Label(2, i+3, (String)map.get("materialCode"), format1));
				ws.addCell(new jxl.write.Label(3, i+3, (String)map.get("materialDetail"), format1));
				ws.addCell(new jxl.write.Label(4, i+3, (String)map.get("unity"), format1));
				ws.addCell(new jxl.write.Number(5, i+3, (Double)map.get("estimatePrice"), wcfN));
				ws.addCell(new jxl.write.Number(6, i+3, (Double)map.get("materialNum"), wcfNint));
				ws.addCell(new jxl.write.Number(7, i+3, (Double)map.get("realNum"), wcfNint));
				ws.addCell(new jxl.write.Number(8, i+3, (Double)map.get("totalPrice"), wcfN));
				ws.addCell(new jxl.write.Label(9, i+3, (String)map.get("remark"), format1));
			}

			
			book.write();
            book.close(); 
            os.close();
            response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return result;
	}
}
