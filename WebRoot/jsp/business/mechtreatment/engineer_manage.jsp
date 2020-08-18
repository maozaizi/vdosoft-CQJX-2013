<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>设备信息管理列表</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript">
			function saveComplete(state){
				document.getElementById("repairState").value=state;
				document.myform.action="${pageContext.request.contextPath}/api/mechtreatment/doEngineerManage";
				document.myform.submit();
			}
			function goBack() {
			 window.location.href = "${pageContext.request.contextPath}/api/businrepairinfo/getEngineerList";
			}
		</script>
	</head>
	<body>
	<div class="container-fluid">
  		<div class="row-fluid">
	    	<div class="span9">
		        <div class="page-header">
		        	<h3>出厂</h3>
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
		                        <dd><a href="${pageContext.request.contextPath}/api/mechtreatment/toMechTreatmentDetailsPage?id=${mechTreatmentMap.Id}" target="_blank">${mechTreatmentMap.workCard}</a>
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
		        <div class="page-header">
		        	<h4>检验结果</h4>
		        	<div class="row-fluid">
		            	<div class="span5">
		                    <dl class="dl-horizontal">
					          <dt>检验结果：</dt>
					          <dd>
					            <p class="span18">${mechTreatmentMap.opinionChecker}</p>
					          </dd>
					        </dl>
	                    </dl>
	                </div>
	                </div>	
		    </div>
	                <form action="" name="myform">
					  	<input type="hidden" name="stepId" id="stepId" value="${stepId }"/>
						<input type="hidden" name="nodeId" id="nodeId" value="${nodeId }"/>
						<input type="hidden" name="instanceId" id="instanceId" value="${instanceId }"/>
					  	<input name="busId" id="busId" value="${mechTreatmentMap.id }" type="hidden" />
					  	<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/businrepairinfo/getEngineerList"/>
					  	<input name="repairState" id="repairState" value="" type="hidden" />
					<div class="page-header">
			        	  <div class="row-fluid">
				        	<span class="span5">
				        		<div class="control-group">
				        		 <dl class="dl-horizontal">
				                        <dt>出厂地点：</dt>
				                        <dd><input id="factoryWhere" name="factoryWhere" value="${mechTreatmentMap.factoryWhere}" class="input-medium" type="text" maxlength="8"/></dd>
				                    </dl> 
						        </div>
				        	</span>
				        	<span class="span4">
				        		<div class="control-group">
						         	<dl class="dl-horizontal">
				                        <dt>出厂时间：</dt>
				                        <dd><input id="factoryDate" name="factoryDate" value="<tag:date  value='${mechTreatmentMap.factoryDate}' />" class="input-medium" type="text" required
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" /></dd>
				                    </dl> 
						        </div>
				        	</span>
			        	  </div>
		        	 <div>
					</form>
            <div class="form-actions">
			  	<a class="btn" href="javascript:void(0)" onclick="saveComplete(3);">到装备库</a>
			  	<a class="btn" href="javascript:void(0)" onclick="goBack();">取消</a> 
			  	<a class="btn btn-primary" href="javascript:void(0)" onclick="saveComplete(4);">出厂</a>
			</div>    
	    </div>
    </div>    
</div>
</html>