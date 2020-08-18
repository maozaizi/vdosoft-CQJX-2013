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
		if(document.getElementById("value").value == ""||document.getElementById("value").value==null){
			alert("请选择有效的设备");
			return;
		}
		
		window.opener.document.getElementById("equipmentId${index}").value=document.getElementById("equipmentId").value;
		window.opener.document.getElementById("equipmentName${index}").value=document.getElementById("equipmentNameParam").value;
		window.opener.document.getElementById("standardNum${index}").value=document.getElementById("standardNum").value;
		window.opener.document.getElementById("unity${index}").value=document.getElementById("unity").value;
		window.opener.document.getElementById("value${index}").value=document.getElementById("value").value;
		window.opener.document.getElementById("quotaHour${index}").value=document.getElementById("quotaHour").value;
		window.opener.document.getElementById("quotaHours${index}").value=document.getElementById("quotaHours").value;
		window.opener.document.getElementById("mediumHour${index}").value=document.getElementById("mediumHour").value;
		window.opener.document.getElementById("checkHour${index}").value=document.getElementById("checkHour").value;
		window.opener.document.getElementById("majorCost${index}").value=document.getElementById("majorCost").value;
		window.opener.document.getElementById("mediumCost${index}").value=document.getElementById("mediumCost").value;
		window.opener.document.getElementById("checkCost${index}").value=document.getElementById("checkCost").value;
		window.opener.document.getElementById("cost${index}").value=document.getElementById("majorCost").value;
		
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
					document.getElementById("standardNum").value=node.standardnum;
					document.getElementById("unity").value=node.unity;
					document.getElementById("value").value=node.value;
					
					if(node.quotahour==null||node.quotahour == ""){
						document.getElementById("quotaHour").value=0;
						document.getElementById("quotaHours").value=0;
					}else{
						document.getElementById("quotaHour").value=node.quotahour;
						document.getElementById("quotaHours").value=node.quotahour;
					}
					if(node.mediumhour==null||node.mediumhour == ""){
						document.getElementById("mediumHour").value=0;
					}else{
						document.getElementById("mediumHour").value=node.mediumhour;
					}
					if(node.checkhour==null||node.checkhour == ""){
						document.getElementById("checkHour").value=0;
					}else{
						document.getElementById("checkHour").value=node.checkhour;
					}
					if(node.majorcost==null||node.majorcost == ""){
						document.getElementById("majorCost").value=0;
					}else{
						document.getElementById("majorCost").value=node.majorcost;
					}
					if(node.mediumcost==null||node.mediumcost == ""){
						document.getElementById("mediumCost").value=0;
					}else{
						document.getElementById("mediumCost").value=node.mediumcost;
					}
					if(node.checkcost==null||node.checkcost == ""){
						document.getElementById("checkCost").value=0;
					}else{
						document.getElementById("checkCost").value=node.checkcost;
					}
				
			}
			}
		};
		
		$(document).ready(function(){
			$.ajax({
				url:'${pageContext.request.contextPath}/ajaxapi/businrepairinfo/chooseEquipmentInfo',
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
    		<div class="span12">
     			<!-- 搜索框开始 start -->
				<div class="row-fluid">
	    			<form class="form-inline" name="myform">
		    			<input type="hidden" name="equipmentId" id="equipmentId" value=""/>
				    	<input type="hidden" name="equipmentNameParam" id="equipmentNameParam" value=""/>
				    	<input type="hidden" name="equipmentModelParam" id="equipmentModelParam" value=""/>
				    	<input type="hidden" name="standardNum" id="standardNum" value=""/>
				    	<input type="hidden" name="unity" id="unity" value=""/>
				    	<input type="hidden" name="value" id="value" value=""/>
				    	<input type="hidden" name="quotaHours" id="quotaHours" value=""/>
				    	<input type="hidden" name="quotaHour" id="quotaHour" value=""/>
				    	<input type="hidden" name="mediumHour" id="mediumHour" value=""/>
				    	<input type="hidden" name="checkHour" id="checkHour" value=""/>
				    	<input type="hidden" name="majorCost" id="majorCost" value=""/>
				    	<input type="hidden" name="mediumCost" id="mediumCost" value=""/>
				    	<input type="hidden" name="checkCost" id="checkCost" value=""/>
           			</form>
           		</div>
           		<!-- 搜索框 end -->
			   	<div class="content_wrap" style="padding-bottom:55px;">
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
