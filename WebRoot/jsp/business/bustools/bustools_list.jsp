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
		document.myform.action="${pageContext.request.contextPath}/api/bustoolswf/getToolsPlanList";
		document.myform.submit();
	}
	
	
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
      			<div>
			        <h3>钻修列表</h3>
     			</div>
				<!-- 搜索框开始 start -->
				<div class="row-fluid">
	    			<form class="form-inline" name="myform" method="post">
    	 				<fieldset>
            				<legend><span class="label label-info">按条件搜索</span></legend>
				            <div class="span6">
				              <p class="control-group">
				               <label class="control-label" for="equipmentName">设备名称</label>
				               <input id="equipmentName" name="equipmentName" class="span9" type="text" value="${param.equipmentName }" />
				              </p>
				            </div>
				            <div class="span4">
				              <p class="control-group">
				               <label class="control-label" for="workCard">工卡号</label>
				               <input id="workCard" name="workCard" class="span9" type="text" value="${param.workCard }" />
				              </p>
				            </div>
            				<div class="span10"> <a href="javascript:void(0);" class="btn btn-info" onclick="doSearch();"><i class="icon-search icon-white" ></i>搜索</a> </div>
          				</fieldset>
     				</form>     
  				</div>
  				<!-- 搜索框 end -->
			   	<div class="table">
   					<h4>劳保材料列表</h4>
		      		<table class="table table-striped table-bordered">
			        	<thead>
				           	<tr>
				           	 <th>工卡号</th>
				           	 <th>设备名称</th>
				             <th>申请时间</th>
				           	</tr>
			         	</thead>  	
			         	<tbody>
			          		<c:if test="${empty page.resultList}">
				          		<tr>
				                   <td colspan="6" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                </tr>   
			          		</c:if>
				          	<c:if test="${not empty page.resultList}">
				            	<c:forEach items="${page.resultList}" var="busTools" varStatus="i">
				                	<tr>
				                		 <td>${busTools.workcard}</td>
					                     <td><a href="${pageContext.request.contextPath}/api/bustoolswf/toBustoolswfDetail?id=${busTools.id}" target="_blank">${busTools.equipmentName}</a></td>
					                     <td><tag:date  value="${busTools.createDate}" /></td>
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
