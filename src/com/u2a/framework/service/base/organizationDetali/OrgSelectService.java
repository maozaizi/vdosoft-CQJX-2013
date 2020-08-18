package com.u2a.framework.service.base.organizationDetali;

import java.util.ArrayList;
import java.util.List;

import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.exception.BusinessException;
import com.brick.manager.BeanFactory;
import com.brick.util.Util;
import com.u2a.framework.commons.Constants;

/**
 * 系统名称：新疆生产建设兵团消防网络化信息管理系统 类名称：OrgSelectService 类描述：提供系统中的组织结构，人员，岗位选择
 * 创建人：zhangbo 创建时间：Apr 13, 2012 3:27:52 PM
 */

@SuppressWarnings("unchecked")
public class OrgSelectService extends com.brick.api.Service {
	public IMap toMain(IMap in) {
		IMap result = new DataMap();
		return result;
	}

	/**
	 * @Description 获取组织结构人员，岗位树
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author zhangbo
	 * @date Apr 13, 2012 3:37:43 PM
	 */

	public IMap getTree(IMap in) {
		IMap result = new DataMap();
		final String PRU = (String) in.get("PRU");
		final String selectedType = (String) in.get("selectedType");
		final String closeAble = (String) in.get("closeAble");
		final String fn = in.get("fn") == null ? "" : (String) in.get("fn");

		// 设置查询条件 查询跟节点
		IMap organizationdetali = BeanFactory
				.getClassBean("com.baseorganization");
		organizationdetali.put("parentId", "-1");
		organizationdetali.put("isValid", Constants.ISVALID);
		List<IMap> orgList = db.getList(organizationdetali, null);
		orgList = getOrganizationDetaliChildList(orgList, organizationdetali
				.getClassName());
		orgList = getRealTreeNode(orgList, PRU, selectedType, closeAble, fn);
		result.put("organizationDetaliList", orgList);
		return result;
	}

	public List<IMap> getRealTreeNode(List<IMap> baseNodeList, String PRU,
			String selectedType, String closeAble, String fn) {
		List<IMap> resultList = new ArrayList<IMap>();
		for (IMap map : baseNodeList) {

			IMap imap = new DataMap();
			imap.put("orgName", map.get("orgName"));
			imap.put("parentId", map.get("parentId"));
			imap.put("memo", map.get("memo"));
			imap.put("iLevel", map.get("iLevel"));
			imap.put("orderby", map.get("orderby"));
			imap.put("url", "/api/org_select/getInfo?id=" + map.get("id")
					+ "&PRU=" + PRU + "&selectedType=" + selectedType + "&closeAble=" + closeAble + "&fn=" + fn);

			resultList.add(imap);
			Object objList = map.get("childList");
			if (null != objList) {
				List<IMap> childList = (List<IMap>) objList;
				imap.put("childList", getRealTreeNode(childList, PRU,
						selectedType, closeAble, fn));
			}
		}
		return resultList;
	}

	public List<IMap> getOrganizationDetaliChildList(List<IMap> parentList,
			String className) {
		for (IMap map : parentList) {
			IMap condition = BeanFactory.getClassBean("com.baseorganization");
			List<IMap> baseOrganizationList = new ArrayList<IMap>();
			condition.put("parentId", map.get("id"));
			baseOrganizationList = db.getList(condition, "getBureauTree",
					condition.getClassName());
			if (baseOrganizationList.size() > 0) {
				map.put("childList", baseOrganizationList);
				getOrganizationDetaliChildList(baseOrganizationList, className);
			}
		}
		return parentList;
	}

	public IMap getInfo(IMap in) {
		IMap result = new DataMap();
		// 基本信息
		IMap organization = (IMap) in.get("organization");
		// 基本信息
		IMap userInfo = BeanFactory.getClassBean("com.UserInfo");
		// 基本信息
		IMap postInfo = BeanFactory.getClassBean("com.PostInfo");

		// 没有传入应用组织ID的情况
		if (Util.checkNull(organization.get("id"))) {
			return result;
		} else {
			// 查询基本信息
			organization = db.get(organization);
		}
		List<IMap> postInfoList = new ArrayList<IMap>();
		List<IMap> userInfoList = new ArrayList<IMap>();
		if (organization != null) {
			// 查询人员信息organizationDetailId
			userInfo.put("orgId", organization.get("id"));
			userInfo.put("isValid", Constants.ISVALID);
			userInfoList = db.getList(userInfo, null);
			// 查询岗位信息
			postInfo.put("orgId", organization.get("id"));
			postInfo.put("isValid", Constants.ISVALID);
			postInfoList = db.getList(postInfo, null);
		}
		result.put("organizationDetali", organization);
		result.put("userInfoList", userInfoList);
		result.put("postInfoList", postInfoList);
		return result;
	}
}
