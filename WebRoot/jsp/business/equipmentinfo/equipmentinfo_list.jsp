<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>设备信息管理列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/zTreeStyle/zTreeStyle.css" type="text/css"></link>
<link href="${pageContext.request.contextPath }/css/fire/layout.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqBootstrapValidation.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<style>
.sticky {
	position: fixed;
	bottom: 0;
	left: 0;
	z-index: 9999;
	width: 100%;
}


.sticky {
	height: 30px;
	padding:10px;
	background: #fefefe;
	border-top: 1px solid #DDDDDD;
	box-shadow: 1px 0px 2px rgba(0,0,0,0.2);
}
.sticky_opt {
	margin-left:3em
}

</style>

<script type="text/javascript">
	function toUpdate() {
		document.myform.action="${pageContext.request.contextPath}/api/equipmentinfo/toUpdateEquipmentInfo";
		document.myform.submit();
	}

	function doDel() {
		if (confirm("您确定删除该设备吗？")) {
			document.myform.action="${pageContext.request.contextPath}/api/equipmentinfo/delEquipmentInfo";
			document.myform.submit();
		}
	}
	function toAdd(){
		document.myform.action="${pageContext.request.contextPath}/api/equipmentinfo/toAddEquipmentInfo";
		document.myform.submit();
	}
</script>
<script type="text/javascript">
	var setting = {
			view: {
				showLine: false
			},
			data:{
				key:{
					children: "children",
					name:"wholename",
					url:'xurl'
				},
				simpleData:{
					enable: true,
					idKey:'id',
					pIdKey:'parentid',
					rootPId: -1
				}
			},
			callback:{
				onClick:function(e,id,node){
					var parent = node.parentid;
					if(parent == -1){
						document.getElementById("del_top").style.display = "none";
					}else{
						document.getElementById("del_top").style.display = "inline";
					}
					document.getElementById("save_top").style.display = "inline";
					document.getElementById("update_top").style.display = "inline";
					document.getElementById("parentId").value=node.id;
					document.getElementById("id").value=node.id;
				}
			}
		};
		
		$(document).ready(function(){
			$.ajax({
				url:'${pageContext.request.contextPath}/ajaxapi/equipmentinfo/getEquipmentInfoList',
				type:'post',
				dataType:'json',
				success:function(data, textStatus, jqXHR){
					var zNodes = data.equipmentlist;
					$.fn.zTree.init($("#treeDemo"), setting, zNodes);
					var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
					var node = treeObj.getNodeByParam('parentid', -1); 
                	treeObj.expandNode(node,true);
				},
				error:function(){
					alert("error");
				}
			});
			
		});
		

</script>


</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
     			<div>
     			<h3>设备信息</h3>
     			</div>
				<div>
					<form action="" name="myform" id="myform" method="post">
			  	 		<input type="hidden" name="id" value="" id="id"/>
			  	 		<input type="hidden" name="parentId" value="" id="parentId"/>
			  	 		<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/equipmentinfo/getEquipmentInfoList"/>
			  	 	</form>
				</div>
            	<!-- 列表开始 -->
            	<div class="content_wrap" style="padding-bottom:55px;">
					<div class="zTreeDemoBackground left">
						<ul id="treeDemo" class="ztree"></ul>
					</div>
				</div>
            	<!-- 列表结束 -->			
     		</div>
     	</div>		
	</div>
	<div id="sticky" class="sticky">	
			<div class="sticky_opt">
			     <button class="btn btn-primary" onclick="toAdd()" id="save_top" title="添加当前级别的组织结构节点" style="display:none">添 加</button>
				 <button class="btn" onclick="toUpdate()" id="update_top" title="编辑当前选择节点" style="display:none">修 改</button>
				 <button class="btn" id="del_top"  onclick="doDel()"   title="作废当前选择节点" style="display:none">作 废</button>
		    </div>
	    </div>
</body>
</html>
