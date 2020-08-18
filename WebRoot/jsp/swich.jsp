<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>cssrain</title>
<meta charset="UTF-8" content="text/html;" http-equiv="Content-Type"/>

<script language="javascript">

function switchSysBar(){
 if (parent.document.getElementById('switch').cols=="199,10,*"){
 document.getElementById('leftbar').style.display="";
 parent.document.getElementById('switch').cols="0,10,*";
 }
 else{
 parent.document.getElementById('switch').cols="199,10,*";
 document.getElementById('leftbar').style.display="none";
 }
}
function load(){
 if (parent.document.getElementById('switch').cols=="0,10,*"){
 document.getElementById('leftbar').style.display="";
 }
}

</script>
<style type="text/css">
body { background:#5ea1c7;margin:0px;padding:0px;}

table{background-image: url(${pageContext.request.contextPath}/css/fire/images/framebgimage.gif);background-repeat: repeat-x;background-position: left top;}
</style>
</head>
<body marginwidth="0" marginheight="0" onLoad="load()" topmargin="0" leftmargin="0">
<center>
<table height="100%" cellspacing="0" cellpadding="0" border="0" width="100%">
<tbody>
<tr>
<td id="leftbar" style="display: none;"><div style="margin-top:240px;height:591px;width:4px;background-image: url(${pageContext.request.contextPath}/css/fire/images/framebgimage.gif);background-repeat: repeat-x;background-position: left top;">
<a onClick="switchSysBar()" href="javascript:void(0);">
<img height="90" border="0" width="9" title="展开菜单" src="${pageContext.request.contextPath}/css/fire/images/pic24.gif"/>
</a></div>
</td>
<td id="rightbar"><div style="margin-top:240px;height:591px;width:4px;background-image: url(${pageContext.request.contextPath}/css/fire/images/framebgimage.gif);background-repeat: repeat-x;background-position: left top;">
<a onClick="switchSysBar()" href="javascript:void(0);">
<img height="90" border="0" width="9" title="隐藏菜单" src="${pageContext.request.contextPath}/css/fire/images/pic23.gif"/>
</a></div>
</td>
</tr>
</tbody>
</table>
</center>
</body>
</html>