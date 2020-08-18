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

	function doDel() {
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
		if (confirm("您确定删除该物料信息吗？")) {
		document.form.action="${pageContext.request.contextPath}/api/material/delMaterialInfo";
		document.form.submit();
		}
	}
	function doSearch(){
		document.form.action="${pageContext.request.contextPath}/api/material/getList";
		document.form.submit();
	}
	function toAdd(){
		document.form.action="${pageContext.request.contextPath}/api/material/toAddMaterial";
		document.form.submit();
	}
	function toImport(){
		document.form.action="${pageContext.request.contextPath}/api/material/toImportMaterialInfo";
		document.form.submit();
	}
	
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
    			<!-- title start 
      			<ul class="breadcrumb">
			        <li>物料列表</li>
     			</ul>
     			-->
     			<div>
     			<h3>物料列表</h3>
     			</div>
     			<!-- title end -->
     			<!-- 搜索框 start -->
     			<div class="row-fluid">
	    			<form class="form-inline" name="form">
	    				<input type="hidden" name="id" id="id"/>
	    				<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/material/getList"/>
	    				<fieldset>
            				<legend><span class="label label-info">按条件搜索物料信息</span></legend>
				        	<div class="span4">
				          		<div class="control-group">
				            		<label class="control-label" for="materialDetail">物料描述</label>
			              			<input id="materialDetail" name="materialDetail" class="input-medium" type="text" value="${param.materialDetail}"/>
				          		</div>
				          		<div class="control-group">
				            		<label class="control-label" for="materialClass">物料大类</label>
			              			<input id="materialClass" name="materialClass" class="input-medium" type="text" value="${param.materialClass}" />
				          		</div>
					          	<div class="control-group">
						        	<label class="control-label" for="isFormal">是否正式</label>
						          	<select id="isFormal" name="isFormal" style="width: 110px;">
						            	<option value="">请选择</option>
						            	<option value="1" <c:if test="${param.isFormal == 1}">selected</c:if> >是</option>
						            	<option value="0" <c:if test="${param.isFormal == 0}">selected</c:if> >否</option>
									</select>	
					          	</div>
				        	</div>
					        <div class="span4">
					        	<div class="control-group" style="">
					            	<label class="control-label" for="materialCode">物料编码</label>
					              	<input id="materialCode" class="input-medium" name="materialCode" type="text" value="${param.materialCode}" />
					          	</div>
					          	<div class="control-group">
					            	<label class="control-label" for="materialGroup">&nbsp;&nbsp;&nbsp;物料组</label>
					              	<input id="materialGroup" name="materialGroup" class="input-medium" type="text" value="${param.materialGroup}" />
					         	 </div>
					        </div>
				       		<div class="span10">
				       			<a class="btn btn-info" href="javascript:void(0)" onclick="doSearch();"><i class="icon-search icon-white"></i>&nbsp;搜 索</a>
				       			<a class="btn btn-primary" href="javascript:void(0)" onclick="toAdd();"><i class="icon-plus icon-white"></i>&nbsp;添 加</a>
				       			<a class="btn btn-primary" href="javascript:void(0)" onclick="toUpdate();"><i class="icon-edit icon-white"></i>&nbsp;修 改</a> 
				       			<a class="btn btn-primary" href="javascript:void(0)" onclick="doDel();"><i class="icon-remove icon-white"></i>&nbsp;删 除</a>
				       			<a class="btn btn-primary" href="javascript:void(0)" onclick="toImport();"><i class="icon-th-list icon-white"></i>&nbsp;批量导入</a>
				       		</div>
            			</fieldset>	
	    			</form>
	    		</div>
	    		<!-- 搜索框 end -->
	    		<!-- 列表 start -->
	    		<div class="table">
			   		<h4>物料信息列表</h4>
		      		<table class="table table-striped table-bordered">	
	                	<thead>
		                	<tr> 
		                		<th width="5%">选择</th>
		                		<th width="5%">物料组</th>
		                        <th width="8%">物资编码</th>
		                        <th>物资描述</th>
		                        <th>计量单位</th>
		                        <th>参考单价(元)</th>
		                        <th>导入日期</th>
		                     </tr>
	                	</thead>
	                    <tbody>
	                    	<c:if test="${empty page.resultList}">
	                    		<tr>
		                            <td colspan="10" style="text-align: center;color:red;">暂无数据！</td>
		                         </tr>   
	                    	</c:if>
	                    	<c:if test="${not empty page.resultList}">
		                    	<c:forEach items="${page.resultList}" var="material" varStatus="i">
			                        <tr>
			                            <td><input type="radio" name="id" value="${material.id }"/></td>
			                            <td>${material.materialGroup}</td>
			                            <td>${material.materialCode}</td>
			                            <td>${material.materialDetail}</td>
			                            <td>${material.unity}</td>
			                            <td>${material.unitPrice}</td>
			                            <td><tag:date value="${material.createDate}"/></td>
			                        </tr>
		                        </c:forEach>
	                        </c:if>
	                    </tbody>
	                </table>
				   <div class="page">
						<%@include file="/jsp/public/standard.jsp"%>
					</div>
				</div>	
				<!-- 列表 end -->	
   			</div>
		</div>
	</div>	
</body>
</html>
