<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>步骤设置</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<!-- Bootstrap -->
		<link
			href="${pageContext.request.contextPath }/css/bootstrap.min.css"
			rel="stylesheet" media="screen" />
		<script
			src="${pageContext.request.contextPath }/js/jquery.1.7.2.min.js"></script>
		<script
			src="${pageContext.request.contextPath }/js/bootstrap.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.js"
			type="text/javascript"></script>

	<script type="text/javascript">
		function save(){
			document.forms[0].action="${pageContext.request.contextPath}/api/workflow/updateProcessStep";
			document.forms[0].submit();
		}
		<%--返回 --%>
		function goBack() {
			window.location.href = "${pageContext.request.contextPath}/api/workflow/getProcessInfo?processId=${processStepMap.processId}";
		 }
		</script>
	</head>
	<body>
<div class="MainContent">
	<div class="ListBlock">
		<h3 class="cBlack">步骤设置</h3>
		<form id="addForm" method="post" action="" >
		<input type="hidden" name="processStepId" id="processStepId" value="${processStepMap.processStepId }"/>
		<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/workflow/getProcessInfo?processId=${processStepMap.processId }"/>
		
			<div class="form-horizontal">
					<div class="control-group">
						<label class="control-label" for="strname">
							步骤编号
						</label>
						<div class="controls">
						${processStepMap.stepId }
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="strname">
							步骤名称
						</label>
						<div class="controls">
						${processStepMap.stepName }
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" >
							步骤类型
						</label>
						<div class="controls">
						       <c:if test="${processStepMap.stepType ==0}">
			<input type="radio" name="stepType" id="stepType" value="0" checked="checked"/>普通
			</c:if>
			 <c:if test="${processStepMap.stepType !=0}">
			<input type="radio" name="stepType"  id="stepType" value="0" />普通
			</c:if>
			  <c:if test="${processStepMap.stepType ==1}">
			<input type="radio" name="stepType"  id="stepType" value="1" checked="checked"/>会签
			</c:if>
			 <c:if test="${processStepMap.stepType !=1}">
			<input type="radio" name="stepType"  id="stepType" value="1" />会签
			</c:if>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="editUrl">
							编辑路径
						</label>
						<div class="controls">
						 <input type="text" name="editUrl" id="editUrl" value="${processStepMap.editUrl}" class="ipt_text" size="25"/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="viewUrl">
							显示路径
						</label>
						<div class="controls">
						  <input type="text" name="viewUrl" id="viewUrl" value="${processStepMap.viewUrl}" class="ipt_text" size="25"/>
						</div>
					</div>
		
		<!-- 
		<div class="EditBox">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0"> 
		   <tr>
			  <th colspan="2">步骤设置</th>
		   </tr>
		   <tr>
	         <td class="EditBox_td1" width="100px">步骤编号：</td>
	         <td>${processStepMap.stepId }</td>
	       </tr>
	      <tr>
	         <td class="EditBox_td1">步骤名称：</td>
	         <td>${processStepMap.stepName }</td>
	       </tr>
	       <tr>
	         <td class="EditBox_td1"><span style="color: Red">*&nbsp;</span>步骤类型：</td>
	         <td>
	         <c:if test="${processStepMap.stepType ==0}">
			<input type="radio" name="stepType" value="0" checked="checked"/>普通
			</c:if>
			 <c:if test="${processStepMap.stepType !=0}">
			<input type="radio" name="stepType" value="0" />普通
			</c:if>
			  <c:if test="${processStepMap.stepType ==1}">
			<input type="radio" name="stepType" value="1" checked="checked"/>会签
			</c:if>
			 <c:if test="${processStepMap.stepType !=1}">
			<input type="radio" name="stepType" value="1" />会签
			</c:if>
	         </td>
	       </tr>
	        <tr>
	         <td class="EditBox_td1"><span style="color: Red">*&nbsp;</span>编辑路径：</td>
	         <td>
	          <input type="text" name="editUrl" value="${processStepMap.editUrl}" class="ipt_text" size="25"/>
	         </td>
	       </tr>
	        <tr>
	         <td class="EditBox_td1"><span style="color: Red">*&nbsp;</span>显示路径：</td>
	         <td>
	          <input type="text" name="viewUrl" value="${processStepMap.viewUrl}" class="ipt_text" size="25"/>
	         </td>
	       </tr>
		</table>
		</div> -->
		<!-- 操作按钮开始 -->
		<div class="form-actions">
			<a class="btn btn-primary" href="#" onclick="save();">保存</span></a>
			<a class="btn" href="#" onclick="goBack();">返回</a>   
		</div>	
		</div>	
		<!-- 操作按钮结束 -->
		</form>
	</div>
</div>
</body>	
</html>

