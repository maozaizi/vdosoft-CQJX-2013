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
<script type="text/javascript">
	function closePage(){
		window.close();
	}
	function savePlan(){
		var checks = document.getElementsByName("checkbox");
		var flag = 0;
		for(i=0;i<checks.length;i++){
			if(checks[i].checked){
				flag++;
			}
		}
		if(flag==0){
			alert("请至少选择一项");
			return;
		}
		
		document.myform.action="${pageContext.request.contextPath}/api/produceplan/savePlan";
		document.myform.submit();
	}
	
	$(document).ready(function (){
	           var flag = ${closeFlag};
				if (flag==1){
				  window.returnValue=1;
				  window.close();
				}
	        }
      	);
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
      			<div class="page-header">
			        <h3>待修、在修设备列表</h3>
     			</div>
     			<form name="myform" action="">
  					<input type="hidden" name="year" id="year" value="${year}"/>
  					<input type="hidden" name="month" id="month" value="${month}"/>
  				<a href="javascript:void(0);" class="btn btn-info" onclick="savePlan();">添加计划</a>
				<a href="javascript:void(0);" class="btn btn-info" onclick="closePage();">关闭</a>
			   	<div class="table">
		      		<table class="table table-striped table-bordered">
			        	<thead>
				           	<tr>
				           		<th>选择</th>
				           	 	<th>序号</th>
				             	<th>施工卡号</th>
				             	<th>设备名称</th>
				             	<th>设备型号</th>
				             	<th>单位</th>
				             	<th>数量</th>
				             	<th>修理性质</th>
				             	<th>产值</th>
				             	<th>送修单位</th>
				           	</tr>
			         	</thead>  	
			         	<tbody>
			          		<c:if test="${empty repairlist}">
				          		<tr>
				                   <td colspan="10" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                </tr>   
			          		</c:if>
				          	<c:if test="${not empty repairlist}">
				            	<c:forEach items="${repairlist}" var="repair" varStatus="i">
				                	<tr>
				                		 <td><input type="checkbox" name="checkbox" value="${repair.workCard}" ></input></td> 
					                     <td>${i.index+1}</td> 
					                     <td>${repair.workCard}</td>
					                     <td>${repair.equipmentName}</td>
					                     <td>
					                     	${repair.equipmentModel}
					                     </td>
					                     <td>${repair.unity}</td>
					                     <td>
					                     	${repair.numbers}
					                     </td>
					                     <td>${repair.repairType}</td>
					                     <td>${repair.equipmentValue}</td>
					                     <td>${repair.deptFrom}</td>
				                 	</tr>
				                </c:forEach>
			               </c:if>
			         	</tbody>
		      		</table>
			    </div>
			    </form>
			</div>
		</div>
	</div>	         	
</body>
</html>
