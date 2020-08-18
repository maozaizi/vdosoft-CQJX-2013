<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="keywords" content="列表" />
	<meta http-equiv="description" content="列表"/>
	<title>我的工作台</title>
	<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		function updateState(nodeId,bsuId){
			document.forms[0].action = "${pageContext.request.contextPath }/api/mywork/updateState?nodeId="+nodeId+"&bsuId="+bsuId;
			document.forms[0].submit();
		}
		function goExec(processId,instanceId,objId,nodeId,stepId,tableName,contTitle,url,processType,stepName){
		//document.getElementById("processId").value = processId;
		//document.getElementById("instanceId").value = instanceId;
		//document.getElementById("objId").value = objId;
		//document.getElementById("nodeId").value = nodeId;
		//document.getElementById("stepId").value = stepId;
		//document.getElementById("tableName").value = tableName;
		//document.getElementById("contTitle").value = contTitle;
		//document.getElementById("url").value = url;
		//document.getElementById("processType").value = processType;
		//document.getElementById("stepName").value = stepName;
		//	document.forms[0].action = "${pageContext.request.contextPath }/api/mywork/goExec";
		var toUrl = encodeURIComponent(url);
			document.forms[0].action = "${pageContext.request.contextPath }/api/mywork/goExec?temp=" + Math.round(Math.random() * 10000)+"&processId="+processId+"&instanceId="+instanceId+"&objId="+objId+"&nodeId="+nodeId+"&stepId="+stepId+"&tableName="+tableName+"&contTitle="+contTitle+"&url="+toUrl+"&processType="+processType+"&stepName="+stepName;
			document.forms[0].submit();
		}
		function start(){
			document.forms[0].action = "${pageContext.request.contextPath }/api/workflow/processStart";
			document.forms[0].submit();
		}
	</script>
</head>
  
  <body>
  <form id="myWorkForm" method="post" action="">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span9">
				<div class="page-header">
					<table class="table table-striped table-bordered">
					    <thead>
						    <tr>
						      <th colspan="2">待办工作</th>
						    </tr>
					    </thead>
				    <tbody>
				   
				    <c:forEach items="${web:getWorkFlow(sessionScope.userMap)}" var="a">
				    	<tr>
							<c:if test="${fn:length(a.contTitle)>=50}">
								<td colspan="3" >
									<ol><li><a href="#" title="${a.contTitle}流程的${a.stepName}步骤是否现在处理！" onclick='goExec("${a.processId}",${a.instanceId},"${a.objId}",${a.nodeId},${a.stepId},"${a.tableName}","${a.contTitle}","${a.url}",${a.processType},"${a.stepName}");'>${a.contTitle}流程的${a.stepName}步骤是否现在处理！</a></li></ol>
								</td>
							</c:if>
							<c:if test="${fn:length(a.contTitle)<50}">
								<td colspan="3">
									<a href="#" title="${a.contTitle}流程的${a.stepName}步骤是否现在处理！" onclick='goExec("${a.processId}",${a.instanceId},"${a.objId}",${a.nodeId},${a.stepId},"${a.tableName}","${a.contTitle}","${a.url}",${a.processType},"${a.stepName}");'>${a.contTitle}流程的${a.stepName}步骤是否现在处理！</a>
								</td>
							</c:if>
						</tr>
				    </c:forEach>
				    </tbody>
				  </table>
				</div>
				
			</div>
		</div>
	</div>
	</form>
	</body>
</html>
