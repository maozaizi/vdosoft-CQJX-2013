package com.u2a.framework.service.base.organizationDetali;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.exception.BusinessException;
import com.brick.manager.BeanFactory;
import com.brick.util.Util;
import com.u2a.framework.commons.Constants;
import com.u2a.framework.commons.OperateHelper;
import com.u2a.framework.service.base.dictionary.DictionaryService;
import com.u2a.framework.util.FireNetHelper;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;

  
@SuppressWarnings("unchecked")
public class OrgManagerService extends com.brick.api.Service {
	/**
	 * @Description获取所有的tree节点
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author zhangbo
	 * @date Feb 15, 2012 3:38:53 PM
	 */

	public IMap getTree(IMap in) {
		// 设置结果集
		IMap result = new DataMap();
		// 设置查询条件 查询跟节点
		IMap myitem = BeanFactory.getClassBean("com.baseorganization");
		IMap userMap = (IMap) in.get("user");
		myitem.put("orgCode", userMap.get("orgid"));
		myitem = db.get(myitem);
		List<IMap> orgList = new ArrayList<IMap>();
		orgList.add(myitem);
		orgList = getOrganizationDetaliChildList(orgList);
		
		result.put("organizationDetaliList", orgList);
		
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

	public List<IMap> getOrganizationDetaliChildList(List<IMap> parentList) {
		for (IMap map : parentList) {
			IMap condition = BeanFactory.getClassBean("com.baseorganization");
			List<IMap> baseOrganizationList = new ArrayList<IMap>();
			condition.put("parentId", map.get("id"));
			
			baseOrganizationList = db.getList(condition,
					"getBureauTree", condition.getClassName());
			if (baseOrganizationList.size() > 0) {
				map.put("childList", baseOrganizationList);
				getOrganizationDetaliChildList(baseOrganizationList);
			}
			
		}
		return parentList;
	}

	/**
	 * @Description 添加消防局组织结构
	 * @param
	 * @param 页面输入参数
	 * @param
	 * @return 后台跳转参数
	 * @return IMap
	 * @author zhangbo
	 * @date Feb 20, 2012 5:52:26 PM
	 */

	public IMap addDept(IMap in) {
		IMap result = new DataMap();
		IMap organizationDetali = (IMap) in.get("organizationDetali");
		// 设置父节点ID
		organizationDetali.put("parentId", organizationDetali
				.get("organizationDetailId"));
		// 生成ID
		String organizationDetailId = HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), organizationDetali.getClassName());
		organizationDetali.put("organizationDetailId", organizationDetailId);
		organizationDetali.put("isValid", Constants.ISVALID);
		// 从session中获取用户信息
		IMap userMap = (IMap) in.get("userMap");
		HelperDB.setCreateInfo(HelperApp.getUserName(userMap), organizationDetali);
		db.insert(organizationDetali);
		//日志
//		LogBean log = new LogBean(userMap, (String)organizationDetali.get("organizationDetailId"),(String)organizationDetali.get("organizationDetailName") , organizationDetali.getClassName(),
//				"应用组织表", Constants.opt_add, "");
//		LogRecordsService.saveOperationLog(log, db);
		result.put("method.url", in.get("tempUrl") + "?organizationDetailId="
				+ organizationDetali.get("organizationDetailId")+"&isRefresh=1");
		result.put("method.infoMsg", OperateHelper.getAddMsg());
		return result;
	}


	/**
	 * @Description组织作废
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author zhangbo
	 * @date Feb 21, 2012 3:49:31 PM
	 */

	public IMap delete(IMap in) {
		IMap result = new DataMap();
		IMap organizationDetali = (IMap) in.get("organizationDetali");
		//检测是否有子节点
		FireNetHelper.checkChild(db, (String)organizationDetali.get("organizationDetailId"));
		String url = in.get("tempUrl").toString();

		// 从session中获取用户信息
		IMap userMap = (IMap) in.get("userMap");
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap), organizationDetali);
		organizationDetali.put("isValid", Constants.ISNOTVALID);
		db.update(organizationDetali);
		
		//删除用户  岗位  以及岗位用户关联关系
		IMap postInfoCondition = BeanFactory.getClassBean("com.PostInfo");
		postInfoCondition.put("isValid",Constants.ISVALID);
		postInfoCondition.put("organizationDetailId", organizationDetali.get("organizationDetailId"));
		List<IMap> postList = db.getList(postInfoCondition, null);
		if(postList != null && postList.size() != 0){
			throw new BusinessException("请先删除所有的岗位！");
		}

		IMap userInfoCondition = BeanFactory.getClassBean("com.UserInfo");
		userInfoCondition.put("isValid", Constants.ISVALID);
		userInfoCondition.put("orgDetailId", organizationDetali.get("organizationDetailId"));
		List<IMap> userList = db.getList(userInfoCondition, null);
		if(userList != null && userList.size() != 0){
			throw new BusinessException("请先删除所有的人员！");
		}
		IMap org = BeanFactory.getClassBean(organizationDetali.getClassName());
		org.put("organizationDetailId", organizationDetali.get("organizationDetailId"));
		org =  db.get(org);
		//日志
