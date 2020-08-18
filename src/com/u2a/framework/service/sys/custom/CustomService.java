package com.u2a.framework.service.sys.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.u2a.framework.util.Util;
import com.brick.api.Service;
import com.brick.data.DataMap;
import com.brick.data.IMap;
import com.brick.manager.BeanFactory;
import com.u2a.framework.util.FireNetHelper;
import com.u2a.framework.util.HelperApp;
import com.u2a.framework.util.HelperDB;

@SuppressWarnings("unchecked")
public class CustomService extends Service {
	static final String TABLE_NAME = "com.custom";

	/**
	 * 
	 * @Description 获取当前所有的菜单项，对用户所定制的自己的快捷方式打钩选中
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author duch
	 * @date May 28, 2012 5:53:04 PM
	 */
	public IMap getList(IMap in) {
		// 获取当前用户的所有快捷方式
		IMap userMap = (IMap) in.get("userMap");
		FireNetHelper.checkUserMapExists4throw(userMap);
		// 获取所有的菜单
		List<IMap> allList = db.getList(userMap, "getCustomAuth", "");
		// String userId = (String)in.get("userid");
		List<IMap> userList = db.getList(userMap, "getUserCustomAuth", "");
		List<Map> resultList = new ArrayList<Map>();
		// 比较用户已经选中的和所有的 对于已经选中的 页面上checkbox置为选中
		for (int i = 0; i < allList.size(); i++) {
			IMap allInfo = new DataMap();
			allInfo = allList.get(i);
			String authId = (String) allInfo.get("auth_id");
			for (int j = 0; j < userList.size(); j++) {
				IMap userInfo = new DataMap();
				userInfo = userList.get(j);
				String userAuthId = (String) userInfo.get("auth_id");
				if (authId.equals(userAuthId)) {
					allInfo.put("ischeck", "1");
				}
			}
			resultList.add(allInfo);
		}
		// 标识是否关闭弹出窗口
		String flag = (String) in.get("flag");
		IMap<String, Object> result = new DataMap<String, Object>();
		// 判断flag的值，确定正确提交以后关闭弹出窗口 当flag为1时关闭窗口
		if (!com.brick.util.Util.checkNull(flag)) {
			result.put("flag", flag);
		} else {
			// 若为第一次进入 则置为-1
			result.put("flag", "-1");
		}
		result.put("customList", resultList);
		return result;
	}

	/**
	 * 
	 * @Description 保存用户自定义快捷方式
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author duch
	 * @date May 28, 2012 8:29:06 PM
	 */
	public IMap saveCustom(IMap in) {
		// 获取当前用户的所有快捷方式
		IMap userMap = (IMap) in.get("userMap");
		FireNetHelper.checkUserMapExists4throw(userMap);
		String userId = (String) userMap.get("userid");
		String[] authids = (String[]) in.get("authid");
		IMap<String, Object> result = new DataMap<String, Object>();
		if (com.brick.util.Util.checkNotNull(authids)) {
			// 判断数据库有 但页面没选中的数据 删除
			IMap userAuthmap = BeanFactory.getClassBean(TABLE_NAME);
			userAuthmap.put("userId", userId);
			List<IMap> userAuthmaps = db.getList(userAuthmap, null);
			for (int j = 0; j < userAuthmaps.size(); j++) {
				IMap tempmap = userAuthmaps.get(j);
				String userauthid = (String) tempmap.get("authId");
				if (!Util.stringIsHave(authids, userauthid)) {
					db.delete(tempmap);
				}
			}
			for (int i = 0; i < authids.length; i++) {
				String authid = authids[i];
				IMap map = BeanFactory.getClassBean(TABLE_NAME);
				map.put("authId", authid);
				map.put("userId", userId);
				IMap custommap = db.get(map);
				// 判断是否已经存在自定义栏目 存在则更新 否则新增
				if (custommap != null) {
					HelperDB.setModifyInfo(HelperApp.getUserName(userMap),
							custommap);
					db.update(custommap);
				} else {
					String id = HelperApp.getAutoIncrementPKID(HelperApp
							.getPackageName(), TABLE_NAME);
					map.put("id", id);
					HelperDB.setCreateInfo(HelperApp.getUserName(userMap), map);
					db.insert(map);
				}
			}
		} else {
			IMap deleteMap = BeanFactory.getClassBean(TABLE_NAME);
			deleteMap.put("userId", userId);
			db.delete(deleteMap);
		}
		result.put("flag", "1");
		return result;
	}

	/**
	 * 
	 * @Description 获取用户的所有快捷方式
	 * @param
	 * @param in
	 * @param
	 * @return
	 * @return IMap
	 * @author duch
	 * @date May 29, 2012 10:24:46 AM
	 */
	public IMap getMyCustom(IMap in) {
		// 获取当前用户的所有快捷方式
		String userId = (String) in.get("userId");
		IMap map = new DataMap();
		map.put("userid", userId);
		List<IMap> userList = db.getList(map, "getUserCustomAuth", "");
		IMap<String, Object> result = new DataMap<String, Object>();
		result.put("list", userList);
		return result;
	}

}
