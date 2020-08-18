<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>基础组织结构</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/zTreeStyle/zTreeStyle.css" type="text/css"></link>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ztree.core-3.5.js"></script>
	<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />

	<script type="text/javascript">
		function goAddFirstAuth() {
			document.forms[0].parentId.value = "-1";
			document.forms[0].flag.value = "0";
			toSave();
		}
		function toSave(){
		         document.form.action="${pageContext.request.contextPath}/api/workflow/toAddProcessDirectory?flag=0";
	             document.form.submit();
		}
		function toSaveMx(){
		         document.form.action="${pageContext.request.contextPath}/api/workflow/toAddProcessDirectory?flag=1";
	             document.form.submit();
		}
		function toUpdate(isUpProCon){
		         document.form.action="${pageContext.request.contextPath}/api/workflow/toUpdateProcess?isUpProCon="+isUpProCon;
	             document.form.submit();
		}
		
		function toDelete(){
			if(confirm("请确定删除此信息吗？")){
		         document.form.action="${pageContext.request.contextPath}/api/workflow/deleteProcess";
		        document.form.submit();
			}
		}
		
		
		var setting = {
			view: {
				showLine: false
			},
			data:{
				key:{
					children:'childlist',
					name:'strname',
					url:'eurl'
				}
			},
			callback:{
				onClick:function(e,id,node){
					var flag = node.flag;
					var parentId = node.parentid;
					if(parentId==-1){
						document.getElementById("add").style.display = "inline";
						
						document.getElementById("updateMx").style.display = "none";
						document.getElementById("update").style.display = "inline";
						document.getElementById("delete").style.display = "inline";
						document.getElementById("addMx").style.display = "none";
						document.getElementById("updatePC").style.display = "none";
					}
					if(flag==0&&parentId!=-1){
						document.getElementById("add").style.display = "none";
						document.getElementById("update").style.display = "inline";
						document.getElementById("delete").style.display = "inline";
						document.getElementById("addMx").style.display = "inline";
						document.getElementById("updateMx").style.display = "none";
						document.getElementById("updatePC").style.display = "none";
					}else if(flag==1&&parentId!=-1){
						document.getElementById("add").style.display = "none";
						document.getElementById("addMx").style.display = "inline";
						document.getElementById("update").style.display = "none";
						document.getElementById("updateMx").style.display = "inline";
						document.getElementById("delete").style.display = "inline";
						document.getElementById("updatePC").style.display = "inline";
					}
				   document.forms[0].parentId.value = node.processid;
				   document.forms[0].goParentId.value = parentId;
				   document.forms[0].flag.value = node.flag;
				   document.forms[0].processId.value = node.processid;
				   window.parent.frames["rightProcess"].location.href = "${pageContext.request.contextPath}/api/workflow/getProcessInfo?tempPat=0&processId="+node.processid;
				}
			}
		};
		$(document).ready(function(){
			$.ajax({
				url:'${pageContext.request.contextPath}/ajaxapi/workflow/getProcessManageList',
				type:'post',
				dataType:'json',
				success:function(data, textStatus, jqXHR){
					$.fn.zTree.init($("#treeDemo"), setting, data.processlist);
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
	<div class="tree_view_button">
		<ul>
			<li>
				<a href="#"  class="tbut" id="add" onclick="toSave()" style="display: none;" ><span class="u">添加目录</span></a>
			</li>
			<li>
                <a href="#"  class="tbut" id="addMx" onclick="toSaveMx()" style="display: none;" ><span class="u">添加</span></a>
			</li>
			<li>
				<a href="#"  class="tbut" id="update" onclick="toUpdate('0')" style="display: none;" ><span class="u">编辑目录</span></a>
			</li>
			<li>
				<a href="#"  class="tbut" id="updateMx" onclick="toUpdate('1')" style="display: none;" ><span class="u">编辑</span></a>
			</li>
			<li>
				<a href="#"  class="tbut" id="delete" onclick="toDelete()" style="display: none;" ><span class="u">删除</span></a>
			</li>
			<li>
				<a href="#"  class="tbut" id="updatePC" onclick="toUpdate('2')" style="display: none;" ><span class="u">编辑流程图</span></a>
			</li>
		</ul>
	</div>
   <table cellspacing="0" cellpadding="0" class="treeblock_in" width="100%" >
		<tr>
			<th colspan="2" class="treeblock_th">流程管理
				<c:if test="${empty processList}">
					<span>
						<a href="#" onclick="goAddFirstAuth()" title="添加当前级别的流程管理节点">添加根</a>
					</span>
		 	    </c:if>
			</th>
		</tr>
		<tr>
			<td class="treeblock_td">
				<table cellspacing="1" cellpadding="0" class="table_box table_add">
				      <tr>
						<td class="rtext">
							<div class="content_wrap">
								<div class="zTreeDemoBackground left">
									<ul id="treeDemo" class="ztree"></ul>
								</div>
							</div>
						</td>
					  </tr>
				</table>
		      </td>
			</tr>
			<tr>
			  <td class="treeblock_td">
				<div class="tree_but">
					 <div class="ee">
						<form action="" name="form" id="form" method="post" target="rightProcess">
					 		 <input type="hidden" name="parentId" value="" id="parentId"/>
					 		 <input type="hidden" name="flag" value="" id="flag"/>
					 		 <input type="hidden" name="processId"  value="" id="processId"/>
					 		 <input type="hidden" name="goParentId"  value="" id="goParentId"/>
							<input type="hidden" name="url" id="url" value = "${pageContext.request.contextPath}/api/workflow/getProcessInfo"/>
						</form>
					  </div>
				</div>
			  </td>
			</tr>
	</table>
</div>
</body>
</html>
