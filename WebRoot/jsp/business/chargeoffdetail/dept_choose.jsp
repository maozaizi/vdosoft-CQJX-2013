<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>选择车间</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript">
	function chooseDept(code,name,pname){
		var parent = window.opener.document;
		document.getElementById("deptName").value = pname+"-"+name;
		document.getElementById("deptId").value = code;
	}
	function saveChoose(){
		var parent = window.opener.document;
		parent.getElementById("deptName").value = document.getElementById("deptName").value;
		var code = document.getElementById("deptId").value;
		parent.getElementById("deptId").value = code;
		window.close();
	}
	function selectTr(id,code,name,pname){
		document.getElementById("id_"+id).checked=true;
		document.getElementById("deptName").value = pname+"-"+name;
		document.getElementById("deptId").value = code;
	}
</script>
</head>
<body>
	<input type="hidden" name="deptId" id="deptId"/>
	<input type="hidden" name="deptName" id="deptName"/>
	<input type="hidden" name="rowId" id="rowId" value="${rowId }"/>
	<div class="container-fluid">
		<div class="row-fluid">
	    	<div class="span9">
	    		<div class="page-header">
			       <h3>车间列表</h3>
			    </div>
	    		<div class="row-fluid">
	            	<table class="table table-striped table-bordered">
	            		<thead>
		                	<tr> 
		                		<th>选择</th>
		                        <th>厂区</th>
		                        <th>车间名称</th>
		                     </tr>
                		</thead>
                		<tbody>
	                    	<c:if test="${empty deptList}">
	                    		<tr>
		                            <td colspan="10" style="text-align: center;"><font color='red'>暂无数据！</font></td>
		                         </tr>   
	                    	</c:if>
	                    	<c:if test="${not empty deptList}">
		                    	<c:forEach items="${deptList}" var="dept" varStatus="i">
			                        <tr onclick="selectTr('${dept.id }','${dept.org_code}','${dept.org_name}','${dept.parent_name}');">
			                            <td><input type="radio" id="id_${dept.id }" name="id" value="${dept.id}" onclick="chooseDept('${dept.org_code}','${dept.org_name}','${dept.parent_name}')"/></td>
			                            <td>${dept.parent_name}</td>
			                            <td>${dept.org_name}</td>
			                        </tr>
		                        </c:forEach>
	                        </c:if>
                    	</tbody>
	        		</table>
		        </div>
	    	</div>
	    </div>
	</div>
<!-- 列表结束 end -->
	<div class="form-actions">
		<a class="btn" href="javaScript:void(0);" onclick="window.close();">关  闭</a>
		<a class="btn btn-primary" href="javaScript:void(0);" onclick="saveChoose();">提  交</a>
	</div>
</body>
</html>
