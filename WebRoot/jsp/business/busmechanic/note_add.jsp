<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>生产日报</title>
		<base target="_self"/>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqBootstrapValidation.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				var flag = ${flag};
					if (flag==1){
					  alert("保存成功！");
					  window.close();
					}
				});
				
			function saveNote(){
				$("#myform").submit();
			}
		</script>
	</head>
	<body>
		<div class="container-fluid" id="testDiv">
			<div class="row-fluid">
		    	<div class="span12">
		    		<!-- 页面title start-->
		        	<div class="page-header">
		        		<h3>生产日报</h3>
		        	</div>
	    			<div class="page-header">
		        		<div class="row-fluid">
		        			<form class="form-horizontal" name="myform" id="myform" action="${pageContext.request.contextPath}/api/busmechanic/addNote">
						  	<input id="busId" name="busId" value="${busMechanicMap.id }" type="hidden"/>
						  	<input id="id" name="id" value="${noteMap.id }" type="hidden"/>
						  	<input id="flag" name="flag" value="${flag }" type="hidden"/>
		        			<div class="span5">
								<div class="control-group">
									<label class="control-label" for="rotationalSpeed">施工卡号:</label>
								  	<div class="controls">
								  		${busMechanicMap.workCard }
								  		<input id="workCard" name="workCard" value="${busMechanicMap.workCard }" type="hidden"/>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="electricity">设备名称:</label>
									<div class="controls">
										${busMechanicMap.equipmentName }
										<input id="equipmentName" name="equipmentName" value="${busMechanicMap.equipmentName }" type="hidden"/>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="bearingTemp ">设备规格:</label>
									<div class="controls">
										${busMechanicMap.equipmentModel }
										<input id="equipmentModel" name="equipmentModel" value="${busMechanicMap.equipmentModel }" type="hidden"/>
									</div>
								</div>
		        			</div>
		        			<div class="span6">
		        				<div class="control-group">
									<label class="control-label" for="oilRate">送修单位:</label>
									<div class="controls">
								    	${busMechanicMap.teamNumber }
								    	<input id="deptFrom" name="deptFrom" value="${busMechanicMap.teamNumber }" type="hidden"/>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="voltage">开工日期:</label>
								    <div class="controls">
								    	<tag:date value="${busMechanicMap.startDate }"/>
								    	<input id="startDate" name="startDate" value="<tag:date value='${busMechanicMap.startDate }'/>" type="hidden"/>
									</div>
								</div>
		        			</div>
		        			<div class="span10">
		        				<div class="control-group">
									<label class="control-label" for="oilRate">工作进度:</label>
									<div class="controls">
								    	<textarea rows="2" class="span8" name="noteContent" id="noteContent">${noteMap.noteContent }</textarea>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="oilRate">待料情况:</label>
									<div class="controls">
								    	<textarea rows="2" class="span8" name="materialQk" id="materialQk">${noteMap.materialQk }</textarea>
									</div>
								</div>
		        			</div>
		        			</form>
		        		</div>
	        		</div>
		    		<!-- 试车参数 end -->
        			<div class="form-actions"><a class="btn" href="javascript:void(0)" onclick="window.close();">关 闭</a>　<a class="btn btn-primary" href="javascript:void(0)" onclick="saveNote();" id="saveBtn" name="saveBtn">保存日报</a> </div>
	       		</div>
        	</div>	
  		</div>
</html>
