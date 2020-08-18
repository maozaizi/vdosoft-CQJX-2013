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
<title>基础组织结构</title>
<link href="${pageContext.request.contextPath }/css/fire/global.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath }/css/fire/layout.css" rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/js/MzTreeView12/MzTreeView12.js" type="text/javascript"></script>		
</head>
<body>
<style> 
a.MzTreeview /* TreeView 链接的基本样式 */ { cursor: hand; color: #000080; margin-top: 5px; padding: 2 1 0 2; text-decoration: none; }
.MzTreeview a.select /* TreeView 链接被选中时的样式 */ { color: highlighttext; background-color: highlight; }
#kkk input {
vertical-align:middle;
}
.MzTreeViewRow {border:none;width:500px;padding:0px;margin:0px;border-collapse:collapse}
.MzTreeViewCell0 {border-bottom:1px solid #CCCCCC;padding:0px;margin:0px;}
.MzTreeViewCell1 {border-bottom:1px solid #CCCCCC;border-left:1px solid #CCCCCC;width:200px;padding:0px;margin:0px;}
</style>
	<div class="MainContent"  >
	<h3 class="cBlack">基础组织结构</h3>
		<div style="height:42px;">
			温馨提示：在选中组织名称后，点击确定按钮选择组织，或者双击组织名称选择组织。
		</div>
	</div>
	<div class="MainContent"  style="overflow:scroll; height:530px;">
		<div class="ListBlock">
		<div>
			
		<table cellspacing="1" cellpadding="0" class="table_box table_add">
		      <tr>
				<td class="rtext" >
					<div id="kkk" align="left"></div>
					<script language="javascript" type="text/javascript">
					var tree = new MzTreeView("tree");
					tree.icons["bing"]="bing.gif";
					tree.icons["shi"]="shi.gif";
					tree.icons["ken"]="ken.gif";
					tree.icons["tuan"]="tuan.gif";
					tree.icons["fopen"]="fopen.gif";
					tree.setIconPath("${pageContext.request.contextPath}/images/MzTreeView12/"); //可用相对路径
					tree.N["0_-1"] = "";
					//tree.N["a_pro"] = "T:列表 ;C:goOperator('0','0','0','1')";
					${tree:getTree(baseOrganizationList,"-1",pageContext,cb)}
					tree.setURL("#");
					tree.wordLine = false;
					tree.setTarget("main");
					document.getElementById("kkk").innerHTML=tree.toString();
					tree.focus('BT.101');
					tree.focus('BT');
					tree.dblClickHandle=function(){
						getValues();
					}
					function goOperator(id,parentId,name){
						if(""!=id&&null!=id&&"undefine"!=typeof(id)){
							document.getElementById('orgId').value = id;
							document.getElementById('orgName').value = name;
						}
					}
					function getValues() {
						var id = document.getElementById('orgId').value;
						var name = document.getElementById('orgName').value;
						if(id=="" || name==""){
							alert('请选择组织结构');
							return false;
						}
					    var idAndName = new Array();
						idAndName.push(id);
						idAndName.push(name);
						window.returnValue = idAndName; 
						window.close();
					}
					</script>
			  	 	<form action="" name="form" id="form" method="post">
			  	 		<input type="hidden" name="orgId" value="{BT}" id="orgId"/>
			  	 		<input type="hidden" name="orgName" value="兵团" id="orgName"/>
			  	 	</form>
				</td>
			  </tr>
			</table> 
			</div>
		</div>
	</div>
	<div class="MainContent"  >
		<div   class="BigBtn xuzhi_btn" style="height:40px;align:center;margin-top:5px;margin-left:175px;"><a class="Bbtn" href="#" onclick="getValues()"><span class="Br">确定</span></a></div>
	</div>
</body>
</html>
