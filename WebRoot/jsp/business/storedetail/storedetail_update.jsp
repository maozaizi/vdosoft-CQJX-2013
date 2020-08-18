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
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/RegExp.js" type="text/javascript"></script>

<script type="text/javascript">
	function save(){
		var divId = document.getElementById("store"); 
        var result = checkText(divId);
   		if(result != ""){
   			alert(result);
   			return false;
   		}else{
			document.myform.action="${pageContext.request.contextPath}/api/storedetail/modifyStoreDetail";
			document.myform.submit();
		}
	}
	function back(){
		window.location.href = "${pageContext.request.contextPath}/api/storedetail/getStoreDetailList";
	}
</script>
</head>
<body>
	<div class="container-fluid">
	  	<div class="row-fluid">
		    <div class="span9">
		        <div class="page-header" id="store">
		        	<h4>修改设备信息</h4>
		        	<div class="row-fluid">
			        	<form action="" name="myform" class="form-inline">
							<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/storedetail/getStoreDetailList"/>
							<input type="hidden" name="id" id="id" value="${param.id }"/>
							 <div class="span5">
						      	
						        <div class="control-group">
						          <label class="control-label-big" for="materialDetail">物料描述</label>
						            <input id="materialDetail" name="materialDetail" class="input-large" type="text" value="${storeMap.materialDetail }" readonly="readonly" />
						        </div>
						        <div class="control-group">
						          <label class="control-label-big" for="referencePrice">参考价</label>
						            <input id="referencePrice" name="referencePrice" class="input-small" type="text" value="${storeMap.referencePrice }" readonly="readonly"/>
						        </div>
						        <div class="control-group">
						          <label class="control-label-big" for="unitPrice">单价</label>
						            <input id="unitPrice" name="unitPrice" class="input-small" type="text" value="${storeMap.unitPrice }" check="1" checktype="number"/>
						        </div>
						        <div class="control-group">
						          <label class="control-label-big" for="materialType">类型</label>
						            <select name="materialType" id="materialType" class="input-small">
						            	<option value="1" <c:if test="${storeMap.materialType==1}">selected</c:if>>材料1</option>
						            	<option value="2" <c:if test="${storeMap.materialType==1}">selected</c:if>>材料2</option>
						            	<option value="3" <c:if test="${storeMap.materialType==1}">selected</c:if>>材料3</option>
						            	<option value="4" <c:if test="${storeMap.materialType==1}">selected</c:if>>材料4</option>
						            	<option value="5" <c:if test="${storeMap.materialType==1}">selected</c:if>>材料5</option>
						            </select>
						        </div>
						      </div>
						      <div class="span5">
							       <div class="control-group">
							          <label class="control-label-big" for="materialGroup">物资组</label>
							            <input id="materialGroup" name="materialGroup" class="input-large" type="text" value="${storeMap.materialGroup }" readonly="readonly"/>
							        </div>
							        <div class="control-group">
							          <label class="control-label-big" for="materialCode">物料编码</label>
							            <input id="materialCode" name="materialCode" class="input-large" type="text"  value="${storeMap.materialCode }" readonly="readonly"/>
							        </div>
							      	<div class="control-group">
							         <label class="control-label-big" for="unity">单位</label>
							           <input id="unity" name="unity" class="input-small" type="text" value="${storeMap.unity }" readonly="readonly"/>
							       </div>
							       <div class="control-group">
							          <label class="control-label-big" for="num">数量</label>
							            <input id="num" name="num" class="input-small" type="text" value="${storeMap.num }" checktype="number" check="1" />
						           </div>
						      </div>
						</form>
					</div>
				</div>
				<!-- 按钮 start -->
				  <div class="form-actions">
				  	<a class="btn" href="javascript:void(0);" onclick="back();">返　回</a>
				  	<a class="btn btn-primary" href="javascript:void(0);" onclick="save();">保　存</a>
				  </div>
				<!-- 按钮结束 end -->
			</div>
		</div>
	</div>
</form>
</body>
</html>
