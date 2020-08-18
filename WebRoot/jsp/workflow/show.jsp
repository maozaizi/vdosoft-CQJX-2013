<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>流程信息</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	
		
				<!-- Bootstrap -->
		<link href="${pageContext.request.contextPath }/css/bootstrap.min.css" rel="stylesheet" media="screen"/>
		<script src="${pageContext.request.contextPath }/js/jquery.1.7.2.min.js"></script>
		<script src="${pageContext.request.contextPath }/js/bootstrap.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
		<script type="text/javascript">
			function shua(){
				var isShua=${param.tempPat==1};
				if(isShua==true){
					window.parent.frames["leftFrameProcess"].location.reload();
				}
			}
			function candidate(){
			var falg =0;
			var processStepId ="";
			var radios = document.getElementsByName("radios"); 
			var falg=false; 
			for(i=0;i<radios.length;i++) { 
			if(radios[i].checked) {
			processStepId = radios[i].value;
			falg = true;
			}
			}
			if(falg==false){
			alert("请选择步骤！");
			}else{
			document.forms[0].action="${pageContext.request.contextPath}/api/workflow/getauthorizedStep?processStepId="+processStepId+"&authorizedType=1";
			document.forms[0].submit();
			}
			}
			function previewPricess(){
				document.forms[0].action="${pageContext.request.contextPath}/api/workflow/goPreviewPricess";
			document.forms[0].submit();
			}
			function executor(){
			var processStepId ="";
			var radios = document.getElementsByName("radios"); 
			var falg=false; 
			for(i=0;i<radios.length;i++) { 
			if(radios[i].checked) {
			processStepId = radios[i].value;
			falg = true;
			}
			} 
			if(falg==false){
			alert("请选择步骤！");
			}else{
			document.forms[0].action="${pageContext.request.contextPath}/api/workflow/getauthorizedStep?processStepId="+processStepId+"&authorizedType=2";
			//document.forms[0].action="${pageContext.request.contextPath}/api/workflow/goAuthorizedIndex?processStepId="+processStepId;
			document.forms[0].submit();
			}
		}
		function updateStep(){
			var falg =0;
			var processStepId ="";
			var radios = document.getElementsByName("radios"); 
			for(i=0;i<radios.length;i++) { 
			if(radios[i].checked) {
			processStepId = radios[i].value;
			}
			} 
			document.forms[0].action="${pageContext.request.contextPath}/api/workflow/getProcessStep?processStepId="+processStepId;
			document.forms[0].submit();
			}
			<%-- 选中行--%>
			function checkRow(id){
			document.getElementById(id).checked = true;
		}
		</script>
	</head>
	<body onload="return shua();">
