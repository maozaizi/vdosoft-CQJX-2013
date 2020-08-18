<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>设备信息管理列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqBootstrapValidation.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script type="text/javascript">
	function addEqu(){
		document.myform.action="${pageContext.request.contextPath}/api/equipmentinfo/addEquipmentInfo";
		$('#myform').submit();
	}
	
	function back(){
		window.location.href = "${pageContext.request.contextPath}/api/equipmentinfo/getEquipmentInfoList";
	}
	$(function () {$("input,select,textarea").not("[type=submit]").jqBootstrapValidation(); } );
</script>
</head>
<body>
	<div class="container-fluid" id="eqInfo">
	  	<div class="row-fluid">
	  		<form action="" name="myform" class="form-horizontal" novalidate="" id="myform">
	  		<input type="hidden" name="parentId" id="parentId" value="${param.parentId}"/>
		    <div class="span10">
		        <div class="page-header">
		        	<h4>添加设备信息</h4>
		        	<div class="row-fluid">
			        	
						<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/equipmentinfo/getEquipmentInfoList"/>
				         <div class="span6">
			        		<div class="control-group">
					          <label class="control-label" for="equipmentName">设备名称</label>
					          <div class="controls">
					            <input id="equipmentName" name="equipmentName" class="input-medium" type="text"   maxlength="40"  required  data-validation-required-message="必填项" />
  							  </div>
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="unity">单位</label>
					          <div class="controls">
						          <input id="unity" name="unity" class="input-small" type="text" maxlength="10"/>
  							  </div>
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="unity">标准台</label>
					          <div class="controls">
						          <input id="standardNum" name="standardNum" class="input-small" type="text" maxlength="10"/>
  							  </div>
					        </div>
			        	</div>
			        	<div class="span5">
					        <div class="control-group">
					          <label class="control-label" for="equipmentCode">序号</label>
					          <div class="controls">
					            <input id="equipmentCode" name="equipmentCode" class="input-small" type="text" maxlength="40"  />
  							  </div>
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="value">原值</label>
					          <div class="controls">
					            <input id="value" name="value" class="input-small" type="number"/>
  							  </div>
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="personNum">人员定额</label>
					          <div class="controls">
					            <input id="personNum" name="personNum" class="input-small" type="text"/>
  							  </div>
					        </div>
			        	</div>
		        	</div>
		        </div>
		        <div class="page-header">
		        	<div class="row-fluid">
		        		<table class="table table-striped table-bordered">
		        			<thead>
				           		<tr> 
			                		<th>修理类别</th>
			                        <th>定额工时</th>
			                        <th>定额价格</th>
			                     </tr>
	                     	</thead>
	                     	<tbody>
	                     		<tr>
	                     			<td>大修</td>
	                     			<td><input id="quotaHour" name="quotaHour" class="input-small" type="number" required/></td>
	                     			<td><input id="majorCost" name="majorCost" class="input-small" type="number"/></td>
	                     		</tr>
	                     		<tr>
	                     			<td>中修</td>
	                     			<td><input id="mediumHour" name="mediumHour" class="input-small" type="number"/></td>
	                     			<td><input id="mediumCost" name="mediumCost" class="input-small" type="number"/></td>
	                     		</tr>
	                     		<tr>
	                     			<td>检修</td>
	                     			<td><input id="checkHour" name="checkHour" class="input-small" type="number"/></td>
	                     			<td><input id="checkCost" name="checkCost" class="input-small" type="number"/></td>
	                     		</tr>
	                     	</tbody>
		        		</table>
		        	</div>
		        </div>
	        	<div class="form-actions">
				  	<a class="btn" href="javascript:void(0);" onclick="back();">返　回</a>
				  	<a class="btn btn-primary" href="javascript:void(0);" onclick="addEqu();">保　存</a>
		  		</div>
		    </div>
		    </form>
	    </div>
    </div>    
</div>
</body>
</html>
