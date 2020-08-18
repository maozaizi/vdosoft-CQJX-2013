package com.u2a.framework.service.workflow.srevice;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.brick.api.Service;
import com.brick.dao.Page;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.data.OrderBean;
import com.brick.exception.BusinessException;
import com.brick.manager.BeanFactory;
import com.brick.manager.ContextUtil;
import com.brick.util.PageUtils;
import com.brick.util.Util;
import com.opensymphony.workflow.loader.JDBCWorkflowFactory;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.loader.WorkflowLoader;
import com.opensymphony.workflow.spi.Step;
import com.u2a.framework.commons.Constants;
import com.u2a.framework.commons.OperateHelper;
import com.u2a.framework.service.workflow.WfHelper;
import com.u2a.framework.service.workflow.tree.AuthorizedTreeTagCallBack;
import com.u2a.framework.service.workflow.tree.ProcessTreeTagCallBack;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;

/**
 * 流程管理service 系统名称：新疆生产建设兵团消防网络化信息管理系统 类名称：WorkFlowService 类描述： 创建人：gaoy
 * 创建时间：Mar 26, 2012 9:49:07 AM
 */
@SuppressWarnings("unchecked")
public class WorkFlowService extends Service {
	/**
	 * @Description 流程管理查询tree结构
	 * @param
	 * @param in
	 * @return IMap
	 * @author gaoy
	 * @date Mar 20, 2012 6:03:13 PM
	 */
	public IMap getProcessManageList(IMap in) {
		// 从Session中获取用户信息
		IMap userMap = (IMap) in.get("userMap");
		IMap<String, Object> flowMap = BeanFactory
				.getClassBean("com.WfProcessDef");
		flowMap.put("parentId", "-1");
		flowMap.put("isValid", 1);
		List<IMap> processList = db.getList(flowMap, null);
		processList = getProcessChildList(processList, "com.WfProcessDef");
		// 返回时要展开的节点
		IMap<String, Object> result = new DataMap<String, Object>();

		result.put("processList", processList);
		return result;

	}

	/**
	 * 递归查询子节点
	 * 
	 * @Description
	 * @param
	 * @param parentList
	 * @param
	 * @param className
	 * @param
	 * @return
	 * @return List<IMap>
	 * @author gaoy
	 * @date Mar 23, 2012 1:49:53 AM
	 */
	private List<IMap> getProcessChildList(List<IMap> parentList,
			String className) {
		for (IMap map : parentList) {
			IMap<String, Object> condition = BeanFactory
					.getClassBean("com.WfProcessDef");
			condition.put("parentId", map.get("processId"));
			condition.put("isValid", 1);
			List<IMap> baseFolderList = db.getList(condition, null);
			if (baseFolderList.size() > 0) {
				map.put("childList", baseFolderList);
				getProcessChildList(baseFolderList, className);
			}
		}
		return parentList;
	}

	/**
	 * 保存流程信息跳转
	 * 
	 * @Description
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author gaoy
	 * @date Mar 23, 2012 1:49:05 AM
	 */
	public IMap toAddProcessDirectory(IMap in) {
		String parentId = in.get("parentId").toString() == null ? "-1" : in
				.get("parentId").toString();
		String flag = (String) in.get("flag");
		IMap<String, Object> result = new DataMap<String, Object>();
		result.put("parentId", parentId);
		result.put("flag", flag);
		return result;
	}

