<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>8.1上井劳务（收入）明细表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function goBack(){
		document.myform.action="${pageContext.request.contextPath}/api/report/toSearchlabourExDetail";
		document.myform.submit();
	}
	function makeExcel(){
		document.myform.action="${pageContext.request.contextPath}/api/report/importlabourExcel";
		document.myform.submit();
	}
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
  			<div class="span9">
      			<div class="page-header">
			        <h3>上井劳务（收入）明细表</h3>
     			</div>
     		</div>
  			<div class="span12"> 
   				<a href="javascript:void(0);" class="btn btn-info" onclick="goBack();">返 回</a> 
   				<a href="javascript:void(0);" class="btn btn-info" onclick="makeExcel();">生成EXCEL</a> 
  			</div>
    		<div class="span9">
  				<!-- 搜索框 end -->
  				<form name="myform" action="">
  					<input type="hidden" name="factory" id="factory" value="${factory }"/>
  					<input type="hidden" name="endDate" id="endDate" value="${endDate }"/>
  				</form>
			   	<div class="table">
		      		<table class="table table-striped table-bordered">
			        	<thead>
				           	<tr>
				           		<th rowspan="2">序号</th>
				           	 	<th rowspan="2">摘要</th>
				             	<th rowspan="2">单机号</th>
				             	<th rowspan="2">修理类别</th>
				             	<th rowspan="2">送修单位</th>
				             	<th rowspan="2">使用单位</th>
				             	<th rowspan="2">项目部</th>
				             	<th rowspan="2">井号</th>
				             	<th colspan="10">上井(金额)</th>
				             	<th colspan="11">钻修</th>
				           	</tr>
				           	<tr>
				           		<th>钻修</th>
				             	<th>机修</th>
				             	<th>电修</th>
				             	<th>机加</th>
				             	<th>铆焊</th>
				             	<th>前指一</th>
				             	<th>前指三</th>
				             	<th>前指四</th>
				             	<th>前指五</th>
				             	<th>小计</th>
				             	<th>钻修</th>
				             	<th>机修</th>
				             	<th>电修</th>
				             	<th>机加</th>
				             	<th>铆焊</th>
				             	<th>前指一</th>
				             	<th>前指三</th>
				             	<th>前指四</th>
				             	<th>前指五</th>
				             	<th>小计</th>
				             	<th>合计</th>
				           	</tr>
			         	</thead>  	
			         	<tbody>
			          		<c:if test="${empty labourExReportList}">
				          		<tr>
				                   <td colspan="41" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                </tr>   
			          		</c:if>
				          	<c:if test="${not empty labourExReportList}">
				          		<c:forEach items="${labourExReportList}" var="labour" varStatus="i">
				          			<tr>
				            			<td>${labour.rownum}</td>
				            			<td>${labour.remark}</td>
				            			<td>${labour.equipmentCode}</td>
				            			<td>${labour.repairType}</td>
				            			<td>${labour.repairFrom}</td>
				            			<td>${labour.useFrom}</td>
				            			<td>${labour.projectDept}</td>
				            			<td>${labour.expatsTo}</td>
				            			<td>${labour.zxValue}</td>
				            			<td>${labour.jxValue}</td>
				            			<td>${labour.dxValue}</td>
				            			<td>${labour.jjValue}</td>
				            			<td>${labour.mhValue}</td>
				            			<td>${labour.qz1Value}</td>
				            			<td>${labour.qz3Value}</td>
				            			<td>${labour.qz4Value}</td>
				            			<td>${labour.qz5Value}</td>
				            			<td>${labour.totalvalue}</td>
				            			<td>${labour.zxamount}</td>
				            			<td>${labour.jxamount}</td>
				            			<td>${labour.dxamount}</td>
				            			<td>${labour.jjamount}</td>
				            			<td>${labour.mhamount}</td>
				            			<td>${labour.qz1amount}</td>
				            			<td>${labour.qz3amount}</td>
				            			<td>${labour.qz4amount}</td>
				            			<td>${labour.qz5amount}</td>
				            			<td>${labour.totalamount}</td>
				            			<td>${labour.total}</td>
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
