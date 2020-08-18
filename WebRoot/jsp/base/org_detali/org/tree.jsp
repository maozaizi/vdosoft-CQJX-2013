<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%--
页面名称：基础组织结构
页面描述：
作者：张波
日期：2012-02-15 14:52
--%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/js/MzTreeView12/MzTreeView12.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/zTreeStyle/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ztree.core-3.5.js"></script>
	
<script type="text/javascript">
	var setting = {
			view: {
				showLine: false
			},
			data:{
				key:{
					children:'childlist',
					name:'orgname',
					url:'eurl'
				}
			},
			callback:{
				onClick:function(e,id,node){
					window.parent.frames["app_rightFrame"].location.href = "${pageContext.request.contextPath}/api/org/getInfo?orgCode="+node.orgcode;
				}
			}
		};
	$("document").ready(function(){
		$.ajax({
				url:'${pageContext.request.contextPath}/ajaxapi/org/getTree',
				type:'post',
				dataType:'json',
				success:function(data, textStatus, jqXHR){
					var zNodes = data.organizationdetalilist;
					$.fn.zTree.init($("#treeDemo"), setting, zNodes);
					var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
					treeObj.expandAll(true);
				},
				error:function(){
					alert("error");
				}
			});
	});
</script>
</head>
<body>
<div class="treeblock_out" >
   <table cellspacing="0" cellpadding="0" class="treeblock_in" width="100%" >
 <tr>
 	<th colspan="2" class="treeblock_th">组织结构</th>
 </tr>
 <tr>
   <td class="treeblock_td">

	<table cellspacing="0" cellpadding="0" >
	      <tr>
			<td class="rtext">
				<div class="content_wrap">
					<div class="zTreeDemoBackground left">
						<ul id="treeDemo" class="ztree"></ul>
					</div>
				</div>
		  	 	<form action="" name="form" id="form" method="post"  target="app_rightFrame" >
		  	 		<input type="hidden" name="parentId" id="parentId"/>
		  	 		<input type="hidden" name="organizationDetailId" id="organizationDetailId"/>
		  	 		<input type="hidden" name="baseOrganizationId" id="baseOrganizationId"/>
		  	 		<input type="hidden" name="type" id="type" value="${param.type }"/>
					<input type="hidden" name="tempUrl" id="tempUrl" value = "${pageContext.request.contextPath}/api/org/getTree"/>
					<input type="hidden" name="nodeType" id="nodeType"/>
		  	 	</form>
			</td>
		  </tr>
	</table>	  
   </td>
 </tr>
 
	</table> 
</div>
</body>
</html>
