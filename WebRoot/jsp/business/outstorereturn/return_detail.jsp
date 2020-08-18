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
		function back(){
			document.myform.action="${pageContext.request.contextPath}/api/storeDetail/getOutStoreReturnList";
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
			        <h3>退料明细</h3>
     			</div>
     			<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/storeDetail/getOutStoreReturnList">
     			<!-- 标题 end -->
     			<!-- 基本信息 start -->
     			<form name="myform" id="myform" action="">
				<div class="page-header">
					 <div class="row-fluid">
			            	<table class="table table-striped table-bordered" id="outStoreTable4">
		            			<tbody>
									<tr>
									  <th>物料描述</th>
						              <th>物料组</th>
						              <th>物料编码</th>
									  <th>单位</th>
									  <th>单价</th>
									  <th>数量</th>
									  <th>总金额（元）</th>
									</tr>
									<c:forEach items="${outStoreDetailList}" var="out" varStatus="i">
		                            <tr>
                            			<td>${out.materialDetail }</td>
							            <td>${out.materialGroup }</td>
							            <td>${out.materialCode }</td>
										<td>${out.unity }</td>
										<td>${out.unitPrice }</td>
										<td>${out.materialNum }</td>
										<td>${out.amount }</td>
		                            </tr> 
		                            </c:forEach>  
			            	</table>
			            </div>
	            	</div>
				</form>
				<!-- 备注 end -->
				<!-- 操作按钮 start -->	    
    			<div class="form-actions">
			  		<a class="btn" href="javascript:void(0)" onclick="back();">返 回</a> 
			  	</div>
				<!-- 操作按钮 end -->
			</div>
		</div>
	</div>
	</body>
</html>
