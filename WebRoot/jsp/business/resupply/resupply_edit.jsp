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
		<script type="text/javascript">
			var price = 0 ;
			function openMaterial(){
				window.open("${pageContext.request.contextPath}/api/businrepairinfo/toResupplyMaterialChoose","","width=1250,height=700,scrollbars=yes");
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
					ptab.rows[i].cells[9].innerHTML = '<a href="javascript:void(0);" onclick="delRow('+i+')">删除</a>';
					materialNums[i-1].onChange= new Function("getTotalPrices("+(i-1)+")");
					getTotalPrice(i-1);
				}
			}
			
			function savePlan(){
				document.myform.action="${pageContext.request.contextPath}/api/businrepairinfo/saveResupplyOutStore";
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
			        <h3>补充材料</h3>
     			</div>
     			<!-- 标题 end -->
     			<!-- 基本信息 start -->
     			<form name="myform" id="myform" action="" method="post">
				<input  type="hidden" name="busId" value="${busMap.id}"/>
				<input  type="hidden" name="busName" value="${tableName }"/>
				<input  type="hidden" name="url" value="${pageContext.request.contextPath}/api/businrepairinfo/getResupplyMainCardList"/>
				<input  type="hidden" name="workCard" value="${busMap.workCard}"/>
     			<div class="page-header">
		        	<h4>基本信息</h4>
		        	<div class="row-fluid">
		            	<div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>厂区：</dt>
		                        <dd>${companyName}
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>工卡号：</dt>
		                        <dd>${busMap.workCard}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>设备名称：</dt>
		                        <dd>${busInrepairInfoMap.equipmentName}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>机身编号：</dt>
		                        <dd>${busInrepairInfoMap.bodyCode}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理类别：</dt>
		                        <dd>
		                        <c:if test="${busInrepairInfoMap.repairType==1}">大修</c:if>
		                        <c:if test="${busInrepairInfoMap.repairType==2}">中修</c:if>
		                        <c:if test="${busInrepairInfoMap.repairType==3}">检修</c:if>
		                        </dd>
		                    </dl>
		                </div>
		                <div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>车间：</dt>
		                        <dd>${groupName}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>自编号：</dt>
		                        <dd>${busInrepairInfoMap.selfCode}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理类型：</dt>
		                        <dd>
		                        <c:if test="${busInrepairInfoMap.repairCategory==1}">抢修</c:if>
		                        <c:if test="${busInrepairInfoMap.repairCategory==2}">正常修理</c:if>
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理性质：</dt>
		                        <dd>
		                        <c:if test="${busInrepairInfoMap.repairProperties==1}">返厂修理</c:if>
		                        <c:if test="${busInrepairInfoMap.repairProperties==2}">正常修理</c:if>
		                        <c:if test="${busInrepairInfoMap.repairProperties==3}">返工修理</c:if>
		                        </dd>
		                    </dl>
		                </div>
					</div>
	                <div class="row-fluid">
		                <div class="span9">
	                		<dl class="dl-horizontal">
		                        <dt>存在问题：</dt>
		                        <dd>${busInrepairInfoMap.problem}</dd>
	                    	</dl>
		                </div>
	        		</div>
				</div>
				<!-- 基本信息 end -->
				<!-- 领用申请  start -->
				<div class="page-header">
					<h4>补充材料</h4>
					 <div class="row-fluid">
					 	<div style="margin:5px 0 5px 5px;"><a class="btn btn-medium" onclick="openMaterial();" href="#"><i class="icon-plus"></i>选择材料</a>
					 	<p class="text-right">总计:<input id="price" name="price" class="input-mini" type="text" value="0"  readonly="readonly" /></p>
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
									<c:if test="${empty busMaterialPlanList}">
			                            <tr>
			                            	<td colspan="10" style="text-align: center;color:red;">暂无数据！</td>
			                            </tr>   
			                        </c:if>
			                        
			                        <c:if test="${not empty busMaterialPlanList}">
			                            <c:forEach items="${busMaterialPlanList}" var="busMaterialPlan" varStatus="i">
			                                <tr id="materialPlanTr_${i.index+1}">
			                                    <td>${busMaterialPlan.materialDetail}<input type="hidden" name="materialDetail" value="${busMaterialPlan.materialDetail}"><input type="hidden" name="materialClass" value="${busMaterialPlan.materialClass}"></td>
			                                    <td>${busMaterialPlan.materialGroup}<input type="hidden" name="materialGroup" value="${busMaterialPlan.materialGroup}"></td>
			                                    <td>${busMaterialPlan.materialCode}<input type="hidden" name="materialCode" value="${busMaterialPlan.materialCode}"></td>
			                                    <td>${busMaterialPlan.unity}<input type="hidden" name="unity" value="${busMaterialPlan.unity}"></td>
			                                    <td>${busMaterialPlan.estimatePrice}<input type="hidden" name="estimatePrice" value="${busMaterialPlan.estimatePrice}"></td>
			                                    <td><input id="materialNum" name="materialNum" class="input-mini" type="text" value="${busMaterialPlan.materialNum}"/></td>
			                                    <td><input id="totalPrice" name="totalPrice" class="input-mini" type="text" value="${busMaterialPlan.totalPrice}"/></td>
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
				</form>
				<!-- 备注 end -->
				<!-- 操作按钮 start -->	    
    			<div class="form-actions">
			  		<a class="btn" href="javascript:void(0)" onclick="back();">取 消</a> 
				  	<a class="btn btn-primary" href="javascript:void(0);" onclick="savePlan();">提 交</a>
			  	</div>
				<!-- 操作按钮 end -->
				<div class="page-header">
				<h4>操作历史记录</h4>
		  		<div class="table">
				<div class="row form-horizontal">
			  	  <table class="table table-striped table-bordered"> 
	                 	<tr>  
								<th>操作动态<span style="float:right"><a href="#" onclick="openeq('${busDrillingMap.Id}','${busInrepairInfoMap.id}');">更多&gt;&gt;</a></span></th>
						</tr>
				  </table>
				</div>
				  <table class="table table-striped table-bordered">
			          	<tbody>
				           	<c:if test="${empty logRecordsList}">
				           		<tr>
				                    <td colspan="6" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                 </tr>   
				           	</c:if>
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
			</div>
		</div>
	</div>
	</body>
</html>
