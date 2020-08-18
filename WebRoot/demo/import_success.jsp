<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <title>Uploadify</title>
        <link href="${pageContext.request.contextPath}/css/uploadify/default.css" rel="stylesheet" type="text/css" />
        <link href="${pageContext.request.contextPath}/css/uploadify/uploadify.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/uploadify/jquery-1.3.2.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/uploadify/swfobject.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/uploadify/jquery.uploadify.v2.1.0.js"></script>
        <script type="text/javascript">
        	
        </script>
    </head>
    <body>
        <!--  -->
        <form action="" name="myform" enctype="application/x-www-form-urlencoded" >
        ${message}
        <div class="Temp-2">
			<a class="Bbtn" href="#" onclick="location.href='${pageContext.request.contextPath}/demo/importExcel.jsp'"><span class="Br">返回</span></a>
		</div>  
        </form>
    </body>
</html>