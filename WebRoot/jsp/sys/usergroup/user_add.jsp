<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>用户组成员添加</title>
		<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
		<script type="text/javascript">
			function save(){
			alert("保存成功！");
			document.forms[0].action = "${pageContext.request.contextPath}/action.do?method=usergroup.SaveUserGroup";
		    document.forms[0].submit();
			}
			function goBack() {
		   window.location.href = "${pageContext.request.contextPath}/jsp/usergroup/user_list1.jsp";
		  }
		</script>
	</head>
<body>
	<div class="MainContent">
		<div class="ListBlock">
		<h3 class="cBlack">用户组成员添加</h3>
			<form method="post" action="">
				<div class="EditBox">
					<table width="100%"  border="0" cellpadding="0" cellspacing="0">
				         <tr>
				            <th colspan="2">用户组成员添加</th>
				         </tr>
						<tr>
							<td class="EditBox_td1" width="100px">
								<span style="color: Red">*&nbsp;</span>用户组成员名称：
							</td>
							<td>
								<input name="userGroupName" id="userGroupName" value="" type="text" class="ipt_text" size="30" />
							</td>
						</tr>
						<tr>
							<td class="EditBox_td1">
								备注：
							</td>
							<td colspan="3">
								<input name="userGroupRemark" id="userGroupRemark" value="" type="text" class="Input_200"/>
							</td>
						</tr>
					</table>
				</div>
				<div class="Temp-2">
					<a class="Bbtn" href="#" onclick="save();"><span class="Br">保存</span></a>
					<a class="Bbtn" href="#" onclick="goBack();"><span class="Br">返回</span></a>
				</div>  
			</form>
		</div>
		</div>
	</body>
</html>