//		LogBean log = new LogBean(userMap, (String)organizationDetali.get("organizationDetailId"),(String)organizationDetali.get("organizationDetailName") , organizationDetali.getClassName(),
//				"应用组织表", Constants.opt_del, (String)in.get("reason"));
//		LogRecordsService.saveOperationLog(log, db);
		result.put("method.url", url + "?organizationDetailId="
						+ org.get("parentId")+"&isRefresh=1");
		result.put("method.infoMsg", OperateHelper.getDelMsg());
		return result;
	}
	/**
	 * @Description 获取消防局主页面信息
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author zhangbo
	 * @date Mar 13, 2012 9:31:55 AM
	 */

	public IMap getInfo(IMap in) {
		IMap result = new DataMap();
		// 基本信息
		IMap baseorganization = (IMap) in.get("baseorganization");
		// 基本信息
		IMap userInfo = BeanFactory.getClassBean("com.UserInfo");
		// 基本信息
		IMap postInfo = BeanFactory.getClassBean("com.PostInfo");

		// 没有传入应用组织ID的情况
		if (Util.checkNull(baseorganization.get("orgCode"))) {
			return result;
		} else {
			// 查询基本信息
			baseorganization = db.get(baseorganization);
		}
		List<IMap> postInfoList = new ArrayList<IMap>();
		List<IMap> userInfoList = new ArrayList<IMap>();
		if (baseorganization != null) {
			// 查询人员信息organizationDetailId
			userInfo.put("orgId", baseorganization
					.get("orgCode"));
			userInfo.put("isValid", Constants.ISVALID);
			userInfoList = db.getList(userInfo, null);
			// 查询岗位信息
			postInfo.put("orgId", baseorganization
					.get("orgCode"));
			postInfo.put("isValid", Constants.ISVALID);
			postInfoList = db.getList(postInfo, null);
		}

		result.put("baseorganization", baseorganization);
		result.put("userInfoList", userInfoList);
		result.put("postInfoList", postInfoList);
		return result;
	}

	public IMap toAddUserInfo(IMap in) {
		IMap result = new DataMap();
		return result;
	}

	public IMap addUserInfo(IMap in) {
		IMap result = new DataMap();
		IMap userInfo = (IMap) in.get("userInfo");

		// 生成ID
		String userIdnew = HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), userInfo.getClassName());
		userInfo.put("userId", userIdnew);
		String orgId = (String) userInfo.get("orgId");
		
		if (Util.checkNull(orgId)) {
			throw new BusinessException("找不到组织id！" + orgId);
		}
		userInfo.put("orgId", orgId);
		// 从session中获取用户信息
		IMap userMap = (IMap) in.get("userMap");
		HelperDB.setCreate4isValid(HelperApp.getUserName(userMap), userInfo);
		db.insert(userInfo);
		result.put("method.url", in.get("tempUrl") + "?orgCode="
				+ userInfo.get("orgId"));
		result.put("method.infoMsg", OperateHelper.getAddMsg());
		return result;
	}

	public IMap toModifyUserInfo(IMap in) {
		IMap result = new DataMap();
		IMap userInfo = (IMap) in.get("userInfo");
		userInfo = db.get(userInfo);
		if (userInfo.get("birthData") != null) {
			userInfo.put("birthData", userInfo.get("birthData").toString()
					.substring(0, 10));
		}
		result.put("userInfo", userInfo);
		return result;
	}

	public IMap modifyUserInfo(IMap in) {
		IMap result = new DataMap();
		IMap userInfo = (IMap) in.get("userInfo");
		// 从session中获取用户信息
		IMap userMap = (IMap) in.get("userMap");
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap), userInfo);
		db.update(userInfo);
		//日志
