<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>流程管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<!-- Bootstrap -->
		<link
			href="${pageContext.request.contextPath }/css/bootstrap.min.css"
			rel="stylesheet" media="screen" />
		<script
			src="${pageContext.request.contextPath }/js/jquery.1.7.2.min.js"></script>
		<script
			src="${pageContext.request.contextPath }/js/bootstrap.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.js"
			type="text/javascript"></script>

	
	<%-- 使用uploadify控件所需引用的js及样式--%>
		<link href="${pageContext.request.contextPath}/css/uploadify/default.css" rel="stylesheet" type="text/css" />
        <link href="${pageContext.request.contextPath}/css/uploadify/uploadify.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/uploadify/jquery-1.3.2.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/uploadify/swfobject.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/uploadify/jquery.uploadify.v2.1.0.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
            $("#processContent").uploadify({
                'uploader'       : '${pageContext.request.contextPath}/js/uploadify/uploadify.swf',//指定上传控件的主体文件，默认‘uploader.swf’
                'script'         : '${pageContext.request.contextPath}/ajaxapi/fileUpload/upload',//指定服务端处理类的入口
                'cancelImg'      : '${pageContext.request.contextPath}/img/uploadify/cancel.png',
                'folder'         : 'uploads',//要上传到的服务器路径，默认‘/’
                'queueID'        : 'fileQueue1',
                'auto'           : true,//是否选取文件后自动上传
                'multi'          : false,//是否支持多文件上传
                'fileDesc':'请选择.xml文件',
                'fileExt':'*.xml',
                'height'         : 34,   
	            'width'          : 118,
                'buttonImg'      : '${pageContext.request.contextPath}/img/uploadify/picture.jpg', 
                'simUploadLimit' : 1,//每次最大上传文件数 
                'wmode'          : 'transparent' ,  
                'onComplete'       : function (event, queueID, fileObj, response, data){
                	var jsondata =  JSON.parse(response);  //以JSON的形式处理文件信息包括：路径filepath,名称name等
                	var filelist = jsondata.filelist;
    				for(var i=0;i<filelist.length;i++){
    					$("#processContentUrl").val(filelist[i].filepath);
    				}
    				$("#processContentId").val(fileObj.name);               	
                	$('<li></li>').appendTo('.files').text(response); },    
               'onError'          : function(event, queueID, fileObj){ 
                   alert("文件:" + fileObj.name + " 上传失败");} 
            });
           	$("#processPictures").uploadify({
                'uploader'       : '${pageContext.request.contextPath}/js/uploadify/uploadify.swf',//指定上传控件的主体文件，默认‘uploader.swf’
                'script'         : '${pageContext.request.contextPath}/ajaxapi/fileUpload/upload',//指定服务端处理类的入口
                'cancelImg'      : '${pageContext.request.contextPath}/img/uploadify/cancel.png',
                'folder'         : 'uploads',//要上传到的服务器路径，默认‘/’
                'queueID'        : 'fileQueue2',
                'auto'           : true,//是否选取文件后自动上传
                'multi'          : false,//是否支持多文件上传
                'fileDesc':'请选择.png文件',
                'fileExt':'*.png',
                'height'         : 34,   
	            'width'          : 118,
                'buttonImg'      : '${pageContext.request.contextPath}/img/uploadify/picture.jpg', 
                'simUploadLimit' : 1,//每次最大上传文件数 
                'wmode'          : 'transparent' ,  
                'onComplete'       : function (event, queueID, fileObj, response, data){
                	var jsondata =  JSON.parse(response);  //以JSON的形式处理文件信息包括：路径filepath,名称name等
                	var filelist = jsondata.filelist;
    				for(var i=0;i<filelist.length;i++){
    					$("#processPicturesUrl").val(filelist[i].filepath);
    				}
    				$("#processPicturesId").val(fileObj.name);                    	
                	$('<li></li>').appendTo('.files').text(response); },    
               'onError'          : function(event, queueID, fileObj){ 
                   alert("文件:" + fileObj.name + " 上传失败");} 
            });
            $("#processPage").uploadify({
                'uploader'       : '${pageContext.request.contextPath}/js/uploadify/uploadify.swf',//指定上传控件的主体文件，默认‘uploader.swf’
                'script'         : '${pageContext.request.contextPath}/ajaxapi/fileUpload/upload',//指定服务端处理类的入口
                'cancelImg'      : '${pageContext.request.contextPath}/img/uploadify/cancel.png',
                'folder'         : 'uploads',//要上传到的服务器路径，默认‘/’
                'queueID'        : 'fileQueue3',
                'auto'           : true,//是否选取文件后自动上传
                'multi'          : false,//是否支持多文件上传
                'fileDesc':'请选择.xml文件',
                'fileExt':'*.xml',
                'height'         : 34,   
	            'width'          : 118,
                'buttonImg'      : '${pageContext.request.contextPath}/img/uploadify/picture.jpg', 
                'simUploadLimit' : 1,//每次最大上传文件数 
                'wmode'          : 'transparent' ,  
                'onComplete'       : function (event, queueID, fileObj, response, data){
                	var jsondata =  JSON.parse(response);  //以JSON的形式处理文件信息包括：路径filepath,名称name等
                	var filelist = jsondata.filelist;
    				for(var i=0;i<filelist.length;i++){
    					$("#processPageUrl").val(filelist[i].filepath);
    				}
    				$("#processPageId").val(fileObj.name);                    	
                	$('<li></li>').appendTo('.files').text(response); },    
               'onError'          : function(event, queueID, fileObj){ 
                   alert("文件:" + fileObj.name + " 上传失败");} 
            });
        });
		function save(isUpProCon){
			document.forms[0].action="${pageContext.request.contextPath}/api/workflow/updateProcess?isUpProCon="+isUpProCon;
			document.forms[0].submit();
		}
		<%--返回 --%>
		function goBack() {
			window.location.href = "${pageContext.request.contextPath}/api/workflow/getProcessInfo?tempPat=0&processId=${flowManagerBean.processId}";
		 }
		</script>
	</head>
	<body>
