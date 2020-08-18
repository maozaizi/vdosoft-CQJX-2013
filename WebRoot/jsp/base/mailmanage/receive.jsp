<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv=Refresh content="30"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/fire/ui.css" rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script type="text/javascript">	
function sub(id,reciveUserId,sendUserId){
	 document.forms[0].action ="${pageContext.request.contextPath}/api/mailManage/getReceiveDetail?ID="+id+"&reciveUserId="+reciveUserId+"&sendUserId="+sendUserId;
 	 document.forms[0].submit();
}
function openSend(){
	 initInputValue();
	 document.forms[0].action ="${pageContext.request.contextPath}/api/mailManage/getSendMail";
 	 document.forms[0].submit();
}
function search1(){
	 document.forms[0].action ="${pageContext.request.contextPath}/api/mailManage/getReceiveMail";
 	 document.forms[0].submit();
}
function initInputValue(){
	$(':input:visible').attr('value','');
}
</script>
</head>
<body>	
  		<div class="mc">
			
				<!-- table页开始-->
				<div class="tabMenu">
				  <ul>
				    <li><a href="#" onclick="openSend();"><span>发件箱</span></a></li>
				    <li  class="active"><a href="#"><span>收件箱</span></a></li>
				  </ul>
				</div>
			  	<!-- table页结束-->

				<form id="mailForm" method="post" action="">
				<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/mailManage/getReceiveMail?currentPageNO=${param.currentPageNO}"/>
		<div class="workbench_mc standardized_list shotRow">
			<table width="100%" cellspacing="0" cellpadding="0" >
		       <thead>
			      <tr>
			      	<th width="25%">标题</th>
			        <th width="25%">发件人</th>
			        <th width="25%">收件人</th>
			        <th width="25%">发送时间</th>
			      </tr>
		       </thead>
		       <tbody>
	     			  	<c:forEach items="${page.resultList}" var="mail">
				       
				      <tr <c:if test="${mail.readStatus==1}">  style="font-weight:bold;"</c:if> ondblclick="sub('${mail.ID}','${mail.reciveUserId}','${mail.sendUserId}');"><input type="hidden" name="ID" id="ID" value="${mail.ID}"/>
				        <td>${mail.contTitle}<c:if test="${mail.readStatus==1}"><img src="${pageContext.request.contextPath}/images/BDNew.gif"/></c:if> </td>
				      	<td>${mail.sendUserName} </td>
				        <td>${mail.reciveUserName}</td>
				       <td><tag:dateTime value="${mail.createDate}"/></td>
				      </tr>
			      </c:forEach>
		       </tbody>
	    		<tfoot>
					<tr>
					  <td  colspan="4" >
					  	<div class="page">
						<%@include file="/jsp/public/standard.jsp"%>
						</div>
					  </td>
					</tr>
				  </tfoot>
	    </table>
		</div>
	   <!-- 查询条件开始 -->
	<div class="SearchBlock">
    <fieldset>
    <legend>查询条件</legend>
    <table>
      <tbody> 
        <tr>
          <th width="96px">标题：</th>
          <td><input name="contTitle" size="26" type="text" value="${param.contTitle}"></td>
           <th width="96px">发件人：</th>
          <td><input name="sendUserName" size="26" type="text" value="${param.sendUserName}"></td>
        </tr> 
        <tr>
       		 <th>发送时间：</th>
	          <td>
				<input name="createDate1"  value="${param.createDate1}" size="9"  type="text" onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="readonly"/>至
			    <input name="createDate2" value="${param.createDate2}" size="9"  type="text" onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="readonly"/>
	          </td>
        </tr>               
      </tbody>
    </table>
    <a class="Bbtn schPos" name="sss" href="#" onclick="search1();"><span class="Br">搜　索</span></a>
    </fieldset>
	</div>
	<!-- 查询条件结束 -->
	</form>
   </div>
</body>
 </html>
