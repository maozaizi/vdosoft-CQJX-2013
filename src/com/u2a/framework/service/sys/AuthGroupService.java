package com.u2a.framework.service.sys;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.brick.api.Service;
import com.brick.dao.IBaseDAO;
import com.brick.dao.Page;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.data.OrderBean;
import com.brick.exception.BusinessException;
import com.brick.manager.BeanFactory;
import com.brick.util.PageUtils;
import com.u2a.framework.commons.Constants;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;
@SuppressWarnings("unchecked")
public class AuthGroupService extends Service {

	private IBaseDAO baseDAO;
	
	public IBaseDAO getBaseDAO() {
		baseDAO = (IBaseDAO) super.db;
		return baseDAO;
	}
	
	/**
	 * 查询所有权限组
	 * @param in
	 * @return
	 */
	public IMap getAllAuthGroup(IMap in) {
		IMap result = new DataMap();
		
		// 查询所有权限组
//		List<IMap> allAuthGroup = getBaseDAO().getList(
//				null, "queryAllAuthGroup", "com.AuthGroup");
		List<IMap> allAuthGroup = null;
		result.put("allAuthGroup", allAuthGroup);
		
		return result;
	}
	
	/**
	 * 加载权限组管理列表，包括根据权限组名称进行查询
	 * @param in 参数集合，主要为查询条件：权限组名称及分页信息
	 * @return 权限组列表
	 */

	public IMap getList(IMap in) {
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		IMap result = new DataMap();
		// 查询参数集合
		IMap paramMap = (IMap) in.get("authGroup");
		
		// 排序信息
		OrderBean orderBean = new OrderBean();
		orderBean.put("authGroupId", OrderBean.DESC);
		orderBean.put("authGroupName", OrderBean.DESC);
		
//		try {
			Page page = null;
			
			Integer currentPageNO = in.get("currentPageNO") == null 
					? 1 : Integer.parseInt(in.get("currentPageNO").toString());
			
			// 自定义sql进行模糊分页查询
			page = getBaseDAO().pageQuery(paramMap, 
					"queryAuthGroupList", paramMap.getClassName(), "AUTH_GROUP_ID",
					currentPageNO.intValue(), 10,orderBean);
			
			page.setAction(PageUtils.getPagePathRequireAttribute(request,
					new String[] { "authGroupName" }));
			
			result.put("authGroupName", paramMap.get("authGroupName"));
			result.put("page", page);
			
//		} catch (Exception e) {
//			throw new BusinessException("权限组列表加载失败！");
//		}
		
		return result;
	}
	
	/**
	 * 跳转至权限组添加页面
	 * @param in
	 * @return
	 */
	public IMap gotoAddPage(IMap in) {
		IMap result = new DataMap();
		return result;
	}
	
	/**
	 * 添加权限组
	 * @param in 新增的权限组信息集合
	 * @return
	 */
	public IMap saveAuthGroup(IMap in) {
		IMap result = new DataMap();
		// 获取request
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		// 从session中获取用户信息
		IMap userMap = (IMap) request.getSession().getAttribute("userMap");
		
		IMap paramMap = (IMap) in.get("authGroup");
		// 设置自增主键
		paramMap.put("authGroupId", HelperApp.getAutoIncrementPKID(
				HelperApp.getPackageName(), "com.AuthGroup"));
		HelperDB.setCreate4isValid(HelperApp.getUserName(userMap), paramMap);
		getBaseDAO().insert(paramMap);
		
		result.put("method.infoMsg", "权限组添加成功！");
		result.put("method.url", in.get("url"));
		
		return result;
	}
	
	/**
	 * 跳转至权限组修改页面
	 * @param in 参数信息集合，主要为权限组ID
	 * @return
	 */
	public IMap goModifyAuthGroup(IMap in) {
		IMap result = new DataMap();
		
		IMap paramMap = (IMap) in.get("authGroup");
		
		IMap groupMap = getBaseDAO().get(paramMap);
		
		result.put("authGroup", groupMap);
		
		return result;
	}
	
	/**
	 * 修改权限组
	 * @param in 权限组信息
	 * @return
	 */
	public IMap modifyAuthGroup(IMap in) {
		IMap result = new DataMap();
		
		try {
			IMap paramMap = (IMap) in.get("authGroup");
			IMap userMap = (IMap) in.get("userMap");
			HelperDB.setModifyInfo(HelperApp.getUserName(userMap), paramMap);
			getBaseDAO().update(paramMap);
			result.put("method.infoMsg", "权限组修改成功！");
			result.put("method.url", in.get("url"));
		} catch (Exception e) {
			throw new BusinessException("权限组修改失败！");
		}
		
		return result;
	}
	
//	public IMap isUsed(IMap in) {
//		
//	}
	