	/**
	 * 保存流程信息
	 * 
	 * @Description
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author gaoy
	 * @date Mar 23, 2012 1:48:49 AM
	 */
	public IMap addProcessDirectory(IMap in) {
		IMap userMap = (IMap) in.get("userMap");
		IMap<String, Object> insertBean = (IMap<String, Object>) in
				.get("flowManager");
		String processId = HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.WfProcessDef");
		String strname = (String)insertBean.get("strname");
		
		insertBean.put("processId", processId);
		HelperDB.setCreate4isValid(HelperApp.getUserName(userMap), insertBean);
		String parentId = (String) insertBean.get("parentId");
		String processCode = (String) insertBean.get("processCode");
		insertBean.put("parentId", parentId);
		// 0目录，1流程
		boolean flag = "1".equals((String) insertBean.get("flag"));
		String processContentUrl = (String) insertBean.get("processContentUrl");
		String processPicturesUrl = (String) insertBean
				.get("processPicturesUrl");
		String processPageUrl = (String) insertBean.get("processPageUrl");
		String application_scope = "0";
		String desc = "添加一条"+strname+"流程目录";
		if (flag) {
			// 判断流程编码不能为空
			if (Util.checkNull(processCode)) {
				throw new BusinessException("流程编码不能为空！");
			}
			// 判断是否跨组织使用
//			if (Util.checkNull(insertBean.get("isOrganization"))) {
//				throw new BusinessException("是否跨组织使用不能为空！");
//			}
//			IMap<String, Object> flowManagerBean = BeanFactory
//					.getClassBean("com.WfProcessDef");
//			flowManagerBean.put("processCode", processCode);
//			String isAuthorizedBt = (String)insertBean.get("isAuthorizedBt");
//			String isAuthorizedSb = (String)insertBean.get("isAuthorizedSb");
//			String isAuthorizedKq = (String)insertBean.get("isAuthorizedKq");
//			String isAuthorizedTc = (String)insertBean.get("isAuthorizedTc");
//			String isAuthorizedQt = (String)insertBean.get("isAuthorizedQt");
//			if (Util.checkNull(isAuthorizedBt)
//					&& Util.checkNull(isAuthorizedSb)
//					&& Util.checkNull(isAuthorizedKq)
//					&& Util.checkNull(isAuthorizedTc)
//					&& Util.checkNull(isAuthorizedQt)) {
//				throw new BusinessException("请授权该流程！");
//			}
//			flowManagerBean.put("isValid", 1);
//			List<IMap> flowManagerList =  db.getList(flowManagerBean,null);
//			for(IMap map : flowManagerList){
//				String istc = ((BigDecimal)map.get("isAuthorizedTc")).toString();
//				if (istc.equals(isAuthorizedTc)) {
//					throw new BusinessException("流程编号" + processCode
//							+ "在团级已经存在！");
//				}else{
//					application_scope = "4";
//				} 
//				String iskq = ((BigDecimal)map.get("isAuthorizedKq")).toString();
//				if (iskq.equals(isAuthorizedKq)) {
//					throw new BusinessException("流程编号" + processCode
//							+ "在垦区已经存在！");
//				}else{
//					application_scope = "3";
//				} 
//				String issb = ((BigDecimal)map.get("isAuthorizedSb")).toString();
//				if (issb.equals(isAuthorizedSb)) {
//					throw new BusinessException("流程编号" + processCode
//							+ "在师级已经存在！");
//				}else{
//					application_scope = "2";
//				}
//				String isbt = ((BigDecimal)map.get("isAuthorizedBt")).toString();
//				if (isbt.equals(isAuthorizedBt)) {
//					throw new BusinessException("流程编号" + processCode
//							+ "在兵团已经存在！");
//				
//				}else{
//					application_scope = "1";
//				} 
//				String isqt = ((BigDecimal)map.get("isAuthorizedQt")).toString();
//				if (isqt.equals(isAuthorizedQt)) {
//					throw new BusinessException("流程编号" + processCode
//							+ "在其他组织已经存在！");
//				}else{
//					application_scope = "0";
//				}
//			}
			// 判断流程内容
			if (Util.checkNull(processContentUrl)) {
				throw new BusinessException("请上传流程文件！");
			}
			// 判断图片
			if (Util.checkNull(processPicturesUrl)) {
				throw new BusinessException("请上传流程图片文件！");
			}
			// 判断界面
			if (Util.checkNull(processPageUrl)) {
				throw new BusinessException("请上传流程界面文件！");
			}
			desc = "添加一条"+strname+"流程与多条步骤";
		}
		db.insert(insertBean);

		if (flag) {
			// 保存到流程表
			try {
				String firePath = ContextUtil.servletContext.getRealPath("/")
						+ processContentUrl;

				JDBCWorkflowFactory functionProvider = (JDBCWorkflowFactory) ContextUtil
						.getSpringBean("workflowFactory");
				WorkflowDescriptor descriptor = WorkflowLoader.load(
						new FileInputStream(firePath), true);
				functionProvider.saveWorkflow(processId, descriptor, true);

				// 获取当前流程所有步骤
				List<StepDescriptor> list = descriptor.getSteps();
				
				for (int i = 0; i < list.size(); i++) {
					StepDescriptor stepDescriptor = list.get(i);
					StepDescriptor step=WfHelper.getStepDescriptor(processId,(String)userMap.get("userId"),Integer.valueOf(stepDescriptor.getId()));
					String goUrl = (String)step.getMetaAttributes().get("url");
					String phurl = (String)step.getMetaAttributes().get("phurl");
					// 根据流程编号获取流程所有步骤保存
					IMap<String, Object> processStepBean = BeanFactory
							.getClassBean("com.WfProcessStep");
					processStepBean.put("processStepId", HelperApp
							.getAutoIncrementPKID(HelperApp.getPackageName(),
									"com.WfProcessStep"));
					processStepBean.put("processId", processId);
					processStepBean.put("stepType", "0");
					processStepBean.put("applicationScope", application_scope);
					processStepBean.put("stepId", stepDescriptor.getId());
					processStepBean.put("stepName", stepDescriptor.getName());
					// 进入步骤页面url
					processStepBean.put("editUrl", goUrl);
					// 进入步骤
					processStepBean.put("viewUrl", phurl);
					db.insert(processStepBean);
				}
			} catch (Exception e) {
				throw new BusinessException("保存流程失败！", e);
			}
		}

		IMap<String, Object> result = new DataMap<String, Object>();
		result.put("method.url", in.get("url") + "?processId=" + processId
				+ "&tempPat=1");
		result.put("method.infoMsg", OperateHelper.getAddMsg());
		return result;
	}

