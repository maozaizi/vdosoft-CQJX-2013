<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>物料申请详细信息</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/RegExp.js"></script>
		<script type="text/javascript">
		function audit(type){
		        $("#zgsResult").val(type);      
				document.myform.action="${pageContext.request.contextPath}/api/busplanwf/toZgsAudit";
				document.myform.submit();
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
			   function goBack() {
				  window.location.href = "${pageContext.request.contextPath}/api/mywork/getMyWork";
			}
			function downLoad(file){
			window.open("${pageContext.request.contextPath}/api/fileUpload/downLoad?file="+file);
			}	
		</script>
	</head>
	<body>
		<div class="container-fluid">
		  <div class="row-fluid">
		    <div class="span9">
		    	
		        <div class="page-header">
		        	<h3>物料申请详细信息</h3>
		        </div>
		        
		        <div class="page-header">
		        	<h4>基本信息</h4>
		            <div class="row-fluid">
		            	<div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>申请编号：</dt>
		                        <dd>${busPlanInfoWfMap.planCode}
		                        </dd>
		                    </dl>
		            		 <dl class="dl-horizontal">
		                        <dt>厂区：</dt>
		                        <dd>${companyName}
		                        </dd>
		                    </dl>
		                </div>
		                
		                <div class="span5">
		                	<dl class="dl-horizontal">
		                        <dt>车间：</dt>
		                        <dd>${groupName}</dd>
		                    </dl>
		                </div>
		                
		            </div> 
		            <div class="row-fluid">
		                <div class="span9">
		                	<dl class="dl-horizontal">
		                        <dt>申请备注：</dt>
		                        <dd>${busPlanInfoWfMap.planRemark}</dd>
		                    </dl>
		                </div>
		             </div>
		             
		        </div>
		        
		        <div class="page-header">
		        	<h4>领用材料申请</h4>
		            <div class="row-fluid">
		            	<table class="table table-striped table-bordered">
		                	<thead>
		                    	<tr>
									<th>物料组</th>
									<th>物料编码</th>
									<th>物料描述</th>
									<th>参考价</th>
									<th>数量</th>
									<th>单位</th>
		                        </tr>
		                    </thead>
		                    <tbody>
		                        <c:if test="${empty busMaterialPlanWfList}">
		                            <tr>
		                            	<td colspan="6" style="text-align: center;"><font color='red'>暂无数据！</font></td>
		                            </tr>   
		                        </c:if>
		                        
		                        <c:if test="${not empty busMaterialPlanWfList}">
		                            <c:forEach items="${busMaterialPlanWfList}" var="busMaterialPlan" varStatus="i">
			                                <tr>
			                                    <td>${busMaterialPlan.materialGroup}</td>
			                                    <td>${busMaterialPlan.materialCode}</td>
			                                    <td>${busMaterialPlan.materialDetail}</td>
			                                    <td>${busMaterialPlan.estimatePrice}</td>
			                                    <td>${busMaterialPlan.materialNum}</td>
			                                    <td>${busMaterialPlan.unity}</td>
			                                </tr>
		                            </c:forEach>
		                        </c:if>
		                    </tbody>
		                </table>
		            </div>
		        </div>
		        
		        <c:if test="${not empty fileList}">
		        <div class="page-header">
		        	<h4>附件信息</h4>
		            <div class="row-fluid">
		            	<table class="table table-striped table-bordered">
		                	<thead>
		                    	<tr>
		                        	<th>文件名称</th>
		                            <th>文件大小</th>
		                            <th>上传时间</th>
		                            <th>上传人员</th>
		                        </tr>
		                    </thead>
		                	<tbody>
		                	   <c:forEach var="attachmentInfo" items='${fileList}' varStatus="status">
		                    	<tr>
		                        	<td><a href="javascript:void(0);" onclick="downLoad('${attachmentInfo.docUrl}');" title="${attachmentInfo.docName}" >${attachmentInfo.docName}</a></td>
		                            <td>${attachmentInfo.docSize}</td>
		                            <td><tag:date  value="${attachmentInfo.modifyDate}" /></td>
		                            <td>${attachmentInfo.createUser}</td>
		                        </tr>
		                        </c:forEach>
		                    </tbody>
		                </table>
		            </div>
		        </div>
		        </c:if>
			  <div class="page-header">
				<h4>历史审批记录</h4>
		  		<div class="table">
				  <table class="table table-striped table-bordered">
			        	<thead>
			            	<tr>
			            	 <th>审批记录:</th>
			            	</tr>
			           	</thead> 
			          	<tbody>
				           	<c:if test="${empty logRecordsList}">
				           		<tr>
				                    <td colspan="6" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                 </tr>   
				           	</c:if>
				           	<c:if test="${not empty logRecordsList}">
				            	<c:forEach items="${logRecordsList}" var="logRecords" varStatus="i">
				                 <tr>
				                     <td>&nbsp; ${logRecords.remark}</td>
				                 </tr>
				                 </c:forEach>
				             </c:if>
					 	</tbody>
			        </table>
				</div>
				</div>
		    </div>
		  </div>
		</div>
	</body>
</html>
