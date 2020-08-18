<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>权限管理</title>
		<link href="${pageContext.request.contextPath}/css/fire/global.css"
			rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/fire/layout.css"
			rel="stylesheet" type="text/css" />
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/css/zTreeStyle/zTreeStyle.css"
			type="text/css"></link>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery.ztree.core-3.5.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery.ztree.excheck-3.5.min.js"></script>

		<script type="text/javascript">
			
			function goBack() {
				window.location.href = "${param.url}";
			}
			
			function save() {
				var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
				var nodes = treeObj.getCheckedNodes(true);
				if(nodes.length>0){
					var authoids = [];
					$.each(nodes,function(index,node){
						authoids.push(node.authid);
					});
					document.forms[0].action = "${pageContext.request.contextPath }/api/authgroup/saveMember?authId="+authoids;
					document.forms[0].submit();
				}else{
					alert("请选择权限！");
				}
			}
		</script>

	<SCRIPT type="text/javascript">
		<!--
		var setting = {
			view: {
				showLine: false
			},
			check: {
				enable: true
			},
			data:{
				key:{
					children:'childlist',
					name:'authname'
				},
				simpleData:{
					enable:true,
					idKey:'authid',
					pIdKey:'parentauthid'
				}
			},
			callback:{
				onClick:function(e,id,node){
					if(node.url != ''){
					}
				}
			}
		};
		$(document).ready(function(){
			var data = {authGroupId:'${authGroupId}'}
			$.ajax({
				url:'${pageContext.request.contextPath}/ajaxapi/authgroup/memberlist',
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
		<div class="MainContent">
			<div class="ListBlock">
				<h3 class="cBlack">
					权限组成员管理
				</h3>


				<form id="form2" name="form2" method="post" action="">
					<input type="hidden" name="url" id="url" value="${param.url}" />
					<input type="hidden" name="authGroupId" id="authGroupId"
						value="${authGroupId}" />
					<div id="treeDemo" class="ztree"></div>
					<div class="opt_btn">
						<div class="yy">
							<a class="btn" href="#" name="saveBtn" onclick="save()"><span
								class="r">保存</span> </a>
							<a class="btn" href="#" name="backBtn" onclick="goBack()"><span
								class="r">返回</span> </a>
						</div>
					</div>
				</form>
			</div>
		</div>
	</body>
</html>
