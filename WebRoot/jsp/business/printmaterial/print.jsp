<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>材料领用申请</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqBootstrapValidation.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script type="text/javascript">
			function back(){
				document.myform.action="${pageContext.request.contextPath}/api/businrepairinfo/getInrepairListByState";
				document.myform.submit();
			}
		</script>
	</head>
	<body>
	<div class="container-fluid">
 		<div class="row-fluid">
   			<div class="span9">
   				<!-- 标题 start -->
     			<div class="page-header">
			        <h3>在修设备</h3>
     			</div>
     			<!-- 标题 end -->
     			<!-- 基本信息 start -->
     			<form name="myform" id="myform" action="">
				<input  type="hidden" name="busId" value="${busMap.id}"/>
				<input  type="hidden" name="busName" value="${tableName }"/>
				<input  type="hidden" name="workCard" value="${busMap.workCard}"/>
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
		                        <dd>${busMap.workCard}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>设备名称：</dt>
		                        <dd>${busInrepairInfoMap.equipmentName}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>机身编号：</dt>
		                        <dd>${busInrepairInfoMap.bodyCode}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理类别：</dt>
		                        <dd>
		                        <c:if test="${busInrepairInfoMap.repairType==1}">大修</c:if>
		                        <c:if test="${busInrepairInfoMap.repairType==2}">中修</c:if>
		                        <c:if test="${busInrepairInfoMap.repairType==3}">检修</c:if>
		                        </dd>
		                    </dl>
		                </div>
		                <div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>车间：</dt>
		                        <dd>${groupName}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>自编号：</dt>
		                        <dd>${busInrepairInfoMap.selfCode}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理类型：</dt>
		                        <dd>
		                        <c:if test="${busInrepairInfoMap.repairCategory==1}">抢修</c:if>
		                        <c:if test="${busInrepairInfoMap.repairCategory==2}">正常修理</c:if>
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理性质：</dt>
		                        <dd>
		                        <c:if test="${busInrepairInfoMap.repairProperties==1}">返厂修理</c:if>
		                        <c:if test="${busInrepairInfoMap.repairProperties==2}">正常修理</c:if>
		                        <c:if test="${busInrepairInfoMap.repairProperties==3}">返工修理</c:if>
		                        </dd>
		                    </dl>
		                </div>
					</div>
	                <div class="row-fluid">
		                <div class="span9">
	                		<dl class="dl-horizontal">
		                        <dt>存在问题：</dt>
		                        <dd>${busInrepairInfoMap.problem}</dd>
	                    	</dl>
		                </div>
	        		</div>
				</div>
				<!-- 基本信息 end -->
				<!-- 领用申请  start -->
				<div class="page-header">
					<h4>材料列表</h4>
					 <div class="row-fluid">
					 	<div style="margin:5px 0 5px 5px;">
					 	</div>
			            	<table class="table table-striped table-bordered" id="materialPlanTable">
		            			<tbody>
									<tr>
									  <th>物料描述</th>
						              <th>物料组</th>
						              <th>物料编码</th>
									  <th>单位</th>
									  <th>参考价（元）</th>
									  <th>数量</th>
									  <th>金额</th>
									  <th>备注</th>
									</tr>
									<c:if test="${empty materialList}">
			                            <tr>
			                            	<td colspan="10" style="text-align: center;color:red;">暂无数据！</td>
			                            </tr>   
			                        </c:if>
			                        
			                        <c:if test="${not empty materialList}">
			                            <c:forEach items="${materialList}" var="busMaterialPlan" varStatus="i">
			                                <tr id="materialPlanTr_${i.index+1}">
			                                    <td>${busMaterialPlan.material_detail}</td>
			                                    <td>${busMaterialPlan.material_group}</td>
			                                    <td>${busMaterialPlan.material_code}</td>
			                                    <td>${busMaterialPlan.unity}</td>
			                                    <td>${busMaterialPlan.estimate_price}</td>
			                                    <td>${busMaterialPlan.material_num}</td>
			                                    <td>${busMaterialPlan.total_price}</td>
			                                    <td>${busMaterialPlan.remark}</td>
			                                </tr>
			                            </c:forEach>
			                        </c:if>
			            	</table>
			            </div>	
			            <div id="errorMessage" style="display: none;"><font color="red">红色标识：必填项；黄色标识：请填写数字；</font></div>
	            	</div>
				<!-- 领用申请  end -->
				</form>
				<!-- 备注 end -->
				<!-- 操作按钮 start -->	    
    			<div class="form-actions">
			  		<a class="btn" href="javascript:void(0)" onclick="back();">取 消</a> 
				  	<a class="btn btn-primary" href="javascript:void(0);" onclick="">打 印</a>
			  	</div>
			</div>
		</div>
	</body>
</html>
