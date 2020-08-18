<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>用户审核列表</title>
<link href="${pageContext.request.contextPath }/css/right.css" rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script type="text/javascript">
	function ault() {
	var userId=0;
		var user = document.getElementsByName("userId");
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
		
		if (confirm("您确定该用户审核通过吗？")) {
		 $("#checkForm").submit();
		}
	}

	function doDel() {
		var user = document.getElementsByName("userId");
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
			alert("删除成功！");
			
			document.forms[0].action = "${pageContext.request.contextPath }/jsp/sys/user/user_check_list.jsp";
			document.forms[0].submit();
		}
	}
	function sub(){
	$("#searchForm").submit();
	}
</script>
</head>


<body>
  <div class="div_box">
		<div class="table_title">
			用户审核列表
		</div>
  </div>
		<div class="right_box">
		    <form id="searchForm" method="post" action="${pageContext.request.contextPath}/api/usergroup/userMembList" class="search_box">
			    用户名&nbsp;<input name="userName"  value="${param.userName}" maxlength="16" class="shuru"/>&nbsp;&nbsp;Email&nbsp;<input name="email"  value="${param.email}" maxlength="50" class="shuru"/>&nbsp;&nbsp;注册起始时间&nbsp;<input name="starttime"  value="${param.starttime}" onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true"  class="datetimebox"/>&nbsp;&nbsp;注册结束时间&nbsp;<input name="endtime"  value="${param.endtime}" onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" class="datetimebox"/>&nbsp;<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/usergroup/getUserList?usergroupid=${param.usergroupid}" class="shuru"/>&nbsp;<input type="hidden" name="usergroupid" id="usergroupid" value="${param.usergroupid}" class="shuru"/>&nbsp;<input type="button" value="搜&nbsp;索" onclick="sub();" class="btn"/></form>
	    </div>
		<div class="clear"></div>
<div class="div_box">
<form id="checkForm" method="post" action="${pageContext.request.contextPath}/api/login/userCheck" target="rightFrame">
<input type="hidden" name="url" value="${pageContext.request.contextPath}/api/login/userCheckList"/>
    <table cellspacing="1" cellpadding="0" class="table_box table_list">
	  <thead>
	      <tr>
	      	<th width="20%">选择</th>
	        <th width="40%">用户名</th>
	        <th width="20%">Eamil</th>
	        <th width="20%">注册时间</th>
	      </tr>
      </thead>
      <tbody>
      <c:forEach var="user" items="${usersMap}">
	      <tr>
	        <td><input type="radio" name="userId" value="${user.userId}"></td>
	      	<td>${user.userName}</td>
	        <td>${user.email}</td>
	        <td>${fn:substring(user.regDate,0,10)}</td>
	      </tr>
      </c:forEach>
      </tbody>
      <tfoot>
      <tr>
        <td colspan="4" class="ltext_end" bgcolor="#ffffff">
	  	  <input type="button" value="审核"  onclick="ault();" class="btn"/>
	  	  <%-- 
	  	  <input type="button" value="删除"  onclick="doDel()"/>--%>
	  	  &nbsp;&nbsp;
		</td>
      </tr>
      </tfoot>
    </table>
 </form>
<div class="page">
	<%@include file="/jsp/public/standard.jsp"%>
</div>
</div>
  </body>
</html>
