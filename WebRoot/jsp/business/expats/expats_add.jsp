<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>上井服务</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/RegExp.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript">
			function openDept(rowId){
				var selectId = document.getElementById("deptId_"+rowId).value;
				var deptIdString =",";
				var deptIds = document.getElementsByName("deptId");
				for(var i=0;i<deptIds.length;i++){
					if(deptIds[i].value!=null&&deptIds[i].value!=""){
						deptIdString += "'"+deptIds[i].value+"',";
					}
				}
				window.open("${pageContext.request.contextPath}/api/businrepairinfo/toChooseDept?tmp=" + Math.round(Math.random() * 10000)+"&rowId="+rowId+"&deptIds="+deptIdString,"newwindow", "height=500, width=400, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
			}
			function openEquis(rowId){
				window.open("${pageContext.request.contextPath}/api/businrepairinfo/toChooseEquips?tmp=" + Math.round(Math.random() * 10000)+"&rowId="+rowId,"newwindow", "height=700, width=800, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no");
			}
			
			function inputDomain(domain){
				var value = domain.value;
				document.getElementById("domainNo").value = value;
				//判断是否有动态添加行数据有的话替换值
				var txtTRLastIndex = document.getElementById("txtTRLastIndex").value;
				if(txtTRLastIndex>2){
					for(var i=2;i<txtTRLastIndex;i++){
						document.getElementById("domainNo"+i).value = value;
					}
				}
			}
			function submit(){
				if(checkForm()){
					document.form.action="${pageContext.request.contextPath}/api/expats/saveExpatsInfo";
					$('#form').submit();
				}else{
					document.getElementById("errorMessage").style="display:block;";
				}
			}
			function addItemLine(){
				//读取最后一行的行号，存放在txtTRLastIndex文本框中
				var txtTRLastIndex = document.getElementById("txtTRLastIndex");
				var rowID = parseInt(txtTRLastIndex.value);
				var signFrame = document.getElementById("SignFrame");
				
				//添加行
				var newTR = signFrame.insertRow(signFrame.rows.length);
				newTR.id = "signItem" + rowID;
				
				var newNameTD=newTR.insertCell(0);
				
				newNameTD.innerHTML = "<div class='input-append'><input id='deptName_" + rowID + "' name='deptName' class='input-medium' type='text' onclick='openDept(" + rowID + ");' readonly='readonly' check='1'/><input id='deptId_" + rowID + "' class='input-medium' name='deptId' type='hidden' /></div>";
				
				var newNameTD=newTR.insertCell(1);
					
				newNameTD.innerHTML = "<input id='equipmentDetail_"+ rowID + "' name='equipmentDetail' class='input-medium' onclick='openEquis(" + rowID + ")' type='text' readonly='readonly'/><input id='equipmentId_"+ rowID + "' name='equipmentId' type='hidden' /><input id='equipmentModel_"+ rowID + "' name='equipmentModel' type='hidden' /><input id='equipmentName_"+ rowID + "' name='equipmentName' type='hidden' />";	
				
				var newNameTD=newTR.insertCell(2);
					
				newNameTD.innerHTML = "<a href='javaScript:void(0);' onclick=\"deleteSignRow(" + rowID + ")\">删除</a>";	
						
				txtTRLastIndex.value = (rowID + 1).toString() ;
			}
			//删除指定行
			function deleteSignRow(rowid){
				var signFrame = document.getElementById("SignFrame");
				
				var signItemId = "signItem" + rowid;
				var signItem = document.getElementById(signItemId);
				
				//获取将要删除的行的Index
				var rowIndex = signItem.rowIndex;
				
				//删除指定Index的行
				signFrame.deleteRow(rowIndex);
				
				//重新排列序号，如果没有序号，这一步省略
				//for(i=rowIndex;i<signFrame.rows.length;i++){
				//  signFrame.rows[i].cells[0].innerHTML = i.toString();
				//}
			}
			
		function goBack() {
		  window.location.href = "${pageContext.request.contextPath}/api/mywork/getMyWork";
		}
		
		function checkForm(){
			var deptNames = document.getElementsByName("deptName");
			var equipmentDetails = document.getElementsByName("equipmentDetail");
			var flag = true;
			for(var i=0;i<deptNames.length;i++){
				if(deptNames[i].value==""){
					deptNames[i].style["borderColor"]="red";
					flag=false;
				}else{
					deptNames[i].style["borderColor"]="";
				}
			}
			for(var i=0;i<equipmentDetails.length;i++){
				equipmentDetails[i].style["borderColor"]="";
				if(equipmentDetails[i].value==""){
					equipmentDetails[i].style["borderColor"]="red";
					flag=false;
				}else{
					equipmentDetails[i].style["borderColor"]="";
				}
			}
			return flag;
		}
		function init(){
			var orgCode = "${userMap.orgId}";
			var myDate = new Date();
			if(orgCode.indexOf("|YC|")>0){
				document.getElementById("workCard").value = myDate.getFullYear()+"YC";
			}else{
				document.getElementById("workCard").value = myDate.getFullYear()+"QY";
			}
		}
		</script>
	</head>
	<body onload="init();">
		<div class="container-fluid">
			<div class="row-fluid">
		    	<div class="span9">
			        <div class="page-header">
			        	<h3>上井调派</h3>
			        </div>
			      <form name="form" id="form" class="form-horizontal" novalidate="">
					<input type="hidden" name="url" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
			        <div class="page-header">
		        	<h4>基本信息</h4>
			        <div class="row-fluid">
			        	<div class="span5">
					        <div class="control-group">
					          <label class="control-label" for="taskCode">任务编号</label>
					          <div class="controls">
					            <input id="taskCode"  name="taskCode" class="input-medium" type="text" maxlength="20" required />
					          </div>  
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="vehiclesUnit">用车单位</label>
					          <div class="controls">
					            <select id="vehiclesUnit" name="vehiclesUnit" style="width: 110px;">
					          		<option value="">请选择</option>
									<c:forEach var="lev" items='${web:getDataItem("car_unit")}'>
										<option value="${lev.dataItemValue }">
											${lev.dataItemName }
										</option>
									</c:forEach>
								</select>
					          </div>  
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="vehiclesPlate">车牌</label>
					          <div class="controls">
					            <input id="vehiclesPlate"  name="vehiclesPlate" class="input-medium" type="text" maxlength="20" required />
					          </div>  
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="departureTime">出发时间</label>
					          <div class="controls">
					            	<input id="departureTime" name="departureTime" class="input-medium" type="text"  value=""
		            							onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
					          </div>  
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="workCard">工卡号</label>
					          <div class="controls">
					            <input id="workCard"  name="workCard" class="input-medium" type="text" maxlength="20" required />
					          </div>  
					        </div>
					      </div>
					      <div class="span6">
					        <div class="control-group">
					          <label class="control-label" for="projectDept">井队</label>
					          <div class="controls">
					          	<select id="projectDept" name="projectDept" style="width: 110px;">
					          		<option value="">请选择</option>
								<c:forEach var="lev" items='${web:getDataItem("pro_dept")}'>
									<option value="${lev.dataItemValue }">
										${lev.dataItemName }
									</option>
								</c:forEach>
							</select>
					            <input id="expatsTo" name="expatsTo" class="input-small" type="text" maxlength="40" required />
					          </div>  
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="vehiclesModel">车型</label>
					          <div class="controls">
					          	<select id="vehiclesModel" name="vehiclesModel" style="width: 110px;">
					          		<option value="">请选择</option>
									<c:forEach var="lev" items='${web:getDataItem("truck_type")}'>
										<option value="${lev.dataItemValue }">
											${lev.dataItemName }
										</option>
									</c:forEach>
								</select>
					          </div>  
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="transUnit">承运单位</label>
					          <div class="controls">
					          	<select id="transUnit" name="transUnit" style="width: 110px;">
					          		<option value="">请选择</option>
									<c:forEach var="lev" items='${web:getDataItem("trans_unit")}'>
										<option value="${lev.dataItemValue }">
											${lev.dataItemName }
										</option>
									</c:forEach>
								</select>
					          </div>  
			        		</div>
			        		<div class="control-group">
					          <label class="control-label" for="repairType">修理性质</label>
					          <div class="controls">
					          	<select id="repairType" name="repairType" style="width: 110px;">
					          		<option value="">请选择</option>
									<c:forEach var="lev" items='${web:getDataItem("repair_type")}'>
										<option value="${lev.dataItemValue }">
											${lev.dataItemName }
										</option>
									</c:forEach>
								</select>
					          </div>  
					        </div>
				        </div>
		        	</div>
		        </div>
		        <div class="page-header">
		        	<h4>修理内容</h4>
		        			<div>    
			        			<ul class="inline">
							    <li><p class="muted">请输入1-500字的存在问题</p></li>
							    <li><p id="plable" class="text-success">已输入0字符</p></li>
							    </ul>	   
							</div>
				    <p><textarea rows="2" class="span10" name="taskDetail" id="taskDetail" onkeyup="textCounter(taskDetail,plable,100);"></textarea></p>
		        </div>
		         <div class="page-header">
		        	<h4>车间调派</h4>
		        	<div class="row-fluid">
		        		<input name='txtTRLastIndex' type='hidden' id='txtTRLastIndex' value="2" />
				      	<div style="margin:5px 0 5px 5px;"> <a class="btn btn-mini" href="javaScript:void(0);" onclick="addItemLine();"><i class="icon-plus"></i>增加车间</a></div>
		        		<table class="table table-striped table-bordered" id="SignFrame">
		                    	<tr>
									<th>施工车间</th>
									<th>修理设备描述</th>
									<th>操作</th>
		                        </tr>
		                    	<tr>
					              <td>
					                  <div class="input-append">
							              <input id="deptName_0" name="deptName" class="input-medium" type="text" onclick="openDept(0);"  readonly="readonly"/>
							              <input id="deptId_0" name="deptId" type="hidden" />
						              </div>
					              </td>
					              <td>
					              		<input id="equipmentDetail_0" name="equipmentDetail" class="input-medium" onclick="openEquis(0)" type="text" readonly="readonly"/>
					              		<input id="equipmentId_0" name="equipmentId" type="hidden" />
					              		<input id="equipmentModel_0" name="equipmentModel" type="hidden" />
					              		<input id="equipmentName_0" name="equipmentName" type="hidden" />
					              </td>
					              <td></td>
					            </tr>
		        		</table>
		        	</div>
		        	<div id="errorMessage" style="display: none;"><font color="red">红色标识：必填项；</font></div>
		        </div>
			    </form>
        		<div class="form-actions">
					<a class="btn" href="#" onclick="goBack();">取 消</a>
					<a class="btn btn-primary" href="javaScript:void(0);" onclick="submit();">确 定</a>
				</div>
			    </div>
			</div>
		</div>
		
		<script type="text/javascript">
		
			// 函数，3个参数，表单名字，表单域元素名，限制字符；
			   function textCounter(field, countfield, maxlimit) {
					var s=field.value.length;
					if(s>maxlimit){
						field.value=field.value.substr(0,maxlimit)
					}else{
						countfield.innerHTML="已输入" + s + "个字符";
					} 
			
			   }
			   
		</script>
		
	</body>
</html>
