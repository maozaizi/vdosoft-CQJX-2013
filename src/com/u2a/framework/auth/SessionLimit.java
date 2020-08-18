package com.u2a.framework.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.brick.data.IMap;
/**
 * session验证
 * @author 王冲
 * @date 2010-9-9 18:03:39
 */
public class SessionLimit implements ILimitAuth{
	
	public Boolean check(HttpServletRequest request,
			HttpServletResponse response, String methodName) {
		IMap userMap = (IMap) request.getSession().getAttribute("userMap");
		if(userMap!=null) return true;
		return false;
	}

}
