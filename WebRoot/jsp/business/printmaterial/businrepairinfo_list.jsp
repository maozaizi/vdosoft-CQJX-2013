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
		document.form.action="${pageContext.request.contextPath}/api/businrepairinfo/getInrepairListByState";
		document.form.submit();
	}
	function toUpdate() {
		var id = document.getElementsByName("id");
		var checked = false;
		for (var i = 0; i < id.length; i++) {
			if (id[i].checked) {
				checked = true;
				document.getElementById("id").value=id[i].value.split(",")[0];
				document.getElementById("tableName").value=id[i].value.split(",")[1];
			}
		}
		if (!checked) {
			alert("请选择设备！");
			return ;
		}
		document.form.action="${pageContext.request.contextPath}/api/businrepairinfo/toPrint";
		document.form.submit();
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<div>
  			<h3>在修设备列表</h3>
		</div>
  		<div class="row-fluid">
    		<div class="span9">
     			<!-- 搜索框开始 start -->
				<div class="row-fluid">
	    			<form class="form-inline" name="form">
			    	<input type="hidden" name="id" id="id"/>
			    	<input type="hidden" name="tableName" id="tableName"/>
    	 				<!-- 搜索框开始 start -->
    	 				<fieldset>
            				<legend><span class="label label-info">按条件搜索在修设备</span></legend>
				        	<div class="span5">
				          		<div class="control-group">
				            		<label class="control-label" for="orgName">车间名称</label>
			              			<select id="orgName" name="orgName">
			              				<c:forEach items="${orgList}" var="org">
			              					<option value="${org.org_name}">${org.org_name}</option>
			              				</c:forEach>
			              			</select>
				          		</div>
				        	</div>
				        	<div class="span5">
				        		<div class="control-group">
				            		<label class="control-label" for="workCard">材料名称</label>
			              			<input id="materialDetail" name="materialDetail" class="input-medium" type="text"  value="${param.materialDetail}"/>
				          		</div>
				        	</div>
				        	<div class="span4">
				          		<div class="control-group">
				            		<label class="control-label" for="materialDetail">时间</label>
			              			 <input id="startDate" name="startDate" class="input-small" type="text"  value="${param.startDate }"
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
						           — <input id="endDate" name="endDate" class="input-small" type="text" value="${param.endDate }"
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
				          		</div>
				        	</div>
				        	<div class="span10">
				       			<a class="btn btn-info" href="javascript:void(0)" onclick="doSearch();"><i class="icon-search icon-white"></i>&nbsp;搜 索</a>
				       			<a class="btn btn-primary" href="javascript:void(0)" onclick="toUpdate();"><i class="icon-print icon-white"></i>&nbsp;打 印</a> 
				       		</div>
				        </fieldset>
				        <!-- 搜索框 结束 end-->
		        	</form>
		       </div>
		       <div class="table">
		      		<table class="table table-striped table-bordered"> 
	                	<thead>
		                	<tr> 
		                        <th>车间名称</th>
		                        <th>材料名称</th>
		                        <th>单位</th>
		                        <th>参考价（元）</th>
		                        <th>数量</th>
		                        <th>合计（元）</th>
		                     </tr>
	                	</thead>
	                    <tbody>
	                    	<c:if test="${empty page.resultList}">
	                    		<tr>
		                            <td colspan="6" style="text-align: center;color:red;">暂无数据！</td>
		                         </tr>   
	                    	</c:if>
	                    	<c:if test="${not empty page.resultList}">
		                    	<c:forEach items="${page.resultList}" var="material" varStatus="i">
			                        <tr>
			                            <td>${material.org_name}</td>
			                            <td>${material.material_detail}</td>
			                            <td>${material.unity}</td>
			                            <td>${material.estimate_price}</td>
			                            <td>${material.ratify_num}</td>
			                            <td>${material.total_price}</td>
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
