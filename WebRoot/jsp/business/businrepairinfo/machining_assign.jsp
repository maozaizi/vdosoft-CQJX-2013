<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>机加调派</title>
		<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/RegExp.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
		<script type="text/javascript">
		function submit(){
			var divId = document.getElementById("divId"); 
        	var result = checkText(divId);
	   		if(result != ""){
	   			alert(result);
	   			return false;
	   		}else{
	   			var firstIds = document.getElementsByName("firstId");
	   			for(var i =0 ;i<firstIds.length;i++){
					if(firstIds[i].value.length!=10){
						alert("施工卡号必须为10位！");
						return ;
					}
				}
				document.form.action="${pageContext.request.contextPath}/api/businrepairinfo/addWorkCode";
				document.form.submit();
			}
		}
		function inputDomain(domain){
				var value = domain.value;
				document.getElementById("domainNo").value = value;
				//判断是否有动态添加行数据有的话替换值
				//var txtTRLastIndex = document.getElementById("txtTRLastIndex").value;
				//if(txtTRLastIndex>2){
				//	for(var i=2;i<txtTRLastIndex;i++){
				//		document.getElementById("domainNo"+i).value = value;
				//	}
				//}
			}
		function goBack() {
		window.location.href = "${pageContext.request.contextPath}/api/mywork/getMyWork";
		}	
		function downLoad(file){
		window.open("${pageContext.request.contextPath}/api/fileUpload/downLoad?file="+file);
		}
		</script>
	</head>
	<body>
		<div class="container-fluid">
			<div class="row-fluid">
		    	<div class="span9">
			        <div class="page-header">
			        	<h3>机加调派</h3>
			        </div>
			        <div class="page-header">
		        	<h4>机加信息</h4>
		            <div class="row-fluid">
		            	<div class="span5">
		            	   <dl class="dl-horizontal">
		                        <dt>送修单位：</dt>
		                        <dd>${machiningMap.userName}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
								<dt>
									图号：
								</dt>
								<dd>
									${machiningMap.figureNum}
								</dd>
							</dl>
		                    <dl class="dl-horizontal">
		                        <dt>规格型号：</dt>
		                        <dd>${machiningMap.equipmentModel}</dd>
		                    </dl>
		                </div>
		                
		                <div class="span5">
		                	<dl class="dl-horizontal">
		                        <dt>名称：</dt>
		                        <dd>${machiningMap.equipmentName}</dd>
		                    </dl>
		                	
		                    <dl class="dl-horizontal">
		                        <dt>计划数量：</dt>
		                        <dd>${machiningMap.planNum}</dd>
		                    </dl>
		                    <dl class="dl-horizontal">
								<dt>
									修制类别：
								</dt>
								<dd>
									${machiningMap.makeSort }
								</dd>
							</dl>
		                </div>
		                <div class="row-fluid">
		                <div class="span9">
		                <dl class="dl-horizontal">
		                        <dt>施工要求：</dt>
		                        <dd>${machiningMap.problem}</dd>
		                    </dl>
		                </div>
		                </div>
		                
		            </div> 
		        </div>
				<c:if test="${not empty fileList}">
			        <div class="page-header">
			        <input type="hidden" name="file" id="file" value=""/>
			         </form>
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
		        <form name="form">
		        <input id="inrepairId" name="inrepairId" type="hidden" value="${machiningMap.id}"/>
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
		        		<table class="table table-striped table-bordered" id="SignFrame">
		                    	<tr>
		                        	<th>施工卡号</th>
									<th>施工车间</th>
		                        </tr>
		                    	<tr>
					              <td><input id="firstId" name="firstId" onkeyup="inputDomain(this);" onblur="inputDomain(this);" class="input-medium" type="text" check="1" maxlength="15"/> / <input id="domainNo"  name="domainNo" class="input-medium" type="text" readonly="readonly" check="1"/></td>
					              <td>
					                  <div class="input-append">
							              <input id="deptName_0" name="deptName" value="机加车间" class="input-medium" type="text" readonly="readonly" check="1"/>
						              </div>
					              </td>
					            </tr>
		        		</table>
		        	</div>
		        </div>
			        <!-- 备注 start -->
					<div class="page-header">
						<h4>填写备注信息</h4>
							<div>    
				        			<ul class="inline">
								    <li><p class="muted">请输入1-1500字的存在问题</p></li>
								    <li><p id="oclable" class="text-success">已输入<span id="oclable">0</span>字符</p></li>
								    </ul>	   
							</div>
	 					<p><textarea onkeyup="textCounter(distributeRemark,oclable,500);" check="1" rows="2" class="span12" name="distributeRemark" id="distributeRemark"></textarea></p>
	 					
					</div>
			    </form>
        		<div class="form-actions">
					<a class="btn" href="#" onclick="back();">取 消</a>
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
