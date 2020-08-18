<%@ page language="java" pageEncoding="utf-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>用户密码修改</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<base target="_self"/>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" media="screen"/>
		<script src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/RegExp.js"></script>
		<script type="text/javascript">
		$(document).ready(function(){
			var flag = ${flag};
				if (flag==1){
				  alert("修改成功！");
				  window.close();
				}
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
			<div class="form-horizontal">
				<div class="control-group">
					<label class="control-label" >原始密码：</label>
					<div class="controls">
						<input type="password"  name="userPwd" class="span2" />
					</div>	
				</div>
				<div class="control-group">
					<label class="control-label" >新密码：</label>
					<div class="controls">
						<input type="password"  name="newPwd" class="span2" />
					</div>	
				</div>
				<div class="control-group">
					<label class="control-label" >重复密码：</label>
					<div class="controls">
						<input type="password"  name="renewPwd" class="span2" />
					</div>	
				</div>
				<div class="form-actions">
					<a class="btn btn-primary" href="#" onclick="sub();">保存</a>
					<a class="btn" href="#" onclick="javascript:window.close();">关闭</a>
				</div>  
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
