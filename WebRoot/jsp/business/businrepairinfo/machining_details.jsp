<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>机加详情</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript">
		function openeq(id1,id2){
		window.open("${pageContext.request.contextPath}/api/logRecords/getLogRecordsList?id1="+id1+"&name1=com.MechTreatment&id2="+id2+"&name2=com.MachiningMessage");
		}
		function downLoad(file){
		window.open("${pageContext.request.contextPath}/api/fileUpload/downLoad?file="+file);
		}
       </script>
       <style type="text/css">
	.table-center th,.table-center td {
		 text-align: center;
	}
</style>
	</head>
	<body>
	<div class="container-fluid">
	   <div class="row-fluid">
		    <div class="span9">
				<div class="page-header">
		        	<h3>机加详情信息</h3>
		        </div>
		         <div class="page-header">
		        	<h4>基本信息</h4>
		            <div class="row-fluid">
		            	<div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>名称：</dt>
		                        <dd>${machiningMap.equipmentName}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>规格型号：</dt>
		                        <dd>${machiningMap.equipmentModel}</dd>
		                    </dl>
							<dl class="dl-horizontal">
		                        <dt>计划数量：</dt>
		                        <dd>${machiningMap.planNum}</dd>
		                    </dl>
		                </div>
		                <div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>送修单位：</dt>
		                        <dd>${machiningMap.userName}</dd>
		                    </dl>
		                     <dl class="dl-horizontal">
								<dt>图号：</dt>
								<dd>
									${machiningMap.figureNum}
								</dd>
							</dl>
							<dl class="dl-horizontal">
								<dt>
									修制类别：
								</dt>
								<dd>
									${machiningMap.makeSort }
								</dd>
							</dl>
		                </div>
		             </div> 
		             <div class="row-fluid">
		                	<div class="span10">
		                	<dl class="dl-horizontal">
		                        <dt>施工要求：</dt>
		                        <dd>${machiningMap.problem}</dd>
		                    </dl>
		                  </div>
		              </div>
		        </div>
	            <c:if test="${not empty fileList}">
		        <div class="page-header">
		        <input type="hidden" name="file" id="file" value=""/>
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
				<h4>操作历史记录</h4>
		  		<div class="table">
		  		<table class="table table-striped table-bordered"> 
                 	<tr>  
						<div class="row form-horizontal">
						<th>操作动态<span style="float:right"><a href="#" onclick="openeq('${mechTreatmentMap.Id}','${machiningMap.Id}');">更多>></a></span></th>
						</div>
					</tr>
				</table>
				  <table class="table table-striped table-bordered">
			          	<tbody>
				           	<c:if test="${empty logRecordsList}">
				           		<tr>
				                    <td colspan="6" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                 </tr>   
				           	</c:if>
				           	<c:if test="${not empty logRecordsList}">
			            	<c:forEach items="${logRecordsList}" var="logRecords" varStatus="i">
			            	<c:if test="${(i.index+1)<6}"> 
			                 <tr>
			                     <td> ${logRecords.remark}</td>
			                 </tr>
			                 </c:if>
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
