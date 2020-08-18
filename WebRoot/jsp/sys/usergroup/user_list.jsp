<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>用户组成员管理列表</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="keywords" content="用户组成员管理">
		<meta http-equiv="description" content="用户组成员管理列表">
		<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script type="text/javascript">
			$(document).ready(function (){
				$("#delete").click(function (){
				var f=false;  
				 $("input[type=radio]:checked").each(function (i){f=true;});
				        if(f){
				        if(confirm("你确认删除吗？")){
				        var a=$("input[type=radio]:checked").val();
				        $("#userid").val(a);
				         document.form2.action="${pageContext.request.contextPath}/api/usergroup/removeUserGroupMember";
						 document.form2.submit();
						 }
						}else{
							alert("请选择要删除的信息！");
						}			
				});
			});
			function goBack() {
			        window.location.href = "${pageContext.request.contextPath}/api/usergroup/getUserGroupList"; 
				 }
			function addClick(){
					     document.form2.action="${pageContext.request.contextPath}/api/usergroup/getAddUserList";
			             document.form2.submit();
			}
		</script>
	</head>
<body>
	<div class="MainContent">
	  	<div class="ListBlock">
	  	<h3 class="cBlack">用户组成员管理列表</h3>
			<form id="form2" name="form2" method="post" action="">
			<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/usergroup/getUserList?currentPageNO=${page.currentPage}&usergroupid=${param.usergroupid}"/>
			<input type="hidden" name="userid" id="userid" value=""/>
			<input type="hidden" name="usergroupid" id="usergroupid" value="${param.usergroupid}"/>
			 <div class="List">
				<table width="100%"  border="0"  cellpadding="0" cellspacing="0">
					<thead>
						<tr>
							<th width="5%">
								选择
							</th>
							<th>
								成员名称
							</th>
							<th>
								成员状态
							</th>
							<th>
								注册时间
							</th>
							<th>
								登陆次数
							</th>
						</tr>
					</thead>
					<tbody>
					 <c:forEach items="${page.resultList}" var="user">
						<tr>
						   <td>
								<input type="radio" name="userId" id="userId" value="${user.userId}" title="${user.userId}"/>
							</td>
							<td>
								${user.userName}
							</td>
							<td>
								${user.verifyState==1?'已审核':'未审核'}
							</td>
							<td>
								${fn:substring(user.regDate,0,10)}
							</td>
							<td>
								${user.loginCount}
							</td>
						</tr>
					</c:forEach>
					</tbody>
					<tfoot>
						<tr>
				        		<td colspan="5">
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
						<a class="btn" href="#" onclick="addClick();" ><span class="r">添加</span></a>
						<a class="btn" href="#" name="delete" id="delete" onclick="" ><span class="r">删除</span></a>
						<a class="btn" href="#" onclick="goBack();"><span class="r">返回</span></a>
					</div>
				</div>
			</form>
		</div>
		<!--鼠标划过tr背景色改变-->
	</body>
</html>


