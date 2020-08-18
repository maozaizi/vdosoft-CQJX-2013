<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>材料信息管理列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script type="text/javascript">
	function doSearch(){
		document.form.action="${pageContext.request.contextPath}/api/storedetail/toMaterialInfoList";
		document.form.submit();
	}
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
     			<!-- 搜索框开始 start -->
				<div class="row-fluid">
	    			<form class="form-inline" name="form">
			    	<input type="hidden" name="id" id="id"/>
			    	<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/material/getList"/>
    	 				<!-- 搜索框开始 start -->
    	 				<fieldset>
            				<legend><span class="label label-info">按条件搜索物料信息</span></legend>
				        	<div class="span5">
				          		<div class="control-group">
				            		<label class="control-label" for="materialDetail">材料描述</label>
			              			<input id="materialDetail" name="materialDetail" class="input-medium" type="text" />
				          		</div>
				          		<div class="control-group">
				            		<label class="control-label" for="materialClass">材料大类</label>
			              			<input id="materialClass" name="materialClass" class="input-medium" type="text" />
				          		</div>
				        	</div>
					        <div class="span5">
					        	<div class="control-group" style="">
					            	<label class="control-label" for="materialCode">材料编码</label>
					                <input id="materialCode" class="input-medium" name="materialCode" type="text" />
					          	</div>
					          	<div class="control-group">
					            	<label class="control-label" for="materialGroup">&nbsp;&nbsp;&nbsp;物料组</label>
						            <input id="materialGroup" name="materialGroup" class="input-medium" type="text" />
						            <a class="btn btn-info" href="javascript:void(0)" onclick="doSearch();"><i class="icon-search icon-white"></i>&nbsp;搜 索</a>
					         	 </div>
					        </div>
				        </fieldset>
				        <!-- 搜索框 结束 end-->
		        	</form>
		       </div>
		       <div class="table">
		      		<table class="table table-striped table-bordered"> 
	                	<thead>
		                	<tr> 
		                        <th>物料描述</th>
		                        <th>物料组</th>
		                        <th>物料编码</th>
		                        <th>单位</th>
		                        <th>参考价（元）</th>
		                     </tr>
	                	</thead>
	                    <tbody>
	                    	<c:if test="${empty page.resultList}">
	                    		<tr>
		                            <td colspan="4" style="text-align: center;color:red;">暂无数据！</td>
		                         </tr>   
	                    	</c:if>
	                    	<c:if test="${not empty page.resultList}">
		                    	<c:forEach items="${page.resultList}" var="material" varStatus="i">
		                    	<c:set var="detail" value="${fn:replace(material.materialDetail , '\"','@')}"/>
			                        <tr onclick="parent.selectMaterial('${material.materialClass}','${material.materialGroup}','${material.materialCode}','${detail}','${material.unity}','${material.unitPrice }')">
			                            <td>${material.materialDetail}</td>
			                            <td>${material.materialGroup}</td>
			                            <td>${material.materialCode}</td>
			                            <td>${material.unity}</td>
			                            <td>${material.unitPrice}</td>
			                        </tr>
		                        </c:forEach>
	                        </c:if>
	                    </tbody>
	                </table>
				   <div class="page">
						<%@include file="/jsp/public/standard.jsp"%>
				   </div>
			   </div>
     		</div>
     	</div>
<!-- 列表结束 end -->
</body>
</html>
