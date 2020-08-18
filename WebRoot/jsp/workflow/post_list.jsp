<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>岗位信息</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="keywords" content="列表">
		<meta http-equiv="description" content="列表">
		<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript">
	 
		function addpost(){
		document.form.action="${pageContext.request.contextPath}/api/workflow/addPost";
		document.form.submit();
		}
		
		function goRet(){
		//document.forms[0].action="${pageContext.request.contextPath}/api/workflow/getauthorizedStep";
		//document.forms[0].target="rightProcess";
		//document.forms[0].submit();
        window.close(); 
		}
		</script>
	</head>	
<body >
<div class="MainContent">
  <div class="ListBlock">
    <h3 class="cBlack">选择岗位</h3>
	<form id="form" name="form" method="post" action="">
	<!-- form 提交表单隐藏域开始-->
	<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/workflow/getPost"/>
	<input type="hidden" name="processStepId" id="processStepId" value="${param.processStepId }"/>
	<input type="hidden" name="authorizedType" id="authorizedType" value="${param.authorizedType }"/>
	<input type="hidden" name="processId" id="processId" value="${processId }"/>
	
	<!-- form 提交表单隐藏域结束-->
	<div class="List">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">

		<thead>
			<tr>
				<th width="10">选择</th>
	        	<th>岗位名称</th>
	        </tr>
        </thead>
        <tbody>
        <c:forEach items="${postList}" var="post" varStatus="index">
	        <tr>
	        	<td><input type="checkbox" name="autId" id="autId" value="${post.AUTHGROUPID}/${post.AUTHGROUPNAME}" title="${post.AUTHGROUPID}"/></td>
		      	<td>${post.AUTHGROUPNAME}</td>
		    </tr>
		</c:forEach>
		</tbody>
	</table>
	</div>
    
    

	<!-- 操作按钮开始 -->
	 <div class="Temp-3">
			<a class="Bbtn" href="#" id="add" name="add" onclick="addpost()"><span class="Br">添加</span></a>
			<a class="Bbtn" href="#" id="goRet" name="goRet" onclick="goRet()"><span class="Br">返回</span></a>
	</div>
     
	</form>
	</div>	
	</div>
</body>
</html>


