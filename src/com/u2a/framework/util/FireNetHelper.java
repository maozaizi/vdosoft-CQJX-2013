package com.u2a.framework.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.brick.dao.IBaseDAO;
import com.brick.data.DBBean;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.data.OrderBean;
import com.brick.exception.BusinessException;
import com.brick.manager.BeanFactory;
import com.brick.manager.ContextUtil;
import com.brick.util.Util;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.u2a.framework.commons.Constants;
import com.u2a.framework.service.workflow.WfHelper;

public class FireNetHelper {
	/**
	 * 根据业务编号获取备注信息
	 * 
	 * @param db
	 * @return
	 */
	public static List getRemarkInfo(String id,String tableName,IBaseDAO db) {
		IMap remarkInfo = BeanFactory.getClassBean("com.RemarkInfo");
		remarkInfo.put("baseInfoId", id);
		remarkInfo.put("operationType", tableName);
		OrderBean orderBean = new OrderBean();
		orderBean.put("createDate", OrderBean.DESC);
		List<IMap> resultList = db.getList(remarkInfo, orderBean);
		return resultList;
	}
	/**
	 * 获取所有师
	 * 
	 * @param db
	 * @return
	 */
	public static List getAllShi(IBaseDAO db) {
		IMap condition = BeanFactory.getClassBean("com.baseorganization");
		condition.put("iLevel", "2");
		condition.put("isValid", Constants.ISVALID);
		OrderBean orderBean = new OrderBean();
		orderBean.put("orderby", OrderBean.ASC);
		List<IMap> resultList = db.getList(condition, orderBean);
		return resultList;
	}

	/**
	 * 获取师下属所有垦
	 * 
	 * @param db
	 * @param shi
	 * @return
	 */
	public static List getKen2Shi(IBaseDAO db, String shi) {
		IMap condition = BeanFactory.getClassBean("com.baseorganization");
		condition.put("parentId", shi);
		condition.put("iLevel", "3");
		condition.put("isValid", Constants.ISVALID);
		OrderBean orderBean = new OrderBean();
		orderBean.put("orderby", OrderBean.ASC);
		List<IMap> resultList = db.getList(condition, orderBean);
		return resultList;

	}

	/**
	 * 获取师下属所有团
	 * 
	 * @param db
	 * @param shi
	 * @return
	 */
	public static List getTuan2Shi(IBaseDAO db, String shi) {
		IMap condition = BeanFactory.getClassBean("com.baseorganization");
		condition.put("parentId", shi);
		List<IMap> resultList = db.getList(condition, "getPlatoonList",
				condition.getClassName());
		return resultList;

	}

	/**
	 * 获取垦下属所有团
	 * 
	 * @param db
	 * @param shi
	 * @return
	 */
	public static List getTuan2Ken(IBaseDAO db, String ken) {
		IMap condition = BeanFactory.getClassBean("com.baseorganization");
		condition.put("parentId", ken);
		condition.put("iLevel", "4");
		condition.put("isValid", Constants.ISVALID);
		OrderBean orderBean = new OrderBean();
		orderBean.put("orderby", OrderBean.ASC);
		List<IMap> resultList = db.getList(condition, orderBean);
		return resultList;

	}

	/**
	 * 跟据组织ID获取组织结构信息
	 * 
	 * @param db
	 * @param organizationDetailId
	 *            组织结构Id
	 * @return
	 */
	public static List getBaseOrganization(IBaseDAO db, String orgId) {
		IMap condition = BeanFactory.getClassBean("com.baseorganization");
		condition.put("baseOrganizationId", orgId);
		condition.put("isValid", Constants.ISVALID);
		return db.getList(condition, null);
	}

	public static String getOrgId(IBaseDAO db, String organizationDetailId) {
		IMap<String, Object> orgDetail = BeanFactory
				.getClassBean("com.organizationdetali");
		orgDetail.put("organizationDetailId", organizationDetailId);
		orgDetail.put("isValid", Constants.ISVALID);
		orgDetail = db.get(orgDetail);
		if (orgDetail == null) {
			throw new BusinessException("组织信息不存在！" + organizationDetailId);
		}
		int i = Integer.parseInt((String) orgDetail.get("type"));
		if (i == 0) {
			return (String) orgDetail.get("organizationDetailId");
		} else {
			return getOrgId(db, (String) orgDetail.get("parentId"));
		}
	}

