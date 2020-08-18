<%@ page language="java" pageEncoding="utf-8"%>
<%@include file="/jsp/config/taglib.jsp"%>
<%@include file="/jsp/public/limit_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<head>
	<link href="${pageContext.request.contextPath}/css/fire/global.css"
		rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/css/fire/layout.css"
		rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/css/fire/ui.css"
		rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/fckeditor/fckeditor.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/jquery.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/RegExp.js"></script>
	<script type="text/javascript">
		$("document").ready(function(){
			$("#selectUser").click(openPage2);
			var Myeditor = new FCKeditor("contContent");
	          Myeditor.BasePath = "${pageContext.request.contextPath}/fckeditor/";
	          //Myeditor.ToolbarSet = "Basic";
	          //Myeditor.Height="700";
	          Myeditor.ReplaceTextarea();
		});
       function openPage2(){
       var reciveUserId = $("#reciveUserId").val();
		var reciveUserName = $("#reciveUserName").val();
		var reciveUserIdArray = [];
		var reciveUserNameArray = [];
		if(reciveUserId!=""){
			reciveUserIdArray = reciveUserId.split(',');
			reciveUserNameArray = reciveUserName.split(',');
		}
		inData = "{userList:[";
		for(var i =0;i<reciveUserIdArray.length;i++){
			if(i==reciveUserIdArray.length-1){
				inData+='{\"userId\":\"'+reciveUserIdArray[i]+'\",\"userName\":\"'+reciveUserNameArray[i]+'\"}';
			}else{
				inData+='{\"userId\":\"'+reciveUserIdArray[i]+'\",\"userName\":\"'+reciveUserNameArray[i]+'\"},';
			}
		}
		inData += "],\"postList\":[]}";
		inData = encodeURIComponent(inData);
        var returnData = window.showModalDialog('${pageContext.request.contextPath}/api/org_select/toMain?tmp=" + Math.round(Math.random() * 10000)+"&treeType=0&auth=0&PRU=1&selectedType=1&closeAble=1&inData='+inData, null, "status=no;help:no;dialogWidth=800px;dialogHeight=600px");
        if(returnData!=null){
	        if(returnData.userList != null && returnData.userList.length>0){
		        var reciveUserId="";
		        var reciveUserName="";
		       	for(var m=0;m<returnData.userList.length;m++){
					reciveUserId+=returnData.userList[m].userId;
					reciveUserName+=returnData.userList[m].name;
					if((m+1)!=returnData.userList.length){
						reciveUserId+=",";
						reciveUserName+=",";
					}
				}
				$("#reciveUserId").attr('value',reciveUserId);
				$("#reciveUserName").attr('value',reciveUserName);
	       	}else{
				$("#reciveUserId").attr('value',"");
				$("#reciveUserName").attr('value',"");
	       	}
       	}
       }
function sub(){
	var reciveUserId = document.getElementById("reciveUserId").value;
	var contContent = document.getElementById("contContent").value;
	if (reciveUserId.replace(/\s+/g,"")== "") {
		alert("请输入收件人！");
		return ;
	}
	var contTitle = document.getElementById("contTitle").value;
	if (contTitle.replace(/\s+/g,"")== "") {
		alert("请输入标题！");
		return ;
	}
	if (contTitle.length>=100){
		alert("标题过长，请修改！");
		return ;
	}
  $("#mailForm").submit();
}
</script>
</head>
<html>
	<body>
	<!-- 导航开始 -->
	<div class="navigation">
		<div class="breadcrumb">
			<img class="cp bread_home" alt="home" src="${pageContext.request.contextPath}/css/fire/images/home.gif" id="td_cate"/>
			<em class="breadem"></em>
			<a href="${pageContext.request.contextPath}/api/project/list">消息中心</a>
			<em class="breadem"></em>
			<a href="javascript:void(0);">写信</a>
		</div>
	</div>	
		<div class="MainContent">
			<div class="ListBlock">
				<form id="mailForm" method="post"
					action="${pageContext.request.contextPath}/api/mailManage/add">
					<input type="hidden" name="url"
						value="${pageContext.request.contextPath}/api/mailManage/getSendMail?currentPageNO=${param.currentPageNO}" />
					<input type="hidden" name="flag" value="0" />

					<div class="mailblock_out">
						<table cellspacing="0" cellpadding="0" class="mailblock_in"
							width="100%">
							<tr>
								<th colspan="2" class="mailblock_th">
									写信
								</th>
							</tr>
							<tr>
								<td class="mailblock_td" width="60px">
									<span style="color: Red">*&nbsp;</span>收件人：
								</td>
								<td>
									<input type="text" class="ipt_text" size="56"
										name="reciveUserName" id="reciveUserName" />
									<input type="hidden" class="ipt_text" size="56"
										name="reciveUserId" id="reciveUserId" />
									<a class="btn" href="#" id="selectUser"><span class="r">选择</span>
									</a>
								</td>
							</tr>
							<tr>
								<td width="50px" class="mailblock_td">
									<span style="color: Red">*&nbsp;</span>标&nbsp;&nbsp;&nbsp;&nbsp;题：
								</td>
								<td class="mailblock_td1">
									<input type="text" class="ipt_text" size="56" name="contTitle"
										id="contTitle" />
								</td>
							</tr>
							<tr>
								<td class="mailblock_biao" colspan="2">
									内容：
								</td>
							</tr>
							<tr>
								<td colspan="2" class="mailblock_td">
									<textarea name="contContent" id="contContent"></textarea>
								</td>
							</tr>
							<tr>
								<td class="mailblock_td"></td>
								<td>
									<div
										style="text-align: right; margin-top: 10px; margin-right: 8px;">
										<a class="Bbtn" href="#" onclick="sub();"><span class="Br">发送</span>
										</a>
										<a class="Bbtn" href="#"
											onclick="location.href='${pageContext.request.contextPath}/api/mailManage/getSendMail?currentPageNO=${param.currentPageNO}'"><span
											class="Br">返回</span>
										</a>
									</div>
								</td>
							</tr>
						</table>
					</div>

				</form>
			</div>
		</div>
	</body>
</html>
