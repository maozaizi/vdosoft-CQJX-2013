<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.brick.data.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.io.*"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>长庆钻井机修公司-设备维修管理系统</title>
<link href="${pageContext.request.contextPath }/css/global.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
</head>
<%
	IMap<String,Object> userMap=(IMap<String,Object>)request.getSession().getAttribute("userMap");
	if(userMap==null){
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().println("<script>alert('尚未登录或登陆超时，请重新登陆后使用！');location.href='"+request.getContextPath()+"/api/login/tologin';</script>");
	}
%>

<frameset rows="90,*" frameborder="no" border="0" framespacing="0">
  <frame src="${pageContext.request.contextPath}/jsp/top.jsp" name="topFrame" scrolling="no" noresize="noresize" id="topFrame" title="topFrame" />
  <frameset id="switch" framespacing="0" border="0" frameborder="no" cols="199,10,*" rows="*">
     <frame scrolling="auto" noresize="" frameborder="no" name="xfjLeftFrame" id="xfjLeftFrame" src="${pageContext.request.contextPath}/jsp/left.jsp"></frame>
     <frame id="leftbar" scrolling="no" noresize="" name="switchFrame" src="${pageContext.request.contextPath}/jsp/swich.jsp"></frame>
	 <frame src="${pageContext.request.contextPath}/api/mywork/getMyWork" name='rightFrame' id="rightFrame" frameborder="no" />
  </frameset>
</frameset>

</html>