	/**
	 * 删除流程信息
	 * 
	 * @Description
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author gaoy
	 * @date Mar 23, 2012 3:47:07 AM
	 */
	public IMap deleteProcess(IMap in) {
		IMap userMap = (IMap) in.get("userMap");
		String infoString = "删除成功！";
		IMap<String, Object> result = new DataMap<String, Object>();
		String processId = (String) in.get("processId");
		String goParentId = (String) in.get("goParentId");
		if (goParentId.equals("-1")) {
			throw new BusinessException("不能删除流程根目录！");
		}
		IMap<String, Object> flowManager = BeanFactory
				.getClassBean("com.WfProcessDef");
		flowManager.put("parentId", processId);
		flowManager.put("isValid", 1);
		List<IMap> flowManagerList = db.getList(flowManager, null);
		String desc = "";
		if (flowManagerList.isEmpty()) {
			IMap<String, Object> bizInstanceBean = BeanFactory
					.getClassBean("com.WfBizInstance");
			bizInstanceBean.put("processId", processId);
			List<IMap> bizInstanceList = db.getList(bizInstanceBean, null);
			// 判断是否发起过流程
			if (bizInstanceList.size() > 0) {
				// 已发起流程
				IMap<String, Object> processDefBean = BeanFactory
						.getClassBean("com.WfProcessDef");
				processDefBean.put("processId", processId);
				processDefBean.put("isValid", 0);
				db.update(processDefBean);
				desc = "修改一条流程定义数据"+processId;
			} else {
				// 未发起过流程
				IMap<String, Object> processStep = BeanFactory
						.getClassBean("com.WfProcessStep");
				processStep.put("processId", processId);
				List<IMap> processStepList = db.getList(processStep, null);
				for (IMap map : processStepList) {
					String processStepId = (String) map.get("processStepId");
					IMap<String, Object> authorizedRelation = BeanFactory
							.getClassBean("com.WfProcessStepAuth");
					authorizedRelation.put("processStepId", processStepId);
					// 删除授权信息
					db.delete(authorizedRelation);
				}
				// 删除流程步骤
				db.delete(processStep);
				IMap<String, Object> flowManagerBean = BeanFactory
						.getClassBean("com.WfProcessDef");
				flowManagerBean.put("processId", processId);
				// 删除工作流程定义
				db.delete(flowManagerBean);
				// 删除流程定义
				IMap<String, Object> workflowdef = BeanFactory
						.getClassBean("com.osWorkflowdefs");
				workflowdef.put("wfName", processId);
				db.delete(workflowdef);
				desc = "删除一条流程定义数据"+processId+"、多条流程步骤数据、多条流程步骤授权数据";
			}

		} else {
			infoString = "要先删除子节点，才能删除父节点！";
		}
		
		String url = in.get("url").toString();
		result.put("method.url", url + "?processId=" + goParentId
				+ "&tempPat=1");
		result.put("method.infoMsg", infoString);
		return result;
	}

	/**
	 * 跳转授权主页面
	 * 
	 * @Description
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author gaoy
	 * @date Apr 6, 2012 3:51:21 PM
	 */
	public IMap goAuthorizedIndex(IMap in) {
		IMap<String, Object> result = new DataMap<String, Object>();
		result.put("processStepId", in.get("processStepId"));
		result.put("authorizedType", in.get("authorizedType"));
		result.put("processId", in.get("processId"));
		return result;
	}

	/**
	 * 根据主键获取流程信息
	 * 
	 * @Description
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author gaoy
	 * @date Mar 23, 2012 12:55:29 AM
	 */
	public IMap getProcessInfo(IMap in) {
		String processId = (String) in.get("processId");
		String parentId = (String) in.get("parentId");
		IMap<String, Object> flowManagerBean = BeanFactory
				.getClassBean("com.WfProcessDef");
		List processList = null;
		if (Util.checkNotNull(processId)) {
			flowManagerBean.put("processId", processId);
			IMap<String, Object> processStep = BeanFactory
					.getClassBean("com.WfProcessStep");
			// 0目录，1流程
			processStep.put("processId", processId);
			// 流程步骤编号
			OrderBean order = new OrderBean();
			order.put("processStepId", order.ASC);
			processList = db.getList(processStep, order);
		}
		if (Util.checkNotNull(parentId)) {
			flowManagerBean.put("parentId", parentId);
		}
		List<IMap> flowManager = db.getList(flowManagerBean, null);
		IMap<String, Object> result = new DataMap<String, Object>();
		result.put("flowManagerBean", flowManager.isEmpty() ? null
				: flowManager.get(0));
		result.put("processList", processList);
		// 判断修改页面内容显示 0目录 1流程 2流程内容
		result.put("isUpProCon", in.get("isUpProCon"));
		return result;
	}

