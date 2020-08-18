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
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqBootstrapValidation.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script type="text/javascript">
		function saveTest(){
			document.myform.action="${pageContext.request.contextPath}/api/mechtreatment/addBusTestRecord";
			$('#myform').submit();
		}
		function back(){
			document.myform.action="${pageContext.request.contextPath}/api/mywork/getMyWork";
			document.myform.submit();
		}
		$(function () {$("input,select,textarea").not("[type=submit]").jqBootstrapValidation(); } );
		</script>
	</head>
	<body>
		<div class="container-fluid" id="testDiv">
			<div class="row-fluid">
		    	<div class="span10">
		    		<!-- 页面title start-->
		        	<div class="page-header">
		        		<h3>试车记录</h3>
		        	</div>
					<!-- 页面title end-->
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
		    		<!-- 基本信息 end -->	
		    		<!-- 试车参数 start -->
	    			<div class="page-header">
		        		<h4>试车参数</h4>
		        		<div class="row-fluid">
		        			<form class="form-horizontal" name="myform" id="myform" novalidate="">
		        			<input type="hidden" name="stepId" id="stepId" value="${stepId }"/>
						    <input type="hidden" name="nodeId" id="nodeId" value="${nodeId }"/>
						    <input type="hidden" name="instanceId" id="instanceId" value="${instanceId }"/>
						  	<input id="busId" name="busId" value="${mechTreatmentMap.id }" type="hidden"/>
						    <input id="url" name="url" type="hidden" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
		        			<div class="span5">
								
								<div class="control-group">
									<label class="control-label" for="oilRate">油耗率(g/KW.h)</label>
									<div class="controls">
								    	<input id="oilRate" name="oilRate" class="input-mini" type="text" type="number" data-validation-number-message="请输入数字！"  aria-invalid="true" maxlength="5"  />
									</div>
								</div>
								
		        			</div>
		        			<div class="span5">
		        				
								<div class="control-group">
									<label class="control-label" for="currentDrain">电耗</label>
									<div class="controls">
								    	<input id="currentDrain" name="currentDrain" class="input-mini" type="text" type="number" data-validation-number-message="请输入数字！"  aria-invalid="true" maxlength="5" />
									</div>
								</div>
								
		        			</div>
		        			</form>
		        		</div>
	        		</div>
		    		<!-- 试车参数 end -->
        			<div class="form-actions"><a class="btn" href="javascript:void(0)" onclick="back();">取　消</a>　<a class="btn btn-primary" href="javascript:void(0)" onclick="saveTest();" id="saveBtn" name="saveBtn">提交试车检验</a> </div>
	       		</div>
        	</div>	
  		</div>
	</div>
</html>