//		LogBean log = new LogBean(userMap, (String)userInfo.get("userId"),(String)userInfo.get("name") , userInfo.getClassName(),
//				"用户表", Constants.opt_update, "");
//		LogRecordsService.saveOperationLog(log, db);
		result.put("method.url", in.get("tempUrl"));
		result.put("method.infoMsg", OperateHelper.getUpdateMsg());
		return result;
	}

	public IMap toDeleteUserInfo(IMap in) {
		IMap result = new DataMap();
		IMap userInfo = (IMap) in.get("userInfo");
		userInfo = db.get(userInfo);
		if (userInfo.get("birthData") != null) {
			userInfo.put("birthData", userInfo.get("birthData").toString()
					.substring(0, 10));
		}
		result.put("userInfo", userInfo);
		return result;
	}

	public IMap deleteUserInfo(IMap in) {
		IMap result = new DataMap();
		IMap userInfo = (IMap) in.get("userInfo");
		userInfo.put("isValid", Constants.ISNOTVALID);
		// 从session中获取用户信息
		IMap userMap = (IMap) in.get("userMap");
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap), userInfo);
		
		IMap postUser = BeanFactory.getClassBean("com.PostUser");
		postUser.put("userId", userInfo.get("userId"));
		postUser.put("isValid", "1");
		postUser = db.get(postUser);
		if(Util.checkNotNull(postUser)){
			throw new BusinessException("该用户已经被引用，请先删除引用！");
		}
		db.update(userInfo);
		//日志
//		LogBean log = new LogBean(userMap, (String)userInfo.get("userId"),(String)userInfo.get("userId") , userInfo.getClassName(),
//				"用户表", Constants.opt_del, (String)in.get("reason"));
//		LogRecordsService.saveOperationLog(log, db);
		result.put("method.url", in.get("tempUrl"));
		result.put("method.infoMsg",OperateHelper.getDelMsg());
		return result;
	}

	public IMap toAddPostInfo(IMap in) {
		IMap result = new DataMap();
		IMap groupPower = BeanFactory.getClassBean("com.AuthGroup");
		groupPower.put("isValid", Constants.ISVALID);
        List<IMap> postList =  db.getList(groupPower, null);//所有的权限组
		result.put("postList", postList);
		return result;
	}

	public IMap addPostInfo(IMap in) {
		IMap result = new DataMap();
		IMap postInfo = (IMap) in.get("postInfo");

		IMap postInfoCheck = BeanFactory.getClassBean(postInfo.getClassName());
		postInfoCheck.put("postCode", postInfo.get("postCode"));
		postInfoCheck.put("isValid", Constants.ISVALID);
		postInfoCheck.put("orgId", postInfo.get("orgId"));
		FireNetHelper.checkDataExists(db,postInfoCheck);
		// 生成ID
		String userIdnew = HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), postInfo.getClassName());
		postInfo.put("postId", userIdnew);
		postInfo.put("isValid", Constants.ISVALID);
		// 从session中获取用户信息
		IMap userMap = (IMap) in.get("userMap");
		HelperDB.setCreateInfo(HelperApp.getUserName(userMap), postInfo);
		db.insert(postInfo);
		//日志
//		LogBean log = new LogBean(userMap, (String)postInfo.get("postId"),(String)postInfo.get("postName") , postInfo.getClassName(),
//				"岗位", Constants.opt_add, "");
//		LogRecordsService.saveOperationLog(log, db);
		result.put("method.url", in.get("tempUrl") + "?orgCode="
				+ postInfo.get("orgId"));
		result.put("method.infoMsg", OperateHelper.getAddMsg());
		return result;
	}

	public IMap toModifyPostInfo(IMap in) {
		IMap result = new DataMap();
		IMap groupPower = BeanFactory.getClassBean("com.AuthGroup");
		groupPower.put("isValid",Constants.ISVALID);
        List<IMap> postList =  db.getList(groupPower, null);//所有的权限组
		IMap postInfo = (IMap) in.get("postInfo");
		postInfo = db.get(postInfo);
		result.put("postList", postList);
		result.put("postInfo", postInfo);
		return result;
	}

	public IMap modifyPostInfo(IMap in) {
		IMap result = new DataMap();
		IMap postInfo = (IMap) in.get("postInfo");
		// 从session中获取用户信息
		IMap userMap = (IMap) in.get("userMap");
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap), postInfo);
		db.update(postInfo);
		//日志
