<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<%@include file="/jsp/config/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
	  <script src="${pageContext.request.contextPath}/js/process_tracking/wz_jsgraphics.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/process_tracking/xmlextras.js" type="text/javascript"></script>
		<script type="text/javascript">
		var currentStepIds = [${steps}];
		var xmlHttp = XmlHttp.create();
		var async = true;
		xmlHttp.open("GET", "${path}", async);
		xmlHttp.onreadystatechange = function () {
    if (xmlHttp.readyState == 4){
        //set up graphics
        var jg = new jsGraphics("workflowCanvas");
        jg.setColor("#ff0000");
        jg.setStroke(3);
        var xAdjust = 2;
        var yAdjust = 2;
        var widthAdjust = 3;
        var heightAdjust = 2;
        
        //parsing xml and paint;
        var cells = xmlHttp.responseXML.getElementsByTagName("cell");
        for(var i = 0; i < currentStepIds.length; i++){
            for(var n = 0; n < cells.length; n++){
                var cell = cells[n];
                if(cell.getAttribute("type") == "StepCell" && currentStepIds[i] == parseInt(cell.getAttribute("id"))){
		                    jg.drawRect(parseInt(cell.getAttribute("x")) + xAdjust, parseInt(cell.getAttribute("y")) + yAdjust, parseInt(cell.getAttribute("width")) + widthAdjust, parseInt(cell.getAttribute("height")) + heightAdjust);
		                }
		            }
		        }
		        jg.paint();
		    }
		};
		xmlHttp.send(null);    
		</script>
		<script type="text/javascript">
		function goBack() {
			//window.location.href = "${pageContext.request.contextPath}/api/workflow/getBizInstanceList?currentPageNO=${param.currentPageNO}";
			window.close();
		 }
    </script>
	</head>
	<body>
		<div id="workflowCanvas"
			style="position: relative; height: 566px; width: 508px;">
			<br />	<br />
			<img src="${imgurl}" border=0 />
		  <div class="OptBar">
			<div class="OptbtnPos">
			 <a class="Bbtn" href="#" onclick="if(confirm('确定关闭当前页吗？')){window.close()};"><span class="Br">关闭</span></a>
				</div>
			</div>
		</div>
		
	</body>
</html>