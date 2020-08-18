<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>日报列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function goBack(){
		document.myform.action="${pageContext.request.contextPath}/api/report/toSearchDaily";
		document.myform.submit();
	}
	
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
  			<div class="span9">
      			<div class="page-header">
			        <h3>日报</h3>
     			</div>
     		</div>
  			<div class="span12"> 
   				<a href="javascript:void(0);" class="btn btn-info" onclick="goBack();">返 回</a> 
  			</div>
    		<div class="span9">
  				<!-- 搜索框 end -->
  				<form name="myform" action=""></form>
			   	<div class="table">
		      		<table class="table table-striped table-bordered">
			        	<thead>
				           	<tr>
				           	 	<th>施工卡号</th>
				             	<th>设备名称</th>
				             	<th>规格型号</th>
				             	<th>送修单位</th>
				             	<th>开工日期</th>
				             	<th>工作进度</th>
				             	<th>待料情况</th>
				           	</tr>
			         	</thead>  	
			         	<tbody>
			          		<c:if test="${empty dailyReportList}">
				          		<tr>
				                   <td colspan="16" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                </tr>   
			          		</c:if>
				          	<c:if test="${not empty dailyReportList}">
				            	<c:forEach items="${dailyReportList}" var="daily" varStatus="i">
				                	<tr>
					                     <td>${daily.workCard}</td>
					                     <td>${daily.equipmentName}</td>
					                     <td>${daily.equipmentModel}</td>
					                     <td>${daily.deptFrom}</td>
					                     <td>${daily.startDate}</td>
					                     <td>${daily.noteContent}</td>
					                     <td>${daily.materialQk}</td>
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
