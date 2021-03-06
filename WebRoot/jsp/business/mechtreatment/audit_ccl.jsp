<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>厂材料员审批</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/RegExp.js"></script>
		<script type="text/javascript">
		function audit(type){
	        $("#cclResult").val(type);      
			document.myform.action="${pageContext.request.contextPath}/api/mechtreatment/tocclAudit";
			document.myform.submit();
		}
		// 函数，3个参数，表单名字，表单域元素名，限制字符；
		   function textCounter(field, countfield, maxlimit) {
				var s=field.value.length;
				if(s>maxlimit){
					field.value=field.value.substr(0,maxlimit)
				}else{
					countfield.innerHTML="已输入" + s + "个字符";
				} 
		
		   }
		   function goBack() {
			  window.location.href = "${pageContext.request.contextPath}/api/mywork/getMyWork";
		}	
		
		function changePrice(index){
			var totalPrice =0;
			var ratifyNum = document.getElementsByName("ratifyNum");
			var realratifyNum = document.getElementsByName("realratifyNum");
			var estimatePrices = document.getElementsByName("estimatePrice");
			if(ratifyNum[index].value!=null&&ratifyNum[index].value!=""&&estimatePrices[index].value!=""){
				if(parseFloat(ratifyNum[index].value) > parseFloat(realratifyNum[index].value)){
					document.getElementsByName("ratifyNum")[index].value = realratifyNum[index].value;
					return;
				}
	   			totalPrice =multiplication(parseFloat(ratifyNum[index].value),parseFloat(estimatePrices[index].value));
	   			document.getElementsByName("totalPrice")[index].value=totalPrice;
	   		}else{
	   			totalPrice = 0;
	   			document.getElementsByName("ratifyNum")[index].value=0;
	   			document.getElementsByName("totalPrice")[index].value=totalPrice;
	   		}
			var ptab = document.getElementById("materialPlanTable");
	   		price = 0 ;
			for (var i = 0; i<ptab.rows.length; i++) {
				var totalPrice =0;
		   		if(ratifyNum[i].value!=null&&ratifyNum[i].value!=""){
		   			totalPrice =multiplication(parseFloat(ratifyNum[i].value),parseFloat(estimatePrices[i].value));
		   			price = addition(price,totalPrice);
		   		}
			}
			document.getElementById("price").value = price;
		}
		function loadpage(){
			var ratifyNum = document.getElementsByName("ratifyNum");
			var estimatePrices = document.getElementsByName("estimatePrice");
			var ptab = document.getElementById("materialPlanTable");
	   		price = 0 ;
			for (var i = 0; i<ptab.rows.length; i++) {
				var totalPrice =0;
		   		if(ratifyNum[i].value!=null&&ratifyNum[i].value!=""){
		   			totalPrice =multiplication(parseFloat(ratifyNum[i].value),parseFloat(estimatePrices[i].value));
		   			price = addition(price,totalPrice);
		   		}
			}
			document.getElementById("price").value = price;
		}
		//加法
		function addition(num1,num2){
		      return  Number((num1+num2).toFixed(2));
		}
		//乘法
		function multiplication(num1,num2)
		{
		     var m=0,s1=num1.toString(),s2=num2.toString();
		     try{m+=s1.split(".")[1].length}catch(e){};
		     try{m+=s2.split(".")[1].length}catch(e){};
		     return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
		}
	</script>
	</head>
	<body onload ="loadpage();">
	<div class="container-fluid">
	   <div class="row-fluid">
		    <div class="span9">
		     	<div class="page-header">
		        	<h3>厂材料员审批</h3>
		        </div>
	            
	            <div class="page-header">
		            <div class="row-fluid">
		            	<div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>厂区：</dt>
		                        <dd>${companyName}
		                        </dd>
		                    </dl>
		                   	<dl class="dl-horizontal">
								<dt>计划完工时间：</dt>
								<dd>
									<tag:date  value="${mechTreatmentMap.startDate}" />
								</dd>
							</dl>
		                </div>
		                <div class="span5">
		                	<dl class="dl-horizontal">
		                        <dt>车间：</dt>
		                        <dd>${groupName}
		                        </dd>
		                    </dl>
		                </div>
		             </div> 
		             <div class="row-fluid">
		                	<div class="span10">
		                	<dl class="dl-horizontal">
		                        <dt>施工要求：</dt>
		                        <dd>
		                        	${machiningMap.dispatchingRemark}
		                        </dd>
		                    </dl>
		                  </div>
		                </div>
		        </div>
		        <div class="page-header">
		        	<h4>加工件信息</h4>
		        	<div class="row-fluid">
		            	<table class="table table-striped table-bordered">
		                	<thead>
		                    	<tr>
		                        	<th>加工/修理件名称</th>
		                            <th>数量</th>
		                            <th>送修车间</th>
		                        </tr>
		                    </thead>
		                	<tbody>
		                	   <c:forEach var="machining" items='${machiningList}' varStatus="status">
		                    	<tr>
		                        	<td>${machining.equipmentName}</td>
		                            <td>${machining.planNum}</td>
		                            <td>${machining.deptfromname}</td>
		                        </tr>
		                        </c:forEach>
		                    </tbody>
		                </table>
		            </div>
		        	
		        </div>
		        <form name="myform" action="">
		        <div class="page-header">
		        	<h4>材料申请</h4>
		        	<p class="text-right">总计:<input id="price" name="price" class="input-mini" type="text" value="0"  readonly="readonly" /></p>
		        	<c:if test="${fn:length(busMaterialPlanList)<11}">
		        	<div class="row-fluid">
		        	</c:if>
		        	<c:if test="${fn:length(busMaterialPlanList)>10}">
		        	<div class="row-fluid" style="OVERFLOW-Y: auto; OVERFLOW-X:hidden; height:400px">
		        	</c:if>
		            	<div style="overflow-y:scroll;">
		            	<table class="table table-striped table-bordered" id="">
		                	<thead>
		                    	<tr>
									<th width="8%">物料组</th>
									<th width="12%">物料编码</th>
									<th width="25%">物料描述</th>
									<th width="10%">参考价</th>
									<th width="7%">数量</th>
									<th width="13%">金额</th>
									<th width="13%">审批数量</th>
									<th width="6%">单位</th>
									<th width="6%">类别</th>
		                        </tr>
		                    </thead>
		                 </table>   
		                 </div>  
		                    <div style="display:block;overflow:auto;height:500px;">
		                    <table class="table table-striped table-bordered" id="materialPlanTable">
		                    <tbody>
		                        <c:if test="${empty busMaterialPlanList}">
		                            <tr>
		                            	<td colspan="8" style="text-align: center;"><font color='red'>暂无数据！</font></td>
		                            </tr>   
		                        </c:if>
		                        <c:if test="${not empty busMaterialPlanList}">
		                            <c:forEach items="${busMaterialPlanList}" var="busMaterialPlan" varStatus="i">
		                                <tr>
		                                    <td width="8%">${busMaterialPlan.materialGroup}</td>
		                                    <td width="12%">${busMaterialPlan.materialCode}</td>
		                                    <td width="25%">${busMaterialPlan.materialDetail}</td>
		                                    <td width="10%">${busMaterialPlan.estimatePrice}</td>
		                                    <td width="7%">${busMaterialPlan.materialNum}</td>
		                                    <td width="13%"><input name="totalPrice" id="totalPrice" class="input-mini" type="text" value="${busMaterialPlan.totalPrice}" readonly/></td>
		                                    <input type="hidden" name="materialId" value="${busMaterialPlan.id}"/>
		                                     <input type="hidden" name="estimatePrice" value="${busMaterialPlan.estimatePrice}"/>
		                                    <input type="hidden" name="realratifyNum"  value="${busMaterialPlan.ratifyNum}"/>
		                                    <td width="13%"><input name="ratifyNum" class="input-mini" type="text" value="${busMaterialPlan.ratifyNum}" onChange="changePrice('${i.index }');" maxlength="5" onkeyup="value=value.replace(/[^\d\.]/g,'')"/></td>
		                                    <td width="6%">${busMaterialPlan.unity}</td>
		                                    <td width="6%"><c:if test="${busMaterialPlan.type==1}">领用</c:if>
		                       					 <c:if test="${busMaterialPlan.type==2}">加工</c:if>
		                       					 <c:if test="${busMaterialPlan.type==3}">修理</c:if>
		                       					 <c:if test="${busMaterialPlan.type==4}">计划加领用</c:if>
		                       			    </td>
		                                </tr>
		                            </c:forEach>
		                        </c:if>
		                    </tbody>
		                </table>
		                </div>
		            </div>
		        </div>
		        
		        <div class="page-header">
				<input type="hidden" name="id" value="${mechTreatmentMap.id}"/>
				<input type="hidden" name="cclResult" id="cclResult" value=""/>
				<input type="hidden" name="stepId" id="stepId" value="${stepId}"/>
				<input type="hidden" name="instanceId" id="instanceId" value="${instanceId}"/>
				<input type="hidden" name="nodeId" id="nodeId" value="${nodeId}"/>
				<input type="hidden" name="type" id="type" value="ccl"/>
				<input id="url" name="url" type="hidden" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
				<h4>大件材料领用原因</h4>
				<p>
				 	${busPlanInfoMap.bigRemark }
				</p>
		        <h4>审批意见</h4>
			        		<div>    
				        			<ul class="inline">
								    <li><p class="muted">请输入1-500字的存在问题</p></li>
								    <li><p id="oclable" class="text-success">已输入<span id="oclable">0</span>字符</p></li>
								    </ul>	   
							</div>
			            <p><textarea onkeyup="textCounter(cclRemark,oclable,500);" id="cclRemark" name="cclRemark" class="span12" rows="2"></textarea></p>
			  </div></form>
			  <div class="form-actions">
			  	<a onclick="audit('0');" href="javascript:void(0);" class="btn">不同意</a>
			  	<a onclick="goBack();" href="#" class="btn">取消</a> 
				<a onclick="audit('1');" href="javascript:void(0);" class="btn btn-primary">同意</a>
			  </div>
			  
			  <div class="page-header">
				<h4>历史审批记录</h4>
		  		<div class="table">
				  <table class="table table-striped table-bordered">
			        	<thead>
			            	<tr>
			            	 <th>审批记录:</th>
			            	</tr>
			           	</thead> 
			          	<tbody>
				           	<c:if test="${empty logRecordsList}">
				           		<tr>
				                    <td colspan="6" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                 </tr>   
				           	</c:if>
				           	<c:if test="${not empty logRecordsList}">
				            	<c:forEach items="${logRecordsList}" var="logRecords" varStatus="i">
				                 <tr>
				                     <td>&nbsp; ${logRecords.remark}</td>
				                 </tr>
				                 </c:forEach>
				             </c:if>
					 	</tbody>
			        </table>
				</div>
				</div>
				
				
			</div>
		</div>
	</body>
</html>
