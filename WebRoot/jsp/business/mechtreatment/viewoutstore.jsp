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
	function back(){
		document.myform.action="${pageContext.request.contextPath}/api/mechtreatment/gettreatstorelist";
		document.myform.submit();
	}
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
      			<div>
			        <h3>加工件出库记录</h3>
     			</div>
     			<form class="form-inline" name="myform" id="myform">
			   	<div class="table">
		      		<table class="table table-striped table-bordered">
			        	<thead>
				           	<tr>
				           	 <th>加工件名称</th>
				             <th>出库数量</th>
				             <th>单位</th>
				             <th>领用部门</th>
				           	</tr>
			         	</thead>  	
			         	<tbody>
			          		<c:if test="${empty page.resultList}">
				          		<tr>
				                   <td colspan="6" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                </tr>   
			          		</c:if>
				          	<c:if test="${not empty page.resultList}">
				            	<c:forEach items="${page.resultList}" var="outstore" varStatus="i">
				                	<tr>
					                     <td>${outstore.name}</a></td>
					                     <td>
					                     	${outstore.outnum}
					               	 	 </td>
					               	 	 <td>
					                     	${outstore.unity}
					               	 	 </td>
					                   	 <td>
					                   	 	<c:if test="${outstore.type==1}">${outstore.proDept}</c:if>
					                   	 	<c:if test="${outstore.type==2}">${outstore.proDept}${outstore.deptFrom}</c:if>
					                     	
					               	 	 </td>
				                 	</tr>
				                </c:forEach>
			               </c:if>
			         	</tbody>
		      		</table>
					<%@include file="/jsp/public/standard.jsp" %>
			    </div>
			    </form>
			</div>
		 <div class="form-actions">
			<a class="btn" href="javascript:void(0);" onclick="back();" >返　回</a>
		 </div>
		</div>
	</div>	         	
</body>
</html>