<div class="MainContent">
	<div class="ListBlock">
		<h3 class="cBlack">您当前的位置：编辑流程信息</h3>
		<form id="addForm" method="post" action="" >
		<input type="hidden" name="processId" id="processId" value="${flowManagerBean.processId}"/>
		<input type="hidden" name="flag" id="flag" value="${flowManagerBean.flag}"/>
		<input type="hidden" name="url" id="url" value = "${pageContext.request.contextPath}/api/workflow/getProcessInfo"/>
		<input type="hidden" name="contentUrl" id="contentUrl" value="${flowManagerBean.processContentUrl}"/>
		<div class="form-horizontal">
					<c:if test="${isUpProCon =='0'}">
					<div class="control-group">
						<label class="control-label" for="strname">
							名称
						</label>
						<div class="controls">
							<input type="text" name="strname" id="strname" value="${flowManagerBean.strname}"  size="25"/>
						</div>
					</div>
					</c:if>
					  <c:if test="${isUpProCon =='1'}">
					  	<div class="control-group">
						<label class="control-label" for="strname">
							名称
						</label>
						<div class="controls">
							<input type="text" name="strname" id="strname" value="${flowManagerBean.strname}" size="25"/>
						</div>
					</div>
						<div class="control-group">
						<label class="control-label" for="processCode">
							流程编码
						</label>
						<div class="controls">
							<input type="text" name="processCode" id="processCode" value="${flowManagerBean.processCode}"  size="25"/>
						</div>
					</div>
					  </c:if>
					  <c:if test="${isUpProCon =='2'}">
					  <div class="control-group">
						<label class="control-label" for="processContentId">
							上传流程文件
						</label>
						<div class="controls">
							 <input type="text" style="length:40" id="processContentId"  value="${flowManagerBean.processContentUrl}" readonly="readonly"/>
					         <input type="file" name="processContent" id="processContent" />
					         <input type="hidden" name="processContentUrl" id="processContentUrl" />
					          <div id="fileQueue1"></div>
						</div>
					</div>
					 <div class="control-group">
						<label class="control-label" for="processPicturesId">
							上传流程图片文件
						</label>
						<div class="controls">
							<input type="text" style="length:40" id="processPicturesId" value="${flowManagerBean.processPicturesUrl}" readonly="readonly"/>
				<input type="file" name="processPictures" id="processPictures" />
	         	<input type="hidden" name="processPicturesUrl" id="processPicturesUrl" />
	          	<div id="fileQueue2"></div>
						</div>
					</div>
					 <div class="control-group">
						<label class="control-label" for="processPageId">
							上传流程界面文件
						</label>
						<div class="controls">
							<input type="text" style="length:40" id="processPageId" value="${flowManagerBean.processPageUrl}" readonly="readonly"/>
				<input type="file" name="processPage" id="processPage" />
	         	<input type="hidden" name="processPageUrl" id="processPageUrl" />
	          	<div id="fileQueue3"></div>
						</div>
					</div>
					  </c:if>
					
		
		<!-- 操作按钮开始 -->
			<div class="form-actions">
			<a class="btn btn-primary" href="#" onclick="save(${isUpProCon });">保存</a>
			<a class="btn" href="#" onclick="goBack();">返回</a>   
		</div>	
		</div>	
		<!-- 操作按钮结束 -->
		</form>
	</div>
</div>
	</body>	
</html>

