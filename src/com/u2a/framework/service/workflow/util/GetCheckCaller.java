package com.u2a.framework.service.workflow.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.brick.dao.IBaseDAO;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.data.OrderBean;
import com.brick.exception.BusinessException;
import com.brick.manager.BeanFactory;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.spi.WorkflowEntry;

@SuppressWarnings("unchecked")  
public class GetCheckCaller implements FunctionProvider {
	protected IBaseDAO db;

	public IBaseDAO getDb() {
		return db;
	}

	public void setDb(IBaseDAO db) {
		this.db = db;
	}

	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		// 获取工作流实例对象
		WorkflowEntry entry = (WorkflowEntry) transientVars.get("entry");
		// 工作流实例
		IMap bizInstance = BeanFactory.getClassBean("com.WFBizInstance");	
		bizInstance.put("id", entry.getId());
		// 0创建1执行中2暂停3无效4完成-1未知
		bizInstance.put("state","1");
		IMap bizInstanceMap = db.get(bizInstance);
		
		// 获取实例任务
		IMap wFBizStep = BeanFactory.getClassBean("com.WFBizStep");	
		wFBizStep.put("insid", entry.getId());
		// 0待执行1执行中2已完成
		wFBizStep.put("state","1");
		// 流程步骤编号
		OrderBean order = new OrderBean();
		order.put("id",order.DESC);
		List<IMap> wFBizStepList = db.getList(wFBizStep,order);
		if (wFBizStepList.isEmpty()) {
			throw new BusinessException("未找到实例任务");
		}
		// 获取第一条实例任务(最后一环节)
		IMap bizStepMap = wFBizStepList.get(0);
		// 任务名称
		String contTitle = (String)bizStepMap.get("contTitle");
		
		// 流程id
		String processId =(String)bizInstanceMap.get("processId");
		IMap processDef = BeanFactory.getClassBean("com.WFProcessDef");
		processDef.put("processId", processId);
		// 流程定义对象
		processDef = db.get(processDef);
		String processCode = (String)processDef.get("processCode");
		// 业务ID
		String businessId = (String)bizInstanceMap.get("objId");
		// 获取操作成员对象(负责人、处理人)
		IMap memeber = BeanFactory.getClassBean("com.OperateMember");
		memeber.put("businessId", businessId);
		memeber.put("isCheck", "1");
		List<IMap> memeberList = db.getList(memeber,"getOperateMember",memeber.getClassName());
		
		IMap wfUser=new DataMap();
		List list = new ArrayList();
//		System.out.println("memeberList=="+memeberList);
		for (IMap memeberMap : memeberList) {
			list.add(memeberMap.get("memberId"));
			wfUser.put("u", list);
			
		}
//		System.out.println("====wfUser=====1===="+wfUser);
		JSONObject ownerUser =JSONObject.fromObject(wfUser);
        transientVars.put("caller", ownerUser);
		
	}

}
