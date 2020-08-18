package com.u2a.framework.service.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.brick.api.Service;
import com.brick.dao.DAO;
import com.brick.dao.Page;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.exception.BusinessException;
import com.brick.manager.BeanFactory;
import com.brick.manager.ContextUtil;
import com.brick.manager.SqlMapClientFactoryBean;
import com.brick.util.MapUtils;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;
import com.u2a.framework.util.MD5;
import com.u2a.framework.util.Util;

@SuppressWarnings("unchecked")
public class UserInfoService extends Service {
	/**
	 * 登陆
	 * 
	 * @param in
	 * @return
	 */
	public IMap login(IMap in) {

		IMap result = new DataMap();// 输出map
		// 获取request
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		// 验证码
		String number = (String) in.get("number");
		String rand01 = (String) in.get("rand01");
		if(!number.equals(rand01)){
			throw new BusinessException("验证码错误！");
		}
		// 加密
		// in.put("userPwd", MD5.createPassword((String)in.get("userPwd")));
		in.put("userPwd", (String) in.get("userPwd"));
		List<IMap> list = super.db.getList(in, "loginsql", "com.UserInfo");

		if (list.size() > 0) {
			IMap<String, Object> user = list.get(0);
			result.put("userMap", user);
			result.put("authList", loadUserAuthList(in, user));

		} else {
			throw new BusinessException("用户名或密码错误！");
		}
		
		result.put("method.url", in.get("url"));
		return result;
	}
	public IMap getAuthTreeData(IMap in) {

		IMap result = new DataMap();// 输出map
		in.put("userPwd", (String) in.get("userPwd"));
		List<IMap> list = super.db.getList(in, "loginsql", "com.UserInfo");

		if (list.size() > 0) {
			IMap<String, Object> user = list.get(0);
			result.put("userMap", user);
			result.put("authList", loadUserAuthList(in, user));
		} else {
			throw new BusinessException("用户名或密码错误！");
		}
		result.put("method.url", in.get("url"));
		return result;
	}
	
	
	/**
	 * @Description 消防局注销
	 * @param
	 * @param in
	 * @return IMap
	 * @author panghaichao
	 * @date Apr 11, 2012 5:17:40 PM
	 */
	public IMap loginOut(IMap in) {
		IMap userMap = (IMap) in.get("userMap");
		if (userMap != null) {
			HttpSession session = (HttpSession) in.get("session");
			session.removeAttribute("userMap");
		}
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		result.put("method.url", in.get("url"));
		return result;
	}


