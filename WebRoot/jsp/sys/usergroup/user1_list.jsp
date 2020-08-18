<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>添加用户组成员管理列表</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="keywords" content="用户组成员管理">
		<meta http-equiv="description" content="用户组成员管理列表">
		<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script type="text/javascript">
		function addClick(){
			var f=false;  
	      $("input[type=radio]:checked").each(function (i){f=true;});
	        if(f){
	        var a=$("input[type=radio]:checked").val();
	        $("#userid").val(a);
	        document.form2.action="${pageContext.request.contextPath}/api/usergroup/saveUserGroupMember";
			document.form2.submit();
			}else{
				alert("请选择要添加的成员！");
			}
		}
		 function goBack() {// ${param.url}
		     window.location.href ="${addugurl}";
		 }
		 function sub(){
			$("#searchForm").submit();
			}
		</script>
	</head>
	<body>
	<div class="MainContent">
	  	<div class="ListBlock">
	  	 <h3 class="cBlack">添加用户组成员管理列表</h3>

	    
		<form id="form2" name="form2" method="post" action="">
			<input type="hidden" name="burl" id="burl" value="${pageContext.request.contextPath}/api/usergroup/getUserGroupList?currentPageNO=${page.currentPage}&${page.action}"/>
            <input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/usergroup/getUserList?usergroupid=${param.usergroupid}"/>
			 <input type="hidden" name="usergroupid" id="usergroupid" value="${param.usergroupid}"/>
			  <input type="hidden" name="userid" id="userid" value=""/>
				<div class="List">
				<table width="100%" border="0"  cellpadding="0" cellspacing="0">
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
			</form>
			<div class="opt_btn">
					<div class="xy">
						<a class="btn" href="#" name="add" id="add" onclick="addClick();" ><span class="r">添加</span></a>
						<a class="btn" href="#" onclick="goBack();"><span class="r">返回</span></a>
					</div>
				</div>
			</div>

		 <form id="searchForm" method="post" action="${pageContext.request.contextPath}/api/usergroup/userMembList" class="search_box">
	  	 <input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/usergroup/getUserList?usergroupid=${param.usergroupid}"/>
		 <input type="hidden" name="usergroupid" id="usergroupid" value="${param.usergroupid}" class="shuru"/>

			    <div class="SearchBlock">
				    <fieldset>
				    <legend>查询条件</legend>
				
				    <table>
				      <tbody>
				        <tr>
				          <th width="96px">用户名：</th>
				          <td><input name="userName" size="23" type="text" value="${param.userName}"></td>
				          <th width="96px">Email：</th>
				          <td><input name="email" size="23" type="text" value="${param.email}"></td>
				        </tr>
				
				        <tr>
				          <th>注册时间：</th>
				          <td>
				            <input name="starttime" size="9" value="${param.starttime}" onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true"/>
				                         至
							<input name="endtime" size="9" value="${param.endtime}" onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true"/>
				          </td>

				        </tr>
				      </tbody>
				    </table>
				    <a class="Bbtn schPos" name="sss" href="#" onclick="sub();"><span class="Br">搜　索</span></a>
				    </fieldset>
			    </div>
			</form>
	</body>
</html>

