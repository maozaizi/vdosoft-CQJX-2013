package com.u2a.business;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.brick.api.Service;
import com.brick.dao.Page;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.exception.BusinessException;
import com.brick.manager.BeanFactory;
import com.brick.util.PageUtils;
import com.brick.util.Util;

/**
 * 系统名称：长庆钻井机修公司 - 设备维修管理系统   
 * 类名称：MyWorkService   
 * 类描述：   
 * 创建人：孟勃婷 
 * 创建时间：Jul 19, 2013 4:30:34 PM
 */
@SuppressWarnings("unchecked")
public class MyWorkService extends Service {

	/**
	 * 我的工作台
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author 孟勃婷
	 * @date Jul 19, 2013 4:33:19 PM
	 */
	public IMap getMyWork(IMap in){
		IMap result = new DataMap();
		//当前登录用户
		IMap userMap = (IMap) in.get("userMap");
		return result;
	}

	/**
	 * 根据用户获取我的工作台
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author gaoy
	 * @date Apr 10, 2012 7:58:31 PM
	 */
	public IMap<String,Object> getMyWorkUserList(IMap<String,Object> in){
		IMap userMap = (IMap) in.get("userMap");
		String userId = (String)userMap.get("userId");
		String state = (String)in.get("state");
		
		IMap  bizInstance = new DataMap();
		bizInstance.put("userId", userId);	
		bizInstance.put("state", "0");
		List<IMap> waitingBizStepList = db.getList(bizInstance,"getWorkUserInfo","");
		
		bizInstance.put("state", "1");
		List<IMap> alreadyBizStepList = db.getList(bizInstance,"getWorkUserInfo","");
		
		List<IMap> completeBizStepList = db.getList(bizInstance,"getcompleteBizStepList","");
		
		IMap<String,Object> result = new DataMap<String,Object>();//输出map
		result.put("waitingBizStepList", waitingBizStepList);
		result.put("alreadyBizStepList", alreadyBizStepList);
		result.put("completeBizStepList", completeBizStepList);
		return result;
	}
	@SuppressWarnings("unchecked")
	public IMap getAllCompleteWorkFlow(IMap in){
		IMap userMap = (IMap) in.get("userMap");
		IMap result = new DataMap();
		IMap user = BeanFactory.getClassBean("com.UserInfo");
		user.put("userId", userMap.get("userId"));
		Page page = db.pageQuery(user, "getcompleteBizStepList", "","objId",in.get("currentPageNo") == null ? 1:Integer.parseInt(in.get("currentPageNo").toString()), 10, null);
		page.setAction(PageUtils.getPagePath((HttpServletRequest) in
				.get("request"), new String[] { "url" }));
		result.put("page", page);
		return result;
	}
	/**
	 * 我要处理修改步骤状态
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author gaoy
	 * @date Apr 12, 2012 5:09:20 PM
	 */
	public IMap<String,Object> updateState(IMap<String,Object> in){
		String nodeId = (String)in.get("nodeId");
		IMap  bizStep = BeanFactory.getClassBean("com.WfBizStep");
		bizStep.put("id", nodeId);
		// 0待执行1执行中2已完成
		bizStep.put("state", "1");
		Integer rowBizStep = (Integer) db.update(bizStep,"updateBizStepState");
		if (rowBizStep == 0) {
			throw new BusinessException("该流程步骤已经被别人处理！");
		}
		String bsuId = (String)in.get("bsuId");
		IMap  authorizedRelation = BeanFactory.getClassBean("com.WfBizStepUser");
		authorizedRelation.put("id", bsuId);
		// 0待执行1执行中2已完成
		authorizedRelation.put("stepState", "1");
		Integer rowBizStepUser = (Integer) db.update(authorizedRelation,"updateBizStepUserState");
		if (rowBizStepUser == 0) {
			throw new BusinessException("该流程步骤已经被别人处理！");
			}
		IMap<String,Object> result = new DataMap<String,Object>();//输出map
		 result.put("method.url", in.get("strurl"));
		 result.put("method.infoMsg", "我要处理成功！");
	return result;
	}
	/**
	 * 跳转至执行流程步骤页面
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap<String,Object>   
	 * @author gaoy
	 * @date Apr 12, 2012 10:07:44 PM
	 */
	public IMap<String,Object> goExec(IMap<String,Object> in){
		// 工作流实例ID    
		String instanceId = (String)in.get("instanceId");
		// 检查流程状态
		checkState(instanceId);
		IMap userMap = (IMap) in.get("userMap");
		String userId = (String)userMap.get("userId");
		// 流程Id
		String processId = (String)in.get("processId");
		// 步骤              
		String stepId = (String)in.get("stepId");
		// 根据流程ID、步骤ID获取跳转页面 
//		StepDescriptor step=WfHelper.getStepDescriptor(processId,userId,Integer.valueOf(stepId));
//		String desUrl = (String)step.getMetaAttributes().get("url");
		
//		IMap<String, Object> processStepBean = BeanFactory.getClassBean("com.WfProcessStep");
//		processStepBean.put("processId", processId);
//		processStepBean.put("stepId", stepId);
//		IMap<String, Object> processStepMap = db.get(processStepBean);
		
		IMap  bizStep = BeanFactory.getClassBean("com.WfBizStep");
		bizStep.put("insid", instanceId);
		bizStep.put("stepId", stepId);
		// 0待执行1执行中2已完成
		bizStep.put("state", "1");
		IMap<String, Object> bizStepMap = db.get(bizStep);
		
		// 进入步骤页面url
		String editUrl = (String)bizStepMap.get("editUrl");
		if (Util.checkNull(editUrl)) {
			throw new BusinessException("请在流程管理配置进入步骤页面Url");
		}
		String viewUrl = (String)bizStepMap.get("viewUrl");
		// 业务ID             
		String objId = (String)in.get("objId");
		// 环节               
		String nodeId = (String)in.get("nodeId");
		// 表名            
		String tableName = (String)in.get("tableName");
		// 标题	          
		String contTitle = (String)in.get("contTitle");
		// 链接		            
		String url = (String)in.get("url");
		// 流程类型	    
		String processType = (String)in.get("processType");
		// 步骤名称	       
		String stepName = (String)in.get("stepName");
		IMap<String,Object> result = new DataMap<String,Object>();//输出map
		result.put("instanceId", Long.valueOf(instanceId));
		result.put("objId", objId);
		result.put("nodeId", Long.valueOf(nodeId));
		result.put("stepId", Integer.valueOf(stepId));
		result.put("tableName", tableName);
		result.put("contTitle", contTitle);
		result.put("url", url);
		result.put("processType", Integer.valueOf(processType));
		result.put("stepName", stepName);
		// ContextUtil.servletContext.getContextPath()+
		//result.put("method.url", desUrl);
		result.put("method.forward",editUrl);
		return result;
		
	}
	/**
	 * 检查流程状态
	 * @Description 
	 * @param @param instanceId    
	 * @return void   
	 * @author gaoy
	 * @date Apr 19, 2012 9:35:50 PM
	 */
	public void checkState(String instanceId){
		IMap  bizInstance = BeanFactory.getClassBean("com.WfBizInstance");
		bizInstance.put("id", instanceId);
		IMap map = db.get(bizInstance);
		int state = Integer.parseInt(map.get("state").toString());
		String strstate = "";
		if(state==0){
			strstate = "未执行";
		}else if(state==2){
			strstate = "暂停";
		}else if(state==3){
			strstate = "无效";
		}else if(state==4){
			strstate = "完成";
		}else if(state==5){
			strstate = "未知";
		}
		if (state!=1) {
			throw new BusinessException("该流程实例"+instanceId+"已被位置"+strstate+"状态！");
		}
	}
}
