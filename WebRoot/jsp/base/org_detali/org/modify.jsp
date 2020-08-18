<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">
function checkForm() {
	var organizationDetailName = document.getElementById("organizationDetailName").value;
	if (organizationDetailName == "") {
		alert("请输入基础名称！");
		return ;
	}
		document.forms[0].action = "${pageContext.request.contextPath }/api/org/modifyDept";
		document.forms[0].submit();
}

function goBack() {
	  window.location.href = "${pageContext.request.contextPath}/api/org/getInfo?organizationDetailId=${param.organizationDetailId}";
}
</script>
</head>
<body>
	<div class="MainContent">
		<div class="ListBlock">
		    <h3 class="cBlack">修改部门</h3>

			<form id="addForm" method="post" action="" >
			<input type="hidden" name="organizationDetailId" id="organizationDetailId" value="${organizationDetali.organizationDetailId}"/>
			<input type="hidden" name="tempUrl" id="tempUrl" value = "${pageContext.request.contextPath}/api/org/getInfo?organizationDetailId=${param.organizationDetailId}"/>
	       	<div class="EditBox">
	        <table width="100%" cellspacing="0" cellpadding="0">
				<tr>
				  <th colspan="2">修改部门</th>
				</tr>
			  <tr>
				<td class="EditBox_td1"><span style="color: Red">*&nbsp;</span>组织名称：</td>
				<td>
				  	<input name="organizationDetailName" id="organizationDetailName" value="${organizationDetali.organizationDetailName }" type="text" class="ipt_text" />
				</td>
			  </tr>
			  <tr>
			  	<td class="EditBox_td1">组织描述：</td>
				<td>
				  	<textarea name="memo" id="memo" rows="6" cols="39" class="ipt_text" >${organizationDetali.memo }</textarea>
				</td>
			  </tr> 
			</table>
            </div>
			  </form>
            
			  <div class="Temp-1">
			  	<a class="Bbtn" href="#"  name="submitter" onclick="checkForm();"><span class="Br">保存</span></a>
			  	<a class="Bbtn" href="#" onclick="goBack()"><span class="Br">返回</span></a>
			  </div>
	</div>
</div>		  
</body>
</html>

