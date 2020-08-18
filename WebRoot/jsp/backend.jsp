<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@include file="/jsp/public/limit_top.jsp"%>
<%@include file="/jsp/config/taglib.jsp"%>
<head>
  <title>Comet php backend</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta http-equiv="Cache-Control" CONTENT="no-store"/>
  <meta http-equiv="Pragram" CONTENT="no-cache"/>
  <meta http-equiv="Expires" CONTENT="no-store"/>
  <meta http-equiv="Refresh" CONTENT="30"/>
</head>
<body>
<script type="text/javascript">
parent.showMessage(${newMailsNum});
</script>
<%
  //out.println("<script type=\"text/javascript\">");
  //out.println ("parent.showMessage(${newMailsNum});");
  //out.println ("</script>");
  //System.out.println("----------------------");  
  //out.flush(); // used to send the echoed data to the client
%>
</body>
</html>