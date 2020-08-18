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
	function save(){
		if(checkForm()){
			document.myform.action="${pageContext.request.contextPath}/api/businrepairinfo/updateManagerMoneyShare";
			document.myform.submit();
		}else{
			document.getElementById("errorMessage").style="display:block;";
		}
	}
	function back(){
		document.myform.action="${pageContext.request.contextPath}/api/businrepairinfo/toManagerMoneyShare";
		document.myform.submit();
	}
	function checkForm(){
		var departmentMoneys = document.getElementsByName("departmentMoney");
		var companyMoneys = document.getElementsByName("companyMoney");
		var groupMoneys = document.getElementsByName("groupMoney");
		var flag = true;
		var re =/^\d{1,12}(?:\.\d{1,2})?$/;
		for(var i=0;i<departmentMoneys.length;i++){
			if(!re.test(departmentMoneys[i].value)&&departmentMoneys[i].value!=''){
				departmentMoneys[i].style["borderColor"]="red";
				flag=false;
			}else{
				departmentMoneys[i].style["borderColor"]="";
			}
		}
		for(var i=0;i<companyMoneys.length;i++){
			if(!re.test(companyMoneys[i].value)&&companyMoneys[i].value!=''){
				companyMoneys[i].style["borderColor"]="red";
				flag=false;
			}else{
				companyMoneys[i].style["borderColor"]="";
			}
		}
		for(var i=0;i<groupMoneys.length;i++){
			if(!re.test(groupMoneys[i].value)&&groupMoneys[i].value!=''){
				groupMoneys[i].style["borderColor"]="red";
				flag=false;
			}else{
				groupMoneys[i].style["borderColor"]="";
			}
		}
		return flag;
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<div>
  			<h3>管理费用分摊</h3>
		</div>
  		<div class="row-fluid">
  			<div class="span9">
		       <div class="table">
		      		<table class="table table-striped table-bordered"> 
	                	<thead>
		                	<tr> 
		                        <th>设备名称</th>
		                        <th>工卡号</th>
		                        <th>修理类别</th>
		                        <th>单位</th>
		                        <th>定额工时</th>
		                        <th>定额价格</th>
		                        <th>结算金额（元）</th>
		                        <th>机关管理费用（元）</th>
		                        <th>厂管理费用（元）</th>
		                        <th>车间管理费用（元）</th>
		                     </tr>
	                	</thead>
	                    <tbody>
	                    	<form name="myform" action="" >
	                    		<input id="url" name="url" type="hidden" value="${pageContext.request.contextPath}/api/businrepairinfo/toManagerMoneyShare" />
		                    	<c:if test="${empty busRepairSettlementList}">
		                    		<tr>
			                            <td colspan="8" style="text-align: center;color:red;">暂无数据！</td>
			                         </tr>   
		                    	</c:if>
		                    	<c:if test="${not empty busRepairSettlementList}">
			                    	<c:forEach items="${busRepairSettlementList}" var="busworkCard" varStatus="i">
				                        <tr>
				                            <td>${busworkCard.equipment_name}<input id="id" name="id" class="input-mini" type="hidden" value="${busworkCard.id}" /></td>
				                            <td>${busworkCard.work_card}</td>
				                            <td><c:if test="${busworkCard.repair_type==1}">大修</c:if><c:if test="${busworkCard.repair_type==1}">中修</c:if><c:if test="${busworkCard.repair_type==1}">检修</c:if></td>
				                            <td>${busworkCard.unity}</td>
				                            <td>${busworkCard.quota_hour}</td>
				                            <td>${busworkCard.repair_value}</td>
				                            <td>${busworkCard.settlement_money}</td>
				                            <td><input name="departmentMoney" class="input-mini" type="text" value="${busworkCard.department_money }" <c:if test="${ not empty busworkCard.department_money}">readonly</c:if>/></td>
				                            <td><input name="companyMoney" class="input-mini" type="text" value="${busworkCard.company_money }" <c:if test="${ not empty busworkCard.company_money}">readonly</c:if>/></td>
				                            <td><input name="groupMoney" class="input-mini" type="text" value="${busworkCard.group_money }" <c:if test="${ not empty busworkCard.group_money}">readonly</c:if>/></td>
				                        </tr>
			                        </c:forEach>
		                        </c:if>
	                        </form>
	                    </tbody>
	                </table>
			   </div>
     		</div>
     		</div>
     	</div>
     	<div class="row" style="margin-bottom:50px;">
		<div class="span10 offset5">
			<p>
				<button class="btn btn-primary" onclick="save();">确  定</button>
				<button class="btn" onclick="back();">返  回</button>
			</p>
		</div>
	</div>
<!-- 列表结束 end -->
</body>
</html>