	/**
	 * 查询组织树
	 * 
	 * @Description
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author gaoy
	 * @date Apr 7, 2012 4:03:52 PM
	 */
	public IMap getAuthorizedList(IMap in) {
		// 设置结果集
		IMap result = new DataMap();
		// 设置查询条件 查询跟节点
		IMap condition = BeanFactory.getClassBean("com.organizationdetali");
		// 获取模块代码：1消防局，2防火委
		condition.put("parentId", "-1");
		Object type = in.get("type");
		// 局内
		if (type == null || ((String) type).equals("")) {
			List<IMap> organizationDetaliList = db.getList(condition,
					"getBureauTree", condition.getClassName());
			for (IMap map : organizationDetaliList) {
				map.put("processStepId", in.get("processStepId"));
				map.put("authorizedType", in.get("authorizedType"));
				map.put("processId", in.get("processId"));
			}
			organizationDetaliList = getOrganizationDetaliChildList(type,
					organizationDetaliList, condition.getClassName(), in
							.get("processStepId"), in.get("authorizedType"), in
							.get("processId"));

			result.put("organizationDetaliList", organizationDetaliList);
		}

		// 返回时要展开的节点
		String expandId = in.get("expandId") == null ? "" : (String) in
				.get("expandId");
		result.put("expandId", expandId);
		result.put("cb", new AuthorizedTreeTagCallBack());
		return result;
	}

	/**
	 * @Description递归查询子组织
	 * @param
	 * @param parentList
	 * @param
	 * @param className
	 * @param
	 * @return
	 * @return List<IMap>
	 * @author zhangbo
	 * @date Feb 15, 2012 3:42:15 PM
	 */
	public List<IMap> getOrganizationDetaliChildList(Object type,
			List<IMap> parentList, String className, Object processStepId,
			Object authorizedType, Object processId) {
		for (IMap map : parentList) {
			IMap condition = BeanFactory.getClassBean("com.organizationdetali");
			List<IMap> baseOrganizationList = new ArrayList<IMap>();
			condition.put("parentId", map.get("organizationDetailId"));
			// 局内
			if (type == null) {
				if (((String) (map.get("type"))).equals("0")
						|| ((String) (map.get("type"))).equals("1")) {
					baseOrganizationList = db.getList(condition,
							"getBureauTree", condition.getClassName());
					if (baseOrganizationList.size() > 0) {
						for (IMap map2 : baseOrganizationList) {
							map2.put("processStepId", processStepId);
							map2.put("authorizedType", authorizedType);
							map2.put("processId", processId);

						}
						map.put("childList", baseOrganizationList);
						getOrganizationDetaliChildList(type,
								baseOrganizationList, className, processStepId,
								authorizedType, processId);
					}
				}
			}
		}
		return parentList;
	}

