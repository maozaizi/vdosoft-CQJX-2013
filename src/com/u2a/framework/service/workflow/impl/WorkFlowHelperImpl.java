package com.u2a.framework.service.workflow.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.brick.dao.IBaseDAO;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.exception.BusinessException;
import com.brick.manager.BeanFactory;
import com.brick.util.Util;
import com.opensymphony.workflow.StoreException;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.u2a.framework.service.workflow.WorkFlowHelper;
import com.u2a.framework.util.FireNetHelper;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;

@SuppressWarnings("unchecked")
public class WorkFlowHelperImpl implements WorkFlowHelper {
	protected IBaseDAO db;

	protected int getState(String state, int v) {
		// 2=Finished 0=Queued 1=Underway
		int i = v;
		if ("Queued".equals(state)) {
			i = 0;
		} else if ("Finished".equals(state)) {
			i = 2;
		} else if ("Underway".equals(state)) {
			i = 1;
		}
		return i;

	}

	public IBaseDAO getDb() {
		return db;
	}

	public void setDb(IBaseDAO db) {
		this.db = db;
	}

	public void createCurrentStep(long entryId, String owner, Step step)
			throws StoreException {
		try {
			// 解析owner
			JSONObject ownerUser = null;
			if ((owner != null) && (!"".equals(owner))) {
				ownerUser = JSONObject.fromObject(owner);
			}
			Long id = step.getId();
			int iState = getState(step.getStatus(), 1);
			// 获取流程实例
			IMap mWorkFlow = BeanFactory.getClassBean("com.WfBizInstance");
			mWorkFlow.put("id", entryId);
			mWorkFlow = db.get(mWorkFlow);
			String orgId = (String) mWorkFlow.get("organizationId");
			// 获取步骤定义
			IMap mStep = BeanFactory.getClassBean("com.WfProcessStep");
			mStep.put("processid", mWorkFlow.get("processId"));
			mStep.put("stepid", step.getStepId());
			mStep = db.get(mStep);
			String scope = (String) mStep.get("applicationScope");
			// 2、创建任务步骤 wf_Biz_Step id=step.getId()
			
			IMap map = BeanFactory.getClassBean("com.WfBizStep");
			map.put("id", step.getId());
			map.put("insid", entryId);
			map.put("stepId", step.getStepId());
			map.put("contTitle", mStep.get("stepName"));
			HelperDB.setDateTime("startdate", map, step.getStartDate());
			HelperDB.setDateTime("duedate", map, step.getDueDate());
			map.put("stepType", mStep.get("stepType"));
			map.put("state", iState);
			map.put("editUrl", mStep.get("editUrl"));
			map.put("viewUrl", mStep.get("viewUrl"));
			Date date = new Date();
			HelperDB.setDateTime("createDate", map, date);
			HelperDB.setDateTime("modifyDate", map, date);
			db.insert(map);
			// 设置步骤授权用户
			IMap<String, IMap> userOwner = new DataMap<String, IMap>();
			if (owner != null) {// 按赋值信息保存
				// 获取用户
				Object t= ownerUser.get("u");
				if (t!=null){
					//JSONArray u = ownerUser.getJSONArray("u");
					JSONArray u = (JSONArray)t;
					if (u != null) {
						for (int i = 0; i < u.size(); i++) {// 遍历JSONArray
							IMap user = BeanFactory.getClassBean("com.WfBizStepUser");
							user.put("userid", u.getString(i));
							userOwner.put(u.getString(i), user);
						}
					}
				}
				
				// 获取岗位
				t= ownerUser.get("p");
				if (t!=null){
					JSONArray u = (JSONArray)t;
				if (u != null) {
					// String postCodeList="";
					// for (int i = 0; i < u.size(); i++) {// 遍历JSONArray
					// IMap user = BeanFactory
					// .getClassBean("com.WfBizStepUser");
					// user.put("userid", u.getString(i));
					// user.put("usertype", "1");
					// if (Util.checkNotNull(postCodeList)){
					// postCodeList=postCodeList+","+"'"+u.getString(i)+"'";
					// }
					// postCodeList="'"+u.getString(i)+"'";
					// }
					IMap postInfo = new DataMap();
					postInfo.put("orgid", orgId);
					postInfo.put("postCodeList", u);
					// 获取字典岗位转换真实岗位
					List postIdList = db.getList(postInfo, "getRealDictPostID",
							postInfo.getClassName());
					for (int i = 0; i < postIdList.size(); i++) {
						IMap userMap = (IMap) postIdList.get(i);
						IMap user = BeanFactory.getClassBean("com.WfBizStepUser");
						user.put("userid", userMap.get("postId"));
						userOwner.put(u.getString(i), user);
					}
				}
				}
				// 获取字典岗位
				t= ownerUser.get("d");
				if (t!=null){
					JSONArray u = (JSONArray)t;
				if (u != null) {
					String myorgId = getMyOrgId(orgId, scope);
					if (Util.checkNull(myorgId)) {
						throw new BusinessException("未找到组织机构！");
					}
					IMap postInfo = new DataMap();
					postInfo.put("orgid", myorgId);
					postInfo.put("postCodeList", u);
					// 获取字典岗位转换真实用户
					List postIdList = db.getList(postInfo, "getRealDictPostID",
							postInfo.getClassName());
					for (int i = 0; i < postIdList.size(); i++) {
						IMap userMap = (IMap) postIdList.get(i);
						IMap user = BeanFactory.getClassBean("com.WfBizStepUser");
						user.put("userid", userMap.get("postId"));
						userOwner.put(u.getString(i), user);
					}

				}
				}
			} else {// 从配置信息中选择
				// 3、用entryId从wf_Biz_Instance选择出对应的流程定义PROCESS_id和obj_id和table_name
				IMap stepUser = new DataMap();
				stepUser.put("processStepId", mStep.get("processStepId"));
				if (iState == 0) {
					// 从候选人选择人员 1候选人 2执行人
					stepUser.put("authorizedType", "1");
				} else if (iState == 1) {
					// 从执行人选择人员 1候选人 2执行人
					stepUser.put("authorizedType", "2");
				}

				// 获取流程实例信息
				String myorgId = getMyOrgId(orgId, scope);
				if (Util.checkNull(myorgId)) {
					throw new BusinessException("未找到组织机构！");
				}
				stepUser.put("orgid", myorgId);
				// 获取用户Id集合
				List uList = db.getList(stepUser, "getUserID",
						"com.WfProcessStepAuth");
				// 获取岗位ID集合
				List pList = db.getList(stepUser, "getPostID",
						"com.WfProcessStepAuth");
				// 获取字典岗位ID集合
				List dList = db.getList(stepUser, "getDictPostID",
						"com.WfProcessStepAuth");
				// 根据obj_id和table_name寻找业务数据所属的组织以及组织类型（兵、师、垦、团）

				// 将userID添加list 1岗位2用户3字典岗位
				for (int i = 0; i < uList.size(); i++) {
					IMap userMap = (IMap) uList.get(i);
					IMap user = BeanFactory.getClassBean("com.WfBizStepUser");
					String u=(String) userMap.get("aut_id");
					user.put("userid",u );
					userOwner.put(u, user);
				}
				// 将postId添加list 1岗位2用户3字典岗位
				for (int i = 0; i < pList.size(); i++) {
					IMap userMap = (IMap) uList.get(i);
					IMap user = BeanFactory.getClassBean("com.WfBizStepUser");
					String u=(String) userMap.get("aut_id");
					user.put("userid",u );
					userOwner.put(u, user);
				}
				// 将真实postId添加list 1岗位2用户3字典岗位
				for (int i = 0; i < dList.size(); i++) {
					IMap userMap = (IMap) uList.get(i);
					IMap user = BeanFactory.getClassBean("com.WfBizStepUser");
					String u=(String) userMap.get("aut_id");
					user.put("userid",u );
					userOwner.put(u, user);
				}
			}
			Iterator iter = userOwner.entrySet().iterator();
			boolean falg = true;
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				IMap item = (IMap) entry.getValue();
				item.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.WfBizStepUser"));
				item.put("stepid", step.getId());
				item.put("state", "0");
				item.put("stepstate", iState);
				date = new Date();
				HelperDB.setDateTime("createDate", item, date);
				HelperDB.setDateTime("modifyDate", item, date);
				db.insert(item);
				falg = false;
			}

			if (falg) {
				IMap bizStepUser = BeanFactory.getClassBean("com.WfBizStepUser");
				bizStepUser.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.WfBizStepUser"));
				bizStepUser.put("stepid", step.getId());
				bizStepUser.put("state", "0");
				bizStepUser.put("stepstate", iState);
				bizStepUser.put("userId", mWorkFlow.get("caller"));
				date = new Date();
				HelperDB.setDateTime("createDate", bizStepUser, date);
				HelperDB.setDateTime("modifyDate", bizStepUser, date);
				db.insert(bizStepUser);
			}
		} catch (Exception e) {
			throw new StoreException(
					"Unable to create current step for workflow instance #"
							+ step.getId(), e);
		}
	}

	public void createEntry(WorkflowEntry entry) throws StoreException {
		try {
			IMap map = BeanFactory.getClassBean("com.WfBizInstance");
			map.put("id", entry.getId());
			map.put("processId", entry.getWorkflowName());
			map.put("state", entry.getState());
			Date date = new Date();
			HelperDB.setDateTime("createDate", map, date);
			HelperDB.setDateTime("modifyDate", map, date);
			db.insert(map);
		} catch (Exception e) {
			throw new StoreException("Error creating new workflow instance", e);
		}
	}

	public void markFinished(Step step) throws StoreException {
		try {
			IMap map = BeanFactory.getClassBean("com.WfBizStep");
			map.put("id", step.getId());
			map.put("state", getState(step.getStatus(), 2));
			map.put("caller", step.getCaller());
			HelperDB.setDateTime("finishDate", map, step.getFinishDate());
			Date date = new Date();
			HelperDB.setDateTime("modifyDate", map, date);
			db.update(map);
		} catch (Exception e) {
			throw new StoreException("Unable to mark step finished for #"
					+ step.getEntryId(), e);
		}
	}

	public void moveToHistory(Step step) throws StoreException {

	}

	public void setEntryState(long id, int state) throws StoreException {
		// 修改自身业务表的流程状态 wf_Biz_Instance id=entry.getId()
		try {
			IMap map = BeanFactory.getClassBean("com.WfBizInstance");
			map.put("id", id);
			map.put("state", state);
			Date date = new Date();
			HelperDB.setDateTime("modifyDate", map, date);
			db.update(map);
		} catch (Exception e) {
			throw new StoreException(
					"Unable to update state for workflow instance #" + id
							+ " to " + state, e);
		}
	}

	private IMap getLevelOrg(int level, IMap orgMap) {
		String baseOrganizationId = (String) orgMap.get("baseOrganizationId");
		String[] arr = baseOrganizationId.replace('.', ',').split(",");
		if (level < arr.length) {
			IMap m = FireNetHelper.getOrgMap(db,
					(String) orgMap.get("parentid"));
			return getLevelOrg(level, m);
		} else if (level == arr.length) {
			return orgMap;
		} else {
			return null;
		}

	}

	private String getMyOrgId(String orgId, String scope) {
		if ("0".equals(scope)) {
			return orgId;
		}
		IMap orgMap = BeanFactory.getClassBean("com.organizationdetali");
		orgMap.put("organizationDetailId", orgId);
		orgMap = db.get(orgMap);
		orgMap = getLevelOrg(Integer.parseInt(scope), orgMap);
		if (Util.checkNull(orgMap)) {
			return null;
		}
		return (String) orgMap.get("organizationDetailId");
	}

}
