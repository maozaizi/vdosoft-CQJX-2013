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
	 <frame src="${pageContext.request.contextPath}/api/workflow/getProcessManageList"  name="leftFrameProcess" id="leftFrameProcess"  style="overflow:auto;"> 
	 <frame src="${pageContext.request.contextPath}/api/workflow/getProcessInfo?parentId=-1" name='rightProcess'  id="rightProcess"  frameborder="no"/>
	</frameset>
</html>
