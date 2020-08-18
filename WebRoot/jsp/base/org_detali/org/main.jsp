<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
  <head>
    <base href="<%=basePath%>">
  </head>
<frameset rows="*" cols="260,800" framespacing="0" frameborder="no" id="fream">
 <frame src="${pageContext.request.contextPath}/api/org/getTree"  name="app_leftFrame" id="app_leftFrame" style="overflow:auto;"> 
 <frame src="${pageContext.request.contextPath}/api/org/getInfo" name='app_rightFrame'  id="app_rightFrame"  frameborder="no"/>
</frameset>
</html>
