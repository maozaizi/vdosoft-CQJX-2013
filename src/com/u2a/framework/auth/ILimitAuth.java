package com.u2a.framework.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * limit验证接口
 * @author 王冲
 * @date 2010-9-8 10:12:05
 */
public interface ILimitAuth {
	/**
	 * 验证limit权限
	 * @param request
	 * @param response
	 * @return
	 */
	public Boolean check(HttpServletRequest request,HttpServletResponse response,String methodName);
}
