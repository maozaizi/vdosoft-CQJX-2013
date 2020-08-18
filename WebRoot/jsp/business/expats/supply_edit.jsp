<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>上井服务售后</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript">
			function goBack(){
				window.location.href = "${pageContext.request.contextPath}/api/expats/getSupplyList";
			}
			function openeq(name,code){
				window.open("${pageContext.request.contextPath}/api/busmechanic/toUserInfoChoose?receiveName="+name+"&receiveCode="+code,"","width=600,height=400");
			}
			function saveSupply(){
				document.myform.action="${pageContext.request.contextPath}/api/expats/saveSupply";
				if(checkForm()){
					document.myform.submit();
				}
			}
			function openMaterial(){
				window.open("${pageContext.request.contextPath}/api/expats/toChooseMaterialInfo","","width=1250,height=700,scrollbars=yes");
			}
			function checkForm(){
				var storeNums = document.getElementsByName("storeNum");
				var flag = true;
				var re =/^[0-9]*$/;
				for(var i=0;i<storeNums.length;i++){
					if(storeNums[i].value==""){
						storeNums[i].style["borderColor"]="red";
						flag=false;
					}else{
						if(!re.test(storeNums[i].value)){
							storeNums[i].style["borderColor"]="#C09853";
							flag=false;
						}else{
							storeNums[i].style["borderColor"]="";
						}
					}
				}
				if(!flag){
					document.getElementById("errorMessage").style="display:block;";
				}
				return flag;
			}
       </script>
	</head>
	<body>
	<div class="container-fluid">
	   <div class="row-fluid">
		    <div class="span9">
				<div class="page-header">
		        	<h3>上井服务售后</h3>
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
		        	<input id="url" name="url" type="hidden" value="${pageContext.request.contextPath}/api/expats/getSupplyList"/>
		        	<input id="id" name="id" type="hidden" value="${expatsdeatail.id}"/>
			        <div class="page-header">
					<h4>补充材料</h4>
					 <div class="row-fluid">
					 	<div style="margin:5px 0 5px 5px;"><a class="btn btn-medium" onclick="openMaterial();" href="#"><i class="icon-plus"></i>选择材料</a></div>
			            	<table class="table table-striped table-bordered" id="materialPlanTable">
		            			<tbody>
									<tr>
									  <th>物料描述</th>
						              <th>物料组</th>
						              <th>物料编码</th>
									  <th>数量</th>
									  <th>操作</th>
									</tr>
									<c:if test="${empty busMaterialPlanList}">
			                            <tr>
			                            	<td colspan="5" style="text-align: center;color:red;">暂无数据！</td>
			                            </tr>   
			                        </c:if>
			                        
			                        <c:if test="${not empty busMaterialPlanList}">
			                            <c:forEach items="${busMaterialPlanList}" var="busMaterialPlan" varStatus="i">
			                                <tr id="materialPlanTr_${i.index+1}">
			                                    <td>${busMaterialPlan.materialDetail}<input type="hidden" name="materialDetail" value="${busMaterialPlan.materialDetail}"></td>
			                                    <td>${busMaterialPlan.materialGroup}<input type="hidden" name="materialGroup" value="${busMaterialPlan.materialGroup}"></td>
			                                    <td>${busMaterialPlan.materialCode}<input type="hidden" name="materialCode" value="${busMaterialPlan.materialCode}"></td>
			                                    <td>${busMaterialPlan.unity}<input type="hidden" name="unity" value="${busMaterialPlan.unity}"></td>
			                                    <td>${busMaterialPlan.estimatePrice}<input type="hidden" name="estimatePrice" value="${busMaterialPlan.estimatePrice}"></td>
			                                    <td>
				                                    <select class="input-small" name="type">
					                                    <option value="1" <c:if test="${busMaterialPlan.type==1}">selected</c:if>>领用</option>
					                                    <option value="4" <c:if test="${busMaterialPlan.type==4}">selected</c:if>>计划加领用</option>
					                                    <option value="2" <c:if test="${busMaterialPlan.type==2}">selected</c:if>>加工</option>
					                                    <option value="3" <c:if test="${busMaterialPlan.type==3}">selected</c:if>>修理</option>
				                                    </select>
			                                    </td>
			                                    <td><input id="remark" name="remark" class="input-mini" type="text" value="${busMaterialPlan.remark}"/></td>
			                                    <td><a href="javascript:void(0);" onclick="delRow(${i.index+1})">删除</a></td>
			                                </tr>
			                            </c:forEach>
			                        </c:if>
			            	</table>
			            </div>
			             <div id="errorMessage" style="display: none;"><font color="red">红色标识：必填项；黄色标识：请填写数字；</font></div>	
	            	</div>
		        </form>
		        <div class="form-actions">
					<p>
						<a href="javascript:void(0);" onclick="goBack();" class="btn">取  消</a>
						<a href="javaScript:void(0);" onclick="saveSupply();" class="btn btn-primary">保  存</a>
					</p>
				</div>
			</div>
		</div>
	</div>
	</body>
</html>
