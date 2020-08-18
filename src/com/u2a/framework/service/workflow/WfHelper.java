package com.u2a.framework.service.workflow;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.brick.dao.IBaseDAO;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.exception.BusinessException;
import com.brick.manager.BeanFactory;
import com.brick.manager.ContextUtil;
import com.brick.util.Util;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.StoreException;
import com.opensymphony.workflow.TypeResolver;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.workflow.config.Configuration;
import com.opensymphony.workflow.config.SpringConfiguration;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.util.SpringTypeResolver;
import com.u2a.framework.commons.Constants;

public class WfHelper {
	public static String code2ID(IBaseDAO ds,String code, String orgId) {
//		IMap<String, Object> orgDetail = BeanFactory
//				.getClassBean("com.organizationdetali");
//		orgDetail.put("organizationDetailId", orgId);
//		orgDetail.put("isValid", Constants.ISVALID);
//		orgDetail = ds.get(orgDetail);
		
		IMap mWorkFlow = BeanFactory.getClassBean("com.WfProcessDef");
		mWorkFlow.put("processCode", code);
//		String baseOrganizationId = (String) orgDetail.get("baseOrganizationId");
//		String[] arr = baseOrganizationId.replace('.', ',').split(",");
//		String scope="";
//		switch (arr.length) {
//		case 1:
//			scope="isAuthorizedBt";
//			break;
//		case 2:
//			scope="isAuthorizedSb";
//			break;
//		case 3:
//			scope="isAuthorizedKq";
//			break;
//		case 4:
//			scope="isAuthorizedTc";
//			break;
//		default:
//			break;
//		}
//		if (Util.checkNotNull(scope)){
//			mWorkFlow.put(scope, 1);
//		}
		
		mWorkFlow.put("processCode", code);
		mWorkFlow.put("isValid", Constants.ISVALID);
		IMap myWorkFlow = ds.get(mWorkFlow);
		if (Util.checkNull(myWorkFlow)){
//			if (Util.checkNotNull(scope)){
//			mWorkFlow.remove(scope);
//			}
//			mWorkFlow.put("isAuthorizedQt", 1);
			myWorkFlow = ds.get(mWorkFlow);
		}
		if (Util.checkNull(myWorkFlow)){
			throw new BusinessException("找不到对应流程code:"+code);
		}
		return (String) myWorkFlow.get("processId");
		
	}
	// 流程启动
	public static long start(IBaseDAO db,String processId,String userId,IMap in){
		if (Util.checkNull(processId)) {
			throw new BusinessException("没有流程id");
		}
		String pname=code2ID(db,processId,(String) in.get("orgId"));
		Workflow workflow= getWorkflow(userId);
		WorkflowDescriptor wd = workflow.getWorkflowDescriptor(
				pname);
		List list = wd.getInitialActions();
		ActionDescriptor ad = (ActionDescriptor) list.get(0);
		
		long workflowId;
		try {
			workflowId = workflow.initialize(pname,
					ad.getId(), in);
		} catch (Exception e) {
			throw new BusinessException("启动流程错误" + processId, e);
		}		
		return workflowId;
	}

	// 流程流转下一步
	public static void next(long processInstanceId,int actionId,String userId,IMap in,IBaseDAO db,long nodeId){
			// 检查流程状态
			IMap  bizInstance = BeanFactory.getClassBean("com.WfBizInstance");
			bizInstance.put("id", processInstanceId);
			IMap insMap = db.get(bizInstance);
			Integer insState = (Integer)insMap.get("state");
			String strstate = "";
			if(insState==0){
				strstate = "未执行";
			}else if(insState==2){
				strstate = "暂停";
			}else if(insState==3){
				strstate = "无效";
			}else if(insState==4){
				strstate = "完成";
			}else if(insState==5){
				strstate = "未知";
			}
			if (insState !=1) {
				throw new BusinessException("该流程实例"+processInstanceId+"已被位置"+strstate+"状态！");
			}
			
			//1 根据环节id获取环节信息
			IMap<String, Object> wFBizStep = BeanFactory.getClassBean("com.WFBizStep");
			wFBizStep.put("id", nodeId);
			IMap bizStepMap= db.get(wFBizStep);
			
			//2 更新环节用户为执行过状态
			IMap<String, Object> wFBizStepUser = BeanFactory.getClassBean("com.WFBizStepUser");
			wFBizStepUser.put("stepId", bizStepMap.get("id"));
			wFBizStepUser.put("userid", userId);
			IMap bizStepUserMap= db.get(wFBizStepUser);
			// 0未执行 1已执行
			bizStepUserMap.put("state", "1");
			db.update(bizStepUserMap);
			// 0普通1会签
			String stepType = (String)bizStepMap.get("stepType");
			//3 判断是否是会签，不是，doaction
			//4 是会签，判断是否执行完，完成， doaction,未完成 返回
			if(stepType.equals("1")){
				wFBizStepUser = BeanFactory.getClassBean("com.WFBizStepUser");
				wFBizStepUser.put("stepId", bizStepMap.get("id"));
				List<IMap> bizStepUserList = db.getList(wFBizStepUser,null);
				for(IMap map : bizStepUserList){
					// 0未执行1已执行
					String state = (String)map.get("state");
					if (state.equals("0")) {
						return ;
					}
				}
			}
			try {
				getWorkflow(userId).doAction(processInstanceId, actionId, in);
			} catch (InvalidInputException e) {
				//异常判断InvalidActionException，提示为 该流程已被执行过，请重新从工作台进入重试！
				throw new BusinessException("该流程已被执行过，请重新从工作台进入重试！", e);
			} catch (WorkflowException e) {
				throw new BusinessException("工作流执行错误！", e);
			}
	}

