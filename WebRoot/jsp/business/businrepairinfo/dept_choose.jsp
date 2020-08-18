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
		var rowId = document.getElementById("rowId").value;
		parent.getElementById("deptName_"+rowId).value = document.getElementById("deptName").value;
		var code = document.getElementById("deptId").value;
		parent.getElementById("deptId_"+rowId).value = code;
		var myDate = new Date();
		parent.getElementById("code"+rowId).value = myDate.getFullYear();
		var snum = document.getElementById("serialnum").value;
		snum = parseInt(snum)+1;
		
		parent.getElementById("serialnum"+rowId).value = snum;
		var len = snum.toString().length;  
    	while(len < 4) {  
        	snum = "0" + snum;  
        	len++;  
    	}  
		
		if(code.indexOf('|YC|')>0){
			parent.getElementById("code"+rowId).value = myDate.getFullYear()+"Y";
		} 
		if(code.indexOf('|QY|')>0){
			parent.getElementById("code"+rowId).value = myDate.getFullYear()+"Q";
		} 
		if(code.indexOf('|ZX|')>0){
			parent.getElementById("code"+rowId).value = parent.getElementById("code"+rowId).value+"Z";
		} 
		if(code.indexOf('|JX01|')>0){
			parent.getElementById("code"+rowId).value = parent.getElementById("code"+rowId).value+"J";
		} 
		if(code.indexOf('|DX|')>0){
			parent.getElementById("code"+rowId).value = parent.getElementById("code"+rowId).value+"D";
		} 
		if(code.indexOf('|MH|')>0){
			parent.getElementById("code"+rowId).value = parent.getElementById("code"+rowId).value+"M";
		} 
		if(code.indexOf('|JJ|')>0){
			parent.getElementById("code"+rowId).value = parent.getElementById("code"+rowId).value+"G";
		} 
		parent.getElementById("code"+rowId).value = parent.getElementById("code"+rowId).value+snum;
		parent.getElementById("orgId"+rowId).value = document.getElementById("orgId").value;
		
		if(rowId == 0){
			parent.getElementById("domainNo0").value = parent.getElementById("code"+rowId).value;
			//判断是否有动态添加行数据有的话替换值
			var txtTRLastIndex = parent.getElementById("txtTRLastIndex").value;
			if(txtTRLastIndex>=2){
					parent.getElementById("domainNo1").value = parent.getElementById("domainNo0").value;
			}
		}
		window.close();
	}
	function selectTr(id,code,name,pname,snum){
		document.getElementById(id).checked=true;
		document.getElementById("deptName").value = pname+"-"+name;
		document.getElementById("deptId").value = code;
		document.getElementById("serialnum").value = snum;
		document.getElementById("orgId").value = id;
	}
</script>
</head>
<body>
	<input type="hidden" name="deptId" id="deptId"/>
	<input type="hidden" name="deptName" id="deptName"/>
	<input type="hidden" name="serialnum" id="serialnum"/>
	<input type="hidden" name="orgId" id="orgId"/>
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
		                        <th>编号</th>
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
			                        <tr onclick="selectTr('${dept.id }','${dept.org_code}','${dept.org_name}','${dept.parent_name}','${dept.serialnum}');">
			                            <td><input type="radio" id="${dept.id }" name="id" value="${dept.id}" onclick="chooseDept('${dept.org_code}','${dept.org_name}','${dept.parent_name}')"/></td>
			                            <td>${dept.parent_name}</td>
			                            <td>${dept.org_name}</td>
			                            <td>${dept.serialnum}</td>
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
