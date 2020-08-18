<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>用户组授权</title>
		<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<script type="text/javascript">
			$(document).ready(function (){
			
			$("#save").click(function (){
			// var f=false;  
			// $("input[type=checkbox]:checked").each(function (i){f=true;});
	       //if(f){
			document.forms[0].action="${pageContext.request.contextPath}/api/usergroup/saveUserGroupPower";
			document.forms[0].submit();
			//}else{
			// alert("请选择权限组！");
			//}
			});
			});
			
			function goBack() {
		    window.location.href = "${backurl}";
		     }
		</script>
	</head>
	<body>
		<div class="MainContent">
		<div class="ListBlock">
		<h3 class="cBlack">用户组授权</h3>
		  <form method="post" action="">
		  <div class="EditBox">
				<table width="100%"  border="0" cellpadding="0" cellspacing="0">
					<tr>
					    <th colspan="3">用户组授权</th>
					</tr>
					<tr>
						<td class="EditBox_td1">
							用户组名称：
						</td>
						<td>
							${userGroup.userGroupName}
							<input name="usergroupid" id="usergroupid" value="${userGroup.userGroupId}" type="hidden" class="shuru"/>
							<input type="hidden" name="url" id="url" value="${backurl}"/>
						</td>
					</tr>
					<tr>
						<td class="EditBox_td1">
							权限组名称：
						</td>
						<td colspan="3">
						<c:forEach items="${groupPowerList}" var="gp">
						<input  name="authgroupid" id="gp_${gp.authGroupId}" type="checkbox"  class="shuru" value="${gp.authGroupId}" title="${gp.authGroupId}"/>${gp.authGroupName}
						 <script type="text/javascript">
					      <c:forEach items="${userGroupPowerList}" var="ugp">
					      if($('#gp_${gp.authGroupId}').val()=='${ugp.authGroupId}')
					       $("#gp_${gp.authGroupId}").attr("checked","checked");
					       </c:forEach>
					    </script>
						</c:forEach>
						</td>
					</tr>
				</table>
				</div>
				<div class="Temp-2">
					<a class="Bbtn" href="#" name="save" id="save" onclick="update();"><span class="Br">保存</span></a>
					<a class="Bbtn" href="#" onclick="goBack();"><span class="Br">返回</span></a>
				</div>  
			</form>
		</div>
		</div>
	</body>
</html>

