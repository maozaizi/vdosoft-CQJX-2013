<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>材料确认</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/RegExp.js" type="text/javascript"></script>
		<script type="text/javascript">
		function submit(type){
		        $("#affirmtype").val(type);
				document.form.action="${pageContext.request.contextPath}/api/mechtreatment/doMaterialAffirm";
				document.form.submit();
		}
		function goBack() {
			  window.location.href = "${pageContext.request.contextPath}/api/mywork/getMyWork";
		}	
</script>			
</head>
<body>
	<div class="container-fluid" id="divId">
 		<div class="row-fluid">
   			<div class="span9">
   				<!-- 标题 start -->
     			<div class="page-header">
			        <h3>材料确认</h3>
     			</div>
     			<!-- 标题 end -->
     			<!-- 基本信息 start -->
     			<div class="page-header">
		        	<h4>基本信息</h4>
		        	<div class="page-header">
		            <div class="row-fluid">
		            	<div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>厂区：</dt>
		                        <dd>${companyName}
		                        </dd>
		                    </dl>
		                   	<dl class="dl-horizontal">
								<dt>计划完工时间：</dt>
								<dd>
									<tag:date  value="${mechTreatmentMap.startDate}" />
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
		                	<div class="span10">
		                	<dl class="dl-horizontal">
		                        <dt>施工要求：</dt>
		                        <dd>
		                        	${machiningMap.dispatchingRemark}
		                        </dd>
		                    </dl>
		                  </div>
		                </div>
		        </div>
		        <div class="page-header">
		        	<h4>加工件信息</h4>
		        	<div class="row-fluid">
		            	<table class="table table-striped table-bordered">
		                	<thead>
		                    	<tr>
		                        	<th>加工/修理件名称</th>
		                            <th>数量</th>
		                            <th>送修车间</th>
		                        </tr>
		                    </thead>
		                	<tbody>
		                	   <c:forEach var="machining" items='${machiningList}' varStatus="status">
		                    	<tr>
		                        	<td>${machining.equipmentName}</td>
		                            <td>${machining.planNum}</td>
		                            <td>${machining.deptfromname}</td>
		                        </tr>
		                        </c:forEach>
		                    </tbody>
		                </table>
		            </div>
		        	
		        </div>
		        <!-- end -->
		        <div class="page-header">
					<h4>材料列表</h4>
            			<c:if test="${planLength<11}">
			        	<div class="row-fluid">
			        	</c:if>
			        	<c:if test="${planLength>10}">
			        	<div class="row-fluid" style="OVERFLOW-Y: auto; OVERFLOW-X:hidden; height:400px">
			        	</c:if>
						<table id="materialPlanTable" class="table table-striped table-bordered">
						  <tbody>
							<tr>
									<th>物料组</th>
									<th>物料编码</th>
									<th>物料描述</th>
									<th>参考价</th>
									<th>批准数量</th>
									<th>单位</th>
									<th>类别</th>
							</tr>
							<c:if test="${empty busPlanInfoMap}">
				           		<tr>
				                    <td colspan="8" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                 </tr>   
				           	</c:if>
				           	<c:forEach items="${busPlanInfoMap}" var="busPlanInfo" varStatus="i">
				           	<c:if test="${not empty web:getMaterialInfoList(busPlanInfo.id)}">
	                            <c:forEach items="${web:getMaterialInfoList(busPlanInfo.id)}" var="busMaterialPlan" varStatus="i">
	                                <tr>
	                                    <td>${busMaterialPlan.materialGroup}</td>
	                                    <td>${busMaterialPlan.materialCode}</td>
	                                    <td>${busMaterialPlan.materialDetail}</td>
	                                    <td>${busMaterialPlan.estimatePrice}</td>
	                                    <td>${busMaterialPlan.ratifyNum}</td>
	                                    <td>${busMaterialPlan.unity}</td>
	                                    <td><c:if test="${busMaterialPlan.type==1}">领用</c:if>
	                       					 <c:if test="${busMaterialPlan.type==2}">加工</c:if>
	                       					 <c:if test="${busMaterialPlan.type==3}">修理</c:if>
	                       					 <c:if test="${busMaterialPlan.type==4}">计划加领用</c:if>
		                       			 </td>
	                                </tr>
	                            </c:forEach>
				             </c:if>
				             </c:forEach>
						  </tbody>
						</table>
					</div>
				 </div>
				 <form name="form" method="post">
					<input type="hidden" name="busId" value="${mechTreatmentMap.id }"/>
					<input type="hidden" name="busName" value="com.MechTreatment"/>
					<input type="hidden" name="affirmtype" id="affirmtype" value=""/>
					<input type="hidden" name="instanceId" value="${instanceId }"/>
					<input type="hidden" name="nodeId" value="${nodeId }"/>
					<input type="hidden" name="stepId" value="${stepId }"/>
					<input type="hidden" name="url" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
				 </form>
				<div class="form-actions">
				  	<a class="btn btn-primary" href="javascript:void(0);" onclick="submit('1');">继续领料</a>
				  	<a class="btn" href="javascript:void(0);" onclick="goBack();">取消</a> 
				  	<a class="btn btn-primary" href="javascript:void(0);" onclick="submit('0');">试车</a>
				</div>	
	        </div>
	     </div>
     </div>

</body>
</html>
