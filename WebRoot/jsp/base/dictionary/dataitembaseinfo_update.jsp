<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>数据字典项</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" media="screen"/>
		<script src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/RegExp.js" type="text/javascript"></script>
		<script type="text/javascript">
		function save(){
			var divId = document.getElementById("divId"); 
        	 var result = checkText(divId);
		   		if(result != ""){
		   			alert(result);
		   			return false;
		   		}else{
       			document.forms[0].action="${pageContext.request.contextPath}/api/dictionary/modify";
				document.forms[0].submit();
        	}
        }
		<%--返回 --%>
		function goBack() {
		   	window.location.href = "${pageContext.request.contextPath}/api/dictionary/get";
		 }
		</script>
	</head>
	<body>
	<div class="row" id="divId">
		<div class="span2"></div>
		<div class="span10">
			<h3 class="cBlack">您当前的位置：${navigation}</h3>
			<form id="addForm" method="post" action="" >
				<input type="hidden" name="parentId" id="parentId" value="${param.parentId}"/>
				<input type="hidden" name="url" id="url" value = "${pageContext.request.contextPath}/api/dictionary/get"/>
				<input type="hidden" name="dataItemId" value="${dataItemBaseInfo.dataItemId}" />
				
				<div class="form-horizontal">
					<div class="control-group">
						<h3>基本信息修改</h3>
					</div>
					<div class="control-group">
						<label class="control-label" for="personName"><span style="color: Red">*&nbsp;</span>名称：</label>
						<div class="controls">
							<input type="text" class="span2" name="dataItemName" value="${dataItemBaseInfo.dataItemName}"  maxlength="100" check="1" placeholder="请填写名称"><span class="help-inline">请填写名称</span>
						</div>
					</div>
					<!-- 
					<div class="control-group">
						<label class="control-label" for="personName"><span style="color: Red">*&nbsp;</span>值：</label>
						<div class="controls">
							<input type="text" class="span2" name="dataItemValue" value="${dataItemBaseInfo.dataItemValue}" size="30"   maxlength="100" check="1"  placeholder="请填写值"><span class="help-inline">请填写值</span>
						</div>
					</div>
					 -->
					<div class="control-group">
						<label class="control-label" for="personName">编码：</label>
						<div class="controls">
							<input type="text" class="span2" name="dataItemCode" value="${dataItemBaseInfo.dataItemCode}" size="30" maxlength="40"><span ></span>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="personName">排序：</label>
						<div class="controls">
							<input type="text" class="span2" name="orderby" value="${dataItemBaseInfo.orderby}" size="30" maxlength="3"><span ></span>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="remark">描述：</label>
						<div class="controls">
							<textarea name="memo"  rows="3">${dataItemBaseInfo.memo}</textarea><span class="help-block">请输入描述</span>
						</div>
					</div>
			 		<div class="form-actions">
						<a onclick="save();" href="#" class="btn btn-primary">保存</a>
						<a href="javascript:history.go(-1);" class="btn">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
	</body>	
</html>