	public static String getCYDWOrgId(IBaseDAO db, String organizationDetailId) {
		IMap<String, Object> orgDetail = BeanFactory
				.getClassBean("com.organizationdetali");
		orgDetail.put("organizationDetailId", organizationDetailId);
		orgDetail.put("isValid", Constants.ISVALID);
		orgDetail = db.get(orgDetail);
		if (orgDetail == null) {
			throw new BusinessException("组织信息不存在！" + organizationDetailId);
		}
		int i = Integer.parseInt((String) orgDetail.get("type"));
		if (i == 2) {
			return (String) orgDetail.get("organizationDetailId");
		} else {
			return getCYDWOrgId(db, (String) orgDetail.get("parentId"));
		}
	}

	public static String getXFDWOrgId(IBaseDAO db, String organizationDetailId) {
		IMap<String, Object> orgDetail = BeanFactory
				.getClassBean("com.organizationdetali");
		orgDetail.put("organizationDetailId", organizationDetailId);
		orgDetail.put("isValid", Constants.ISVALID);
		orgDetail = db.get(orgDetail);
		if (orgDetail == null) {
			throw new BusinessException("组织信息不存在！" + organizationDetailId);
		}
		int i = Integer.parseInt((String) orgDetail.get("type"));
		if (i == 3) {
			return (String) orgDetail.get("organizationDetailId");
		} else {
			return getXFDWOrgId(db, (String) orgDetail.get("parentId"));
		}
	}

	public static String getZDDWOrgId(IBaseDAO db, String organizationDetailId) {
		IMap<String, Object> orgDetail = BeanFactory
				.getClassBean("com.organizationdetali");
		orgDetail.put("organizationDetailId", organizationDetailId);
		orgDetail.put("isValid", Constants.ISVALID);
		orgDetail = db.get(orgDetail);
		if (orgDetail == null) {
			throw new BusinessException("组织信息不存在！" + organizationDetailId);
		}
		int i = Integer.parseInt((String) orgDetail.get("type"));
		if (i == 4) {
			return (String) orgDetail.get("organizationDetailId");
		} else {
			return getZDDWOrgId(db, (String) orgDetail.get("parentId"));
		}
	}

	public static IMap getOrgMap(IBaseDAO db, String organizationDetailId) {
		IMap<String, Object> orgDetail = BeanFactory
				.getClassBean("com.organizationdetali");
		orgDetail.put("organizationDetailId", organizationDetailId);
		orgDetail.put("isValid", Constants.ISVALID);
		String mysort = "";

		orgDetail = db.get(orgDetail);
		if (orgDetail == null) {
			throw new BusinessException("组织信息不存在！" + organizationDetailId);
		}
		int i = Integer.parseInt((String) orgDetail.get("type"));
		if (i == 0) {
			return orgDetail;
		} else {
			return getOrgMap(db, (String) orgDetail.get("parentId"));
		}
	}

	public static void checkChild(IBaseDAO db, String organizationDetailId) {
		// 检查有没有子节点
		IMap childCondition = BeanFactory
				.getClassBean("com.organizationdetali");
		childCondition.put("isValid", Constants.ISVALID);
		childCondition.put("parentId", organizationDetailId);
		List child = db.getList(childCondition, null);
		if (null != child && !child.isEmpty() && child.size() > 0) {
			throw new BusinessException("请先删除子节点！");
		}
	}

	/**
	 * 检查数据权限的操作
	 * 
	 * @param user
	 * @param dataOrgId
	 */
	public static void checkDataAuth(IMap user, IMap data, String orgField) {
		if (Util.checkNull(orgField)) {
			orgField = "baseOrganizationId";
		}
		checkDataAuth(user, (String) data.get(orgField));
	}

	public static void checkDataAuth(IMap user, String dataOrgId) {
		if (!((String) user.get("baseorganizationid")).equals(dataOrgId)) {
			throw new BusinessException("您无权操作该数据！");
		}
	}

	/**
	 * 检查user是否存在
	 * 
	 * @param user
	 */
	public static boolean checkUserMapExists(IMap user) {
		boolean r = false;
		if (user != null) {
			r = Util.checkNotNull(user.get("userid"));
		}
		return r;
	}

	/**
	 * 检查user是否存在
	 * 
	 * @param user
	 */
	public static void checkUserMapExists4throw(IMap user) {
		boolean r = false;
		if (user != null) {
			r = Util.checkNotNull(user.get("userid"));
		}
		if (!r) {
			throw new BusinessException("非法用户！");
		}
	}

	/**
	 * 检查数据状态权限的操作
	 * 
	 * @param user
	 * @param dataOrgId
	 */
	public static void checkDataState(String[] state, IMap data, String field) {
		boolean bl = false;
		for (int i = 0; i < state.length; i++) {
			bl = state[i].equals((String) data.get(field));
			if (bl) {
				break;
			}
		}
		if (!bl) {
			throw new BusinessException("您不能操作该数据！");
		}
	}

