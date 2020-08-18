package com.u2a.framework.service.sys;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.brick.api.Service;
import com.brick.dao.IBaseDAO;
import com.brick.dao.Page;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.manager.BeanFactory;
import com.brick.util.MapUtils;
import com.brick.util.PageUtils;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;
/**
 * 用户组管理
 * 
 * @author 刘广
 * @filename UserGroupService.java
 * @date Jul 13, 201010:13:05 AM
 */
@SuppressWarnings("unchecked")
public class UserGroupService extends Service {
	public IBaseDAO getBaseDAO() {
		IBaseDAO baseDAO = (IBaseDAO) super.db;
		return baseDAO;
	}

	/**
	 * 用户组添加
	 * 
	 * @UserGroupService.java
	 * @Jul 13, 20101:30:28 PM
	 * @param in
	 * @userGroupName 用户组名称
	 * @userGroupRemark 用户组备注
	 * @userGroup 用户组对象
	 * @return
	 * @res 提示信息
	 */
	public IMap saveUserGroup(IMap in) {
		IMap result = new DataMap();
		// 参数处理
		IMap userGroup = (IMap) in.get("userGroup");
	   if(in.get("userMap")!=null){
		userGroup.put("createUser",  ((IMap)in.get("userMap")).get("userName"));
	   }
	   HelperDB.setDateTime("createDate", userGroup, new Date());
		userGroup.put("userGroupId",  HelperApp.getAutoIncrementPKID(HelperApp.getPackageName(),"com.UserGroup"));
		getBaseDAO().insert(userGroup);
		result.put("method.infoMsg", "添加成功");
		result.put("method.url", in.get("url"));
		return result;
	}
	/**
	 * 用户组列表
	 * 
	 * @UserGroupService.java
	 * @Jul 13, 20105:19:39 PM
	 * @param in
	 * @userGroupId用户组id
	 * @currentPageNO 当前页
	 * @request
	 * @return 用户组列表集合 ${page.resultList}
	 */
	public IMap getUserGroupList(IMap in) {
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		IMap result = new DataMap();
		IMap input = BeanFactory.getClassBean("com.UserGroup");
			Page page = getBaseDAO().pageQuery(input, "usergrouplist",
					"com.UserGroup", in.get("currentPageNO") == null ? 1 : Integer.parseInt(in.get("currentPageNO").toString()) , 10);
			page.setAction(PageUtils.getPagePathRequireAttribute(request,new String[] {"userGroupId" }));
			result.put("page", page);
		return result;
	}
	/**
	 * 用户组添加
	 * @param in
	 * @return
	 */
	public IMap addUserGroup(IMap in) {
		IMap result = new DataMap();
		return result;
	}

	/**
	 * 查询所有用户组
	 * @param in
	 * @return
	 */
	public IMap getAllUserGroup(IMap in) {
		IMap result = new DataMap();
		
		// 查询所有用户组
//		List<IMap> allUserGroup = getBaseDAO().getList(
//				null, "queryAllAuthList", "com.UserGroup");
		List<IMap> allUserGroup = null;
		result.put("allUserGroup", allUserGroup);
		
		return result;
	}

	/**
	 * 修改用户组信息
	 * 
	 * @UserGroupService.java
	 * @Jul 14, 20103:40:41 PM
	 * @param in
	 * @userGroupId用户组id
	 * @return
	 */
	public IMap goModifyUserGroup(IMap in) {
		IMap result = new DataMap();
		IMap input = BeanFactory.getClassBean("com.userGroup");
		MapUtils.copyMapRemoveField(in, null, input);
		IMap userGroup = getBaseDAO().get(input);
		result.put("userGroup", userGroup);
		return result;
	}

	/**
	 * 用户组修改
	 * 
	 * @UserGroupService.java
	 * @Jul 14, 20103:40:41 PM
	 * @param in
	 * @userGroupName 用户组名称
	 * @userGroupRemark 用户组备注
	 * @userGroup 用户组对象
	 * @return
	 * @res 提示信息
	 */
	public IMap modifyUserGroup(IMap in) {
		IMap result = new DataMap();
		// 参数处理
		IMap userGroup = (IMap) in.get("userGroup");
		getBaseDAO().update(userGroup);
		result.put("method.infoMsg", "修改成功");
		result.put("method.url", in.get("url"));
		return result;
	}

	/**
	 * 用户组删除
	 * 
	 * @UserGroupService.java
	 * @Jul 14, 20105:02:27 PM
	 * @param in
	 * @userGroupId用户组id
	 * @return
	 */
	public IMap removeUserGroup(IMap in) {
		IMap result = new DataMap();
		// 参数处理
		IMap userGroup = BeanFactory.getClassBean("com.userGroup");
		userGroup.put("userGroupId", in.get("usergroupid"));
		getBaseDAO().delete(in, "delusergroupmem");//级联删除用户组成员
	    getBaseDAO().delete(in, "delusergroupauth");//级联删除用户组权限
		// IMap userGroup = (IMap) in.get("userGroup");
		getBaseDAO().delete(userGroup);
		result.put("method.infoMsg", "删除成功");
		result.put("method.url", in.get("url"));
		return result;
	}
	