	/**
	 * 修改流程信息
	 * 
	 * @Description
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author gaoy
	 * @date Mar 23, 2012 12:55:43 AM
	 */
	public IMap updateProcess(IMap in) {
		IMap userMap = (IMap) in.get("userMap");
//		String contentUrl = (String) in.get("contentUrl");
		IMap<String, Object> insertBean = (IMap<String, Object>) in
				.get("flowManager");
		String processId = (String) insertBean.get("processId");
		String isUpProCon = (String) in.get("isUpProCon");
		String flag = (String) insertBean.get("flag");
		String processCode = (String) insertBean.get("processCode");
		String processContentUrl = (String) insertBean.get("processContentUrl");
		String strname = (String) insertBean.get("strname");

		HelperDB.setModifyInfo(HelperApp.getUserName(userMap), insertBean);
//		String isAuthorizedBt = (String) insertBean.get("isAuthorizedBt");
//		String isAuthorizedSb = (String) insertBean.get("isAuthorizedSb");
//		String isAuthorizedKq = (String) insertBean.get("isAuthorizedKq");
//		String isAuthorizedTc = (String) insertBean.get("isAuthorizedTc");
//		String isAuthorizedQt = (String) insertBean.get("isAuthorizedQt");
//		String desc = "";
//		String application_scope = "";
		if (isUpProCon.equals("0") || isUpProCon.equals("1")) {
			if (Util.checkNull(strname)) {
				throw new BusinessException("请输入流程名称！");
			}
			// 0目录，1流程
//			if (flag.equals("1") && isUpProCon.equals("1")) {
//				desc = "修改一条流程"+processId;
//				if (Util.checkNull(isAuthorizedBt)
//						&& Util.checkNull(isAuthorizedSb)
//						&& Util.checkNull(isAuthorizedKq)
//						&& Util.checkNull(isAuthorizedTc)
//						&& Util.checkNull(isAuthorizedQt)) {
//					throw new BusinessException("请授权该流程！");
//				}
//				IMap<String, Object> flowManagerBean = BeanFactory
//						.getClassBean("com.WfProcessDef");
//				flowManagerBean.put("processCode", processCode);
//
//				flowManagerBean.put("isValid", 1);
//				List<IMap> flowManagerList =  db.getList(flowManagerBean,null);
//			
//				for(IMap map : flowManagerList){
//					if (!processId.equals(map.get("processId"))) {
//				
//					String istc = ((BigDecimal)map.get("isAuthorizedTc")).toString();
//					String iskq = ((BigDecimal)map.get("isAuthorizedKq")).toString();
//					String issb = ((BigDecimal)map.get("isAuthorizedSb")).toString();
//					String isbt = ((BigDecimal)map.get("isAuthorizedBt")).toString();
//					String isqt = ((BigDecimal)map.get("isAuthorizedQt")).toString();
//					if (istc.equals(isAuthorizedTc)) {
//						throw new BusinessException("流程编号" + processCode
//								+ "在团级已经存在！");
//					}else{
//							application_scope = "4";
//					} 
//					if (iskq.equals(isAuthorizedKq)) {
//						throw new BusinessException("流程编号" + processCode
//								+ "在垦区已经存在！");
//					}else{
//						application_scope = "3";
//					} 
//					if (issb.equals(isAuthorizedSb)) {
//						throw new BusinessException("流程编号" + processCode
//								+ "在师级已经存在！");
//					}else{
//						application_scope = "2";
//					} 
//					if (isbt.equals(isAuthorizedBt)) {
//						throw new BusinessException("流程编号" + processCode
//								+ "在兵团已经存在！");
//					}else{
//						application_scope = "1";
//					} 
//					if (isqt.equals(isAuthorizedQt)) {
//						throw new BusinessException("流程编号" + processCode
//								+ "在其他组织已经存在！");
//					}else {
//						application_scope = "0";
//					}
//					
//					}
//				}
//			}
//			insertBean.put("isAuthorizedBt", Util.checkNull(isAuthorizedBt) ? "0"
//					: isAuthorizedBt);
//			insertBean.put("isAuthorizedSb", Util.checkNull(isAuthorizedSb) ? "0"
//					: isAuthorizedSb);
//			insertBean.put("isAuthorizedKq", Util.checkNull(isAuthorizedKq) ? "0"
//					: isAuthorizedKq);
//			insertBean.put("isAuthorizedTc", Util.checkNull(isAuthorizedTc) ? "0"
//					: isAuthorizedTc);
//			insertBean.put("isAuthorizedQt", Util.checkNull(isAuthorizedQt) ? "0"
//					: isAuthorizedQt);
		} else {
//			desc = "修改一条流程图"+processId;
			// 判断修改页面内容显示 0目录 1流程 2流程内容
			IMap<String, Object> bizInstance = BeanFactory
					.getClassBean("com.WfBizInstance");
			bizInstance.put("processId", processId);
			List<IMap> bizInstanceList = db.getList(bizInstance, null);
			for (IMap map : bizInstanceList) {
				// 0创建1执行中2暂停3无效4完成-1未知
				int state = ((Integer) map.get("state"));
				String stateName = "";
				if (state==0) {
					stateName = "创建中";
				} else if (state==1) {
					stateName = "执行中";
				} else if (state==2) {
					stateName = "暂停中";
				}
				if (Util.checkNotNull(stateName)) {
					throw new BusinessException("该流程处于" + stateName + "，不能编辑！");
				}
			}
			
			// 修改工作流定义内容
			String firePath = ContextUtil.servletContext.getRealPath("/")
					+ processContentUrl;
			JDBCWorkflowFactory functionProvider = (JDBCWorkflowFactory) ContextUtil
					.getSpringBean("workflowFactory");
			try {
				WorkflowDescriptor descriptor = WorkflowLoader.load(
						new FileInputStream(firePath), true);
				functionProvider.saveWorkflow(processId, descriptor, true);
				// 重新加载流程定义内容
				functionProvider.setReload(true);
				WfHelper.getWorkflow((String)userMap.get("userId"));
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				functionProvider.setReload(false);
			}
//			}
			// 位置流程
			insertBean.put("flag", "1");
		}
		db.update(insertBean);
		
		IMap<String, Object> result = new DataMap<String, Object>();
		result.put("method.url", in.get("url") + "?processId=" + processId
				+ "&tempPat=1");
		result.put("method.infoMsg", OperateHelper.getUpdateMsg());
		return result;
	}

