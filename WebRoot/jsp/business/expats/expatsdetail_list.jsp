<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>上井记录列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function doSearch(){
		document.myform.action="${pageContext.request.contextPath}/api/expats/getExpatsInfoList";
		document.myform.submit();
	}
	function update(id){
		document.getElementById("id").value = id;
		document.myform.action="${pageContext.request.contextPath}/api/expats/toUpdateExpatsDetail";
		document.myform.submit();
	}
	function updateComplete(id){
		document.getElementById("id").value = id;
		document.myform.action="${pageContext.request.contextPath}/api/expats/toCompleteExpats";
		document.myform.submit();
	}
	
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
      			<div class="page-header">
			        <h3>车间上井服务</h3>
     			</div>
				<!-- 搜索框开始 start -->
				<div class="row-fluid">
	    			<form class="form-inline" name="myform">
	    			<input type="hidden" name="id" id="id"/>
    	 				<fieldset>
				            <div class="span4">
				              <p class="control-group">
				                <label class="control-label" for="taskCode">任务编号</label>
				              	<input id="taskCode" name="taskCode" class="input-medium" type="text" value="${param.taskCode }" />
				              </p>
				            </div>
				            <div class="span8">
				            	<p class="control-group">
				                <label for="">创建时间</label>
				                <input id="beginInDate" name="beginInDate" class="input-small" type="text"  value="${param.beginInDate }"
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
						           — <input id="endInDate" name="endDate" class="input-small" type="text" value="${param.endInDate }"
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
				              </p>
				            </div>
            				<div class="span10"> 
            					<a href="javascript:void(0);" class="btn btn-info" onclick="doSearch();"><i class="icon-search icon-white" ></i>搜索</a> 
            				</div>	
          				</fieldset>
     				</form>     
  				</div>
  				<!-- 搜索框 end -->
			   	<div class="table">
   					<h4>上井列表</h4>
		      		<table class="table table-striped table-bordered">
			        	<thead>
				           	<tr>
				           	 	<th>任务编号</th>
				             	<th>创建时间</th>
				             	<th>井	队</th>
				             	<th>任务状态</th>
				             	<th>任务描述</th>
				             	<th>操作</th>
				           	</tr>
			         	</thead>  	
			         	<tbody>
			          		<c:if test="${empty page.resultList}">
				          		<tr>
				                   <td colspan="6" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                </tr>   
			          		</c:if>
				          	<c:if test="${not empty page.resultList}">
				            	<c:forEach items="${page.resultList}" var="expats" varStatus="i">
				                	<tr>
					                     <td><a href="${pageContext.request.contextPath}/api/expats/toDetailExpats?id=${expats.bid}" target="_blank">${expats.task_code}</a></td> 
					                     <td><tag:date  value="${expats.create_date}" /></td>
					                     <td>${expats.expats_to}</td> 
					                     <td>
					                     	<c:if test="${expats.is_complete==1}">完成</c:if>
					                     	<c:if test="${expats.is_complete==0}">待处理</c:if>
					                     </td>
					                     <td>
					                     	${expats.task_detail}
					                     </td>
					                      <td>
					                     	<c:if test="${expats.is_complete==0}">
					                     		<a class="btn btn-primary" href="#" onclick="update('${expats.id }');"><i class="icon-edit icon-white"></i>编 辑</a>
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
