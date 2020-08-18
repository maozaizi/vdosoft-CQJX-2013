<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script type="text/javascript">
		function sub(){
			$("#searchForm").submit();
		}
		</script>
		<script type="text/javascript">
		function toSelect(){//选择
		  if($("input[name='userId']").filter(":checked").length>0){
		  		var aa = "";
				var parentWindow = window.opener;	
				var $userId  = $("input[name='userId']").filter(":checked");
				$("input[name='userId']").filter(":checked").each(function(){
					aa += $(this).val() + ",";
				})
				var cn = ""; 
				$userId.parent().find("input[name='userName']").each(function(){
					cn += $(this).val() + ",";
				})
			    var paramValue = {userId:aa.substring(0,aa.length-1),pm:"${param.pm}",name:cn.substring(0,cn.length-1)};
				window.opener.reback(paramValue);
		        window.opener.focus();
		        window.opener = null;
		        window.close();
		        return ;
		  }else{
		    	alert("请选择要选择的信息！");
		  }
		}
		function toUpdatePage(obj){
			$(obj).find("input[name='userId']").attr("checked",true);
			toSelect();
		}
		</script>
	</head>
	<body>
	<div class="MainContent">
  		<div class="ListBlock">
			<h3 class="cBlack">用户管理列表</h3>	

			    <form id="userForm" method="post" action="">
				<input type="hidden" name="url" value="${pageContext.request.contextPath}/api/login/userList?${page.action}&currentPageNO=${page.currentPage}" />
				
			<div class="List">
				<table cellspacing="0" cellpadding="0" width="100%">
					<thead>
						<tr>
							<th width="10%">
								选择
							</th>
							<th width="20%">
								姓名
							</th>
							<th width="20%">
								Eamil
							</th>
							<th width="20%">
								注册时间
							</th>
							<th width="12%">
								用户类型
							</th>
							<th width="12%">
								审核状态
							</th>
						</tr>
					</thead>
					<tbody>
						<c:set value="" var="sids" />
						<c:forEach var="user" items="${usersMap}" varStatus="sta">
							<c:if test="${not empty user.userTypeId}">
								<c:set value="${sids},${user.userTypeId}" var="sids" />
							</c:if>
							<tr>
								<td>
									<input type="checkbox" id="userId" name="userId"
										value="${user.userId}">
									<input type="hidden" id="userName" name="userName"
										value="${user.name}" />
								</td>
								<td>
									${user.name}
								</td>
								<td>
									${user.email}
								</td>
								<td>
									${fn:substring(user.regDate,0,19)}
								</td>
								<td id="state${user.userTypeId}">
									${user.userTypeName}
								</td>
								<td>
									${user.verifyState==0?"<font color='red'>未审核</font>":"已审核"}
								</td>
							</tr>
						</c:forEach>
						</tbody>
						
						 <tfoot>
							 <tr>
							   <td  colspan="6" >
							   <div class="page">
									<%@include file="/jsp/public/standard.jsp"%>
								</div></td>
							 </tr>
						 <tfoot>
				</table>
				</div>
				
				<div class="Temp-0">
					<a class="Bbtn" href="#" onclick="toSelect();"><span class="Br">选择</span></a>
				</div>
			</form>
	</div>
  </div>
</body>

</html>
