<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
		
      <head>
        <title>Uploadify</title>
        <link href="${pageContext.request.contextPath}/css/uploadify/default.css" rel="stylesheet" type="text/css" />
        <link href="${pageContext.request.contextPath}/css/uploadify/uploadify.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/uploadify/jquery-1.3.2.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/uploadify/swfobject.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/uploadify/jquery.uploadify.v2.1.0.js"></script>
        <script type="text/javascript">
        $(document).ready(function() {
            $("#uploadify").uploadify({
                'uploader'       : '${pageContext.request.contextPath}/js/uploadify/uploadify.swf',//指定上传控件的主体文件，默认‘uploader.swf’
                'script'         : '${pageContext.request.contextPath}/servlet/Upload',//指定服务端处理类的入口
                'cancelImg'      : '${pageContext.request.contextPath}/img/uploadify/cancel.png',
                'folder'         : 'uploads',//要上传到的服务器路径，默认‘/’
                'queueID'        : 'fileQueue',
                'auto'           : false,//是否选取文件后自动上传
                'multi'          : true,//是否支持多文件上传
                'fileDesc':'请选择rar doc pdf文件',
                'fileExt':'*.doc;*.pdf;*.rar;*.zip;*.xls',
                'height'         : 30,   
	            'width'          : 120,
                'buttonImg'      : '${pageContext.request.contextPath}/img/browse-btn.png', 
                'simUploadLimit' : 1,//每次最大上传文件数 
                'wmode'          : 'transparent' ,   
                'onComplete'     : function (event, queueID, fileObj, response, data){    
               		
                  $('<li></li>').appendTo('.files').text(response); },    
               'onError'          : function(event, queueID, fileObj){ 
                   alert("文件:" + fileObj.name + " 上传失败");} 
            });
            // $("#uploadify2").uploadify({
            //   'uploader'       : 'js/uploadify.swf',
             //  'script'         : 'servlet/Upload',
             //  'cancelImg'      : 'images/cancel.png',
             //  'folder'         : 'uploads',
             //  'auto'           : false,
             //  'multi'          : true,
             //  'simUploadLimit' : 2,
            //     'fileDesc':'请选择rar doc pdf文件',
             //   'fileExt':'*.doc;*.pdf;*.rar',
             //   'height'         : 34,   
	         //   'width'          : 118,
             //   'buttonImg'      : 'images/picture.jpg', 
             //   'wmode'          : 'transparent',   
              //  onComplete       : function (event, queueID, fileObj, response, data){    
              //    $('<li></li>').appendTo('.files').text(response); },    
              //  onError          : function(event, queueID, fileObj){ 
               //    alert("文件:" + fileObj.name + " 上传失败");} 
             // });
        });
        function callback(msg)   
		{   
			alert('123123');
		    document.getElementById("msg").innerHTML = "<font color=red>"+msg+"</font>";   
		} 
        </script>
    </head>
    <body>
        <!--  -->
        <input cla type="file" name="uploadify" id="uploadify" />
        <p>
        <a href="javascript:jQuery('#uploadify').uploadifyUpload()">开始上传</a>&nbsp;
        <a href="javascript:jQuery('#uploadify').uploadifyClearQueue()">取消所有上传</a>
        </p>
        <div id="fileQueue">
        	<div id="msg" class="div3">文件上传中...</div>
        </div>
    </body>
</html>