	/**
	 * 保存岗位、用户授权
	 * 
	 * @Description
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author gaoy
	 * @date Apr 6, 2012 5:45:20 PM
	 */
	public IMap addPostAndUserInfo(IMap in) {
		IMap userMap = (IMap) in.get("userMap");
		IMap<String, Object> insertBean = (IMap<String, Object>) in
				.get("authorizedRelation");
		IMap authorizedRelation = BeanFactory
				.getClassBean("com.WfProcessStepAuth");
		String authorizedType = (String) insertBean.get("authorizedType");
		String[] post = (String[]) in.get("postId");
		String[] user = (String[]) in.get("userId");

		if (Util.checkNull(user) && Util.checkNull(post)) {
			throw new BusinessException("请选择岗位用户信息");
		} else {
			if (Util.checkNotNull(user)) {
				// 判断添加user是否存在
				for (int i = 0; i < user.length; i++) {
					authorizedRelation.put("processStepId", insertBean
							.get("processStepId"));
					authorizedRelation.put("autId", user[i].split("/")[0]);
					authorizedRelation.put("strtype", "2");
					authorizedRelation.put("authorizedType", authorizedType);
					List<IMap> authorizedRelationList = db.getList(
							authorizedRelation, null);

					if (authorizedRelationList.size() > 0) {
						IMap processStep = BeanFactory
								.getClassBean("com.WfProcessStep");
						processStep.put("processStepId", authorizedRelationList
								.get(0).get("processStepId"));
						IMap processStepMap = db.get(processStep);
						if (null != processStepMap) {
							throw new BusinessException("该用户已在该步骤存在！"
									+ user[i].split("/")[0]);
						}
					}
				}
				// 添加用户
				for (int i = 0; i < user.length; i++) {
					String id = HelperApp.getAutoIncrementPKID(HelperApp
							.getPackageName(), "com.WfProcessStepAuth");
					insertBean.put("id", id);
					insertBean.put("autId", user[i].split("/")[0]);
					insertBean.put("autName", user[i].split("/")[1]);
					// 1岗位2用户3字典岗位
					insertBean.put("strtype", "2");
					db.insert(insertBean);
				}
			}
			if (Util.checkNotNull(post)) {
				// 判断添加post是否存在
				for (int i = 0; i < post.length; i++) {
					authorizedRelation.put("processStepId", insertBean
							.get("processStepId"));
					authorizedRelation.put("autId", post[i].split("/")[0]);
					authorizedRelation.put("strtype", "1");
					authorizedRelation.put("authorizedType", authorizedType);
					List<IMap> authorizedRelationList = db.getList(
							authorizedRelation, null);

					if (authorizedRelationList.size() > 0) {
						IMap processStep = BeanFactory
								.getClassBean("com.WfProcessStep");
						processStep.put("processStepId", authorizedRelationList
								.get(0).get("processStepId"));
						IMap processStepMap = db.get(processStep);
						if (null != processStepMap) {
							throw new BusinessException("该岗位已在该步骤存在！");
						}
					}
				}
				// 添加岗位
				for (int i = 0; i < post.length; i++) {
					String id = HelperApp.getAutoIncrementPKID(HelperApp
							.getPackageName(), "com.WfProcessStepAuth");
					insertBean.put("id", id);
					insertBean.put("autId",  post[i].split("/")[0]);
					insertBean.put("autName", post[i].split("/")[1]);
					// 1岗位2用户3字典岗位
					insertBean.put("strtype", "1");
					db.insert(insertBean);
				}
			}
		}
		IMap result = new DataMap();
		result.put("authorizedType", authorizedType);
		result.put("pricessId", in.get("pricessId"));
		result.put("processStepId", insertBean.get("processStepId"));
		result.put("method.url", in.get("url"));
		result.put("method.infoMsg",  OperateHelper.getAddMsg());
		return result;
	}

	/**
	 * 根据流程步骤子表主键查询授权信息关系表
	 * 
	 * @Description
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author gaoy
	 * @date Apr 6, 2012 9:20:02 PM
	 */
	public IMap getauthorizedStep(IMap in) {
		String processStepId = (String) in.get("processStepId");
		if (Util.checkNull(processStepId)) {
			throw new BusinessException("请选择步骤！");
		}
		// 1候选人 2执行人
		String authorizedType = (String) in.get("authorizedType");
		IMap authorizedRelation = BeanFactory
				.getClassBean("com.WfProcessStepAuth");
		authorizedRelation.put("processStepId", processStepId);
		authorizedRelation.put("authorizedType", authorizedType);
		OrderBean order = new OrderBean();
		order.put("authorizedType", order.ASC);
		List authorizedRelationList = db.getList(authorizedRelation, order);
		IMap result = new DataMap();
		result.put("authorizedRelationList", authorizedRelationList);
		result.put("authorizedType", authorizedType);
		result.put("processStepId", processStepId);
		result.put("processId", in.get("processId"));
		return result;
	}

	/**
	 * 根据主键获取步骤
	 * 
	 * @Description
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author gaoy
	 * @date Apr 9, 2012 9:33:28 AM
	 */
	public IMap getProcessStep(IMap in) {
		String processStepId = (String) in.get("processStepId");
		if (Util.checkNull(processStepId)) {
			throw new BusinessException("请选择步骤！");
		}
		IMap processStep = BeanFactory.getClassBean("com.WfProcessStep");
		processStep.put("processStepId", processStepId);
		IMap processStepMap = db.get(processStep);
		IMap result = new DataMap();
		result.put("processStepMap", processStepMap);
		return result;
	}

	/**
	 * 修改步骤信息
	 * 
	 * @Description
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author gaoy
	 * @date Apr 9, 2012 9:48:54 AM
	 */
	public IMap updateProcessStep(IMap in) {
		IMap userMap = (IMap) in.get("userMap");
		String processStepId = (String) in.get("processStepId");
		String applicationScope = (String) in.get("applicationScope");
		String stepType = (String) in.get("stepType");
		// 进入步骤页面url
		String editUrl = (String) in.get("editUrl");
		String viewUrl = (String) in.get("viewUrl");

		IMap processStep = BeanFactory.getClassBean("com.WfProcessStep");
		processStep.put("processStepId", processStepId);
		processStep.put("applicationScope", applicationScope);
		processStep.put("stepType", stepType);
		processStep.put("editUrl", editUrl);
		processStep.put("viewUrl", viewUrl);
		db.update(processStep);
		IMap result = new DataMap();
		result.put("method.url", in.get("url"));
		result.put("method.infoMsg", OperateHelper.getUpdateMsg());
		return result;
	}

