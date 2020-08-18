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
				document.myform.action="${pageContext.request.contextPath}/api/mechtreatment/modifyMechTreatmentOpinionCheck";
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
			window.open("${pageContext.request.contextPath}/api/logRecords/getLogRecordsList?id1="+id1+"&name1=com.MechTreatment&id2="+id2+"&name2=com.MachiningMessage");
			}
			function downLoad(file){
			window.open("${pageContext.request.contextPath}/api/fileUpload/downLoad?file="+file);
			}
			function checknum(){
				var outnum = document.getElementById("plannum").value;
				var storenum = document.getElementById("completeNum").value;
				if(outnum>storenum){
					document.getElementById("outnum").value = storenum;
				}
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
		            <div class="row-fluid">
		            	<div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>厂区：</dt>
		                        <dd>${companyName}
		                        </dd>
		                    </dl>
		                   	<dl class="dl-horizontal">
								<dt>计划完工时间：</dt>
								<dd>
									<tag:date  value="${mechTreatmentMap.startDate}" />
								</dd>
							</dl>
		                </div>
		                <div class="span5">
		                	<dl class="dl-horizontal">
		                        <dt>车间：</dt>
		                        <dd>${groupName}
		                        </dd>
		                    </dl>
		                </div>
		             </div> 
		             <div class="row-fluid">
		                	<div class="span10">
		                	<dl class="dl-horizontal">
		                        <dt>施工要求：</dt>
		                        <dd>
		                        	${machiningMap.dispatchingRemark}
		                        </dd>
		                    </dl>
		                  </div>
		                </div>
		        </div>
		        <div class="page-header">
		        	<h4>加工件信息</h4>
		        	<div class="row-fluid">
		            	<table class="table table-striped table-bordered">
		                	<thead>
		                    	<tr>
		                        	<th>加工/修理件名称</th>
		                            <th>数量</th>
		                            <th>送修车间</th>
		                        </tr>
		                    </thead>
		                	<tbody>
		                	   <c:forEach var="machining" items='${machiningList}' varStatus="status">
		                    	<tr>
		                        	<td>${machining.equipmentName}</td>
		                            <td>${machining.planNum}</td>
		                            <td>${machining.deptfromname}</td>
		                        </tr>
		                        </c:forEach>
		                    </tbody>
		                </table>
		            </div>
		        	
		        </div>
		       
			      <!-- 检验结果 start -->
       	 	   <form name="myform" action="" id="myform" class="form-horizontal">
				  <input type="hidden" name="stepId" id="stepId" value="${stepId }"/>
				  <input type="hidden" name="nodeId" id="nodeId" value="${nodeId }"/>
				  <input type="hidden" name="plannum" id="plannum" value="${machiningMap.planNum}"/>
				  <input type="hidden" name="instanceId" id="instanceId" value="${instanceId }"/>
				  <input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
       	 		  <input name="busId" id="busId" value="${mechTreatmentMap.id }" type="hidden"/>
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
		        	  	  		<div>    
				        			<ul class="inline">
								    <li><p class="muted">请输入1-100字的存在问题</p></li>
								    <li><p id="oclable" class="text-success">已输入0字符</p></li>
								    </ul>	   
							  </div>
			              <p><textarea rows="2" class="span9" name="opinionChecker" id="opinionChecker" check="1" onkeyup="textCounter(opinionChecker,oclable,100);"></textarea>
		              	 
		        	 	  </form>
		        	 </div>
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
						<th>操作动态<span style="float:right"><a href="javascript:void(0);" onclick="openeq('${mechTreatmentMap.Id}','${machiningMap.Id}');">更多>></a></span></th>
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
