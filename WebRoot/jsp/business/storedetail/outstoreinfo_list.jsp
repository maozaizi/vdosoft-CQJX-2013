<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>出库历史管理列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function goBack(){
		document.myform.action="${pageContext.request.contextPath}/api/storedetail/getStoreList";
		document.myform.submit();
	}
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
    			 <form action="" name="myform" id="myform">
    			<!-- title start 
      			<ul class="breadcrumb">
			        <li>库存明细</li>
     			</ul>
     			-->
     			<div class="page-header">
     			<h3>库存明细</h3>
     			</div>
     			<!-- title end -->
     			<!-- 搜索框 start -->
     			<div>
				  	<a class="btn btn-primary" href="javascript:void(0)" onclick="goBack();">返回</a> 
				</div> 
  		  		<!-- 搜索框 end -->
		  		<!-- 列表 start -->
	    		<div class="table">
			   		<h4>出库历史列表</h4>
		      		<table class="table table-striped table-bordered">	
	                	<thead>
		                	<tr> 
		                		<th>工卡号</th>
		                        <th>物料描述</th>
		                        <th>物料组</th>
		                        <th>物料编码</th>
		                        <th>出库数量</th>
		                        <th>单位</th>
		                        <th>总金额</th>
		                        <th>车间</th>
		                     </tr>
	                	</thead>
	                    <tbody>
	                    	<c:if test="${empty page.resultList}">
	                    		<tr>
		                            <td colspan="9" style="text-align: center;color:red;">暂无数据！</td>
		                         </tr>   
	                    	</c:if>
	                    	<c:if test="${not empty page.resultList}">
		                    	<c:forEach items="${page.resultList}" var="store" varStatus="i">
			                        <tr>
			                        	<td><a href="${pageContext.request.contextPath}/api/storeDetail/getOutStoreDetailList?outId=${store.id}&workCard=${store.workCard}">${store.workCard}</a></td>
			                            <td>${store.materialDetail}</td>
			                            <td>${store.materialGroup}</td>
			                            <td>${store.materialCode}</td>
			                            <td>${store.storeNum}</td>
			                            <td>${store.unity}</td>
			                            <td>${store.amount}</td>
			                            <td>${store.deptName}</td>
			                        </tr>
		                        </c:forEach>
	                        </c:if>
	                    </tbody>
	                </table>
	                <div class="page">
						<%@include file="/jsp/public/standard.jsp"%>
					</div>
				</div>
				</form>
			</div>
		</div>
	</div>				 
</body>
</html>
