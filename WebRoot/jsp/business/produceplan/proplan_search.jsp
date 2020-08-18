<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>制定月生产计划</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function toAddPlan(month){
		document.getElementById("month").value = month;
		document.myform.action="${pageContext.request.contextPath}/api/produceplan/toAddplan";
		document.myform.submit();
	}
	function toViewPlan(month){
		document.getElementById("month").value = month;
		document.myform.action="${pageContext.request.contextPath}/api/produceplan/toViewplan";
		document.myform.submit();
	}
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
  			<div class="span9">
      			<div class="page-header">
			        <h3>${year }年月度生产计划</h3>
     			</div>
     		</div>
    		<div class="span9">
  				<!-- 搜索框 end -->
  				<form name="myform" action="">
  					<input type="hidden" name="year" id="year" value="${year}"/>
  					<input type="hidden" name="month" id="month"/>
  				</form>
			   	<div class="table">
		      		<table class="table table-striped table-bordered">
			        	<thead>
				           	<tr>
				           	 	<th>月份</th>
				             	<th>操作</th>
				           	</tr>
			         	</thead>  	
			         	<tbody>
			         		<tr>
			                     <td>一月份</td>
			                     <td>
			                     	<c:if test="${month==12}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(1);"><i class="icon-search icon-white" ></i>制定生产计划</a> 
			                     	</c:if>
			                     	<c:if test="${month!=12}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toViewPlan(1);"><i class="icon-search icon-white" ></i>生产计划</a> 
			                     	</c:if>
			                     </td>
		                 	</tr>
		                 	<tr>
			                     <td>二月份</td>
			                     <td>
			                     	<c:if test="${month==1}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(2);"><i class="icon-edit icon-white" ></i>制定生产计划</a> 
			                     	</c:if>
			                     	<c:if test="${month!=1}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(2);"><i class="icon-search icon-white" ></i>生产计划</a> 
			                     	</c:if>
			                     </td>
		                 	</tr>
		                 	<tr>
			                     <td>三月份</td>
			                     <td>
			                     	<c:if test="${month==2}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(3);"><i class="icon-search icon-white" ></i>制定生产计划</a> 
			                     	</c:if>
			                     	<c:if test="${month!=2}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(3);"><i class="icon-search icon-white" ></i>生产计划</a> 
			                     	</c:if>
			                     </td>
		                 	</tr>
		                 	<tr>
			                     <td>四月份</td>
			                     <td>
			                     	<c:if test="${month==3}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(4);"><i class="icon-search icon-white" ></i>制定生产计划</a> 
			                     	</c:if>
			                     	<c:if test="${month!=3}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(4);"><i class="icon-search icon-white" ></i>生产计划</a> 
			                     	</c:if>
			                     </td>
		                 	</tr>
		                 	<tr>
			                     <td>五月份</td>
			                     <td>
			                     	<c:if test="${month==4}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(5);"><i class="icon-search icon-white" ></i>制定生产计划</a> 
			                     	</c:if>
			                     	<c:if test="${month!=4}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(5);"><i class="icon-search icon-white" ></i>生产计划</a> 
			                     	</c:if>
			                     </td>
		                 	</tr>
		                 	<tr>
			                     <td>六月份</td>
			                     <td>
			                     	<c:if test="${month==5}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(6);"><i class="icon-search icon-white" ></i>制定生产计划</a> 
			                     	</c:if>
			                     	<c:if test="${month!=5}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(6);"><i class="icon-search icon-white" ></i>生产计划</a> 
			                     	</c:if>
			                     </td>
		                 	</tr>
		                 	<tr>
			                     <td>七月份</td>
			                     <td>
			                     	<c:if test="${month==6}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(7);"><i class="icon-search icon-white" ></i>制定生产计划</a> 
			                     	</c:if>
			                     	<c:if test="${month!=6}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(7);"><i class="icon-search icon-white" ></i>生产计划</a> 
			                     	</c:if>
			                     </td>
		                 	</tr>
		                 	<tr>
			                     <td>八月份</td>
			                     <td>
			                     	<c:if test="${month==7}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(8);"><i class="icon-search icon-white" ></i>制定生产计划</a> 
			                     	</c:if>
			                     	<c:if test="${month!=7}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(8);"><i class="icon-search icon-white" ></i>生产计划</a> 
			                     	</c:if>
			                     </td>
		                 	</tr>
		                 	<tr>
			                     <td>九月份</td>
			                     <td>
			                     	<c:if test="${month==8}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(9);"><i class="icon-search icon-white" ></i>制定生产计划</a> 
			                     	</c:if>
			                     	<c:if test="${month!=8}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(9);"><i class="icon-search icon-white" ></i>生产计划</a> 
			                     	</c:if>
			                     </td>
		                 	</tr>
		                 	<tr>
			                     <td>十月份</td>
			                     <td>
			                     	<c:if test="${month==9}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(10);"><i class="icon-search icon-white" ></i>制定生产计划</a> 
			                     	</c:if>
			                     	<c:if test="${month!=9}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(10);"><i class="icon-search icon-white" ></i>生产计划</a> 
			                     	</c:if>
			                     </td>
		                 	</tr>
		                 	<tr>
			                     <td>十一月份</td>
			                     <td>
			                     	<c:if test="${month==10}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(11);"><i class="icon-search icon-white" ></i>制定生产计划</a> 
			                     	</c:if>
			                     	<c:if test="${month!=10}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(11);"><i class="icon-search icon-white" ></i>生产计划</a> 
			                     	</c:if>
			                     </td>
		                 	</tr>
		                 	<tr>
			                     <td>十二月份</td>
			                     <td>
			                     	<c:if test="${month==11}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(12);"><i class="icon-search icon-white" ></i>制定生产计划</a> 
			                     	</c:if>
			                     	<c:if test="${month!=11}">
			                     		<a href="javascript:void(0);" class="btn btn-info" onclick="toAddPlan(12);"><i class="icon-search icon-white" ></i>生产计划</a> 
			                     	</c:if>
			                     </td>
		                 	</tr>
			         	</tbody>
		      		</table>
			    </div>
			</div>
		</div>
	</div>	         	
</body>
</html>
