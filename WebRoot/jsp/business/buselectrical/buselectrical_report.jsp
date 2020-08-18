<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>设备信息管理列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function save(){
		if(checkForm()){
			document.myform.action="${pageContext.request.contextPath}/api/buselectrical/modifyBusElectricalReport";
			document.myform.submit();
		}
	}
	function checkForm(){
		var realNums = document.getElementsByName("realNum");
		var materialNums = document.getElementsByName("materialNum");
		var flag = true;
		var re =/^\d{1,12}(?:\.\d{1,2})?$/;
		for(var i=0;i<realNums.length;i++){
			if(realNums[i].value==""||!re.test(realNums[i].value)||parseFloat(materialNums[i].value)<parseFloat(realNums[i].value)){
				realNums[i].style["borderColor"]="red";
				flag=false;
			}else{
				realNums[i].style["borderColor"]="";
			}
		}
		return flag;
	}
</script>
</head>
<body>
<!-- title start -->
	<div class="container-fluid">
	  	<div class="row-fluid">
		    <div class="span9">
		        <div class="page-header">
		        	<h3>上报器材库</h3>
		        </div>
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
		                        <dd><a href="${pageContext.request.contextPath}/api/buselectrical/toBusElectricalDetailsPage?id=${busElectricalMap.Id}" target="_blank">${busElectricalMap.workCard}</a>
		                        </dd>
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
		        <div class="page-header">
		        	<h4>物料计划</h4>
		        	<div class="table">
			      		<table class="table table-striped table-bordered">
				      		 <tbody>
					           <tr>
									<th>物料组</th>
									<th>物料编码</th>
									<th>物料描述</th>
									<th>参考价</th>
									<th>计划数量</th>
									<th>实际数量</th>
									<th>单位</th>
					           </tr>
		          	 <form action="" name="myform" id="myform">
						           <c:if test="${empty busMaterialPlanList}">
						            <tr>
						            	<th colspan="6" style="text-align: center;"><font color='red'>暂无数据！</font></th>
						            </tr>	
						           </c:if>
						           <c:if test="${not empty busMaterialPlanList}">
						           	<c:forEach items="${busMaterialPlanList}" var="material" varStatus="i">
						                <tr>
						                	<input type="hidden" name="id" id="id" value="${material.id}" class="input-mini"/>
						                    <td>${material.materialGroup}</td>
						                    <td>${material.materialCode}</td>
						                    <td>${material.materialDetail}</td>
						                    <td>${material.estimatePrice}</td>
						                    <td><input type="hidden" name="materialNum" value="${material.materialNum}"/>${material.materialNum}</td>
						                    <td><input type="number" name="realNum" value="" class="input-mini"/></td>
						                    <td>${material.unity}</td>
						                </tr>
						               </c:forEach>
					              </c:if>
					         </tbody>
					       </table>
				       </div>
			        </div>
				    <div class="page-header" style="border-bottom: 0px;">
							<input type="hidden" name="stepId" id="stepId" value="${stepId }"/>
							<input type="hidden" name="nodeId" id="nodeId" value="${nodeId }"/>
							<input type="hidden" name="instanceId" id="instanceId" value="${instanceId }"/>
							<input type="hidden" name="orgCode" id="orgCode" value="${orgCode }"/>
							<input type="hidden" name="objId" id="objId" value="${busId }"/>
							<input id="url" name="url" type="hidden" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
			        </div>
					</form>
				    <div class="form-actions">
						<p>
							<a href="javascript:void(0);" onclick="goBack();" class="btn">取  消</a>
							<a href="javaScript:void(0);" onclick="save();" class="btn btn-primary">确 认</a>
						</p>
					</div>
			    </div>
		    </div>
		</div>
	</div>        
</body>
</html>
