<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>调度确认</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript">
		function openeq(id1,id2){
			window.open("${pageContext.request.contextPath}/api/logRecords/getLogRecordsList?id1="+id1+"&name1=com.BusDrilling&id2="+id2+"&name2=com.BusInrepairInfo");
		}
		
		function goBack() {
			 window.location.href = "${pageContext.request.contextPath}/api/businrepairinfo/getFactoryList";
		}	
		function downLoad(file){
			window.open("${pageContext.request.contextPath}/api/fileUpload/downLoad?file="+file);
		}
		function save(res){
			document.getElementById("result").value=res;
			document.myform.action="${pageContext.request.contextPath}/api/busdrilling/doSchedulingManage";
			document.myform.submit();
		}
       </script>
	</head>
	<body>
	<div class="container-fluid">
	   <div class="row-fluid">
		    <div class="span9">
				<div class="page-header">
		        	<h3>调度确认</h3>
		        </div>
		        
		         <div class="page-header">
		        	<h4>基本信息</h4>
		            <div class="row-fluid">
		            	<div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>工卡号：</dt>
		                        <dd>
		                         <a href="${pageContext.request.contextPath}/api/busdrilling/toBusDrillingDetailsPage?id=${busDrillingMap.Id}" target="_blank">${busDrillingMap.workCard}</a>
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>设备名称：</dt>
		                        <dd>${busInrepairInfoMap.equipmentName}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>机身编号：</dt>
		                        <dd>${busInrepairInfoMap.bodyCode}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
							<dt>单位：</dt>
							<dd>${busInrepairInfoMap.unity }</dd>
							</dl>
							<dl class="dl-horizontal">
								<dt>
									人员定额（人次）：
								</dt>
								<dd>
									${busInrepairInfoMap.quotaHour }
								</dd>
							</dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理类别：</dt>
		                        <dd>
		                        <c:if test="${busInrepairInfoMap.repairType==1}">大修</c:if>
		                        <c:if test="${busInrepairInfoMap.repairType==2}">中修</c:if>
		                        <c:if test="${busInrepairInfoMap.repairType==3}">检修</c:if>
		                        </dd>
		                    </dl>
		                </div>
		                
		                <div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>自编号：</dt>
		                        <dd>${busInrepairInfoMap.selfCode}</dd>
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
	            <c:if test="${not empty busWorkCardList}">
	            <div class="page-header">
		        	<h4>施工卡</h4>
		            <div id="accordion2" class="accordion">
					     <c:forEach items="${busWorkCardList}" var="busWorkCard" varStatus="i">
				        <div class="accordion-group">
				          <div class="accordion-heading"> <a href="#collapse${busWorkCard.id}" data-parent="#accordion2" data-toggle="collapse" class="accordion-toggle collapsed"> 施工卡号:${busWorkCard.workCard} </a> </div>
				          <div class="accordion-body in collapse" id="collapse${busWorkCard.id}">
				            <div class="accordion-inner">
				              <div class="row-fluid">
				                <div class="span5">
				                  <dl class="dl-horizontal">
				                    <dt>主修人：</dt>
				                    <dd>${busWorkCard.mainRepair}</dd>
				                  </dl>
				                  <dl class="dl-horizontal">
				                    <dt>派工日期：</dt>
				                    <dd>
				                      <tag:date  value="${busWorkCard.appointDate}" />
				                    </dd>
				                  </dl>
				                   <dl class="dl-horizontal">
				                    <dt>指定完成日期：</dt>
				                    <dd>
				                      <tag:date  value="${busWorkCard.orderEndDate}" />
				                    </dd>
				                  </dl>
				                </div>
				                <div class="span5">
				                <dl class="dl-horizontal">
				                    <dt>辅修人：</dt>
				                    <dd>${busWorkCard.assistRepair}</dd>
				                  </dl>
				                  <dl class="dl-horizontal">
				                    <dt>开工日期：</dt>
				                    <dd>
				                      <tag:date  value="${busWorkCard.startDate}" />
				                    </dd>
				                  </dl>
				                </div>
				                <div class="row-fluid">
				                <div class="span10">
				                  <dl class="dl-horizontal">
				                    <dt>车间修理意见：</dt>
				                    <dd>${busWorkCard.repairContent}</dd>
				                  </dl>
				                </div>
				                </div>
				              </div>
				            </div>
				          </div>
				        </div>
				        </c:forEach>
				      </div>
		        </div>
	            </c:if>
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
		        <form action="" name="myform" id="myform">
		        	<input type="hidden" name="id" value="${busDrillingMap.id}"/>
					<input type="hidden" name="zgsResult" id="zgsResult" value=""/>
					<input type="hidden" name="result" id="result" value=""/>
					 <input type="hidden" name="file" id="file" value=""/>
					<input id="url" name="url" type="hidden" value="${pageContext.request.contextPath}/api/businrepairinfo/getFactoryList"/>
		        	 <div class="page-header">
			        	  <div class="row-fluid">
				        	<span class="span5">
				        		<div class="control-group">
				        		 <dl class="dl-horizontal">
				                        <dt>出厂地点：</dt>
				                        <dd><input id="factoryWhere" name="factoryWhere" value="${busDrillingMap.factoryWhere}" class="input-medium" type="text" maxlength="8"/></dd>
				                    </dl> 
						        </div>
				        	</span>
				        	<span class="span4">
				        		<div class="control-group">
						         	<dl class="dl-horizontal">
				                        <dt>出厂时间：</dt>
				                        <dd><input id="factoryDate" name="factoryDate" value="<tag:date  value='${busDrillingMap.factoryDate}' />" class="input-medium" type="text" required
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" /></dd>
				                    </dl> 
						        </div>
				        	</span>
			        	  </div>
		        	 <div>    
		        	 </form>
		       	<div class="form-actions">
				  	<a class="btn" href="javascript:void(0)" onclick="goBack();">取消</a> 
				  	<a class="btn btn-primary" href="javascript:void(0)" onclick="save(0);">确认出厂</a>
				  	<a class="btn btn-primary" href="javascript:void(0)" onclick="save(1);">发送监造</a>
				</div> 
	           <div class="page-header">
				<h4>操作历史记录</h4>
		  		<div class="table">
		  		<table class="table table-striped table-bordered"> 
                 	<tr>  
						<div class="row form-horizontal">
						<th>操作动态<span style="float:right"><a href="#" onclick="openeq('${busDrillingMap.Id}','${busInrepairInfoMap.id}');">更多>></a></span></th>
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
