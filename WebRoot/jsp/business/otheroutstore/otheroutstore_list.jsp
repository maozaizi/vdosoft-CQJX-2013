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
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function doSearch(){
		document.form.action="${pageContext.request.contextPath}/api/otherout/getOtherOutStoreList";
		document.form.submit();
	}
	function toAdd(){
		document.form.action="${pageContext.request.contextPath}/api/otherout/toAddOtherOutStore";
		document.form.submit();
	}
	
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
     			<div>
     			<h3>其他材料列表</h3>
     			</div>
     			<!-- title end -->
     			<!-- 搜索框 start -->
     			<div class="row-fluid">
	    			<form class="form-inline" name="form">
	    				<input type="hidden" name="id" id="id"/>
	    				<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/material/getList"/>
	    				<fieldset>
            				<legend><span class="label label-info">按条件搜索其他材料信息</span></legend>
				        	<div class="span4">
				          		<div class="control-group">
				            		<label class="control-label" for="materialDetail">时间</label>
			              			 <input id="startDate" name="startDate" class="input-small" type="text"  value="${param.startDate }"
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
						           — <input id="endDate" name="endDate" class="input-small" type="text" value="${param.endDate }"
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
				          		</div>
				        	</div>
					        <div class="span6">
					        	<div class="control-group" style="">
					            	<label class="control-label" for="materialType">材料类型</label>
					              	<select id="materialType" name="materialType" style="width: 110px;">
						            	<option>请选择</option>
										<c:forEach var="lev" items='${web:getDataItem("material_type")}'>
											<option value="${lev.dataItemValue}" <c:if test="${lev.dataItemValue==param.materialType}">selected</c:if> >
												${lev.dataItemName }
											</option>
										</c:forEach>
									</select>
					          	</div>
					        </div>
				       		<div class="span10">
				       			<a class="btn btn-info" href="javascript:void(0)" onclick="doSearch();"><i class="icon-search icon-white"></i>&nbsp;搜 索</a>
				       			<a class="btn btn-primary" href="javascript:void(0)" onclick="toAdd();"><i class="icon-plus icon-white"></i>&nbsp;添加材料</a>
				       		</div>
            			</fieldset>	
	    			</form>
	    		</div>
	    		<!-- 搜索框 end -->
	    		<!-- 列表 start -->
	    		<div class="table">
			   		<h4>其他材料列表</h4>
		      		<table class="table table-striped table-bordered">	
	                	<thead>
		                	<tr> 
		                        <th>车间</th>
		                        <th>材料类型</th>
		                        <th>物料描述</th>
		                        <th>物料组</th>
		                        <th>物料编码</th>
		                        <th>单位</th>
		                        <th>数量</th>
		                        <th>总价</th>
		                        <th>时间</th>
		                     </tr>
	                	</thead>
	                    <tbody>
	                    	<c:if test="${empty page.resultList}">
	                    		<tr>
		                            <td colspan="9" style="text-align: center;color:red;">暂无数据！</td>
		                         </tr>   
	                    	</c:if>
	                    	<c:if test="${not empty page.resultList}">
		                    	<c:forEach items="${page.resultList}" var="material" varStatus="i">
			                        <tr>
			                        	<td>${material.dept_name}</td>
			                            <td>
				                           <c:forEach var="lev" items='${web:getDataItem("material_type")}'>
												<c:if test="${lev.dataItemValue==material.material_type}">${lev.dataItemName }</c:if>
										   </c:forEach>
										</td>
			                            <td>${material.material_detail}</td>
			                            <td>${material.material_group}</td>
			                            <td>${material.material_code}</td>
			                            <td>${material.unity}</td>
			                            <td>${material.store_num}</td>
			                            <td>${material.amount}</td>
			                            <td><tag:date value="${material.out_date}"/></td>
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
