<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
	<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>添加模块</title>
	
	<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" media="screen">
	<script src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	
	
	
	<script type="text/javascript">
		function checkForm() {
			var authName = document.getElementById("authName").value;
			
			if (authName == "") {
				alert("请输入模块名称！");
				return ;
			}
			
			if (confirm("请确定保存模块信息吗？")) {
				document.forms[0].action = "${pageContext.request.contextPath }/api/auth/add";
				document.forms[0].submit();
			}
		}
		
		function doChange() {
			var authType = document.getElementById("authType");
			var typeValue;
			for (var i = 0; i < authType.length; i++) {
				if (authType.options[i].selected == true) {
					typeValue = authType.options[i].value;
				}
			}
			if (typeValue == "0") {
				document.getElementById("url").readOnly = true;
			} else {
				document.getElementById("url").readOnly = false;
			}
		}
		
		function init() {
			var authType = document.getElementById("authType");
			var typeValue;
			for (var i = 0; i < authType.length; i++) {
				if (authType.options[i].selected == true) {
					typeValue = authType.options[i].value;
				}
			}
			if (typeValue == "0") {
				document.getElementById("url").readOnly = true;
			}
		}
		
		function goBack() {
			if ("${authGrade}" == 1) {
				window.location.href = "${param.actionUrl}";
			} else {
				window.location.href = "${param.actionUrl}?expandId=${parentAuthId}";
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
			<input type="hidden" name="parentAuthId" id="parentAuthId" value="${parentAuthId}"/>
			<input type="hidden" name="authGrade" id="authGrade" value="${authGrade}"/>
			<input type="hidden" name="rootAuthId" id="rootAuthId" value="${rootAuthId}"/>
	     
		 <div class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="authName">权限名称</label>
				<div class="controls">
					<input type="text" class="span2" id="authName" name="authName" placeholder="请输入权限名称"><span class="help-inline">请输入权限名称</span>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="authRemark">权限描述</label>
				<div class="controls">
					<textarea rows="3" name="authRemark" id="authRemark"></textarea><span class="help-block">请输入权限描述</span>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label">分类</label>
				<div class="controls">
					<select name="authType" id="authType" onchange="doChange()" class="span2">
						<option value="0">模块</option>
				  		<option value="1">页面</option>
				  		<option value="2">控件</option>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="url">URL</label>
				<div class="controls">
					<input type="text" class="span4" name="url" id="url" placeholder="请输入URL"><span class="help-inline">请输入URL</span>
				</div>
			</div>
	
			<div class="control-group">
				<label class="control-label" for="authCode">权限code</label>
				<div class="controls">
					<input type="text" class="span2" name="authCode" id="authCode" placeholder="请输入权限code"><span class="help-inline">请输入权限code</span>
				</div>
			</div>
	
			<div class="control-group">
				<label class="control-label" for="authOrder">排序</label>
				<div class="controls">
					<input name="authOrder" id="authOrder" value="" type="text" class="span2" placeholder="请输入排序" onKeyUp="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
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