	/**
	 * 删除已选中授权信息关系
	 * 
	 * @Description
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author gaoy
	 * @date Apr 7, 2012 9:39:12 AM
	 */
	public IMap delAuthorizedStep(IMap in) {
		IMap userMap = (IMap) in.get("userMap");
		String processStepId = (String) in.get("processStepId");
		// 1候选人 2执行人
		String authorizedType = (String) in.get("authorizedType");
		String processId = (String) in.get("processId");
		IMap authorizedRelation = BeanFactory
				.getClassBean("com.WfProcessStepAuth");
		authorizedRelation.put("id", in.get("id"));
		db.delete(authorizedRelation);
		IMap result = new DataMap();
		result.put("method.url", in.get("url") + "?processStepId="
				+ processStepId + "&authorizedType=" + authorizedType
				+ "&processId=" + processId);
		result.put("method.infoMsg", OperateHelper.getDelMsg());
		return result;
	}

	/**
	 * @Description 岗位用户授权信息页面
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author zhangbo
	 * @date Mar 13, 2012 9:31:55 AM
	 */
	public IMap getPostAndUserInfo(IMap in) {
		IMap result = new DataMap();
		// 基本信息
		IMap organizationDetali = (IMap) in.get("organizationDetali");
		// 基本信息
		IMap userInfo = BeanFactory.getClassBean("com.UserInfo");
		// 基本信息
		IMap postInfo = BeanFactory.getClassBean("com.PostInfo");

		// 没有传入应用组织ID的情况
		if (organizationDetali.get("organizationDetailId") == null
				|| organizationDetali.get("organizationDetailId").equals("")) {
			organizationDetali.put("baseOrganizationId", "{BT}");
			organizationDetali.put("parentId", "-1");
			organizationDetali = db.get(organizationDetali);
		} else {
			// 查询基本信息
			organizationDetali = db.get(organizationDetali);
		}
		List<IMap> postInfoList = new ArrayList<IMap>();
		List<IMap> userInfoList = new ArrayList<IMap>();
		if (organizationDetali != null) {
			// 查询人员信息organizationDetailId
			userInfo.put("orgDetailId", organizationDetali
					.get("organizationDetailId"));
			userInfo.put("isValid", Constants.ISVALID);
			userInfoList = db.getList(userInfo, null);
			// 查询岗位信息
			postInfo.put("organizationDetailId", organizationDetali
					.get("organizationDetailId"));
			postInfo.put("isValid", Constants.ISVALID);
			postInfoList = db.getList(postInfo, null);
		}

		result.put("organizationDetali", organizationDetali);
		result.put("userInfoList", userInfoList);
		result.put("postInfoList", postInfoList);
		result.put("processStepId", in.get("processStepId"));
		result.put("authorizedType", in.get("authorizedType"));
		result.put("processId", in.get("processId"));
		return result;
	}

	/**
	 * @Description 获取岗位列表
	 * @param
	 * @param in
	 * @return IMap
	 * @author panghaichao
	 * @date Apr 7, 2012 12:58:37 PM
	 */
	public IMap getPost(IMap in) {
		IMap groupPower = BeanFactory.getClassBean("com.AuthGroup");
		groupPower.put("isValid", Constants.ISVALID);
        List<IMap> postList =  db.getList(groupPower, null);//所有的权限组
		IMap<String, Object> result = new DataMap<String, Object>();
		result.put("postList", postList);
		result.put("processStepId", in.get("processStepId"));
		result.put("authorizedType", in.get("authorizedType"));
		result.put("processId", in.get("processId"));
		return result;
	}

	/**
	 * @Description 添加岗位信息
	 * @param
	 * @param in
	 * @return IMap
	 * @author panghaichao
	 * @date Apr 7, 2012 1:48:18 PM
	 */
	public IMap addPost(IMap in) {
		IMap userMap = (IMap) in.get("userMap");
		IMap insertBean = (IMap) in.get("authorizedRelation");
		String[] autId = (String[]) insertBean.get("autId");
		String processId = (String) in.get("processId");
		if (Util.checkNull(autId)) {
			throw new BusinessException("请选择岗位信息！");
		} else {
			for (int i = 0; i < autId.length; i++) {
				IMap wfProcessStepAuth = BeanFactory
						.getClassBean("com.WfProcessStepAuth");
				wfProcessStepAuth.put("processStepId", insertBean
						.get("processStepId"));
				wfProcessStepAuth.put("autId", autId[i].split("/")[0]);
				wfProcessStepAuth.put("strtype", "3");
				wfProcessStepAuth.put("authorizedType", insertBean
						.get("authorizedType"));
				List list = db.getList(wfProcessStepAuth, null);
				if (list.size() > 0) {
					throw new BusinessException("该岗位已经存在，不能重复添加！" + autId[i].split("/")[0]);
				}
				insertBean.put("id", HelperApp.getAutoIncrementPKID(HelperApp
						.getPackageName(), "com.WfProcessStepAuth"));
				insertBean.put("autId", autId[i].split("/")[0]);
				insertBean.put("autName", autId[i].split("/")[1]);
				// 1岗位2用户3字典岗位
				insertBean.put("strtype", "3");
				db.insert(insertBean);
			}
		}
		IMap<String, Object> result = new DataMap<String, Object>();
		String url = (String) in.get("url");
		result.put("method.url", url + "?isShua=1&processStepId="
				+ insertBean.get("processStepId") + "&authorizedType="
				+ insertBean.get("authorizedType") 
				+ "&processId=" + processId);
		result.put("method.infoMsg", OperateHelper.getAddMsg());
		return result;
	}

