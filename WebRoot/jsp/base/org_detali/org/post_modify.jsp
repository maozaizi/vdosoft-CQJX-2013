<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<link href="${pageContext.request.contextPath }/css/bootstrap.min.css" rel="stylesheet" media="screen" />
<script src="${pageContext.request.contextPath }/js/jquery.1.7.2.min.js"></script>
<script src="${pageContext.request.contextPath }/js/bootstrap.min.js"></script>
<script type="text/javascript">
function checkForm() {
	var postName = document.getElementById("postName").value;
	if (postName == "") {
		alert("请输入岗位名称！");
		return ;
	}
		document.forms[0].action = "${pageContext.request.contextPath }/api/org/modifyPostInfo";
		document.forms[0].submit();
}

function goBack() {
	  window.location.href = "${pageContext.request.contextPath}/api/org/getInfo?orgCode=${postInfo.orgId}";
}
</script>
</head>
<body>
	<div class="MainContent">
		<div class="ListBlock">
		    <h3 class="cBlack">修改岗位</h3>

			<!-- 增加组织 -->
			<form id="addForm" method="post" action="" >
			<input type="hidden" name="postId" id="postId" value="${postInfo.postId}"/>
			<input type="hidden" name="orgId" id="orgId" value="${postInfo.orgId}"/>
			<input type="hidden" name="tempUrl" id="tempUrl" value="${pageContext.request.contextPath}/api/org/getInfo"/>
			<div class="form-horizontal">
	        	<div class="control-group">
					<label class="control-label" for="personName">岗位名称</label>
					<div class="controls">
						<input type="text" value="${postInfo.postName }"  name="postName" class="span2" id="postName" placeholder="请输入岗位名称"/>
					</div>	
				</div>
				<div class="control-group">
					<label class="control-label" for="personEducational">岗位属性</label>
					<div class="controls">
						<select class="span2" name="postCode" id="postCode" >
						<c:forEach var="item" items="${postList}">
		              	<option value="${item.AUTHGROUPID}" <c:if test='${postInfo.postCode==item.AUTHGROUPID}'>selected="selected"</c:if>>${item.AUTHGROUPNAME}</option>
		                </c:forEach>
						</select>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="remark">岗位职责</label>
					<div class="controls">
						<textarea rows="6" cols="39" name="postDuties" id="postDuties">${postInfo.postDuties }</textarea>
					</div>
				</div>
				<div class="form-actions">
				  	<a class="btn btn-primary" href="#" name="submitter" onclick="checkForm();">保存</a>
			  		<a class="btn" href="#" onclick="goBack()">返回</a>
				</div>
			</div>
			</form>
	 </div>
 </div>

</body>
</html>

