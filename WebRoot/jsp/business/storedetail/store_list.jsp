<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>出库详细信息管理列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function doSearch(){
		document.myform.action="${pageContext.request.contextPath}/api/storeDetail/getStoreList";
		document.myform.submit();
	}
	
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
    			<!-- title start -->
      			 <h3>库存信息</h3>
     			<!-- title end -->
     			<div class="row-fluid">
	    			<form class="form-inline" name="myform">
	    				<fieldset>
            				<legend><span class="label label-info">按条件搜索库存信息</span></legend>
					        <div class="span5">
					          <div class="control-group">
					            <label class="control-label" for="materialClass">工卡号</label>
				              	<input id="id" name="id" type="hidden" />
				              	<input id="workCard" name="workCard" class="input-medium" type="text" value="${param.workCard }" />
					          </div>
					    </fieldset>	
				  		<div class="span10">
				  			<a class="btn btn-info" href="javascript:void(0)" onclick="doSearch();"><i class="icon-search icon-white"></i>&nbsp;搜 索</a>
				  		</div>
	    			</form>
	    		</div>
     			<!-- 列表 start -->
	    		<div class="table">
			   		<h4>出库信息列表</h4>
		      		<table class="table table-striped table-bordered">	
	                	<thead>
		                	<tr> 
		                		<th>工卡号</th>
		                        <th>车间</th>
		                        <th>总金额（元）</th>
		                     </tr>
	                	</thead>
	                    <tbody>
	                    	<c:if test="${empty page.resultList}">
	                    		<tr>
		                            <td colspan="10" style="text-align: center;color:red;">暂无数据！</td>
		                         </tr>   
	                    	</c:if>
	                    	<c:if test="${not empty page.resultList}">
		                    	<c:forEach items="${page.resultList}" var="store" varStatus="i">
			                        <tr>
			                        	<td><a href="${pageContext.request.contextPath}/api/storeDetail/getOutStoreInfoList?workCard=${store.workCard}" >${store.workCard}</a></td>
			                        	<td>${store.deptName}</td>
			                            <td>${store.amount}</td>
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
</body>
</html>
