<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/notlimit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>长庆钻井机修公司-设备维修管理系统</title>
<head>
<link href="${pageContext.request.contextPath}/css/adminlogin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/RegExp.js"></script>
<script type="text/javascript">
if (top.location != self.location){
 	top.location.href ='${pageContext.request.contextPath}/api/login/tologin';
 }
$(document).ready(function(){
 $("#loginForm").bind("submit",function(){
   if(ifnull($(this['userName']).val())){
   alert("请输入用户名！");
   $(this['userName']).focus();
   return false;
   }
   if(ifnull($(this['userPwd']).val())){
   alert("请输入密码！");
   $(this['userPwd']).focus();
   return false;
   }
  if(ifnull($(this['number']).val())){
   alert("请输入验证码！");
   $(this['number']).focus();
   return false;
   }
 });

});
function changeImg(){
$("#vdimgck").attr("src","${pageContext.request.contextPath}/jsp/sys/login/image.jsp?d="+new Date());
}
</script>
</head>

<body>
<div class="login_top_img"></div>
	<div style="width:907px;">
		<div class="admin_top_title">
		    <h3 style="">长庆钻井机修公司-设备维修管理系统</h3>
		</div>
	<div class="login_top_zaozuo">
	<form name="loginForm" id="loginForm" method="post" action="${pageContext.request.contextPath}/api/login/login">
<table border="0" cellspacing="0" cellpadding="0">
	
      <input type="hidden" name="url" value="${pageContext.request.contextPath}/main.jsp"/>
      <tr>
        <td width="90px" align="right">用户名 </td>
        <td colspan="3">
            <label>
              <input type="text" id="userName" name="userName" value="" class="input"  style="ime-mode:disabled"  />
            </label>
        </td>
      </tr>
      <tr>
        <td width="50" align="right">密&nbsp;&nbsp;&nbsp;&nbsp;码</td>
        <td colspan="3">
        	<label>
              <input type="password" id="userPwd" name="userPwd" value="" style="ime-mode:disabled" class="input2" />
            </label>
        </td>
      </tr>
      <tr>
        <td width="50" align="right">验证码</td>
        <td width="52">
        <label>
          <input name="number" type="text" id="number" maxlength="4" class="input3"/>
        </label>
        	
        </td>
        <td width="60"><img id="vdimgck" src="${pageContext.request.contextPath}/jsp/sys/login/image.jsp" alt="看不清？点击更换" style="cursor:pointer" onclick="changeImg();"/></td>
        <td width="88" class="huanyizhang" onclick="changeImg();" style="cursor: pointer;">换一张</td>
      </tr>
      <tr>
        <td colspan="4">
            <span><label>
              <input type="submit" name="login" value="" class="btn"/>
            </label></span>
            <span><label>
              <!--  
      <input type="button" name="button" value="" class="btn2" onclick="location.href='${pageContext.request.contextPath}/jsp/sys/login/reg.jsp'"/>-->
      <input type="reset" name="button" value="" class="btn2"/>
              
            </label> </span>       </td>
      </tr>
    </table>
    </form>
</div>
	</div>
         <%-- 
         <li style="padding-top:10px;text-align:center;width:90px;float:left;">
       		<input type="button"  value="重复登陆失例" class="btn_login" tabindex="5" onclick="location.href='${pageContext.request.contextPath}/jsp/login/repeat.jsp'"/>
         </li>
         --%>
<script type="text/javascript">
<c:if test="${not empty errMsg}">
alert("${errMsg}");
</c:if>
<c:if test="${not empty param.reg}">
alert("注册成功！");
</c:if>
</script>
${msg}
</body>
</html>