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
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script type="text/javascript">
	function updateEqu(){
		document.myform.action="${pageContext.request.contextPath}/api/equipmentinfo/updateEquipmentInfo";
		document.myform.submit();
	}
</script>
</head>
<body>
<form action="" name="myform">
<input type="hidden" name="id" id="id"  value="${equipmentInfoMap.id }"/>
<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/equipmentinfo/getEquipmentInfoList"/>
<!-- 设备信息 start -->
	<div class="container-fluid">
	   <div class="row-fluid">
		    <div class="span9">
		     	<div class="page-header">
		        	<h4>设备信息详情</h4>
		        </div>
		        <!-- 设备信息详情start -->
		        <div class="page-header">
		        	<div class="row-fluid">
		            	<div class="span5">
		            		 <dl class="dl-horizontal">
						         <dt>设备名称</dt>
						         <dd>${equipmentInfoMap.equipmentName }</dd>
			       			 </dl>
						       <dl class="dl-horizontal">
						         <dt>车号及设备编号</dt>
						         <dd>${equipmentInfoMap.equipmentCode }</dd>
						       </dl>
						       <dl class="dl-horizontal">
						         <dt>单位</dt>
						         <dd>${equipmentInfoMap.unity }</dd>
						       </dl>
						       <dl class="dl-horizontal">
						         <dt>极数</dt>
						         <dd>${equipmentInfoMap.polesNum }</dd>
						       </dl>
				       	 </div>
				       	 <div class="span5">
				       	 	 <dl class="dl-horizontal">
						         <dt>规格型号</dt>
						         <dd>${equipmentInfoMap.equipmentModel }</dd>
					         </dl>
					         <dl class="dl-horizontal">
						         <dt>机身号</dt>
						         <dd>${equipmentInfoMap.machineCode }</dd>
					         </dl>
					         <dl class="dl-horizontal">
						         <dt>原值</dt>
						         <dd>${equipmentInfoMap.value }&nbsp;元</dd>
					        </dl>
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
	                     			<td>${equipmentInfoMap.quotaHour }</td>
	                     			<td>${equipmentInfoMap.majorCost }</td>
	                     		</tr>
	                     		<tr>
	                     			<td>中修</td>
	                     			<td>${equipmentInfoMap.mediumHour }</td>
	                     			<td>${equipmentInfoMap.mediumCost }</td>
	                     		</tr>
	                     		<tr>
	                     			<td>检修</td>
	                     			<td>${equipmentInfoMap.checkHour }</td>
	                     			<td>${equipmentInfoMap.checkCost }</td>
	                     		</tr>
	                     	</tbody>
		        		</table>
		        	</div>
		        </div>
	       		<!-- 设备信息详情end -->
			    <div class="form-actions">
			  		<a class="btn" href="javascript:void(0);" onclick="location.href='javascript:history.go(-1);'">返　回</a>
			    </div>
        	</div>
    	</div>
</form>
</body>
</html>
