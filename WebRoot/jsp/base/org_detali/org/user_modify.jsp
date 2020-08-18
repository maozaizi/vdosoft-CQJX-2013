<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
		<link href="${pageContext.request.contextPath }/css/bootstrap/bootstrap.min.css" rel="stylesheet" media="screen" />
		<link href="${pageContext.request.contextPath}/css/bootstrap/datetimepicker.css" rel="stylesheet" media="screen"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/RegExp.js" type="text/javascript"></script>
		<script type="text/javascript">
			function checkForm() {
				var name = document.getElementById("name").value;
				if (name == "") {
					alert("请输入人员名称！");
					return ;
				}
					document.forms[0].action = "${pageContext.request.contextPath }/api/org/modifyUserInfo";
					document.forms[0].submit();
			}
			
			function goBack() {
				  window.location.href = "${pageContext.request.contextPath}/api/org/getInfo?orgCode=${userInfo.orgId}";
			}
		</script>
</head>
<body>
	<div class="MainContent">
		<div class="ListBlock">
		    <h3 class="cBlack">修改人员</h3>

			<!-- 添加人员 -->
			<form id="addForm" method="post" action="" >
			<input type="hidden" name="userId" id="userId" value="${userInfo.userId}"/>
			<input type="hidden" name="orgId" id="orgId" value="${userInfo.orgId}"/>
			<input type="hidden" name="tempUrl" id="tempUrl" value="${pageContext.request.contextPath}/api/org/getInfo?orgCode=${userInfo.orgId}"/>
			<div class="form-horizontal">
	        	<div class="control-group">
					<label class="control-label" for="personName">姓名</label>
					<div class="controls">
						<input type="text" value="${userInfo.name}" name="name" class="span2" id="name" placeholder="请输入姓名"/>
					</div>	
				</div>
				<div class="control-group">
					<label class="control-label" for="personSex">性别</label>
					<div class="controls">
					<c:if test="${userInfo.gender=='男'}">
						<label class="radio inline">
						<input type="radio" name="gender" id="gender" value="男" checked="checked" />男
						</label>
						<label class="radio inline">
						<input type="radio" name="gender" id="gender" value="女" />女
						</label>
					</c:if>
					<c:if test="${userInfo.gender=='女'}">
						<label class="radio inline">
						<input type="radio" name="gender" id="gender" value="男"/>男
						</label>
						<label class="radio inline">
						<input type="radio" name="gender" id="gender" value="女" checked="checked" />女
						</label>
					</c:if>
					</div>
				</div>
				<!-- 
	        	<div class="control-group">
					<label class="control-label" for="personName">民族</label>
					<div class="controls">
						<input type="text" value="${userInfo.nation}" name="nation" class="span2" id="nation" placeholder="请输入民族"/>
					</div>	
				</div>
	        	<div class="control-group">
					<label class="control-label" for="personName">身份证号码</label>
					<div class="controls">
						<input type="text" value="${userInfo.idcardNumber}" name="idcardNumber" class="span2" id="idcardNumber" placeholder="请输入身份证号码"/>
					</div>	
				</div>
				<div class="control-group">
               		<label class="control-label" for="personName">出生日期</label>
					<div class="controls  date form_date" data-date="" data-date-format="yyyy-mm-dd" data-link-format="yyyy-mm-dd">
					    <input size="10" type="text" value="${userInfo.birthData}" class="span2"  name="birthData" id="birthData" placeholder="请输入出生日期" value="1990-01-01" readonly/>
	                    <span class="add-on"><i class="icon-remove"></i></span>
						<span class="add-on"><i class="icon-th"></i></span>
               		</div>
               		
           		</div>
           		 -->
			 	<!--  
	        	<div class="control-group">
					<label class="control-label" for="personName">出生日期</label>
					<div class="controls">
						<input type="text" value="${userInfo.birthData}" name="birthData" id="birthData" placeholder="请输入出生日期" value="1990-01-01" onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="readonly"/>
					</div>	
				</div>
				
				<div class="control-group">
					<label class="control-label" for="personEducational">学历</label>
					<div class="controls" >
						<select class="span2" name="degree" id="degree">
							<option value="" >----请选择----</option>
							<c:forEach var="item" items="${XL}">
								<option value="${item.dataItemValue}" ${userInfo.degree == item.dataItemValue ?"selected='selected'":""}>${item.dataItemName}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				
	        	<div class="control-group">
					<label class="control-label" for="personName">专业</label>
					<div class="controls">
						<input type="text" value="${userInfo.professional}" name="professional" class="span2" id="professional" placeholder="请输入专业"/>
					</div>	
				</div>
				-->
	        	<div class="control-group">
					<label class="control-label" for="personName">职务</label>
					<div class="controls">
						<input type="text" value="${userInfo.position}"  name="position" class="span2" id="position" placeholder="请输入职务"/>
					</div>	
				</div>
	        	<div class="control-group">
					<label class="control-label" for="personName">电子邮件</label>
					<div class="controls">
						<input type="text" value="${userInfo.email}" name="email" class="span2" id="email" placeholder="请输入电子邮件"/>
					</div>	
				</div>
				<div class="control-group">
					<label class="control-label" for="remark">备注</label>
					<div class="controls">
						<textarea name="remark" class="span4" id="remark">${userInfo.remark}</textarea>
					</div>	
				</div>
				<div class="form-actions">
				  	<a class="btn btn-primary" href="#" name="submitter" onclick="checkForm();">保存</a>
			  		<a class="btn" href="#" onclick="goBack()">返回</a>
				</div>
	        </div>
			 </form>

</div>
</div>
	<script src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap-datetimepicker.js"></script>
	
	<script type="text/javascript">
    $('.form_date').datetimepicker({
        language:  'zh-CN',
        weekStart: 1,
        todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		forceParse: 0
    });

    
	</script>
</body>
</html>

