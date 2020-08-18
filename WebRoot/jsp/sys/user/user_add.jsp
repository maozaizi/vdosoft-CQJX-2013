<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
 		<title>用户添加</title>
 		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 		<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/RegExp.js"></script>
		<script type="text/javascript">
		$(document).ready(function(){
		 $("#userForm").bind("submit",function(){
		   if(!usernamevali($(this['userName']).val(),4,16)){
		   $(this['userName']).focus();
		   return false;
		   }
		   if($(this['userPwd']).val().length<4||$(this['userPwd']).val().length>16){
		   alert("密码长度不合法！4-16位之间！");
		   $(this['userPwd']).focus();
		   return false;
		   }
		  
		   if(!email($(this['email']).val())){
		   alert("Email输入不合法！");
		   $(this['email']).focus();
		   return false;
		   }
		 });
		
		});
		
		function sub(){
		  $("#userForm").submit();
		}
		</script>
	</head>
<body>
   	<div class="MainContent">
		<div class="ListBlock">
		<h3 class="cBlack">添加用户</h3>
		<form id="userForm" method="post" action="${pageContext.request.contextPath}/api/login/addUser" >
		<input  type="hidden"  name="url" value="${pageContext.request.contextPath}/api/login/userList"/>
		<div class="EditBox">
			<table width="100%"  border="0" cellpadding="0" cellspacing="0">
					  <tr>
					  	<th class="EditBox_td1" colspan="2">添加用户</th>
					  </tr>
					  <tr>
					  	<td class="EditBox_td1"><span style="color: Red">*&nbsp;</span>用户名：</td>
						<td>
						  	<input  type="text"  name="userName" value="${param.userName}" class="ipt_text" size="30"/>
						</td>
					  </tr>
					 <tr>
						<td class="EditBox_td1"><span style="color: Red">*&nbsp;</span>密码：</td>
						<td>
						  	<input  type="password"   name="userPwd" class="ipt_text" size="30"/>
						</td>
					  </tr>
					   <tr>
						<td class="EditBox_td1">Email：</td>
						<td>
						  	<input  type="text"   name="email" value="${param.email}" class="ipt_text" size="30"/>
						</td>
					  </tr>
					   <tr>
						<td class="EditBox_td1">用户类型：</td>
						<td>
						  	<select name="userTypeId">
							  	<c:forEach var="type" items="${usertypes}">
					      			<option value="${type.userTypeId}" ${param.userTypeId==type.userTypeId?"selected='selected'":""}>${type.userTypeName}</option>
					      		</c:forEach>
						  	</select>
						</td>
					  </tr>
					</table> 
				</div>
				<div class="Temp-2">
					<a class="Bbtn" href="#" onclick="sub();"><span class="Br">保存</span></a>
					<a class="Bbtn" href="#" onclick="location.href='${pageContext.request.contextPath}/api/login/userList'"><span class="Br">返回</span></a>
				</div>  
		</form>
		</div>
	</div>
  </body>
</html>
