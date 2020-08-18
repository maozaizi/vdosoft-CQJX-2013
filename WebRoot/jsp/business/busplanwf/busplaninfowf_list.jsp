<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>进修单列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function doSearch(){
		document.myform.action="${pageContext.request.contextPath}/api/busplanwf/getBusPlanInfoWfList";
		document.myform.submit();
	}
	function update() {
		var equipment = document.getElementsByName("id");
		var checked = false;
		var objId ="";
		var state="";
		for (var i = 0; i < equipment.length; i++) {
			if (equipment[i].checked) {
				checked = true;
				document.getElementById("objId").value=equipment[i].value;
				state=document.getElementById("auditState_"+equipment[i].value).value;
			}
		}
		
		if (!checked) {
			alert("请选择申请信息！");
			return false;
		}
		if (state==1) {
			alert("申请信息正在审核中，不能修改！");
			return false;
		}
		
		document.myform.action="${pageContext.request.contextPath}/api/busplanwf/toMaterialPlan";
		document.myform.submit();
	}
	
	function selectTr(select){
		document.getElementById("id_"+select).checked=true;
	}
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
      			<div>
			        <h3>物料申请列表</h3>
     			</div>
				<!-- 搜索框开始 start -->
				<form class="form-inline" name="myform">
				<input name="objId" id="objId" value="" type="hidden"/>
				<div class="row-fluid">
    	 				<fieldset>
            				<legend><span class="label label-info">按条件搜索物料申请</span></legend>
				            <div class="span6">
				              <p class="control-group">
				               <label class="control-label" for="planCode">申请编号</label>
				               <input id="planCode" name="planCode" class="span9" type="text" value="${param.planCode}" />
				              </p>
				            </div>
            				<div class="span10">
            				 <a href="javascript:void(0);" class="btn btn-info" onclick="doSearch();"><i class="icon-search icon-white"></i>搜 索</a> 
            				 <!-- <a class="btn btn-primary" href="javascript:void(0);" onclick="update();"><i class="icon-edit icon-white"></i>修 改</a> -->
            				 </div>
          				</fieldset>
  				</div>
  				<!-- 搜索框 end -->
			   	<div class="table">
   					<h4>物料申请列表</h4>
		      		<table class="table table-striped table-bordered">
			        	<thead>
				           	<tr>
				           	 <th>选择</th>
				           	 <th>申请编号</th>
				             <th>申请状态</th>
				             <th>备注</th>
				           	</tr>
			         	</thead>  	
			         	<tbody>
			          		<c:if test="${empty page.resultList}">
				          		<tr>
				                   <td colspan="7" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                </tr>   
			          		</c:if>
				          	<c:if test="${not empty page.resultList}">
				            	<c:forEach items="${page.resultList}" var="planMap" varStatus="i">
				                	<tr onclick="selectTr('${planMap.id }');">
				                		 <td><input type="radio" id="id_${planMap.id }" name="id" value="${planMap.id }"/></td>
					                     <td><a href="${pageContext.request.contextPath}/api/busplanwf/toBusPlanInfoWfDetailPage?objId=${planMap.id}" target="_blank">${planMap.planCode}</a></td>
					                     <td><input type="hidden" id="auditState_${planMap.id }" value="${fn:trim(planMap.auditState)}"/><c:if test="${fn:trim(planMap.auditState)==2}">未审</c:if><c:if test="${fn:trim(planMap.auditState)==0}">未通过</c:if><c:if test="${fn:trim(planMap.auditState)==1}">通过</c:if></td>
					                     <td>${planMap.planRemark}</td>
				                 	</tr>
				                </c:forEach>
			               </c:if>
			         	</tbody>
		      		</table>
					<%@include file="/jsp/public/standard.jsp" %>
			    </div>
			    </form>  
			</div>
		</div>
	</div>	         	
</body>
</html>
