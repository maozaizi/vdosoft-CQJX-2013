package com.u2a.framework.service.workflow.util;

import java.util.Date;
import java.util.Map;

import com.brick.dao.IBaseDAO;
import com.brick.data.IMap;
import com.brick.manager.BeanFactory;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowContext;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.u2a.framework.util.HelperDB;

public class InitEntry implements FunctionProvider {
	protected IBaseDAO db;

	public IBaseDAO getDb() {
		return db;
	}

	public void setDb(IBaseDAO db) {
		this.db = db;
	}

	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		WorkflowEntry entry = (WorkflowEntry) transientVars.get("entry");
		WorkflowContext context = (WorkflowContext) transientVars.get("context");
        transientVars.put("caller", context.getCaller());
		try {
			IMap map = BeanFactory.getClassBean("com.WfBizInstance");	
			String objId=(String) transientVars.get("objid");
			String tableName=(String) transientVars.get("tablename");
			String caller=context.getCaller();
			String contTitle=(String) transientVars.get("conttitle");
			String url=(String) transientVars.get("url");
			Integer processType=(Integer) transientVars.get("processtype");
			ps.setString("objId", objId);
			ps.setString("tableName", tableName);
			ps.setInt("processType", processType);
			map.put("id", entry.getId());
			map.put("processId", entry.getWorkflowName());
			map.put("objId", objId);
			map.put("organizationId", transientVars.get("orgid"));
			map.put("tableName", tableName);
			map.put("contTitle", contTitle);
			map.put("url", url);
			map.put("processType", processType);
			map.put("caller", caller);
			map.put("state", entry.getState());
			Date date = new Date();
			HelperDB.setDateTime("createDate", map, date);
			HelperDB.setDateTime("modifyDate", map, date);
			db.insert(map);
		} catch (Exception e) {
			throw new WorkflowException("Error creating new workflow instance", e);
		}
	}

}
