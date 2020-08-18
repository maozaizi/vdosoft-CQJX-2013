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
<title>组织结构</title>
<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
<style>
.black_overlay {display: none;position: absolute;top: 0%;left: 0%;width: 100%;height: 100%;background-color: black;z-index: 1001;-moz-opacity: 0.8;opacity: .80;filter: alpha(opacity = 80);}
.white_content {display: none;position: absolute;top: 25%;left: 25%;width: 50%;height: 50%;padding: 16px;border: 16px solid orange;background-color: white;z-index: 1002;overflow: auto;}
a.MzTreeview /* TreeView 链接的基本样式 */ { cursor: hand; color: #000080; margin-top: 5px; padding: 2 1 0 2; text-decoration: none; }
.MzTreeview a.select /* TreeView 链接被选中时的样式 */ { color: highlighttext; background-color: highlight; }
#kkk input {}
.MzTreeViewRow {border:none;width:200px;padding:0px;margin:0px;border-collapse:collapse}
.MzTreeViewCell0 {border-bottom:1px solid #CCCCCC;padding:0px;margin:0px;}
.MzTreeViewCell1 {border-bottom:1px solid #CCCCCC;border-left:1px solid #CCCCCC;width:200px;padding:0px;margin:0px;}
</style>
<script src="${pageContext.request.contextPath}/js/MzTreeView12/MzTreeView12.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>		
<script type="text/javascript">
	function toSynchro(){
		if(confirm("请确定同步组织信息吗？")){
	        document.form.action="${pageContext.request.contextPath}/api/organizationdetali/synchro";
	        document.form.target="app_leftFrame";
	        document.form.submit();
		}
	}
		
</script>
</head>
<body>
<div class="treeblock_out" >
   <table cellspacing="0" cellpadding="0" class="treeblock_in" width="100%" >
	<tr>
		<th colspan="2" class="treeblock_th">
			组织结构
		</th>
	</tr>
	<tr>
		<td class="treeblock_td">

	<table cellspacing="1" cellpadding="0" class="table_box table_add">
	      <tr>
			<td class="rtext">
				<div id="kkk" align="left"></div>
				<script language="javascript" type="text/javascript">
				var MzTreeViewTH="<table class='MzTreeViewRow'><tr><td>";
				var MzTreeViewTD="\"<input type='hidden' name='contId' value='\"+ sid +\"' /></td></tr></table></tr></table>\"";
				window.tree = new MzTreeView("tree");
				tree.setIconPath("${pageContext.request.contextPath}/images/MzTreeView12/"); //可用相对路径
				tree.N["0_pro"] = "";
				//tree.N["a_pro"] = "T:列表 ;C:goOperator('0','0','0','1')";
				${tree:getTree(organizationDetaliList,"pro",pageContext,cb)}
				tree.setURL("#");
				tree.wordLine = false;
				tree.setTarget("main");
				document.getElementById("kkk").innerHTML=tree.toString();
				<c:if test="${not empty expandId}">
					<c:if test = "${expandId == -1}">
						tree.focus("0");
					</c:if>
					<c:if test = "${expandId != -1}">
						tree.focus("${expandId}");
					</c:if>
				</c:if>
				tree.expandAll();
				function goOperator(parentId,selfId,app_orgName,baseOrgId,nodeType){
						$("#nodeType").attr("value",nodeType);
						$('#parentId').attr("value",parentId);
						$('#organizationDetailId').attr("value",selfId);
						$('#baseOrganizationId').attr("value",baseOrgId);
				}	
				</script>
		  	 	<form action="" name="form" id="form" method="post"  target="app_rightFrame" >
		  	 		<input type="hidden" name="parentId" id="parentId"/>
		  	 		<input type="hidden" name="organizationDetailId" id="organizationDetailId"/>
		  	 		<input type="hidden" name="baseOrganizationId" id="baseOrganizationId"/>
		  	 		<input type="hidden" name="type" id="type" value="${param.type }"/>
					<input type="hidden" name="tempUrl" id="tempUrl" value = "${pageContext.request.contextPath}/api/organizationdetali/list"/>
					<input type="hidden" name="nodeType" id="nodeType"/>
		  	 	</form>
			</td>
		  </tr>
		</table> 
</div>
</body>
</html>
