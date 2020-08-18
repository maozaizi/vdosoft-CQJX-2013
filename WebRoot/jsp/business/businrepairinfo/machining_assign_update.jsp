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
	$(function () { $("input,select,textarea").not("[type=submit]").jqBootstrapValidation(); } );
	function doAdd(){
		var firstIds = document.getElementsByName("firstId");
		for(var i =0 ;i<firstIds.length;i++){
			if(firstIds[i].value.length!=10){
				alert("施工卡号必须为10位！");
				return ;
			}
		}
		document.myform.action="${pageContext.request.contextPath}/api/businrepairinfo/addExternalMachining";
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
				           <input name="id" value="${machiningMessageMap.id}" type="hidden"/>
					        <input id="userName" name="userName" class="input-mini" value="${machiningMessageMap.userName}" type="text" maxlength="40" required />
				           </div>  
				        </div>
				        <div class="control-group">
				          <label class="control-label" for="selfCode">规格</label>
				          <div class="controls">
				            <input id="equipmentModel" name="equipmentModel" class="input-medium" value="${machiningMessageMap.equipmentModel}" type="text" maxlength="20" required/>
				          </div>  
				        </div>
				        <div class="control-group">
				          <label class="control-label" for="selfCode">图号</label>
				          <div class="controls">
				            <input id="figureNum" name="figureNum" class="input-medium" value="${machiningMessageMap.figureNum}" type="text" maxlength="20" required/>
				          </div>  
				        </div>
				      </div>
				      <div class="span5">
				        <div class="control-group">
				          <label class="control-label" for="deptFrom">名称</label>
				          <div class="controls">
				            <input id="equipmentName" name="equipmentName" class="input-medium" value="${machiningMessageMap.equipmentName}" type="text" maxlength="40" required />
				          </div>  
				        </div>
				        <div class="control-group">
				          <label class="control-label" for="workHour">计划数量</label>
				          <div class="controls">
				          	<input id="planNum" name="planNum" class="input-medium" type="number" value="${machiningMessageMap.planNum}" required maxlength="5"/>
				          </div>	
				        </div>
				        <div class="control-group">
				          <label class="control-label" for="selfCode">修制类别</label>
				          <div class="controls">
				            <input id="makeSort" name="makeSort" class="input-medium" type="text" value="${machiningMessageMap.makeSort}" maxlength="20" required/>
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
				    <p><textarea rows="2" class="span10" name="problem" value="${machiningMessageMap.problem}" id="problem" onkeyup="textCounter(problem,plable,500);"></textarea></p>
		        </div>
		        <!-- 上传送修单图片start -->
		       <%@include file="/jsp/public/uploadify.jsp" %>
		        <!-- 上传送修单图片end -->
		        <!-- 进修单信息end -->

				<input type="hidden" name="url" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
		        <div class="page-header">
		        	<h4>计划完工时间</h4>
		        	<div class="row-fluid">
		        		<div class="control-group">
							<input id="planEndTime" name="planEndTime" class="input-medium" type="text" 
		            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
		           		</div>
		        	</div>
		        </div>
		         <div class="page-header">
		        	<h4>车间调派</h4>
		        	<div class="row-fluid" id="divId">
		        		<table class="table table-striped table-bordered" id="SignFrame">
		                    	<tr>
		                        	<th>施工卡号</th>
									<th>施工车间</th>
		                        </tr>
		                    	<tr>
					              <td><input id="firstId" name="firstId" class="input-medium" type="text" check="1" maxlength="15"/> / <input id="domainNo"  name="domainNo" class="input-medium" type="text" readonly="readonly" value="${machiningMessageMap.mainCard}" check="1"/></td>
					              <td>
					                  <div class="input-append">
							              <input id="deptName_0" name="deptName" value="机加车间" class="input-medium" type="text" readonly="readonly" check="1"/>
						              </div>
					              </td>
					            </tr>
		        		</table>
		        	</div>
		        	
		        				        <!-- 备注 start -->
					<div>
						<h4>填写备注信息</h4>
							<div>    
				        			<ul class="inline">
								    <li><p class="muted">请输入1-500字的存在问题</p></li>
								    <li><p id="oclable" class="text-success">已输入<span id="oclable">0</span>字符</p></li>
								    </ul>	   
							</div>
	 					<p><textarea onkeyup="textCounter(distributeRemark,oclable,500);" check="1" rows="2" class="span10" name="distributeRemark" id="distributeRemark"></textarea></p>
					</div>
		        </div>

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
