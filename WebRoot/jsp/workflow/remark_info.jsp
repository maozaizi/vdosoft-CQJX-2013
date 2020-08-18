<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>流程步骤历史操作记录</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
	    <link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
	    		<style type="text/css">
/* ======== 系统-通用-文本录入框样式 ======== */
div.general_textarea h4 {
	margin-bottom: 12px;
	padding-left: 5px;
}

div.general_textarea textarea {
	width: 100%
}

/* ======== 系统-通用-基础信息显示样式 ======== */
div.general_viewinfo_list {
	margin-bottom: 20px;
}

div.general_viewinfo_list h4 {
	margin-bottom: 12px;
	padding-left: 5px;
}

div.general_viewinfo_list table {
	border-collapse: collapse;
	width: 100%
}

div.general_viewinfo_list th {
	background: none repeat scroll 0 0 #efefef;
	border-bottom: medium none;
	border-top: medium none;
	color: #000;
	font-weight: bold;
	line-height: 1.3;
	white-space: nowrap
}

div.general_viewinfo_list td,div.general_viewinfo_list th {
	border: 1px solid #99bce8;
	padding: 5px 12px
}

div.general_viewinfo_list td {
	background: none repeat scroll 0 0 #fff
}

/* ======== 系统-通用-数据表格样式 ======== */
div.general_data_list {
	margin-bottom: 24px;
	border: solid #99bce8 1px;
	position: relative;
}

div.general_data_list div.general_data_list_optbtn {
	width: 50px;
	height: 24px;
	position: absolute;
	right: 20px;
	top: 7px;
}

div.general_data_list h4 {
	height: 33px;
	line-height: 33px;
	background: none repeat scroll 0 0 #d2e0f0;
	border-bottom: solid #99bce8 1px;
	padding-left: 12px;
	font-weight: normal;
}

div.general_data_list table {
	border-collapse: collapse;
	width: 100%
}

div.general_data_list table thead th {
	padding-left: 12px;
	height: 25px;
	background-color: #e2e4e6;
	background-image:
		url("${pageContext.request.contextPath}/css/fire/images/general_data_list_title_bg.png")
		;
	background-repeat: repeat-x;
	border-right: solid #c5c5c5 1px;
}

div.general_data_list table tbody td {
	padding: 5px 12px;
	border-right: 1px solid #d0d0d0;
	border-bottom: 1px solid #ededed;
	background-color: #fff;
}

div.general_data_list table thead th.lost,div.general_data_list table tbody td.lost
	{
	border-right: none;
}

/* ======== 系统-通用-页面底部操作按钮样式 ======== */
div.sys_page_btn {
	width: 210px;
	margin: 30px auto 10px;
	height: 40px
}
</style>
	</head>
	<body>
<div id="fom"
			style="width: 750px; margin-top: 10px; margin-left: 10px;">
			<h3>
				您当前的位置：流程步骤历史操作记录
			</h3>
				<div class="general_data_list">
		<form id="addForm" method="post" action="" >
			<table width="100%" border="0"  cellpadding="0" cellspacing="0">
		<thead>
		<tr>
	   	<h4>流程步骤历史操作记录 </h4>
		</tr>
        </thead>
        <tbody>
        
       <c:forEach items='${remarkList}' var="rem">
	        <tr>
	        	<td>${rem.type}</td>
	        	<td>${rem.remark}</td>
		      	<td><tag:dateTime value="${rem.createDate}"/></td>
		      	<td>${rem.createUser}</td>
		    </tr>
		</c:forEach>
		</tbody>
	</table>
		</form>
	</div>
	<div class="BigBtn sys_page_btn">
				<a class="Bbtn" href="#" onclick="if(confirm('确定关闭当前页吗？')){window.close()};"><span class="Br">关闭</span>
	</div>
</div>
	</body>	
</html>

