<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>设备信息管理列表</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqBootstrapValidation.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script type="text/javascript">
			function openMaterial(){
				window.open("${pageContext.request.contextPath}/api/busmechanic/toOutStoreMaterial","","width=1200,height=700,scrollbars=yes");
			}
			function delRow(rowId,useType){
			var selectTable=document.getElementById("outStoreTable4");
			var signItem = document.getElementById( "materialPlanTr_"+rowId);
			//获取将要删除的行的Index
			var rowIndex = signItem.rowIndex;
			selectTable.deleteRow(rowIndex);
			//重新绘制ID
			repaintNumber();
			//材料计划列表，删除所有选择信息，显示暂无数据
			if(selectTable.rows.length==1){
				var newTR = selectTable.insertRow(1);
				var mark = newTR.insertCell(0);
				mark.colSpan=10;
				mark.style="text-align:center;color:red;";
				mark.innerHTML="暂无数据！";
			}
		}
			
		//重新绘制ID
		function repaintNumber(){
			var ptab=document.getElementById("outStoreTable4");
			for (var i = 1; i<ptab.rows.length; i++) {
				ptab.rows[i].id="materialPlanTr_"+i;
				ptab.rows[i].cells[6].innerHTML = '<a href="javascript:void(0)" onclick="delRow('+i+',\'\')">删除</a>';
			}
		}
			
		function savePlan(){
			document.myform.action="${pageContext.request.contextPath}/api/storeDetail/saveOutStoreReturn";
			if(checkForm()){
				$('#myform').submit();
			}else{
				document.getElementById("errorMessage").style="display:block;";
			}
		}
		
		function back(){
			document.myform.action="${pageContext.request.contextPath}/api/storeDetail/getOutStoreReturnList";
			document.myform.submit();
		}
		// 函数，3个参数，表单名字，表单域元素名，限制字符；
	   function textCounter(field, countfield, maxlimit) {  
			var s=field.value.length;
			if(s>maxlimit){
				field.value=field.value.substr(0,maxlimit)
			}else{
				countfield.innerHTML="已输入"+s+"字符";
			} 
	
	   }
	  $(function () {$("input,select,textarea").not("[type=submit]").jqBootstrapValidation(); } );
		function checkForm(){
			var storeNums = document.getElementsByName("storeNum");
			var flag = true;
			var re =/^\d{1,12}(?:\.\d{1,2})?$/;
			for(var i=0;i<storeNums.length;i++){
				if(storeNums[i].value==""||!re.test(storeNums[i].value)){
					storeNums[i].style["borderColor"]="red";
					flag=false;
				}else{
					storeNums[i].style["borderColor"]="";
				}
			}
			return flag;
		}
		</script>
	</head>
	<body>
	<div class="container-fluid">
 		<div class="row-fluid">
   			<div class="span9">
   				<!-- 标题 start -->
     			<div class="page-header">
			        <h3>退料管理</h3>
     			</div>
     			<input type="hidden" name="useType" id="useType" value="4">
     			<!-- 标题 end -->
     			<!-- 基本信息 start -->
     			<form name="myform" id="myform" action="">
     			<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/storeDetail/getOutStoreReturnList">
				<div class="page-header">
					<h4>退料</h4>
					 <div class="row-fluid">
					 	<div style="margin:5px 0 5px 5px;"><a class="btn btn-medium" onclick="openMaterial();" href="javascript:void(0);"><i class="icon-plus"></i>选择物料</a></div>
			            	<table class="table table-striped table-bordered" id="outStoreTable4">
		            			<tbody>
									<tr>
									  <th>物料描述</th>
						              <th>物料组</th>
						              <th>物料编码</th>
									  <th>单位</th>
									  <th>数量</th>
									  <th>操作</th>
									</tr>
		                            <tr>
		                            	<td colspan="6" style="text-align: center;color:red;">暂无数据！</td>
		                            </tr>   
			            	</table>
			            </div>
			             <div id="errorMessage" style="display: none;"><font color="red">红色标识：必填项；黄色标识：请填写数字；</font></div>	
	            	</div>
				</form>
				<!-- 备注 end -->
				<!-- 操作按钮 start -->	    
    			<div class="form-actions">
			  		<a class="btn" href="javascript:void(0)" onclick="back();">取 消</a> 
				  	<a class="btn btn-primary" href="javascript:void(0);" onclick="savePlan();">提 交</a>
			  	</div>
				<!-- 操作按钮 end -->
			</div>
		</div>
	</div>
	</body>
</html>