	/**
	 * 组装树结构的用户权限集合
	 * 
	 * @param in
	 *            登录输入信息
	 * @param userMap
	 *            登录用户信息
	 * @param userGroupList
	 *            该用户所在用户组
	 * @return
	 */
	private List<IMap> loadUserAuthList(IMap in, IMap userMap) {

		// 获取request
		@SuppressWarnings("unused")
		HttpServletRequest request = (HttpServletRequest) in.get("request");
		// 从session中获取用户信息
		// 定义变量，过滤相同权限
		IMap allAuthMap = new DataMap();
		try {
			// 根据用户所在权限组查找其权限
			List<IMap> authGroupMember = super.db.getList(userMap,
					"queryUserAuth", "com.AuthInfo");
			// 遍历用户的权限
			for (IMap auth : authGroupMember) {
				String authId = (String) auth.get("authId");
				// 过滤相同权限
				if (allAuthMap.get("authId") == null) {
					allAuthMap.put(authId, auth);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return this.getListAll(in, allAuthMap);
	}

	/**
	 * 
	 * @param in
	 * @param userAuthMap
	 *            用户拥有的所有权限集合
	 * @return
	 */
	private List<IMap> getListAll(IMap in, IMap userAuthMap) {

		// 定义一级权限
		IMap userFirstAuthMap = new DataMap();
		List userAuthList = new ArrayList();

		IMap paramMap = (IMap) in.get("auth");
		String className = paramMap.getClassName();
		in.put("authId", "-1");
		// 查询所有一级权限
		List<IMap> authList = super.db.getList(in, "queryAuthList", className);

		// 遍历系统所有权限，找出用户已分配权限的一级权限集合
		for (IMap authMap : authList) {
			// 用户已有权限集合
			Iterator iter = userAuthMap.keySet().iterator();

			String authId = (String) authMap.get("authId");
			while (iter.hasNext()) {
				// 用户已分配权限
				IMap currMap = (IMap) userAuthMap.get(iter.next());
				if (currMap != null && !currMap.isEmpty()) {
					if (authId.equals(currMap.get("rootAuthId"))) {
						if (userFirstAuthMap.get(authId) == null) {
							userFirstAuthMap.put(authId, authMap);
							userAuthList.add(authMap);
						}
					}
				}
			}
		}

		authList = getAuthList(userAuthList, userAuthMap, className);

		return authList;
	}

	/**
	 * 递归查询子权限
	 * 
	 * @param parentList
	 * @return
	 */

	private List<IMap> getAuthList(List<IMap> parentList, IMap userAuthMap,
			String className) {
		// 遍历父权限集合，查找子权限
		for (IMap map : parentList) {
			// 某权限的子权限集合
			List<IMap> authList = super.db.getList(map, "queryAuthList",
					className);

			// 定义新的子权限集合，用于保存用户子权限
			List<IMap> childList = null;
			if (authList != null && authList.size() > 0) {
				childList = new ArrayList<IMap>();
				// 遍历系统中该权限的所有子权限，用于过滤出用户分配的权限
				for (IMap childMap : authList) {
					// 用户已分配权限的key集合
					Iterator iter = userAuthMap.keySet().iterator();
					String authId = (String) childMap.get("authId");
					// 判断当前权限ID是否与用户已分配权限集合中某个权限相同，
					// 如果相同则添加至自定义的子权限集合中
					while (iter.hasNext()) {
						if (authId.equals(iter.next())) {
							childList.add(childMap);
						}
					}
				}
				map.put("childList", childList);
				getAuthList(childList, userAuthMap, className);
			}
		}

		return parentList;
	}

	/**
	 * 注销
	 */
	public IMap<String, Object> destory(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		IMap userMap = (IMap) in.get("userMap");
		if (userMap != null) {
			ServletContext serlvetContext = (ServletContext) in
					.get("serlvetContext");
			LinkedHashMap<String, Object> onlineUserMap = (LinkedHashMap<String, Object>) serlvetContext
					.getAttribute("OnlineUserMap");
			if (onlineUserMap != null) {
				onlineUserMap.remove((String) userMap.get("userId"));
			}
			HttpSession session = (HttpSession) in.get("session");
			session.removeAttribute("userMap");
			session.removeAttribute("userGroupList");
		}
		HttpServletResponse response = (HttpServletResponse) in.get("response");
		HttpServletRequest request = (HttpServletRequest) in.get("request");

		String url = (String) in.get("url");
		if (!HelperApp.isEmpty(url)) {
			result.put("method.redirect", url);
		}
		return result;
	}

	/**
	 * 注册
	 */

	@SuppressWarnings("unchecked")
	public IMap<String, Object> reg(IMap<String, Object> in) {
		IMap paramMap = (IMap) in.get("user");
		String userName = (String) paramMap.get("userName");
		if (!Util.isUserName(userName)) {
			throw new BusinessException("用户名不合法！");// 校验用户名
		}
		in.put("userName", userName);
		List list = super.db.getList(in, "loginsql", "com.UserInfo");
		if (list.size() > 0) {
			throw new BusinessException("用户名已存在！");// 校验用户名
		}
		// 加密
		paramMap.put("userPwd", MD5.createPassword((String) paramMap
				.get("userPwd")));
		// PKFactory.getAutoIncrementPKID("4")
		paramMap.put("userId", HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.UserInfo"));// 4 userinfo的ID值
		paramMap.put("userName", userName);
		HelperDB.setDate("regDate", paramMap, new Date());
		paramMap.put("loginCount", 0);
		paramMap.put("verifyState", 0);
		// for(Object key:paramMap.keySet()){
		// map.put((String)key,paramMap.get(key));
		// }
		// throw new BusinessException("注册失败！");

		super.db.insert(paramMap);

		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		// result.put("redirect", "");
		// result.put("appendUrl", "?reg=t");
		// result.put("msg", "注册成功");

		result.put("method.infoMsg", "注册成功！");
		result.put("method.url", in.get("url"));
		return result;
	}

	/**
	 * 密码修改
	 */

	@SuppressWarnings("unchecked")
	public IMap<String, Object> updatePwd(IMap<String, Object> in) {

		IMap<String, Object> result = new DataMap<String, Object>();// 输出map

		IMap paramMap = (IMap) in.get("user");

		IMap<String, Object> map = BeanFactory.getClassBean(paramMap
				.getClassName());

		// 加密
		// String oldpwd = MD5.createPassword((String) paramMap.get("userPwd"));
		// String newpwd = MD5.createPassword((String) in.get("newPwd"));
		String oldpwd = (String) paramMap.get("userPwd");
		String newpwd = (String) in.get("newPwd");

		IMap<String, Object> userMap = (IMap<String, Object>) in.get("userMap");

		if (!userMap.get("userPwd").equals(oldpwd)) {
			throw new BusinessException("旧密码不正确！");
		} else {
			map.put("userPwd", newpwd);
			map.put("userId", userMap.get("userId"));

			super.db.update(map);

			userMap.put("userPwd", newpwd);

		}
		result.put("flag", "1");
		return result;
	}
	/**
	 * 用户管理列表
	 */
	@SuppressWarnings("unchecked")
	public IMap<String, Object> userList(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		Page page = super.db.pageQuery(in, "searchsql", "com.UserInfo", Integer
				.parseInt((String) in.get("currentPageNO")), Integer
				.parseInt((String) in.get("perCount")));

		// List<IMap>
		// list=super.db.getList(in,"searchsql","com.UserInfo",0,Integer.parseInt((String)in.get("perCount")));
		StringBuffer url = new StringBuffer();
		url.append("1=1");
		String userName = (String) in.get("userName");
		String email = (String) in.get("email");
		Date starttime = (Date) in.get("starttime");
		Date endtime = (Date) in.get("endtime");

		if (userName != null && !"".equals(userName)) {
			url.append("&userName=" + userName);
		}
		if (email != null && !"".equals(email)) {
			url.append("&email=" + email);
		}
		if (starttime != null && !"".equals(starttime)) {
			url.append("&starttime=" + starttime);
		}
		if (endtime != null && !"".equals(endtime)) {
			url.append("&endtime=" + endtime);
		}
		page.setAction(url.toString());
		result.put("usersMap", page.getResultList());
		result.put("page", page);
		return result;
	}

	/**
	 * 用户添加 跳转
	 */

	public IMap<String, Object> toAddUser(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		List<IMap> list = super.db.getList(in, "usertypesql",
				"com.UserTypeInfo", 0, 100000000);
		result.put("usertypes", list);
		return result;
	}

	/**
	 * 用户添加
	 */

	public IMap<String, Object> addUser(IMap<String, Object> in) {

		IMap paramMap = (IMap) in.get("user");

		/*
		 * 校验用户名 1.合法 2.未占用
		 */

		// 加密
		paramMap.put("userPwd", MD5.createPassword((String) paramMap
				.get("userPwd")));
		paramMap.put("userId", HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.UserInfo"));
		paramMap.put("userName", Util.removeCharIsNotWord((String) paramMap
				.get("userName")));
		IMap userParam = BeanFactory.getClassBean("com.UserInfo");
		MapUtils.copyMapField(paramMap, "userName", userParam);
		List<IMap> existList = db.getList(userParam, null);
		if (existList != null & existList.size() > 0) {
			throw new BusinessException("用户名不能重复！");
		}
		paramMap.put("userTypeId", (String) paramMap.get("userTypeId"));
		HelperDB.setDate("regDate", paramMap, new Date());
		paramMap.put("loginCount", 0);
		paramMap.put("verifyState", 0);

		super.db.insert(paramMap);

		IMap<String, Object> result = new DataMap<String, Object>();// 输出map

		result.put("method.infoMsg", "添加用户成功！");
		result.put("method.url", in.get("url"));
		return result;
	}

	/**
	 * 用户修改
	 */

	@SuppressWarnings("unchecked")
	public IMap<String, Object> toUpdateUser(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map

		// result.put("usersMap",super.db.get(in,"loginsql","com.UserInfo"));
		IMap selectMap = BeanFactory.getClassBean("com.UserInfo");
		selectMap.put("userId", in.get("userId"));
		result.put("usersMap", super.db.get(selectMap));
		List<IMap> list = super.db.getList(in, "usertypesql",
				"com.UserTypeInfo", 0, 100000000);

		result.put("usertypes", list);
		return result;
	}

	/**
	 * 用户修改保存
	 */

	public IMap<String, Object> updateUser(IMap<String, Object> in) {

		IMap paramMap = (IMap) in.get("user");

		super.db.update(paramMap);

		IMap<String, Object> result = new DataMap<String, Object>();// 输出map

		result.put("method.infoMsg", "修改用户成功！");
		result.put("method.url", in.get("url"));
		return result;
	}

	/**
	 * 用户删除
	 */

	public IMap<String, Object> delUser(IMap<String, Object> in) {

		IMap paramMap = (IMap) in.get("user");

		super.db.delete(paramMap);

		IMap<String, Object> result = new DataMap<String, Object>();// 输出map

		result.put("method.infoMsg", "删除用户成功！");
		result.put("method.url", in.get("url"));
		return result;
	}

	/**
	 * Ajax 密码重置
	 */
	public IMap<String, Object> rePwd(IMap<String, Object> in) {
		IMap<String, Object> result = new DataMap<String, Object>();// 输出map

		String userId = (String) in.get("UserId");

		IMap<String, Object> selectMap = BeanFactory
				.getClassBean("com.UserInfo");

		selectMap.put("UserId", userId);
		selectMap.put("userPwd", MD5.createPassword("123456"));

		try {
			super.db.update(selectMap);
			result.put("str", "1");
			result.put("pwd", "123456");
		} catch (RuntimeException e) {
			e.printStackTrace();
			result.put("str", "0");
		}
		return result;
	}

	/**
	 * 用户类型管理
	 */
	public IMap<String, Object> usertypeList(IMap<String, Object> in) {

		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		Page page = super.db.pageQuery(in, "usertypesql", "com.UserTypeInfo",
				Integer.parseInt((String) in.get("currentPageNO")), Integer
						.parseInt((String) in.get("perCount")));
		result.put("usertypes", page.getResultList());
		result.put("page", page);
		return result;
	}

	/**
	 * 用户类型 跳转
	 */
	public IMap<String, Object> toAddUsertype(IMap<String, Object> in) {

		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		return result;
	}

	/**
	 * 用户类型 添加
	 */
	public IMap<String, Object> addUsertype(IMap<String, Object> in) {

		IMap<String, Object> result = new DataMap<String, Object>();// 输出map

		IMap<String, Object> selectMap = BeanFactory
				.getClassBean("com.UserTypeInfo");
		selectMap.put("userTypeId", HelperApp.getAutoIncrementPKID(HelperApp
				.getPackageName(), "com.UserTypeInfo"));
		selectMap.put("userTypeName", in.get("userTypeName"));
		selectMap.put("userTypeRemark", in.get("userTypeRemark"));
		selectMap.put("userTypeCode", in.get("userTypeCode"));
		selectMap.put("createUser", ((IMap<String, Object>) in.get("userMap"))
				.get("userId"));
		HelperDB.setDateTime("createDate", selectMap, new Date());

		super.db.insert(selectMap);

		result.put("method.infoMsg", "添加用户分类成功！");
		result.put("method.url", in.get("url"));
		return result;
	}

	/**
	 * 用户类型修改查
	 */
	public IMap<String, Object> toUpdateUsertype(IMap<String, Object> in) {

		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		result.put("usertype", super.db.get(in, "usertypesql",
				"com.UserTypeInfo"));
		return result;
	}

	/**
	 * 用户类型 修改
	 */
	public IMap<String, Object> updateUsertype(IMap<String, Object> in) {

		IMap<String, Object> result = new DataMap<String, Object>();// 输出map

		IMap<String, Object> selectMap = BeanFactory
				.getClassBean("com.UserTypeInfo");
		selectMap.put("userTypeId", in.get("userTypeId"));
		selectMap.put("userTypeName", in.get("userTypeName"));
		selectMap.put("userTypeCode", in.get("userTypeCode"));
		selectMap.put("userTypeRemark", in.get("userTypeRemark"));
		super.db.update(selectMap);

		result.put("method.infoMsg", "修改用户分类成功！");
		result.put("method.url", in.get("url"));
		return result;
	}

	/**
	 * 用户类型 修改
	 */
	public IMap<String, Object> delUsertype(IMap<String, Object> in) {

		IMap<String, Object> result = new DataMap<String, Object>();// 输出map

		IMap<String, Object> selectMap = BeanFactory
				.getClassBean("com.UserTypeInfo");
		selectMap.put("userTypeId", in.get("userTypeId"));
		super.db.delete(selectMap);

		result.put("method.infoMsg", "删除用户分类成功！");
		result.put("method.url", in.get("url"));
		return result;
	}

	/**
	 * 修改密码
	 */
	public IMap<String, Object> toupdatePwd(IMap<String, Object> in) {
		String flag = (String) in.get("flag");

		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		if (!com.brick.util.Util.checkNull(flag)) {
			result.put("flag", flag);
		} else {
			result.put("flag", "-1");
		}
		return result;
	}

	public IMap<String, Object> test(IMap<String, Object> in) throws Exception {

		IMap<String, Object> result = new DataMap<String, Object>();// 输出map
		Object obj = ContextUtil.getSpringBean("&sqlMapClient");
		SqlMapClientFactoryBean sqlMapClientFactoryBean = (SqlMapClientFactoryBean) obj;
		sqlMapClientFactoryBean.resetSqlMapClient();

		com.brick.dao.DAO adb = (DAO) ContextUtil.getSpringBean(HelperApp
				.getDaoName());
		adb.setSqlMapClient((SqlMapClient) sqlMapClientFactoryBean.getObject());
		adb.getConnection();
		adb.get(in, "test1", "com.UserInfo");
		// db.get(in, "test","com.UserInfo");
		return result;
	}
}
