<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>分配修理件/加工件</title>
	<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

	<script type="text/javascript">

		function handleResize() {
		var h = $(window).height();
		        $('#autoHeight').css({'height':h +'px'});
		}
		$(function(){
		        handleResize();

		        $(window).resize(function(){
		        handleResize();
		    });
		});
		
		function chooseright(index){
			
			var choosehtml = $('#l'+index).html();
			
			$('#l'+index).remove();
			$("#selectTable").append("<tr id=r"+index+" onclick=chooseleft("+index+")>"+choosehtml+"</tr>");
			
		}
		
		function chooseleft(index){
			
			var choosehtml = $('#r'+index).html();
			
			$('#r'+index).remove();
			$("#leftTable").append("<tr id=l"+index+" onclick=chooseright("+index+")>"+choosehtml+"</tr>");
			
		}
		
		
		function checkForm() {
			var workcard = document.getElementById("workcard").value;

			if (workcard == "") {
				alert("请输入工卡号！");
				return ;
			}
		     document.forms[0].action = "${pageContext.request.contextPath }/api/businrepairinfo/addeverywf";
			 document.forms[0].submit();
		}
	</script>

</head>
<body>
	
	<div id="autoHeight" class="container-fluid">
       
		<div class="row">

			<div class="span7">

				<h3>待分配修理件/加工件</h3>

				<div class="scroll_view">

					<div class="table">
					
						<table id="leftTable" class="table table-striped table-bordered">

							<thead>
								<tr>
									<th>加工件/修理件</th>
									<th>主卡号</th>
									<th>送修车间</th>
									<th>数量</th>
									<th>类别</th>
								</tr>
							</thead>  	

							<tbody>
								<c:forEach items="${mechlist}" var="mechMap" varStatus="i">
									<tr id="l${i.index}" onclick="chooseright('${i.index}');">
										<input type="hidden" name="chooseid" value="${mechMap.id}"/>
										<td>${mechMap.equipmentName}</td>
										<td>${mechMap.mainCard}</td>
										<td>${mechMap.deptfromname}</td>
										<td>${mechMap.planNum}</td>
										<td>
											<c:if test="${mechMap.repairType=='2'}">加工件</c:if>
											<c:if test="${mechMap.repairType=='3'}">修理件</c:if>
										</td>
									</tr>
								</c:forEach>
								
							</tbody>

						</table>

		    		</div>

				</div>

			</div>
			<form id="addForm" action="" method="post">
				<input type="hidden" name="url" id="url" value = "${pageContext.request.contextPath}/api/businrepairinfo/tolistjg"/>
				<div class="span5 show_view">
	
					<h3>已选择修理件/加工件</h3>
					<div class="scroll_view">
	
						<div class="table ">
						
							<table id="selectTable" class="table table-striped table-bordered">
	
								<thead>
									<tr>
										<th>加工件/修理件</th>
										<th>主卡号</th>
										<th>送修车间</th>
										<th>数量</th>
										<th>类别</th>
									</tr>
								</thead>  	
	
						
							</table>
	
			    		</div>
	
					</div>
	
					<div class="opt_bar">
	
						<p>
							<label for="dept">车间</label>
							<select name="dept" id="dept">
								<c:forEach items="${deptlist}" var="deptlist" varStatus="p">
									<option value="${deptlist.org_code}" <c:if test="${deptlist.org_name=='机加车间'}">selected</c:if>>${deptlist.org_name}</option>
								</c:forEach>
							</select>
						</p>
	
						<p class="control-group">
							<label class="control-label" for="workcard">工卡号</label>
							<input id="workcard" name="workcard" type="text" value="">
						</p>
						
						<button class="btn btn-primary" type="button" onclick="checkForm()">确认分配</button>
					</div>
	
				</div>
		</form>
		</div>

    </div>

</body>
</html>
