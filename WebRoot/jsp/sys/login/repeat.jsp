<%@ page language="java" pageEncoding="utf-8"%>
<%@include file="/jsp/public/notlimit_top.jsp" %>
<head>
 <title>用户重复登录</title>
<link href="${pageContext.request.contextPath }/css/right.css" rel="stylesheet" type="text/css" />
</head>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
 
  <body>
   您的用户已经在线: <br>
   用户名：admin<br>
   IP：192.168.51.254<br>
   登录时间：2010-12-12 23:11:24<br>
   <input value="强制下线" type="button" onclick="location.href='${pageContext.request.contextPath}/index.jsp'">&nbsp;
   <input value="退出" type="button" onclick="location.href='${pageContext.request.contextPath}/index.jsp'"><br>
  </body>
</html>
