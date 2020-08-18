<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/fire/ui.css" rel="stylesheet" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">	
function sub(id,reciveUserId,sendUserId){
	 document.forms[0].action ="${pageContext.request.contextPath}/api/mailManage/getSendDetail?ID="+id+"&reciveUserId="+reciveUserId+"&sendUserId="+sendUserId;
 	 document.forms[0].submit();
}
function openReceive(){
	 initInputValue();
	 document.forms[0].action ="${pageContext.request.contextPath}/api/mailManage/getReceiveMail";
 	 document.forms[0].submit();
}
function search1(){
	 document.forms[0].action ="${pageContext.request.contextPath}/api/mailManage/getSendMail";
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
				    <li class="active"><a href="#"><span>发件箱</span></a></li>
				    <li><a href="#" onclick="openReceive();"><span>收件箱</span></a></li>
				  </ul>
				</div>
			  	<!-- table页结束-->	
				<form id="mailForm" method="post" action="${pageContext.request.contextPath}/api/mailManage/getSendMail">
				<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/mailManage/getSendMail?currentPageNO=${param.currentPageNO}"/>
				<input type="hidden" name="flag" id="flag" value="0"/>
		
		<div class="workbench_mc standardized_list shotRow">		
			<table width="100%" cellspacing="0" cellpadding="0" >
			  <thead>
			      <tr>
			      	<th width="12%">标题</th>
			        <th width="20%">发件人</th>
			        <th width="20%">收件人</th>
			        <th width="20%">发送时间</th>
			      </tr>
		      </thead>
		      <tbody>
		     
			  	<c:forEach items="${page.resultList}" var="mail">
				     <tr ondblclick="sub('${mail.ID}','${mail.reciveUserId}','${mail.sendUserId}');"><input type="hidden" name="ID" id="ID" value="${mail.ID}"/>
				        <td>${mail.contTitle} </td>
				        <td>${mail.sendUserName}</td>
				        <td>${mail.reciveUserName}</td>
				        <td><tag:dateTime value="${mail.createDate}"/></td>
				      </tr>
		      </c:forEach>
		      </tbody>
			  <tfoot>
				<tr>
				  <td  colspan="4" >
					  	<div class="page">
							<%@include file="/jsp/public/standard.jsp" %>
						</div>
				</td>
				</tr>
			  </tfoot>
		    </table>
		</div>
		
		 <div class="opt_btn">
		  <div class="xy">
			<a class="btn" href="#" onclick="location.href='${pageContext.request.contextPath}/api/mailManage/toAdd?currentPageNO=${param.currentPageNO}'" class="com_but"><span class="r">写信</span></a> 
			<!--  
			<a class="btn" href="#" onclick="location.href='${pageContext.request.contextPath}/api/mailManage/daoChuTiaoWen'" class="com_but"><span class="r">导出检查项条文库</span></a>
			<a class="btn" href="#" onclick="location.href='${pageContext.request.contextPath}/api/mailManage/downLoad'" class="com_but"><span class="r">下载到客户端</span></a> 
		 	-->
		 </div>
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
           <th width="96px">收件人：</th>
          <td><input name="reciveUserName" size="26" type="text" value="${param.reciveUserName}"></td>
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
