<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>用户组管理列表</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="keywords" content="用户组管理">
		<meta http-equiv="description" content="用户组管理列表">
		<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script type="text/javascript">
		function addClick(){
		    document.forms[0].action = "${pageContext.request.contextPath}/api/usergroup/addUserGroup";
	        document.forms[0].submit();
		}
		function editClick(){
		  var f=false; 
		  $("input[type=radio]:checked").each(function (i){f=true;});
          if(f){
            var a=$("input[type=radio]:checked").val();
            document.forms[0].action = "${pageContext.request.contextPath}/api/usergroup/goModifyUserGroup?userGroupId="+a;
	        document.forms[0].submit();
		   }else{
			alert("请选择要修改的信息！");
		   }
		}
		function deleteClick(){
		  var f=false;  
            $("input[type=radio]:checked").each(function (i){f=true;});
			if(f){
			if(confirm("你确认删除吗？")){
			 var a=$("input[type=radio]:checked").val();
			 // window.location.href="${pageContext.request.contextPath}/action.do?method=usergroup.removeUserGroup&userGroupId="+a;
			$("#usergroupid").val(a);
			document.forms[0].action ="${pageContext.request.contextPath}/api/usergroup/removeUserGroup";
	        document.forms[0].submit();
	        }
			}else{
				alert("请选择要删除的信息！");
			}
			
		}
		function usermanageClick(){
		  var f=false;  
          $("input[type=radio]:checked").each(function (i){f=true;});
          if(f){ 
             var a=$("input[type=radio]:checked").val();
             $("#usergroupid").val(a);
              document.forms[0].action="${pageContext.request.contextPath}/api/usergroup/getUserList";
		      document.forms[0].submit();
		  }else{
			 alert("请选择用户组！");
		  }
		}
		function grouppowerClick(){
		  var f=false;  
          $("input[type=radio]:checked").each(function (i){f=true;});
          if(f){
             var a=$("input[type=radio]:checked").val();
             $("#usergroupid").val(a);
			 document.forms[0].action="${pageContext.request.contextPath}/api/usergroup/goUserGroupPower";
		     document.forms[0].submit();
		  }else{
			alert("请选择用户组！");
		  }
		}
</script>
<body>
 	<div class="MainContent">
	  	<div class="ListBlock">
	  	<h3 class="cBlack">用户组管理列表</h3>
			<form id="form2" name="form2" method="post" action="">
			    <input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/usergroup/getUserGroupList?currentPageNO=${page.currentPage}&${page.action}"/>
				 <input type="hidden" name="usergroupid" id="usergroupid" value=""/>
				<div class="List">
					<table width="100%" cellspacing="0" cellpadding="0">
						<thead>
							<tr>
								<th width="5%">
									选择
								</th>
								<th width="40%">
									用户组名称
								</th>
								<th width="55%">
									备注
								</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.resultList}" var="ugroup">
								<tr>
									<td>
										<input type="radio" name="userGroupId" id="userGroupId"
											value="${ugroup.userGroupId}" title="${ugroup.userGroupId}" />
									</td>
									<td>
										${ugroup.userGroupName}
									</td>
									<td>
										${ugroup.userGroupRemark}
									</td>
								</tr>
							</c:forEach>
						</tbody>
						<tfoot>
							<tr>
				        		<td colspan="3">
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
						<a class="btn" href="#" onclick="editClick();"><span class="r">修改</span></a>
						<a class="btn" href="#" onclick="deleteClick();" ><span class="r">删除</span></a>
						<a class="btn" href="#" onclick="usermanageClick();"><span class="r">成员管理</span></a>
						<a class="btn" href="#" onclick="grouppowerClick();"><span class="r">用户组授权</span></a>
					</div>
				</div>
			</form>
			<!-- 分页 -->
			
		</div>
		<!--鼠标划过tr背景色改变-->
	</body>
</html>