//		LogBean log = new LogBean(userMap, (String)postInfo.get("postId"),(String)postInfo.get("postName") , postInfo.getClassName(),
//				"岗位", Constants.opt_update, "");
//		LogRecordsService.saveOperationLog(log, db);
		result.put("method.url", in.get("tempUrl") + "?orgCode="
				+ postInfo.get("orgId"));
		result.put("method.infoMsg", OperateHelper.getUpdateMsg());
		return result;
	}

	public IMap toDeletePostInfo(IMap in) {
		IMap result = new DataMap();
		IMap postInfo = (IMap) in.get("postInfo");
		postInfo = db.get(postInfo);
		result.put("postInfo", postInfo);
		return result;
	}

	public IMap deletePostInfo(IMap in) {
		IMap result = new DataMap();
		IMap postInfo = (IMap) in.get("postInfo");

		//检查岗位中是否有人员
		FireNetHelper.checkPostChild(db,(String)postInfo.get("postId"));
		
		postInfo.put("isValid",Constants.ISNOTVALID);
		// 从session中获取用户信息
		IMap userMap = (IMap) in.get("userMap");
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap), postInfo);
		db.update(postInfo);
		//日志
//		LogBean log = new LogBean(userMap, (String)postInfo.get("postId"),(String)postInfo.get("postId") , postInfo.getClassName(),
//				"岗位", Constants.opt_del,(String)in.get("reason"));
//		LogRecordsService.saveOperationLog(log, db);
		result.put("method.url", in.get("tempUrl"));
		result.put("method.infoMsg", OperateHelper.getDelMsg());
		return result;
	}

	/**
	 * @Description 获取岗位用户列表 页面跳转
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author zhangbo
	 * @date Mar 12, 2012 4:01:13 PM
	 */

	public IMap toManagerPost(IMap in) {
		IMap result = new DataMap();
		IMap postInfo = (IMap) in.get("postInfo");
		// 获取岗位信息
		postInfo = db.get(postInfo);
		
		IMap userInfo = BeanFactory.getClassBean("com.UserInfo");

		userInfo.put("postId", postInfo.get("postid"));
		userInfo.put("orgId", postInfo.get("orgId"));
		List<IMap> userInfoList = db.getList(userInfo, "getPostUserList",
				userInfo.getClassName());
		// 循环用户列表，如果岗位ID为空，则改人员没有加入到该岗位，将其敢为id置为空
		for (IMap item : userInfoList) {
			if (item.get("postId") == null) {
				item.put("postId", "");
			}
		}
		result.put("userInfoList", userInfoList);
		result.put("postId", postInfo.get("postId").toString());
		result.put("orgId", postInfo.get("orgId"));
		return result;
	}

	public IMap deletePost(IMap in) {
		IMap result = new DataMap();
		IMap postUser = (IMap) in.get("postUser");
		String orgId = in.get("orgId").toString();
		String tempUrl = in.get("tempUrl").toString();
		postUser.put("isValid", Constants.ISNOTVALID);
		// 从session中获取用户信息
		IMap userMap = (IMap) in.get("userMap");
		HelperDB.setModifyInfo(HelperApp.getUserName(userMap), postUser);
		db.update(postUser);
		//日志
//		LogBean log = new LogBean(userMap, (String)postUser.get("postUserId"),(String)postUser.get("postUserId") , postUser.getClassName(),
//				"岗位人员关系", Constants.opt_del, (String)in.get("reason"));
//		LogRecordsService.saveOperationLog(log, db);
		result.put("method.url", tempUrl + "?orgCode="
				+ orgId);
		result.put("method.infoMsg", OperateHelper.getDelMsg());
		return result;
	}

	public IMap joinPostUser(IMap in) {
		IMap result = new DataMap();
		IMap postUser = (IMap) in.get("postUser");
		String tempUrl = in.get("tempUrl").toString();
		String orgId = in.get("orgId").toString();
		// 从session中获取用户信息
		IMap userMap = (IMap) in.get("userMap");
		// 在查询之前保存前台传来的元数据 需要添加的岗位人员信息
		IMap postUserToAdd = postUser;

		// 查看数据库中是否已经存在该数据，只是被标示无效
		postUser = db.get(postUser);
		// 如果没有该条数据 就添加一条
		if (postUser == null || postUser.isEmpty()) {
			// 用保存的元数据进行数据添加
			addCreateAndModify(postUserToAdd, userMap, "postUserId", "1");
		} else {
			// 如果有这条数据 就修改词条数据为 有效
			addCreateAndModify(postUser, userMap, "postUserId", "2");
		}
		result.put("method.url", tempUrl + "?id="
				+ orgId);
		result.put("method.infoMsg", OperateHelper.getAddMsg());
		return result;
	}

	/**
	 * @Description 添加 创建时间，修改时间，创建人，修改人，创建主键，设置数据有效
	 * @param
	 * @param in
	 *            要添加的Bean
	 * @param
	 * @param userMap
	 *            用户
	 * @param
	 * @param idField
	 *            要添加的Bean主键字段
	 * @param
	 * @param type
	 *            类型：1添加，2修改
	 * @return void
	 * @author zhangbo
	 * @date Mar 13, 2012 2:13:07 PM
	 */
	public void addCreateAndModify(IMap in, IMap userMap, String idField,
			String type) {
		// 修改和创建时间
		Date now = new Date();
		// 从session中获取用户信息
		String userId = userMap.get("userId").toString();
		String userName = userMap.get("userName").toString();
		if (type.equals("1")) {
			// 生成ID
			String userIdnew = HelperApp.getAutoIncrementPKID(HelperApp
					.getPackageName(), in.getClassName());
			in.put(idField, userIdnew);
		}
		in.put("isValid", Constants.ISVALID);
		// 添加人和修改人
		if (type.equals("1")) {
			in.put("createUser", userName + "/" + userId);
		}
		in.put("modifyUser", userName + "/" + userId);

		if (type.equals("1")) {
			HelperDB.setDateTime("createDate", in, now);
		}
		HelperDB.setDateTime("modifyDate", in, now);
		if (type.equals("1")) {
			db.insert(in);
		} else {
			db.update(in);
		}
	}
	
	
	/** 
	 * @Description 设置用户登陆信息 转向
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author zhangbo
	 * @date Apr 10, 2012 2:26:14 PM
	 */ 
		
	public IMap toSetUserLogin(IMap in){
		IMap result = new DataMap();
		IMap userInfo = (IMap) in.get("userInfo");
		userInfo = db.get(userInfo);
		result.put("userInfo", userInfo);
		return result;
	}
	
	
	/** 
	 * @Description 设置用户登陆信息
	 * @param @param in
	 * @param @return    
	 * @return IMap   
	 * @author zhangbo
	 * @date Apr 10, 2012 2:26:33 PM
	 */ 
		
	public IMap setUserLogin(IMap in){
		IMap result = new DataMap();
		result.put("method.url", in.get("tempUrl"));
		IMap userInfo = (IMap) in.get("userInfo");
		IMap userInfoCondition = BeanFactory.getClassBean(userInfo.getClassName());
		userInfoCondition.put("username", userInfo.get("username")); 
		userInfoCondition.put("isValid", Constants.ISVALID);
		List userList = db.getList(userInfoCondition, null);
		if(null != userList && userList.size() > 0 ){
			if(!((String)userInfo.get("userId")).equals((String)(((IMap)userList.get(0)).get("userId")))){
				result.put("method.infoMsg", "用户名已经存在！");
			}else{
				// 从session中获取用户信息
				IMap userMap = (IMap) in.get("userMap");
				HelperDB.setModifyInfo(HelperApp.getUserName(userMap), userInfo);
				db.update(userInfo);
				result.put("method.infoMsg", "设置成功！");
			}
		}else{
			// 从session中获取用户信息
			IMap userMap = (IMap) in.get("userMap");
			HelperDB.setModifyInfo(HelperApp.getUserName(userMap), userInfo);
			db.update(userInfo);
			result.put("method.infoMsg", "设置成功！");
			
		}
		return result;
	}
}
