<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
	<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>修改模块</title>
	
	<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" media="screen">
	<script src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	
	
	
	<script type="text/javascript">
		function checkForm() {
			var authName = document.getElementById("authName").value;
		/**
			var coluCode = document.getElementById("coluCode").value;
			var coluOrder = document.getElementById("coluOrder").value;
			
			if (trim(coluName) == "") {
				alert("请输入栏目名称！");
				return ;
			}
			
			if (trim(coluCode)) {
				alert("请输入栏目编码！");
				return ;
			}
			
			if (coluOrder == "") {
				document.getElementById("coluOrder").value = 1;
			}
		**/
			
			if (authName == "") {
				alert("请输入权限名称！");
				return ;
			}
			
			if (confirm("请确定保存权限信息吗？")) {
				document.forms[0].action = "${pageContext.request.contextPath }/api/auth/modify";
				document.forms[0].submit();
			}
		}
		
		function goBack() {
			window.location.href = "${param.actionUrl}?expandId=${auth.authId}";
		}
		
		function init() {
			var selected = "${auth.authType}";
			var authType = document.getElementById("authType");
			for (var i = 0; i < authType.length; i++) {
				if (authType.options[i].value == selected) {
					authType.options[i].selected = true;
				}
			}
		}
	</script>
	
	</head>
<body onload="init()">
	<div class="row">
	  <div class="span0"></div>
	  <div class="span12">
	     <form id="addForm" method="post" action="" >
			<input type="hidden" name="actionUrl" id="actionUrl" value="${param.actionUrl}"/>
			<input type="hidden" name="authId" id="authId" value="${auth.authId }"/>
			<input type="hidden" name="authGrade" id="authGrade" value="${auth.authGrade }"/>
	     
		 <div class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="authName">权限名称</label>
				<div class="controls">
					<input type="text" class="span2" id="authName" name="authName" value="${auth.authName}" placeholder="请输入权限名称"><span class="help-inline">请输入权限名称</span>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="authRemark">权限描述</label>
				<div class="controls">
					<textarea rows="3" name="authRemark" id="authRemark">${auth.authRemark }</textarea><span class="help-block">请输入权限描述</span>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label">分类</label>
				<div class="controls">
					<select name="authType" id="authType" class="span2" disabled="true">
						<option value="0">模块</option>
				  		<option value="1">页面</option>
				  		<option value="2">控件</option>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="url">URL</label>
				<div class="controls">
					<input type="text" class="span4" name="url" value="${auth.url }" id="url" placeholder="请输入URL"><span class="help-inline">请输入URL</span>
				</div>
			</div>
	
			<div class="control-group">
				<label class="control-label" for="authCode">权限code</label>
				<div class="controls">
					<input type="text" class="span2" name="authCode" value="${auth.authCode }" id="authCode" placeholder="请输入权限code"><span class="help-inline">请输入权限code</span>
				</div>
			</div>
	
			<div class="control-group">
				<label class="control-label" for="authOrder">排序</label>
				<div class="controls">
					<input name="authOrder" id="authOrder" value="${auth.authOrder }" type="text" class="span2" placeholder="请输入排序" onKeyUp="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
					<span class="help-inline">请输入排序</span>
				</div>
			</div>
		
	
	
			<div class="form-actions">
				<a href="#" class="btn btn-primary" onclick="checkForm();">保存</a>
				<a href="#" class="btn" onclick="goBack();">取消</a>
			</div>
		</div>
		</form>
	
	  </div>		
	</div>
</body>
</html>

