<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
	<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
	<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" media="screen">
	<script src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	
	
	
	<script type="text/javascript">
		function checkForm() {
			var orgName = document.getElementById("orgName").value;
			var orgCode = document.getElementById("orgCode").value;
			if (orgName == "") {
				alert("请输入组织名称！");
				return ;
			}
			if (orgCode == "") {
				alert("请输入编码！");
				return ;
			}
		     document.forms[0].action = "${pageContext.request.contextPath }/api/baseorganization/save";
			 document.forms[0].submit();
		}
		
		function goBack() {
		      var id = document.getElementById("parentId").value;
			  window.location.href = "${pageContext.request.contextPath}/api/baseorganization/list?";
		}
	</script>
	
	</head>
<body>
	<div class="row">
	  <div class="span0"></div>
	  <div class="span12">
	     <form id="addForm" method="post" action="" >
			<input type="hidden" name="parentId" id="parentId" value="${param.id}"/>
			<input type="hidden" name="parentCode" id="parentCode" value="${param.parentCode}"/>
			<input type="hidden" name="tempUrl" id="tempUrl" value = "${pageContext.request.contextPath}/api/baseorganization/list"/>
	     
		 <div class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="orgName">名称</label>
				<div class="controls">
					<input type="text" class="span2" id="orgName" name="orgName" placeholder="请输入组织名称"><span class="help-inline">请输入组织名称</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="orgName">上级编码</label>
				<div class="controls">
						<c:if test="${param.parentId == -1}">根节点</c:if>
						<c:if test="${param.parentId != -1}">${param.parentCode}</c:if>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="orgName">编码</label>
				<div class="controls">
					<input type="text" class="span2" id="orgCode" name="orgCode" placeholder="请输入编码"><span class="help-inline">请输入编码</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="memo">组织描述</label>
				<div class="controls">
					<textarea rows="3" name="memo" id="memo"></textarea><span class="help-block">请输入组织描述</span>
				</div>
			</div>

			<div class="form-actions">
				<a href="#" class="btn btn-primary" onclick="checkForm();">保存</a>
				<a href="#" class="btn" onclick="goBack()">取消</a>
			</div>
		</div>
		</form>
	
	  </div>		
	</div>
</body>
</html>

