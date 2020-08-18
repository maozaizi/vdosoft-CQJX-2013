<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>机加派工单 </title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/RegExp.js"></script>
		<script type="text/javascript">
		function openeq(name,code,verify){
			window.open("${pageContext.request.contextPath}/api/busmechanic/toUserInfoChoose?receiveName="+name+"&receiveCode="+code+"&verify="+verify,"newwindow","width=400,height=400");
	   	}
		function verify(state){
			   if(document.getElementById(state).value==""){
			     document.getElementById(state).style["borderColor"]="red";
			     document.getElementById("Span"+state).innerHTML="<ul role='alert'><li>必填项</li></ul>";
			    }else{
			     document.getElementById(state).style=null;
			     document.getElementById("Span"+state).innerHTML=null;
			    }
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
		function modifyTest(){
			var str1 = "${busInrepairInfoMap.inDate}";  
			var arr1 = str1.substring(0,10).split("-");  
			var date1 = new Date(arr1[0],parseInt(arr1[1])-1,arr1[2]);  
			  
			var str2 = document.getElementById("appointDate").value;  
			var arr2 = str2.split("-");  
			var date2 = new Date(arr2[0],parseInt(arr2[1])-1,arr2[2]);  
			if(date1>date2){
				alert("车间派工日期不可小于进厂时间（"+str1.substring(0,10)+"）！");
				return false;
			}
			if(date2> new Date()){
				alert("车间派工日期不得大于当前日期!");
				return false;
			}
			//var outNum = document.getElementById("outNum").value;
			//var makeNum = document.getElementById("makeNum").value;
			
			//var outNum1 = document.getElementById("storeNum").value;
			//var makeNum = document.getElementById("planNum").value;
			
			
			///if(outNum>outNum1){
			//	alert("库存数量不足！")
			//	return false;
			//}
			//if(outNum>makeNum){
			//	alert("领用数量大于计划数量！")
			//	return false;
			//}
			document.myform.action="${pageContext.request.contextPath}/api/mechtreatment/saveBusWorkCard";
			document.myform.submit();
		}
		function downLoad(file){
		window.open("${pageContext.request.contextPath}/api/fileUpload/downLoad?file="+file);
		}
		 function goBack() {
			  window.location.href = "${pageContext.request.contextPath}/api/mywork/getMyWork";
		}	
		//手风琴收缩，图片切换
		function changeImg(){
			var src=document.getElementById('collapseImg').src;
			if(src.indexOf("up")>= 0 ){  
			    src=src.replace('up','down');
			}else{
				src=src.replace('down','up');
			}
			document.getElementById('collapseImg').src=src;
		}
		function addItemLine(rowID){
				var signFrame = document.getElementById("SignFrame");
				//添加行
				rowID = parseInt(rowID);
				var newTR = signFrame.insertRow(signFrame.rows.length);
				newTR.id = "signItem" + rowID;
				
				var newNameTD=newTR.insertCell(0);
				newNameTD.innerHTML = rowID+"<input name='ranking' type='hidden' value='"+rowID+"'/>";
				
				var newNameTD=newTR.insertCell(1);
				
				newNameTD.innerHTML = "<select id='workType_" + rowID + "' name='workType' style='width: 80px;'><c:forEach var='work' items='${web:getDataItem(\"work_type\")}'><option value='${work.dataItemValue}@${work.dataItemName }'>${work.dataItemName }</option></c:forEach></select>";
								
				var newNameTD=newTR.insertCell(2);
				
				newNameTD.innerHTML = "<input id='mainId_"+ rowID + "' name='userCode' type='hidden'/><input id='mainRepair_"+ rowID + "' name='userName' class='input-medium' type='text' readonly='readonly' onclick=\"openeq('mainRepair_"+ rowID + "','mainId_"+ rowID + "','1');\" />";
				
				var newNameTD=newTR.insertCell(3);
				
				newNameTD.innerHTML = "<input type='text' name='quotaHour' class='input-mini'/>";
					              		
				var newNameTD=newTR.insertCell(4);
				var rowIDs=(rowID + 1).toString()
				newNameTD.innerHTML = "<a href='javaScript:void(0);' onclick=\"addItemLine(" + rowIDs+ ")\">添加</a>||<a href='javaScript:void(0);' onclick=\"deleteSignRow(" + rowID + ")\">删除</a>";	
						
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
			
</script>
<style type="text/css">
	.table-center th,.table-center td {
		 text-align: center;
	}
</style>
	</head>
	<body>
	<div class="container-fluid">
		  <div class="row-fluid">
		    <div class="span10">
		    <form name="myform" action="" class="form-horizontal">
	             <div class="page-header">
	             	<h3>派工单</h3>
	             </div>
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
								<c:if test="${mechTreatmentMap.planEndTime!=null}">
									<tag:date  value="${mechTreatmentMap.planEndTime}" />
								</c:if>
								<c:if test="${mechTreatmentMap.planEndTime==null}">
									<input name="planEndTime" class="input-medium" type="text"  value=""
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" />
								</c:if>
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
		                        <c:if test="${machiningMap.problem!=null}">
		                        	${machiningMap.problem}
		                        </c:if>
		                        <c:if test="${machiningMap.problem==null}">
		                        	<textarea rows="2" class="span10" name="problem" id="problem" ></textarea>
		                        </c:if>
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
			
		        <div class="row-fluid">
    	 			  <fieldset>
		               <h4>施工卡</h4>
		                <c:forEach items="${busWorkCardList}" var="busWorkCard" varStatus="i">
		                 <div class="accordion-group">
				          <div class="accordion-heading"> <a href="#collapse${busWorkCard.id}" data-parent="#accordion2" data-toggle="collapse" class="accordion-toggle collapsed" style="text-decoration:none;color: black;" onclick="changeImg();" > 施工卡号:${busWorkCard.workCard}<img id="collapseImg" src="${pageContext.request.contextPath}/img/up.png" style="float: right;"/></a> </div>
				          <div class="accordion-body in collapse" id="collapse${busWorkCard.id}">
				            <div class="accordion-inner">
				            <div class="row-fluid">
							<div style="display:inline;" id="material${busWorkCard.id}">
					            <div class="span6">
						              <div class="control-group">
						                <label class="control-label" for="startDate">开工日期</label>
						                <div class="controls">
						                <input id="id" name="id" type="hidden" value="${busWorkCard.id}"/>
						               	<input id="startDate${busWorkCard.id}" onchange="verify('startDate${busWorkCard.id}');" name="startDate" class="input-medium" type="text"  value=""
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" /><div class='help-inline' id="SpanstartDate${busWorkCard.id}" style="color: red;"></div>
						              </div>
						              </div>
					            </div>
					            <div class="span6">
					              	 <div class="control-group">
							                <label class="control-label" for="appointDate">派工日期</label>
							                <div class="controls">
							              	<input id="appointDate" onchange="verify('appointDate${busWorkCard.id}');" name="appointDate" class="input-medium" type="text"  value=""
					            				onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" /><div class='help-inline' id="SpanappointDate${busWorkCard.id}" style="color: red;"></div>
							              </div>
							         </div>
				               </div>
				            </div>
			            	<table class="table table-center table-striped table-bordered" id="SignFrame">
			                	<thead>
			                	    <tr>
			                	        <th>序号</th>
			                            <th>工种</th>
			                            <th>修理人</th>
			                            <th>定额工时</th>
			                            <th>操作</th>
			                        </tr>
			                    </thead>
			                	<tbody>
			                    	<tr><td>1<input name='ranking' type='hidden' value='1'/></td>
			                            <td>
			                            <select id="workType_0" name="workType" style="width: 80px;">
			                            <c:forEach var="work" items='${web:getDataItem("work_type")}'>
												<option value="${work.dataItemValue}@${work.dataItemName }">
													${work.dataItemName}
												</option>
											</c:forEach>
										</select>
											</td>
											
										<td><input id="mainId_0" name="userCode" type="hidden" value=""/><input id="mainRepair_0"  name="userName" class="input-medium" type="text" readonly="readonly" onclick="openeq('mainRepair_0','mainId_0','1');" /></td>
			                            <td><input type="text" name="quotaHour" class="input-mini"/></td>
			                            <td><a href='javaScript:void(0);' onclick="addItemLine(2)">添加</a></td>
			                        </tr>
			                    </tbody>
			                </table>
				              </div>
				            </div>
				          </div>
				         </div>
				         </c:forEach>
		        	</fieldset>
		        </div>
		        <div class="page-header">
		            <input id="busId" name="busId" type="hidden" value="${mechTreatmentMap.id}"/>
				    <input type="hidden" name="stepId" id="stepId" value="${stepId}"/>
					<input type="hidden" name="instanceId" id="instanceId" value="${instanceId}"/>
				    <input type="hidden" name="nodeId" id="nodeId" value="${nodeId}"/>
					<input id="url" name="url" type="hidden" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
			        <h4>派工意见</h4>
		        					<div>    
				        			<ul class="inline">
								    <li><p class="muted">请输入1-500字的存在问题</p></li>
								    <li><p id="oclables" class="text-success">已输入<span id="oclables">0</span>字符</p></li>
								    </ul>	   
									</div>
		            <p><textarea onkeyup="textCounter(dispatchingRemark,oclables,500);" id="dispatchingRemark" name="dispatchingRemark" class="span12" rows="2"></textarea></p>
		            
			  	</div>
			  <div class="form-actions">
			  	<a onclick="goBack();" href="#" class="btn">取消</a> 
				<a class="btn btn-primary" href="#" onclick="modifyTest();">提 交</a>
			  </div>
		     </form>
			</div>
		</div>
	</div>
	
	</body>
</html>
