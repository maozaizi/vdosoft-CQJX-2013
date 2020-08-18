<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>用户组修改</title>
		<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
		<script type="text/javascript">
			function update(){
				if($("#usergroupname").val()==null ||$("#usergroupname").val()==''){
				$("#usergroupname").focus();
				alert("用户组名称不能为空！");
				return false;
				}
				document.forms[0].action = "${pageContext.request.contextPath}/api/usergroup/modifyUserGroup";
			    document.forms[0].submit();
				}
				function goBack() {
				window.location.href ="${param.url}";
			   // window.location.href = "${pageContext.request.contextPath}/action.do?method=usergroup.getUserGroupList";
		 	}
		</script>
	</head>
<body>
	<div class="MainContent">
		<div class="ListBlock">
		<h3 class="cBlack">用户组修改</h3>
			<form method="post" action="">
			    <input type="hidden" name="url" id="url" value="${param.url}"/>
				<div class="EditBox">
					<table width="100%"  border="0" cellpadding="0" cellspacing="0">
						<tr>
							<th colspan="2">
								用户组修改
							</th>
						</tr>
						<tr>
						<tr>
							<td class="EditBox_td1">
								<span style="color: Red">*&nbsp;</span>用户组名称：
							</td>
							<td>
								<input name="usergroupname" id="usergroupname" value="${userGroup.userGroupName}" type="text" class="ipt_text" size="30" />
							</td>
						</tr>
						<tr>
							<td class="EditBox_td1">
								备注：
							</td>
							<td colspan="3">
								<input name="usergroupremark" id="usergroupremark" value="${userGroup.userGroupRemark}" type="text" class="Input_200" />
						        <input name="usergroupid" id="usergroupid" value="${userGroup.userGroupId}" type="hidden"/>
						        <input name="createuser" id="createuser" value="${userGroup.createUser}" type="hidden"/>
						        <input name="createdate" id="createdate" value="${userGroup.createDate}" type="hidden"/>
						   </td>
						</tr>
					</table>
				</div>
				<div class="Temp-2">
					<a class="Bbtn" href="#" onclick="update();"><span class="Br">保存</span></a>
					<a class="Bbtn" href="#" onclick="goBack();"><span class="Br">返回</span></a>
				</div>  
			</form>
		</div>
	</div>
</body>
</html>

