<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>调度确认</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript">
		
		function goBack() {
			 window.location.href = "${pageContext.request.contextPath}/api/businrepairinfo/getFactoryList";
		}	
		function downLoad(file){
			window.open("${pageContext.request.contextPath}/api/fileUpload/downLoad?file="+file);
		}
		function save(res){
			document.getElementById("result").value=res;
			document.myform.action="${pageContext.request.contextPath}/api/businrepairinfo/doSchedulingManage";
			document.myform.submit();
		}
       </script>
	</head>
	<body>
	<div class="container-fluid">
	   <div class="row-fluid">
		    <div class="span9">
				<div class="page-header">
		        	<h3>调度确认</h3>
		        </div>
		        
		         <div class="page-header">
		        	<h4>基本信息</h4>
		            <div class="row-fluid">
		            	<div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>工卡号：</dt>
		                        <dd>
		                        ${busInrepairInfoMap.workcard}
		                        </dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>设备名称：</dt>
		                        <dd>${busInrepairInfoMap.equipmentName}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
		                        <dt>机身编号：</dt>
		                        <dd>${busInrepairInfoMap.bodyCode}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
							<dt>单位：</dt>
							<dd>${busInrepairInfoMap.unity }</dd>
							</dl>
							<dl class="dl-horizontal">
								<dt>
									人员定额（人次）：
								</dt>
								<dd>
									${busInrepairInfoMap.quotaHour }
								</dd>
							</dl>
		                    <dl class="dl-horizontal">
		                        <dt>修理类别：</dt>
		                        <dd>
		                        <c:if test="${busInrepairInfoMap.repairType==1}">大修</c:if>
		                        <c:if test="${busInrepairInfoMap.repairType==2}">中修</c:if>
		                        <c:if test="${busInrepairInfoMap.repairType==3}">检修</c:if>
		                        </dd>
		                    </dl>
		                </div>
		                
		                <div class="span5">
		                    <dl class="dl-horizontal">
		                        <dt>自编号：</dt>
		                        <dd>${busInrepairInfoMap.selfCode}</dd>
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
		        <form action="" name="myform" id="myform">
		        	<input type="hidden" name="id" value="${busInrepairInfoMap.id}"/>
					<input type="hidden" name="result" id="result" value=""/>
					<input id="url" name="url" type="hidden" value="${pageContext.request.contextPath}/api/mywork/getMyWork"/>
		         <div class="page-header">
			        	  <div class="row-fluid">
				        	<span class="span5">
				        		<div class="control-group">
				        		 <dl class="dl-horizontal">
				                        <dt>使用单位：</dt>
				                        <dd><input id="factoryWhere" value="${busInrepairInfoMap.factoryWhere}" name="factoryWhere" class="input-medium" type="text" maxlength="8"/></dd>
				                    </dl> 
						        </div>
				        	</span>
				        	<span class="span4">
				        		<div class="control-group">
						         	<dl class="dl-horizontal">
				                        <dt>出厂时间：</dt>
				                        <dd><input id="factoryDate" name="factoryDate" value="<tag:date  value='${busInrepairInfoMap.factoryDate}' />" class="input-medium" type="text" required
						            	onclick="SelectDate(this,'yyyy-MM-dd',0,0)" onfocus="SelectDate(this,'yyyy-MM-dd',0,0)" readonly="true" /></dd>
				                    </dl> 
						        </div>
				        	</span>
			        	  </div>
		        	 <div>    
		        	 </form>
		       	<div class="form-actions">
				  	<a class="btn" href="javascript:void(0)" onclick="goBack();">取消</a> 
				  	<a class="btn btn-primary" href="javascript:void(0)" onclick="save(0);">确认出厂</a>
				  	<a class="btn btn-primary" href="javascript:void(0)" onclick="save(1);">发送监造</a>
				</div> 
	           
			</div>
		</div>
	</div>
	</body>
</html>
