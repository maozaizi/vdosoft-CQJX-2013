<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>机修列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function doSearch(){
		document.myform.action="${pageContext.request.contextPath}/api/busmechanic/getBusMechanicList";
		document.myform.submit();
	}
	function openSheet(state){
		document.getElementById("state").value = state;
		document.myform.action="${pageContext.request.contextPath}/api/busmechanic/getBusMechanicList";
		document.myform.target="_self";
		document.myform.submit();
	}
	function toaddNote(busId){
		window.open("${pageContext.request.contextPath}/api/busmechanic/toaddNote?id="+busId,"","width=700,height=500,scrollbars=no");
	}
	
	$(function () {
		$('#myTab a').click(function (e) {
    		e.preventDefault();
    		$(this).tab('show');
    		var state = $(this).attr('state');
    		var url = '${pageContext.request.contextPath}/api/busmechanic/getBusMechanicList?state='+state;
    	})
	})

</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
      			<div>
			        <h3>机修列表</h3>
     			</div>
				<!-- 搜索框开始 start -->
				<div class="row-fluid">
	    			<form class="form-inline" name="myform">
	    				<input type="hidden" id="state" name="state" value="${param.state}"/>
	    				<div class="tabMenu">
	    					<ul class="nav nav-tabs" id="myTab">
								<li <c:if test="${empty param.state||param.state=='0'}">class="active" </c:if>><a href="#home" onclick="openSheet('0');" >全部</a></li>
								<li <c:if test="${param.state=='1'}">class="active" </c:if>><a href="#profile" 	onclick="openSheet('1');">在修</a></li>
								<li	<c:if test="${param.state=='2'}">class="active" </c:if>><a href="#messages" onclick="openSheet('2');">待修</a></li>
								<li	<c:if test="${param.state=='3'}">class="active" </c:if>><a href="#settings" onclick="openSheet('3');">完工</a></li>
							</ul>
	    				</div>
    	 				<fieldset>
            				<legend><span class="label label-info">按条件搜索进修设备</span></legend>
				            <div class="span6">
				              <p class="control-group">
				               <label class="control-label" for="equipmentName">设备名称</label>
				               <input id="equipmentName" name="equipmentName" class="span9" type="text" value="${param.equipmentName }" />
				              </p>
				              <p class="control-group">
				                <label for="">进厂时间</label>
				                <input id="beginInDate" name="beginInDate" class="input-small" type="text"  value="${param.beginInDate }"
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
						           — <input id="endInDate" name="endInDate" class="input-small" type="text" value="${param.endInDate }"
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
				              </p>
				            </div>
				            <div class="span4">
				              <p>
				                <label for="repairType">修理类别</label>
				                <select class="span7" name="repairType" id="repairType">
					              <option value="">请选择</option>
					              <option value="1" <c:if test="${param.repairType==1 }">selected</c:if>>大修</option>
					              <option value="2" <c:if test="${param.repairType==2 }">selected</c:if>>中修</option>
								  <option value="3" <c:if test="${param.repairType==3 }">selected</c:if>>检修</option>
					            </select>
				              </p>
				              <p>
				                <label for="repairProperties">修理性质</label>
				                <select class="span7" name="repairProperties" id="repairProperties">
				              	 <option value="">请选择</option>
					             <option value="1" <c:if test="${param.repairProperties==1 }">selected</c:if> >返厂修理</option>
					             <option value="2" <c:if test="${param.repairProperties==2 }">selected</c:if> >正常修理</option>
							 	 <option value="3" <c:if test="${param.repairProperties==3 }">selected</c:if>>返工修理</option>
				           		</select>
				              </p>
				            </div>
            				<div class="span10"> <a href="javascript:void(0);" class="btn btn-info" onclick="doSearch();"><i class="icon-search icon-white" ></i>搜索</a> </div>
          				</fieldset>
     				</form>     
  				</div>
  				<!-- 搜索框 end -->
			   	<div class="table">
   					<h4>进修设备列表</h4>
		      		<table class="table table-striped table-bordered">
			        	<thead>
				           	<tr>
				           	 <th>工卡号</th>
				           	 <th>车间</th>
				           	 <th>设备名称</th>
				             <th>修理类别</th>
				             <th>修理性质</th>
				             <th>进厂时间</th>
				             <th>存在问题</th>
				             <th>操作</th>
				           	</tr>
			         	</thead>  	
			         	<tbody>
			          		<c:if test="${empty page.resultList}">
				          		<tr>
				                   <td colspan="7" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                </tr>   
			          		</c:if>
				          	<c:if test="${not empty page.resultList}">
				            	<c:forEach items="${page.resultList}" var="busMechanic" varStatus="i">
				                	<tr>
				                		 <td>${busMechanic.workcard}</td>
				                		 <td>${busMechanic.deptName }</td>
					                     <td><a href="${pageContext.request.contextPath}/api/busmechanic/toBusMechanicDetailsPage?id=${busMechanic.busId}" target="_blank">${busMechanic.equipmentName}</a></td>
					                     <td>
					                     	<c:if test="${busMechanic.repairType==1}">大修</c:if>
					                   	 	<c:if test="${busMechanic.repairType==2}">中修</c:if>
					                   	 	<c:if test="${busMechanic.repairType==3}">检修</c:if>
					               	 	 </td>
					                     <td>
					                     	<c:if test="${busMechanic.repairProperties==1}">返厂修理</c:if>
					                   	 	<c:if test="${busMechanic.repairProperties==2}">正常修理</c:if>
					                   	 	<c:if test="${busMechanic.repairProperties==3}">返工修理</c:if>
					                   	 </td>
					                     <td><tag:date  value="${busMechanic.inDate}" /></td>
					                     <td>${busMechanic.problem}</td>
					                     <td>
						                     <c:if test="${param.state==1}">
						                     	<a class="btn btn-primary" href="#" onclick="toaddNote('${busMechanic.busId}');"><i class="icon-edit icon-white"></i>生产日报</a>
						                     </c:if>
						                 </td>
				                 	</tr>
				                </c:forEach>
			               </c:if>
			         	</tbody>
		      		</table>
					<%@include file="/jsp/public/standard.jsp" %>
			    </div>
			</div>
		</div>
	</div>	         	
</body>
</html>
