<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<%@include file="/jsp/config/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<style type="text/css">
a:link {
	color: #1b1b1b;
	text-decoration: none;
}

a:visited {
	text-decoration: none;
	color: #1b1b1b;
}

a:hover {
	text-decoration: none;
	color: #0066cc;
}

a:active {
	text-decoration: none;
	color: #1b1b1b;
}






.container {width:97%;margin: 0 auto; overflow: hidden;}

.clock {width:97%; margin:0 auto; border:1px solid #333; color:#fff;}

#Date {margin:0 auto;width:100%;font-family:'BebasNeueRegular', Arial, Helvetica, sans-serif; font-size:12px;color:#cad4e9; text-align:center; text-shadow:0 0 6px #00c6ff;padding-top:8px; }

ul {margin:0 auto; width:100%; padding:0px; list-style:none; text-align:center; }
ul li {margin-top:18px;display:inline; font-size:26px;font-weight:bold;color:#fff; text-align:center; font-family:'BebasNeueRegular', Arial, Helvetica, sans-serif; text-shadow:0 0 6px #00c6ff; text-align:center; }

#point { position:relative; -moz-animation:mymove 1s ease infinite; -webkit-animation:mymove 1s ease infinite; padding-left:2px; padding-right:2px; }

@-webkit-keyframes mymove 
{
0% {opacity:1.0; text-shadow:0 0 20px #00c6ff;}
50% {opacity:0; text-shadow:none; }
100% {opacity:1.0; text-shadow:0 0 20px #00c6ff; }	
}


@-moz-keyframes mymove 
{
0% {opacity:1.0; text-shadow:0 0 20px #00c6ff;}
50% {opacity:0; text-shadow:none; }
100% {opacity:1.0; text-shadow:0 0 20px #00c6ff; }	
}
.kj_btn{
	width:140%;
	height:22px;
	position:relative;
	line-height:normal;
	margin-top:8px;
	padding-top:10px;
	background-image:;
	background-repeat:repeat-x;
}
.kj{position:absolute;top:11px;left:3px}
</style>
	
	<script type="text/javascript">
		$(document).ready(function() {
		// Create two variable with the names of the months and days in an array
		var monthNames = [ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" ]; 
		var dayNames= ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"]
		
		// Create a newDate() object
		var newDate = new Date();
		// Extract the current date from Date object
		newDate.setDate(newDate.getDate());
		// Output the day, date, month and year    
		
		
		$('#Date').html(newDate.getFullYear() + "年" + monthNames[newDate.getMonth()] + "月" + newDate.getDate() + "日  " + dayNames[newDate.getDay()]);
		
		setInterval( function() {
			// Create a newDate() object and extract the seconds of the current time on the visitor's
			var seconds = new Date().getSeconds();
			// Add a leading zero to seconds value
			$("#sec").html(( seconds < 10 ? "0" : "" ) + seconds);
			},1000);
			
		setInterval( function() {
			// Create a newDate() object and extract the minutes of the current time on the visitor's
			var minutes = new Date().getMinutes();
			// Add a leading zero to the minutes value
			$("#min").html(( minutes < 10 ? "0" : "" ) + minutes);
		    },1000);
			
		setInterval( function() {
			// Create a newDate() object and extract the hours of the current time on the visitor's
			var hours = new Date().getHours();
			// Add a leading zero to the hours value
			$("#hours").html(( hours < 10 ? "0" : "" ) + hours);
		    }, 1000);
			
		}); 
		
		
		function search(){
			var text = document.getElementById("text").value;
			if(text==""){
				alert("请输入查询条件!");
				return false;
			}
		
			var id = document.getElementsByName("radiobutton");
			var value = "";
			for (var i = 0; i < id.length; i++) {
				if (id[i].checked) {
					checked = true;
					value = id[i].value;
				}
			}
			if(value==""){
				alert("请选择搜索类型!")
				return false;
			}else{
				// value 1、设计备案 2、设计审核 3、验收备案 4、验收审核 5、监督检查 6、安全检查 7、行政处罚 8、火灾调查
				if(value==1){
					document.forms[0].action="${pageContext.request.contextPath}/api/sjsh/getDesingAuditList?apptype=0&projectName="+text;
					document.forms[0].target="_blank"
					document.forms[0].submit();
				}
				if(value==2){
					document.forms[0].action="${pageContext.request.contextPath}/api/sjsh/getDesingAuditList?apptype=1&projectName="+text;
					document.forms[0].target="_blank"
					document.forms[0].submit();
				}
				if(value==3){
					document.forms[0].action="${pageContext.request.contextPath}/api/xfys/getAcceptanceList?apptype=2&projectName="+text;
					document.forms[0].target="_blank"
					document.forms[0].submit();
				}
				if(value==4){
					document.forms[0].action="${pageContext.request.contextPath}/api/xfys/getAcceptanceList?apptype=3&projectName="+text;
					document.forms[0].target="_blank"
					document.forms[0].submit();
				}
				if(value==5){
					document.forms[0].action="${pageContext.request.contextPath}/api/aqjc/getSafetyCheckAllList?projectName="+text;
					document.forms[0].target="_blank"
					document.forms[0].submit();
				}
				if(value==6){
					document.forms[0].action="${pageContext.request.contextPath}/api/aqjc/getDesingAuditList_aqjc_dengji?projectName="+text;
					document.forms[0].target="_blank"
					document.forms[0].submit();
				}
				if(value==7){
					document.forms[0].action="${pageContext.request.contextPath}/api/xzcf/getXZCFList?projectReason="+text;
					document.forms[0].target="_blank"
					document.forms[0].submit();
				}
				if(value==8){
					document.forms[0].action="${pageContext.request.contextPath}/api/hzdc/getHzList?address="+text;
					document.forms[0].target="_blank"
					document.forms[0].submit();
				}
			}
			
			
		}
		
		
		function cc(url,id){
			var action = "";
			if(url.indexOf('?')>0){
				action= "${pageContext.request.contextPath}/"+url+"&id="+id+"&objId="+id;
			}else{
				action= "${pageContext.request.contextPath}/"+url+"?fireId="+id+"&objId="+id+"&id="+id;
			}
			document.forms[0].action = action;
		 	document.forms[0].target='_blank';
			document.forms[0].submit();
		}
		function settingCustom(){
			var i=window.showModalDialog("${pageContext.request.contextPath}/api/custom/getList?temp=" + Math.round(Math.random() * 10000),"","dialogWidth=550px;dialogHeight=500px;");
			if (1==i){
			  window.location.reload();
			}
		}
		function goExec(processId,instanceId,objId,nodeId,stepId,tableName,contTitle,url,processType,stepName){
			//document.getElementById("processId").value = processId;
		//document.getElementById("instanceId").value = instanceId;
		//document.getElementById("objId").value = objId;
		//document.getElementById("nodeId").value = nodeId;
		//document.getElementById("stepId").value = stepId;
		//document.getElementById("tableName").value = tableName;
		//document.getElementById("contTitle").value = contTitle;
		//document.getElementById("url").value = url;
		//document.getElementById("processType").value = processType;
		//document.getElementById("stepName").value = stepName;
		var toUrl = encodeURIComponent(url);
			document.forms[0].action = "${pageContext.request.contextPath }/api/mywork/goExec?temp=" + Math.round(Math.random() * 10000)+"&processId="+processId+"&instanceId="+instanceId+"&objId="+objId+"&nodeId="+nodeId+"&stepId="+stepId+"&tableName="+tableName+"&processType="+processType+"&stepName="+stepName+"&url="+toUrl+"&contTitle="+contTitle;
			//document.forms[0].action = "${pageContext.request.contextPath }/api/mywork/goExec";
			document.forms[0].target="_self"
			document.forms[0].submit();
		}
	</script>



</head>
<body>
	<form id="myWorkForm" method="post" action="">
		<div class="container-fluid">
			<div class="row-fluid">
		    	<div class="span9">
		    		<div class="page-header">
			        	<h4>待办工作</h4>
			            <div class="row-fluid">
			            	<table class="table table-striped table-bordered">
			            		<thead>
									<tr>
										<th style="text-align: right; font-weight: normal;" colspan="2">
											<a href="${pageContext.request.contextPath}/api/mywork/getMyWorkUserList">更多>></a>
										</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items='${web:getWorkFlow(sessionScope.userMap)}'
										var="a" begin="0" end="20" >
										<tr>
											<c:if test="${fn:length(a.contTitle)>=70}">
												<td colspan="3" >
													<ol><li><a href="#" title="${a.contTitle}流程的${a.stepName}步骤是否现在处理！" onclick='goExec("${a.processId}",${a.instanceId},"${a.objId}",${a.nodeId},${a.stepId},"${a.tableName}","${a.contTitle}","${a.url}",${a.processType},"${a.stepName}");'>${a.contTitle}流程的${a.stepName}步骤是否现在处理！</a></li></ol>
												</td>
											</c:if>
											<c:if test="${fn:length(a.contTitle)<70}">
												<td colspan="3">
													<a href="#" title="${a.contTitle}流程的${a.stepName}步骤是否现在处理！" onclick='goExec("${a.processId}",${a.instanceId},"${a.objId}",${a.nodeId},${a.stepId},"${a.tableName}","${a.contTitle}","${a.url}",${a.processType},"${a.stepName}");'>${a.contTitle}流程的${a.stepName}步骤是否现在处理！</a>
												</td>
											</c:if>
										</tr>
									</c:forEach>
								</tbody>
			            	</table>
			            </div>
			        </div> 
		    	</div>
		    </div>
		</div>
	</form>
<!--  <td valign="top">
       <!---快捷按钮 begin-
			<div class="quickblock">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<thead>
						<tr>
							<th width="85%" colspan="4">
								快捷按钮
							</th>
							<th style="text-align: right; font-weight: normal;">
								<span style="float:right"><a href="#" onclick="settingCustom();">设置>></a></span>
							</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="a"
							items='${web:getMyCustom(sessionScope.userMap.userId)}' varStatus="status">
								<c:if test="${(status.index+1)%3==1}"><tr></c:if> 
								<td>
									<div>
									        <div>
											   <a href="${pageContext.request.contextPath}${a.url}"  onclick="doSubmit();" title="${a.auth_name}">${fn:substring(a.auth_name,0,8)}</br>
											   <c:if test="${fn:length(a.auth_name)>8}">
											   		<a href="${pageContext.request.contextPath}${a.url}"  onclick="doSubmit();" title="${a.auth_name}">${fn:substring(a.auth_name,8,fn:length(a.auth_name))}</a>
												</c:if>
											</div>
									  </div>
								</td>
								<c:if test="${(status.index+1)%3==0}"></tr></c:if> 
						</c:forEach>
					</tbody>
				</table>
			</div>
    </td> -->   
	</body>
</html>
