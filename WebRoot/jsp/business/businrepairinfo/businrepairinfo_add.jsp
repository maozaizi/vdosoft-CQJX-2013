<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>进修单 </title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/js/uploadify/uploadify.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jqBootstrapValidation.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/RegExp.js"></script>
<script type="text/javascript">
	$(function () { 
		$("input,select,textarea").not("[type=submit]").jqBootstrapValidation();
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
							var option = "<option value = '"+item.teamcode+"'>"+item.teamname+"</option>";
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
	function doAdd(){
		var problem = document.getElementById("problem").value;
		if(problem==""){
			alert("存在问题为必填项,请填写!");
			return;
		}
		var opinionDeptFrom = document.getElementById("opinionDeptFrom").value;
		if(opinionDeptFrom==""){
			alert("送修单位意见为必填项,请填写!");
			return;
		}
		var wholeSituation = document.getElementById("wholeSituation").value;
		if(wholeSituation==""){
			alert("缺件及完整性为必填项,请填写!");
			return;
		}
		document.myform.action="${pageContext.request.contextPath}/api/businrepairinfo/addBusInrepairInfo";
		$('#myform').submit();
	}
	
	function openeq(index){
		window.open("${pageContext.request.contextPath}/api/businrepairinfo/chooseEquipmentInfo?index="+index,"","width=300,height=800,scrollbars=yes");
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
	   function back(){
			document.myform.action="${pageContext.request.contextPath}/api/mywork/getMyWork";
			document.myform.submit();
		}
		
		function changeHours(){
			var hours = document.getElementById("repairType").value;
			if(hours==1){
				document.getElementById("quotaHour").value = document.getElementById("quotaHours").value;
				document.getElementById("cost").value = document.getElementById("majorCost").value;
			}else if(hours==2){
				document.getElementById("quotaHour").value = document.getElementById("mediumHour").value;
				document.getElementById("cost").value = document.getElementById("mediumCost").value;
			}else{
				document.getElementById("quotaHour").value = document.getElementById("checkHour").value;
				document.getElementById("cost").value = document.getElementById("checkCost").value;
			}
		}
		
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
	    	<div class="span9">
	    		<form name="myform" action="" id="myform" class="form-horizontal" novalidate="" method="post">
		        <div class="page-header">
		        	<h3>进厂登记</h3><span style="color:red">(* 为必填项)</span>
		        </div>
		        <div class="page-header">
		        	<h4>设备信息</h4>
		        	<div class="row-fluid">
		            	<div class="span4">
		            		<div class="control-group">
					          <label class="control-label" for="equipmentName">设备名称</label>
					          	<input id="equipmentId" name="equipmentId" type="hidden" value=""/>
					          	<input id="docName" name="docName" type="hidden" value=""/>
					          	<input id="docSize" name="docSize" type="hidden" value=""/>
					          	<input id="docType" name="docType" type="hidden" value=""/>
					          	<input id="docUrl" name="docUrl" type="hidden" value=""/>
					          	<input id="quotaHours" name="quotaHours" type="hidden" value=""/>
					          	<input id="mediumHour" name="mediumHour" type="hidden" value=""/>
					          	<input id="checkHour" name="checkHour" type="hidden" value=""/>
					          	<input id="majorCost" name="majorCost" type="hidden" value=""/>
					          	<input id="mediumCost" name="mediumCost" type="hidden" value=""/>
					          	<input id="checkCost" name="checkCost" type="hidden" value=""/>
					          	<input id="cost" name="cost" type="hidden" value=""/>
					          	<input id="url" name="url" type="hidden" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
					            <div class="controls">
					            	<input id="equipmentName" name="equipmentName" class="input-medium" type="text" value="" readonly="readonly"/>
					        	</div>
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="standardNum">标准台</label>
					          <div class="controls">
					          	<input id="standardNum" name="standardNum" class="input-mini" type="text" value="" readonly="readonly"/>
					          </div>
					        </div>
					        
					        <div class="control-group">
					          <label class="control-label" for="repairType">修理类别</label>
					          <div class="controls">
					            <select class="input-small" name="repairType" id="repairType" onChange="changeHours();">
					              <option value="1">大修</option>
					              <option value="2">中修</option>
								  <option value="3">检修</option>
					            </select>
					          </div>  
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="repairCategory">修理类型</label>
					          <div class="controls">
					            <select class="input-small" id="repairCategory" name="repairCategory">
					            	<option value="2">正常修理</option>
					              	<option value="1">抢修</option>
					            </select>
					          </div>  
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="selfCode">自编号<span style="color:red"> *</span></label>
					          <div class="controls">
					            <input id="selfCode" name="selfCode" class="input-small" type="text" value="0000" maxlength="20" required/>
					          </div>  
					        </div>
		            	</div>
		            	<div class="span4">
		            		<div class="control-group">
					          <label class="control-label" for="value">原值</label>
					          <div class="controls">
					          	<input id="value" name="value" class="input-mini" type="text" readonly="readonly"/>
					          </div>
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="unity">单位</label>
					          <div class="controls">
					          	<input id="unity" name="unity" class="input-mini" type="text" value="" readonly="readonly"/>
					       	  </div>	
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="quotaHour">额定工时</label>
					          <div class="controls">
					          	<input id="quotaHour" name="quotaHour" class="input-mini" type="text" value="" readonly="readonly"/>
					       	  </div>	
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="repairProperties">修理性质</label>
					          <div class="controls">
					            <select class="input-small" name="repairProperties" id="repairProperties">
					              <option value="2">正常修理</option>
					              <option value="1">返厂修理</option>
								  <option value="3">返工修理</option>
					            </select>
					          </div>  
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="bodyCode">机身号</label>
					          <div class="controls">
					            <input id="bodyCode"  name="bodyCode" class="input-small" type="text" maxlength="20"/>
					          </div>  
					        </div>
		            	</div>
		            	 <div class="span2"> 
		            	 	<a class="btn" href="#" onclick="openeq('');">选择设备</a>
		            	 	<br/><br/><br/><br/><br/>
	            	 	 	
	            	 	</div>
	            	</div>
		        </div>
		        <!-- 进修单信息start -->
		        <div class="page-header">
		        	<h4>进修单信息</h4>
		        	<div class="row-fluid">
		        	<div class="span4">
				        <div class="control-group">
				          <label class="control-label" for="workHour">运转时间</label>
				          <div class="controls">
				          	<input id="workHour" name="workHour" class="input-small" type="text"/>
				          </div>	
				        </div>
				        <div class="control-group">
				          <label class="control-label" for="inDate">进厂时间</label>
				          <div class="controls">
				            <input id="idDate" name="inDate" class="input-small" type="text" required
				            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
				          </div>  	
				        </div>
				      </div>
				      <div class="span6">
				        <div class="control-group">
				          <label class="control-label" for="deptFrom">送修单位</label>
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
							井队
				          </div>
				        </div>
				        <div class="control-group">
				          <label class="control-label" for="inCarNo">送修车号</label>
				          <div class="controls">
				          	<input id="inCarNo" name="inCarNo" class="input-small" type="text"/>
				          </div>	
				        </div>
		        	</div>
		        	
		        	
	        	</div>
			        	<!-- 送修单位意见start -->
				        <div>
				        	<h4>存在问题<span style="color:red"> *</span></h4>
				        	<div>    
				        			<ul class="inline">
								    <li><p class="muted">请输入1-100字的存在问题</p></li>
								    <li><p id="plable" class="text-success">已输入0字符</p></li>
								    </ul>	   
							</div>
						    <p><textarea rows="2" class="span10" name="problem" id="problem" onkeyup="textCounter(problem,plable,100);" required></textarea></p>
		
				        </div>
				        <!-- 送修单位意见end -->
				        <!-- 送修单位意见start -->
				        <div>
				        	<h4>送修单位意见<span style="color:red"> *</span></h4>
				        	<div>    
				        			<ul class="inline">
								    <li><p class="muted">请输入1-100字的存在问题</p></li>
								    <li><p id="tishi" class="text-success">已输入0字符</p></li>
								    </ul>	   
							</div>
						    <p><textarea rows="2" class="span10" id="opinionDeptFrom" name="opinionDeptFrom" onkeyup="textCounter(opinionDeptFrom,tishi,100);" required></textarea></p>
				        </div>
				        <!-- 送修单位意见end -->
				         <!-- 完整性及缺件start -->
				        <div>
				        	<h4>完整性及缺件<span style="color:red"> *</span></h4>
				        	<div>    
				        			<ul class="inline">
								    <li><p class="muted">请输入1-100字的存在问题</p></li>
								    <li><p id="wslable" class="text-success">已输入0字符</p></li>
								    </ul>	   
							</div>
				            <p><textarea rows="2" class="span10" name="wholeSituation" id="wholeSituation" onkeyup="textCounter(wholeSituation,wslable,100);" required></textarea></p>
				        </div>
				        <!-- 完整性及缺件end -->
		        </div>
		        <!-- 进修单信息end -->

		        <!-- 上传送修单图片start -->
		       <%@include file="/jsp/public/uploadify.jsp" %>
		        <!-- 上传送修单图片end -->
		        </form>
			   	<div class="form-actions">
					<a class="btn" href="javascript:void(0);" onclick="back();" >返　回</a>
					<a class="btn btn-primary" href="javascript:void(0);" onclick="doAdd();">提　交</a>
				</div>
	    	</div>
	    </div>
    </div>	
</body>
</html