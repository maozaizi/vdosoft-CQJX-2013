<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>批量材料计划</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqBootstrapValidation.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script type="text/javascript">
			var price = 0 ; 
			function openMaterial(){
				window.open("${pageContext.request.contextPath}/api/mechtreatment/toChooseMaterialInfo","","width=1250,height=700,scrollbars=yes");
			}
			//删除行
			function delRow(rowId){
				var selectTable=document.getElementById("materialPlanTable");
				var signItem = document.getElementById("materialPlanTr_"+rowId);
				//获取将要删除的行的Index
				var rowIndex = signItem.rowIndex;
				selectTable.deleteRow(rowIndex);
				document.getElementById("numSpan").innerHTML  = selectTable.rows.length-1;
				//重新绘制ID
				repaintNumber();
				//材料计划列表，删除所有选择信息，显示暂无数据
				if(selectTable.rows.length==1){
					var newTR = selectTable.insertRow(1);
					var mark = newTR.insertCell(0);
					mark.colSpan=11;
					mark.style="text-align:center;color:red;";
					mark.innerHTML="暂无数据！";
				}
			}
			
			//重新绘制ID
			function repaintNumber(){
				var ptab = document.getElementById("materialPlanTable");
				var materialNums = document.getElementsByName("materialNum");
				price = 0;
				for (var i = 1; i<ptab.rows.length; i++) {
					ptab.rows[i].id="materialPlanTr_"+i;
					ptab.rows[i].cells[9].innerHTML = '<a href="javascript:void(0);" onclick="delRow('+i+')">删除</a>';
					materialNums[i-1].onChange= new Function("getTotalPrices("+(i-1)+")");
					getTotalPrice(i-1);
				}
			}
			
			function savePlan(){
				document.myform.action="${pageContext.request.contextPath}/api/busplanwf/saveBusMaterialPlanWf";
				if(checkForm()){
					var estimatePrices = document.getElementsByName("estimatePrice");
					//for(var i = 0 ;i < estimatePrices.length;i++ ){
				   	//	if(estimatePrices[i].value!=null&&parseFloat(estimatePrices[i].value)>10000&&document.getElementById("bigRemark").value==""){
				   	//		alert("请填写大件材料领用原因！")
				   	//		return false;
				   	//	}
			   		//}
					$('#myform').submit();
				}else{
					document.getElementById("errorMessage").style="display:block;";
				}
			}
			
			function back(){
				document.myform.action="${pageContext.request.contextPath}/api/mywork/getMyWork";
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
	    function openeq(id1,id2){
			window.open("${pageContext.request.contextPath}/api/logRecords/getLogRecordsList?id1="+id1+"&name1=com.BusElectrical&id2="+id2+"&name2=com.BusInrepairInfo");
	    }
		///$(function () {$("input,select,textarea").not("[type=submit]").jqBootstrapValidation(); } );
		
		function checkForm(){
			var materialNums = document.getElementsByName("materialNum");
			var flag = true;
			var re =/^\d{1,12}(?:\.\d{1,2})?$/;
			for(var i=0;i<materialNums.length;i++){
				if(materialNums[i].value==""){
					materialNums[i].style["borderColor"]="red";
					flag=false;
				}else{
					if(!re.test(materialNums[i].value)){
						materialNums[i].style["borderColor"]="#C09853";
						flag=false;
					}else{
						materialNums[i].style["borderColor"]="";
					}
				}
			}
			return flag;
		}
		function getTotalPrice(index){
			var totalPrice =0;
			var materialNums = document.getElementsByName("materialNum");
			var estimatePrices = document.getElementsByName("estimatePrice");
	   		if(materialNums[index].value!=null&&materialNums[index].value!=""){
	   			totalPrice =multiplication(parseFloat(materialNums[index].value),parseFloat(estimatePrices[index].value));
	   			document.getElementsByName("totalPrice")[index].value=totalPrice;
	   		}
	   		price = addition(price,totalPrice) ;
	   		document.getElementById("price").value = price;
		}
		function getTotalPrices(index){
			var totalPrice =0;
			var materialNums = document.getElementsByName("materialNum");
			var estimatePrices = document.getElementsByName("estimatePrice");
			var reg=/^\d+(?=\.{0,1}\d+$|$)/
			var e =  materialNums[index];
			if (e.value != "") {        
			 if (!reg.test(e.value)) {    
			 alert("请输入正确的数字");       
			 e.value = "";  
			 e.focus();
			 return false;
			 }
			 }
	   		if(materialNums[index].value!=null&&materialNums[index].value!=""){
	   			totalPrice =multiplication(parseFloat(materialNums[index].value),parseFloat(estimatePrices[index].value));
	   			document.getElementsByName("totalPrice")[index].value=totalPrice;
	   		}
	   		var ptab = document.getElementById("materialPlanTable");
	   		price = 0 ;
			for (var i = 0; i<ptab.rows.length-1; i++) {
				var totalPrice =0;
		   		if(materialNums[i].value!=null&&materialNums[i].value!=""){
		   			totalPrice =multiplication(parseFloat(materialNums[i].value),parseFloat(estimatePrices[i].value));
		   			price = addition(price,totalPrice);
		   		}
			}
			document.getElementById("price").value = price;
		}
		function multiplication(num1,num2)
		{
		     var m=0,s1=num1.toString(),s2=num2.toString();
		     try{m+=s1.split(".")[1].length}catch(e){};
		     try{m+=s2.split(".")[1].length}catch(e){};
		     return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
		}
		//加法
		function addition(num1,num2){
		      return  Number((num1+num2).toFixed(2));
		}
		function loadpage(){
			var totalPrice =0;
			var price = 0 ;
			var materialNums = document.getElementsByName("materialNum");
			var estimatePrices = document.getElementsByName("estimatePrice");
			var ptab = document.getElementById("materialPlanTable");
			for (var i = 0; i<ptab.rows.length-1; i++) {
				var totalPrice =0;
		   		if(materialNums[i].value!=null&&materialNums[i].value!=""){
		   			totalPrice =multiplication(parseFloat(materialNums[i].value),parseFloat(estimatePrices[i].value));
		   			price = addition(price,totalPrice);
		   		}
			}
			document.getElementById("price").value = price;
		}
		</script>
	</head>
	<body onload="loadpage();">
	<div class="container-fluid">
 		<div class="row-fluid">
   			<div class="span9">
   				<!-- 标题 start -->
     			<div class="page-header">
			        <h3>批量材料计划</h3>
     			</div>
     			<!-- 标题 end -->
     			<form name="myform" id="myform" action="" method="post">
     			<input type="hidden" name="stepId" id="stepId" value="${stepId }" />
				<input type="hidden" name="nodeId" id="nodeId" value="${nodeId }"/>
				<input type="hidden" name="instanceId" id="instanceId" value="${instanceId }"/>
				<input  type="hidden" name="objId" value="${busPlanInfoWfMap.id}"/>
				<input  type="hidden" name="orgCode" value="${orgCode}"/>
				<input  type="hidden" name="serialnum" value="${serialnum}"/>
				<input type="hidden" name="url" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
				<!-- 领用申请  start -->
				<div class="page-header">
					<h4>材料计划（共<span id="numSpan">0</span>项材料）</h4>
					 <div class="row-fluid">
					 	<div style="margin:5px 0 5px 5px;"><a class="btn btn-medium" onclick="openMaterial();" href="#"><i class="icon-plus"></i>选择材料</a></div>
					 		<p class="text-right">总计:<input id="price" name="price" class="input-mini" type="text" value="0"  readonly="readonly" /></p>
			            	<table class="table table-striped table-bordered" id="materialPlanTable">
		            			<tbody>
									<tr>
									  <th>物料描述</th>
						              <th>物料组</th>
						              <th>物料编码</th>
									  <th>单位</th>
									  <th>参考价（元）</th>
									  <th>数量</th>
									  <th>金额</th>
									  <th>类型</th>
									  <th>备注</th>
									  <th>操作</th>
									</tr>
									<c:if test="${empty busMaterialPlanWfList}">
			                            <tr>
			                            	<td colspan="10" style="text-align: center;color:red;">暂无数据！</td>
			                            </tr>   
			                        </c:if>
			                         <c:set var="mnum" value="0"/>
			                        <c:if test="${not empty busMaterialPlanWfList}">
			                            <c:forEach items="${busMaterialPlanWfList}" var="busMaterialPlan" varStatus="i">
			                            	<c:set var="mnum" value="${mnum+1}"/>
			                                <tr id="materialPlanTr_${i.index+1}">
			                                    <td>${busMaterialPlan.materialGroup}</td>
			                                    <td>${busMaterialPlan.materialCode}</td>
			                                    <td>${busMaterialPlan.materialDetail}</td>
			                                    <td>${busMaterialPlan.unity}</td>
			                                    <td>${busMaterialPlan.estimatePrice}</td>
			                                    <td><input id="materialNum" name="materialNum" class="input-mini" type="text" value="${busMaterialPlan.materialNum}"/></td>
			                                    <td><input id="totalPrice" name="totalPrice" class="input-mini" type="text" readonly="readonly" value="${busMaterialPlan.totalPrice}"/></td>
			                                    <td>
				                                    <select class="input-small" name="type">
					                                    <option value="4" <c:if test="${busMaterialPlan.type==4}">selected</c:if>>计划加领用</option>
					                                    <option value="1" <c:if test="${busMaterialPlan.type==1}">selected</c:if>>领用</option>
					                                    <option value="2" <c:if test="${busMaterialPlan.type==2}">selected</c:if>>加工</option>
					                                    <option value="3" <c:if test="${busMaterialPlan.type==3}">selected</c:if>>修理</option>
				                                    </select>
			                                    </td>
			                                    <td><input id="remark" name="remark" class="input-mini" type="text" value="${busMaterialPlan.remark}"/></td>
			                                    <td><a href="javascript:void(0);" onclick="delRow(${i.index+1})">删除</a></td>
			                                </tr>
			                            </c:forEach>
			                        </c:if>
			            	</table>
			            	</div>	
			            <div id="errorMessage" style="display: none;"><font color="red">红色标识：必填项；黄色标识：请填写数字；</font></div>		
	            	</div>
				<!-- 领用申请  end -->
				<div class="page-header">
					<h4>申请编号</h4>
	        		<div class="row-fluid">
	        			<div class="span5">
							<div class="control-group">
							  	<div class="controls">
							  		<c:if test="${not empty busPlanInfoWfMap.planCode }">
							  			<input class="input-large" id="planCode" readonly name="planCode" type="text" required value="${busPlanInfoWfMap.planCode }" readonly="readonly" aria-invalid="true" />
							  		</c:if>
							  		<c:if test="${empty busPlanInfoWfMap.planCode }">
							  			<span id="codeSpan"></span><input class="input-large" id="planCode" name="planCode" type="text" required value="${planCode}" readonly="readonly" aria-invalid="true" />
							  		</c:if>
								</div>
							</div>
						</div>
					</div>
					
									<!-- 备注 start -->
				<div>
					<h4>备注</h4>
		            		<div>    
				        			<ul class="inline">
								    <li><p class="muted">请输入1-500字的存在问题</p></li>
								    <li><p id="prlable" class="text-success">已输入0字符</p></li>
								    </ul>	   
							</div>
					<p><textarea rows="2" class="span12" name="memo" id="memo" onkeyup="textCounter(memo,prlable,500);">${busPlanInfoWfMap.planRemark }</textarea></p>
				</div>
				</div>	

				</form>
				<!-- 备注 end -->
				<!-- 操作按钮 start -->	    
    			<div class="form-actions">
			  		<a class="btn" href="javascript:void(0)" onclick="back();">取 消</a> 
				  	<a class="btn btn-primary" href="javascript:void(0);" onclick="savePlan();">提 交</a>
			  	</div>
				<!-- 操作按钮 end -->
				<c:if test="${not empty logRecordsList}">
				<div class="page-header">
				<h4>操作历史记录</h4>
		  		<div class="table">
				  	  <table class="table table-striped table-bordered"> 
		                 	<tr>  
								<div class="row form-horizontal">
								<th>操作动态<span style="float:right"><a href="javascript:void(0);" onclick="openeq('${busPlanInfoWfMap.id}','1');">更多>></a></span></th>
								</div>
							</tr>
					  </table>
					  <table class="table table-striped table-bordered">
				          	<tbody>
					           	<c:if test="${not empty logRecordsList}">
				            	<c:forEach items="${logRecordsList}" var="logRecords" varStatus="i">
				            	<c:if test="${(i.index+1)<6}"> 
				                 <tr>
				                     <td> ${logRecords.remark}</td>
				                 </tr>
				                 </c:if>
				                </c:forEach>
				               </c:if>
						 	</tbody>
				        </table>
				</div>
				</div>
				</c:if>
			</div>
		</div>
	</div>
	</body>
</html>
