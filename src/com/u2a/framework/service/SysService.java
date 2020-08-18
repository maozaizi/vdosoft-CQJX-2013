package com.u2a.framework.service;

import com.brick.api.Service;
import com.brick.data.DataMap;
import com.brick.data.IMap;
/**
 * 系统服务类，提供一些公共服务
 * @author 吴明强
 *@date 2010-09-08
 */
public class SysService extends Service {
	/**
	 * 页面空跳转
	 * @param in
	 * @return
	 */
	public IMap toPage(IMap in) {
		IMap result=new DataMap();
		String inData = (String)in.get("inData");
		return result;
	}
}
