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
		document.myform.action="${pageContext.request.contextPath}/api/busplanwf/saveMaterialConfirm";
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
		    	<form action="" name="myform" id="myform">
		        <div class="page-header">
		        	<h3>器材公司确认</h3>
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
		                </div>
		                
		                <div class="span5">
		                	<dl class="dl-horizontal">
		                        <dt>车间：</dt>
		                        <dd>${groupName}
		                        </dd>
		                    </dl>
		                </div>
		            </div> 
		            <div class="row-fluid">
		                <div class="span9">
		                	<dl class="dl-horizontal">
		                        <dt>申请备注：</dt>
		                        <dd>${busPlanInfoWfMap.planRemark}</dd>
		                    </dl>
		                </div>
		             </div>
		        </div>
		         <div class="page-header">
		        	<h4>材料申请</h4>
		        	<p class="text-right">总计:<input id="price" name="price" class="input-mini" type="text" value="0"  readonly="readonly" /></p>
		        	<c:if test="${fn:length(busMaterialPlanList)<11}">
		        	<div class="row-fluid">
		        	</c:if>
		        	<c:if test="${fn:length(busMaterialPlanList)>10}">
		        	<div class="row-fluid" style="OVERFLOW-Y: auto; OVERFLOW-X:hidden; height:400px">
		        	</c:if>
		            	<table class="table table-striped table-bordered" id="materialPlanTable">
		                	<thead>
		                    	<tr>
									<th width="8%">物料组</th>
									<th width="13%">物料编码</th>
									<th width="34%">物料描述</th>
									<th width="8%">参考价</th>
									<th width="6%">数量</th>
									<th width="6%">金额</th>
									<th width="13%">审批数量</th>
									<th width="6%">单位</th>
									<th width="6%">类别</th>
		                        </tr>
		                    </thead>
		                    <tbody>
		                        <c:if test="${empty busMaterialPlanWfList}">
		                            <tr>
		                            	<td colspan="8" style="text-align: center;"><font color='red'>暂无数据！</font></td>
		                            </tr>   
		                        </c:if>
		                        <c:if test="${not empty busMaterialPlanWfList}">
		                            <c:forEach items="${busMaterialPlanWfList}" var="busMaterialPlan" varStatus="i">
		                                <tr>
		                                    <td>${busMaterialPlan.materialGroup}</td>
		                                    <td>${busMaterialPlan.materialCode}</td>
		                                    <td>${busMaterialPlan.materialDetail}</td>
		                                    <td>${busMaterialPlan.estimatePrice}</td>
		                                    <td>${busMaterialPlan.materialNum}</td>
		                                    <td><input name="totalPrice" id="totalPrice" class="input-mini" type="text" value="${busMaterialPlan.totalPrice}" readonly/></td>
		                                    <input type="hidden" name="materialId" value="${busMaterialPlan.id}"/>
		                                     <input type="hidden" name="estimatePrice" value="${busMaterialPlan.estimatePrice}"/>
		                                    <td><input name="ratifyNum" class="input-mini" type="text" value="${busMaterialPlan.ratifyNum}" readonly="true" maxlength="5" onkeyup="value=value.replace(/[^\d\.]/g,'')"/></td>
		                                    <td>${busMaterialPlan.unity}</td>
		                                    <td><c:if test="${busMaterialPlan.type==1}">领用</c:if>
		                       					 <c:if test="${busMaterialPlan.type==2}">加工</c:if>
		                       					 <c:if test="${busMaterialPlan.type==3}">修理</c:if>
		                       					 <c:if test="${busMaterialPlan.type==4}">计划加领用</c:if>
		                       			    </td>
		                                </tr>
		                            </c:forEach>
		                        </c:if>
		                    </tbody>
		                </table>
		            </div>
				    <div class="page-header" style="border-bottom: 0px;">
							<input type="hidden" name="stepId" id="stepId" value="${stepId }"/>
							<input type="hidden" name="nodeId" id="nodeId" value="${nodeId }"/>
							<input type="hidden" name="instanceId" id="instanceId" value="${instanceId }"/>
							<input type="hidden" name="id" id="id" value="${busId}"/>
							<input id="url" name="url" type="hidden" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
			        </div>
					
				    <div class="form-actions">
						<p>
							<a href="javascript:void(0);" onclick="goBack();" class="btn">取  消</a>
							<a href="javaScript:void(0);" onclick="save();" class="btn btn-primary">确 认</a>
						</p>
					</div>
					</form>
			    </div>
		    </div>
		</div>       
</body>
</html>
