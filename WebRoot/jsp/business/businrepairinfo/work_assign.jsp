<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>维修调派</title>
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
				
				document.getElementById("domainNo0").value = document.getElementById("code0").value+value;
				//判断是否有动态添加行数据有的话替换值
				var signFrame = document.getElementById("SignFrame");
				//var txtTRLastIndex = document.getElementById("txtTRLastIndex").value;
				txtTRLastIndex = signFrame.rows.length;
				if(txtTRLastIndex>2){
					for(var i=2;i<txtTRLastIndex;i++){
						document.getElementById("domainNo"+i).value = document.getElementById("code0").value+value;
					}
				}
			}
			function submit(){
				if(checkForm()){
					var str1 = "${busInrepairInfoMap.inDate}";  
					var arr1 = str1.substring(0,10).split("-");  
					var date1 = new Date(arr1[0],parseInt(arr1[1])-1,arr1[2]);  
					  
					var str2 = document.getElementById("planEndTime").value;  
					var arr2 = str2.split("-");  
					var date2 = new Date(arr2[0],parseInt(arr2[1])-1,arr2[2]);  
					if(date1>date2){
						alert("计划完工时间不可小于进厂时间（"+str1.substring(0,10)+"）！");
						return false;
					}
					var firstIds = document.getElementsByName("firstId");
					for(var i =0 ;i<firstIds.length;i++){
						if(firstIds[i].value.length!=4){
							alert("施工卡号必须为10位！");
							return ;
						}
					}
					document.form.action="${pageContext.request.contextPath}/api/businrepairinfo/saveWorkAssign";
					$('#form').submit();
				}else{
					document.getElementById("errorMessage").style="display:block;";
				}
			}
			function backCheck(){
				document.form.action="${pageContext.request.contextPath}/api/businrepairinfo/updateBusInrepairInfo";
				$('#form').submit();
			}
			function addItemLine(){
				var value = document.getElementById("domainNo0").value;
				if(value==""||value==null){
					alert("请先填写主卡信息!");
					return;
				}
				//读取最后一行的行号，存放在txtTRLastIndex文本框中
				var txtTRLastIndex = document.getElementById("txtTRLastIndex");
				var rowID = parseInt(txtTRLastIndex.value);
				var signFrame = document.getElementById("SignFrame");
				if(signFrame.rows.length>=3){	
					return;
				}
				
				//添加行
				var newTR = signFrame.insertRow(signFrame.rows.length);
				newTR.id = "signItem" + rowID;
				
				var newNameTD=newTR.insertCell(0);
				
				newNameTD.innerHTML = "<div class='input-append'><input id='deptName_" + rowID + "' name='deptName' class='input-medium' type='text' readonly='readonly' check='1' onclick='openDept(" + rowID + ");' /><input id='deptId_" + rowID + "' class='input-medium' name='deptId' type='hidden' /> ";
				
				var newNameTD=newTR.insertCell(1);
					
				newNameTD.innerHTML = "<input type='text' name='code' id='code"+rowID+"'  style='border-right:0; width:50px;'   Readonly='true' /><input name='firstId' id='firstId" + rowID + "' type='text' class='input-medium' check='1' maxlength='4'/> / <input id='domainNo" + rowID + "' name='domainNo' value='" + value + "' class='input-medium' type='text' readonly='readonly'/>";
				
				var newNameTD=newTR.insertCell(2);
				
				newNameTD.innerHTML = "<input id='equipmentDetail_" + rowID + "' name='equipmentDetail' class='input-medium' type='text' readonly='readonly' onclick='openEquis(" + rowID + ");' check='1'/><input id='equipmentId_"+rowID+"' name='equipmentIds' type='hidden' /><input id='equipmentName_"+rowID+"' name='equipmentNames' type='hidden' />";
					              		
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
		function downLoad(file){
			window.open("${pageContext.request.contextPath}/api/fileUpload/downLoad?file="+file);
		}
		function checkForm(){
			var deptNames = document.getElementsByName("deptName");
			var firstIds = document.getElementsByName("firstId");
			var flag = true;
			for(var i=0;i<deptNames.length;i++){
				if(deptNames[i].value==""){
					deptNames[i].style["borderColor"]="red";
					flag=false;
				}else{
					deptNames[i].style["borderColor"]="";
				}
			}
			for(var i=0;i<firstIds.length;i++){
				firstIds[i].style["borderColor"]="";
				if(firstIds[i].value==""){
					firstIds[i].style["borderColor"]="red";
					flag=false;
				}else{
					for(var j=0;j<firstIds.length;j++){
						if(firstIds[i].value==firstIds[j].value&&i!=j){
							firstIds[i].style["borderColor"]="#C09853";
							flag=false;
						}
					}
					
				}
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
			        	<h3>维修调派</h3>
			        </div>
			        <div class="page-header">
		        	<h4>基本信息</h4>
		            <div class="row-fluid">
		            	<div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>设备名称：</dt>
		                        <dd>${busInrepairInfoMap.equipmentName}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>产值:</dt>
		                        <dd>${busInrepairInfoMap.cost}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
							<dt>单位：</dt>
							<dd>${busInrepairInfoMap.unity }</dd>
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
		                        <dt>自编号：</dt>
		                        <dd>${busInrepairInfoMap.selfCode}</dd>
		                    </dl>
							<dl class="dl-horizontal">
		                        <dt>送修单位：</dt>
		                        <dd>${web:getDeptBycode(busInrepairInfoMap.deptFrom)}井队</dd>
		                    </dl>
		                     
		                </div>
		                
		                <div class="span5">
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
								<dt>
									原值（元）：
								</dt>
								<dd>
									${busInrepairInfoMap.equipmentValue}
								</dd>
							</dl>
							 <dl class="dl-horizontal">
		                        <dt>人员定额：</dt>
		                        <dd>${busInrepairInfoMap.quotaHour}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>机身编号：</dt>
		                        <dd>${busInrepairInfoMap.bodyCode}</dd>
		                    </dl>
		                </div>
		                <div class="row-fluid">
		                <div class="span9">
		                <dl class="dl-horizontal">
		                        <dt>存在问题：</dt>
		                        <dd>${busInrepairInfoMap.problem}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
								<dt>
									送修单位意见：
								</dt>
								<dd>
									${busInrepairInfoMap.opinionDeptFrom }
								</dd>
							</dl>
							<dl class="dl-horizontal">
								<dt>
									完整性及缺件情况：
								</dt>
								<dd>
									${busInrepairInfoMap.wholeSituation}
								</dd>
							</dl>
		                </div>
		                </div>
		                
		            </div> 
		        </div>
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
		        <form name="form" id="form">
		        <input id="inrepairId" name="inrepairId" type="hidden" value="${busInrepairInfoMap.id}"/>
		        <input id="id" name="id" type="hidden" value="${busInrepairInfoMap.id}"/>
				<input type="hidden" name="url" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
		        <div class="page-header">
		        	<h4>计划完工时间</h4>
		        	<div class="row-fluid">
		        		<div class="control-group">
							<input id="planEndTime" name="planEndTime" class="input-medium" type="text" 
		            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" minDate="${busInrepairInfoMap.inDate}" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
		           		</div>
		        	</div>
		        </div>
		         <div class="page-header">
		        	<h4>车间调派</h4>
		        	<div class="row-fluid" id="divId">
				      	<!-- <div style="margin:5px 0 5px 5px;"> <a class="btn btn-mini" href="javaScript:void(0);" onclick="addItemLine();"><i class="icon-plus"></i>增加施工卡</a></div> -->
		        		<table class="table table-striped table-bordered" id="SignFrame">
		                    	<tr>
									<th>施工车间</th>
		                        	<th>施工卡号</th>
									<th>修理设备描述</th>
		                        </tr>
		                        <c:if test="${empty equipmentList}">
		                        <input name='txtTRLastIndex' type='hidden' id='txtTRLastIndex' value="1" />
		                        	<tr>
						              <td>
						                  <div class="input-append">
								              <input id="deptName_0" name="deptName" class="input-medium"  onclick="openDept(0);" type="text" readonly="readonly"/>
								              <input id="deptId_0" name="deptId" class="input-medium" type="hidden" /> 
								              <input id="orgId0" name="orgId" class="input-medium" type="hidden" /> 
								              <input id="serialnum0" name="serialnum" class="input-medium" type="hidden" />
							              </div>
						              </td>
						              <td>
						              	<input type="text" name="code" id="code0"  class="input-medium" onchange="inputDomain(this);"  readonly="true" />
						              	/ <input id="domainNo0"  name="domainNo" class="input-medium" type="text" readonly="readonly" required data-validation-required-message="必填项"/></td>
						              <td>
						              		<input id="equipmentDetail_0" name="equipmentDetail" value="${busInrepairInfoMap.equipmentName}" class="input-medium" type="text" readonly="readonly"/>
						              		<input id="equipmentId_0" name="equipmentIds" type="hidden" value="${busInrepairInfoMap.equipmentId}"/>
						              </td>
						            </tr>
		                        </c:if>
		                        <c:if test="${not empty equipmentList}">
		                        	<input name='txtTRLastIndex' type='hidden' id='txtTRLastIndex' value="${fn:length(equipmentList)}" />
		                        	<c:forEach items="${equipmentList}" var="eqs" varStatus="i">
			                        	<tr>
							              <td>
							                  <div class="input-append">
									              <input id="deptName_${i.index}" name="deptName" class="input-medium"  onclick="openDept('${i.index}');" type="text" readonly="readonly"/>
									              <input id="deptId_${i.index}" name="deptId" class="input-medium" type="hidden" /> 
									              <input id="orgId${i.index}" name="orgId" class="input-medium" type="hidden" /> 
									              <input id="serialnum${i.index}" name="serialnum" class="input-medium" type="hidden" />
								              </div>
							              </td>
							              <td>
							              	<input type="text" name="code" id="code${i.index}"  class="input-medium" onchange="inputDomain(this);"  readonly="true" />
							              	/ <input id="domainNo${i.index}"  name="domainNo" class="input-medium" type="text" readonly="readonly" required data-validation-required-message="必填项"/></td>
							              <td>
							              		<input id="equipmentDetail_${i.index}" name="equipmentDetail" value="${eqs.equipmentName}" class="input-medium" type="text" readonly="readonly"/>
							              		<input id="equipmentId_${i.index}" name="equipmentIds" type="hidden" value="${eqs.id}"/>
							              </td>
							            </tr>
						            </c:forEach>
		                        </c:if>
		        		</table>
		        	</div>
		        	<div id="errorMessage" style="display: none;"><font color="red">红色标识：必填项；黄色标识：工卡号重复；</font></div>
		        </div>
		        <!-- 备注 start -->
				<div class="page-header">
					<h4>填写备注信息</h4>
					<p class="muted">请输入1-500字的备注信息</p>
 					<p><textarea onkeyup="textCounter(distributeRemark,oclable,500);" check="1" rows="5" class="span9" name="distributeRemark" id="distributeRemark"></textarea></p>
 					<p id="oclable" class="text-success">已输入<span id="oclable">0</span>字符</p>
				</div>
			    </form>
        		<div class="form-actions">
					<a class="btn" href="#" onclick="goBack();">取 消</a>
					<a class="btn btn-primary" href="javaScript:void(0);" onclick="backCheck();">返回厂检</a>
					<a class="btn btn-primary" href="javaScript:void(0);" onclick="submit();">提交</a>
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
