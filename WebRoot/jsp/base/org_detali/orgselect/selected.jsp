<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
		div.person_selected_list,div.post_selected_list {
			width:97%;
			height:auto;
			padding:6px;
			background-color:#FFFFFF;
			border:solid #CCCCCC 1px;
		}
		div.person_selected_list span,div.post_selected_list span {
			display:inline-block;
			position:relative;
			height:20px;
			margin:0 4px 8px 4px;
			padding:4px 16px;
			border:solid #CCCCCC 1px;
		}
		.colse_button{
			background-image: url("${pageContext.request.contextPath}/css/fire/images/cancle.png");
			background-repeat: no-repeat;
			width:8px;
			height:8px;
			background-color: #FFFFFF;
			background-position: 0px 0px;
			position:absolute;
			right:4px;
			top: 10px;
			cursor: pointer;
		}
    </style>
    <script src="${pageContext.request.contextPath}/js/jquery.js" type="text/javascript"></script>
    <script type="text/javascript">
		$("document").ready(function (){
			var inData = ${param.inData};
			if(inData != null){
				var userList = inData.userList;
				var postList = inData.postList;
				if(userList.length>0){
					//承载用户的div
					var checked_bottom = $('.person_selected_list');
					for(var i = 0,j = userList.length;i<j;i++){
						user = userList[i];
						var newPerson = '<span userName = "'+user.userName+'" userId= "'+user.userId+'" gender = "'+user.gender+'">'+user.userName+'<div class="colse_button" style="display:none"></div></span>';
						var $newPerson =  checked_bottom.append(newPerson);
						$newPerson.find('span:last').hover(function(){
							$(this).find('div').show();
						},function(){
							$(this).find('div').hide();
						})
						$newPerson.find('span:last>div').click(function(){
							$(this).parent('span').remove();
						});
					}
				}
				if(postList.length>0){
					//承载岗位的div
					var checked_bottom = $('.post_selected_list');
					for(var i = 0,j = postList.length;i<j;i++){
						post = postList[i];
						var newPost = '<span postId = "'+post.postId+'" postName= "'+post.postName+'">'+post.postName+'<div class="colse_button" style="display:none"></div></span>';
						var $newPost =  checked_bottom.append(newPost);
						$newPost.find('span:last').hover(function(){
							$(this).find('div').show();
						},function(){
							$(this).find('div').hide();
						})
						$newPost.find('span:last>div').click(function(){
							$(this).parent('span').remove();
						});
					}
				}
			}
		
			$("#okAndClocePage").click(function(){
				var PRU = ${param.PRU};
				if(PRU == 0){
					getRetunDate(0);
				}else if(PRU == 1){
					getRetunDate(1);
				}else if(PRU == 2){
					getRetunDate(2);
				}
			});
			$("#closePage").click(function(){
				returnData = {"userList":[],"postList":[]};
				window.parent.close();
			});
		});
		
		//type:
		//0：人员和岗位1:人员:岗位
		function getRetunDate (type){
		    var returnData = {"userList":[],"postList":[]};
		    if(type == 0 || type == 1){
		    	var personArray = $('.person_selected_list').find('span');
				$.each(personArray,function (index,e){
					var user = {"userId":'',"name":'',"gender":''};
					user.userId = $(e).attr("userId");
					user.name = $(e).attr("userName");
					user.gender = $(e).attr("gender");
					returnData.userList.push(user);
				});
		    }
		    if(type == 0 || type == 2){
				var postArray = $('.post_selected_list').find('span');
				$.each(postArray,function (index,e){
					var post = {"postId":'',"postName":''};
					post.postId = $(e).attr("postId");
					post.postName = $(e).attr("postName");
					returnData.postList.push(post);
				});
		    }
			window.parent.returnValue = returnData;
			window.parent.close();
		}
    </script>
  </head>
  <body>
    <c:if test="${param.closeAble != 0}">
    <span class="OptBar"><a href="#" class="btn" id="okAndClocePage"><span class="r">确定</span></a></span>
    <span class="OptBar"><a href="#" class="btn" id="closePage"><span class="r">取消</span></a></span>
  	<div id="selected_show">
  		您已经选择：
  	</div>
    <c:if test="${param.PRU == 0}">
  	<div class="List" >
	  	<div class="person_selected_list" >
	  	</div>
	 </div>
   	<div class="List" >
	  	<div class="post_selected_list" >
	  	</div>
  	</div>
	 </c:if>
	  <c:if test="${param.PRU == 1}">
	  	<div class="person_selected_list" >
	  	</div>
	  </c:if>
	 <c:if test="${param.PRU == 2}">
   	<div class="List" >
	  	<div class="post_selected_list" >
	  	</div>
  	</div>
	 </c:if>
    </c:if>
  </body>
</html>
