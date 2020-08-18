<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>机构选择</title>
	<script type="text/javascript">
		function optSelect(){
			window.open("${pageContext.request.contextPath}/api/organizationdetali/getSelectOrgTree?tmp=" + Math.round(Math.random() * 10000)+"&parentId=${sessionScope.orangaizationNameId }","newwindow", "height=500, width=400, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
		}
	</script>
  </head>
  <body>
		<div>
			<input id="orangaizationName" name="orangaizationName"  type="text" value="${sessionScope.orangaizationName }"/>
			<input id="orangaizationNameId" name="orangaizationNameId"  type="hidden" value="${sessionScope.orangaizationNameId }"/>
			<input value="选择" id="optselect" onclick="optSelect()" type="button"/>
		</div>
  </body>
</html>