	/**
	 * 检查
	 * 
	 * @param db
	 * @param postId
	 */
	public static void checkPostChild(IBaseDAO db, String postId) {
		// 检查岗位中是否有人员
		IMap postUserCondition = BeanFactory.getClassBean("com.PostUser");
		postUserCondition.put("isValid",Constants.ISVALID);
		postUserCondition.put("postId", postId);
		List postUserList = db.getList(postUserCondition, null);
		if (postUserList.size() > 0) {
			throw new BusinessException("请先删除岗位下的人员！");
		}
	}

	/**
	 * @Description 检查该项数据是否已经存在
	 * @param
	 * @param checkDate
	 *            检查的IMap条件，一定要又className
	 * @param
	 * @return false代表数据库中不存在，true表示数据库中存在
	 * @return Boolean
	 * @author zhangbo
	 * @date Apr 9, 2012 11:48:07 AM
	 */

	public static void checkDataExists(IBaseDAO db, IMap checkDate) {
		checkDate = (IMap) db.get(checkDate);
		if (checkDate != null) {
			throw new BusinessException("编号已存在！");
		}
	}

	/**
	 * 验证数据是否存在，不存在出异常
	 * 
	 * @param db
	 * @param checkDate
	 * @return
	 */
	public static IMap checkDataNotExists(IBaseDAO db, IMap checkDate) {
		checkDate = (IMap) db.get(checkDate);
		if (checkDate == null) {
			throw new BusinessException("该数据不存在!");
		}
		return checkDate;
	}

	/**
	 * 添加备注信息
	 * 
	 * @param db
	 *            数据DAO
	 * @param user
	 *            用户信息
	 * @param type
	 *            备注类型，自定义
	 * @param date
	 *            处理时间，为空则当前时间
	 * @param text
	 *            备注内容
	 * @param tablename
	 *            业务表名称
	 * @param objId
	 *            业务表id
	 * @param complateState
	 *            完成状态 0 未完成 1完成，也可自定义使用
	 * @param instanceId
	 *            工作流实例id 可以不填
	 * @param stepId
	 *            工作流步骤id 可以不填
	 * @param nodeId
	 *            工作流环节id 可以不填
	 */
	public static void addRemarkInfo(IBaseDAO db, IMap user, String type,
			String date, String text, String objId, String tablename,
			String complateState, String instanceId, String stepId,
			String nodeId) {
		// 添加备注信息表
		IMap<String, Object> remarkInfo = BeanFactory
				.getClassBean("com.RemarkInfo");
		String rid = HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(),
				"com.RemarkInfo");
		// 备注ID
		remarkInfo.put("id", rid);
		// 类型
		remarkInfo.put("type", type);
		// 处理时间
		HelperDB
				.setDate("checkDate", remarkInfo, DateTimeUtil.toDate(date, ""));
		// 备注
		remarkInfo.put("remark", text);
		// 完成状态0 未完成1 完成
		remarkInfo.put("complateState", complateState);
		// 基本信息表id
		remarkInfo.put("baseInfoId", objId);
		// 业务类型
		remarkInfo.put("operationType", tablename);
		// 流程实例ID
		remarkInfo.put("instanceId", instanceId);
		// 任务ID
		remarkInfo.put("stepId", stepId);
		// 环节id
		remarkInfo.put("nodeId", nodeId);

		HelperDB.setCreate4isValid(HelperApp.getUserName(user), remarkInfo);

