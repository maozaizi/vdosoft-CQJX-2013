<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>流程监控管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="description" content="列表"/>
		<!--  
		<link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/css/fire/ui.css" rel="stylesheet" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
		<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>-->
		
			<!-- Bootstrap -->
		<link href="${pageContext.request.contextPath }/css/bootstrap.min.css" rel="stylesheet" media="screen"/>
		<script src="${pageContext.request.contextPath }/js/jquery.1.7.2.min.js"></script>
		<script src="${pageContext.request.contextPath }/js/bootstrap.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
		
		<script type="text/javascript">
	 	
	 	function search1(){ 
	 		$("#form").attr("action","${pageContext.request.contextPath}/api/workflow/getBizInstanceList");
			$("#form").submit();
	 	}
	 	
	 	//$(document).ready(function (){
		//	$("#getMx").click(function (){
		//	var f=false;  
		//	 $("input[type=radio]:checked").each(function (i){f=true;});
		//	        if(f){
		//	        var a=$("input[type=radio]:checked").val();
		//	        $("#templateManagerId").val(a);
		//	         document.form.action="${pageContext.request.contextPath}/api/task/get";
		//			 document.form.submit();
		//			}else{
		//				alert("请选择要查看的信息！");
		//			}			
		//	});
		//});
		function openSheet(p){
		    $("#state").val(p);
			search1();
		}
		function checkRow(id){
			document.getElementById(id).checked = true;
		}
		function orgChoose(){
		   var idAndName = window.showModalDialog('${pageContext.request.contextPath}/api/baseorganization/ListWin?tmp=' + Math.round(Math.random() * 10000), window, "status=no;help:no;dialogWidth=460px;dialogHeight=670px");
           if(null!=idAndName&&idAndName.length>0){
				document.getElementById("id").value=idAndName[0];
				document.getElementById("name").value=idAndName[1];
           }
	}
		function updState(id,updstate){
			
			document.forms[0].action="${pageContext.request.contextPath}/api/workflow/updateBizInstanceState?id="+id+"&updstate="+updstate;
			document.forms[0].submit();
		}
		function getProcessTracking(processPageUrl,processPicturesUrl,insid){
			//document.forms[0].action="${pageContext.request.contextPath}/jsp/workflow/process_tracking.jsp?path=${pageContext.request.contextPath}/"+processPageUrl+"&imgurl=${pageContext.request.contextPath}/"+processPicturesUrl+"&steps=1";
			//document.forms[0].action="${pageContext.request.contextPath}/api/workflow/getProcessTracking?path=${pageContext.request.contextPath}/"+processPageUrl+"&imgurl=${pageContext.request.contextPath}/"+processPicturesUrl+"&insid="+insid+"&currentPageNO=${page.currentPage}";
			//document.forms[0].submit();
				window.showModalDialog("${pageContext.request.contextPath}/api/workflow/getProcessTracking?tmp=" + Math.round(Math.random() * 10000)+"&path=${pageContext.request.contextPath}/"+processPageUrl+"&imgurl=${pageContext.request.contextPath}/"+processPicturesUrl+"&insid="+insid+"","a","dialogheight=450px;dialogwidth=800px;edge=r aised;scroll=yes");
			}
		</script>		
