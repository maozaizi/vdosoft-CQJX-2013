<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="${pageContext.request.contextPath}/css/fire/global.css"
			rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/fire/layout.css"
			rel="stylesheet" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/webcalendar.js"
			type="text/javascript"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/fckeditor/fckeditor.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js"
			type="text/javascript"></script>
		<script type="text/javascript">	
$("document").ready(function(){
  <c:if test="${!empty detail.sendUserId}">
			var Myeditor = new FCKeditor("contContent");
	          Myeditor.BasePath = "${pageContext.request.contextPath}/fckeditor/";
	          //Myeditor.ToolbarSet = "Basic";
	          //Myeditor.Height="700";
	          Myeditor.ReplaceTextarea();
   </c:if>
		});
function sub(){
    document.forms[0].action ="${pageContext.request.contextPath}/api/mailManage/add";
  	document.forms[0].submit();
}
</script>
	</head>
	<body>
		<div class="MainContent">
			<div class="ListBlock">
				<h3 class="cBlack">
					收件箱
				</h3>
				<form id="mail1Form" method="post"
					action="${pageContext.request.contextPath}/api/mailManage/getReceiveDetail">
					<input type="hidden" name="url" id="url" value="${param.url}" />
					<input type="hidden" name="sendUserId" id="sendUserId"
						value="${detail.reciveUserId}" />
					<input type="hidden" name="ID" id="ID" value="${detail.ID}" />
					<input type="hidden" name="reciveUserId" id="reciveUserId"
						value="${detail.sendUserId}" />
					<input type="hidden" name="reciveUserName" id="reciveUserName"
						value="${detail.sendUserName}" />
					<input type="hidden" name="contTitle" id="contTitle"
						value="${detail.contTitle}" />
					<input type="hidden" name="flag" id="flag" value="1" />

					<div class="mailblock_out">
						<table cellspacing="0" cellpadding="0" class="mailblock_in"
							width="100%">
							<tr>
								<th colspan="2" class="mailblock_th">
									收件箱
								</th>
							</tr>
							<tr>
								<td class="mailblock_td" width="8%">
									标&nbsp;&nbsp;&nbsp;&nbsp;题：
								</td>
								<c:if test="${!empty detail.sendUserId}">
									<td width="92%" align="left">
										${detail.contTitle}
									</td>
								</c:if>
								<c:if test="${empty detail.sendUserId}">
									<td width="92%" align="left">
										<a
											href="${pageContext.request.contextPath}/api/task/get?id=${detail.objId}">${detail.contTitle}</a>
									</td>
								</c:if>
							</tr>
							<tr>
								<td class="mailblock_td">
									发件人：
								</td>
								<td align="left">
									${detail.sendUserName}
								</td>
							</tr>
							<tr>
								<td class="mailblock_td">
									时&nbsp;&nbsp;&nbsp;&nbsp;间：
								</td>
								<td align="left">
									<tag:dateTime value="${detail.createDate}" />
								</td>
							</tr>
							<tr>
								<td class="mailblock_td">
									内&nbsp;&nbsp;&nbsp;&nbsp;容：
								</td>
								<td align="left">
									${detail.contContent}
								</td>
							</tr>


							<c:if test="${!empty detail.sendUserId}">


								<tr>
									<td class="mailblock_biao" colspan="2">
										快速回复：
									</td>
								</tr>


								<tr>
									<td class="mailblock_td" colspan="2">
										<div>
											<textarea name="contContent" id="contContent"></textarea>

											<div
												style="text-align: right; margin-top: 10px; margin-right: 8px;">
												<a class="Bbtn" href="#" onclick="sub();"><span
													class="Br">发送</span>
												</a>
												<a class="Bbtn" href="#"
													onclick="location.href='${param.url}'"><span class="Br">返回</span>
												</a>
											</div>
										</div>


										<table width="100%" cellspacing="0" cellpadding="0"
											class="reply">
											<c:forEach items="${page.resultList}" var="mail">
												<tr>
													<td width="60px" height="66" rowspan="2" class="reply_td">
														<img
															src="${pageContext.request.contextPath}/images/person_ion.jpg" />
													</td>
													<td>
														<span class="reply_name">${mail.sendUserName}:</span><span>${mail.contTitle}</span>
														<br />
														<span>${mail.contContent}</span>
													</td>
												</tr>
												<tr>
													<td class="reply_td1">
														<tag:dateTime value="${mail.createDate}" />
													</td>
												</tr>
											</c:forEach>
										</table>
								<tr>
									<td class="mailblock_tftd" colspan="2">
										<div class="page">
											<%@include file="/jsp/public/standard.jsp"%>
										</div>
									</td>
								</tr>


								<tr>
									<td colspan="2"></td>
								<tr>
									<td colspan="2"></td>
								<tr>
									<td colspan="2"></td>
								</tr>

							</c:if>

						</table>
						<c:if test="${empty detail.sendUserId}">
							<a class="Bbtn" href="#" onclick="location.href='${param.url}'"><span
								class="Br">返回</span>
							</a>
						</c:if>
					</div>
				</form>

			</div>
		</div>
	</body>

</html>
