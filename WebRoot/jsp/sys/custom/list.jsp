<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>快捷方式选择</title>
<base target="_self"/>
<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>	
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>

<script type="text/javascript">	
	function doSubmit(){
		document.forms[0].action = "${pageContext.request.contextPath}/api/custom/saveCustom";
		document.forms[0].submit();
	}
	function isCheck(id){
		document.getElementById(id).checked = true;
	}
	$(document).ready(function (){
	           var flag = ${flag};
				if (flag==1){
				  window.returnValue=1;
				  window.close();
				}
	        }
      	);
     
</script>
</head>
<body>
	<div class="MainContent">
	  <div class="ListBlock">
	  <h3 class="cBlack">您当前的位置：自定义快捷方式</h3>
	<form id="customForm" method="post" action="">
		<input type="hidden" name="url" value="${pageContext.request.contextPath}/api/custom/getList"/>
	   <div class="List">
	   <table width="100%" border="0"  cellpadding="0" cellspacing="0">
 			<thead>
				<tr>
					<th colspan="8">自定义选择项</th>
		        </tr>
	        </thead>
			<tbody>
		        <c:forEach items="${customList}" var="custom" varStatus="index">
			        <c:if test="${(index.index+1)%4==1}"><tr ondblclick="isCheck('${index.index}');"></c:if>
			        	<c:if test="${custom.ischeck=='1'}">
			        	<td><input type="checkbox" name="authid" id="${index.index}" value="${custom.auth_id}" checked="true"/></td>
			        	</c:if>
			        	<c:if test="${custom.ischeck!='1'}">
			        	<td><input type="checkbox" name="authid" id="${index.index}" value="${custom.auth_id}"/></td>
			        	</c:if>
				      	<td>${custom.auth_name}</td>
				    <c:if test="${(index.index+1)%4==0}"></tr></c:if> 
				</c:forEach>
			</tbody>
			<tfoot>
			</tfoot> 	
	    	</table>
	    </div>
	</form>
	 <div class="opt_btn">
	        <div class="yy">
			   <a href="#" class="btn" onclick="doSubmit();" title="完成"><span class="r">完成</span></a>
			    <a href="#" class="btn" onclick="javascript:window.close();" title="关闭"><span class="r">关闭</span></a>
			</div>
	  </div>
	</div>
	</div>
  </body>
 
</html>