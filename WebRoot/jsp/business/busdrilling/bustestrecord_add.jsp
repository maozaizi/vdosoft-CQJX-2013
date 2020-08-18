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
			document.myform.action="${pageContext.request.contextPath}/api/busdrilling/addBusTestRecord";
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
		                        <dd><a href="${pageContext.request.contextPath}/api/busdrilling/toBusDrillingDetailsPage?id=${busDrillingMap.Id}" target="_blank">${busDrillingMap.workCard}</a></dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>设备名称：</dt>
		                        <dd>${busDrillingMap.equipmentName}</dd>
		                    </dl>
		                   
		                    <dl class="dl-horizontal">
		                        <dt>修理类别：</dt>
		                        <dd>
		                        <c:if test="${busDrillingMap.repairType==1}">大修</c:if>
		                        <c:if test="${busDrillingMap.repairType==2}">中修</c:if>
		                        <c:if test="${busDrillingMap.repairType==3}">检修</c:if>
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>额定工时：</dt>
		                        <dd>${busDrillingMap.quotaHour}</dd>
		                    </dl>
		                     <dl class="dl-horizontal">
		                        <dt>机身编号：</dt>
		                        <dd>${busDrillingMap.bodyCode}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>送修单位：</dt>
		                        <dd>${web:getDeptBycode(busInrepairInfoMap.deptFrom)}井队</dd>
		                    </dl>
		                </div>
		                
		                <div class="span5">
		                	<dl class="dl-horizontal">
		                        <dt>车间：</dt>
		                        <dd>${groupName}</dd>
		                    </dl>
		                    
		                    <dl class="dl-horizontal">
		                        <dt>自编号：</dt>
		                        <dd>${busDrillingMap.selfCode}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理类型：</dt>
		                        <dd>
		                        <c:if test="${busDrillingMap.repairCategory==1}">抢修</c:if>
		                        <c:if test="${busDrillingMap.repairCategory==2}">正常修理</c:if>
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理性质：</dt>
		                        <dd>
		                        <c:if test="${busDrillingMap.repairProperties==1}">返厂修理</c:if>
		                        <c:if test="${busDrillingMap.repairProperties==2}">正常修理</c:if>
		                        <c:if test="${busDrillingMap.repairProperties==3}">返工修理</c:if>
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>产值：</dt>
		                        <dd>${busDrillingMap.repairValue}</dd>
		                    </dl>
		                </div>
		                <div class="row-fluid">
		                <div class="span9">
		                <dl class="dl-horizontal">
		                        <dt>存在问题：</dt>
		                        <dd>${busInrepairInfoMap.problem}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
								<dt>
									送修单位意见：
								</dt>
								<dd>
									${busInrepairInfoMap.opinionDeptFrom }
								</dd>
							</dl>
							<dl class="dl-horizontal">
								<dt>
									完整性及缺件情况：
								</dt>
								<dd>
									${busInrepairInfoMap.wholeSituation}
								</dd>
							</dl>
							
							
		                </div>
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
						  	<input id="busId" name="busId" value="${busDrillingMap.id }" type="hidden"/>
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
