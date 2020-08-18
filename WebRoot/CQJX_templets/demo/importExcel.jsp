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
        	function sub(){
        		document.forms[0].action="${pageContext.request.contextPath}/api/import/importMaterialInfo";
        		document.forms[0].submit();
        	}
        </script>
    </head>
    <body>
        <!--  -->
        <form action="" name="myform" enctype="multipart/form-data" >
        <input type="file" name="fileUrl" id="fileUrl" size="64"/>
        <div class="Temp-2">
			<a class="Bbtn" href="#" onclick="sub();"><span class="Br">导入</span></a>
		</div>  
        </form>
    </body>
</html>