	/**
	 * 删除权限组，如果权限组包含有权限则不允许删除
	 * @param in 权限组ID信息
	 * @return
	 */
	public IMap deleteAuthGroup(IMap in) {
		IMap result = new DataMap();
		
		try {
			IMap paramMap = (IMap) in.get("authGroup");
			
			List<IMap> memberList = getMemberList(paramMap,db);
				// 删除权限组成员
				getBaseDAO().update(paramMap, "deleteAuthGroupMemb");
				// 删除权限组
				IMap userMap = (IMap)in.get("userMap");
				HelperDB.setModifyInfo(HelperApp.getUserName(userMap), paramMap);
				paramMap.put("ISVALID",Constants.ISNOTVALID);
				db.update(paramMap);
				//getBaseDAO().delete(paramMap);
				result.put("method.infoMsg", "权限组删除成功！");
				result.put("method.url", in.get("url"));
		}catch (BusinessException e) {
			throw new BusinessException("该权限组已被使用，不能删除！",e);
		} catch (Exception e) {
			throw new BusinessException("系统异常，权限组删除失败！",e);
		}
		
		return result;
	}
	
	private List<IMap> getUserAuthGroupList(IMap in) {
		IMap paramMap = (IMap) in.get("userGroupAuth");
		
		List<IMap> userGroupAuthLsit = getBaseDAO().getList(paramMap,
				"queryUserAuthGroup", paramMap.getClassName());
		
		return userGroupAuthLsit;
	}
	
	public IMap getAuthGroupMebList(IMap in) {
		IMap result = new DataMap();
		
		IMap paramMap = (IMap) in.get("authGroupMember");
		
		List<IMap> usedAuthList = getMemberList(paramMap,db);
		
		result.put("authList", getTreeData(getListAll(in),usedAuthList));
		result.put("authGroupId", paramMap.get("authGroupId"));
		return result;
	}
	
	
	public List<IMap> getTreeData(List<IMap> authList,List<IMap> usedAuthList){
		for(IMap map1 : authList){
			boolean flag = false;
			for(IMap map2 : usedAuthList){
				if(map1.get("authid").equals(map2.get("authid"))){
					flag = true;
				}
			}
			map1.put("checked", flag);

		}
		return authList;
	}
	public static List<IMap> getMemberList(IMap in,IBaseDAO db) {
		IMap m=new DataMap();
		m.putAll(in);
		return db.getList(
				m, "queryAuthGroupMemb", "com.AuthGroupMember");
	}
	
	private List<IMap> getListAll(IMap in) {
		
		IMap paramMap = (IMap) in.get("auth");
		String className = paramMap.getClassName();
		in.put("authId", "-1");
		// 查询所有权限
		List<IMap> authList = getBaseDAO().getList(
				in, "queryAuthList", className);
		List<IMap> resultList = new ArrayList<IMap>();
		getAuthListSimple(authList, className,resultList);
		
		return resultList;
	}
	private List<IMap> getAuthListSimple(List<IMap> parentList, String className,List<IMap> resultList) {
		
		for (int i =0;i<parentList.size();i++) {
			IMap map = (IMap)parentList.get(i);
			IMap map1 = new DataMap();
			map1.put("authtype", map.get("authtype"));
			map1.put("authgrade", map.get("authgrade"));
			map1.put("authid", map.get("authid"));
			map1.put("isvalid", map.get("isvalid"));
			map1.put("authname", map.get("authname"));
			map1.put("authcode", map.get("authcode"));
			map1.put("authorder", map.get("authorder"));
			map1.put("authremark", map.get("authremark"));
			map1.put("rootauthid", map.get("rootauthid"));
			map1.put("parentauthid", map.get("parentauthid"));
			resultList.add(map1);
			List<IMap> authList = getBaseDAO().getList(
					map, "queryAuthList", className);
			
			if (authList.size() > 0) {
				map.put("childList", authList);
				getAuthListSimple(authList, className,resultList);
			}
		}
		
		return parentList;
	}
	
	/**
	 * 递归查询子权限
	 * @param parentList
	 * @return
	 */
	private List<IMap> getAuthList(List<IMap> parentList, String className) {
		
		for (IMap map : parentList) {
			
			List<IMap> authList = getBaseDAO().getList(
					map, "queryAuthList", className);
			
			if (authList.size() > 0) {
				map.put("childList", authList);
				getAuthList(authList, className);
			}
		}
		
		return parentList;
	}
	
	/**
	 * 保存权限组成员信息
	 * @param in 一个权限组ID和一个到多个权限ID
	 * @return
	 */
	public IMap saveAuthGroupMember(IMap in) {
		IMap result = new DataMap();
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		
		IMap paramMap = (IMap) in.get("authGroupMember");
//		try {
			
			getBaseDAO().update(paramMap, "deleteAuthGroupMemb");
			
			String authIds = request.getParameter("authId");
			String[] authArray = authIds.split(",");
			if (authArray != null && authArray.length > 0) {
				for (String authId : authArray) {
					paramMap.put("authId", authId);
					paramMap.put("id", HelperApp.getAutoIncrementPKID(HelperApp
					.getPackageName(), "com.AuthGroupMember"));
					getBaseDAO().insert(paramMap, "saveAuthGroupMemb");
					
					IMap auth= BeanFactory.getClassBean("com.AuthInfo");
					auth.put("authId", authId);
					auth=super.db.get(auth);
					paramMap.put("authId",auth.get("parentAuthId"));
					try {
						getBaseDAO().insert(paramMap, "saveAuthGroupMemb");
					} catch (RuntimeException e) {
					}
					
				}
			}
			
			result.put("method.infoMsg", "权限组成员保存成功！");
			result.put("method.url", in.get("url"));
//		} catch (Exception e) {
//			throw new BusinessException("权限组成员保存失败！");
//		}
		
		return result;
	}
}
