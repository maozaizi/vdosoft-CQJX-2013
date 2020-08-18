<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>主体左边</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/zTreeStyle/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ztree.core-3.5.js"></script>
<SCRIPT type="text/javascript">
		<!--
		var setting = {
			view: {
				showLine: false
			},
			data:{
				key:{
					children:'childlist',
					name:'authname',
					url:'eurl'
				}
			},
			callback:{
				onClick:function(e,id,node){
					if(node.url != ''){
						window.parent.frames["rightFrame"].location.href = "${pageContext.request.contextPath}"+node.url;
					}
				}
			}
		};
		$(document).ready(function(){
			var data = {userpwd:'${userMap.userpwd}',username:'${userMap.username}'}
			$.ajax({
				url:'${pageContext.request.contextPath}/ajaxapi/login/getauthtreedata',
				data:data,
				type:'post',
				dataType:'json',
				success:function(data, textStatus, jqXHR){
					$.fn.zTree.init($("#treeDemo"), setting, data.authlist);
					var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
					treeObj.expandAll(true);
				},
				error:function(){
					alert("error");
				}
			});
		});
		//-->
	</SCRIPT>
</head>
	<body>
		<div class="content_wrap">
			<div class="zTreeDemoBackground left">
				<ul id="treeDemo" class="ztree"></ul>
			</div>
		</div>
	</body>
</html>