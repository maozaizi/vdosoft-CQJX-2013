<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>长庆钻井机修公司-设备维修管理系统</title>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js" ></script>	
	<script src="${pageContext.request.contextPath}/js/MzTreeView12/MzTreeView12.js" type="text/javascript"></script>	
  </head>
  <body>
  	<div id="kkk" style="width:100%;height:100%" ></div>
	<script language="javascript" type="text/javascript">
		var MzTreeViewTH="<table class='MzTreeViewRow'><tr><td>";
		var MzTreeViewTD="\"<input type='hidden' name='contId' value='\"+ sid +\"' /></td></tr></table></tr></table>\"";
		window.tree = new MzTreeView("tree");
		tree.setIconPath("${pageContext.request.contextPath}/images/MzTreeView12/"); //可用相对路径
		tree.N["0_pro"] = "";
		//tree.N["a_pro"] = "T:列表 ;C:goOperator('0','0','0','1')";
		<tag:organizationDetaliTree list="${organizationDetaliList}" parentId="pro"></tag:organizationDetaliTree>
		tree.setURL("#");
		tree.wordLine = false;
		tree.setTarget("main");
		document.getElementById("kkk").innerHTML=tree.toString();
		tree.expandAll();
		function goOperator(parentId,selfId,name){
				var parent = window.opener.document;
				parent.getElementById("orangaizationName").value = name;
				parent.getElementById("orangaizationNameId").value = selfId;
				window.close();
		}	
	</script>
  </body>
</html>