		db.insert(remarkInfo);
	}

	/**
	 * 发起工作流
	 * 
	 * @param db
	 *            数据对象
	 * @param userMap
	 *            用户信息
	 * @param processId
	 *            流程编码
	 * @param objId
	 *            对象id
	 * @param tableName
	 *            表名
	 * @param contTitle
	 *            标题
	 * @param url
	 *            页面url
	 * @param processType
	 *            流程类型
	 * @param wfMap
	 *            自定义参数
	 * @return
	 */
	public static long wf_start(IBaseDAO db, IMap userMap, String processId,
			String objId, String tableName, String contTitle, String url,
			int processType, IMap wfMap) {
		// 发起工作流
		IMap<String, Object> statMap = new DataMap<String, Object>();
		// 调用者
		statMap.put("caller", userMap.get("userId"));
		// 关联id
		statMap.put("objId", objId);
		// 表名
		statMap.put("tableName", tableName);
		// 标题
		statMap.put("contTitle", contTitle);
		// 链接
		statMap.put("url", url);
		// 流程类型
		statMap.put("processType", processType);
		// 所属上级组织编号
		statMap.put("orgId", userMap.get("orgId"));
		// 自定义参数放入
		statMap.putAll(wfMap);
		long m = WfHelper.start(db, processId, (String) userMap.get("userId"),
				statMap);
		return m;
	}

	/**
	 * 
	 * @param db
	 * @param userMap
	 * @param processId
	 * @param objId
	 * @param tableName
	 * @param contTitle
	 * @param url
	 * @param processType
	 * @return
	 */
	public static void wf_next(IBaseDAO db, IMap userMap, String instanceId,
			String stepId, String nodeId, IMap wfmap) {
		List<ActionDescriptor> actionDescriptorList = WfHelper.getActionInfo(
				db, Long.valueOf(instanceId), (String) userMap.get("userId"),
				Integer.parseInt(stepId));
		int actionid = actionDescriptorList.get(0).getId();
		WfHelper.next(Long.valueOf(instanceId), actionid, (String) userMap
				.get("userId"), wfmap, db, Long.valueOf(nodeId));
	}
	public static void wf_next(IBaseDAO db, IMap userMap, String instanceId,String stepId,String nodeId, String actionid,IMap wfmap) {
		WfHelper.next(Long.valueOf(instanceId), Integer.valueOf(actionid), (String) userMap
				.get("userId"), wfmap, db, Long.valueOf(nodeId));
	}
	/**
	 * 获取自动生成code
	 * 
	 * @param key
	 *            编码字段名称
	 * @param tableName
	 *            表名名称
	 * @param db
	 * @return
	 */
	public static String getAutoCode(String packageName, String key,
			String tableName, IBaseDAO db) {
		DBBean bean = BeanFactory.getDbBean(tableName);
		if (bean == null) {
			throw new BusinessException("类名称不正确！" + tableName);
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String nowTime = df.format(new Date());
		nowTime = nowTime.toLowerCase();
		IMap in = new DataMap();
		in.put("key", key);
		in.put("tableName", bean.getTableName());
		in.put("id", packageName + nowTime);
		IMap out = db.get(in, "getMaxPrimary", "");
		String maxkey = (String) out.get("maxkey");
		System.out.println(maxkey);
		Integer i = 0;
		if (HelperApp.isEmpty(maxkey)) {
			i = 0;
		} else {
			maxkey = maxkey.replaceFirst(packageName + nowTime, "").trim();
			i = Integer.parseInt(maxkey);
		}
		i++;
		String code = i.toString();
		while (code.length() < 3) {
			code = "0" + code;
		}

		return code = packageName + nowTime + code;
	}

	/**
	 * 生成空json
	 * 
	 * @param name
	 * @return
	 */
	public static String cerateEmptyJson(String name) {
		IMap m = new DataMap();
		JSONObject j = JSONObject.fromObject(m);
		return j.toString();
	}

	/**
	 * json 转imap
	 * 
	 * @param json
	 * @return
	 */
	public static IMap json2IMap(Map json) {
		IMap m = new DataMap();
		if (json != null) {
			m.putAll(json);
		}
		return m;
	}

	/**
	 * json 转imap
	 * 
	 * @param json
	 * @return
	 */
	public static IMap json2IMap(String json) {
		IMap m = new DataMap();
		if (Util.checkNotNull(json)) {
			JSONObject o = JSONObject.fromObject(json);
			m.putAll(o);
		}
		return m;
	}

	public static List json2List(String json) {
		List list = new ArrayList();
		if (Util.checkNotNull(json)) {
			JSONArray o = JSONArray.fromObject(json);
			list.addAll(o);
		}
		return list;
	}

	public static boolean checkRepeat(String fieldname, String value,
			String classname, String idName, String id, IBaseDAO db) {
		IMap temp = BeanFactory.getClassBean(classname);
		if (Util.checkNotNull(value)) {
			temp.put(fieldname, value);
			temp.put("isValid", Constants.ISVALID);
			List list = db.getList(temp, null);
			if (Util.checkNotNull(id)) {
				// 修改检查
				if (list.size() > 1) {
					throw new BusinessException("添加失败，编码不能重复！");
				} else if (list.size() == 1) {
					if (!id.equals(((IMap) list.get(0)).get(idName))) {
						throw new BusinessException("添加失败，编码不能重复！");
					}
				}
			} else {
				if (list.size() > 0) {
					throw new BusinessException("添加失败，编码不能重复！");
				}
			}

		}
		return false;
	}

}
