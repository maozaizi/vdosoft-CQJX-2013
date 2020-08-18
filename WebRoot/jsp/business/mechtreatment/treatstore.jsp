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
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function doSearch(){
		document.myform.action="${pageContext.request.contextPath}/api/mechtreatment/gettreatstorelist";
		document.myform.submit();
	}
	function tooutStore(id){
		document.getElementById("id").value = id;
		document.myform.action="${pageContext.request.contextPath}/api/mechtreatment/toOutTreatStore";
		document.myform.submit();
	}
	function toViewOutstore(){
		document.myform.action="${pageContext.request.contextPath}/api/mechtreatment/toViewtreatOutstore";
		document.myform.submit();
	}
	function toViewInstore(){
		document.myform.action="${pageContext.request.contextPath}/api/mechtreatment/toViewtreatinstore";
		document.myform.submit();
	}
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
      			<div>
			        <h3>加工件库存列表</h3>
     			</div>
				<!-- 搜索框开始 start -->
				<div class="row-fluid">
	    			<form class="form-inline" name="myform">
	    				<input type="hidden" id="id" name="id" />
    	 				<fieldset>
            				<legend><span class="label label-info">按条件搜索加工件</span></legend>
				            <div class="span4">
				              <p class="control-group">
				               <label class="control-label" for="name">加工件名称</label>
				               <input id="name" name="name" class="span9" type="text" value="${param.name }" />
				              </p>
				            </div>
            				<div class="span10"> 
            					<a href="javascript:void(0);" class="btn btn-info" onclick="doSearch();"><i class="icon-search icon-white" ></i>搜索</a> 
            					<a href="javascript:void(0);" class="btn btn-info" onclick="toViewOutstore();"><i class="icon-search icon-white" ></i>查看出库记录</a> 
            					<a href="javascript:void(0);" class="btn btn-info" onclick="toViewInstore();"><i class="icon-search icon-white" ></i>查看入库记录</a> 
            					
            				</div>
          				</fieldset>
     				</form>     
  				</div>
  				<!-- 搜索框 end -->
			   	<div class="table">
		      		<table class="table table-striped table-bordered">
			        	<thead>
				           	<tr>
				           	 <th>加工件名称</th>
				             <th>加工件数量</th>
				             <th>单位</th>
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
				            	<c:forEach items="${page.resultList}" var="treatstore" varStatus="i">
				                	<tr>
					                     <td>${treatstore.name}</a></td>
					                     <td>
					                     	${treatstore.mtnum}
					               	 	 </td>
					               	 	 <td>
					                     	${treatstore.unity}
					               	 	 </td>
					                     <td>
						                    <a class="btn btn-primary" href="#" onclick="tooutStore('${treatstore.id}');"><i class="icon-edit icon-white"></i>手工出库</a>
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
