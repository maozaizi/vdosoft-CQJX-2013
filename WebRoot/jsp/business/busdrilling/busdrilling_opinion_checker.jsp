<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>出厂检验</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqBootstrapValidation.js"></script> 
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(function () { $("input,select,textarea").not("[type=submit]").jqBootstrapValidation(); } );
			function saveCheck(res){
				document.getElementById("result").value=res;
				document.myform.action="${pageContext.request.contextPath}/api/busdrilling/modifyBusDrillingOpinionCheck";
				$('#myform').submit();
			}
			function back(){
				document.myform.action="${pageContext.request.contextPath}/api/mywork/getMyWork";
				document.myform.submit();
			}
    		// 函数，3个参数，表单名字，表单域元素名，限制字符；
		   function textCounter(field, countfield, maxlimit){  
				var s=field.value.length;
				if(s>maxlimit){
					field.value=field.value.substr(0,maxlimit)
				}else{
					countfield.innerHTML="已输入"+s+"字符";
				} 
		   }
		   function openeq(id1,id2){
			window.open("${pageContext.request.contextPath}/api/logRecords/getLogRecordsList?id1="+id1+"&name1=com.BusDrilling&id2="+id2+"&name2=com.BusInrepairInfo");
		}
		</script>
	</head>
	<body>
		<div class="container-fluid" id="divId">
	  		<div class="row-fluid">
		    	<div class="span9">
			        <div class="page-header">
			        	<h3>检验</h3>
			        </div>
			        <!-- 基本信息 -->
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
			                        <dd><a href="${pageContext.request.contextPath}/api/busdrilling/toBusDrillingDetailsPage?id=${busDrillingMap.Id}" target="_blank">${busDrillingMap.workCard}</a>
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
			                        <dt>修理类别：</dt>
			                        <dd>
			                        <c:if test="${busInrepairInfoMap.repairType==1}">大修</c:if>
			                        <c:if test="${busInrepairInfoMap.repairType==2}">中修</c:if>
			                        <c:if test="${busInrepairInfoMap.repairType==3}">检修</c:if>
			                        </dd>
			                    </dl>
			                     <dl class="dl-horizontal">
			                        <dt>设备产值：</dt>
			                        <dd>${busInrepairInfoMap.equipmentValue}</dd>
			                    </dl>
			                </div>
			                <div class="span5">
			                	<dl class="dl-horizontal">
			                        <dt>车间：</dt>
			                        <dd>${groupName}
			                        </dd>
			                    </dl>
			                    <dl class="dl-horizontal">
			                        <dt>自编号：</dt>
			                        <dd>${busInrepairInfoMap.selfCode}</dd>
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
			                    <dl class="dl-horizontal">
			                        <dt>工时：</dt>
			                        <dd>${busInrepairInfoMap.quotaHour}</dd>
			                    </dl>
			                </div>
		            	</div> 
		            	 <div class="row-fluid">
			                <div class="span9">
			                	<dl class="dl-horizontal">
			                        <dt>存在问题：</dt>
			                        <dd>${busInrepairInfoMap.problem}</dd>
			                    </dl>
			                </div>
			             </div>   
			        </div>
			        <!-- 试车参数 -->
			        <div class="page-header">
			        	 <h4 class="title-color">试车参数</h4>
			        	 <div class="row-fluid">
			            	<div class="span5">
			            		 
						          <dl class="dl-horizontal">
						            <dt> 油耗率(g/KW.h)</dt>
						            <dd>${busTestRecordMap.oilRate}</dd>
						          </dl>
						          
			            	</div>
			            	<div class="span5">
			            		  
						          <dl class="dl-horizontal">
						            <dt> 电耗</dt>
						            <dd>${busTestRecordMap.currentDrain}</dd>
						          </dl>
			            	</div>
		            	 </div>  
			        </div>
			        <!-- 试车参数 end -->
			        <!-- 检验结果 start -->
        	 	  <form name="myform" action="" id="myform" class="form-horizontal">
					  <input type="hidden" name="stepId" id="stepId" value="${stepId }"/>
					  <input type="hidden" name="nodeId" id="nodeId" value="${nodeId }"/>
					  <input type="hidden" name="instanceId" id="instanceId" value="${instanceId }"/>
					  <input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
	       	 		  <input name="busId" id="busId" value="${busDrillingMap.id }" type="hidden"/>
		              <input name="result" id="result" value="" type="hidden"/>
			        <div class="page-header">
			        	 <h4 class="title-color">检验结果</h4>
			        	 <div class="row-fluid">
				        	<span class="span5">
				        		<div class="control-group">
						          <label class="control-label" for="inDate">完工时间</label>
						          <div class="controls">
						            <input id="completeDate" name="completeDate" class="input-small" type="text" required
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
						          </div>  	
						        </div>
				        	</span>
			        	</div>
			        	<c:set value="${ fn:split(busWorkMap.mainId, ',') }" var="mainId" />
			        	<c:set value="${ fn:split(busWorkMap.assistId,',') }" var="assistId" />
			        	<c:set value="${ fn:split(busWorkMap.mainRepair, ',') }" var="mainRepair" />
			        	<c:set value="${ fn:split(busWorkMap.assistRepair, ',') }" var="assistRepair" />
			        	<table class="table table-striped table-bordered" id="materialPlanTable">
		            			<tbody>
									<tr>
									  <th>修理人</th>
									  <th>工时</th>
									</tr>
									<c:forEach items="${mainId}" var="main" varStatus="i">
										<tr>
										  <input type="hidden" class="input-mini" name="workCard" value="${busWorkMap.workCard}"/>
										  <th>${mainRepair[i.index]}（主）<input type="hidden" class="input-mini" name="repairer" value="${main }"/></th>
										  <th><input type="text" class="input-mini" name="hours" value=""/></th>
										</tr>
									</c:forEach>
									<c:forEach items="${assistId}" var="assist" varStatus="i">
										<input type="hidden" class="input-mini" name="workCard" value="${busWorkMap.workCard}"/>
										<tr>
										  <th>${assistRepair[i.index]}（辅）<input type="hidden" class="input-mini" name="repairer" value="${assist }"/></th>
										  <th><input type="text" class="input-mini" name="hours" value=""/></th>
										</tr>
									</c:forEach>
								</tbody>
						</table>
			        	  	  <div>    
				        			<ul class="inline">
								    <li><p class="muted">请输入1-100字的存在问题</p></li>
								    <li><p id="oclable" class="text-success">已输入0字符</p></li>
								    </ul>	   
							  </div>
				              <p><textarea rows="2" class="span9" name="opinionChecker" id="opinionChecker" check="1" onkeyup="textCounter(opinionChecker,oclable,100);"></textarea>
			              	  
		        	 </div>
        	 	  </form>
			        <!-- 检验结果 end -->
	        	 	<div class="form-actions">
					  	<a class="btn" href="javascript:void(0)" onclick="back();">取消</a> 
					    <a class="btn" href="javascript:void(0)" onclick="saveCheck(0);">检验不通过</a>
					  	<a class="btn btn-primary" href="javascript:void(0)" onclick="saveCheck(1);">检验通过</a>
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
