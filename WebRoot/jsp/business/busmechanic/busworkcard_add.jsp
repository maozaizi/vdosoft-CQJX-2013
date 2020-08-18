<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>施工卡添加</title>
<link href="${pageContext.request.contextPath}/css/u2aframework.css"" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function openeq(name,code){
		window.open("${pageContext.request.contextPath}/api/busmechanic/toUserInfoChoose?receiveName="+name+"&receiveCode="+code,"","width=600,height=400,,scrollbars=yes");
	}
	function saveChoose(id){
         window.opener.document.getElementById("mainRepair"+id).value=document.getElementById("mainRepair").value;
         window.opener.document.getElementById("assistRepair"+id).value=document.getElementById("assistRepair").value;
         var mainRepair=document.getElementById("mainRepairCode").value+"@"+document.getElementById("mainRepair").value;
         var assistRepair=document.getElementById("assistRepairCode").value+"@"+document.getElementById("assistRepair").value;
         window.opener.document.getElementById("mainRepairCode"+id).value=mainRepair;
         window.opener.document.getElementById("assistRepairCode"+id).value=assistRepair;
         window.opener.document.getElementById("id"+id).value=document.getElementById("id").value;
         window.opener.document.getElementById("appointDate"+id).value=document.getElementById("appointDate").value;
         window.opener.document.getElementById("startDate"+id).value=document.getElementById("startDate").value;
         window.opener.document.getElementById("orderEndDate"+id).value=document.getElementById("orderEndDate").value;
         window.opener.document.getElementById("repairContent"+id).value=document.getElementById("repairContent").value;
         window.close();
	}
</script>
</head>
<body>
<div class="row" style="margin-top:20px;">
	<div  class="span23 offset1">
    	<form class="form-horizontal" id="myform" method="post" action="${pageContext.request.contextPath}/api/busmechanic/saveBusWorkCard" >
    	<input id="id" name="id" type="hidden" value="${busWorkCardMap.id}"/>
    	<div class="row">
			<div class="span23 offset1">
				<h4 class="title-color">
					派工单
				</h4>
			</div>
		</div>
    	<div class="row">
        	<div class="span10">
        	   <div class="control-group">
            		<label class="control-label" for="workCard">施工卡号</label>
            		<div class="controls">
              			<input id="workCard" name="workCard" class="input-large" type="text" readonly="readonly" value="${busWorkCardMap.workCard}" />
            		</div>
          		</div>
          		<div class="control-group">
            		<label class="control-label" for="mainRepair">主修人</label>
            		<div class="controls">
            		    <input id="mainRepairCode" name="mainRepairCode" type="hidden" value=""/>
              			<input id="mainRepair" name="mainRepair" class="input-medium" type="text" readonly="readonly" /><a class="btn btn-small" href="#" onclick="openeq('mainRepair','mainRepairCode');"><i class="icon-plus"></i>选择</a>
            		</div>
          		</div>
          		<div class="control-group">
            		<label class="control-label" for="assistRepair">辅修人</label>
            		<div class="controls">
            		    <input id="assistRepairCode" name="assistRepairCode" type="hidden" value=""/>
              			<input id="assistRepair" name="assistRepair" class="input-medium" type="text" readonly="readonly" /><a class="btn btn-small" href="#" onclick="openeq('assistRepair','assistRepairCode');"><i class="icon-plus"></i>选择</a>
            		</div>
          		</div>
          		<div class="control-group">
            		<label class="control-label" for="appointDate">派工日期</label>
	            		<div class="controls">
		               <input id="appointDate" name="appointDate" class="input-medium" type="text"  value=""
		            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
		            </div>
          		</div>
          		<div class="control-group">
            		<label class="control-label" for="startDate">开工日期</label>
	            		<div class="controls">
		               <input id="startDate" name="startDate" class="input-medium" type="text"  value=""
		            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
		            </div>
          		</div>
          		<div class="control-group">
            		<label class="control-label" for="orderEndDate">指定完成日期</label>
	            		<div class="controls">
		               <input id="orderEndDate" name="orderEndDate" class="input-medium" type="text"  value=""
		            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
		            </div>
          		</div>
          		<div class="control-group">
            		<label class="control-label" for="repairContent">车间修理意见</label>
            		<div class="controls">
								<textarea rows="2" class="span12" name="repairContent" id="repairContent"></textarea>
					</div>
           	   </div>
        	</div>
    	</div>
   		</form>
   	</div>
 </div>  		
	<div class="row" style="margin-bottom:50px;">
		<div class="span10 offset9">
			<p>
				<button class="btn btn-primary" onclick="saveChoose('${busWorkCardMap.id}');">确  定</button>
				<button class="btn" onclick="window.close();">关  闭</button>
			</p>
		</div>
	</div>
	
</body>
</html>
