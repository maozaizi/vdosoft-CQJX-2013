<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>上井记录列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function goBack(){
		document.myform.action="${pageContext.request.contextPath}/api/report/toSearchExpats";
		document.myform.submit();
	}
	
	function goOut(){
		document.myform.action="${pageContext.request.contextPath}/api/report/getExpatsReportOut";
		document.myform.submit();
	}
	
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
  			<div class="span9">
      			<div class="page-header">
			        <h3>上井服务明细表</h3>
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
				           	 	<th>序号</th>
				             	<th>用车单位</th>
				             	<th>车型</th>
				             	<th>车牌号</th>
				             	<th>承运单位</th>
				             	<th>规格型号</th>
				             	<th>修理内容</th>
				             	<th>修理性质</th>
				             	<th>完成情况</th>
				             	<th>车间</th>
				             	<th>项目部</th>
				             	<th>队号</th>
				             	<th>上井人数</th>
				             	<th>出发时间</th>
				             	<th>回厂时间</th>
				             	<th>备注</th>
				           	</tr>
			         	</thead>  	
			         	<tbody>
			          		<c:if test="${empty expatsReportList}">
				          		<tr>
				                   <td colspan="16" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                </tr>   
			          		</c:if>
				          	<c:if test="${not empty expatsReportList}">
				            	<c:forEach items="${expatsReportList}" var="expats" varStatus="i">
				                	<tr>
					                     <td>${i.index}</td> 
					                     <td>${expats.vehiclesUnit}</td>
					                     <td>${expats.vehiclesModel}</td>
					                     <td>${expats.vehiclesPlate}</td>
					                     <td>${expats.transUnit}</td>
					                     <td>${expats.equipmentName}</td>
					                     <td>${expats.equipmentModel}</td>
					                     <td>${expats.taskDetail}</td>
					                     <td>${expats.repairType}</td>
					                     <td>
					                     	<c:if test="${expats.isComplete==1}">完成</c:if>
					                     	<c:if test="${expats.isComplete!=1}">未完成</c:if>
					                     </td>
					                     <td>${expats.deptName}</td>
					                     <td>${expats.projectDept}</td>
					                     <td>${expats.expatsTo}</td>
					                     <td>${expats.expatsNum}</td>
					                     <td>${expats.departureTime}</td>
					                     <td>
					                     	${expats.backTime}
					                     </td>
					                     <td>${expats.remark}</td>
				                 	</tr>
				                </c:forEach>
			               </c:if>
			         	</tbody>
		      		</table>
		      		
			    </div>
			   <a href="javascript:void(0);" class="btn btn-info" onclick="goOut();">导 出</a> 
			</div>
		</div>
	</div>	         	
</body>
</html>