	// 获取当前流程走到第几步
	public static List getCurrStep(long processInstanceId,String usetId){
		List list = getWorkflow(usetId).getCurrentSteps(processInstanceId);
		return list;
	}
	// 获取流程历史步骤
	public static List getHistorySteps(long processInstanceId,String usetId){
		List list = getWorkflow(usetId).getHistorySteps(processInstanceId);
		return list;
	}
	// 获取出参
	public static PropertySet getPropertySet(long processInstanceId,String usetId){
		PropertySet ps=null;
		try {
			ps = getConfiguration().getWorkflowStore().getPropertySet(
					processInstanceId);
		} catch (StoreException e) {
			throw new BusinessException("流程getPropertySet错误" + processInstanceId, e);
		}
		return ps;
	}
	public static Workflow getWorkflow(String userId) {
		BasicWorkflow workflow = new BasicWorkflow(userId);
			workflow.setConfiguration(getConfiguration());
			workflow.setResolver(getTypeResolver());
		return workflow;
	}
	public static TypeResolver getTypeResolver() {
		SpringTypeResolver tr = (SpringTypeResolver) ContextUtil
					.getSpringBean("workflowTypeResolver");
		//tr.setApplicationContext(SpringApplicationContext.getContext());
		return tr;
	}
	public static Configuration getConfiguration() {
		Configuration configuration = (SpringConfiguration) ContextUtil
					.getSpringBean("osworkflowConfiguration");
		return configuration;
	}
	public static StepDescriptor getStepDescriptor(IBaseDAO db,long processInstanceId,String userId,int id){
		WorkflowDescriptor wd=getWorkflowDescriptor(db,processInstanceId,userId);
		return wd.getStep(id);
	}
	public static ActionDescriptor getActionDescriptor(IBaseDAO db,long processInstanceId,String userId,int id){
		WorkflowDescriptor wd=getWorkflowDescriptor(db,processInstanceId,userId);
		return wd.getAction(id);
	}
	public static WorkflowDescriptor getWorkflowDescriptor(IBaseDAO db,long processInstanceId,String userId){
		IMap mWorkFlow = BeanFactory.getClassBean("com.WfBizInstance");
		mWorkFlow.put("id", processInstanceId);
		mWorkFlow = db.get(mWorkFlow);
		String processId=(String) mWorkFlow.get("processId");
		WorkflowDescriptor wd = getWorkflow(userId).getWorkflowDescriptor(
				processId);
		return wd;
	}
	public static StepDescriptor getStepDescriptor(String processId,String userId,int id){
		WorkflowDescriptor wd=getWorkflowDescriptor(processId,userId);
		return wd.getStep(id);
	}
	public static ActionDescriptor getActionDescriptor(String processId,String userId,int id){
		WorkflowDescriptor wd=getWorkflowDescriptor(processId,userId);
		return wd.getAction(id);
	}
	public static WorkflowDescriptor getWorkflowDescriptor(String processId,String userId){
		WorkflowDescriptor wd = getWorkflow(userId).getWorkflowDescriptor(
				processId);
		return wd;
	}

	/**
	 * 获取步骤的可执行action
	 * @param db
	 * @param processInstanceId
	 * @param userId
	 * @param stepId
	 * @return
	 */
	public static List<ActionDescriptor> getActionInfo(IBaseDAO db,long processInstanceId,String userId, int stepId) {
		StepDescriptor s = getStepDescriptor(db,processInstanceId,userId,stepId);
		List<ActionDescriptor> listAction = s.getActions();
		return listAction;
	}
}