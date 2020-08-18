<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>修改权限组</title>
	<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" media="screen"/>
	<script src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
	<script type="text/javascript">
		function checkForm() {
			var authGroupName = document.getElementById("authGroupName").value;
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
			
			if (authGroupName == "") {
				alert("请输入权限组名称！");
				return ;
			}
			
			if (confirm("请确定修改权限组信息吗？")) {
				document.forms[0].action = "${pageContext.request.contextPath }/api/authgroup/modify";
				document.forms[0].submit();
			}
		}
		
		function goBack() {
			window.location.href = "${param.url}";
		}
	</script>
</head>
<body>
<div class="row">
		<div class="span2"></div>
		<div class="span10">
		<h3 class="cBlack">权限组管理>>修改权限组</h3>
			<form method="post" action="" >
			<input type="hidden" name="url" id="url" value="${param.url}"/>
			<input type="hidden" name="authGroupId" id="authGroupId" value="${authGroup.authGroupId}"/>
			<div class="form-horizontal">
				<div class="control-group">
					<h3>添加权限组</h3>
				</div>
				<div class="control-group">
					<label class="control-label" for="personName"><span style="color: Red">*</span>权限组名称</label>
					<div class="controls">
						<input type="text" class="span2" name="authGroupName" value="${authGroup.authGroupName }" id="authGroupName"  size="30" check="1" placeholder="请填写权限组名称"><span class="help-inline">请填写权限组名称</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="remark">描述</label>
					<div class="controls">
						<textarea name="authGroupRemark" id="authGroupRemark" rows="3">${authGroup.authGroupRemark }</textarea><span class="help-block">请输入描述</span>
					</div>
				</div>
				<div class="form-actions">
					<a class="btn btn-primary" href="#" onclick="checkForm();">保存</a>
					<a class="btn" href="#" onclick="goBack();">返回</a>
				</div>
				
				</div>  
			  </form>
		</div>
	</div>
<!--鼠标划过tr背景色改变-->
</body>
</html>

