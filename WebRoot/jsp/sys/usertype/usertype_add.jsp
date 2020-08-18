<%@ page language="java" pageEncoding="utf-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<head>
 <title>用户分类添加</title>
<link href="${pageContext.request.contextPath}/css/right.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/RegExp.js"></script>
<script type="text/javascript">
$(document).ready(function(){
 $("#typeForm").bind("submit",function(){
   if(ifnull($(this['userTypeName']).val())){
   alert("分类名称不能为空！");
   $(this['userTypeName']).focus();
   return false;
   }
   if(ifnull($(this['userTypeCode']).val())){
   alert("分类代码不能为空！");
   $(this['userTypeCode']).focus();
   return false;
   }
 });

});

function sub(){
  $("#typeForm").submit();
}
</script>
</head>
<html>
   <body>
   <div class="div_box">
	<div class="table_title">
		用户分类添加
	</div>
	<form id="typeForm" method="post" action="${pageContext.request.contextPath}/api/login/addUsertype" >
	<input  type="hidden"  name="url" value="${pageContext.request.contextPath}/api/login/usertypeList?currentPageNO=${param.currentPage}"/>
		<table cellspacing="1" cellpadding="0" class="table_box table_add">
				  <tr>
					<td class="ltext">分类名称</td>
					<td class="rtext">
					  	<input  type="text"  class="shuru" name="userTypeName" value="${param.userTypeName}" maxlength="25" />
					</td>
				  </tr>
				  <tr>
					<td class="ltext">分类代码</td>
					<td class="rtext">
					  	<input  type="text"  class="shuru" name="userTypeCode" value="${param.userTypeCode}" maxlength="25" />
					</td>
				  </tr>
				  
				 <tr>
					<td class="ltext">备注</td>
					<td class="rtext">
					  	<input  type="text"  class="shuru" name="userTypeRemark" value="${param.userTypeRemark}" maxlength="25"/>
					</td>
				  </tr>
				  <tr>
					<td class="ltext">&nbsp;</td>
					<td class="rtext">
					    <input type="button" value="提&nbsp;交"  onclick="sub();" class="btn"/>&nbsp;
						<input type="button" value="返&nbsp;回"  onclick="window.location.href='${pageContext.request.contextPath}/api/login/usertypeList?currentPageNO=${param.currentPage}';" class="btn"/>&nbsp;
					</td>
				  </tr>
				</table>   
	</form>
</div>
  </body>
</html>
