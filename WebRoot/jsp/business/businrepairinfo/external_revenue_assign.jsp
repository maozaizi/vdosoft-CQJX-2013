<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>维修调派</title>
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
		$(function () { $("input,select,textarea").not("[type=submit]").jqBootstrapValidation(); 
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
				var firstIds = document.getElementsByName("firstId");
				for(var i =0 ;i<firstIds.length;i++){
					if(firstIds[i].value.length!=4){
						alert("施工卡号必须为10位！");
						return ;
					}
				}
				document.myform.action="${pageContext.request.contextPath}/api/businrepairinfo/addExternalRevenue";
				$('#myform').submit();
			}
			function openDept(rowId){
				var selectId = document.getElementById("deptId_"+rowId).value;
				var deptIdString =",";
				//var deptIds = document.getElementsByName("deptId");
				//for(var i=0;i<deptIds.length;i++){
				//	if(deptIds[i].value!=null&&deptIds[i].value!=""){
				//		deptIdString += "'"+deptIds[i].value+"',";
				//	}
				//}
				window.open("${pageContext.request.contextPath}/api/businrepairinfo/toChooseDept?tmp=" + Math.round(Math.random() * 10000)+"&rowId="+rowId+"&deptIds="+deptIdString,"newwindow", "height=500, width=400, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
			}
			function openeq(){
				window.open("${pageContext.request.contextPath}/api/businrepairinfo/chooseEquipmentInfo","","width=700,height=800,scrollbars=yes");
			}
			function openEquis(rowId){
				window.open("${pageContext.request.contextPath}/api/businrepairinfo/toChooseEquips?tmp=" + Math.round(Math.random() * 10000)+"&rowId="+rowId,"newwindow", "height=700, width=800, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no");
			}
			function inputDomain(domain){
				var value = domain.value;
				document.getElementById("domainNo0").value = document.getElementById("code0").value+value;
				//判断是否有动态添加行数据有的话替换值
				var txtTRLastIndex = document.getElementById("txtTRLastIndex").value;
				if(txtTRLastIndex>2){
					for(var i=2;i<txtTRLastIndex;i++){
						document.getElementById("domainNo"+i).value = document.getElementById("code0").value+value;
					}
				}
			}
			function addItemLine(){
				var value = document.getElementById("domainNo0").value;
				if(value==""||value==null){
					alert("请先填写主卡信息！");
					return;
				}
				if(value.length!=10){
					alert("施工卡号必须为10位！");
					return;
				}
				//读取最后一行的行号，存放在txtTRLastIndex文本框中
				var txtTRLastIndex = document.getElementById("txtTRLastIndex");
				var signFrame = document.getElementById("SignFrame");
				if(signFrame.rows.length>=3){	
					return;
				}
				var rowID = parseInt(txtTRLastIndex.value);
				
				//添加行
				var newTR = signFrame.insertRow(signFrame.rows.length);
				newTR.id = "signItem" + rowID;
				var newNameTD=newTR.insertCell(0);
				
				newNameTD.innerHTML = "<div class='input-append'><input id='deptName_" + rowID + "' name='deptName' class='input-medium' type='text' onclick='openDept(" + rowID + ");' readonly='readonly' check='1'/><input id='deptId_" + rowID + "' class='input-medium' name='deptId' type='hidden' /></div>";
				
				var newNameTD=newTR.insertCell(1);
					
				newNameTD.innerHTML = "<input type='text' name='code' id='code"+rowID+"'  style='border-right:0; width:50px;'   Readonly='true' /><input name='firstId' id='firstId" + rowID + "' type='text' class='input-medium' check='1' maxlength='4'/> / <input id='domainNo" + rowID + "' name='domainNo' value='" + value + "' class='input-medium' type='text' readonly='readonly'/>";
				
				
				var newNameTD=newTR.insertCell(2);
				newNameTD.innerHTML = "<input id='equipmentDetail_" + rowID + "' name='equipmentDetails' class='input-medium' type='text' readonly='readonly' onclick='openEquis(" + rowID + ");' check='1'/><input id='equipmentId_"+rowID+"' name='equipmentIds' type='hidden' /><input id='equipmentModel_"+rowID+"'name='equipmentModels' type='hidden' /><input id='equipmentName_"+rowID+"' name='equipmentNames' type='hidden' />";
					              		
				var newNameTD=newTR.insertCell(3);
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
		    
		    function changeHours(){
			var hours = document.getElementById("repairType").value;
			if(hours==1){
				document.getElementById("quotaHour").value = document.getElementById("quotaHours").value;
			}else if(hours==2){
				document.getElementById("quotaHour").value = document.getElementById("mediumHour").value;
			}else{
				document.getElementById("quotaHour").value = document.getElementById("checkHour").value;
			}
		}
		</script>
	</head>
	<body>
		<div class="container-fluid">
			<div class="row-fluid">
		    	<div class="span9">
			        <div class="page-header">
			        	<h3>维修调派</h3>
			        </div>
			        <form name="myform" action="" id="myform" class="form-horizontal" novalidate="">
			        <div class="page-header">
		        	<h4>设备信息</h4>
		        	<div class="row-fluid">
		            	<div class="span5">
		            		<div class="control-group">
					          <label class="control-label" for="equipmentName">设备名称</label>
					          	<input id="equipmentId" name="equipmentId" type="hidden" value=""/>
					          	<input id="docName" name="docName" type="hidden" value=""/>
					          	<input id="standardNum" name="standardNum" type="hidden" value=""/>
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
					            	<input id="equipmentName" name="equipmentName" class="input-large" type="text" value="" readonly="readonly"/>
					        	</div>
					        </div>
					        <div class="control-group">
					          <label class="control-label" for="quotaHour">额定工时</label>
					          <div class="controls">
					          	<input id="quotaHour" name="quotaHour" class="input-mini" type="text" value="" required  data-validation-required-message="请选择设备！" aria-invalid="true"/>
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
		            	</div>
		            	 <div class="span2"> <a class="btn" href="#" onclick="openeq();">选择设备</a></div>
	            	</div>
		        </div>
		        <!-- 进修单信息start -->
		        <div class="page-header">
		        	<h4>进修单信息</h4>
		        	<div class="row-fluid">
		        	<div class="span5">
				        <div class="control-group">
				          <label class="control-label" for="bodyCode">机身号</label>
				          <div class="controls">
				            <input id="bodyCode"  name="bodyCode" class="input-small" type="text" maxlength="20" required />
				          </div>  
				        </div>
				        <div class="control-group">
				          <label class="control-label" for="selfCode">自编号</label>
				          <div class="controls">
				            <input id="selfCode" name="selfCode" class="input-small" value="0000" type="text" maxlength="20" required/>
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
				           </div>  
				        </div>
				        <div class="control-group">
				          <label class="control-label" for="inDate">进厂时间</label>
				          <div class="controls">
				            <input id="idDate" name="inDate" class="input-small" type="text" required
				            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
				          </div>  	
				        </div>
				        <div class="control-group">
				          <label class="control-label" for="workHour">运转时间</label>
				          <div class="controls">
				          	<input id="workHour" name="workHour" class="input-small" type="text" required/>
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
		        	</div>
	        	</div>
	        	
	        			         <!-- 送修单位意见start -->
		        <div>
		        	<h4>存在问题</h4>
		        		    <div>    
				        			<ul class="inline">
								    <li><p class="muted">请输入1-100字的存在问题</p></li>
								    <li><p id="plable" class="text-success">已输入0字符</p></li>
								    </ul>	   
							</div>
				    <p><textarea rows="2" class="span10" name="problem" id="problem" onkeyup="textCounter(problem,plable,100);"></textarea></p>
		        </div>
		        <!-- 送修单位意见end -->
		        <!-- 送修单位意见start -->
		        <div>
		        	<h4>送修单位意见</h4>
		        			<div>    
				        			<ul class="inline">
								    <li><p class="muted">请输入1-100字的存在问题</p></li>
								    <li><p id="tishi" class="text-success">已输入0字符</p></li>
								    </ul>	   
							</div>
				    <p><textarea rows="2" class="span10" id="opinionDeptFrom" name="opinionDeptFrom" onkeyup="textCounter(opinionDeptFrom,tishi,100);"></textarea></p>
		        </div>
		        <!-- 送修单位意见end -->
		         <!-- 完整性及缺件start -->
		        <div>
		        	<h4>完整性及缺件</h4>
		        			<div>    
				        			<ul class="inline">
								    <li><p class="muted">请输入1-100字的存在问题</p></li>
								    <li><p id="wslable" class="text-success">已输入0字符</p></li>
								    </ul>	   
							</div>
		            <p><textarea rows="2" class="span10" name="wholeSituation" id="wholeSituation" onkeyup="textCounter(wholeSituation,wslable,100);"></textarea></p>
		        </div>
		        </div>
		        <!-- 进修单信息end -->

		        <!-- 上传送修单图片start -->
		       <%@include file="/jsp/public/uploadify.jsp" %>
		        <!-- 上传送修单图片end -->
		        <input id="inrepairId" name="inrepairId" type="hidden" value="${busInrepairInfoMap.id}"/>
				<input type="hidden" name="url" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
		        <div class="page-header">
		        	<h4>计划完工时间</h4>
		        	<div class="row-fluid">
		        		<div class="control-group">
							<input id="planEndTime" name="planEndTime" class="input-medium" type="text" 
		            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
		           		</div>
		        	</div>
		        </div>
		         <div class="page-header">
		        	<h4>车间调派</h4>
		        	<div class="row-fluid" id="divId">
		        		<input name='txtTRLastIndex' type='hidden' id='txtTRLastIndex' value="2" />
					      	<div style="margin:5px 0 5px 5px;"> <a class="btn btn-mini" href="javaScript:void(0);" onclick="addItemLine();"><i class="icon-plus"></i>增加施工卡</a></div>
		        		<table class="table table-striped table-bordered" id="SignFrame">
		                    	<tr>
									<th>施工车间</th>
		                        	<th>施工卡号</th>
									<th>修理设备描述</th>
									<th>操作</th>
		                        </tr>
		                    	<tr>
					              <td>
					                  <div class="input-append">
							              <input id="deptName_0" name="deptName" class="input-medium"  onclick="openDept(0);" type="text" readonly="readonly"/>
							              <input id="deptId_0" name="deptId" class="input-medium" type="hidden" /> 
						              </div>
					              </td>
					              <td>
					              	<input type="text" name="code" id="code0"  style="border-right:0; width:50px;"  readonly="true" />
					              	<input id="firstId0" name="firstId" onkeyup="inputDomain(this);" onblur="inputDomain(this);" class="input-medium" type="text" maxlength="4"/> / <input id="domainNo0"  name="domainNo" class="input-medium" type="text" readonly="readonly" required data-validation-required-message="必填项"/></td>
					              <td>
					              		<input id="equipmentDetail_0" name="equipmentDetails" value="" class="input-medium" type="text" readonly="readonly"/>
					              		<input id="equipmentId_0" name="equipmentIds" type="hidden" value=""/>
					              </td>
					              <td></td>
					            </tr>
		        		</table>
		        	</div>
		        	<!-- 备注 start -->
					<div>
						<h4>填写备注信息</h4>
							<div>    
				        			<ul class="inline">
								    <li><p class="muted">请输入1-500字的存在问题</p></li>
								    <li><p id="oclable" class="text-success">已输入<span id="oclable">0</span>字符</p></li>
								    </ul>	   
							</div>
	 					<p><textarea onkeyup="textCounter(distributeRemark,oclable,500);" check="1" rows="2" class="span12" name="distributeRemark" id="distributeRemark"></textarea></p>
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
