<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<%@include file="/jsp/config/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="${pageContext.request.contextPath}/css/fire/global.css"
			rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/fire/layout.css"
			rel="stylesheet" type="text/css" />
		<!-- \\ 此处引用YUI version: 2.3.1 \\ -->
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/yui/build/fonts/fonts-min.css" />
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/yui/build/calendar/assets/skins/sam/calendar.css" />
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/yui/build/yahoo-dom-event/yahoo-dom-event.js"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.js"
			type="text/javascript"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/yui/build/calendar/calendar.js"></script>
		
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.2.6.min.js"></script>
		
		<script type="text/javascript">
			function goExec(processId,instanceId,objId,nodeId,stepId,tableName,contTitle,url,processType,stepName){
				var toUrl = encodeURIComponent(url);
					document.forms[0].action = "${pageContext.request.contextPath }/api/mywork/goExec?temp=" + Math.round(Math.random() * 10000)+"&processId="+processId+"&instanceId="+instanceId+"&objId="+objId+"&nodeId="+nodeId+"&stepId="+stepId+"&tableName="+tableName+"&contTitle="+contTitle+"&url="+toUrl+"&processType="+processType+"&stepName="+stepName;
					document.forms[0].submit();
				}
			function settingCustom(){
				var i=window.showModalDialog("${pageContext.request.contextPath}/api/custom/getList?temp=" + Math.round(Math.random() * 10000),"","dialogWidth=400px;dialogHeight=500px;");
				if (1==i){
				  window.location.reload();
				}
			}
		</script>
		
		
		<script type="text/javascript">
    
        $(document).ready(function() {
         
              setInterval( function() {
              var seconds = new Date().getSeconds();
              var sdegree = seconds * 6;
              var srotate = "rotate(" + sdegree + "deg)";
              
              $("#sec").css({"-moz-transform" : srotate, "-webkit-transform" : srotate});
                  
              }, 1000 );
              
         
              setInterval( function() {
              var hours = new Date().getHours();
              var mins = new Date().getMinutes();
              var hdegree = hours * 30 + (mins / 2);
              var hrotate = "rotate(" + hdegree + "deg)";
              
              $("#hour").css({"-moz-transform" : hrotate, "-webkit-transform" : hrotate});
                  
              }, 1000 );
        
        
              setInterval( function() {
              var mins = new Date().getMinutes();
              var mdegree = mins * 6;
              var mrotate = "rotate(" + mdegree + "deg)";
              
              $("#min").css({"-moz-transform" : mrotate, "-webkit-transform" : mrotate});
                  
              }, 1000 );
         
        }); 
    </script>

<!-- \\ 此处引用钟表样式 -Begin \\ -->
 	<style type="text/css">
        * {
        	margin: 0;
        	padding: 0;
        }
        
        #clock {
        	position: relative;
        	width: 190px;
        	height: 190px;
        	margin: 20px auto 0 auto;
        	background: url(${pageContext.request.contextPath}/images/clock/clockface.jpg);
			background-repeat:no-repeat;
        	list-style: none;
        	}
        
        #sec, #min, #hour {
        	position: absolute;
        	width: 10px;
        	height: 190px;
        	top: 0px;
        	left: 90px;
        	}
        
        #sec {
        	background: url(${pageContext.request.contextPath}/images/clock/sechand.png);
        	z-index: 3;
           	}
           
        #min {
        	background: url(${pageContext.request.contextPath}/images/clock/minhand.png);
        	z-index: 2;
           	}
           
        #hour {
        	background: url(${pageContext.request.contextPath}/images/clock/hourhand.png);
        	z-index: 1;
           	}
    </style>	

