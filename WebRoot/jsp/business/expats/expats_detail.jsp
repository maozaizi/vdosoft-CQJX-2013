<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>上井服务详情</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript">
			function goBack(){
				window.close();
			}
			function collapseDiv(divId){
				var style =document.getElementById(divId).style.display;
				if(style=='none'){
					document.getElementById(divId).style='display:block';
				}else{
					document.getElementById(divId).style='display:none';
				}
			}
		
       </script>
	</head>
	<body>
	<div class="container-fluid">
	   <div class="row-fluid">
		    <div class="span9">
				<div class="page-header">
		        	<h3>上井服务详情</h3>
		        </div>
		         <div class="page-header">
		        	<h4>基本信息</h4>
		            <div class="row-fluid">
		            	<div class="span5">
		            		 <dl class="dl-horizontal">
		                        <dt>任务编号：</dt>
		                        <dd>${expatsMap.taskCode}
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
			                    <dt>完工日期：</dt>
			                    <dd>
			                      <tag:date  value="${expatsMap.backTime}"/>
			                    </dd>
			                  </dl>
		                </div>
		                <div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>井队：</dt>
		                        <dd>
		                        <c:forEach var="lev" items='${web:getDataItem("pro_dept")}'>
									<c:if test='${fn:contains(expatsMap.projectDept,lev.dataItemValue)}'>${lev.dataItemName}-</c:if>
								</c:forEach>
		                        ${expatsMap.expatsTo}
		                        </dd>
		                    </dl>
		                </div>
		                <div class="row-fluid">
			                <div class="span9">
			                    <dl class="dl-horizontal">
			                        <dt>任务描述：</dt>
			                        <dd>${expatsMap.taskDetail}</dd>
			                    </dl>
			                </div>
		                </div>
		            </div> 
		        </div>
		         <div class="page-header">
		        	<h4 style="cursor: pointer;" onclick="collapseDiv('plan${i.index+1}')">材料详情</h4>
		            <div class="row-fluid" id="plan${i.index+1}" style="display: none">
		            	<table class="table table-striped table-bordered">
		                	<thead>
		                    	<tr>
									<th>物料组</th>
									<th>物料编码</th>
									<th>物料描述</th>
									<th>数量</th>
									<th>单位</th>
		                        </tr>
		                    </thead>
		                    <tbody>
		                        <c:if test="${not empty materialList}">
		                            <c:forEach items="${materialList}" var="outStoreInfo" varStatus="i">
		                                <tr>
		                                    <td>${outStoreInfo.materialGroup}</td>
		                                    <td>${outStoreInfo.materialCode}</td>
		                                    <td>${outStoreInfo.materialDetail}</td>
		                                    <td>${outStoreInfo.storeNum}</td>
		                                    <td>${outStoreInfo.unity}</td>
		                                </tr>
		                            </c:forEach>
		                        </c:if>
		                    </tbody>
		                </table>
		            </div>
		        </div>
		        <form name="myform" action="">
		        	<input id="url" name="url" type="hidden" value="${pageContext.request.contextPath}/api/expats/getExpatsInfoList"/>
		        	<input id="id" name="id" type="hidden" value="${expatsMap.id}"/>
		            <div class="page-header">
			            <div id="accordion2" class="accordion">
						     <c:forEach items="${expatsDetailList}" var="expatsDetail" varStatus="i">
					        <div class="accordion-group">
					          <div class="accordion-heading"> <a href="#collapse${expatsDetail.id}" data-parent="#accordion2" data-toggle="collapse" class="accordion-toggle collapsed"> </a> </div>
					          <div class="accordion-body in collapse" id="collapse${expatsDetail.id}">
					            <div class="accordion-inner">
					              <div class="row-fluid">
					                <div class="span5">
					                  <dl class="dl-horizontal">
					                    <dt>车间：</dt>
					                    <dd>${expatsDetail.deptName}</dd>
					                  </dl>
					                  <dl class="dl-horizontal">
					                    <dt>施工人员：</dt>
					                    <dd>
					                      ${expatsDetail.expatsPerson}
					                    </dd>
					                  </dl>
					                   
					                </div>
					                
					              </div>
					            </div>
					          </div>
					        </div>
					        </c:forEach>
					    </div>
			        </div>
		        </form>
		        <div class="form-actions">
					<p>
						<a href="javascript:void(0);" onclick="goBack();" class="btn">关 闭</a>
					</p>
				</div>
			</div>
		</div>
	</div>
	</body>
</html>
