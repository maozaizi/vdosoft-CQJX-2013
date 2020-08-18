package com.u2a.framework.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.brick.data.IMap;

/**
 * 类 型:  过滤器
 * 描 述： 企业会员后台，登陆状态过滤
 * 日 期:  2009-05-21
 */
public class UserFiter implements Filter {

	
    public void init(FilterConfig arg0) throws ServletException {
	}
	
	public void destroy() {
	}

	public void doFilter(ServletRequest request0, ServletResponse response0,
			FilterChain chan) throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest)request0;
		HttpServletResponse response=(HttpServletResponse)response0;
		
	    String uri=request.getRequestURI();
	    
	    String regex ="/*/api/*";
	    Pattern p = Pattern.compile(regex); 
	    Matcher matcher = p.matcher(uri);
	    boolean result = matcher.find();    
	    if(result){
	    	if(!uri.endsWith("/api/login/login") && !uri.endsWith("/api/login/tologin")){
	    		IMap userMap=(IMap)request.getSession().getAttribute("userMap");
	    		if(userMap==null){
					response.setContentType("text/html; charset=UTF-8");
					response.getWriter().println("<script>alert('尚未登录或登陆超时，请重新登陆后使用！');window.top.location.href='"+request.getContextPath()+"';</script>");
					return;
				} 
	    	}
	    }

	     
//	    if(!uri.endsWith("/api/login/tologin")&&!uri.endsWith("/api/login/destory")&&!uri.endsWith("/api/login/login")&&!uri.endsWith("/api/login/reg")){
//		IMap<String,Object> userMap=(IMap<String,Object>)request.getSession().getAttribute("userMap");
				
//				if(userMap==null){
//					response.setContentType("text/html; charset=UTF-8");
//					response.getWriter().println("<script>alert('尚未登录或登陆超时，请重新登陆后使用！');location.href='"+request.getContextPath()+"/api/login/tologin';</script>");
//					return; 
//				} 
//	    }
		
	    chan.doFilter(request0, response0);

	}

	

}
