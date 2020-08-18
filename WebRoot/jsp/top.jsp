<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<%@include file="/jsp/config/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>智能信息平台</title>
		<link href="${pageContext.request.contextPath}/css/fire/global.css"
			rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/fire/layout.css"
			rel="stylesheet" type="text/css" />
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/ie6pngfix.js"></script>
		<script src="${pageContext.request.contextPath}/js/strutils.js"
			type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.js"
			type="text/javascript"></script>

		<script type="text/javascript">
			function toPage(top,left,right){
				document.getElementById("1").className="";
				//document.getElementById("3").className="";
				 //document.getElementById("2").className="";
				document.getElementById(top).className="nav_avtive";
				
				if(left.trim()!=""){
				    parent.frames["xfjLeftFrame"].location.href=left;
				}
				parent.frames["rightFrame"].location.href=right;
			}
			
			function todes(){
				var url = "${pageContext.request.contextPath}/welcome.jsp";
				window.parent.window.location.href="${pageContext.request.contextPath}/api/login/loginOut?url="+url;
			}
			function showMessage(count){
			$("#infoMesssage").html("");
			document.getElementById("infoMesssage").className="";
			if (count>0){
			  document.getElementById("infoMesssage").className="sys_message";
			  $("#infoMesssage").html("您有"+count+"条短消息未查看！");
			  }
			}
			function toupdatePwd(){
				window.showModalDialog("${pageContext.request.contextPath}/api/login/toupdatePwd?tmp=" + Math.round(Math.random() * 10000),"","dialogWidth=400px;dialogHeight=500px;");
			}
	    </script>
	</head>

	<body>
		<div class="sys_head">
			<div class="sys_hbinfo">
				<h1>
					<a title="长庆钻井机修公司-设备维修管理系统" href="#">长庆钻井机修公司-设备维修管理系统 <span></span>
					</a>
				</h1>
				<div class="sys_user_links">
<a class="dxx" href="#" onclick="javascript:toPage('1','','${pageContext.request.contextPath}/api/mailManage/getReceiveMail');"><span id="infoMesssage" class=""></span></a>
					<span class="sys_uname">欢迎您：<a href="#"
						title="Hello ${sessionScope.userMap.name}">${sessionScope.userMap.name}</a>
					</span>
					<span class="sys_pwd edit_pwd"><a href="#" onclick="toupdatePwd();" title="修改密码">修改密码</a>
					</span>
					<span class="sys_pwd logout"><a href="javascript:todes();"
						title="退出登录">退出登录</a> </span>
				</div>
			</div>

			<div class="sys_head_nav">
				<ul>
					<li id="1" class="nav_avtive">
						<a class="yw" href="#"
							onclick="javascript:toPage('1','','${pageContext.request.contextPath}/api/mywork/getMyWork');">我的工作</a>
					</li>
				<!-- <li id="2">
						<a class="wjgl" href="#"
							onclick="javascript:toPage('2','','${pageContext.request.contextPath}/jsp/comprehensive/filemanage/index.jsp');">文件共享</a>
					</li> 
					<li id="3">
						<a class="dxx" href="#"
							onclick="javascript:toPage('3','','${pageContext.request.contextPath}/api/mailManage/getReceiveMail');"></a>
					</li> -->
				</ul>
			</div>
		<iframe id='comet_iframe'
			
			style='display: none'></iframe>
			<!-- src='${pageContext.request.contextPath}/api/mailManage/getNewMails' -->
		</div>
	</body>
</html>
