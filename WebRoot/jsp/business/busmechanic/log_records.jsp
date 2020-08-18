<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>操作历史记录</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
	</head>
	<body>
	<div class="container-fluid">
	   <div class="row-fluid">
		    <div class="span9">
				<div class="page-header">
		        	<h3>操作历史记录</h3>
		        </div>
		        <div class="page-header">
		        <table class="table table-striped table-bordered"> 
                 	<tr>  
						<div class="row form-horizontal">
						<th>操作动态</th>
						</div>
					</tr>
				</table>
				<table class="table table-striped table-bordered">
			          	<tbody>
				           <c:if test="${empty page.resultList}">
				           		<tr>
				                    <td colspan="6" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                 </tr>   
				           	</c:if>
				           <c:if test="${not empty page.resultList}">
			            	<c:forEach items="${page.resultList}" var="logRecords" varStatus="i">
			                 <tr>
			                     <td> ${logRecords.remark}</td>
			                 </tr>
			                </c:forEach>
			               </c:if>
					 	</tbody>
					 	<tfoot>
							<tr>
				        		<td colspan="6">
				        			<div class="page">
								  		<%@include file="/jsp/public/standard.jsp" %>
									</div>
				        		</td>
					        </tr>
						</tfoot>
			        </table>
			        <center><button class="btn" onclick="window.close();">关  闭</button></center>
		        </div>
		       
			</div>
		</div>
	</div>
		

	</body>
</html>
