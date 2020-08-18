<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/strutils.js"></script>

<%
	String infoType = (String) request.getParameter("type");
	String infoMsg = (String) request.getAttribute("infoMsg");
	String url = (String) request.getAttribute("url");
	if ("err".equals(infoType)) {
%>
<script type="text/javascript">ErrMsg('${errMsg}')</script>
<%
	} else {
%>
<script type="text/javascript">InfoMsg('${infoMsg}','${url}')</script>
<%
	}
%>


