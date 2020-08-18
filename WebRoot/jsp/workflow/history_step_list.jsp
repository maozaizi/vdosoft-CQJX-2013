<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
 <%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<script type="text/javascript">
		function getProcessTracking(processPageUrl,processPicturesUrl,insid){
			//document.forms[0].action="${pageContext.request.contextPath}/api/workflow/getProcessTracking?path=${pageContext.request.contextPath}/"+processPageUrl+"&imgurl=${pageContext.request.contextPath}/"+processPicturesUrl+"&insid="+insid+"&currentPageNO=${page.currentPage}";
			//document.forms[0].submit();
				window.showModalDialog("${pageContext.request.contextPath}/api/workflow/getProcessTracking?tmp=" + Math.round(Math.random() * 10000)+"&path=${pageContext.request.contextPath}/"+processPageUrl+"&imgurl=${pageContext.request.contextPath}/"+processPicturesUrl+"&insid="+insid+"","a","dialogheight=450px;dialogwidth=800px;edge=r aised;scroll=yes");
			}
		function viewPages(url,nodeid,distanceid,stepid){
			var id = document.getElementById("id").value;
			if(url!=''){
				//window.open("${pageContext.request.contextPath}/"+url+"?id="+id+"&nodeId="+nodeid+"&instanceId="+distanceid+"&stepId="+stepid, "newwindow", "height=1000, width=800");
				window.showModalDialog("${pageContext.request.contextPath}/"+url+"?tmp=" + Math.round(Math.random() * 10000)+"&id="+id+"&nodeId="+nodeid+"&instanceId="+distanceid+"&stepId="+stepid+"","a","dialogheight=450px;dialogwidth=800px;edge=r aised;scroll=yes");
			// document.forms[0].action = "${pageContext.request.contextPath}/"+url+"?tmp=" + Math.round(Math.random() * 10000)+"&id="+id+"&nodeId="+nodeid+"&instanceId="+distanceid+"&stepId="+stepid;
		 //document.forms[0].target='_blank';
		// document.forms[0].submit();
			}else{
			alert('该流程步骤无历史操作记录！');
			}
		}
		
		</script>
<div class="MainContent">
  <div class="ListBlock" >
	<table width="100%" border="0"  cellpadding="0" cellspacing="0">
		<thead>
		<tr>
	    <th colspan="5">流程信息 </th>
		</tr>
			<tr>
	        	<th>步骤名称</th>
	        	<th>步骤状态</th>
	        	<th>开始时间</th>
	        	<th>结束时间</th>
	        	<th>下一步名称</th>
	        </tr>
        </thead>
        <tbody>
        
       <c:forEach items='${web:getPHStep(pageContext)}' var="historyStep">
	        <tr  ondblclick="viewPages('${historyStep.phurl}','${historyStep.id}','${historyStep.insId}','${historyStep.stepId}')">
	        	<td>${historyStep.stepName}</td>
	        	<td>
	        	<c:if test="${historyStep.status == 'Finished'}">已完成</c:if>
	        	<c:if test="${historyStep.status == 'Underway'}">执行中</c:if>
	        	</td>
		      	<td><tag:dateTime value="${historyStep.startDate}"/></td>
		      	<td><tag:dateTime value="${historyStep.finishDate}"/></td>
		      	<td>${historyStep.previd}</td>
		    </tr>
		</c:forEach>
		<tr>
	    <th colspan="5"><span class="must_answer">双击流程步骤信息显示历史操作记录</span></th>
		</tr>
		</tbody>
	</table>
	</div>
	<!-- 操作按钮开始 -->
	<div class="opt_btn">
		<div class="xy">
		 <c:forEach items='${web:getPHStep(pageContext)}' var="historyStep" varStatus="index">
		 <c:if test="${index.index ==  0}">
			<a class="btn" href="#" id="getMx" name="updat" onclick="getProcessTracking('${historyStep.processPageUrl}','${historyStep.processPicturesUrl}','${historyStep.insId}');"><span class="r">查看流程跟踪图</span></a>
		</c:if>
		</c:forEach>
		</div>
	</div>

	</div>	




