<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp" %>
<!DOCTYPE html>
<html>
  <head>
    <title>right</title>
	<link href="${pageContext.request.contextPath }/css/bootstrap.min.css" rel="stylesheet" media="screen" />
	<script src="${pageContext.request.contextPath }/js/jquery.1.7.2.min.js"></script>
	<script src="${pageContext.request.contextPath }/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath }/js/strutils.js"></script>
	<style type="text/css">
		div.white_content{width:500px;background-color:#ced9e7;background-image:url("images/del_layer_bg2.png");background-repeat:repeat-y}
		span.t,span.b{display:block;width:500px;height:15px}
		span.t{background-image:url("images/del_layer_bg1.png");background-repeat:no-repeat;background-position:0 0}
		span.b{background-image:url("images/del_layer_bg3.png");background-repeat:no-repeat;background-position:0 0}
		div.del_reason{width:480px;margin:0 auto}
		div.del_btn{width:140px;margin:10px auto}
		div.del_reason table{border:solid #99bbe8 1px}
		div.del_reason th{height:24px;line-height:24px;padding-left:6px;border-bottom:1px solid #bebebe;background-image:url("images/list_titlebg.png");background-repeat:repeat-x}
		div.del_reason td{border-bottom:1px solid #ededed;background-color:#fff;padding:6px}
		.black_overlay{display:none;position:absolute;top:0%;left:0%;width:100%;height:100%;background-color:black;z-index:1001;-moz-opacity:0.8;opacity:.80;filter: alpha(opacity=80)}  
		.white_content {display:none;position: absolute;top:20%;left:25%;z-index:1002;overflow: auto}
	</style>
    <script type="text/javascript">
		$(document).ready(function (){
			$("#toModifyUserInfo").click(function(){
				var f=false;  
				$("input[id=userId]:checked").each(function (i){f=true;});
		        if(f){
					$("#userInfoList").attr("action","${pageContext.request.contextPath}/api/org/toModifyUserInfo").submit();
				}else{
					alert("请选择要修改的人员信息！");
				}
			});
			$("#toDeleteUserInfo").click(function(){
				var f=false;
			    var selectValue = "";
				$("input[id=userId]:checked").each(function (i,e){
					f=true;
					selectValue = $(e).val();
				});
		        if(f){
		        	$("#delform").attr("action","${pageContext.request.contextPath}/api/org/deleteUserInfo");
					showDelReason();
					$("#delPersonnelId").attr("value",selectValue);
				}else{
					alert("请选择要作废的人员信息！");
					return;
				}
			});
			
			$("#toSetUserLogin").click(function(){
				var f=false;  
				$("input[id=userId]:checked").each(function (i){f=true;});
		        if(f){
					$("#userInfoList").attr("action","${pageContext.request.contextPath}/api/org/toSetUserLogin").submit();
				}else{
					alert("请选择要设置的人员信息！");
				}
			});
			
			$("#toModifyPostInfo").click(function(){
				var f=false;  
				$("input[id=postId]:checked").each(function (i){f=true;});
		        if(f){
					$("#postInfoList").attr("action","${pageContext.request.contextPath}/api/org/toModifyPostInfo").submit();
				}else{
					alert("请选择要修改的岗位信息！");
				}	
			});
			$("#toDeletePostInfo").click(function(){
				var f=false;
			    var selectValue = "";
				$("input[id=postId]:checked").each(function (i,e){
					f=true;
					selectValue = $(e).val();
				});
		        if(f){
		        	$("#delform").attr("action","${pageContext.request.contextPath}/api/org/deletePostInfo");
					showDelReason();
					$("#delPostId").attr("value",selectValue);
				}else{
					alert("请选择要修改的岗位信息！");
					return;
				}
			});
			
			function showDelReason(){
				document.getElementById('delete_reason').style.display='block';
				document.getElementById('fade').style.display='block'; 
			}
			
			$("#deleteBtn").click(function(){
				var reason = document.getElementById("reason").value;
				if(reason == ""){
					alert("请输入作废原因！");
					return false;
				}
				if(confirm('请确认要作废吗？')){
				   var delform = document.getElementById("delform");
					delform.submit();
				}
			});
			$("#delCloseBtn").click(function(){
				closeDelReason();
			});
		});
		function refreshTree(){
			var isRefresh="${param.isRefresh=='1'?'1':''}";
			if(isRefresh==1){
				window.parent.frames["app_leftFrame"].location.reload();
			}
		}
    </script>
  </head>
  
  <body onload="refreshTree()">
	<div class="MainContent">
		<div class="ListBlock">
		    <h3>基本信息</h3>
		    <div class="EditBox">
	  		<table class="table table-hover table-bordered">
	  	  	<tr>
	  	  	<th  colspan="2">基本信息</th>
	  	  	</tr>
			  <tr>
				<td class="EditBox_td1">名称：</td>
				<td>
				  	${baseorganization.orgName }
				</td>
			  </tr>
			  <tr>
				<td class="EditBox_td1">创建时间：</td>
				<td>
					${fn:substring(baseorganization.createDate,0,10)}
				</td>
			  </tr>
		</table>
		</div>
		
		<div class="List">
		<form action="" id="userInfoList">
		  <h3>人员信息</h3>
	  	  <table class="table table-hover table-bordered">
	  	  	<thead>
			<tr>
				<th width="25%"  class="List_Th">姓名</th>
	        	<th width="25%"  class="List_Th">性别</th>
	        	<th width="25%"  class="List_Th">职务</th>
	        	<th width="25%"  class="List_Th">备注</th>
	        </tr>
	        </thead>
			<c:forEach items="${userInfoList}" var="item">
				<tr>
		        	<td bordercolor="1" >
		        		<input type="radio" name="userId" id="userId" value="${item.userId }" title="${item.userId }"/>
		        		${item.name }
		        	</td>
			      	<td  >${item.gender }</td>
			      	<td  >${item.position}</td>
			      	<td  >${item.remark }</td>
			    </tr>
			</c:forEach>
		</table>
		<a class="btn" href="#" id="toSetUserLogin">设置</a>
		<a class="btn" href="${pageContext.request.contextPath}/api/org/toAddUserInfo?orgId=${baseorganization.orgCode }">添加</a>
		<a class="btn" href="#" id="toModifyUserInfo">修改</a>
		<a class="btn" href="#" id="toDeleteUserInfo">作废</a>
		</form>
		</div>
		
		<form action="" id="postInfoList">
		<div class="List">
		  <h3>岗位信息</h3>
	  	  <table class="table table-hover table-bordered">
	  	  	<thead>
			<tr>
				<th width="40%">名称</th>
	        	<th width="40%">职责</th>
	        	<th width="20%">操作</th>
	        </tr>
	        </thead>
	        <tbody>
	        <c:forEach items="${postInfoList}" var="item">
				<tr>
			      	<td><input type="radio" name="postId" id="postId" value="${item.postId }" title="${item.postId }"/>
			      		${item.postName }
			      	</td>
			      	<td>${item.postDuties }</td>
			      	<td><a href="${pageContext.request.contextPath}/api/org/toManagerPost?postId=${item.postId}" class="btn" ><span class="r">管理</span></a></td>
			    </tr>
			</c:forEach>
			</tbody>
		</table>
		<a class="btn" href="${pageContext.request.contextPath}/api/org/toAddPostInfo?orgId=${baseorganization.orgCode }"><span class="r">添加</span></a>
		<a class="btn" href="#" id="toModifyPostInfo"><span class="r">修改</span></a>
		<a class="btn" href="#" id="toDeletePostInfo"><span class="r">作废</span></a>
  		</div>
		</form>
  		
    </div>
</div>
	<form id="delform" method="post">
		<input type="hidden" id="delPersonnelId" name="userId" />
		<input type="hidden" id="delPostId" name="postId" />
		<input type="hidden" id="tempUrl" name="tempUrl" value="${pageContext.request.contextPath}/api/org/getInfo?orgCode=${baseorganization.orgCode }"/>
		<%@include file="/jsp/public/delete_layout.jsp" %>
	</form>
  </body>
</html>
