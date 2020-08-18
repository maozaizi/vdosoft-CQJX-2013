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
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function fina(){
		document.myform.action="${pageContext.request.contextPath}/api/businrepairinfo/getStoreList";
		document.myform.submit();
	}
	
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
    			<!-- title start -->
      			<div>
     			<h3>库存明细1</h3>
     			</div>
     			<!-- title end -->
     			<!-- 搜索框 start -->
     			<div class="row-fluid">
	    			<form class="form-inline" name="myform">
	    			<input type="hidden" name="orgId" id="orgId" value="${param.orgId }"/>
	    				<fieldset>
	    				<legend><span class="label label-info">按条件搜索库存信息</span></legend>
					        <div class="span5">
					          <div class="control-group">
					            <label class="control-label" for="materialClass">物料大类</label>
				              	<input id="id" name="id" type="hidden" />
				              	<input id="materialClass" name="materialClass" class="input-medium" type="text" value="${param.materialClass }" />
					          </div>
					          <div class="control-group">
					            <label class="control-label" for="materialDetail">物料描述</label>
				              	<input id="materialDetail" name="materialDetail" class="input-medium" type="text" value="${param.materialDetail }" />
					          </div>
					          <div class="control-group">
					           <label for="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;时间</label>
				                <input id="startDate" name="startDate" class="input-small" type="text"  value="${param.startDate }"
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
						           — <input id="endDate" name="endDate" class="input-small" type="text" value="${param.endDate }"
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
				              
					          </div>
					        </div>
					        <div class="span4">
					          <div class="control-group">
					            <label class="control-label" for="materialGroup">&nbsp;&nbsp;&nbsp;物料组</label>
				              	<input id="materialDetail" name="materialGroup" class="input-medium" type="text" value="${param.materialGroup }" />
					          </div>
					          <div class="control-group">
					            <label class="control-label" for="materialCode">物料编码</label>
					             <input id="materialCode" name="materialCode" class="input-medium" type="text" value="${param.materialCode }" />
					          </div>
					        </div>
            			</fieldset>	
				  		<div class="span10">
				  			<a class="btn btn-info" href="javascript:void(0)" onclick="fina();"><i class="icon-search icon-white"></i>&nbsp;搜 索</a>
				  		</div>
	    			</form>
	    		</div>
	    		<!-- 搜索框 end -->
	    		<!-- 列表 start -->
	    		<div class="table">
			   		<h4>库存信息列表</h4>
		      		<table class="table table-striped table-bordered">	
	                	<thead>
		                	<tr> 
		                		<th>选择</th>
		                        <th>物料描述</th>
		                        <th>物料组</th>
		                        <th>物料编码</th>
		                        <th>参考价</th>
		                        <th>单价</th>
		                        <th>数量</th>
		                        <th>单位</th>
		                        <th>车间编号</th>
		                     </tr>
                		</thead>
	                    <tbody>
	                    	<c:if test="${empty page.resultList}">
	                    		<tr>
		                            <td colspan="10" style="text-align: center;color:red;">暂无数据！</td>
		                         </tr>   
	                    	</c:if>
	                    	<c:if test="${not empty page.resultList}">
		                    	<c:forEach items="${page.resultList}" var="store" varStatus="i">
		                    	<tr onclick="parent.selectMaterial('${store.id}','${store.materialDetail}','${store.materialClass}','${store.materialGroup}','${store.materialCode}','${store.referencePrice}','${store.unitPrice}','${store.num}','${store.unity}','${store.deptName}')">
			                            <td><input type="radio" name="id" value="${store.id }"/></td>
			                            <td>${store.materialDetail}</td>
			                            <td>${store.materialGroup}</td>
			                            <td>${store.materialCode}</td>
			                            <td>${store.referencePrice}</td>
			                            <td>${store.unitPrice}</td>
			                            <td>${store.num}</td>
			                            <td>${store.unity}</td>
			                            <td>${store.deptName}</td>
			                        </tr>
		                        </c:forEach>
	                        </c:if>
	                    </tbody>
	                </table>
	                <div class="page">
						<%@include file="/jsp/public/standard.jsp"%>
					</div>
					<!-- 列表 end --> 
				</div>
			</div>
		</div>
	</div>
</body>
</html>
