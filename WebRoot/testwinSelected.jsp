<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  	<title>长庆钻井机修公司-设备维修管理系统</title>
	<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
	<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
    <script type="text/javascript">
	    function callback(returnData){
	    	if(returnData != null){
	    		if(returnData.postList != null && returnData.postList.length>0){
	    			for(var i=0,j=returnData.postList.length;i<j;i++){
	    				if($("#postId").val() != ''){
							$("#postId").attr('value',$("#postId").val()+","+returnData.postList[i].postId);
							$("#postName").attr('value',$("#postName").val()+","+returnData.postList[i].postName);
	    				}else{
							$("#postId").attr('value',returnData.postList[i].postId);
							$("#postName").attr('value',returnData.postList[i].postName);
	    				}
	    			}
	            }
	            if(returnData.userList != null && returnData.userList.length>0){
	    			for(var i=0,j=returnData.userList.length;i<j;i++){
	    				if($("#userId").val() != '' ){
							$("#userId").attr('value',$("#userId").val()+","+returnData.userList[i].userId);
							$("#userName").attr('value',$("#userName").val()+","+returnData.userList[i].name);
	    				}else{
							$("#userId").attr('value',returnData.userList[i].userId);
							$("#userName").attr('value',returnData.userList[i].name);
	    				}
	    			}
	            }
			}
	    }
    	$('document').ready(function(){
    		$('#selectOrgneirong').click(function(){
    			var inData = {"userList":[{name:"adsf",age:"23"},{name:"ddd",age :"22"}],"postList":[]};
    			var returnData = window.showModalDialog('${pageContext.request.contextPath}/api/org_select/toMain?treeType=0&auth=1&PRU=1&selectedType=0&closeAble=0&inData='+inData, window, "status=no;help:no;dialogWidth=800px;dialogHeight=600px");
    			if(returnData != null){
		            if(returnData.userList != null && returnData.userList.length>0){
						$("#userId").attr('value',returnData.userList[0].userId);
						$("#userName").attr('value',returnData.userList[0].name);
		            }
		            if(returnData.postList != null && returnData.postList.length>0){
						$("#postId").attr('value',returnData.postList[0].postId);
						$("#postName").attr('value',returnData.postList[0].postName);
		            }
    			}
    		});
    		$('#selectorg').click(function(){
	    		var idAndName = window.showModalDialog('${pageContext.request.contextPath}/api/baseorganization/ListWin?tmp=' + Math.round(Math.random() * 10000), window, "status=no;help:no;dialogWidth=460px;dialogHeight=670px");
	            if(null!=idAndName&&idAndName.length>0){
					document.getElementById("orgId").value=idAndName[0];
					document.getElementById("orgName").value=idAndName[1];
	            }
    		});
    	});
    </script>
  </head>
  <body>
  	<p>组织结构选择页面测试：</p>
    <div>
    	人员ID：<input type="text" value="" id="userId"/><br />
    	人员姓名：<input type="text" value="" id="userName"/><br />
    	岗位ID：<input type="text" value="" id="postId"/><br />
    	岗位名称：<input type="text" value="" id="postName"/><br />
    	组织名称：<input type="text" value="" id="orgName"/><br />
    	组织id：<input type="text" value="" id="orgId"/><br />
    	<input type="button" id="selectOrgneirong" value="选择"/>
    	<input type="button" id="selectorg" value="选择组织"/>
    </div>
  </body>
</html>
