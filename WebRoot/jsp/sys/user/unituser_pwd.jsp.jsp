<%@ page language="java" pageEncoding="utf-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>用户密码修改</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/RegExp.js"></script>
		<script type="text/javascript">
		$(document).ready(function(){
		
		$("#pwdForm").bind("submit",function(){
		   if(ifnull($(this['userPwd']).val())){
		   alert("请输入密码！");
		   $(this['userPwd']).focus();
		   return false;
		   }
		  if(ifnull($(this['newPwd']).val())){
		   alert("请输入新密码！");
		   $(this['newPwd']).focus();
		   return false;
		   }
		   if($(this['newPwd']).val().length<4||$(this['newPwd']).val().length>16){
		   alert("新密码长度不合法！4-16位之间！");
		   $(this['newPwd']).focus();
		   return false;
		   }
		   if($(this['newPwd']).val()!=$(this['renewPwd']).val()){
		   alert("两次输入的密码不一至！");
		   $(this['renewPwd']).focus();
		   return false;
		   }
		 });
		
		});
		
		function sub(){
		$("#pwdForm").submit();
		}
		</script>
	</head>

   <body>
   <div class="MainContent">
		<div class="ListBlock">
		<h3 class="cBlack">修改用户密码</h3>
		<form id="pwdForm" method="post" action="${pageContext.request.contextPath}/api/login/updatePwd">
			<input type="hidden" name="url" value="${pageContext.request.contextPath}/api/mywork/getMyWorkUserList"/>
			<div class="EditBox">
				<table width="45%"  border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="EditBox_td1">原始密码：</td>
						<td>
						  	<input  type="password"  class="Input_20" name="userPwd"/>
						</td>
					</tr>
					<tr>
							<td class="EditBox_td1">新密码：</td>
							<td>
							  	<input  type="password"  class="Input_20" name="newPwd"/>
							</td>
						  </tr>
						  <tr>
							<td class="EditBox_td1">重复密码：</td>
							<td>
							  	<input  type="password"  class="Input_20" name="renewPwd"/>
							</td>
						  </tr>
						</table> 
					</div>  
					<div class="Temp-2">
						<a class="Bbtn" href="#" onclick="sub();"><span class="Br">保存</span></a>
						<a class="Bbtn" href="#" onclick="location.href='${pageContext.request.contextPath}/api/mywork/getMyWorkUserList'"><span class="Br">返回</span></a>
					</div>  
			</form>
		</div>
</div>

<script type="text/javascript">
<c:if test="${not empty errMsg}">
alert("${errMsg}");
</c:if>
</script>

  </body>
</html>
