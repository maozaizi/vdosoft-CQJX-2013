<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>设备信息管理列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function changeDiv(){
		var choose = document.getElementById("type").value;
		if(choose==2){
				document.getElementById("deptChoose").style.display="none";
				document.getElementById("crewChoose").style.display="block";
		}else{
			document.getElementById("crewChoose").style.display="none";
			document.getElementById("deptChoose").style.display="block";
		}
	}
	function doAdd(){
		var outtime = document.getElementById("outtime").value;
		if(outtime==null||outtime==""){
			alert("请填写出库日期!");
			return;
		}
		document.myform.action="${pageContext.request.contextPath}/api/mechtreatment/saveOutTreatStore";
		document.myform.submit();
	}
	function checknum(){
		var outnum = document.getElementById("outnum").value;
		var storenum = document.getElementById("storenum").value;
		if(outnum>storenum){
			document.getElementById("outnum").value = storenum;
		}
	}
</script>
<script type="text/javascript">
	$(function () { 
		$("#proDept").bind('change',function(){
				var data = {"proDept":$(this).val()};
				$('#deptFrom').html("<option  value=''>请选择</option>");
				$.ajax({
					data:data,
					url:'${pageContext.request.contextPath}/ajaxapi/drillingCrew/getTeamList',
					dataType:'json',
					success:function(data,status,jaxData){
						for(var i = 0;i<data.teamlist.length;i++){
							var item = data.teamlist[i];
							var option = "<option value = '"+item.teamname+"'>"+item.teamname+"</option>";
							$('#deptFrom').append(option);
						};
					},
					error:function(){
						alert('获取井队信息失败！'); 
					},
					timeout:5000
				});
			});
	
	} );
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
      			<div>
			        <h3>加工件库存出库操作</h3>
     			</div>
				<div class="page-header">
		        	<h4>基本信息</h4>
		            <div class="row-fluid">
		            	<div class="span5">
		            		 <dl class="dl-horizontal">
		                        <dt>加工件名称：</dt>
		                        <dd>${storeInfo.name}
		                        </dd>
		                    </dl>
		                </div>
		                <div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>加工件数量：</dt>
		                        <dd>
		                        	${storeInfo.mtnum}
		                        </dd>
		                    </dl>
		                </div>
		        </div>
		        <div class="row-fluid">
			                <div class="span9">
			                    <dl class="dl-horizontal">
			                        <dt>加工件单位：</dt>
			                        <dd>${storeInfo.unity}</dd>
			                    </dl>
			                </div>
		                </div>
		            </div> 
		        <form name="myform" action="">
		        	<input type="hidden" name="id" id="id" value="${storeInfo.id}"/>
		        	<input type="hidden" name="storenum" id="storenum" value="${storeInfo.mtnum}"/>
		        	<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/mechtreatment/gettreatstorelist"/>
		        	<div class="page-header">
			        	<h4>出库操作</h4>
			            <div class="row-fluid">
			            	<div class="span5">
			            		<dl class="dl-horizontal">
			                        <dt>出库数量：</dt>
			                        <dd>
			                        	<input id="outnum" name="outnum" class="input-medium" type="text" value="${storeInfo.mtnum}" onkeyup="checknum();"/>
			                        </dd>
			                    </dl>
		                	</div>
		                	<div class="span5">
			            		<dl class="dl-horizontal">
			                        <dt>出库时间：</dt>
			                        <dd>
			                        	<input id="outtime" name="outtime" class="input-small" type="text"
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
			                        </dd>
			                    </dl>
		                	</div>
			            </div>
			            <div class="row-fluid">
			            	<div class="span5">
			            		<dl class="dl-horizontal">
			                        <dt>出库类型：</dt>
			                        <dd>
			                        	<select class="span5" name="type" id="type" onChange="changeDiv();">
			                        			<option value="2">井队领用</option>
			                        			<option value="1">车间领用</option>
			                        	</select>
			                        </dd>
			                    </dl>
		                	</div>
			            </div>
			            <div class="row-fluid" id="deptChoose" style="display:none">
			            	<div class="span5">
			            		<dl class="dl-horizontal">
			                        <dt>领用车间：<br /></dt>
			                        <dd>
			                        	<select class="span5" name="orgCode" id="orgCode">
			                        		<option value="">请选择</option>
			                        		<c:forEach var="dept" items="${deptList}">
			                        			<option value="${dept.orgcode }">${dept.orgname }</option>
			                        		</c:forEach>
			                        	</select>
			                        <br /></dd>
			                    </dl>
		                	</div>
			            </div>
			            <div class="row-fluid" id="crewChoose" >
			            	<div class="span5">
			            		<dl class="dl-horizontal">
			                        <dt>领用井队：</dt>
			                        <dd>
			                        	<div class="controls">
								            <select id="proDept" name="proDept" style="width: 110px;">
								            	<option selected="selected" value="">
															请选择
												</option>
												<c:forEach var="lev" items='${web:getDataItem("pro_dept")}'>
													<option value="${lev.dataItemCode }">
														${lev.dataItemName }
													</option>
												</c:forEach>
											</select>
											<select style="width: 80px" id="deptFrom" name="deptFrom" check="1">
													<option selected="selected" value="">
															请选择
													</option>
											</select>
								          </div>
			                        </dd>
			                    </dl>
		                	</div>
			            </div>
			        </div>
		        </form> 
		        <div class="form-actions">
					<a class="btn" href="javascript:void(0);" onclick="back();" >返　回</a>
					<a class="btn btn-primary" href="javascript:void(0);" onclick="doAdd();">提　交</a>
				</div>
			</div>
		</div>
	</div>	         	
</body>
</html>
