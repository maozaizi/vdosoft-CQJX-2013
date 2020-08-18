<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>设备信息管理列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/js/uploadify/uploadify.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqBootstrapValidation.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/RegExp.js"></script>
<script type="text/javascript">
	$(function () { 
		$("input,select,textarea").not("[type=submit]").jqBootstrapValidation();  
		$("#proDept").bind('change',function(){
				var data = {"proDept":$(this).val()};
				$('#deptFrom').html("<option  value=''>请选择</option>");
				$.ajax({
					data:data,
					url:'${pageContext.request.contextPath}/ajaxapi/drillingCrew/getTeamList',
					dataType:'json',
					success:function(data,status,jaxData){
						for(var i = 0;i<data.teamlist.length;i++){
							var item = data.teamlist[i];
							var option = "<option value = '"+item.teamcode+"'>"+item.teamname+"</option>";
							$('#deptFrom').append(option);
						};
					},
					error:function(){
						alert('获取井队信息失败！'); 
					},
					timeout:5000
				});
			});
	});
	function doAdd(){
		document.myform.action="${pageContext.request.contextPath}/api/businrepairinfo/addMachiningMessage";
		$('#myform').submit();
	}
        		// 函数，3个参数，表单名字，表单域元素名，限制字符；
	   function textCounter(field, countfield, maxlimit) {  
			var s=field.value.length;
			if(s>maxlimit){
				field.value=field.value.substr(0,maxlimit)
			}else{
				countfield.innerHTML="已输入" + s + "个字符";
			} 
	
	   }
	   function back(){
			document.myform.action="${pageContext.request.contextPath}/api/mywork/getMyWork";
			document.myform.submit();
		}
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
	    	<div class="span9">
	    		<form name="myform" action="" id="myform" class="form-horizontal" novalidate="">
		        <div class="page-header">
		        	<h3>机加登记</h3>
		        </div>
		        <div class="page-header">
		        	<h4>加工件信息</h4>
		        	<div class="row-fluid">
		        	<div class="span6">
				        <div class="control-group">
				          <label class="control-label" for="bodyCode">送修单位</label>
				          <div class="controls">
				             <select id="proDept" name="proDept" style="width: 110px;">
				            	<option selected="selected" value="">
											请选择
								</option>
								<c:forEach var="lev" items='${web:getDataItem("pro_dept")}'>
									<option value="${lev.dataItemCode }">
										${lev.dataItemName }
									</option>
								</c:forEach>
							</select>
					        <select style="width: 80px" id="deptFrom" name="deptFrom" check="1">
									<option selected="selected" value="">
											请选择
									</option>
							</select>
				          </div>  
				        </div>
				        <div class="control-group">
				          <label class="control-label" for="selfCode">规格</label>
				          <div class="controls">
				            <input id="equipmentModel" name="equipmentModel" class="input-medium" type="text" maxlength="20" required/>
				          </div>  
				        </div>
				        <div class="control-group">
				          <label class="control-label" for="selfCode">图号</label>
				          <div class="controls">
				            <input id="figureNum" name="figureNum" class="input-medium" type="text" maxlength="20" required/>
				          </div>  
				        </div>
				      </div>
				      <div class="span4">
				        <div class="control-group">
				          <label class="control-label" for="deptFrom">名称</label>
				          <div class="controls">
				            <input id="equipmentName" name="equipmentName" class="input-medium" type="text" maxlength="40" required />
				          </div>  
				        </div>
				        <div class="control-group">
				          <label class="control-label" for="workHour">计划数量</label>
				          <div class="controls">
				          	<input id="planNum" name="planNum" class="input-medium" type="number" required maxlength="5"/>
				          </div>	
				        </div>
				        <div class="control-group">
				          <label class="control-label" for="selfCode">修制类别</label>
				          <div class="controls">
				            <input id="makeSort" name="makeSort" class="input-medium" type="text" maxlength="20" required/>
				          </div>  
				        </div>
		        	</div>
		        	
	        	</div>
	        	 <!-- 送修单位意见start -->
	        	<div>
		        	<h4>施工要求</h4>
		        			<div>    
				        			<ul class="inline">
								    <li><p class="muted">请输入1-500字的存在问题</p></li>
								    <li><p id="plable" class="text-success">已输入0字符</p></li>
								    </ul>	   
							</div>
				    <p><textarea rows="2" class="span10" name="problem" id="problem" onkeyup="textCounter(problem,plable,500);"></textarea></p>
		        </div>
		         <!-- 上传送修单图片start -->
		       <%@include file="/jsp/public/uploadify.jsp" %>
		        <!-- 上传送修单图片end -->
		        </form>
			   	<div class="form-actions">
					<a class="btn" href="javascript:void(0);" onclick="back();" >返　回</a>
					<a class="btn btn-primary" href="javascript:void(0);" onclick="doAdd();">提　交</a>
				</div>
	    	</div>
	    </div>
    </div>	
</body>
</html>
