<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>物料出库</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/RegExp.js" type="text/javascript"></script>
<script type="text/javascript">
		function save(){
			document.form.action="${pageContext.request.contextPath}/api/mechtreatment/inStore";
			document.form.submit();
		}
		//删除行
		function goBack() {
			  window.location.href = "${pageContext.request.contextPath}/api/mywork/getMyWork";
		}	
		function openDept(){
			window.open("${pageContext.request.contextPath}/api/chargeoff/toChooseDept?tmp=" + Math.round(Math.random() * 10000),"newwindow", "height=500, width=400, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
		}
		function showDiv(){
			if(document.getElementById("dept").style.display==""){
				document.getElementById("dept").style.display="none";
			}else{
				document.getElementById("dept").style.display="";
			}
		}
</script>			
			
	
</head>

<body>
	<input type="hidden" name="useType" id="useType" value=""/>
	<form name="form" method="post">
	<input type="hidden" name="busId" value="${mechTreatmentMap.id }"/>
	<input type="hidden" name="busName" value="com.MechTreatment"/>
	<input type="hidden" name="workCard" value="${mechTreatmentMap.workCard}"/>
	<input type="hidden" name="instanceId" value="${instanceId }"/>
	<input type="hidden" name="nodeId" value="${nodeId }"/>
	<input type="hidden" name="stepId" value="${stepId }"/>
	<input type="hidden" name="url" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
	<div class="container-fluid" id="divId">
 		<div class="row-fluid">
   			<div class="span9">
   				<!-- 标题 start -->
     			<div class="page-header">
			        <h3>加工件确认</h3>
     			</div>
     			<!-- 标题 end -->
     			<!-- 基本信息 start -->
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
		                        <dd><a href="${pageContext.request.contextPath}/api/mechtreatment/toMechTreatmentDetailsPage?id=${machiningMap.Id}" target="_blank">${mechTreatmentMap.workCard}</a>
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>名称：</dt>
		                        <dd>${machiningMap.equipmentName}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>规格型号：</dt>
		                        <dd>${machiningMap.equipmentModel}</dd>
		                    </dl>
							<dl class="dl-horizontal">
		                        <dt>计划数量：</dt>
		                        <dd>${machiningMap.planNum}</dd>
		                    </dl>
		                </div>
		                <div class="span5">
		                	<dl class="dl-horizontal">
		                        <dt>车间：</dt>
		                        <dd>${groupName}
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>送修单位：</dt>
		                        <dd>${machiningMap.userName}</dd>
		                    </dl>
		                     <dl class="dl-horizontal">
								<dt>图号：</dt>
								<dd>
									${machiningMap.figureNum}
								</dd>
							</dl>
							<dl class="dl-horizontal">
								<dt>
									修制类别：
								</dt>
								<dd>
									${machiningMap.makeSort }
								</dd>
							</dl>
		                    <dl class="dl-horizontal">
								<dt>计划完工时间：</dt>
								<dd><tag:date  value="${mechTreatmentMap.planEndTime}" /></dd>
							</dl>
		                </div>
					</div>
	                <div class="row-fluid">
		                <div class="span9">
	                		<dl class="dl-horizontal">
		                        <dt>施工要求：</dt>
		                        <dd>${machiningMap.problem}</dd>
		                    </dl>
		                </div>
	        		</div>
				</div>
		        <!-- end -->
		        <div class="page-header">
		        	<h4>材料员确认</h4>
		        	<div class="row-fluid">
		            	<div class="span5">
							<dl class="dl-horizontal">
		                        <dt>加工数量：</dt>
		                        <dd>${machiningMap.makeNum}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>领用数量：</dt>
		                        <dd>${machiningMap.outNum}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
								<dt>发其他车间：</dt>
								<dd><input type="checkbox" name="otherDept" id="otherDept" onclick="showDiv();"/></dd>
							</dl>
							<div  id="dept" style="display:none;">
								<dl class="dl-horizontal">
									<dt>选择车间：</dt>
									<dd>
										<input id="deptName" name="deptName" class="input-medium"  onclick="openDept();" type="text" readonly="readonly"/>
								        <input id="deptId" name="deptId" class="input-medium" type="hidden" />
								    </dd>
								</dl>
								<dl class="dl-horizontal">
									<dt>数量：</dt>
									<dd>
										<input id="otherNum" name="otherNum" class="input-medium" type="text"/>
								    </dd>
								</dl>
							</div>
		            	</div>
		            </div>	
		        </div>	
				<div class="form-actions">
				  	<a class="btn" href="javascript:void(0);" onclick="goBack();">取消</a> 
				  	<button class="btn btn-primary" href="javascript:void(0);" id="inOther" onclick="save();">确认</button>
				</div>	
	        </div>
	     </div>
     </div>
</form>
</body>
</html>
