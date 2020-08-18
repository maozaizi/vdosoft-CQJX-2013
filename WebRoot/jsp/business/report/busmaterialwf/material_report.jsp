<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>机修公司材料计划列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function goBack(){
		document.myform.action="${pageContext.request.contextPath}/api/report/toSearchMaterialWf";
		document.myform.submit();
	}
	function goOut(){
		document.myform.action="${pageContext.request.contextPath}/api/report/importMaterialWfReport";
		document.myform.submit();
	}
	
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
  			<div class="span9">
      			<div class="page-header">
			        <h3>机修公司材料计划列表（<c:if test="${factory==1}">银川厂</c:if><c:if test="${factory==2}">庆阳厂</c:if>${searchDate }报表）</h3>
     			</div>
     		</div>
  			<div class="span12"> 
   				<a href="javascript:void(0);" class="btn btn-info" onclick="goBack();">返 回</a> 
   				 <a href="javascript:void(0);" class="btn btn-info" onclick="goOut();">导 出</a> 
  			</div>
    		<div class="span9">
  				<!-- 搜索框 end -->
  				<form name="myform" action="">
  					<input type="hidden" name="searchDate" id="searchDate" value="${searchDate}" />
  					<input type="hidden" name="factory" id="factory" value="${factory}" />
  				</form>
			   	<div class="table">
		      		<table class="table table-striped table-bordered">
			        	<thead>
				           	<tr>
				           	 	<th>厂区</th>
				             	<th>车间</th>
				             	<th>物资编码</th>
				             	<th>规格名称及型号</th>
				             	<th>单位</th>
				             	<th>单价</th>
				             	<th>申请数量</th>
				             	<th>领用数量</th>
				             	<th>金额</th>
				             	<th>备注</th>
				           	</tr>
			         	</thead>  	
			         	<tbody>
			          		<c:if test="${empty materialWfReportList}">
				          		<tr>
				                   <td colspan="16" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                </tr>   
			          		</c:if>
				          	<c:if test="${not empty materialWfReportList}">
				            	<c:forEach items="${materialWfReportList}" var="wf" varStatus="i">
				                	<tr>
					                     <td>${wf.parentOrgName}</td>
					                     <td>${wf.orgName}</td>
					                     <td>${wf.materialCode}</td>
					                     <td>${wf.materialDetail}</td>
					                     <td>${wf.unity}</td>
					                     <td>${wf.estimatePrice}</td>
					                     <td>${wf.materialNum}</td>
					                     <td>${wf.realNum}</td>
					                     <td>${wf.totalPrice}</td>
					                     <td>${wf.remark}</td>
				                 	</tr>
				                </c:forEach>
			               </c:if>
			         	</tbody>
		      		</table>
			    </div>
			</div>
		</div>
	</div>	         	
</body>
</html>
