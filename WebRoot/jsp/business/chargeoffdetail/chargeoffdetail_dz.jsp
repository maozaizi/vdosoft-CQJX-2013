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
<script type="text/javascript">
	function updateEqu(){
		document.myform.action="${pageContext.request.contextPath}/api/equipmentinfo/updateEquipmentInfo";
		document.myform.submit();
	}
	function openDept(){
		window.open("${pageContext.request.contextPath}/api/chargeoff/toChooseDept?tmp=" + Math.round(Math.random() * 10000),"newwindow", "height=500, width=400, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
	}
	function doSearch(){
		if(document.getElementById("deptId").value==""){
			alert("请选择车间！");
			return ;
		}
		if(document.getElementById("workCard").value==""){
			alert("请填写工卡号！");
			return ;
		}
		document.myform.action="${pageContext.request.contextPath}/api/chargeoff/getChargeoffDetail";
		document.myform.submit();
	}
	function saveDz(outId){
		document.getElementById("outId").value = outId;
		document.myform.action="${pageContext.request.contextPath}/api/chargeoff/saveDz";
		document.myform.submit();
	}
</script>
</head>
<body>
<form action="" name="myform">
<input type="hidden" name="id" id="id" value="${chargeoffDetail.id}" />
<input type="hidden" name="outId" id="outId" value="" />
<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/chargeoff/getChargeoffDetail?id=${chargeoffDetail.id}"/>
<input type="hidden" name="listUrl" id="listUrl" value="${pageContext.request.contextPath}/api/chargeoff/getChargeoffDetailList"/>
<!-- 设备信息 start -->
	<div class="container-fluid">
	   <div class="row-fluid">
		    <div class="span9">
		     	<div class="page-header">
		        	<h4>对账剩余材料信息</h4>
		        </div>
		        <!-- 设备信息详情start -->
		        <div class="page-header">
		        	<div class="row-fluid">
		            	<div class="span5">
				            <dl class="dl-horizontal">
					            <dt>大类</dt>
					            <dd>${chargeoffDetail.materialClass }</dd>
					         </dl>
		            		 <dl class="dl-horizontal">
						         <dt>物料描述</dt>
						         <dd>${chargeoffDetail.materialDetail }</dd>
			       			 </dl>
		            		 <dl class="dl-horizontal">
						         <dt>领料单位</dt>
						         <dd>${chargeoffDetail.deptGet }</dd>
			       			 </dl>
		            		 <dl class="dl-horizontal">
						         <dt>发料日期</dt>
						         <dd><tag:date value="${chargeoffDetail.receiptDate }"/></dd>
			       			 </dl>
		            		 <dl class="dl-horizontal">
						         <dt>剩余数量</dt>
						         <dd>${chargeoffDetail.num }</dd>
			       			 </dl>
				       	 </div>
				       	 <div class="span5">
				       	 	 <dl class="dl-horizontal">
						         <dt>物料编码</dt>
						         <dd>${chargeoffDetail.materialCode }</dd>
					         </dl>
					         <dl class="dl-horizontal">
						         <dt>物料分组</dt>
						         <dd>${chargeoffDetail.materialGroup }</dd>
					         </dl>
					         <dl class="dl-horizontal">
						         <dt>批次</dt>
						         <dd>${chargeoffDetail.batch }</dd>
					         </dl>
					         <dl class="dl-horizontal">
						         <dt>成本中心描述</dt>
						         <dd>${chargeoffDetail.accountDetail}</dd>
					         </dl>
				       	 </div>
		        	</div>
	       		</div>
	       		<div class="page-header">
		        	<div class="row-fluid">
		        		<div class="span5">
				            <dl class="dl-horizontal">
					            <dt>车间</dt>
					            <dd>
					            	<input id="deptName" name="deptName" class="input-medium"  onclick="openDept(0);" type="text" value="${deptName }" readonly="readonly"/>
							        <input id="deptId" name="deptId" class="input-medium" type="hidden" value="${deptId }"/> 
							     </dd>
					         </dl>
				         </div>
				         <div class="span5">
				            <dl class="dl-horizontal">
					            <dt>工卡号</dt>
					            <dd>
					            	<input id="workCard" name="workCard" class="input-medium"  onclick="" type="text" value="${workCard }"/>
						       		<a class="btn btn-info" href="javascript:void(0)" onclick="doSearch();"><i class="icon-search icon-white"></i>&nbsp;搜 索</a>
						        </dd>
					         </dl>
					     </div>    
		        	</div>
		        	<div class="table">
			      		<table class="table table-striped table-bordered">
				        	<thead>
			                	<tr> 
			                		<th>物料组</th>
			                        <th>物资编码</th>
			                        <th>物资描述</th>
			                        <th>计量单位</th>
			                        <th>数量</th>
			                        <th>操作</th> 
			                     </tr>
		                	</thead>
		                	<tbody>
		                		<c:if test="${empty list}">
		                			<tr> 
		                				<td colspan="6" style="text-align:center;color:red;">暂无数据！</td>
		                			</tr>
		                		</c:if>
		                		<c:forEach items="${list}" var="out" varStatus="i">
		                			<tr> 
		                				<td>${out.materialGroup }</td>
		                				<td>${out.materialCode }</td>
		                				<td>${out.materialDetail }</td>
		                				<td>${out.unity }</td>
		                				<td>${out.materialNum }</td>
		                				<td><a href="#" onclick="saveDz('${out.id }');">对账</a></td>
		                			</tr>
		                		</c:forEach>
		                	</tbody>
		                </table>
	                </div>		
		        </div>
	       		<!-- 设备信息详情end -->
			    <div class="form-actions">
			  		<a class="btn" href="javascript:void(0);" onclick="location.href='javascript:history.go(-1);'">返　回</a>
			    </div>
        	</div>
    	</div>
</form>
</body>
</html>