	/**
	 * 添加用户组成员列表
	 * 
	 * @UserGroupService.java
	 * @Jul 15, 20109:35:46 AM
	 * @param in
	 * @userGroupId用户组id
	 * @currentPageNO 当前页
	 * @request
	 * @return 用户组成员列表集合 ${page.resultList}
	 */
	public IMap getAddUserList(IMap in) {
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		IMap result = new DataMap();
		Page page = getBaseDAO().pageQuery(in, "adduserlist", "com.UserInfo",in.get("currentPageNO") == null ? 1 : Integer.parseInt(in.get("currentPageNO").toString()), 10);
		page.setAction(PageUtils.getPagePathRequireAttribute(request,new String[] { "usergroupid" }));
		result.put("addugurl", in.get("url"));
		result.put("page", page);
		return result;
	}

	/**
	 * 用户组成员搜索
	  @UserGroupService.java
	  @Jul 18, 20101:27:34 PM
	 * @param in
	 * @return
	 */
	public IMap<String,Object> userMembList(IMap<String,Object> in){
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		IMap result = new DataMap();
		Page page = getBaseDAO().pageQuery(in,"searchusermemsql","com.UserInfo",in.get("currentPageNO") == null ? 1 : Integer.parseInt(in.get("currentPageNO").toString()),10);
		page.setAction(PageUtils.getPagePathRequireAttribute(request,new String[] { "usergroupid","userName","email","starttime","endtime"}));
		result.put("addugurl", in.get("url"));
		result.put("page", page);
		return result;
	}
	/**
	 * 用户组成员列表
	 * 
	 * @UserGroupService.java
	 * @Jul 15, 20109:35:46 AM
	 * @param in
	 * @userGroupId用户组id
	 * @currentPageNO 当前页
	 * @request
	 * @return 用户组成员列表集合 ${page.resultList}
	 */
	public IMap getUserList(IMap in) {
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		IMap result = new DataMap();
		Page page = getBaseDAO().pageQuery(in, "userlist", "com.UserInfo",
				in.get("currentPageNO") == null ? 1 : Integer.parseInt(in.get("currentPageNO").toString()), 10);
		page.setAction(PageUtils.getPagePathRequireAttribute(request,
				new String[] {"usergroupid" }));
		result.put("ubackurl", in.get("url"));
		result.put("page", page);
		return result;
	}

	/**
	 * 用户组添加
	 * 
	 * @UserGroupService.java
	 * @Jul 13, 20101:30:28 PM
	 * @param in
	 * @userGroupId 用户组id
	 * @UserGroupMember 用户组成员对象
	 * @userId 用户id
	 * @return
	 * @res 提示信息
	 */
	public IMap saveUserGroupMember(IMap in) {
		@SuppressWarnings("unused")
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		IMap result = new DataMap();
		// 参数处理
		IMap userGroup = (IMap) in.get("userGroupMember");
		getBaseDAO().insert(userGroup);
		result.put("method.infoMsg", "添加成功");
		result.put("method.url", in.get("url"));
		// request.getSession().setAttribute("mbackurl", in.get("burl"));
		return result;
	}
	
	/**
	 * 用户组成员删除
	 * 
	 * @UserGroupService.java
	 * @Jul 14, 20105:02:27 PM
	 * @param in
	 * @userGroupId用户组id
	 * @return
	 */
	public IMap removeUserGroupMember(IMap in) {
		IMap result = new DataMap();
		// 参数处理
		IMap userGroupMember = (IMap) in.get("userGroupMember");
		getBaseDAO().delete(userGroupMember);
		result.put("method.infoMsg", "删除成功");
		result.put("method.url", in.get("url"));
		return result;
	}
	
