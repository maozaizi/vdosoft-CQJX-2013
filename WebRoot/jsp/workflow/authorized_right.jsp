<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<html>
  <head>
    <title>right</title>
    <link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
	<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
    <script type="text/javascript">
		function addUser(){
			document.forms[0].action="${pageContext.request.contextPath}/api/workflow/addPostAndUserInfo";
			document.forms[0].submit();
		}
		//function addPost(){
		//var autId ="";
		//	var radio_postId = document.getElementsByName("radio_postId"); 
		//	for(i=0;i<radio_postId.length;i++) { 
		///	if(radio_postId[i].checked) {
		//	autId = radio_postId[i].value;
		//	}
		//	} 
		//	document.forms[0].action="${pageContext.request.contextPath}/api/workflow/addPostAndUserInfo?strtype=1&autId="+autId;
		//	document.forms[0].submit();
		//}
		function goRet(){
		//document.forms[0].action="${pageContext.request.contextPath}/api/workflow/getauthorizedStep";
		//document.forms[0].target="rightProcess";
		//document.forms[0].submit();
		top.close();
		}
    </script>
  </head>
  
  <body>
	<div class="MainContent">
		<div class="ListBlock">
		    <h3 class="cBlack">选择人员</h3>
		    <div class="EditBox">
	  		<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  	  	<tr>
	  	  	<th  colspan="2">基本信息</th>
	  	  	</tr>
			  <tr>
				<td class="EditBox_td1">名称：</td>
				<td>
				  	${organizationDetali.organizationDetailName }
				</td>
			  </tr>
			  <tr>
			  	<td class="EditBox_td1">节点类型：</td>
				<td >
				  	<c:if test="${organizationDetali.type==0}">组织</c:if>
				  	<c:if test="${organizationDetali.type==1}">部门</c:if>
				  	<c:if test="${organizationDetali.type==2}">成员单位</c:if>
				  	<c:if test="${organizationDetali.type==3}">消防队</c:if>
				  	<c:if test="${organizationDetali.type==4}">重点单位</c:if>
				</td>
			  </tr>
			  <tr>
				<td class="EditBox_td1">创建时间：</td>
				<td>
					${fn:substring(organizationDetali.createDate,0,10)}
				</td>
			  </tr>
			  <c:if test="${organizationDetali.type!=0 && organizationDetali.type != 1}">
				  <tr>
					<td class="EditBox_td1">所在地址：</td>
					<td>
						有问题
					</td>
				  </tr>
			  </c:if>
		</table>
		</div>
		
		<form id="addForm" method="post" action="" >
		<input type="hidden" name="organizationDetailId" value="${organizationDetali.organizationDetailId }" id="organizationDetailId"/>
		<input type="hidden" name="processStepId" value="${processStepId }" id="processStepId"/>
		<input type="hidden" name="authorizedType" value="${authorizedType }" id="authorizedType"/>
		<input type="hidden" name="processId" value="${param.processId }" id="processId"/>
		<input type="hidden" name="url" id="url" value = "${pageContext.request.contextPath}/api/workflow/getPostAndUserInfo?organizationDetailId=${organizationDetali.organizationDetailId}&processStepId=${processStepId }&authorizedType=${authorizedType }&processId=${param.processId }"/>
		<div class="List">
	  	  <table width="100%" cellspacing="0" cellpadding="0">
	  	    <caption>人员信息</caption>
	  	  	<thead>
			<tr>
				<th width="5%"  class="List_Th">选择</th>
				<th width="25%"  class="List_Th">姓名</th>
	        	<th width="25%"  class="List_Th">性别</th>
	        	<th width="25%"  class="List_Th">出生年月</th>
	        	<th width="25%"  class="List_Th">民族</th>
	        </tr>
	        </thead>
	  	  	
            <tbody>
			<c:forEach items="${userInfoList}" var="item">
				<tr>
		        	<td bordercolor="1" >
		        		<input type="checkbox" name="userId" id="userId" value="${item.userId }/${item.name }" title="${item.userId }"/>
		        		
		        	</td>
		        	<td >${item.name }</td>
			      	<td  >${item.gender }</td>
			      	<td  >${fn:substring(item.birthData,0,10) }</td>
			      	<td  >${item.nation }</td>
			    </tr>
			<tbody>
			</c:forEach>
		</table>
		</div>
		<div class="List">
	  	  <table width="100%" cellspacing="0" cellpadding="0">
	  	  	<caption>岗位信息</caption>
	  	  	<thead>
			<tr>
				<th width="5%" >选择</th>
				<th width="40%">名称</th>
	        	<th width="40%">职责</th>
	        </tr>
	        </thead>
	        <tbody>
	        <c:forEach items="${postInfoList}" var="item">
				<tr>
			      	<td><input type="checkbox" name="postId" id="postId" value="${item.postId }/${item.postName }" title="${item.postId }"/>
			      	</td>
			      	<td>${item.postName }</td>
			      	<td>${item.postDuties }</td>
			    </tr>
			</c:forEach>
			</tbody>
		</table>
  		</div>
  		      
       <div class="Temp-3">
		      <a class="Bbtn" href="#" onclick="addUser()"><span class="Br">加入</span>
		      <a class="Bbtn" href="#" onclick="goRet()"><span class="Br">返回</span>
       </div>
  		      
		</form>

    </div>
</div>
  	  
  </body>
</html>
