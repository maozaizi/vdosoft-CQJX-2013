<%@ page language="java" pageEncoding="utf-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/notlimit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<head>
 <title>用户注册</title>
<link href="${pageContext.request.contextPath}/css/right.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/RegExp.js"></script>
<script type="text/javascript">
$(document).ready(function(){
 $("#regForm").bind("submit",function(){
   if(!usernamevali($(this['userName']).val(),4,16)){
   $(this['userName']).focus();
   return false;
   }
   if($(this['userPwd']).val().length<4||$(this['userPwd']).val().length>16){
   alert("密码长度不合法！4-16位之间！");
   $(this['userPwd']).focus();
   return false;
   }
   if($(this['reuserPwd']).val()!=$(this['userPwd']).val()){
   alert("两次输入的密码不一至！");
   $(this['reuserPwd']).focus();
   return false;
   }
   if(!email($(this['email']).val())){
   alert("Email输入不合法！");
   $(this['email']).focus();
   return false;
   }
 });

});
</script>
</head>
<html>
   <body>
   注册
   <div class="right_box">
	<!-- 添加栏目 -->
	<form id="regForm" method="post" action="${pageContext.request.contextPath}/api/login/reg" >
	
		<table border="0" cellspacing="0" cellpadding="0">
		<input  type="hidden"  name="url" value="${pageContext.request.contextPath}/api/login/tologin"/>
				  <tr>
					<td width="200" class="list_name">用户名</td>
					<td class="endright right_text" colspan="3">
					  	<input  type="text"  class="shuru" name="userName" maxlength="16" style="ime-mode:disabled;" value="${param.userName}"/>
					</td>
				  </tr>
				 
				 <tr>
					<td width="200" class="list_name">密码</td>
					<td class="endright right_text" colspan="3">
					  	<input  type="password"  class="shuru" name="userPwd"  maxlength="16" style="ime-mode:disabled;"/>
					</td>
				  </tr>
				  
				  <tr>
					<td width="200" class="list_name">重复密码</td>
					<td class="endright right_text" colspan="3">
					  	<input  type="password"  class="shuru" name="reuserPwd"  maxlength="16" style="ime-mode:disabled;"/>
					</td>
				  </tr>
				  
				   <tr>
					<td width="200" class="list_name">Email</td>
					<td class="endright right_text" colspan="3">
					  	<input  type="text"  class="shuru" name="email"  maxlength="50" style="ime-mode:disabled;"  value="${param.email}"/>
					</td>
				  </tr>
				  <tr>
					<td width="200" class="list_name">&nbsp;</td>
					<td  class="endright right_text" colspan="4">
					    <input type="submit" value="注&nbsp;册" />&nbsp;
						<input type="button" value="返&nbsp;回"  onclick="location.href='${pageContext.request.contextPath}/index.jsp';"/>&nbsp;
					</td>
				  </tr>
				  
				</table>   
	</form>
</div>
<script type="text/javascript">
<c:if test="${not empty errMsg}">
alert("${errMsg}");
</c:if>
</script>
  </body>
</html>
