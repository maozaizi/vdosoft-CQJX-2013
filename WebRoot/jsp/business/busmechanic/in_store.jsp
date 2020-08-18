<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>材料入库</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
			
<script type="text/javascript">
		function submit(){
			document.form.action="${pageContext.request.contextPath}/api/busmechanic/saveInStore";
			document.form.submit();
		}
		function goBack() {
			  window.location.href = "${pageContext.request.contextPath}/api/mywork/getMyWork";
		}
</script>		
</head>

<body>
	<div class="container-fluid">
  		<div class="row-fluid">
	    	<div class="span9">
		        <div class="page-header">
		        	<h3>物料入库</h3>
		        </div>
		        <div class="page-header">
		        	<h4>基本信息</h4>
		        	<div class="row-fluid">
		            	<div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>工卡号：</dt>
		                        <dd>
		                       		${busMechanicMap.workCard}
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>设备名称：</dt>
		                        <dd>${busMechanicMap.equipmentName}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>机身编号：</dt>
		                        <dd>${busMechanicMap.bodyCode}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理类别：</dt>
		                        <dd>
		                        <c:if test="${busMechanicMap.repairType==1}">大修</c:if>
		                        <c:if test="${busMechanicMap.repairType==2}">中修</c:if>
		                        <c:if test="${busMechanicMap.repairType==3}">检修</c:if>
		                        </dd>
		                    </dl>
		                </div>
		                <div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>自编号：</dt>
		                        <dd>${busMechanicMap.selfCode}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理类型：</dt>
		                        <dd>
		                        <c:if test="${busMechanicMap.repairCategory==1}">抢修</c:if>
		                        <c:if test="${busMechanicMap.repairCategory==2}">正常修理</c:if>
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理性质：</dt>
		                        <dd>
		                        <c:if test="${busMechanicMap.repairProperties==1}">返厂修理</c:if>
		                        <c:if test="${busMechanicMap.repairProperties==2}">正常修理</c:if>
		                        <c:if test="${busMechanicMap.repairProperties==3}">返工修理</c:if>
		                        </dd>
		                    </dl>
		                </div>
		        	</div>
		        	 <div class="row-fluid">
		                <div class="span9">
		                	<dl class="dl-horizontal">
		                        <dt>存在问题：</dt>
		                        <dd>${busMechanicMap.problem}</dd>
		                    </dl>
		                </div>
		             </div>
		        </div>
		        <form name="form" method="post">
					<input type="hidden" name="instanceId" value="${instanceId }"/>
					<input type="hidden" name="nodeId" value="${nodeId }"/>
					<input type="hidden" name="stepId" value="${stepId }"/>
					<input id="url" name="url" type="hidden" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
			        <div class="page-header">
			        	<h4>物料入库信息</h4>
			            <div class="row-fluid">
			            	<table class="table table-striped table-bordered">
			            		<thead>
				            		<tr>
						              <th>物料组</th>
						              <th>物资编码</th>
									  <th>配件描述</th>
									  <th>计划数量</th>
									  <th>参考价</th>
									  <th>单位</th>
									  <th>数量</th>
									  <th>单价</th>
									</tr>
			            		</thead>
			            		<tbody>
			            			<c:if test="${empty materialList}">
							           		<tr>
							                    <td colspan="8" style="text-align: center;"><font color='red'>暂无数据！</font></td>
							                 </tr>   
							           	</c:if>
							           	<c:if test="${not empty materialList}">
							            	<c:forEach items="${materialList}" var="material" varStatus="i">
							            		<tr>
							            			<td>
							            				${material.materialGroup }
							            			</td>
							            			<td>
							            				${material.materialCode }
							            			</td>
							            			<td>
							            				${material.materialDetail }
							            			</td>
							            			<td>
							            				${material.estimatePrice }
							            			</td>
							            			<td>
							            				${material.materialNum }
							            			</td>
							            			<td>
							            				${material.unity }
							            			</td>
							            			<td>
							            				<input name="" class="input-mini" type="text"/>
							            			</td>
							            			<td>
							            				<input name="" class="input-mini" type="text"/>
							            			</td>
							            		</tr>
							            	</c:forEach>
							            </c:if>
			            		</tbody>
			            	</table>
			            </div>
			        </div>
		        </form>
		        <div class="form-actions">
			        <a class="btn" href="#" onclick="back();">取消</a> 
	  				<a class="btn btn-primary" href="#" onclick="submit();">提交入库</a>
		        </div>
		     </div>
		</div>
	</div>
</body>
</html>
