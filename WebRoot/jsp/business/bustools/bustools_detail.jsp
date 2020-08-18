<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>劳保工具申请详情</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript">
		function openeq(id1,id2){
			window.open("${pageContext.request.contextPath}/api/logRecords/getLogRecordsList?id1="+id1+"&name1=com.BusMechanic&id2="+id2+"&name2=com.BusInrepairInfo");
	    }
		
       </script>
	</head>
	<body>
	<div class="container-fluid">
	   <div class="row-fluid">
		    <div class="span9">
				<div class="page-header">
		        	<h3>机修详情信息</h3>
		        </div>
		        
		         <div class="page-header">
		        	<h4>基本信息</h4>
		            <div class="row-fluid">
		            	<div class="span5">
		            		 <dl class="dl-horizontal">
		                        <dt>厂区：</dt>
		                        <dd>${companyName}
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>工卡号：</dt>
		                        <dd>${busToolswfMap.workCard}
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>设备名称：</dt>
		                        <dd>${busToolswfMap.equipmentName}</dd>
		                    </dl>
		                </div>
		                
		                <div class="span5">
		                	<dl class="dl-horizontal">
		                        <dt>车间：</dt>
		                        <dd>${groupName}</dd>
		                    </dl>
		                   
		                </div>
		            </div> 
		        </div>
	            <c:if test="${not empty busPlanInfoMap}">
			            <div class="page-header">
				        	<h4 style="cursor: pointer;">材料申请 ${busPlanInfoMap.plancode}</h4>
				            <div class="row-fluid">
				            	<table class="table table-striped table-bordered">
				                	<thead>
				                    	<tr>
											<th>物料组</th>
											<th>物料编码</th>
											<th>物料描述</th>
											<th>参考价</th>
											<th>数量</th>
											<th>单位</th>
				                        </tr>
				                    </thead>
				                    <tbody>
			                            <c:forEach items="${materialPlanList}" var="busMaterialPlan" varStatus="i">
			                                <tr>
			                                    <td>${busMaterialPlan.materialGroup}</td>
			                                    <td>${busMaterialPlan.materialCode}</td>
			                                    <td>${busMaterialPlan.materialDetail}</td>
			                                    <td>${busMaterialPlan.estimatePrice}</td>
			                                    <td>${busMaterialPlan.materialNum}</td>
			                                    <td>${busMaterialPlan.unity}</td>
			                                </tr>
			                            </c:forEach>
				                    </tbody>
				                </table>
				            </div>
				        </div>
		        </c:if>
	            <c:if test="${not empty outList}">
			            <div class="page-header">
			            	<h4 style="cursor: pointer;">出库明细</h4>
				            <div class="row-fluid">
				            	<table class="table table-striped table-bordered">
				                	<thead>
				                    	<tr>
											<th>物料组</th>
											<th>物料编码</th>
											<th>物料描述</th>
											<th>实际价格</th>
											<th>数量</th>
											<th>单位</th>
				                        </tr>
				                    </thead>
				                    <tbody>
			                            <c:forEach items="${outList}" var="outMaterial" varStatus="i">
			                                <tr>
			                                    <td>${outMaterial.materialGroup}</td>
			                                    <td>${outMaterial.materialCode}</td>
			                                    <td>${outMaterial.materialDetail}</td>
			                                    <td>${outMaterial.unitPrice}</td>
			                                    <td>${outMaterial.materialNum}</td>
			                                    <td>${outMaterial.unity}</td>
			                                </tr>
			                            </c:forEach>
				                    </tbody>
				                </table>
				            </div>
				        </div>
		        </c:if>
	           
	           <div class="page-header">
				<h4>操作历史记录</h4>
		  		<div class="table">
				  <table class="table table-striped table-bordered">
			          	<tbody>
				           	<c:if test="${empty logRecordsList}">
				           		<tr>
				                    <td colspan="6" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                 </tr>   
				           	</c:if>
				           	<c:if test="${not empty logRecordsList}">
			            	<c:forEach items="${logRecordsList}" var="logRecords" varStatus="i">
			                 <tr>
			                     <td> ${logRecords.remark}</td>
			                 </tr>
			                </c:forEach>
			               </c:if>
					 	</tbody>
			        </table>
				</div>
				</div>
	       
			</div>
		</div>
	</div>
	</body>
</html>
