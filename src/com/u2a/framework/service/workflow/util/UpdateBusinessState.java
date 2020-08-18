package com.u2a.framework.service.workflow.util;


import java.util.Map;

import com.brick.dao.IBaseDAO;
import com.brick.data.IMap;
import com.brick.manager.BeanFactory;
import com.brick.util.Util;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.spi.WorkflowEntry;

public class UpdateBusinessState implements FunctionProvider {
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
		IMap wFBizStep = BeanFactory.getClassBean("com.WFBizInstance");	
		wFBizStep.put("id", entry.getId());
		// 0创建1执行中2暂停3无效4完成-1未知
		wFBizStep.put("state","1");
		IMap bizStepMap = db.get(wFBizStep);
		String tableName = (String)bizStepMap.get("tableName");
		String objIid = (String)bizStepMap.get("objId");
		
		IMap designAuditing = BeanFactory.getClassBean(tableName);	
		designAuditing.put("id", objIid);
		String state=(String) transientVars.get("state");
		if(Util.checkNull(state)){
			designAuditing.put("state", "2");
		}else{
			designAuditing.put("state", state);
		}
		
		db.update(designAuditing);
	}

}
