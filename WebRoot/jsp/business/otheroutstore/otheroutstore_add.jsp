<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>材料领用申请</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqBootstrapValidation.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript">
			var price = 0 ;
			function openMaterial(){
				window.open("${pageContext.request.contextPath}/api/otherout/toChooseMaterialInfo","","width=1250,height=700,scrollbars=yes");
			}
			//删除行
			function delRow(rowId){
				var selectTable=document.getElementById("materialPlanTable");
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
					ptab.rows[i].cells[8].innerHTML = '<a href="javascript:void(0);" onclick="delRow('+i+')">删除</a>';
					materialNums[i-1].onChange= new Function("getTotalPrices("+(i-1)+")");
					getTotalPrice(i-1);
				}
			}
			
			function savePlan(){
				document.myform.action="${pageContext.request.contextPath}/api/otherout/saveOutStore";
				if(checkForm()){
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
			window.open("${pageContext.request.contextPath}/api/logRecords/getLogRecordsList?id1="+id1+"&name1=com.BusDrilling&id2="+id2+"&name2=com.BusInrepairInfo");
		}
		//$(function () {$("input,select,textarea").not("[type=submit]").jqBootstrapValidation();});
		
		function checkForm(){
			var materialNums = document.getElementsByName("materialNum");
			var flag = true;
			var re =/^[0-9]*$/;
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
		</script>
	</head>
	<body>
	<div class="container-fluid">
 		<div class="row-fluid">
   			<div class="span9">
   				<!-- 标题 start -->
     			<div class="page-header">
			        <h3>车间其他材料领用</h3>
     			</div>
     			<!-- 标题 end -->
     			<form name="myform" id="myform" action="" method="post">
     			<input type="hidden"  name="url" value="${pageContext.request.contextPath}/api/otherout/getOtherOutStoreList"/>
				<!-- 领用申请  start -->
				<div class="page-header">
					 <div class="row-fluid">
					 	<div style="margin:5px 0 5px 5px;"><a class="btn btn-medium" onclick="openMaterial();" href="#"><i class="icon-plus"></i>选择材料</a>
					 	<p class="text-right">
					 	时间<input id="outDate" name="outDate" class="input-small" type="text"  value="${param.startDate }"
						onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
					 	材料类型
		              	<select id="materialType" name="materialType" style="width: 110px;">
			            	<option>请选择</option>
							<c:forEach var="lev" items='${web:getDataItem("material_type")}'>
								<option value="${lev.dataItemValue}" <c:if test="${lev.dataItemValue==param.materialType}">selected</c:if> >
									${lev.dataItemName }
								</option>
							</c:forEach>
						</select>
					 	总计:<input id="price" name="price" class="input-mini" type="text" value="0"  readonly="readonly" /></p>
					 	</div>
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
									  <th>备注</th>
									  <th>操作</th>
									</tr>
		                            <tr>
		                            	<td colspan="10" style="text-align: center;color:red;">暂无数据！</td>
		                            </tr>   
			            	</table>
			            </div>	
			            <div id="errorMessage" style="display: none;"><font color="red">红色标识：必填项；黄色标识：请填写数字；</font></div>
	            	</div>
				<!-- 领用申请  end -->
				</form>
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
