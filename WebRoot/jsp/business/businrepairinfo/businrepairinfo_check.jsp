<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
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
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript">
		function modifyTest(repairStep){
		    document.getElementById("repairStep").value=repairStep;
			document.myform.action="${pageContext.request.contextPath}/api/businrepairinfo/modifyBusInrepairInfoTest";
			document.myform.submit();
		}
		
		function goBack() {
			  window.location.href = "${pageContext.request.contextPath}/api/mywork/getMyWork";
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
			        	<h3>设备进厂检验</h3>
			        </div>
			        <div class="page-header">
		        	<h4>基本信息</h4>
		            <div class="row-fluid">
		            	<div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>设备名称：</dt>
		                        <dd>${busInrepairInfoMap.equipmentName}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>产值:</dt>
		                        <dd>${busInrepairInfoMap.cost}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
							<dt>单位：</dt>
							<dd>${busInrepairInfoMap.unity }</dd>
							</dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理类别：</dt>
		                        <dd>
		                        <c:if test="${busInrepairInfoMap.repairType==1}">大修</c:if>
		                        <c:if test="${busInrepairInfoMap.repairType==2}">中修</c:if>
		                        <c:if test="${busInrepairInfoMap.repairType==3}">检修</c:if>
		                        </dd>
		                    </dl>
		                    
							<dl class="dl-horizontal">
		                        <dt>送修单位：</dt>
		                        <dd>${web:getDeptBycode(busInrepairInfoMap.deptFrom)}井队</dd>
		                    </dl>
		                     <dl class="dl-horizontal">
		                        <dt>自编号：</dt>
		                        <dd>${busInrepairInfoMap.selfCode}</dd>
		                    </dl>
		                </div>
		                
		                <div class="span5">
					        <dl class="dl-horizontal">
					          <dt>修理类型：</dt>
					          <dd>
					          	<c:if test="${busInrepairInfoMap.repairCategory==1}">抢修</c:if>
								<c:if test="${busInrepairInfoMap.repairCategory==2}">正常修理</c:if>
							  </dd>
					        </dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理性质：</dt>
		                        <dd>
		                        <c:if test="${busInrepairInfoMap.repairProperties==1}">返厂修理</c:if>
		                        <c:if test="${busInrepairInfoMap.repairProperties==2}">正常修理</c:if>
		                        <c:if test="${busInrepairInfoMap.repairProperties==3}">返工修理</c:if>
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
								<dt>
									原值（元）：
								</dt>
								<dd>
									${busInrepairInfoMap.equipmentValue}
								</dd>
							</dl>
							 <dl class="dl-horizontal">
		                        <dt>人员定额：</dt>
		                        <dd>${busInrepairInfoMap.quotaHour}</dd>
		                    </dl>
		                
		                </div>
		                <div class="row-fluid">
		                <div class="span9">
		                <dl class="dl-horizontal">
		                        <dt>存在问题：</dt>
		                        <dd>${busInrepairInfoMap.problem}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
								<dt>
									送修单位意见：
								</dt>
								<dd>
									${busInrepairInfoMap.opinionDeptFrom }
								</dd>
							</dl>
							<dl class="dl-horizontal">
								<dt>
									完整性及缺件情况：
								</dt>
								<dd>
									${busInrepairInfoMap.wholeSituation}
								</dd>
							</dl>
		                </div>
		                </div>
		                
		            </div> 
		        </div>
			         <c:if test="${not empty fileList}">
			        <div class="page-header">
			        <input type="hidden" name="file" id="file" value=""/>
			         </form>
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
		        	<form name="myform">
	        		<input id="id" name="id" type="hidden" value="${busInrepairInfoMap.id}"/>
	        		<input id="repairStep" name="repairStep" type="hidden" value=""/>
		        	<input type="hidden" name="instanceId" value="${instanceId }"/>
					<input type="hidden" name="nodeId" value="${nodeId }"/>
					<input type="hidden" name="stepId" value="${stepId }"/>
					<input type="hidden" name="url" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
				        <!-- 上传附件 -->
				        <%@include file="/jsp/public/uploadify.jsp" %>
				        <!-- 检验意见 start -->
				        <div class="page-header">
				        	<h4>检验意见</h4>
				        	<div>    
				        			<ul class="inline">
								    <li><p class="muted">请输入1-500字的存在问题</p></li>
								    <li><p id="trlable" class="text-success">已输入0字符</p></li>
								    </ul>	   
							</div>
				        	<p><textarea rows="2" class="span15" name="testRemark" id="testRemark" onkeyup="textCounter(testRemark,trlable,500);"></textarea></p>
				        	
				        </div>
		        	</form>
			        <!-- 检验意见 end -->
	        		<div class="form-actions">
						<a class="btn" onclick="modifyTest(1);" href="javascript:void(0)" >检验不通过</a>
						<a class="btn" onclick="back();" href="javascript:void(0)" >取 消</a>
						<a class="btn btn-primary" href="javascript:void(0)" onclick="modifyTest(3);">检验通过</a>
					</div>
			    </div>
			</div>
		</div>	 
	</body>
</html>
