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
	function toUpdate() {
		var material = document.getElementsByName("id");
		var checked = false;
		for (var i = 0; i < material.length; i++) {
			if (material[i].checked) {
				checked = true;
				document.getElementById("id").value=material[i].value;
			}
		}
		if (!checked) {
			alert("请选择物料！");
			return ;
		}
		document.form.action="${pageContext.request.contextPath}/api/material/toUpdateMaterial";
		document.form.submit();
	}

	function setValue(id) 
	{
		document.getElementById("id").value = id;
	}
	function doSearch(){
		document.form.action="${pageContext.request.contextPath}/api/chargeoff/getChargeoffDetailList";
		document.form.submit();
	}
	
	function toImport(){
		location.href="${pageContext.request.contextPath}/api/chargeoff/toImportChargeoffDetail";
	}
	
	function doDz(){
		if(document.getElementById("id").value==""){
			alert("请选择一条剩余材料进行手工对账！");
			return false;
		}
		document.form.action="${pageContext.request.contextPath}/api/chargeoff/getChargeoffDetail";
		document.form.submit();
	}
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
      			<div class="page-header">
			        <h3>材料出账明细</h3>
     			</div>
     			<!-- 搜索框 start -->
     			<div class="row-fluid">
	    			<form class="form-inline" name="form">
	    				<input type="hidden" name="id" id="id"/>
	    				<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/material/getList"/>
	    				<fieldset>
            				<legend><span class="label label-info">按条件搜索出账明细</span></legend>
				        	<div class="span4">
				          		<div class="control-group">
				            		<label class="control-label" for="materialDetail">物料描述</label>
			              			<input id="materialDetail" name="materialDetail" class="input-medium" type="text" value="${param.materialDetail}"/>
				          		</div>
				        	</div>
					        <div class="span4">
					        	<div class="control-group" style="">
					            	<label class="control-label" for="materialCode">物料编码</label>
					              	<input id="materialCode" class="input-medium" name="materialCode" type="text" value="${param.materialCode}" />
					          	</div>
					        </div>
				       		<div class="span10">
				       			<a class="btn btn-info" href="javascript:void(0)" onclick="doSearch();"><i class="icon-search icon-white"></i>&nbsp;搜 索</a>
				       			<a class="btn btn-primary" href="javascript:void(0)" onclick="doDz();"><i class="icon-plus icon-white"></i>&nbsp;手工对账</a>
				       		</div>
            			</fieldset>	
	    			</form>
	    		</div>
	    		<!-- 搜索框 end -->
   				<div class="table">
		      		<table class="table table-striped table-bordered">
			        	<thead>
		                	<tr> 
		                		<th>选择</th>
		                		<th>物料组</th>
		                        <th>物资编码</th>
		                        <th>物资描述</th>
		                        <th>计量单位</th>
		                        <th>数量</th>
		                        <th>单价</th>
		                        <th>金额</th>
		                        <th>发料日期</th>
		                        <th>成本中心描述</th> 
		                     </tr>
	                	</thead>
	                    <tbody>
	                    	<c:if test="${empty page.resultList}">
	                    		<tr>
		                            <td colspan="10" style="text-align: center;color:red;">暂无数据！</td>
		                         </tr>   
	                    	</c:if>
	                    	<c:if test="${not empty page.resultList}">
		                    	<c:forEach items="${page.resultList}" var="charge" varStatus="i">
			                        <tr>
			                        	<td><input type="radio" name="chargeId" id="" value="${charge.id}" onclick="setValue('${charge.id}')"/></td>
			                        	<td>${charge.materialGroup}</td>
			                            <td>${charge.materialCode}</td>
			                            <td>${charge.materialDetail}</td>
			                            <td>${charge.measureUnit}</td>
			                            <td>${charge.num}</td>
			                            <td>${charge.unitPrice}</td>
			                            <td>${charge.amount}</td>
			                            <td><tag:date value="${charge.receiptDate}"/></td>
			                            <td>${charge.accountDetail}</td>
			                        </tr>
		                        </c:forEach>
	                        </c:if>
	                    </tbody>
	                </table>
				   <div class="page">
						<%@include file="/jsp/public/standard.jsp"%>
					</div>
	    		</div>
     			<div class="form-actions">
	       			<a class="btn btn-primary" href="javascript:void(0)" onclick="toImport();">&nbsp;返&nbsp;回</a>
	       		</div>
	    	</div>
	   	</div>		
	 </div>  		
</body>
</html>
