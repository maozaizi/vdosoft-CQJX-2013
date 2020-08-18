<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<%@include file="/jsp/config/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>流程管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/js/uploadify/uploadify.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/uploadify/jquery.uploadify.min.js"></script>
		
		<script type="text/javascript">
		$(document).ready(function() {
            $("#processContent").uploadify({
                height         :  	30,
               	buttonClass    :  	'btn',
               	buttonText     :  	'选择上传文件',
			   	swf            :  	'${pageContext.request.contextPath}/js/uploadify/uploadify.swf',
				uploader       :  	'${pageContext.request.contextPath}/ajaxapi/fileUpload/upload',
				width          :  	120,
				auto           : 	true,//是否选取文件后自动上传
                multi          : 	false,//是否支持多文件上传
                fileTypeExts   : 	'*.xml',
                onUploadSuccess       : function (file, data, response){
                	var jsondata =  JSON.parse(data);  //以JSON的形式处理文件信息包括：路径filepath,名称name等
                	var filelist = jsondata.filelist;
    				$("#processContentUrl").val(filelist[0].filepath);
    				$("#processContentdiv").show();
    				$("#processContentdis").html(filelist[0].filename+"("+filelist[0].filesize+"bytes)");
    				} 
            });
            $("#deleteprocessContent").click(function(){
            	$("#processContentdis").html("");
            	$("#processContentdiv").css("display","none");;
            	$("#processContentUrl").val("");
            });
            
            $("#processPictures").uploadify({
                height         :  	30,
               	buttonClass    :  	'btn',
               	buttonText     :  	'选择上传文件',
			   	swf            :  	'${pageContext.request.contextPath}/js/uploadify/uploadify.swf',
				uploader       :  	'${pageContext.request.contextPath}/ajaxapi/fileUpload/upload',
				width          :  	120,
				auto           : 	true,//是否选取文件后自动上传
                multi          : 	false,//是否支持多文件上传
                fileTypeExts   : 	'*.png',
                onUploadSuccess       : function (file, data, response){
                	var jsondata =  JSON.parse(data);  //以JSON的形式处理文件信息包括：路径filepath,名称name等
                	var filelist = jsondata.filelist;
    				$("#processPicturesUrl").val(filelist[0].filepath);
    				$("#processPicturesdiv").show();
    				$("#processPicturesdis").html(filelist[0].filename+"("+filelist[0].filesize+"bytes)");
    				} 
            });
            $("#deleteprocessPictures").click(function(){
            	$("#processPicturesdis").html("");
            	$("#processPicturesdiv").css("display","none");;
            	$("#processPicturesUrl").val("");
            });
            
            $("#processPage").uploadify({
                height         :  	30,
               	buttonClass    :  	'btn',
               	buttonText     :  	'选择上传文件',
			   	swf            :  	'${pageContext.request.contextPath}/js/uploadify/uploadify.swf',
				uploader       :  	'${pageContext.request.contextPath}/ajaxapi/fileUpload/upload',
				width          :  	120,
				auto           : 	true,//是否选取文件后自动上传
                multi          : 	false,//是否支持多文件上传
                fileTypeExts   : 	'*.xml',
                onUploadSuccess       : function (file, data, response){
                	var jsondata =  JSON.parse(data);  //以JSON的形式处理文件信息包括：路径filepath,名称name等
                	var filelist = jsondata.filelist;
    				$("#processPageUrl").val(filelist[0].filepath);
    				$("#processPagediv").show();
    				$("#processPagedis").html(filelist[0].filename+"("+filelist[0].filesize+"bytes)");
    				} 
            });
            $("#deleteprocessPage").click(function(){
            	$("#processPagedis").html("");
            	$("#processPagediv").css("display","none");;
            	$("#processPageUrl").val("");
            });  
        });
		
		
		function save(){
			if($("#processContentUrl").val()==""){
				alert("请上传流程文件!");
				return;
			}
			document.forms[0].action="${pageContext.request.contextPath}/api/workflow/addProcessDirectory";
			document.forms[0].submit();
		}
		
		<%--返回 --%>
		function goBack() {
		   	window.location.href = "${pageContext.request.contextPath}/api/workflow/getProcessInfo?tempPat=0&processId=${parentId}";
		 }
		</script>
	</head>
	<body>
		<div >
			<div>
				<h3>
					您当前的位置：添加流程
				</h3>
				<form id="addForm" method="post" action="">
					<input type="hidden" name="parentId" id="parentId"
						value="${parentId}" />
					<input type="hidden" name="flag" id="flag" value="${flag}" />
					<input type="hidden" name="url" id="url"
						value="${pageContext.request.contextPath}/api/workflow/getProcessInfo" />
				<div class="form-horizontal">
					<div class="control-group">
						<label class="control-label" for="strname">
							流程名称
						</label>
						<div class="controls">
							<input type="text" name="strname" id="strname" class="ipt_text"
								size="30" />
						</div>
					</div>
					<c:if test="${flag =='1'}">
						<div class="control-group">
							<label class="control-label" for="processCode">
								流程编码
							</label>
							<div class="controls">
								<input type="text" name="processCode" id="processCode"
									class="ipt_text" size="30" />
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="processContent">
								上传流程文件
							</label>
							<div class="controls">
								<input type="file" name="processContent" id="processContent" />
								<input type="hidden" name="processContentUrl"
									id="processContentUrl" />
								<div id="processContentdiv" style="display: none">
									<span style="height: 80px; width: 100px" id="processContentdis"></span>
									<a href="#" id="deleteprocessContent">删除</a>
								</div>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="processContent">
								上传流程图片文件
							</label>
							<div class="controls">
								<input type="file" name="processPictures" id="processPictures" />
								<input type="hidden" name="processPicturesUrl"
									id="processPicturesUrl" />
								<div id="processPicturesdiv" style="display: none">
									<span style="height: 80px; width: 100px"
										id="processPicturesdis"></span>
									<a href="#" id="deleteprocessPictures">删除</a>
								</div>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="processContent">
								上传流程界面文件
							</label>
							<div class="controls">
								<input type="file" name="processPage" id="processPage" />
								<input type="hidden" name="processPageUrl" id="processPageUrl" />
								<div id="processPagediv" style="display: none">
									<span style="height: 80px; width: 100px" id="processPagedis"></span>
									<a href="#" id="deleteprocessPage">删除</a>
								</div>
							</div>
						</div>
					</c:if>
							<div class="form-actions">
					<a class="btn btn-primary" href="#" onclick="save();">保存</a>
						<a class="btn" href="#" onclick="goBack();">返回</a>
				</div>
				</div>
				</form>
			</div>
		</div>
	</body>
</html>