	/**
	 * 给用户组授权
	  @UserGroupService.java
	  @Jul 16, 20103:06:49 PM
	 * @param in  @userGroupId用户组id
	 * @return
	 */
	public IMap goUserGroupPower(IMap in) {
		IMap result=new DataMap();
		IMap groupPower = BeanFactory.getClassBean("com.AuthGroup");
        List<IMap> groupPowerList =  getBaseDAO().getList(groupPower, null);//所有的权限组
        result.put("backurl", in.get("url"));
        IMap userGroup1 = (IMap) in.get("userGroup");
    	IMap  userGroup=getBaseDAO().get(userGroup1);
    	//已有的权限组
    	List<IMap> userGroupPowerList =  getBaseDAO().getList(userGroup1, "usergrouppowerlist", "com.AuthGroup");
    	result.put("userGroupPowerList", userGroupPowerList);
		result.put("groupPowerList", groupPowerList);
		result.put("userGroup", userGroup);
		return result;
	}
	/**
	 * 保存用户组权限
	 @Jul 17, 201010:55:35 AM
	 * @param in  @userGroupId用户组id @userGroupPowerId权限组id
	 * @return
	 */
	public IMap saveUserGroupPower(IMap in) {
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		IMap result = new DataMap();
		String []ss=request.getParameterValues("authgroupid");
		//删除已有的权限组
			List<IMap> userGroupPowerList =  getBaseDAO().getList(in, "getgrouppowerlist", "com.UserGroupAuth");
			if(userGroupPowerList!=null && !userGroupPowerList.isEmpty()){
				for (int j = 0; j < userGroupPowerList.size(); j++) {
					getBaseDAO().delete(userGroupPowerList.get(j));
				}
			}
		if(ss!=null && ss.length>0){
		for (int i = 0; i < ss.length; i++) {//添加新权限组
				IMap userGroupPower = BeanFactory.getClassBean("com.UserGroupAuth");
				userGroupPower.put("userGroupId", in.get("usergroupid"));
				userGroupPower.put("authGroupId", ss[i]);
				getBaseDAO().insert(userGroupPower);
			}
		result.put("method.infoMsg", "添加成功");
		}
		result.put("method.url", in.get("url"));
	   return result;
	}
	
	/**
	 * 添加快捷方式
	  @UserGroupService.java
	  @Jul 17, 201010:59:00 AM
	 * @param in @userid用户id @authid权限id
	 * @return
	 */
	public IMap goAddUserQuick12(IMap in) {
	    IMap result = new DataMap();
		 List<IMap> userQuickList =  getBaseDAO().getList(in, "getquicklist", "com.QuickOperation");
			List<IMap> authList=getBaseDAO().getList(result,"urlauthlist","com.AuthInfo");
			result.put("userQuickList", userQuickList);
			result.put("authList", authList);
		 return result;
	}
	
	/**
	 * 跳转添加用户快捷方式
	  @UserGroupService.java
	  @Aug 19, 201010:04:17 AM
	 * @param in
	 * @return
	 */	
	public IMap goAddUserQuick(IMap in) {
		IMap result = new DataMap();
		IMap paramMap = (IMap) in.get("auth");
		String className = paramMap.getClassName();
		in.put("authId", "-1");// 查询所有权限
		List<IMap> authList = getBaseDAO().getList(in, "quickauthlist", className);
		authList = getAuthList(authList, className);
	 List<IMap> userQuickList =  getBaseDAO().getList(in, "getquicklist", "com.QuickOperation");
		result.put("userQuickList", userQuickList);
		result.put("quickAuthList", authList);
		return result;
	}
	/**
	 * 递归查询子权限
	 * @param parentList
	 * @return
	 */
	private List<IMap> getAuthList(List<IMap> parentList, String className) {
		for (IMap map : parentList) {

			IMap map1=map;
			List<IMap> authList = getBaseDAO().getList(map, "quickauthlist", className);
			if (authList.size() > 0) {
				map.put("childList", authList);
				getAuthList(authList, className);
			}
		}
		return parentList;
	}	
	/**
	 * 保存快捷方式
	  @UserGroupService.java
	  @Jul 17, 201010:59:00 AM
	 * @param in @userid用户id @authid权限id
	 * @return
	 */
	public IMap saveUserQuick(IMap in) {
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		IMap result = new DataMap();
		String []ss=request.getParameterValues("authId");
		//IMap usermap = (IMap) result.get("userMap");
		//  if(usermap!=null && ss!=null && ss.length>0){//删除已有的快捷方式
		
		if(ss!=null && ss.length>0){//删除已有的快捷方式
			List<IMap> userQuickList =  getBaseDAO().getList(in, "getquicklist", "com.QuickOperation");
			if(userQuickList!=null && !userQuickList.isEmpty()){
				for (int j = 0; j < userQuickList.size(); j++) {
					getBaseDAO().delete(userQuickList.get(j));
				}
			}
		for (int i = 0; i < ss.length; i++) {//添加新快捷方式
				IMap userQuick = BeanFactory.getClassBean("com.QuickOperation");
				//userQuick.put("userId", usermap.get("userId"));
				 if(in.get("userMap")!=null){
				userQuick.put("userId",((IMap)in.get("userMap")).get("userId"));
				}
				userQuick.put("authId", ss[i]);
				getBaseDAO().insert(userQuick);
			}
		result.put("method.infoMsg", "添加成功");
		}
	   result.put("method.url", in.get("url"));
	   return result;
	}


	/**
	 *  right页面 快捷方式展示
	  @UserGroupService.java
	  @Jul 18, 20102:57:01 PM
	 * @param in
	 * @return
	 */
	public IMap goHead(IMap in) {
		IMap result = new DataMap();
		 List<IMap> userQuickList =  getBaseDAO().getList(in, "quicklist", "com.AuthInfo");
		 result.put("userQuickList", userQuickList);
		return result;
	}
}
