<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>用户分类管理列表</title>
<link href="${pageContext.request.contextPath}/css/right.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script type="text/javascript">
	function update() {
	var userId=0;
		var user = document.getElementsByName("id");
		var checked = false;
		for (var i = 0; i < user.length; i++) {
			if (user[i].checked) {
				checked = true;
				userId=user[i].value;
			}
		}
		
		if (!checked) {
			alert("请选择用户！");
			return ;
		}
		
		$("#typeForm").submit();
		
		
	}

	function doDel() {
		var user = document.getElementsByName("id");
		var checked = false;
		for (var i = 0; i < user.length; i++) {
			if (user[i].checked) {
				checked = true;
			}
		}
		
		if (!checked) {
			alert("请选择用户！");
			return ;
		}
		
		if (confirm("您确定删除该用户吗？")) {
		$("#typeForm").attr("action","${pageContext.request.contextPath}/api/login/delUsertype?currentPageNO=${page.currentPage}")
		$("#typeForm").submit();
		}
	}
	

</script>

</head>
<body>
<div class="div_box">
	<div class="table_title">
		用户分类管理列表
	</div>
	<form id="typeForm" method="post" action="${pageContext.request.contextPath}/api/login/toUpdateUsertype?currentPageNO=${page.currentPage}">
	<input  type="hidden"  name="url" value="${pageContext.request.contextPath}/api/login/usertypeList?currentPageNO=${page.currentPage}"/>
    <table cellspacing="1" cellpadding="0" class="table_box table_list">
	  <thead>
	      <tr>
	      	<th width="20%">选择</th>
	        <th width="40%">分类名称</th>
	        <th width="40%">备注</th>
	      </tr>
      </thead>
      <tbody>
	    <c:forEach var="usertype" items="${usertypes}"> 
	      <tr>
	        <td><input type="radio" name="id" value="${usertype.userTypeId}"></td>
	      	<td>${usertype.userTypeName}</td>
	        <td>${usertype.userTypeRemark}</td>
	      </tr>
      </c:forEach>
      </tbody>
      <tfoot>
      <tr bgcolor="#ffffff">
        <td colspan="3" class="ltext_end">
          <input type="button" value="添加分类"  onclick="location.href='${pageContext.request.contextPath}/jsp/sys/usertype/usertype_add.jsp?currentPageNO=${page.currentPage}'" class="btn"/>
	  	  <input type="button" value="修改"  onclick="update();" class="btn"/>
	  	  <input type="button" value="删除"  onclick="doDel();" class="btn"/>
	  	  &nbsp;&nbsp;
		</td>
      </tr>
      </tfoot>
    </table>
	</form>
	<div class="page">
       <%@include file="/jsp/public/standard.jsp" %>
    </div>
</div>
  </body>
</html>
