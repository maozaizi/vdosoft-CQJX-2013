<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>月生产计划</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function toAddequipment(){
		var year = document.getElementById("year").value;
		var month = document.getElementById("month").value;
		var i= window.showModalDialog("${pageContext.request.contextPath}/api/produceplan/toChooseplan?year="+year+"&month="+month+"&tmp=" + Math.round(Math.random() * 10000),"","dialogWidth=1250,dialogHeight=700,scroll=yes");
		if (1==i){
			window.location.reload();
		}
	}
	function goBack(){
		document.myform.action="${pageContext.request.contextPath}/api/produceplan/toSearchPlan";
		document.myform.submit();
	}
	function delPlan(delId){
		document.getElementById("delId").value = delId;
		document.myform.action="${pageContext.request.contextPath}/api/produceplan/delPlan";
		document.myform.submit();
	}
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
      			<div class="page-header">
			        <h3>${year}年${month}月生产计划</h3>
     			</div>
     			<form name="myform" action="">
  					<input type="hidden" name="year" id="year" value="${year}"/>
  					<input type="hidden" name="month" id="month" value="${month}"/>
  					<input type="hidden" name="delId" id="delId"/>
  					<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/produceplan/toAddplan"/>
  				</form>
     			<a href="javascript:void(0);" class="btn btn-info" onclick="toAddequipment();">添加计划</a>
     			<a href="javascript:void(0);" class="btn btn-info" onclick="goBack();">返回</a>
			   	<div class="table">
		      		<table class="table table-striped table-bordered">
			        	<thead>
				           	<tr>
				           	 	<th>序号</th>
				             	<th>施工卡号</th>
				             	<th>设备名称</th>
				             	<th>设备型号</th>
				             	<th>单位</th>
				             	<th>数量</th>
				             	<th>修理性质</th>
				             	<th>产值</th>
				             	<th>送修单位</th>
				             	<th>操作</th>
				           	</tr>
			         	</thead>  	
			         	<tbody>
			          		<c:if test="${empty maplist}">
				          		<tr>
				                   <td colspan="10" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                </tr>   
			          		</c:if>
				          	<c:if test="${not empty maplist}">
				            	<c:forEach items="${maplist}" var="proplan" varStatus="i">
				                	<tr>
					                     <td>${i.index+1}</td> 
					                     <td>${proplan.workCard}</td>
					                     <td>${proplan.equipmentName}</td>
					                     <td>
					                     	${proplan.equipmentModel}
					                     </td>
					                     <td>${proplan.unity}</td>
					                     <td>
					                     	${proplan.numbers}
					                     </td>
					                     <td>${proplan.repairType}</td>
					                     <td>${proplan.equipmentValue}</td>
					                     <td>${proplan.teamNumber}</td>
					                     <td><a href="javascript:void(0);" class="btn btn-info" onclick="delPlan('${proplan.id}');">删除</a></td>
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
