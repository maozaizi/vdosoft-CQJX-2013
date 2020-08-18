<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>工具劳保登记</title>
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
			function openeq(){
					window.open("${pageContext.request.contextPath}/api/businrepairinfo/chooseEquipmentInfoBytype","","width=300,height=800,scrollbars=yes");
				}
			function doAdd(){
				document.myform.action="${pageContext.request.contextPath}/api/bustoolswf/saveToolsPlan";
				document.myform.submit();
			}
			
		</script>
	</head>
	<body>
		<div class="container-fluid">
			<div class="row-fluid">
		    	<div class="span9">
			        <div class="page-header">
			        	<h3>工具劳保登记</h3>
			        </div>
			        <form name="myform" action="" id="myform" class="form-horizontal" novalidate="" method="post">
			        <input id="url" name="url" type="hidden" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
			        <div class="page-header">
		        	<h4>基本信息</h4>
		        	<div class="row-fluid">
		            	<div class="span5">
		            		<div class="control-group">
					          <label class="control-label" for="equipmentName">名称</label>
					          	<input id="equipmentId" name="equipmentId" type="hidden" value=""/>
					          	<input id="standardNum" name="standardNum" type="hidden" value=""/>
					          	<input id="unity" name="unity" type="hidden" value=""/>
					          	<input id="value" name="value" type="hidden" value=""/>
					          	<input id="equipmentId_0" name="equipmentId_0" type="hidden" value=""/>
					          	<input id="equipmentDetail_0" name="equipmentDetail_0" type="hidden" value=""/>
					          	<input id="quotaHours" name="quotaHours" type="hidden" value=""/>
					          	<input id="quotaHour" name="quotaHour" type="hidden" value=""/>
					          	<input id="mediumHour" name="mediumHour" type="hidden" value=""/>
					          	<input id="checkHour" name="checkHour" type="hidden" value=""/>
					          	<input id="majorCost" name="majorCost" type="hidden" value=""/>
					          	<input id="mediumCost" name="mediumCost" type="hidden" value=""/>
					          	<input id="checkCost" name="checkCost" type="hidden" value=""/>
					          	<input id="serialnum" name="serialnum" type="hidden" value="${serialnum }"/>
					          	<input id="orgId" name="orgId" type="hidden" value="${orgId }"/>
					          	<input id="cost" name="cost" type="hidden" value=""/>
					            <div class="controls">
					            	<input id="equipmentName" name="equipmentName" class="input-large" type="text" value="" readonly="readonly"/>
					        	</div>
					        </div>
		            	</div>
		            	 <div class="span2"><a class="btn" href="#" onclick="openeq();">选择设备</a></div>
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
					              <td>
					              	<input id="workCard" name="workCard" class="input-medium" value="${dept}" type="text" readonly="true" maxlength="10"/>
					              </td>
					              <td>
				                  	${deptname}
				                  	<input id="deptName" name="deptName" type="hidden" value="${deptname}"/>
					              </td>
					            </tr>
		        		</table>
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
