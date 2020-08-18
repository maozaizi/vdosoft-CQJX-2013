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
		document.form.action="${pageContext.request.contextPath}/api/businrepairinfo/getInrepairMainCardList";
		document.form.submit();
	}
	function toUpdate() {
		var mainCard = document.getElementsByName("id");
		var checked = false;
		for (var i = 0; i < mainCard.length; i++) {
			if (mainCard[i].checked) {
				checked = true;
				document.getElementById("mainCard").value=mainCard[i].value;
			}
		}
		if (!checked) {
			alert("请选择设备！");
			return ;
		}
		document.form.action="${pageContext.request.contextPath}/api/businrepairinfo/getRepairSettlementList";
		document.form.submit();
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<div>
  			<h3>维修结算列表</h3>
		</div>
  		<div class="row-fluid">
    		<div class="span9">
     			<!-- 搜索框开始 start -->
				<div class="row-fluid">
	    			<form class="form-inline" name="form">
			    	<input type="hidden" name="mainCard" id="mainCard"/>
			    	<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/businrepairinfo/getInrepairMainCardList"/>
    	 				<!-- 搜索框开始 start -->
    	 				<fieldset>
            				<legend><span class="label label-info">按条件搜索结算信息</span></legend>
				        	<div class="span5">
				          		<div class="control-group">
				            		<label class="control-label" for="equipmentName">设备名称</label>
			              			<input id="equipmentName" name="equipmentName" class="input-medium" type="text" />
				          		</div>
				        	</div>
				        	<div class="span5">
				        		<div class="control-group">
				            		<label class="control-label" for="workCard">工卡号</label>
			              			<input id="workCard" name="workCard" class="input-medium" type="text" />
				          		</div>
				        	</div>
				        	<div class="span10">
				       			<a class="btn btn-info" href="javascript:void(0)" onclick="doSearch();"><i class="icon-search icon-white"></i>&nbsp;搜 索</a>
				       			<a class="btn btn-primary" href="javascript:void(0)" onclick="toUpdate();"><i class="icon-edit icon-white"></i>&nbsp;结 算</a> 
				       		</div>
				        </fieldset>
				        <!-- 搜索框 结束 end-->
		        	</form>
		       </div>
		       <div class="table">
		      		<table class="table table-striped table-bordered"> 
	                	<thead>
		                	<tr> 
		                        <th>选择</th>
		                        <th>设备名称</th>
		                        <th>主卡号</th>
		                        <th>修理类别</th>
		                        <th>单位</th>
		                        <th>定额工时</th>
		                        <th>定额价格（元）</th>
		                     </tr>
	                	</thead>
	                    <tbody>
	                    	<c:if test="${empty page.resultList}">
	                    		<tr>
		                            <td colspan="7" style="text-align: center;color:red;">暂无数据！</td>
		                         </tr>   
	                    	</c:if>
	                    	<c:if test="${not empty page.resultList}">
		                    	<c:forEach items="${page.resultList}" var="busworkCard" varStatus="i">
			                        <tr>
			                            <td><input type="radio" name="id" value="${busworkCard.main_card}"/></td>
			                            <td>${busworkCard.equipment_name}</td>
			                            <td>${busworkCard.main_card}</td>
			                            <td><c:if test="${busworkCard.repair_type==1}">大修</c:if><c:if test="${busworkCard.repair_type==1}">中修</c:if><c:if test="${busworkCard.repair_type==1}">检修</c:if></td>
			                            <td>${busworkCard.unity}</td>
			                            <td>${busworkCard.quota_hour}</td>
			                            <td>${busworkCard.repair_value}</td>
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
