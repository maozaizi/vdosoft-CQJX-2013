<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>right</title>
   			<!-- Bootstrap -->
		<link href="${pageContext.request.contextPath }/css/bootstrap.min.css" rel="stylesheet" media="screen"/>
		<script src="${pageContext.request.contextPath }/js/jquery.1.7.2.min.js"></script>
		<script src="${pageContext.request.contextPath }/js/bootstrap.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
    <script type="text/javascript">
			function addUser(){
			//document.forms[0].action="${pageContext.request.contextPath}/api/workflow/goAuthorizedIndex";
			//document.forms[0].submit();
			window.showModalDialog("${pageContext.request.contextPath}/api/workflow/goAuthorizedIndex?tmp=" + Math.round(Math.random() * 10000)+"&sort=${sort}&processStepId=${processStepId}&processId=${processId}&authorizedType=${authorizedType}","a","dialogheight=700px;dialogwidth=900px;edge=r aised;scroll=yes");
			document.forms[0].action="${pageContext.request.contextPath}/api/workflow/getauthorizedStep";
			document.forms[0].submit();
		}
		function addPost(){
			//document.forms[0].action="${pageContext.request.contextPath}/api/workflow/getPost";
			//document.forms[0].submit();
			window.showModalDialog("${pageContext.request.contextPath}/api/workflow/getPost?tmp=" + Math.round(Math.random() * 10000)+"&sort=${sort}&processStepId=${processStepId}&processId=${processId}&authorizedType=${authorizedType}","a","dialogheight=500px;dialogwidth=700px;edge=r aised;scroll=yes");
			document.forms[0].action="${pageContext.request.contextPath}/api/workflow/getauthorizedStep";
			document.forms[0].submit();
		}
		function del(id){
			if(confirm("请确定删除此信息吗？")){
			document.forms[0].action="${pageContext.request.contextPath}/api/workflow/delAuthorizedStep?id="+id;
			document.forms[0].submit();
			}
		}
		function goRet(){
		document.forms[0].action="${pageContext.request.contextPath}/api/workflow/getProcessInfo";
		document.forms[0].target="rightProcess";
		document.forms[0].submit();
		}
    </script>
  </head>
  
  <body>
<div class="MainContent">
  <div class="ListBlock">
    <h3 class="cBlack">岗位用户信息</h3>
		<div class="List">
		<form id="addForm" method="post" action="" >
		<input type="hidden" name="sort" value="${sort}" id="sort"/>
		<input  type="hidden"  name="url" value="${pageContext.request.contextPath}/api/workflow/getauthorizedStep"/>
	  	<input type="hidden" name="processStepId" value="${processStepId}" id="processStepId"/>
	  	<input type="hidden" name="processId" value="${param.processId}" id="processId"/>
		<input type="hidden" name="authorizedType" value="${authorizedType}" id="authorizedType"/>
			  <table class="table table-hover table-bordered">

	  	  	<thead>
			<tr>
				<th width="20%">授权的岗位用户ID</th>
				<th width="30%">授权的岗位用户名称</th>
	        	<th width="20%">授权用户类型</th>
	        	<th width="20%">授权类型</th>
	        	<th width="10%">操作</th>
	        </tr>
	        </thead>
	        <tbody>
	        <c:forEach items="${authorizedRelationList}" var="item">
				<tr>
			      	<td>
			      	${item.autId }
			      	</td>
			      	<td>
			      	${item.autName }
			      	</td>
			      	<td>
			      	<c:if test="${item.strtype ==1 }">岗位 </c:if>
			      	 <c:if test="${item.strtype ==2 }">用户 </c:if>
			      	  <c:if test="${item.strtype ==3 }">字典岗位 </c:if>
			      	</td>
			      	<td>
			      	<c:if test="${item.authorizedType ==1 }">候选人 </c:if>
			      	 <c:if test="${item.authorizedType ==2 }">执行人 </c:if>
			      	</td>
			      	<td><a class="btn" href="#" onclick="del('${item.id}');" ><span class="btn">移除</span></a></td>
			    </tr>
			</c:forEach>
			</tbody>
		</table>
  		</div>
  		
  		 <div  style="margin-top:30px; width:500px; margin-left:300px;">
  		      <a class="Bbtn" href="#" onclick="addUser()"><span class="btn">选择人员</span></a>
			  <a class="Bbtn" href="#" onclick="addPost()"><span class="btn">选择岗位</span></a>
			  <a class="Bbtn" href="#" onclick="goRet()"><span class="btn">返回</span></a>
		</div>
		</form>
    </div>
</div>
  	  
  </body>
</html>
