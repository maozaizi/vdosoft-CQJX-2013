<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>材料信息管理列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script type="text/javascript">
	function doSearch(){
		document.form.action="${pageContext.request.contextPath}/api/busmechanic/getMaterialListByWorkCard";
		document.form.submit();
	}
	//确认选择材料
	function saveSelect(){
		//材料计划页面 选择材料table
	   	var materialPlanTable = window.opener.document.getElementById("materialPlanTable");
	   	//本页面 选择材料table
	   	var selectTable=document.getElementById("selectTable");
	   	//判断材料计划页面，是否存在 暂无数据，删除该行
   		if(materialPlanTable.rows.length==2&&materialPlanTable.rows[1].cells[0].innerHTML=='暂无数据！'){
			materialPlanTable.deleteRow(1);
   		}
   		var tablelength=materialPlanTable.rows.length;
	   	for (var i = 1; i<selectTable.rows.length; i++) {
	   		//页面选择后所填值  材料数量，备注，领料类型
	   		//将本页面 选择材料table 动态添加 到 材料计划页面
			var newTR =materialPlanTable.insertRow();
			newTR.id="materialPlanTr_"+(tablelength+i-1);
			
			newTR.insertCell(0).innerHTML=selectTable.rows[i].cells[0].innerHTML;
			newTR.insertCell(1).innerHTML=selectTable.rows[i].cells[1].innerHTML;
			newTR.insertCell(2).innerHTML=selectTable.rows[i].cells[2].innerHTML;
			newTR.insertCell(3).innerHTML=selectTable.rows[i].cells[3].innerHTML;
			newTR.insertCell(4).innerHTML=selectTable.rows[i].cells[4].innerHTML;
			//newTR.insertCell(5).innerHTML=selectTable.rows[i].cells[5].innerHTML;
			//循环将本页面填写值，赋值给父页面（材料计划页面）
			newTR.insertCell(5).innerHTML='<input id="materialNum" name="materialNum" onchange="getTotalPrices('+(tablelength+i-2)+')" class="input-mini" value="" type="text"  onkeyup="getTotalPrice('+(tablelength+i-2)+')"  required aria-invalid="true" /><p class="help-block"></p>';
			newTR.insertCell(6).innerHTML='<input id="totalPrice" name="totalPrice" class="input-mini" value="" type="text" readonly/><p class="help-block"></p>';
			newTR.insertCell(7).innerHTML='<select class="input-small" name="type"><option value="1">领用</option><option value="1">计划加领用</option><option value="2">加工</option><option value="3">修理</option></select>';
			newTR.insertCell(8).innerHTML='<input id="remark" name="remark" class="input-mini" value="" type="text"/>';
			newTR.insertCell(9).innerHTML='<a href="javascript:void(0);" onclick="delRow('+(tablelength+i-1)+')">删除</a>';
		}
		//window.opener.$("input,select,textarea").not("[type=submit]").jqBootstrapValidation();
		window.opener.document.getElementById("numSpan").innerHTML  = materialPlanTable.rows.length-1;
		window.close();
	}
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span11">
     			<!-- 搜索框开始 start -->
				<div class="row-fluid">
	    			<form class="form-inline" name="form" >
			    	<input type="hidden" name="id" id="id"/>
			    	<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/material/getList"/>
    	 				<!-- 搜索框开始 start -->
    	 				<fieldset>
            				<legend><span class="label label-info">按工卡号搜索历史物料申请信息</span></legend>
				        	<div class="span5">
				          		<div class="control-group">
				            		<label class="control-label" for="workCard">工卡号</label>
			              			<input id="workCard" name="workCard" class="input-large" type="text" value="${workCard}"/>
			              			<input id="busName" name="busName" type="hidden" value="${busName}"/>
				          		</div>
				        	</div>
				        	<div class="span5">
				          		<div class="control-group">
			              			 <a class="btn btn-info" href="javascript:void(0)" onclick="doSearch();"><i class="icon-search icon-white"></i>&nbsp;搜 索</a>
									<a class="btn btn-primary" href="javascript:void(0);"  onclick="saveSelect();" >确  定</a>
									<a class="btn"  href="javascript:void(0);" onclick="window.close();">关  闭</a>
				          		</div>
				        	</div>
				        </fieldset>
				        <!-- 搜索框 结束 end-->
		        	</form>
		       </div>
               	<c:if test="${not empty busMaterialPlanList}">
                <div class="table" style="height: 350px;overflow-y:auto;">
		      		<table class="table table-striped table-bordered" id="selectTable"> 
	                	<thead>
		                	<tr> 
		                        <th>物料描述</th>
		                        <th>物料组</th>
		                        <th>物料编码</th>
		                        <th>单位</th>
		                        <th>参考价（元）</th>
		                     </tr>
	                	</thead>
	                    <tbody>
		                    	<c:forEach items="${busMaterialPlanList}" var="material" varStatus="i">
			                        <tr>
			                            <td>${material.material_detail}<input type="hidden" name="materialDetail" value="${material.material_detail}"></td>
			                            <td>${material.material_group}<input type="hidden" name="materialGroup" value="${material.material_group}"></td>
			                            <td>${material.material_code}<input type="hidden" name="materialCode" value="${material.material_code}"></td>
			                            <td>${material.unity}<input type="hidden" name="unity" value="${material.unity}"></td>
			                            <td>${material.estimate_price}<input type="hidden" name="estimatePrice" value="${material.estimate_price}"></td>
			                        </tr>
		                        </c:forEach>
	                    </tbody>
	                </table>
			   </div>
               </c:if>
     		</div>
     	</div>
</html>
