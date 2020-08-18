<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>上井服务编辑</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript">
			function goBack(){
				window.location.href = "${pageContext.request.contextPath}/api/expats/getExpatsList";
			}
			function saveComplete(){
				document.myform.action="${pageContext.request.contextPath}/api/expats/saveExpatsComplete";
				document.myform.submit();
			}
       </script>
	</head>
	<body>
	<div class="container-fluid">
	   <div class="row-fluid">
		    <div class="span9">
				<div class="page-header">
		        	<h3>上井服务编辑</h3>
		        </div>
		         <div class="page-header">
		        	<h4>基本信息</h4>
		            <div class="row-fluid">
		            	<div class="span5">
		            		 <dl class="dl-horizontal">
		                        <dt>任务编号：</dt>
		                        <dd>${expatsInfo.taskCode}
		                        </dd>
		                    </dl>
		                </div>
		                <div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>井队：</dt>
		                        <dd>
		                        <c:forEach var="lev" items='${web:getDataItem("pro_dept")}'>
									<c:if test='${fn:contains(expatsMap.projectDept,lev.dataItemValue)}'>${lev.dataItemName}-</c:if>
								</c:forEach>${expatsInfo.expatsTo}
		                        </dd>
		                    </dl>
		                </div>
		                <div class="row-fluid">
			                <div class="span9">
			                    <dl class="dl-horizontal">
			                        <dt>任务描述：</dt>
			                        <dd>${expatsInfo.taskDetail}</dd>
			                    </dl>
			                </div>
		                </div>
		            </div> 
		        </div>
		        <form name="myform" action="">
		        	<input id="url" name="url" type="hidden" value="${pageContext.request.contextPath}/api/expats/getExpatsList"/>
		        	<input id="id" name="id" type="hidden" value="${expatsdeatail.id}"/>
		        	<input type="hidden" id="expatsPersonCode" name="expatsPersonCode"/>
		            <div class="page-header">
			            <div id="accordion2" class="accordion">
					        <div class="accordion-group">
					          <div class="accordion-heading"></div>
					          <div class="accordion-body in collapse" id="collapse${expatsdeatail.id}">
					            <div class="accordion-inner">
					              <div class="row-fluid">
					                <div class="span5">
					                  <dl class="dl-horizontal">
					                    <dt>车间：</dt>
					                    <dd>
					                    	<input value="${expatsdeatail.deptName}" class="input-medium" type="text" readonly="readonly"/>
					                    </dd>
					                  </dl>
					                   <dl class="dl-horizontal">
					                    <dt>交通方式：</dt>
					                    <dd>
					                     	<input id="transportation" name="transportation" value="${expatsdeatail.transportation}" readonly="readonly" class="input-medium" type="text" />
					                    </dd>
					                  </dl>
					                </div>
					              </div>
					              <div class="row-fluid">
					                <div class="span9">
					                  <dl class="dl-horizontal">
					                    <dt>施工人员：</dt>
					                    <dd>
					                      	<input id="expatsPerson" name="expatsPerson" value="${expatsdeatail.expatsPerson}" readonly="readonly" class="input-medium" type="text" readonly="readonly" /><a class="btn btn-medium" href="javaScript:void(0);" onclick="openeq('expatsPerson','expatsPersonCode');"><i class="icon-plus"></i>选择</a>
					                    </dd>
					                  </dl>
					                </div>
					              </div>
					              <div class="row-fluid">
					                <div class="span5">
					                  <dl class="dl-horizontal">
					                    <dt>完工时间：</dt>
					                    <dd>
					                    	<input id="departureDate" name="departureDate" class="input-medium" type="text"  value=""
		            							onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
					                    </dd>
					                  </dl>
					                   <dl class="dl-horizontal">
					                    <dt>实耗工时：</dt>
					                    <dd>
					                     	<input id="workHour" name="workHour" class="input-medium" type="text" />
					                    </dd>
					                  </dl>
					                </div>
					              </div>
					            </div>
					          </div>
					        </div>
					    </div>
			        </div>
		        </form>
		        <div class="form-actions">
					<p>
						<a href="javascript:void(0);" onclick="goBack();" class="btn">取  消</a>
						<c:if test="${expatsdeatail.isComplete ==0}">
							<a href="javaScript:void(0);" onclick="saveExpats();" class="btn btn-primary">确认上井</a>
						</c:if>
						<c:if test="${expatsdeatail.isComplete ==1}">
							<a href="javaScript:void(0);" onclick="saveComplete();" class="btn btn-primary">确认完工</a>
						</c:if>
					</p>
				</div>
			</div>
		</div>
	</div>
	</body>
</html>