	/**
	 * @Description 删除岗位关系
	 * @param
	 * @param in
	 * @return IMap
	 * @author panghaichao
	 * @date Apr 7, 2012 2:04:02 PM
	 */
	public IMap deletePost(IMap in) {
		IMap deleteBean = (IMap) in.get("authorizedRelation");
		db.delete(deleteBean);
		IMap<String, Object> result = new DataMap<String, Object>();
		result.put("method.url", in.get("url"));
		result.put("method.infoMsg", OperateHelper.getDelMsg());
		return result;
	}

	public IMap addprocessStart(IMap in) {
		IMap<String, Object> statMap = new DataMap<String, Object>();

		IMap userMap = (IMap) in.get("userMap");
		// 调用者
		statMap.put("caller", userMap.get("userId"));
		// 关联id
		statMap.put("objId", "10920120413000002");
		// 表名
		statMap.put("tableName", "com.DesignAuditing");
		// 标题
		statMap.put("contTitle", "绿地假日酒店的建筑工程消防设计");
		// 链接
		statMap.put("url", "/api/designAuditing/auditingAllList?type=1");
		// 流程类型
		statMap.put("processType", 0);
		// 当前用户组织ID
		statMap.put("orgId", userMap.get("orgdetailid"));
		// 前用用户分类1消防局2防火委
		long m = WfHelper.start(db, "design", (String) userMap.get("userId"),
				statMap);
		IMap<String, Object> result = new DataMap<String, Object>();
		result.put("method.url", in.get("strurl"));
		result.put("method.infoMsg", "流程发起成功！");
		return result;
	}

	/**
	 * 获取流程实例信息
	 * 
	 * @Description
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author gaoy
	 * @date Apr 17, 2012 2:00:20 PM
	 */
	public IMap getBizInstanceList(IMap in) {
		String state = (String)in.get("state");
		// 分页查询
		HttpServletRequest request = (HttpServletRequest) in.get("request");

		Page page = db.pageQuery(in, "bizInstanceList", "", "process_id",in
				.get("currentPageNO") == null ? 1 : Integer.parseInt(in.get(
				"currentPageNO").toString()), Constants.PAGESIZE,null);
		page.setAction(PageUtils.getPagePathRequireAttribute(request,
				new String[] { "id" }));

		// 创建返回结果集
		IMap result = new DataMap();
		result.put("page", page);
		result.put("state", state);
		return result;
	}
	/**
	 * 修改流程实例状态
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author gaoy
	 * @date Apr 18, 2012 7:11:19 PM
	 */
	public IMap updateBizInstanceState(IMap in) {
		String id = (String) in.get("id");
		String updstate = (String) in.get("updstate");
		String state = (String)in.get("state");
		IMap wfBizInstance = BeanFactory.getClassBean("com.WfBizInstance");
		wfBizInstance.put("id", id);
		wfBizInstance.put("state", updstate);
		db.update(wfBizInstance);
		IMap result = new DataMap();
		result.put("method.url", in.get("url")+"&state="+state+"&processCode="+in.get("processCode")+"&strname="+in.get("strname")+"&contTitle="+in.get("contTitle")+"&insid="+in.get("insid")+"&objId="+in.get("objId"));
		result.put("method.infoMsg", OperateHelper.getUpdateMsg());
		return result;
	}
	/**
	 * 查看流程跟踪图
	 * @Description 
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author gaoy
	 * @date Apr 19, 2012 9:19:40 AM
	 */
	public IMap getProcessTracking(IMap in) {
		IMap userMap = (IMap) in.get("userMap");
		String path = (String) in.get("path");
		String imgurl = (String) in.get("imgurl");
		String insid = (String) in.get("insid");
		List list = WfHelper.getCurrStep(Long.valueOf(insid), (String)userMap.get("userId"));
		int size = list.size();
		String currentStepIds = "";
		for(int i = 0; i < size; i++){
			if (i==0) {
				currentStepIds += ((Step) list.get(i)).getStepId();
			}else{
				currentStepIds += ","+((Step) list.get(i)).getStepId();
			}
		}
		IMap result = new DataMap();
		result.put("steps", currentStepIds);
		result.put("path", path);
		result.put("imgurl", imgurl);
		return result;
	}
}
