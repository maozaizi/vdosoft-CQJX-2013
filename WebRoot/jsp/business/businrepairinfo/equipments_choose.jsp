<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>选择设备</title>
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
	function saveChoose(){
		var rowId = document.getElementById("rowId").value;
		if(document.getElementById("equipmentNameParam").value!=""){
		window.opener.document.getElementById("equipmentId_"+rowId).value=document.getElementById("equipmentId").value;
		window.opener.document.getElementById("equipmentName_"+rowId).value=document.getElementById("equipmentNameParam").value;
		window.opener.document.getElementById("equipmentDetail_"+rowId).value=document.getElementById("equipmentNameParam").value;
		}else{
			window.opener.document.getElementById("equipmentDetail_"+rowId).value="";
		}
		window.close();
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
					document.getElementById("equipmentId").value=node.id;
					document.getElementById("equipmentNameParam").value=node.equipmentname;
				
			}
			}
		};
		
		$(document).ready(function(){
			$.ajax({
				url:'${pageContext.request.contextPath}/ajaxapi/businrepairinfo/toChooseEquips',
				type:'post',
				dataType:'json',
				success:function(data, textStatus, jqXHR){
					var zNodes = data.equipmentlist;
					$.fn.zTree.init($("#treeDemo"), setting, zNodes);
					var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
					var node = treeObj.getNodeByParam('parentid', -1); 
                	treeObj.expandNode(node,true);
					//treeObj.expandAll(true);
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
    		<div class="span12">
     			<form class="form-inline" name="myform">
	    			<input type="hidden" name="equipmentId" id="equipmentId" value=""/>
			    	<input type="hidden" name="equipmentNameParam" id="equipmentNameParam" value=""/>
			    	<input type="hidden" name="rowId" id="rowId" value="${rowId }"/>
			    </form>
			   	<div class="content_wrap">
					<div class="zTreeDemoBackground left">
						<ul id="treeDemo" class="ztree"></ul>
					</div>
				</div>
     		</div>
     	</div>
     </div>	
	<div id="sticky" class="sticky">	
			<div class="sticky_opt">
			     <button class="btn btn-primary" onclick="saveChoose();">确  定</button>
				 <button class="btn" onclick="window.close();">关  闭</button>
		    </div>
	    </div>
</body>
</html>