<body>
<div >
  <div >
    <h3 >您当前的位置：流程监控列表</h3>
	<form id="form" name="form" method="post" action="">
	<!-- form 提交表单隐藏域开始-->
	<input type="hidden" name="url" id="url" value="${pageContext.request.contextPath}/api/workflow/getBizInstanceList?currentPageNO=${page.currentPage}"/>
	<input type="hidden" name="state" id="state" value="${state}"/>
	<!-- form 提交表单隐藏域结束-->
    <div >
				  <ul class="nav nav-tabs nav-pills">
				   	<li <c:if test="${empty state}"> class="active" </c:if> ><a href="#" onclick="openSheet('');"><span>全部流程</span></a></li>			    		 				    		   
				    <li <c:if test="${state=='0'}"> class="active" </c:if>><a href="#" onclick="openSheet('0');"><span>未执行</span></a></li>
				    <li<c:if test="${state=='1'}"> class="active" </c:if>><a href="#" onclick="openSheet('1');"><span>执行中</span></a></li>
				    <li <c:if test="${state=='2'}"> class="active" </c:if>><a href="#" onclick="openSheet('2');"><span>暂停</span></a></li>
				   <li <c:if test="${state=='3'}"> class="active" </c:if>><a href="#" onclick="openSheet('3');"><span>无效</span></a></li>
				    <li <c:if test="${state=='4'}"> class="active" </c:if>><a href="#" onclick="openSheet('4');"><span>完成</span></a></li>
				     <li <c:if test="${state=='-1'}"> class="active" </c:if> ><a href="#" onclick="openSheet('-1');"><span>未知</span></a></li>	
				  </ul>
				</div>
	<div>
	  <table class="table table-hover table-bordered">
		<thead>
			<tr>
				<!--<th width="10">选择</th>-->
	        	<th>流程定义名称</th>
	        	<th>流程编码</th>
	        	<th>发布流程名称</th>
	        	<th>发布流程状态</th>
	        	<th>业务编号</th>
	        	<th>分类</th>
	        	<!-- <th>是否有效</th>-->
	        	<th>调用者</th>
	        	<th>创建时间</th>
	        	<th>操作</th>
	        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.resultList}" var="process">
	        <tr>
	        	<!-- <td><input type="radio" name="radios" id="${process.id}" value="${process.id}" title="${process.id}"/></td> -->
	        	<td>${process.strname}</td>
	        	<td>${process.process_code}</td>
		      	<td>${process.cont_title}</td>
		      	<td>
		      	<c:if test="${process.state==0}">未执行
		      	</c:if>
		      	<c:if test="${process.state==1}">执行中
		      	</c:if>
		      	<c:if test="${process.state==2}">暂停
		      	</c:if>
		      	<c:if test="${process.state==3}">无效
		      	</c:if>
		      	<c:if test="${process.state==4}">完成
		      	</c:if>
		      	<c:if test="${process.state==-1}">未知
		      	</c:if>
		      	</td>
		      	<td>${process.obj_id}</td>
		      	<td>
		      	
		      	</td>
		      	<!--<td>
		      	<c:if test="${process.is_valid==0}">无效
		      	</c:if>
		      	<c:if test="${process.is_valid==1}">有效
		      	</c:if>
		      	</td>-->
		      	<td>${process.caller}</td>
		      	<td><tag:date value="${process.create_date}"/></td>
		      	<td>
		      	<c:if test="${process.state==1 }">
		      	<a class="btn" href="#" id="upd" name="upd" onclick="updState('${process.id}','2')"><span class="r">暂停</span></a>
				<a class="btn" href="#" id="getMx" name="updat" onclick="getProcessTracking('${process.process_page_url}','${process.process_pictures_url}','${process.id}');"><span class="r">查看流程跟踪图</span></a>	
		      	</c:if>
		      	<c:if test="${process.state==2}">
		      	<a class="btn" href="#" id="getMx" name="upda" onclick="updState('${process.id}','1')"><span class="r">恢复</span></a>
		      	<a class="btn" href="#" id="getMx" name="updat" onclick="updState('${process.id}','3')"><span class="r">无效</span></a>
		      	</c:if>
		      	
		      	</td>
		    </tr>
		</c:forEach>
		</tbody>
		<tfoot>
	        <tr>
        		<td colspan="11">
				  <div class="page">
				    <%@include file="/jsp/public/standard.jsp" %>
	              </div>
	            </td>
	        </tr>
		</tfoot>
	</table>
	</div>
	<!-- 操作按钮开始 
	<div class="opt_btn">
		<div class="xy">
			<a class="btn" href="#" onclick="location.href='${pageContext.request.contextPath}/api/task/toAdd'" ><span class="r">新建任务</span></a>
			<a class="btn" href="#" id="getMx" name="getMx"><span class="r">查看任务</span></a>
		</div>
	</div>
		-->
	<!-- 查询条件开始 -->
	<div >
    <fieldset>
    <legend>查询条件</legend>
    <table>
      <tbody>
        <tr>
          <th >流程定义名称：</th>
          <td>
          <input name="strname" type="text" value="${param.strname }"/>
          </td>
          <th >流程编码：</th>
          <td>
          <input name="processCode" type="text" value="${param.processCode }"/>
          </td>
        </tr> 
         <tr>
          <th >发布流程名称：</th>
          <td>
          <input name="contTitle" type="text" value="${param.contTitle }"/>
          </td>
          <th >发布流程编码：</th>
          <td>
          <input name="insid" type="text" value="${param.insid }"/>
          </td>
        </tr> 
         <tr>
          <th >业务编码：</th>
          <td>
          <input name="objId" type="text" value="${param.objId }"/>
          </td>
          <td>
           <a class="btn btn-primary" name="sss" href="#" onclick="search1();"><span class="Br">搜　索</span></a>
           </td>   
        </tr> 
         
      </tbody>
    </table>
    </fieldset>
	</div>

	</form>
	</div>	
	</div>
	</div>
</body>
</html>


