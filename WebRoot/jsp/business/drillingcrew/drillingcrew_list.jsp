<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>井队管理列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script type="text/javascript">
	function doSearch(){
		document.form.action="${pageContext.request.contextPath}/api/drillingcrew/getDrillingCrewList";
		document.form.submit();
	}
	
	function toAdd(){
		document.form.action="${pageContext.request.contextPath}/api/drillingcrew/toAddcrew";
		document.form.submit();
	}
	function toUpdate() {
		var crews = document.getElementsByName("chooseid");
		var checked = false;
		for (var i = 0; i < crews.length; i++) {
			if (crews[i].checked) {
				checked = true;
				document.getElementById("id").value=crews[i].value;
			}
		}
		
		if (!checked) {
			alert("请选择井队！");
			return false;
		}
		document.form.action="${pageContext.request.contextPath}/api/drillingcrew/toUpdateCrew";
		document.form.submit();
	}
	
	function doDel() {
		var equipment = document.getElementsByName("chooseid");
		var checked = false;
		for (var i = 0; i < equipment.length; i++) {
			if (equipment[i].checked) {
				checked = true;
				document.getElementById("id").value=equipment[i].value;
			}
		}
		
		if (!checked) {
			alert("请选择井队！");
			return false;
		}
		if (confirm("您确定删除该井队吗？")) {
			document.form.action="${pageContext.request.contextPath}/api/drillingcrew/delDrillingcrew";
			document.form.submit();
		}
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
      			<div class="page-header">
			        <h3>井队管理列表</h3>
     			</div>
     			<div class="row-fluid">
	    			<form class="form-inline" name="form">
	    			<input type="hidden" name="id" id="id"/>
	    			<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/drillingcrew/getDrillingCrewList"/>
	    				<fieldset>
            				<legend><span class="label label-info">按条件搜索井队信息</span></legend>
				        	<div class="span6">
				          		<div class="control-group">
				            		<label class="control-label" for="teamName">井队序号</label>
			              			<input id="teamName" name="teamName" class="input-medium" type="text" value="${param.teamName }"/>
				          		</div>
				          		<div class="control-group" style="">
					            	<label class="control-label" for="projectDept">井队队号</label>
					              	<input id="teamCode" class="input-medium" name="teamCode" type="text" value="${param.teamCode }" />
					          	</div>
				        	</div>
					        <div class="span4">
					        	<div class="control-group" style="">
					            	<label class="control-label" for="projectDept">备注</label>
					              	<select id="projectDept" name="projectDept" style="width: 110px;">
					              		<option selected="selected" value="">
											请选择
										</option>
										<c:forEach var="lev" items='${web:getDataItem("pro_dept")}'>
											<option value="${lev.dataItemCode }" <c:if test='${param.projectDept==lev.dataItemCode}'>selected="selected"</c:if>>
												${lev.dataItemName }
											</option>
										</c:forEach>
									</select>
					          	</div>
					        </div>
					        <div class="span10">
				       			<a class="btn btn-info" href="javascript:void(0)" onclick="doSearch();"><i class="icon-search icon-white"></i>&nbsp;搜 索</a>
				       			<a class="btn btn-primary" href="javascript:void(0)" onclick="toAdd();"><i class="icon-plus icon-white"></i>&nbsp;添 加</a>
				       			<a class="btn btn-primary" href="javascript:void(0)" onclick="toUpdate();"><i class="icon-edit icon-white"></i>&nbsp;修 改</a> 
				       			<a class="btn btn-primary" href="javascript:void(0)" onclick="doDel();"><i class="icon-remove icon-white"></i>&nbsp;删 除</a>
				       		</div>
            			</fieldset>
	    			</form>
	    		</div>
   				<div class="table">
		      		<table class="table table-striped table-bordered">
			        	<thead>
		                	<tr>
		                		<th></th>
		                		<th>井队序号</th>
		                       	<th>井队队号</th>
		                        <th>备注</th>
		                     </tr>
	                	</thead>
	                    <tbody>
	                    	<c:if test="${empty page.resultList}">
	                    		<tr>
		                            <td colspan="10" style="text-align: center;color:red;">暂无数据！</td>
		                         </tr>   
	                    	</c:if>
	                    	<c:if test="${not empty page.resultList}">
		                    	<c:forEach items="${page.resultList}" var="crews" varStatus="i">
			                        <tr onclick="selectTr('${crews.id}');">
			                        	<td><input type="radio" id="id_${crews.id }" name="chooseid" value="${crews.id }"/></td>
			                        	<td>${crews.teamCode}</td>
			                        	<td>${crews.teamName}</td>
			                        	<td>${crews.remark}</td>
			                        </tr>
			                    </c:forEach>
			                </c:if>
	                    </tbody>
	                </table>
				   <div class="page">
						<%@include file="/jsp/public/standard.jsp"%>
					</div>
	    		</div>
	    	</div>
	   	</div>		
	 </div>  		
</body>
</html>
