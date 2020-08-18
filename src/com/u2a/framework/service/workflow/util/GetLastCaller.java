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

public class GetLastCaller implements FunctionProvider {
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
		// 获取实例任务最后一环节
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
		// 获取第一条实例任务
		IMap bizStepMap = wFBizStepList.get(0);
		// 获取实例任务用户
		IMap wfBizStepUser = BeanFactory.getClassBean("com.WfBizStepUser");	
		wfBizStepUser.put("stepid", bizStepMap.get("id"));
		// 步骤状态0待执行1执行中
		wfBizStepUser.put("stepstate","1");
		List<IMap> wfBizStepUserList = db.getList(wfBizStepUser,null);
		IMap wfUser=new DataMap();
		List list = new ArrayList();
		for (IMap map : wfBizStepUserList) {
			list.add(map.get("userid"));
			wfUser.put("u", list);
		}
		JSONObject ownerUser =JSONObject.fromObject(wfUser);
        transientVars.put("caller", ownerUser);
	}

}
