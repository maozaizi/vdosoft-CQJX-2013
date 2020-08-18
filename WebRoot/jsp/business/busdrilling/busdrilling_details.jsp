<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>钻修详情</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript">
		 function openeq(id1,id2){
			window.open("${pageContext.request.contextPath}/api/logRecords/getLogRecordsList?id1="+id1+"&name1=com.BusDrilling&id2="+id2+"&name2=com.BusInrepairInfo");
		}
		function showinfo(type,state){
		   if(type=="sq"){
		    document.getElementById("divId"+state).style["display"]="none";
			document.getElementById("Edu_hidden"+state).style["display"]="none";
			document.getElementById("Edu_show"+state).style["display"]="inline";
		   }else if(type=="zk"){
		    document.getElementById("divId"+state).style["display"]="inline";
			document.getElementById("Edu_hidden"+state).style["display"]="inline";
			document.getElementById("Edu_show"+state).style["display"]="none";
		   }
		
		}
		function downLoad(file){
			window.open("${pageContext.request.contextPath}/api/fileUpload/downLoad?file="+file);
		}
		function collapseDiv(divId){
			var style =document.getElementById(divId).style.display;
			if(style=='none'){
				document.getElementById(divId).style='display:block';
			}else{
				document.getElementById(divId).style='display:none';
			}
		}
		function changeImg(){
			var src=document.getElementById('collapseImg').src;
			if(src.indexOf("up")>= 0 ){  
			    src=src.replace('up','down');
			}else{
				src=src.replace('down','up');
			}
			document.getElementById('collapseImg').src=src;
		}
       </script>
	</head>
	<body>
	<div class="container-fluid">
	   <div class="row-fluid">
		    <div class="span9">
				<div class="page-header">
		        	<h3>钻修详情信息</h3>
		        </div>
		         <div class="page-header">
		        	<h4>基本信息</h4>
		            <div class="row-fluid">
		            	<div class="span5">
		            		 <dl class="dl-horizontal">
		                        <dt>厂区：</dt>
		                        <dd>${companyName}
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>工卡号：</dt>
		                        <dd>${busDrillingMap.workCard}
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>设备名称：</dt>
		                        <dd>${busDrillingMap.equipmentName}</dd>
		                    </dl>
		                   
		                    <dl class="dl-horizontal">
		                        <dt>修理类别：</dt>
		                        <dd>
		                        <c:if test="${busDrillingMap.repairType==1}">大修</c:if>
		                        <c:if test="${busDrillingMap.repairType==2}">中修</c:if>
		                        <c:if test="${busDrillingMap.repairType==3}">检修</c:if>
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>额定工时：</dt>
		                        <dd>${busDrillingMap.quotaHour}</dd>
		                    </dl>
		                     <dl class="dl-horizontal">
		                        <dt>机身编号：</dt>
		                        <dd>${busDrillingMap.bodyCode}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>送修单位：</dt>
		                        <dd>${web:getDeptBycode(busInrepairInfoMap.deptFrom)}井队</dd>
		                    </dl>
		                </div>
		                
		                <div class="span5">
		                	<dl class="dl-horizontal">
		                        <dt>车间：</dt>
		                        <dd>${groupName}</dd>
		                    </dl>
		                    
		                    <dl class="dl-horizontal">
		                        <dt>自编号：</dt>
		                        <dd>${busDrillingMap.selfCode}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理类型：</dt>
		                        <dd>
		                        <c:if test="${busDrillingMap.repairCategory==1}">抢修</c:if>
		                        <c:if test="${busDrillingMap.repairCategory==2}">正常修理</c:if>
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理性质：</dt>
		                        <dd>
		                        <c:if test="${busDrillingMap.repairProperties==1}">返厂修理</c:if>
		                        <c:if test="${busDrillingMap.repairProperties==2}">正常修理</c:if>
		                        <c:if test="${busDrillingMap.repairProperties==3}">返工修理</c:if>
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>产值：</dt>
		                        <dd>${busDrillingMap.repairValue}</dd>
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
							
							 <dl class="dl-horizontal">
		                        <dt>备注：</dt>
		                        <dd>${busDrillingMap.planRemark}</dd>
		                    </dl>
							<c:if test="${not empty busDrillingMap.opinionChecker}">
							<dl class="dl-horizontal">
								<dt>
									出厂检验意见：
								</dt>
								<dd>
									${busDrillingMap.opinionChecker}
								</dd>
							</dl>
							</c:if>
		                </div>
		                </div>
		                
		            </div> 
		        </div>
	            <c:if test="${not empty busPlanInfoList}">
		            <c:forEach items="${busPlanInfoList}" var="busPlanInfo" varStatus="i">
			            <div class="page-header">
				        	<h4 style="cursor: pointer;" onclick="collapseDiv('plan${i.index+1}')">${i.index+1}.材料申请 ${busPlanInfo.plan_code}</h4>
				            <div class="row-fluid" id="plan${i.index+1}" style="display: none">
				            	<table class="table table-striped table-bordered">
				                	<thead>
				                    	<tr>
											<th>物料组</th>
											<th>物料编码</th>
											<th>物料描述</th>
											<th>参考价</th>
											<th>数量</th>
											<th>单位</th>
											<th>类型</th>
				                        </tr>
				                    </thead>
				                    <tbody>
				                        <c:if test="${not empty web:getMaterialInfoList(busPlanInfo.id)}">
				                            <c:forEach items="${web:getMaterialInfoList(busPlanInfo.id)}" var="busMaterialPlan" varStatus="i">
				                                <tr>
				                                    <td>${busMaterialPlan.materialGroup}</td>
				                                    <td>${busMaterialPlan.materialCode}</td>
				                                    <td>${busMaterialPlan.materialDetail}</td>
				                                    <td>${busMaterialPlan.estimatePrice}</td>
				                                    <td>${busMaterialPlan.materialNum}</td>
				                                    <td>${busMaterialPlan.unity}</td>
				                                    <td>
				                                    	 <c:if test="${busMaterialPlan.type==1}">领用</c:if>
				                       					 <c:if test="${busMaterialPlan.type==2}">加工</c:if>
				                       					 <c:if test="${busMaterialPlan.type==3}">修理</c:if>
				                       					 <c:if test="${busMaterialPlan.type==4}">计划加领用</c:if>
				                       			    </td>
				                                </tr>
				                            </c:forEach>
				                        </c:if>
				                    </tbody>
				                </table>
				            </div>
				        </div>
			        </c:forEach>
		        </c:if>
	            <c:if test="${not empty busWorkCardList}">
	            <div class="page-header">
		        	<h4>施工卡</h4>
		            <div id="accordion2" class="accordion">
					     <c:forEach items="${busWorkCardList}" var="busWorkCard" varStatus="i">
				        <div class="accordion-group">
				          <div class="accordion-heading"> <a href="#collapse${busWorkCard.id}" data-parent="#accordion2" data-toggle="collapse" class="accordion-toggle collapsed" style="text-decoration:none;color: black;" onclick="changeImg();" > 施工卡号:${busWorkCard.workCard}<img id="collapseImg" src="${pageContext.request.contextPath}/img/up.png" style="float: right;"/></a> </div>
				           <c:if test="${not empty busWorkCard.mainRepair}">
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
				                  <c:if test="${not empty busDrillingMap.dispatchingRemark}">
									<dl class="dl-horizontal">
										<dt>
											车间派工意见：
										</dt>
										<dd>
											${busDrillingMap.dispatchingRemark}
										</dd>
									</dl>
									</c:if>
				                </div>
				                </div>
				              </div>
				            </div>
				          </div>
				          </c:if>
				        </div>
				        </c:forEach>
				      </div>
		        </div>
	            </c:if>
	            <c:forEach items="${busTestRecordList}" var="busTestRecord" varStatus="i">
		             <div class="page-header">
			        	<h4 style="cursor: pointer;" onclick="collapseDiv('test${i.index+1}');">${i.index+1}.试车记录 <tag:date value="${busTestRecord.createDate}" /></h4>
			            <div class="row-fluid" id="test${i.index+1}" style="display: none;">
			            	<div class="span5">
			            		 
			                    <dl class="dl-horizontal">
			                        <dt>油耗率(g/KW.h)：</dt>
			                        <dd>${busTestRecord.oilRate}</dd>
			                    </dl>
			                    
			                </div>
			                <div class="span5">
			                	
			                    <dl class="dl-horizontal">
			                        <dt>电耗：</dt>
			                        <dd>${busTestRecord.currentDrain}</dd>
			                    </dl>
			                   
			                </div>
			            </div> 
			        </div>
		        </c:forEach>
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