<!-- \\ 此处引用钟表样式 -End \\ -->

	
<!-- \\ 此处引用YUI 2.3.1 -Begin \\ -->
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
</style>
<!-- \\ 此处引用YUI 2.3.1 -End \\ -->

	</head>
	<body class="yui-skin-sam">
		<div class="user_workbench_grid">
			<div class="benchblock">
				<div id="cal1Container"></div>
				<script type="text/javascript">
				YAHOO.namespace("example.calendar");
				YAHOO.example.calendar.init = function() {
					YAHOO.example.calendar.cal1 = new YAHOO.widget.Calendar("cal1","cal1Container");
					
					/*本地化语言*/
					YAHOO.example.calendar.cal1.cfg.setProperty("MY_LABEL_YEAR_POSITION", 1); 
					YAHOO.example.calendar.cal1.cfg.setProperty("MY_LABEL_MONTH_POSITION", 2);
					YAHOO.example.calendar.cal1.cfg.setProperty("MY_LABEL_YEAR_SUFFIX", "年");
					YAHOO.example.calendar.cal1.cfg.setProperty("MY_LABEL_MONTH_SUFFIX", "月");
					YAHOO.example.calendar.cal1.cfg.setProperty("MONTHS_LONG", ["一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"]);
					YAHOO.example.calendar.cal1.cfg.setProperty("WEEKDAYS_SHORT", ["日", "一", "二", "三", "四", "五", "六"]); 
					/*本地化语言*/
					//加颜色
					
					var list=${myworklist}.list;
					for(var i=0;i<list.length;i++){
						date=list[i];
						if (date.programimportance=="3"){
							YAHOO.example.calendar.cal1.addRenderer(date.programdate, YAHOO.example.calendar.cal1.renderCellStyleHighlight3);
						}else if (date.programimportance=="2"){
							YAHOO.example.calendar.cal1.addRenderer(date.programdate, YAHOO.example.calendar.cal1.renderCellStyleHighlight2);
						}else{
							YAHOO.example.calendar.cal1.addRenderer(date.programdate, YAHOO.example.calendar.cal1.renderCellStyleHighlight1);
						}
					}
					//事件
					var mySelectHandler = function(type,args,obj) {
						 var dates = args[0]; 
						 var date = dates[0]; 
						 var year = date[0], month = date[1], day = date[2]; 
						 
						 var today  = year+'-'+month+'-'+day;
						 window.parent.frames['3'].location.href="${pageContext.request.contextPath}/api/program/listByDate?programDate="+today;
						 
					};
					var myChangePageHandler = function(type,args,obj) {
						var cfgPageDate = YAHOO.widget.Calendar._DEFAULT_CONFIG.PAGEDATE.key;
					  	var currdate=this.cfg.getProperty(cfgPageDate);
				        var strdate=currdate.getMonth()+1+"/"+currdate.getFullYear();
					  	var data = {"date":strdate};
						$.ajax({
							url:'${pageContext.request.contextPath}/ajaxapi/program/getListMyWork',
							data:data,
							dataType:'json',
							success:function(data,status,jaxData){
							    var list=data.list;
								for(var i=0;i<list.length;i++){
								  date=list[i];
								  if (date.programimportance=="3"){
								  	YAHOO.example.calendar.cal1.addRenderer(date.programdate, YAHOO.example.calendar.cal1.renderCellStyleHighlight3);
								  }else if (date.programimportance=="2"){
								  	YAHOO.example.calendar.cal1.addRenderer(date.programdate, YAHOO.example.calendar.cal1.renderCellStyleHighlight2);
								  }else{
								  	YAHOO.example.calendar.cal1.addRenderer(date.programdate, YAHOO.example.calendar.cal1.renderCellStyleHighlight1);
								  }
								}
								YAHOO.example.calendar.cal1.render();
							},
							error:function(){
								alert('获取我的日程失败！');
							}
						});
					};
					YAHOO.example.calendar.cal1.selectEvent.subscribe(mySelectHandler, YAHOO.example.calendar.cal1, true);
					YAHOO.example.calendar.cal1.changePageEvent.subscribe(myChangePageHandler, YAHOO.example.calendar.cal1, true);
					YAHOO.example.calendar.cal1.render();
				}
				YAHOO.util.Event.onDOMReady(YAHOO.example.calendar.init);
			</script>
			</div>
			
			
			<div class="benchblock">
				<ul id="clock">	
				   	<li id="sec"></li>
				   	<li id="hour"></li>
					<li id="min"></li>
				</ul>
			</div>
			
			
			<c:if test="${!empty web:getWorkFlow(sessionScope.userMap)}">
				<form id="myWorkForm" method="post" action="">
					<!---工作流 begin--->
					<div class="benchblock">
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<thead>
								<tr>
									<th width="85%">
										工作流
									</th>
									<th style="text-align: right; font-weight: normal;">
										<a
											href="${pageContext.request.contextPath}/api/mywork/getMyWorkUserList">更多>></a>
									</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items='${web:getWorkFlow(sessionScope.userMap)}'
									var="a" begin="0" end="4">
									<tr>
										<c:if test="${fn:length(a.contTitle)>=26}">
											<td colspan="2">
												<a href="#" title="${a.contTitle}流程的${a.stepName}步骤是否现在处理！"
													onclick='goExec("${a.processId}",${a.instanceId},"${a.objId}",${a.nodeId},${a.stepId},"${a.tableName}","${a.contTitle}","${a.url}",${a.processType},"${a.stepName}");'>${fn:substring(a.contTitle,0,25)}...</a>
											</td>
										</c:if>
										<c:if test="${fn:length(a.contTitle)<26}">
											<td colspan="2">
												<a href="#" title="${a.contTitle}流程的${a.stepName}步骤是否现在处理！"
													onclick='goExec("${a.processId}",${a.instanceId},"${a.objId}",${a.nodeId},${a.stepId},"${a.tableName}","${a.contTitle}","${a.url}",${a.processType},"${a.stepName}");'>${fn:substring(a.contTitle,0,25)}...</a>
											</td>
										</c:if>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</form>
				<!---工作流 end--->
			</c:if>

			<c:if
				test="${!empty web:getMyTask(sessionScope.userMap.userId,5)}">
				<!---我的任务 begin--->
				<div class="benchblock">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<thead>
							<tr>
								<th width="85%">
									我的任务
								</th>
								<th style="text-align: right; font-weight: normal;">
									<a href="${pageContext.request.contextPath}/api/task/list">更多>></a>
								</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="a"
								items='${web:getMyTask(sessionScope.userMap.userId,5)}'>
								<tr>
									<c:if test="${fn:length(a.contTitle)>=26}">
										<td colspan="2">
											<a
												href="${pageContext.request.contextPath}/api/task/get?id=${a.id}">${fn:substring(a.contTitle,0,26)}...</a>
										</td>
									</c:if>
									<c:if test="${fn:length(a.contTitle)<26}">
										<td colspan="2">
											<a
												href="${pageContext.request.contextPath}/api/task/get?id=${a.id}">${a.contTitle}</a>
										</td>
									</c:if>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<!---我的任务 end--->
			</c:if>

			<c:if
				test="${!empty web:getMyMail(sessionScope.userMap.userId,5)}">
				<!---我的收件箱 begin--->
				<div class="benchblock">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<thead>
							<tr>
								<th width="85%">
									我的收件箱
								</th>
								<th style="text-align: right; font-weight: normal;">
									<a
										href="${pageContext.request.contextPath}/api/mailManage/getReceiveMail">更多>></a>
								</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="a"
								items='${web:getMyMail(sessionScope.userMap.userId,5)}'>
								<tr>
									<c:if test="${fn:length(a.contTitle)>=26}">
										<td colspan="2">
											<a
												href="${pageContext.request.contextPath}/api/mailManage/getReceiveDetail?id=${a.ID}&url=${pageContext.request.contextPath}/api/program/getListMyWork">${fn:substring(a.contTitle,0,26)}...</a>
										</td>
									</c:if>
									<c:if test="${fn:length(a.contTitle)<26}">
										<td colspan="2">
											<a
												href="${pageContext.request.contextPath}/api/mailManage/getReceiveDetail?id=${a.ID}&url=${pageContext.request.contextPath}/api/program/getListMyWork"">${a.contTitle}</a>
										</td>
									</c:if>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<!---我的收件箱 end--->
			</c:if>

			<!---快捷按钮 begin--->
			<div class="quickblock">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<thead>
						<tr>
							<th width="85%" colspan="4">
								快捷按钮
							</th>
							<th style="text-align: right; font-weight: normal;">
								<span style="float: right"><a href="#"
									onclick="settingCustom();">设置>></a>
								</span>
							</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="a"
							items='${web:getMyCustom(sessionScope.userMap.userId)}'
							varStatus="status">
							<c:if test="${(status.index+1)%4==1}">
								<tr>
							</c:if>
							<td>
								<div class="opt_btn">
									<div class="yy">
										<a href="${pageContext.request.contextPath}${a.url}"
											class="btn" onclick="doSubmit();" title="${a.auth_name}"><span
											class="r">${a.auth_name}</span>
										</a>
									</div>
								</div>
							</td>
							<c:if test="${(status.index+1)%4==0}">
								</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</body>
</html>
