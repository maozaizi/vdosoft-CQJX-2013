<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
	String flag = (String) request.getAttribute("method.flag");
	if(!"1".equals(flag)){
		//out.println("<script>alert('没有权限访问该页面！')</script>");
		//return ;
	}
%>