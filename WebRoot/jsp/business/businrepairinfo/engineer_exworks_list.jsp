<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>监造出厂列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function doSearch(){
		document.myform.action="${pageContext.request.contextPath}/api/businrepairinfo/getEngineerList";
		document.myform.submit();
	}
	function update(id){
		document.myform.action="${pageContext.request.contextPath}/api/businrepairinfo/toEngineerManage?id="+id;
		document.myform.submit();
	}
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
      			<div>
			        <h3>监造管理列表</h3>
     			</div>
				<!-- 搜索框开始 start -->
				<form class="form-inline" name="myform" method="post">
				<div class="row-fluid">
    	 				<!-- <fieldset>
            				<legend><span class="label label-info">按条件搜索信息</span></legend>
				            <div class="span5">
				              <p class="control-group">
				               <label class="control-label" for="equipmentName">设备名称</label>
				               <input id="equipmentName" name="equipmentName" class="input-medium" type="text" value="${param.equipmentName }" />
				              </p>
				               <p class="control-group">
				                <label for="">出厂时间</label>
				                <input id="beginInDate" name="beginInDate" class="input-small" type="text"  value="${param.beginInDate }"
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
						           — <input id="endInDate" name="endInDate" class="input-small" type="text" value="${param.endInDate }"
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
				              </p>
				            </div>
				            <div class="span5">
				              <p class="control-group">
				               <label class="control-label" for="factoryState">设备状态</label>
				               <select name="factoryState" id="factoryState">
				               		<option value="0">未出厂</option>
				               		<option value="1">已出厂</option>
				               </select>
				              </p>
				             </div>
				            <div class="span10">
            				 <a href="javascript:void(0);" class="btn btn-info" onclick="doSearch();"><i class="icon-search icon-white" ></i>搜索</a> 
            				 </div>
          				</fieldset> -->
  				</div>
  				<!-- 搜索框 end -->
			   	<div class="table">
   					<h4>出厂列表</h4>
		      		<table class="table table-striped table-bordered">
			        	<thead>
				           	<tr>
				           	 <th>序号</th>
				           	 <th>设备名称</th>
				             <th>修理类别</th>
				             <th>修理性质</th>
				             <th>进厂时间</th>
				             <th>送修单位</th>
				             <th>厂区</th>
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
				            	<c:forEach items="${page.resultList}" var="busInrepairInfoMap" varStatus="i">
				                	<tr>
				                		<td>${i.index+1 }</td>
					                     <td>
					                     	<a href="${pageContext.request.contextPath}/api/businrepairinfo/toBusInrepairInfoPage?id=${busInrepairInfoMap.id}" target="_blank">
					                     	${busInrepairInfoMap.equipmentName}
					                     	</a>
					                     	
					                     </td>
					                     <td>
					                     	<c:if test="${busInrepairInfoMap.repairType==1}">大修</c:if>
					                   	 	<c:if test="${busInrepairInfoMap.repairType==2}">中修</c:if>
					                   	 	<c:if test="${busInrepairInfoMap.repairType==3}">检修</c:if>
					               	 	 </td>
					                     <td>
					                     	<c:if test="${busInrepairInfoMap.repairProperties==1}">返厂修理</c:if>
					                   	 	<c:if test="${busInrepairInfoMap.repairProperties==2}">正常修理</c:if>
					                   	 	<c:if test="${busInrepairInfoMap.repairProperties==3}">返工修理</c:if>
					                   	 </td>
					                     <td><tag:date  value="${busInrepairInfoMap.inDate}" /></td>
					                     <td>${web:getDeptBycode(busInrepairInfoMap.deptFrom)}</td>
					                     <td>
					                     <c:if test="${busInrepairInfoMap.orgId=='|CQ|.|JX|.|YC|'}">银川厂</c:if>
					                     <c:if test="${busInrepairInfoMap.orgId=='|CQ|.|JX|.|QY|'}">庆阳厂</c:if>
					                     </td>
					                     <td>
					                     	<a class="btn btn-primary" href="#" onclick="update('${busInrepairInfoMap.id }');"><i class="icon-edit icon-white"></i>确认</a>
				                 		 </td>
				                 		
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
