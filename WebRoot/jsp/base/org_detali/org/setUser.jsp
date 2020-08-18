<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
	<link href="${pageContext.request.contextPath }/css/bootstrap.min.css" rel="stylesheet" media="screen" />
	<script src="${pageContext.request.contextPath }/js/jquery.1.7.2.min.js"></script>
	<script src="${pageContext.request.contextPath }/js/bootstrap.min.js"></script>
<script type="text/javascript">
	function checkForm() {
		var userName = document.getElementById("userName").value;
		if (userName == "") {
			alert("请输入登录名！");
			return;
		}
		var userPwd = document.getElementById("userPwd").value;
		if (userPwd == "") {
			alert("请输入登陆密码！");
			return;
		}
		var re_user_pwd = document.getElementById("re_user_pwd").value;
		if (re_user_pwd == "") {
			alert("请确认您的密码！");
			return;
		}
		if(re_user_pwd != userPwd){
			alert("两次输入的密码不相同，请检查！");
			return;
		}
		document.forms[0].action = "${pageContext.request.contextPath }/api/org/setUserLogin";
		document.forms[0].submit();
	}
	function goBack() {
		  window.location.href = "${pageContext.request.contextPath}/api/org/getInfo?orgCode=${userInfo.orgId}";
	}
</script>
</head>
<body>
	<div class="MainContent">
		<div class="ListBlock">
		    <h3 class="cBlack">设置用户信息</h3>

			<!-- 增加组织 -->
			<form id="addForm" method="post" action="" >
			   <input type="hidden" name="tempUrl" id="tempUrl" value="${pageContext.request.contextPath}/api/org/getInfo?orgCode=${userInfo.orgId}"/>
			   <input type="hidden" name="userId" id="userId" value="${userInfo.userId}" />
			   
			   <div class="form-horizontal">
	        	<div class="control-group">
					<label class="control-label" for="personName">登录名</label>
					<div class="controls">
						<input type="text" value = "${userInfo.userName}" name="userName" class="span2" id="userName" placeholder="请输入登录名"/>
					</div>	
				</div>
	        	<div class="control-group">
					<label class="control-label" for="personName">密码</label>
					<div class="controls">
						<input type="password" value="" name="userPwd" class="span2" id="userPwd" placeholder="请输入密码"/>
					</div>	
				</div>
	        	<div class="control-group">
					<label class="control-label" for="personName">请确认密码</label>
					<div class="controls">
						<input type="password" name="re_user_pwd" class="span2" id="re_user_pwd" placeholder="请确认密码"/>
					</div>	
				</div>
				<div class="form-actions">
					<a class="btn btn-primary" href="#" name="submitter" onclick="checkForm();">保存</a>
				  	<a class="btn" href="#"  onclick="goBack()">返回</a>
				</div>
			   </div>
			</form>
	</div>
	</div>
</body>
</html>

