<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>添加井队信息</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqBootstrapValidation.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script type="text/javascript">
	   // 函数，3个参数，表单名字，表单域元素名，限制字符；
	   function textCounter(field, countfield, maxlimit) {  
			var s=field.value.length;
			if(s>maxlimit){
				field.value=field.value.substr(0,maxlimit)
			}else{
				countfield.innerHTML="已输入" + s + "个字符";
			} 
	   }
	   function add(){
	   		document.myform.action="${pageContext.request.contextPath}/api/drillingcrew/addCrew";
			$('#myform').submit();
	   }
	   
</script>
</head>
<body>
	<div class="container-fluid" id="eqInfo">
	  	<div class="row-fluid">
	  		<form action="" name="myform" class="form-horizontal" novalidate="" id="myform">
		    <div class="span10">
		        <div class="page-header">
		        	<h4>添加井队信息</h4>
		        	<div class="row-fluid">
						<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/drillingcrew/getDrillingCrewList"/>
				         <div class="span6">
			        		<div class="control-group">
					          <label class="control-label" for="teamName">井队队号<span style="color:red"> *</span></label>
					          <div class="controls">
					            <input id="teamName" name="teamName" class="input-medium" type="text"   maxlength="40"  required  data-validation-required-message="必填项" />
  							  </div>
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="teamCode">井队序号<span style="color:red"> *</span></label>
							  <div class="controls">
					            <input id="teamCode" name="teamCode" class="input-medium" type="text"  maxlength="40" />
  							  </div>	
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="projectDept">项目部</label>
					          <div class="controls">
						          <select id="projectDept" name="projectDept" style="width: 110px;">
										<c:forEach var="lev" items='${web:getDataItem("pro_dept")}'>
											<option value="${lev.dataItemCode }">
												${lev.dataItemName }
											</option>
										</c:forEach>
									</select>
  							  </div>
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="remark">备注</label>
							  <div class="controls">
					          	<div>
						        	<div>    
						        			<ul class="inline">
										    <li><p class="muted">请输入1-100字的存在问题</p></li>
										    <li><p id="tishi" class="text-success">已输入0字符</p></li>
										    </ul>	   
									</div>
								    <p><textarea rows="5" class="span12" id="remark" name="remark" onkeyup="textCounter(remark,tishi,100);"></textarea></p>
						        </div>
  							  </div>	
					        </div>
			        	</div>
		        	</div>
		        </div>
	        	<div class="form-actions">
				  	<a class="btn" href="javascript:void(0);" onclick="back();">返　回</a>
				  	<a class="btn btn-primary" href="javascript:void(0);" onclick="add();">保　存</a>
		  		</div>
		    </div>
		    </form>
	    </div>
    </div>    
</body>
</html>
