<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
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
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/RegExp.js"></script>
		<script type="text/javascript">
		function openeq(name,code,verify){
		window.open("${pageContext.request.contextPath}/api/busmechanic/toUserInfoChoose?receiveName="+name+"&receiveCode="+code+"&verify="+verify,"newwindow","width=400,height=400,scrollbars=yes");
	   	}
		function save(state){
		    var type=0;
		    var mainRepair=document.getElementById("mainRepair"+state);
		    var orderEndDate=document.getElementById("orderEndDate"+state);
		    var startDate=document.getElementById("startDate"+state);
		    var appointDate=document.getElementById("appointDate"+state);
		    var repairContent=document.getElementById("repairContent"+state);
		     	if(mainRepair.value==""){
			     mainRepair.style["borderColor"]="red";
			     document.getElementById("SpanmainRepair"+state).innerHTML="<ul role='alert'><li>必填项</li></ul>";
	  		     type=1;
			    }
			    if(orderEndDate.value==""){
			     orderEndDate.style["borderColor"]="red";
			     document.getElementById("SpanorderEndDate"+state).innerHTML="<ul role='alert'><li>必填项</li></ul>";
	  		     type=1;
			    }
			    if(startDate.value==""){
			     startDate.style["borderColor"]="red";
			     document.getElementById("SpanstartDate"+state).innerHTML="<ul role='alert'><li>必填项</li></ul>";
	  		     type=1;
			    }
			    if(appointDate.value==""){
			     appointDate.style["borderColor"]="red";
			     document.getElementById("SpanappointDate"+state).innerHTML="<ul role='alert'><li>必填项</li></ul>";
	  		     type=1;
			    }
			
		    if(type==0){
			    document.getElementById("mainRepairshow"+state).innerHTML=$("#mainRepair"+state).val();
			    document.getElementById("assistRepairshow"+state).innerHTML=$("#assistRepair"+state).val();
			    document.getElementById("repairContentshow"+state).innerHTML=$("#repairContent"+state).val();
			    document.getElementById("appointDateshow"+state).innerHTML=$("#appointDate"+state).val();
			    document.getElementById("startDateshow"+state).innerHTML=$("#startDate"+state).val();
			    document.getElementById("orderEndDateshow"+state).innerHTML=$("#orderEndDate"+state).val();
			    document.getElementById("material"+state).style["display"]="none";
			    document.getElementById("show"+state).style["display"]="inline";
		    }
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
		function update(state){
		    document.getElementById("material"+state).style["display"]="inline";
		    document.getElementById("show"+state).style["display"]="none";
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
			document.myform.action="${pageContext.request.contextPath}/api/buselectrical/saveBusWorkCard";
			document.myform.submit();
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
</script>
	</head>
	<body>
	<div class="container-fluid">
		  <div class="row-fluid">
		    <div class="span10">
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
		                        <dt>工卡号：</dt>
		                        <dd><a href="${pageContext.request.contextPath}/api/buselectrical/toBusElectricalDetailsPage?id=${busElectricalMap.Id}" target="_blank">${busElectricalMap.workCard}</a>
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>设备名称：</dt>
		                        <dd>${busElectricalMap.equipmentName}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理类别：</dt>
		                        <dd>
		                        <c:if test="${busElectricalMap.repairType==1}">大修</c:if>
		                        <c:if test="${busElectricalMap.repairType==2}">中修</c:if>
		                        <c:if test="${busElectricalMap.repairType==3}">检修</c:if>
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>额定工时：</dt>
		                        <dd>${busElectricalMap.quotaHour}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>机身编号：</dt>
		                        <dd>${busElectricalMap.bodyCode}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>送修单位：</dt>
		                        <dd>${web:getDeptBycode(busInrepairInfoMap.deptFrom)}井队</dd>
		                    </dl>
		                </div>
		                <div class="span5">
		                	<dl class="dl-horizontal">
		                        <dt>车间：</dt>
		                        <dd>${groupName}
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理类型：</dt>
		                        <dd>
		                        <c:if test="${busElectricalMap.repairCategory==1}">抢修</c:if>
		                        <c:if test="${busElectricalMap.repairCategory==2}">正常修理</c:if>
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理性质：</dt>
		                        <dd>
		                        <c:if test="${busElectricalMap.repairProperties==1}">返厂修理</c:if>
		                        <c:if test="${busElectricalMap.repairProperties==2}">正常修理</c:if>
		                        <c:if test="${busElectricalMap.repairProperties==3}">返工修理</c:if>
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>产值：</dt>
		                        <dd>${busElectricalMap.repairValue}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>自编号：</dt>
		                        <dd>${busElectricalMap.selfCode}</dd>
		                    </dl>
		                </div>
		             </div> 
		             <div class="row-fluid">
		                	<div class="span10">
		                	<dl class="dl-horizontal">
								<dt>
									存在问题：
								</dt>
								<dd>
									${busInrepairInfoMap.problem }	
								</dd>
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
									设备完整性及缺件情况：
								</dt>
								<dd>
									${busInrepairInfoMap.wholeSituation}
								</dd>
							</dl>
		                  </div>
		                </div>
		        </div>
		            
			<form name="myform" action="" class="form-horizontal">
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
				               <label class="control-label" for="mainRepair">主修人</label>
				               <input id="id" name="id" type="hidden" value="${busWorkCard.id}"/>
				               <input id="mainId${busWorkCard.id}" onchange="verify('${busWorkCard.id}');" name="mainId" type="hidden" value=""/>
				               <div class="controls">
              				   <input id="mainRepair${busWorkCard.id}"  name="mainRepair" class="input-medium" type="text" readonly="readonly" onclick="openeq('mainRepair${busWorkCard.id}','mainId${busWorkCard.id}','0');" /><div class='help-inline' id="SpanmainRepair${busWorkCard.id}" style="color: red;"></div>
				               </div>
				               </div>
				              <div class="control-group">
				                <label class="control-label" for="appointDate">派工日期</label>
				                <div class="controls">
				              	<input id="appointDate" onchange="verify('appointDate${busWorkCard.id}');" name="appointDate" class="input-medium" type="text"  value=""
		            				onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" /><div class='help-inline' id="SpanappointDate${busWorkCard.id}" style="color: red;"></div>
				              </div>
				               </div>
				               
				               <div class="control-group">
				                <label class="control-label" for="orderEndDate">指定完成日期</label>
				                <div class="controls">
				               <input id="orderEndDate${busWorkCard.id}" name="orderEndDate" class="input-medium" type="text"  value="<tag:date  value="${busElectricalMap.planEndTime}" />" readonly="true" /><div class='help-inline' id="SpanorderEndDate${busWorkCard.id}" style="color: red;"></div>
				             </div>
				               </div>
				            </div>
				            <div class="span6">
				              <div class="control-group">
				               <label class="control-label" for="assistRepair">辅修人</label>
		            		    <input id="assistId${busWorkCard.id}" name="assistId" type="hidden" value=""/>
		            		    <div class="controls">
		              			<input id="assistRepair${busWorkCard.id}"  name="assistRepair" class="input-medium" onclick="openeq('assistRepair${busWorkCard.id}','assistId${busWorkCard.id}','1');" type="text" readonly="readonly" />
				              </div>
				               </div>
				              <div class="control-group">
				                <label class="control-label" for="startDate">开工日期</label>
				                <div class="controls">
				               	<input id="startDate${busWorkCard.id}" onchange="verify('startDate${busWorkCard.id}');" name="startDate" class="input-medium" type="text"  value=""
				            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" /><div class='help-inline' id="SpanstartDate${busWorkCard.id}" style="color: red;"></div>
				              </div>
				              </div>
				            </div>
				            <div class="row-fluid">
				               <div class="span10">
	            				<div class="control-group">
				                  <label class="control-label" for="repairContent">车间修理意见</label>
					               <div class="controls">
					                <div>    
				        			<ul class="inline">
								    <li><p class="muted">请输入1-500字的存在问题</p></li>
								    <li><p id="oclable" class="text-success">已输入<span id="oclable">0</span>字符</p></li>
								    </ul>	   
									</div>
						            <p><textarea onkeyup="textCounter(repairContent,oclable,500);" onchange="verify('repairContent${busWorkCard.id}');" id="repairContent${busWorkCard.id}" name="repairContent" class="span12" rows="2"></textarea><div class='help-inline' id="SpanrepairContent${busWorkCard.id}" style="color: red;"></div></p>
					              </div>
				              </div>
        						</div>
        						</div>
        						<div class="row-fluid">
				            	<div class="span10">
	            				
        						</div>
        						</div>
				            </div>
				            
				            <div id="show${busWorkCard.id}" style="display:none;">
			            	<div class="span5">
			                    <dl class="dl-horizontal">
			                        <dt>主修人：</dt>
							          <dd>
							          	   <span id="mainRepairshow${busWorkCard.id}"></span>
							          </dd>
			                    </dl>
			                    <dl class="dl-horizontal">
						          <dt>派工日期：</dt>
						          <dd>
						          	   <span id="appointDateshow${busWorkCard.id}"></span>
						          </dd>
			       				</dl>
				       			<dl class="dl-horizontal">
								          <dt>指定完成日期：</dt>
								          <dd>
								          	   <span id="orderEndDateshow${busWorkCard.id}"></span>
								          </dd>
				       			</dl>
				       			<dl class="dl-horizontal">
								          <dt>车间修理意见：</dt>
								          <dd>
								          	   <span id="repairContentshow${busWorkCard.id}"></span>
								          </dd>
				       			</dl>
					          	</div>
					          	<div class="span5">
						          	<dl class="dl-horizontal">
									          <dt>辅修人：</dt>
									          <dd>
									          	   <span id="assistRepairshow${busWorkCard.id}"></span>
									          </dd>
					       			</dl>
					       			<dl class="dl-horizontal">
									          <dt>开工日期：</dt>
									          <dd>
									          	   <span id="startDateshow${busWorkCard.id}"></span>
									          </dd>
					       			</dl>
					        	</div>
					        	<div class="row-fluid">
					        	<div class="span10">
						            <span style="float:right;"><a id="Edu_show${busWorkCard.id}" style="display: inline;" class="btn" onclick="update('${busWorkCard.id}');">修改</a></span>
					        	</div>
					        	</div>
			                </div>
				             </div>
				              </div>
				            </div>
				          </div>
	               		</c:forEach>
		        	</fieldset>
		        </div>
		        <div class="page-header">
		            <input id="busId" name="busId" type="hidden" value="${busElectricalMap.id}"/>
				    <input type="hidden" name="stepId" id="stepId" value="${stepId}"/>
					<input type="hidden" name="instanceId" id="instanceId" value="${instanceId}"/>
				    <input type="hidden" name="nodeId" id="nodeId" value="${nodeId}"/>
					<input id="url" name="url" type="hidden" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
			        
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
