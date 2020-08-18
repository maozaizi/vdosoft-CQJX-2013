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
		//手风琴收缩，图片切换
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
		            	    <c:if test="${companyName!=null}">
		                    <dl class="dl-horizontal">
		                        <dt>厂区：</dt>
		                        <dd>${companyName}
		                        </dd>
		                    </dl>
		                    </c:if>
		                    <c:if test="${mechTreatmentMap.workCard!=null}">
		                    <dl class="dl-horizontal">
		                        <dt>工卡号：</dt>
		                        <dd>${mechTreatmentMap.workCard}
		                        </dd>
		                    </dl>
		                    </c:if>
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
		                	<c:if test="${groupName!=null}">
		                	<dl class="dl-horizontal">
		                        <dt>车间：</dt>
		                        <dd>${groupName}
		                        </dd>
		                    </dl>
		                    </c:if>
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
							<c:if test="${mechTreatmentMap.planEndTime!=null}">
		                    <dl class="dl-horizontal">
								<dt>计划完工时间：</dt>
								<dd><tag:date  value="${mechTreatmentMap.planEndTime}" /></dd>
							</dl>
							</c:if>
		                </div>
		             </div> 
		             <div class="row-fluid">
		                	<div class="span10">
		                	<dl class="dl-horizontal">
		                        <dt>施工要求：</dt>
		                        <dd>${machiningMap.problem}</dd>
		                    </dl>
		                    
		                    <dl class="dl-horizontal">
		                        <dt>备注：</dt>
		                        <dd>${mechTreatmentMap.planRemark}</dd>
		                    </dl>
		                    <c:if test="${not empty mechTreatmentMap.opinionChecker}">
							<dl class="dl-horizontal">
								<dt>
									出厂检验意见：
								</dt>
								<dd>
									${mechTreatmentMap.opinionChecker}
								</dd>
							</dl>
							</c:if>
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
				                                </tr>
				                            </c:forEach>
				                        </c:if>
				                    </tbody>
				                </table>
				            </div>
				        </div>
			        </c:forEach>
		        </c:if>
	            <c:if test="${not empty busWorkCard}">
	            <div class="page-header">
		        	<h4>施工卡</h4>
		            <div id="accordion2" class="accordion">
				        <div class="accordion-group">
				          <div class="accordion-heading"> <a href="#collapse${busWorkCard.id}" data-parent="#accordion2" data-toggle="collapse" class="accordion-toggle collapsed" style="text-decoration:none;color: black;" onclick="changeImg();" > 施工卡号:${busWorkCard.workCard}<img id="collapseImg" src="${pageContext.request.contextPath}/img/up.png" style="float: right;"/></a> </div>
				          <div class="accordion-body in collapse" id="collapse${busWorkCard.id}">
				            <div class="accordion-inner">
				              <div class="row-fluid">
				                <div class="span5">
				                  <dl class="dl-horizontal">
				                    <dt>开工日期：</dt>
				                    <dd>
				                      <tag:date  value="${busWorkCard.startDate}" />
				                    </dd>
				                  </dl>
				                  <dl class="dl-horizontal">
				                    <dt>派工日期：</dt>
				                    <dd>
				                      <tag:date  value="${busWorkCard.appointDate}" />
				                    </dd>
				                  </dl>
				                </div>
				                <div class="span5">
				                   <dl class="dl-horizontal">
				                    <dt>指定完成日期：</dt>
				                    <dd>
				                      <tag:date  value="${busWorkCard.orderEndDate}" />
				                    </dd>
				                  </dl>
				                </div>
				              </div>
				            <c:if test="${not empty workTypeList}">
				              <div class="row-fluid">
			            		<table class="table table-center table-striped table-bordered">
			                	<thead>
			                         <tr>
			                            <th>序号</th>
			                            <th>工种</th>
			                            <th>修理人</th>
			                            <th>定额工时</th>
			                        </tr>
			                    </thead>
			                	<tbody>
			                	   <c:forEach var="work" items='${workTypeList}' varStatus="status">
			                    	<tr><td>${work.ranking}</td>
			                        	<td>${work.workType}</td>
			                            <td>${work.userName}</td>
			                            <td>${work.quotaHour}</td>
			                        </tr>
			                        </c:forEach>
			                    </tbody>
			                	</table>
			            		 </div>
			            	 </c:if>
				            </div>
				          </div>
				        </div>
				      </div>
		        </div>
	            </c:if>
	            
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
