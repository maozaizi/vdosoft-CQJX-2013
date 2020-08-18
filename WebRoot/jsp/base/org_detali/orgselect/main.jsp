<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/json2.js" type="text/javascript"></script>
	<script type="text/javascript">
		$('document').ready(function(){
			var selectedType = '${param.selectedType}';
			if(selectedType!=0){
				var inData = ${param.inData}
				//将json转换成string
				inData = JSON.stringify(inData);
				inData = encodeURIComponent(inData)
				window.parent.frames['bottomFream'].location = "${pageContext.request.contextPath}/api/org_select/selectedPage?PRU=${param.PRU }&closeAble=${param.closeAble }&inData="+inData;
			}
		});
	</script>
  </head>
<frameset rows="*" cols="200,800" framespacing="0" frameborder="no" id="fream">
 <frame src="${pageContext.request.contextPath}/jsp/base/org_detali/orgselect/tree.jsp?PRU=${param.PRU }&selectedType=${param.selectedType }&closeAble=${param.closeAble }&fn=${param.fn }"  name="app_leftFrame" id="app_leftFrame" style="border-top:1px solid #fff;border-right:2px solid #a0bed8;border-left:2px solid #a0bed8;" scrolling="yes">
 <c:if test="${param.closeAble==0 }">
 	<frame src="${pageContext.request.contextPath}/api/org_select/getInfo?PRU=${param.PRU }&selectedType=${param.selectedType }&treeType=${param.treeType }&closeAble=${param.closeAble }&fn=${param.fn }" name='app_rightFrame'  id="app_rightFrame"  frameborder="no" style="border-top:1px solid #fff;"/>
 </c:if>
 <c:if test="${param.closeAble!=0 }">
    <c:if test="${param.PRU == 0}">
 		<frameset rows="*,220" cols="*" framespacing="0" frameborder="no" id="rightFream">
		 	<frame src="${pageContext.request.contextPath}/api/org_select/getInfo?PRU=${param.PRU }&selectedType=${param.selectedType }&treeType=${param.treeType }&closeAble=${param.closeAble }&fn=${param.fn }" name='app_rightFrame'  id="app_rightFrame"  frameborder="no" style="border-top:1px solid #fff;"/>
		 	<frame src=""  name="bottomFream" id="bottomFream" style="border-top:0px solid #fff;border-right:0px solid #a0bed8;border-left:0px solid #a0bed8;">
		 </frameset>
 	</c:if>
    <c:if test="${param.PRU != 0}">
 		<frameset rows="*,140" cols="*" framespacing="0" frameborder="no" id="rightFream">
		 	<frame src="${pageContext.request.contextPath}/api/org_select/getInfo?PRU=${param.PRU }&selectedType=${param.selectedType }&treeType=${param.treeType }&closeAble=${param.closeAble }&fn=${param.fn }" name='app_rightFrame'  id="app_rightFrame"  frameborder="no" style="border-top:1px solid #fff;"/>
		 	<frame src=""  name="bottomFream" id="bottomFream" style="border-top:0px solid #fff;border-right:0px solid #a0bed8;border-left:0px solid #a0bed8;">
		 </frameset>
 	</c:if>
 </c:if>
</frameset>
</html>