<div class="MainContent">
	<div class="ListBlock">
		<h3 class="cBlack">您当前的位置：<c:if test="${flowManagerBean.flag =='0'}"> 目录基本信息</c:if>
	     	<c:if test="${flowManagerBean.flag =='1'}">流程基本信息</c:if>
	     	<c:if test="${empty flowManagerBean}">请您添加流程目录</c:if>  </h3>
		<form id="addForm" method="post" action="" >
		 <input type="hidden" name="isOrganization" value="${flowManagerBean.isOrganization }" id="parentId"/>
		    <input type="hidden" name="processPicturesUrl" value="${flowManagerBean.processPicturesUrl }" id="processPicturesUrl"/>
		    <input type="hidden" name="processPageUrl" value="${flowManagerBean.processPageUrl }" id="processPageUrl"/>
		 <input type="hidden" name="processId" value="${flowManagerBean.processId }" id="processId"/>

			<!-- 1 -->
	     <div>
			<c:if test="${empty flowManagerBean}">
	       	  <table class="table table-hover table-bordered">
	       </c:if>
	     	<c:if test="${!empty flowManagerBean}">
	       <table class="table table-hover table-bordered"  style="display: none;">
	       	</c:if>  
           <tr>
				<th>添加流程目录</th>
		   </tr>
		   <tr>
	         <td>欢迎使用流程管理！</td>
	       </tr>
	       </table>
	     </div>
	    <!-- 2 -->
	     <div class="EditBox" >
	       <c:if test="${!empty flowManagerBean}">
		   <c:if test="${flowManagerBean.flag =='0'}">
	        	  <table class="table table-hover table-bordered">
	       </c:if>
	     	<c:if test="${flowManagerBean.flag =='1'}">
	     	  	  <table class="table table-hover table-bordered" style="display: none;">
	       	</c:if>
           <tr>
				<th colspan="2">目录基本信息</th>
		   </tr>
		   <tr>
	         <td class="EditBox_td1" width="100px">目录名称：</td>
	         <td>${flowManagerBean.strname}</td>
	       </tr>
		   <tr>
	         <td class="EditBox_td1">创建人：</td>
	         <td>${flowManagerBean.createUser}</td>
	       </tr>
	       <tr>
	         <td class="EditBox_td1">创建时间：</td>
	         <td>${fn:substring(flowManagerBean.createDate,0,19)}</td>
	       </tr>
		   </table>
	     </div>
			<!-- 3 -->
		<div class="EditBox" >
		   <c:if test="${flowManagerBean.flag =='1'}">
	         	  <table class="table table-hover table-bordered">
	       </c:if>
	     	<c:if test="${flowManagerBean.flag =='0'}">
	     	  	  <table class="table table-hover table-bordered" style="display: none;">
	       	</c:if>  
           <tr>
				<th colspan="2">流程基本信息</th>
		   </tr>
		   <tr>
	         <td class="EditBox_td1">流程名称：</td>
	         <td>${flowManagerBean.strname}</td>
	       </tr>
	        <tr>
	         <td class="EditBox_td1">流程编码：</td>
	         <td>${flowManagerBean.processCode}</td>
	       </tr>
	       <!--  <tr>
	         <td class="EditBox_td1">应用范围：</td>
	         <td>
	         <c:if test="${flowManagerBean.isAuthorizedBt==1}">【兵】
	         </c:if>
	         <c:if test="${flowManagerBean.isAuthorizedSb==1}">【师】
	         </c:if>
	         <c:if test="${flowManagerBean.isAuthorizedKq==1}">【垦】
	         </c:if>
	         <c:if test="${flowManagerBean.isAuthorizedTc==1}">【团】
	         </c:if>
	         <c:if test="${flowManagerBean.isAuthorizedQt==1}">【其他】
	         </c:if>
	         </td>
	       </tr>
	        <tr>
	         <td class="EditBox_td1">是否跨组织使用：</td>
	         <td>
	         <c:if test="${flowManagerBean.isOrganization==0}">否
	         </c:if>
	         <c:if test="${flowManagerBean.isOrganization==1}">是
	         </c:if>
	         </td>
	       </tr> -->
		   <tr>
	         <td class="EditBox_td1">创建人：</td>
	         <td>${flowManagerBean.createUser}</td>
	       </tr>
	       <tr>
	         <td class="EditBox_td1">创建时间：</td>
	         <td>${fn:substring(flowManagerBean.createDate,0,19)}</td>
	       </tr>
		</table>
		</div>
			<!-- 4 -->
		<div class="List">
		  <c:if test="${flowManagerBean.flag =='1'}">
	        	  <table class="table table-hover table-bordered">
	       </c:if>
	       	<c:if test="${flowManagerBean.flag =='0'}">
	     	  	  <table class="table table-hover table-bordered" style="display: none;">
	       	</c:if>
					<thead>
						<tr>
							<th width="5%">
								选择
							</th>
							<th width="20%">
								步骤编号
							</th>
							<th width="40%">
								步骤名称
							</th>
							<!-- <th width="10%">
								应用范围
							</th> -->
							<th width="10%">
								步骤类型
							</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${processList}" var="processStep" varStatus="index">
							<tr onclick="checkRow('${index.index}')">
								<td>
									<input type="radio" id="${index.index}" name="radios" value="${processStep.processStepId }" />
								</td>
								<td>
									<label>
										${processStep.stepId }
									</label>
								</td>
								<td>
									${processStep.stepName }
								</td>
								<!-- <td>
									<c:if test="${processStep.applicationScope ==1}">
									兵
									</c:if>
									<c:if test="${processStep.applicationScope ==2}">
									师
									</c:if>
									<c:if test="${processStep.applicationScope ==3}">
									垦
									</c:if>
									<c:if test="${processStep.applicationScope ==4}">
									团
									</c:if>
									<c:if test="${processStep.applicationScope ==0}">
									其他
									</c:if>
								</td> -->
								<td>
									<c:if test="${processStep.stepType ==0}">
									普通
									</c:if>
									<c:if test="${processStep.stepType ==1}">
									会签
									</c:if>
								</td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
						<td colspan="5">
				      <div class="OptBar">
						<div class="OptbtnPos">
								<a class="btn" href="#" onclick="updateStep();" ><span class="r">步骤设置</span></a>
								<a class="btn" href="#" onclick="candidate();" ><span class="r">候选人</span></a>
								<a class="btn" href="#" onclick="executor();"><span class="r">执行人</span></a>
								<a class="btn" href="#" onclick="previewPricess();"><span class="r">查看流程图</span></a>
							</div>
						</div>
						</td>
					    </tr>
					</tfoot>
				</table>
				</c:if>
		</div>
		
		</form>
	</div>
</div>	
	
	</body>	
</html>

