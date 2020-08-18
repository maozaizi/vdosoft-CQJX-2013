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
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function doSearch(){
		document.myform.action="${pageContext.request.contextPath}/api/mechtreatment/getMechTreatmentList";
		document.myform.submit();
	}
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
      			<div>
			        <h3>机加列表</h3>
     			</div>
				<!-- 搜索框开始 start -->
				<div class="row-fluid">
	    			<form class="form-inline" name="myform">
    	 				<fieldset>
            				<legend><span class="label label-info">按条件搜索进修设备</span></legend>
				            <div class="span5">
				              <p class="control-group">
				               <label class="control-label" for="equipmentName">名称</label>
				               <input id="equipmentName" name="equipmentName" class="input-medium" type="text" value="${param.equipmentName }" />
				              </p>
				              <p class="control-group">
				                <label class="control-label" for="equipmentModel">规格</label>
				              	<input id="equipmentModel" name="equipmentModel" class="input-medium" type="text" value="${param.equipmentModel }" />
				              </p>
				            </div>
				            <div class="span5">
				              <p class="control-group">
				                <label class="control-label" for="equipmentModel">送修单位</label>
				              	<input id="userName" name="userName" class="input-medium" type="text" value="${param.userName }" />
				              </p>
				              <p class="control-group">
				                <label class="control-label" for="equipmentModel">修制类别</label>
				              	<input id="makeSort" name="makeSort" class="input-medium" type="text" value="${param.userName }" />
				              </p>
				            </div>
            				<div class="span10"> <a href="javascript:void(0);" class="btn btn-info" onclick="doSearch();"><i class="icon-search icon-white" ></i>搜索</a> </div>
          				</fieldset>
     				</form>     
  				</div>
  				<div class="table">
   					<h4>机加信息列表</h4>
		      		<table class="table table-striped table-bordered">
			        	<thead>
				           	<tr>
			           		<th>工卡号</th>
				           	 <th>车间</th>
				           	 <th>名称</th>
				             <th>规格</th>
				             <th>修制类别</th>
				             <th>施工要求</th>
				           	</tr>
			         	</thead>  	
			         	<tbody>
			          		<c:if test="${empty page.resultList}">
				          		<tr>
				                   <td colspan="6" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                </tr>   
			          		</c:if>
				          	<c:if test="${not empty page.resultList}">
				            	<c:forEach items="${page.resultList}" var="machiningMap" varStatus="i">
				                	<tr onclick="selectTr('${machiningMap.id }');">
				                		<td>${machiningMap.mainCard}</td>
				                		 <td>${web:getDeptName(machiningMap.orgId)}-机加车间</td>
					                     <td><a href="${pageContext.request.contextPath}/api/mechtreatment/toMechTreatmentDetailsPage?id=${machiningMap.Id}" target="_blank">${machiningMap.equipmentName}</a></td>
					                     <td>${machiningMap.equipmentModel}</td>
					                     <td>${machiningMap.makeSort}</td>
					                     <td>${machiningMap.problem}</td>
				                 	</tr>
				                </c:forEach>
			               </c:if>
			         	</tbody>
		      		</table>
					<%@include file="/jsp/public/standard.jsp" %>
			    </div>
			</div>
		</div>
	</div>	         	
</body>
</html>