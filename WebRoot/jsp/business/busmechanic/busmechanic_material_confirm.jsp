<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>器材公司确认</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function save(){
		document.myform.action="${pageContext.request.contextPath}/api/busmechanic/saveMaterialConfirm";
		document.myform.submit();
	}
	function goBack(){
		document.myform.action="${pageContext.request.contextPath}/api/mywork/getMyWork";
		document.myform.submit();
	}
</script>
</head>
<body>
<!-- title start -->
	<div class="container-fluid">
	  	<div class="row-fluid">
		    <div class="span9">
		        <div class="page-header">
		        	<h3>器材公司确认</h3>
		        </div>
		        <div class="page-header">
		        	<h4>物料计划</h4>
		        	<div class="table">
			      		<table class="table table-striped table-bordered">
				      		 <tbody>
					           <tr>
									<th>物料组</th>
									<th>物料编码</th>
									<th>物料描述</th>
									<th>数量</th>
									<th>单位</th>
					           </tr>
		          			 <form action="" name="myform" id="myform">
						           <c:if test="${empty busMaterialPlanList}">
						            <tr>
						            	<th colspan="6" style="text-align: center;"><font color='red'>暂无数据！</font></th>
						            </tr>	
						           </c:if>
						           <c:if test="${not empty busMaterialPlanList}">
						           	<c:forEach items="${busMaterialPlanList}" var="material" varStatus="i">
						                <tr>
						                    <td>${material.materialGroup}</td>
						                    <td>${material.materialCode}</td>
						                    <td>${material.materialDetail}</td>
						                    <td>${material.ratifyNum}</td>
						                    <td>${material.unity}</td>
						                </tr>
						               </c:forEach>
					              </c:if>
					         </tbody>
					       </table>
				       </div>
			        </div>
				    <div class="page-header" style="border-bottom: 0px;">
							<input type="hidden" name="stepId" id="stepId" value="${stepId }"/>
							<input type="hidden" name="nodeId" id="nodeId" value="${nodeId }"/>
							<input type="hidden" name="instanceId" id="instanceId" value="${instanceId }"/>
							<input type="hidden" name="id" id="id" value="${busId}"/>
							<input id="url" name="url" type="hidden" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
			        </div>
					</form>
				    <div class="form-actions">
						<p>
							<a href="javascript:void(0);" onclick="goBack();" class="btn">取  消</a>
							<a href="javaScript:void(0);" onclick="save();" class="btn btn-primary">确 认</a>
						</p>
					</div>
			    </div>
		    </div>
		</div>
	</div>        
</body>
</html>
