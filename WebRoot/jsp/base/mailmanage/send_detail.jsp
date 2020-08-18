<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
	<div class="MainContent">
  		<div class="ListBlock">
			<h3 class="cBlack">发件箱</h3>	
				<form id="mail1Form" method="post" action="">
			<!-- 	<input type="hidden" name="taskCode" id="taskCode" value="${send.taskCode}"/>
				<input type="hidden" name="sendUserId" id="sendUserId" value="${send.sendUserId}"/> -->
<input type="hidden" name="url" id="url" value="${param.url}"/>
		    <div class="mailblock_out"  >
			      <table cellspacing="0" cellpadding="0" class="mailblock_in" width="100%" >
						  <tr>
					  	    <th colspan="2" class="mailblock_th">发件箱</th>
					      </tr>
						  <tr>
							<td class="mailblock_td" width="8%">标&nbsp;&nbsp;&nbsp;&nbsp;题：</td>
					        <td width="92%" align="left">${detail.contTitle}</td>
						  </tr>
						 <tr>
							<td class="mailblock_td">收件人：</td>
					        <td align="left">${detail.reciveUserName}</td>
						  </tr>
						   <tr>
							<td class="mailblock_td">时&nbsp;&nbsp;&nbsp;&nbsp;间：</td>
					        <td align="left"><tag:dateTime value="${detail.createDate}"/></td>
						  </tr>
						   <tr>
							<td class="mailblock_td">内&nbsp;&nbsp;&nbsp;&nbsp;容：</td>
					        <td align="left">${detail.contContent}</td>
						  </tr>
						  <tr>
							<td colspan="2" class="mailblock_biao">
								<div class="Temp-4">
					 				<a class="Bbtn" href="#" onclick="location.href='${param.url}'" class="btn"><span class="Br">返回</span></a>
			   				    </div>
			   				</td>
			   				</tr>
						  <tr>
							<td class="mailblock_td"  colspan="2" >
							
							 <c:forEach items="${page.resultList}" var="mail">
								<table width="100%"  cellspacing="0" cellpadding="0" class="reply">
	                              
								  <tr >
								    <td width="60px" height="66" rowspan="2" class="reply_td"><img src="${pageContext.request.contextPath}/images/person_ion.jpg"/></td>
								    <td><span class="reply_name" >${mail.sendUserName}:</span><span>${mail.contContent}</span>
								    </td>
								  </tr>
								    <tr>
									    <td class="reply_td1" ><tag:dateTime value="${mail.createDate}"/></td>
									  </tr>
								</table>
							 </c:forEach>

						 <tr> 
							<td class="mailblock_tftd" colspan="2">
								<div class="page">
									<%@include file="/jsp/public/standard.jsp"%>
								</div>
							</td>
						  </tr>
						  
						  
					    </div>	
						</td>
					  </tr>
		       </table>
		    </div>

	</form>
		</div>
	</div>
</body>
</html>
