<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="keywords" content="列表">
<meta http-equiv="description" content="列表">
<title>用户管理列表</title>
<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script type="text/javascript">
	function update() {
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
		$("#userForm").attr("action","${pageContext.request.contextPath}/api/login/toUpdateUser")
		$("#userForm").submit();
		
		
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
		$("#userForm").attr("action","${pageContext.request.contextPath}/api/login/delUser")
		$("#userForm").submit();
		}
	}
	function sub(){
	$("#searchForm").submit();
	}
	
	function repwd(){
		var user = document.getElementsByName("userId");
		var id="";
			var checked = false;
			for (var i = 0; i < user.length; i++) {
				if (user[i].checked) {
					checked = true;
					id=user[i].value;
				}
			}
			
			if (!checked) {
				alert("请选择用户！");
				return ;
			}
			
			if (!confirm("您要重置此用户密码？")) {
			return;
			}

	
    $.ajax({
	   type: "POST",
	   url: "${pageContext.request.contextPath}/ajaxapi/login/rePwd",
	   data: "userId="+id,
		   success: function(msg){
		  // alert(msg);
		   var resu=eval("["+msg+"]");
		   if(resu[0].str=="1"){
		    alert("重置成功!\r\n\r\n初始密码:"+resu[0].pwd);
		    }else{
		    alert("重置失败！");
		    }
		   }
	  });

	}
</script>
</head>
<body>
  	<div class="MainContent">
	  	<div class="ListBlock">
	  	<h3 class="cBlack">用户信息列表</h3>

				
				<form id="userForm" method="post" action="">
				<div class="List">
					<input  type="hidden"  name="url" value="${pageContext.request.contextPath}/api/login/userList?${page.action}&currentPageNO=${page.currentPage}"/>
					<table width="100%" border="0"  cellpadding="0" cellspacing="0">
						<thead>
							<tr>
								<th width="5%">选择</th>
					        	<th>用户名</th>
					        	<th>Eamil</th>
					        	<th>注册时间</th>
					        	<th>用户类型</th>
					        	<th>审核状态</th>
					        </tr>
				        </thead>
				        <tbody>
				      		<c:set value="" var="sids"/>
						   <c:forEach var="user" items="${usersMap}" varStatus="sta">
						       <c:if test="${not empty user.userTypeId}"><c:set value="${sids},${user.userTypeId}" var="sids"/></c:if>
						      <tr>
						        <td><input type="radio" name="userId" value="${user.userId}"></td>
						      	<td>${user.userName}</td>
						        <td>${user.email}</td>
						        <td>${fn:substring(user.regDate,0,10)}</td>
						        <td id="state${user.userTypeId}">${user.userTypeName}</td>
						        <td>${user.verifyState==0?"<font color='red'>未审核</font>":"已审核"}</td>
						      </tr>
					      	</c:forEach>
				      	</tbody>
				      	<tfoot>
					        <tr>
				        		<td colspan="6">
				        			<div class="page">
								  		<%@include file="/jsp/public/standard.jsp" %>
									</div>
				        		</td>
					        </tr>
						</tfoot>
				     </table>
				</div>
				<div class="opt_btn">
					<div class="xy">
						<a class="btn" href="#" onclick="location.href='${pageContext.request.contextPath}/api/login/toAddUser'" ><span class="r">添加用户</span></a>
						<a class="btn" href="#" onclick="repwd();"><span class="r">重设密码</span></a>
						<a class="btn" href="#" onclick="update();" ><span class="r">修改</span></a>
						<a class="btn" href="#" onclick="doDel();"><span class="r">删除</span></a>
					</div>
				</div>
			</form>
		</div>
		
		
		
		  	<form id="searchForm" name="form" method="post" action="${pageContext.request.contextPath}/api/login/userList">
		  		<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/usergroup/getUserList?usergroupid=${param.usergroupid}" />&nbsp;
			    <input type="hidden" name="usergroupid" id="usergroupid" value="${param.usergroupid}"/>&nbsp;

					<div class="SearchBlock">
					    <fieldset>
					    <legend>查询条件</legend>
					    <table>
					      <tbody>
					        <tr>
					          <th width="70px">用户名：</th>
					          <td><input name="userName" type="text" size="30" value="${param.userName}"></td>
					          <th width="86px">注册时间：</th>
					          <td><input name="starttime" size="9"  value="${param.starttime}" onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true"/>至
					              <input name="endtime"  size="9" value="${param.endtime}" onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true"/>
					          </td>
					        </tr>
					        <tr>
 							  <th width="96px">Email：</th>
					          <td><input name="email" size="30" type="text" value="${param.email}"></td>
					          <th>审核状态：</th>
					          <td >
					          	<select name="verifyState">
									<option value="">全部</option>
									<option value="0" ${param.verifyState==0?"selected=selected":""}>未审核</option>
									<option value="1" ${param.verifyState==1?"selected=selected":""}>已审核</option>
								</select>
					          </td>
					        </tr>
					      </tbody>
					    </table>
					    <a class="Bbtn schPos" name="sss" href="#" onclick="sub();"><span class="Br">搜　索</span></a>
					    </fieldset>
						</div>
				</form>
		
		
		
		
	</div>
</body>
<%-- 
<c:if test="${not empty sids}">

<script type="text/javascript">
$(document).ready(function(){
  $.ajax({
   type: "POST",
   url: "${pageContext.request.contextPath}/api/login/excuteUserTypes",
   data: "ids=${sids}",
   success: function(msg){
    alert(msg);
    
   
   }
  });
   
});
</script>
</c:if> --%>
</html>
