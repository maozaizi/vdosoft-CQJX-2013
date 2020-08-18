<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>物料出库</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/RegExp.js" type="text/javascript"></script>
<script type="text/javascript">
		function submit(){
			if(checkForm()){
				document.getElementById("errorMessage").style="display:none;";
				document.form.action="${pageContext.request.contextPath}/api/bustoolswf/saveOutStore";
				document.form.submit();
			}else{
				document.getElementById("errorMessage").style="display:block;";
			}
		}
		function save(){
			document.form.action="${pageContext.request.contextPath}/api/bustoolswf/updateRatifyNum";
			document.form.submit();
		}
		function openMaterial(useType){
			document.getElementById("useType").value=useType;	
			window.open("${pageContext.request.contextPath}/api/bustoolswf/toOutStoreMaterial","","width=1250,height=700,scrollbars=yes");
		}
		//删除行
		function delRow(rowId,useType){
			var selectTable=document.getElementById("outStoreTable"+useType);
			var signItem = document.getElementById( "materialPlanTr_"+rowId);
			//获取将要删除的行的Index
			var rowIndex = signItem.rowIndex;
			selectTable.deleteRow(rowIndex);
			//重新绘制ID
			repaintNumber(useType);
			//材料计划列表，删除所有选择信息，显示暂无数据
			if(selectTable.rows.length==1){
				var newTR = selectTable.insertRow(1);
				var mark = newTR.insertCell(0);
				mark.colSpan=10;
				mark.style="text-align:center;color:red;";
				mark.innerHTML="暂无数据！";
			}
		}
			
		//重新绘制ID
		function repaintNumber(useType){
			var ptab=document.getElementById("outStoreTable"+useType);
			for (var i = 1; i<ptab.rows.length; i++) {
				ptab.rows[i].id="materialPlanTr_"+i;
				ptab.rows[i].cells[6].innerHTML = '<a href="javascript:void(0)" onclick="delRow('+i+','+useType+')">删除</a>';
			}
		}
		function goBack() {
			  window.location.href = "${pageContext.request.contextPath}/api/mywork/getMyWork";
		}	
		function checkForm(){
			var storeNums = document.getElementsByName("storeNum");
			var realNums = document.getElementsByName("ratifyNum");
			var flag = true;
			var re =/^\d{1,12}(?:\.\d{1,2})?$/;
			for(var i=0;i<storeNums.length;i++){
				if(realNums!=null&&realNums[i]!=null){ //材料计划，有实际数量 不为空
					if(storeNums[i].value==""||!re.test(storeNums[i].value)||parseFloat(storeNums[i].value)>parseFloat(realNums[i].value)){
						//出库数量必须为数字，且小于计划数量
						storeNums[i].style["borderColor"]="red";
						flag=false;
					}else{
						storeNums[i].style["borderColor"]="";
					}
				}else{
						//手动出库，只做非空和数字校验
					if(storeNums[i].value==""||!re.test(storeNums[i].value)){
						storeNums[i].style["borderColor"]="red";
						flag=false;
					}else{
						storeNums[i].style["borderColor"]="";
					}
				}
			}
			return flag;
		}
</script>			
			
	
</head>

<body>
	<input type="hidden" name="useType" id="useType" value=""/>
	<form name="form" method="post">
	<input type="hidden" name="busId" value="${busToolswfMap.id }"/>
	<input type="hidden" name="busName" value="com.BusToolswf"/>
	<input type="hidden" name="workCard" value="${busToolswfMap.workCard}"/>
	<input type="hidden" name="instanceId" value="${instanceId }"/>
	<input type="hidden" name="nodeId" value="${nodeId }"/>
	<input type="hidden" name="stepId" value="${stepId }"/>
	<input type="hidden" name="url" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
	<div class="container-fluid" id="divId">
 		<div class="row-fluid">
   			<div class="span9">
   				<!-- 标题 start -->
     			<div class="page-header">
		        	<h4>基本信息</h4>
		            <div class="row-fluid">
		            	<div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>工卡号：</dt>
		                        <dd><a href="${pageContext.request.contextPath}/api/bustoolswf/toBustoolswfDetail?id=${busToolswfMap.Id}" target="_blank">${busToolswfMap.workCard}</a>
		                        </dd>
		                    </dl>
		                </div>
		                
		                <div class="span5">
		                	<dl class="dl-horizontal">
		                        <dt>设备名称：</dt>
		                        <dd>${busToolswfMap.equipmentName}</dd>
		                    </dl>
		                </div>
		                
		            </div> 
		        </div>
		        <!-- end -->
		        <div class="page-header">
					<h4>领用材料列表</h4>
					<div class="table">
            			<div style="margin:5px 0 5px 5px;"></div>
						<table id="materialPlanTable" class="table table-striped table-bordered">
						  <tbody>
							<tr>
							  <th>序号</th>
							  <th>物料描述</th>
				              <th>物料组</th>
				              <th>物料编码</th>
							  <th>单位</th>
							  <th>领用审批数量</th>
							  <th>实际领用数量</th>
							</tr>
							<c:if test="${empty materialList}">
				           		<tr>
				                    <td colspan="7" style="text-align: center;"><font color='red'>暂无数据！</font></td>
				                 </tr>   
				           	</c:if>
				           	<c:if test="${not empty materialList}">
				            	<c:forEach items="${materialList}" var="material" varStatus="i">
				            		<tr>
				            			<td>${i.index+1 }</td>
				            			<td>
				            				${material.materialDetail }
				            				<input type="hidden" name="id" value="${material.id }"/>
				            				<input type="hidden" name="useType" value="1"/>
				            				<input type="hidden" name="materialDetail" value="${material.materialDetail }"/>
				            			</td>
				            			<td>
				            				${material.materialGroup }
				            				<input type="hidden" name="materialGroup" value="${material.materialGroup }"/>
				            			</td>
				            			<td>
				            				${material.materialCode }
				            				<input type="hidden" name="materialCode" value="${material.materialCode }"/>
				            			</td>
				            			<td>
				            				${material.unity }
				            				<input type="hidden" name="unity" value="${material.unity }"/>
				            			</td>
				            			<td>
				            				${material.ratifyNum }
				            				<input type="hidden" name="ratifyNum" value="${material.ratifyNum }"/>
				            			</td>
				            			<td>
				            				<c:if test="${not empty material.realNum }">
				            					<input name="storeNum" class="input-mini" type="text" check="1" value="${material.realNum }"/>
				            				</c:if>
				            				<c:if test="${empty material.realNum }">
				            					<input name="storeNum" class="input-mini" type="text" check="1" value="${material.ratifyNum }"/>
				            				</c:if>
				            			</td>
				            		</tr>
				            	</c:forEach>
				            </c:if>
						  </tbody>
						</table>
					</div>
				 </div>
				 
		 		<div id="errorMessage" style="display: none;"><font color="red">红色标识：请填写数字，并保证领用数量小于等于审批数量；</font></div>	
				<div class="form-actions">
				  	<a class="btn" href="javascript:void(0);" onclick="goBack();">取消</a> 
				  	<a class="btn btn-primary" href="javascript:void(0);" onclick="save();">保存</a>
				  	<a class="btn btn-primary" href="javascript:void(0);" onclick="submit();">提交出库</a>
				</div>	
	        </div>
	     </div>
     </div>
</form>
</body>
</html>
