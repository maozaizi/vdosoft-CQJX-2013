<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>权限组列表</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" media="screen"/>
		<script src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/RegExp.js" type="text/javascript"></script>
		<script type="text/javascript">
			function doQuery() {
				document.form1.action = "${pageContext.request.contextPath }/api/authgroup/list";
				document.form1.submit();
			}
		
			function goAdd() {
				document.form1.action = "${pageContext.request.contextPath }/jsp/sys/authgroup/authgroup_add.jsp";
				document.form1.submit();
			}
			
			function goModify() {
				var checked = false;
				var id = document.getElementsByName("authGroupID");
				var value = "";
				for (var i = 0; i < id.length; i++) {
					if (id[i].checked) {
						checked = true;
						value = id[i].value;
					}
				}
				
				if(!checked) {
					alert("请选择权限组！");
					return ;
				}
				
				document.form1.action = "${pageContext.request.contextPath }/api/authgroup/detail?authGroupId=" + value;
				document.form1.submit();
			}
			
			function doDel() {
				var checked = false;
				var id = document.getElementsByName("authGroupID");
				var value = "";
				for (var i = 0; i < id.length; i++) {
					if (id[i].checked) {
						checked = true;
						value = id[i].value;
					}
				}
				
				if(!checked) {
					alert("请选择权限组！");
					return ;
				}
				
				if (confirm("您确定删除该权限组吗？")) {
					document.form1.action = "${pageContext.request.contextPath }/api/authgroup/delete?authGroupId=" + value;
					document.form1.submit();
				}
			}
			
			function goMembList() {
				var authGroupId;
				var checked = false;
				var id = document.getElementsByName("authGroupID");
				for (var i = 0; i < id.length; i++) {
					if (id[i].checked) {
						authGroupId = id[i].value;
						checked = true;
					}
				}
				
				if(!checked) {
					alert("请选择权限组！");
					return ;
				}
				
				document.form1.action = "${pageContext.request.contextPath }/api/authgroup/memberlist?authGroupId=" + authGroupId;
				document.form1.submit();
			}
		</script>
	</head>
	<body>
		<div class="row">
	  	<div class="span2"></div>
	  	<div class="span10">
		<h3 >权限组信息列表</h3>
			<form id="form2" name="form2" method="post" action="">
				<table width="100%" border="0"  cellpadding="0" cellspacing="0" class="table table-hover table-bordered">
					<thead>
						<tr>
							<th width="10%">
								选择
							</th>
							<th width="40%">
								权限组名称
							</th>
							<th width="50%">
								描述
							</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.resultList}" var="authGroup">
							<tr>
								<td>
									<input type="radio" id="authGroupID" name="authGroupID" value="${authGroup.authGroupId }" />
								</td>
								<td>
									<label>
										${authGroup.authGroupName }
									</label>
								</td>
								<td>
									${authGroup.authGroupRemark }
								</td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
			        		<td colspan="3">
			        			<div class="page">
									<%@include file="/jsp/public/standard.jsp"%>
								</div>
			        		</td>
					    </tr>
					</tfoot>
				</table>
				
			</form>
			
				<a class="btn" href="#" onclick="goAdd();" ><span class="r">添加</span></a>
				<a class="btn" href="#" onclick="goModify();"><span class="r">修改</span></a>
				<a class="btn" href="#" onclick="doDel();" ><span class="r">删除</span></a>
				<a class="btn" href="#" onclick="goMembList();"><span class="r">权限管理</span></a>
			
		
		<form id="form1" name="form1" method="post" action="" >
			<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/authgroup/list?currentPageNO=${page.currentPage}"/>
				<fieldset>
				    <legend>查询条件</legend>
					<table>
						<tbody>
						<tr>
						   <th width="96">权限组名称：</th>
						<td><input name="authGroupName" id="authGroupName" size="36" type="text" value="${authGroupName }">
						    <a class="btn btn-primary" name="sss" href="#" onclick="doQuery();;">搜　索</a>
						</td>
						</tr>
						</tbody>
					</table>
						    
					</fieldset>
			</form>
	</div>
	</body>
</html>

