<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>物料信息修改</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqBootstrapValidation.js"></script> 	
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/RegExp.js" type="text/javascript"></script>
<script type="text/javascript">
	function save(){
		document.myform.action="${pageContext.request.contextPath}/api/material/saveMaterialInfo";
		$('#myform').submit();
	}
	function back(){
		window.location.href = "${pageContext.request.contextPath}/api/material/getList";
	}
	$(function () {$("input,select,textarea").not("[type=submit]").jqBootstrapValidation(); } );
</script>
</head>
<body>
	<div class="container-fluid">
	  	<div class="row-fluid">
		    <div class="span9">
		        <div class="page-header">
		        	<h4>修改物料信息</h4>
		        	<div class="row-fluid" id="material">
					<form action="" method="post" name="myform" id="myform" class="form-horizontal">
					<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/material/getList"/>
					<input type="hidden" name="id" id="id" value="${param.id }"/>
					 <div class="span6">
				        <div class="control-group">
				          <label class="control-label" for="materialDetail">物料描述</label>
				          <div class="controls">
					          <input id="materialDetail" name="materialDetail" class="input-large" type="text" value="${materialInfo.materialDetail }" required/>
				          </div>
				        </div>
				         <div class="control-group">
					          <label class="control-label" for="otherName">物料别名</label>
					          <div class="controls">
					          	<input id="otherName" name="otherName" class="input-medium" type="text" value="${materialInfo.otherName }"/>
					          </div>
				        </div>
				        <div class="control-group">
				          <label class="control-label" for="materialGroup">物资组</label>
				          <div class="controls">
					          <input id="materialGroup" name="materialGroup" class="input-large" type="text" value="${materialInfo.materialGroup }"/>
				          </div>
				        </div>
				        <div class="control-group">
				          <label class="control-label" for="materialClass">大类</label>
				          <div class="controls">
					          <input id="materialClass" name="materialClass" class="input-small" type="text" value="${materialInfo.materialClass }"/>
				          </div>
				        </div>
				        <div class="control-group">
					          <label class="control-label" for="isFormal">是否正式</label>
					          <div class="controls">
					          	<select id="isFormal" name="isFormal" style="width: 110px;">
					            	<option value="1" <c:if test="${materialInfo.isFormal == 1}">selected</c:if> >是</option>
					            	<option value="0" <c:if test="${materialInfo.isFormal == 0}">selected</c:if> >否</option>
								</select>	
					          </div>
				        </div>
				      </div>
				      <div class="span5">
				        <div class="control-group">
				          <label class="control-label" for="materialCode">物料编码</label>
				          <div class="controls">
					          <input id="materialCode" name="materialCode" class="input-large" type="text"  value="${materialInfo.materialCode }"/>
				          </div>
				        </div>
				        <div class="control-group">
				          <label class="control-label" for="unitPrice">单价</label>
				          <div class="controls">
					          <input id="unitPrice" name="unitPrice" class="input-small" type="number" value="${materialInfo.unitPrice }"required/>
				          </div>
				        </div>
				         <div class="control-group">
				          <label class="control-label" for="unity">单位</label>
				          <div class="controls">
				          	<input id="unity" name="unity" class="input-small" type="text" value="${materialInfo.unity }" required/>
				          </div>
				        </div>
				        <div class="control-group">
				          <label class="control-label" for="unity">物料类型</label>
				          <div class="controls">
				          		<select id="materialType" name="materialType" style="width: 110px;">
					            	<option  value="">
												请选择
									</option>
									<c:forEach var="lev" items='${web:getDataItem("materialtype")}'>
										<option value="${lev.dataItemValue }" <c:if test="${lev.dataItemValue == materialInfo.materialType }">selected="selected"</c:if>>
											${lev.dataItemName }
										</option>
									</c:forEach>
								</select>
				          </div>
				        </div>
				      </div>
					</form>
					</div>
				</div>
				<div class="form-actions">
				  	<a class="btn" href="#" onclick="back();">返　回</a>
				  	<a class="btn btn-primary" href="#" onclick="save();">保　存</a>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>
