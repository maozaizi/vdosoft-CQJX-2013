<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>材料互转列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">
	
	function openMaterial(){
		var cq1 = document.getElementById("cq1").value;
		var cj1 = document.getElementById("cj1").value;
		var cq2 = document.getElementById("cq2").value;
		var cj2 = document.getElementById("cj2").value;
		if(cq1=="" || cq2==""){
			alert('请选择厂区!');
			return ;
		}
		if(cj1=="" || cj2==""){
			alert('请选择车间!');
			return ;
		}
		window.open("${pageContext.request.contextPath}/api/businrepairinfo/toStoreInfo?cj1="+cj1,"","width=1250,height=700,scrollbars=yes");
	}
	//删除行
			function delRow(rowId){
				var selectTable=document.getElementById("storePlanTable");
				var signItem = document.getElementById( "materialPlanTr_"+rowId);
				//获取将要删除的行的Index
				var rowIndex = signItem.rowIndex;
				selectTable.deleteRow(rowIndex);
				//材料计划列表，删除所有选择信息，显示暂无数据
				if(selectTable.rows.length==1){
					var newTR = selectTable.insertRow(1);
					var mark = newTR.insertCell(0);
					mark.colSpan=11;
					mark.style="text-align:center;color:red;";
					mark.innerHTML="暂无数据！";
				}
				//重新绘制ID
				repaintNumber();
			}
			
			//重新绘制ID
			function repaintNumber(){
				var ptab = document.getElementById("storePlanTable");
				var materialNums = document.getElementsByName("materialNum");
				price = 0;
				for (var i = 1; i<ptab.rows.length; i++) {
					ptab.rows[i].id="materialPlanTr_"+i;
					ptab.rows[i].cells[12].innerHTML = '<a href="javascript:void(0);" onclick="delRow('+i+')">删除</a>';
					///materialNums[i-1].onChange= new Function("getTotalPrices("+(i-1)+")");
					//getTotalPrice(i-1);
				}
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
		
		function savePlan(){
				document.myform.action="${pageContext.request.contextPath}/api/businrepairinfo/saveOutStoreDetail";
				document.myform.submit();
				//if(checkForm()){
				//	$('#myform').submit();
				//}else{
				//	document.getElementById("errorMessage").style="display:block;";
				//}
			}
		function checkForm(){
			var materialNums = document.getElementsByName("num");
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
	$("document").ready(function(){
			$("#cq1").bind('change',function(){
				var data = {"cq":$(this).val()};
				$('#cj1').html("<option value=''>请选择</option>");
				$.ajax({
					data:data,
					url:'${pageContext.request.contextPath}/ajaxapi/businrepairinfo/getWorkshopList',
					dataType:'json',
					success:function(data,status,jaxData){
						//platoon
						for(var i = 0;i<data.workshoplist.length;i++){
							var org = data.workshoplist[i];
							var option = "<option value = '"+org.org_code+"'>"+org.org_name+"</option>";
							$('#cj1').append(option);
						};
					},
					error:function(){
						alert('获取车间信息失败！');
					},
					timeout:5000
				});
			});
			$("#cq2").bind('change',function(){
				var data = {"cq":$(this).val()};
				$('#cj2').html("<option value=''>请选择</option>");
				$.ajax({
					data:data,
					url:'${pageContext.request.contextPath}/ajaxapi/businrepairinfo/getWorkshopList',
					dataType:'json',
					success:function(data,status,jaxData){
						//platoon
						for(var i = 0;i<data.workshoplist.length;i++){
							var org = data.workshoplist[i];
							var option = "<option value = '"+org.org_code+"'>"+org.org_name+"</option>";
							$('#cj2').append(option);
						};
					},
					error:function(){
						alert('获取车间信息失败！');
					},
					timeout:5000
				});
			});
			
			
		});
</script>
</head>
<body>
	<div class="container-fluid">
		<div>
  			<h3>材料互转</h3>
		</div>
		<form name="myform" id="myform" action="" method="post">
  		<div class="row-fluid">
    		<div class="span9">
     			<!-- 搜索框开始 start -->
				<div class="row-fluid">
			    	<input type="hidden" name="mainCard" id="mainCard"/>
			    	<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/businrepairinfo/toManagerMoneyShare"/>
    	 				<!-- 搜索框开始 start -->
    	 				<fieldset>
            				<legend><span class="label label-info">请选择材料互转的厂区及车间</span></legend>
				        	<div class="span4">
				          		<div class="control-group">
				          		  <p>
			              		<label for="repairType">厂区</label>
				                <select class="span5" name="cq1" id="cq1">
					              <option value="">请选择</option>
					              <option value="|CQ|.|JX|.|QY|">庆阳厂</option>
					              <option value="|CQ|.|JX|.|YC|">银川厂</option>
					            </select>
					           
					            <label for="repairType">车间</label>
				                <select class="span5" name="cj1" id="cj1">
				                 <option value="">请选择</option>
					            </select>
					             </p>
				          		</div>
				        	</div>
				        	<div class="span1">
				        	<h4>转入</h4>
				        	</div>
				        	<div class="span4">
				        		<div class="control-group">
				            		  <p>
			              		<label for="repairType">厂区</label>
				                <select class="span5" name="cq2" id="cq2">
					              <option value="">请选择</option>
					              <option value="|CQ|.|JX|.|QY|">庆阳厂</option>
					              <option value="|CQ|.|JX|.|YC|">银川厂</option>
					            </select>
					           
					            <label for="repairType">车间</label>
				                <select class="span5" name="cj2" id="cj2">
					              <option value="">请选择</option>
					            </select>
					             </p>
				          		</div>
				        	</div>
				        </fieldset>
				        <!-- 搜索框 结束 end-->
		       </div>
		       <div class="table">
		       <legend><span class="label label-info">请选择转出材料的库存列表</span></legend>
		       <div style="margin:5px 0 5px 5px;"><a class="btn btn-medium" onclick="openMaterial();" href="#"><i class="icon-plus"></i>库存查询</a></div>
		      		<table class="table table-striped table-bordered" id="storePlanTable"> 
	                	<thead>
		                	<tr> 
		                        <th>物料描述</th>
		                        <th>物料组</th>
		                        <th>物料编码</th>
		                        <th>参考价</th>
		                        <th>单价</th>
		                        <th>数量</th>
		                        <th>转入数量</th>
		                        <th>单位</th>
		                        <th>车间编号</th>
								<th>备注</th>
								<th>操作</th>
		                     </tr>
	                	</thead>
	                    <tbody>
	                    	<tr>
                            	<td colspan="11" style="text-align: center;color:red;">暂无数据！</td>
                            </tr>  
	                    	
	                    </tbody>
	                </table>
				   <div class="page">
						<%@include file="/jsp/public/standard.jsp"%>
				   </div>
			   </div>
     		</div>
     	</div>
     	</form>
     	<div class="form-actions">
			  		<a class="btn" href="javascript:void(0)" onclick="back();">取 消</a> 
				  	<a class="btn btn-primary" href="javascript:void(0);" onclick="savePlan();">提 交</a>
			  	</div>
<!-- 列表结束 end -->
</body>
</html>
