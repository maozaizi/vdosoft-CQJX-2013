<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp" %>
<html>
  <head>
    <title>right</title>
    <link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
	<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
    <script type="text/javascript">
		function goBack() {
			window.location.href = "${pageContext.request.contextPath}/api/workflow/getProcessInfo?processId=${param.processId}";
		 }
		
    </script>
  </head>
  
  <body>
	<div class="MainContent">
		<div class="ListBlock">
		<div class="List">
		<form id="addForm" method="post" action="" >
		<img src="${pageContext.request.contextPath}/${param.processPicturesUrl }" border=0 />
			<div style="margin-left:300px;margin-top:20px;">
			 <a class="Bbtn" href="#" onclick="goBack()"><span class="Br">返回</span></a>
			</div>
		</form>
  		</div>
    </div>
</div>
  	  
  </body>
</html>
