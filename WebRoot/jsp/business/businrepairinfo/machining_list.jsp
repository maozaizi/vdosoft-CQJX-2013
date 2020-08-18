<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>机加调度列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function doSearch(){
		document.myform.action="${pageContext.request.contextPath}/api/businrepairinfo/getMachiningWorkAssignList";
		document.myform.submit();
	}
	function toCheckMachining() {
		var equipment = document.getElementsByName("id");
		var checked = false;
		for (var i = 0; i < equipment.length; i++) {
			if (equipment[i].checked) {
				checked = true;
				document.getElementsByName("id").value=equipment[i].value;
			}
		}
		
		if (!checked) {
			alert("请选择设备！");
			return false;
		}
		var select=document.getElementsByName("id").value;
		var repairStep=document.getElementById("repairStep_"+select).value;
		if(repairStep=="3"){
		document.myform.action="${pageContext.request.contextPath}/api/businrepairinfo/toMachiningAssign";
		}else{
		document.myform.action="${pageContext.request.contextPath}/api/businrepairinfo/toUpdateExternalMachining";
		}
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
			        <h3>机加调派列表</h3>
     			</div>
				<!-- 搜索框开始 start -->
				<form class="form-inline" name="myform">
				<div class="row-fluid">
    	 				<fieldset>
            				<legend><span class="label label-info">按条件搜索机加信息</span></legend>
				            <div class="span5">
				              <p class="control-group">
				               <label class="control-label" for="equipmentName">名称</label>
				               <input id="equipmentName" name="equipmentName" class="input-medium" type="text" value="${param.equipmentName }" />
				              </p>
				              <p class="control-group">
				                <label class="control-label" for="equipmentModel">规格</label>
				              	<input id="equipmentModel" name="equipmentModel" class="input-medium" type="text" value="${param.equipmentModel }" />
				              </p>
				            </div>
				            <div class="span5">
				              <p class="control-group">
				                <label class="control-label" for="equipmentModel">送修单位</label>
				              	<input id="userName" name="userName" class="input-medium" type="text" value="${param.userName }" />
				              </p>
				              <p class="control-group">
				                <label class="control-label" for="equipmentModel">修制类别</label>
				              	<input id="makeSort" name="makeSort" class="input-medium" type="text" value="${param.userName }" />
				              </p>
				            </div>
				            <div class="span10">
            				 <a href="javascript:void(0);" class="btn btn-info" onclick="doSearch();"><i class="icon-search icon-white" ></i>搜索</a> 
            				 <a class="btn btn-primary" href="#" onclick="toCheckMachining();"><i class="icon-edit icon-white"></i>调 派</a>
            				 </div>
          				</fieldset>
  				</div>
  				<!-- 搜索框 end -->
			   	<div class="table">
   					<h4>机加信息列表</h4>
		      		<table class="table table-striped table-bordered">
			        	<thead>
				           	<tr>
				           	 <th width="6%">选择</th>
				           	 <th>名称</th>
				             <th>规格</th>
				             <th>修制类别</th>
				             <th>送修单位</th>
				             <th>施工要求</th>
				           	</tr>
			         	</thead>  	
			         	<tbody>
			          		<c:if test="${empty page.resultList}">
				          		<tr>
				                   <td colspan="7" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                </tr>   
			          		</c:if>
				          	<c:if test="${not empty page.resultList}">
				            	<c:forEach items="${page.resultList}" var="machiningMap" varStatus="i">
				                	<tr onclick="selectTr('${machiningMap.id }');">
				                	     <input id="repairStep_${machiningMap.id }" name="repairStep" value="${machiningMap.repairStep }" type="hidden"/>
				                	     <td><input type="radio" id="id_${machiningMap.id }" name="id" value="${machiningMap.id }"/></td>
					                     <td><a href="${pageContext.request.contextPath}/api/businrepairinfo/toMachiningPage?id=${machiningMap.Id}" target="_blank">${machiningMap.equipmentName}</a></td>
					                     <td>${machiningMap.equipmentModel}</td>
					                     <td>${machiningMap.makeSort}</td>
					                     <td>${machiningMap.userName}</td>
					                     <td>${machiningMap.problem}</td>
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
