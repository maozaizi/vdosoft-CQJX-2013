<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>材料信息管理列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/js/uploadify/uploadify.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqBootstrapValidation.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
            $("#uploadify").uploadify({
               	height         :  30,
               	buttonClass    :  'btn',
               	buttonText     :  '选择上传文件',
			   	swf            :  '${pageContext.request.contextPath}/js/uploadify/uploadify.swf',
				uploader       :  '${pageContext.request.contextPath}/ajaxapi/fileUpload/upload',
				width          :  120,
				auto           : true,//是否选取文件后自动上传
                multi          : true,//是否支持多文件上传
                fileTypeExts   :'*.xls',
                fileTypeDesc   :'请选择EXCEL',
                fileSizeLimit  :'51200',
                onUploadSuccess: function(file, data, response){
                	var jsondata =  JSON.parse(data);  //以JSON的形式处理文件信息包括：路径filepath,名称name等
                	var filelist = jsondata.filelist;
    				var fileUrl = filelist[0].filepath;
    				var filename = filelist[0].filename;
    				var filesize = filelist[0].filesize;
    				var fileext = filelist[0].fileext;
    				var params ="fileUrl="+fileUrl+"&docName="+filename+"&docSize="+filesize+"&docType="+fileext;
	   				$.ajax({
					   type: "POST",
					   url: "${pageContext.request.contextPath}/ajaxapi/material/saveMaterialInfoImport",
					   data: params,
					   dataType:"json",
					   success: function(ajaxdata){
				   	   		document.getElementById("message").innerHTML=ajaxdata.message;
				   	   		var flag = ajaxdata.flag;
				   	   		document.getElementById("attId").value=ajaxdata.attid;
				   	   		if(flag==1){
				   	   			//document.myform.submit();
				   	   		}
					   }
				    });
                   },    
               'onError'          : function(event, queueID, fileObj){ 
                   alert("文件:" + fileObj.name + " 上传失败");} 
            });
        });
        
        function back(){
        	location.href="${pageContext.request.contextPath}/api/material/getList";
        }
</script>
</head>
<body>
<div class="row">
  <div class="span23 offset1">
    <h4 class="title-color">物料信息导入</h4>
  </div>
</div>
<div class="row">
	<div class="span23 offset1">
		<div class="row">
			<dl class="dl-horizontal">
				<dt></dt>
				<dd>
					<p>
						<p><input type="file" name="uploadify" id="uploadify" /></p>
					</p>
					<p>
						导入说明
						1.导入文件的格式必须是xls格式，并且大小不超过50MB；
						<br />
						2.文件的内容格式必须完全依照模板进行编辑：填写前请仔细阅读批注；
						<br />
						3.请确定导入文件中除数量、单价、金额列其他列均为文本；
						<br />
						4.导入完成后请仔细阅读导入信息；
						<br />
						5.如果导入失败，请将excel中的数据有效性全部变为任何值；
						<br />
					</p>
				</dd>
			</dl>
			<dl class="dl-horizontal">
				<dd>
					<div id="message"></div>
				</dd>
			</dl>
			<dl class="dl-horizontal">
				<dd>
					<a class="btn btn-primary" href="javascript:void(0)" onclick="back();">&nbsp;返回</a>
				</dd>
			</dl>
		</div>
	</div>
</div>
</body>
</html>
