<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="keywords" content="列表" />
	<meta http-equiv="description" content="列表"/>
	<title>我的工作台</title>
	<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		function cc(url,id){
			var action = "";
			if(url.indexOf('?')>0){
				action= "${pageContext.request.contextPath}/"+url+"&id="+id+"&objId="+id;
			}else{
				action= "${pageContext.request.contextPath}/"+url+"?fireId="+id+"&objId="+id+"&id="+id;
			}
			document.forms[0].action = action;
			document.forms[0].submit();
		}
	</script>
</head>
  
  <body>
  
  	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span9">
				<div class="page-header">
					<ul class="breadcrumb">
					  <li><a href="#">工作台</a> <span class="divider">/</span></li>
					  <li class="active">已办工作</li>
					</ul>
				</div>
				<form id="myWorkForm" method="post" action="">
		  		<input type="hidden" name="strurl" id="strurl" value="${pageContext.request.contextPath}/api/mywork/getMyWorkUserList"/>
				<div class="page-header">
					<table class="table table-striped table-bordered">
					    <thead>
						    <tr>
						      <th colspan="2">已办工作</th>
						    </tr>
					    </thead>
				    <tbody>
				    <c:if test="${empty page.resultList}">
		          		<tr>
		                   <td colspan="6" style="text-align: center;">太棒了！你今天的工作都经完成了。</td>
		                </tr>   
			        </c:if>
				    <c:forEach items="${page.resultList}" var="a">
				    	<tr>
					      <td>
					      	<a href="#" title="${a.contTitle}流程！" onclick="cc('${a.url}','${a.objId}')">${a.contTitle}</a>
					      </td>
					    </tr>
				    </c:forEach>
				    </tbody>
				  </table>
				  <%@include file="/jsp/public/standard.jsp" %>
				</div>
				</form>
			</div>
		</div>
	</div>
  </body>
</html>
