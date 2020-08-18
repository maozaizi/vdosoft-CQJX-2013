<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
  <head>
    <base href="<%=basePath%>">
  </head>
	<frameset rows="*" cols="400,800" framespacing="0" frameborder="no" id="fream">
	 <frame src="${pageContext.request.contextPath}/api/dictionary/getDataItemBaseInfoList"  name="dictionaryLeftFrame" id="dictionaryLeftFrame"  style="overflow:auto;"/> 
	 <frame src="${pageContext.request.contextPath}/api/dictionary/get?isShua=0" name='dictionaryRightFrame'  id="dictionaryRightFrame"  frameborder="no" />
	</frameset>
</html>
