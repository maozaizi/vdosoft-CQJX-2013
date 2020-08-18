<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/public/limit_top.jsp" %>
<%@include file="/jsp/config/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
  	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>right</title>
    <link href="${pageContext.request.contextPath}/css/fire/global.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/css/fire/layout.css" rel="stylesheet" type="text/css" />
     <style type="text/css">
		div.person_selected_list,div.post_selected_list {
			width:730px;
			height:auto;
			padding:3px;
			background-color:#FFFFFF;
			border:solid #CCCCCC 1px;
		}
		div.person_selected_list span,div.post_selected_list span {
			display:inline-block;
			position:relative;
			height:20px;
			margin:0 4px 4px 4px;
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
			returnData = {"userList":[],"postList":[]};
			var selectedType = '${param.selectedType}';
			$("#selectButton_person").click(selectUsers);
			$("#selectButton_post").click(selectPosts);
			if(selectedType != 0){
				//页面载入的时候，给已经选中的数据块加上样式
				var selected_page_dom = window.parent.frames[2].document;
				//鼠标悬停出现关闭按钮
				$(".person_selected_list>span>div,.post_selected_list>span>div",selected_page_dom).click(function(){
					$(this).parent('span').remove();
				});
				$(".person_selected_list>span>div,.post_selected_list>span>div",selected_page_dom).addClass('colse_button');
				$(".person_selected_list>span,.post_selected_list>span",selected_page_dom).hover(function(){
					$(this).find('div').show();
				},function(){
					$(this).find('div').hide();
				});
				//人员  点击多选/单选框的时候添加数据框并且添加样式和删除事件
				$('input[id=userId]').click(function(){
					var _this = $(this)
					//承载用户名称的div
					var checked_bottom = $(window.parent.frames[2].document).find('.person_selected_list');
					//用户名称记录列表
					var checked_bottom_values = checked_bottom.find('span');
					if(_this.attr('checked')){
						flag = true;
						$.each(checked_bottom_values,function(index,e){
							var el = $(e);
							if(_this.attr('userId')==el.attr('userId')){
								alert('该用户已经存在');
								flag = false;
							}
						});
						if(flag){
							var newPerson = '<span userName = "'+_this.attr('userName')+'" userId= "'+_this.attr('userId')+'" gender = "'+_this.attr('gender')+'">'+_this.attr('userName')+'<div class="colse_button" style="display:none"></div></span>';
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
					}else{
						$.each(checked_bottom_values,function(index,e){
							var el = $(e);
							if(_this.attr('userId')==el.attr('userId')){
								flag = true;
								el.remove();
							}
						});
					}
				});
				
				
				//岗位  点击多选/单选框的时候添加数据框并且添加样式和删除事件
				$('input[id=postId]').click(function(){
					var _this = $(this)
					//承载用户名称的div
					var checked_bottom = $(window.parent.frames[2].document).find('.post_selected_list');
					//用户名称记录列表
					var checked_bottom_values = checked_bottom.find('span');
					if(_this.attr('checked')){
						flag = true;
						$.each(checked_bottom_values,function(index,e){
							var el = $(e);
							if(_this.attr('postId')==el.attr('postId')){
								alert('该岗位已经存在');
								flag = false;
							}
						});
						if(flag){
							var newPost = '<span postId = "'+_this.attr('postId')+'" postName= "'+_this.attr('postName')+'">'+_this.attr('postName')+'<div class="colse_button" style="display:none"></div></span>';
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
					}else{
						$.each(checked_bottom_values,function(index,e){
							var el = $(e);
							if(_this.attr('postId')==el.attr('postId')){
								flag = true;
								el.remove();
							}
						});
					}
				});
			}
		});
		
		
		
		
		
		function selectUsers(){
			returnData.userList = [];
			var f=false;
			isClose = true;
			$("input[id=userId]:checked").each(function (index,e){
				var user = {"userId":'',"name":'',"gender":''};
				f=true;
				user.userId = $(e).attr("userId");
				user.name = $(e).attr("userName");
				user.gender = $(e).attr("gender");
				returnData.userList.push(user);
			});
	        if(f){
				window.parent.returnValue = returnData;
				window.parent.close();
			}else{
				alert("请勾选要选择的人员！");
				isClose =  false;
			}
		}
		function selectPosts(){
				returnData.postList = [];
				var f=false;
				isClose = true;
				$("input[id=postId]:checked").each(function (index,e){
					var post = {"postId":'',"postName":''};
					f=true;
					post.postId = $(e).attr("postId");
					post.postName = $(e).attr("postName");
					returnData.postList.push(post);
				});
		        if(f){
					window.parent.returnValue = returnData;
					window.parent.close();
				}else{
					alert("请勾选要选择的岗位！");
					isClose =  false;
				}
			}
    </script>
  </head>
  
  <body>
	<div class="MainContent">
		<div class="ListBlock">
		    <h3 class="cBlack">基本信息</h3>
		    <div class="EditBox">
	  		<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  	  	<tr>
	  	  	<th  colspan="2">基本信息</th>
	  	  	</tr>
			  <tr>
				<td class="EditBox_td1">名称：</td>
				<td>
				  	${organizationDetali.orgName }
				</td>
			  </tr>
		</table>
		</div>
		<c:if test="${param.PRU == '0'}">
		<div class="List">
		<form action="" id="userInfoList">
	  	  <table width="100%" cellspacing="0" cellpadding="0">
	  	    <caption>人员信息</caption>
	  	  	<thead>
			<tr>
				<th width="25%"  class="List_Th">姓名</th>
	        	<th width="25%"  class="List_Th">性别</th>
	        	<th width="25%"  class="List_Th">出生年月</th>
	        	<th width="25%"  class="List_Th">民族</th>
	        </tr>
	        </thead>
			<c:forEach items="${userInfoList}" var="item">
				<tr>
		        	<td bordercolor="1" >
		        		<input type="${param.selectedType==0?'radio':'checkbox'}" name="userId" id="userId" userId="${item.userId }" userName="${item.name }" gender="${item.gender }" title="${item.userId }"/>
		        		${item.name }
		        	</td>
		        	
			      	<td  >${item.gender }</td>
			      	<td  >${fn:substring(item.birthData,0,10) }</td>
			      	<td  >${item.nation }</td>
			    </tr>
			</c:forEach>
			<tfoot>
			<c:if test="${param.closeAble==0}">
			<tr>
				<td  colspan="5">
				 <div class="OptBar">
					 <a class="btn" href="#" id="selectButton_person"><span class="r">选择</span></a>
				</div>
				</td>
	        </tr>
	        </c:if>
			
	        </tfoot>
		</table>
		</form>
		</div>
		
		<form action="" id="postInfoList">
		<div class="List">
	  	  <table width="100%" cellspacing="0" cellpadding="0">
	  	  	<caption>岗位信息</caption>
	  	  	<thead>
			<tr>
				<th width="40%">名称</th>
	        	<th width="40%">职责</th>
	        </tr>
	        </thead>
	        <tbody>
	        <c:forEach items="${postInfoList}" var="item">
				<tr>
			      	<td><input type="${param.selectedType==0?'radio':'checkbox'}" name="postId" id="postId" postId="${item.postId }" postName = "${item.postName }" title="${item.postId }"/>
			      		${item.postName }
			      	</td>
			      	<td>${item.postDuties }</td>
			    </tr>
			</c:forEach>
			</tbody>
			<tfoot>
			<c:if test="${param.closeAble==0}">
			<tr>
				<td  colspan="5">
				 <div class="OptBar">
					 <a class="btn" href="#" id="selectButton_post"><span class="r">选择</span></a>
				</div>
				</td>
	        </tr>
	        </c:if>
			
	        </tfoot>
		</table>
  		</div>
		</form>
		</c:if>
		<c:if test="${param.PRU == '1'}">
		<div class="List">
		<form action="" id="userInfoList">
	  	  <table width="100%" cellspacing="0" cellpadding="0">
	  	    <caption>人员信息</caption>
	  	  	<thead>
			<tr>
				<th width="25%"  class="List_Th">姓名</th>
	        	<th width="25%"  class="List_Th">性别</th>
	        	<th width="25%"  class="List_Th">出生年月</th>
	        	<th width="25%"  class="List_Th">民族</th>
	        </tr>
	        </thead>
			<c:forEach items="${userInfoList}" var="item">
				<tr>
		        	<td bordercolor="1" >
		        		<input type="${param.selectedType==0?'radio':'checkbox'}" name="userId" id="userId" userId="${item.userId }" userName="${item.name }" gender="${item.gender }" title="${item.userId }"/>
		        		${item.name }
		        	</td>
			      	<td  >${item.gender }</td>
			      	<td  >${fn:substring(item.birthData,0,10) }</td>
			      	<td  >${item.nation }</td>
			    </tr>
			</c:forEach>
			<tfoot>
			<c:if test="${param.closeAble==0}">
			<tr>
				<td  colspan="5">
				 <div class="OptBar">
					 <a class="btn" href="#" id="selectButton_person"><span class="r">选择</span></a>
				</div>
				</td>
	        </tr>
	        </c:if>
			
	        </tfoot>
		</table>
		</form>
		</div>
		</c:if>
		<c:if test="${param.PRU == '2'}">
		<div class="List">
		<form action="" id="postInfoList">
		<div class="List">
	  	  <table width="100%" cellspacing="0" cellpadding="0">
	  	  	<caption>岗位信息</caption>
	  	  	<thead>
			<tr>
				<th width="40%">名称</th>
	        	<th width="40%">职责</th>
	        </tr>
	        </thead>
	        <tbody>
	        <c:forEach items="${postInfoList}" var="item">
				<tr>
			      	<td><input type="${param.selectedType==0?'radio':'checkbox'}" name="postId" id="postId" postId="${item.postId }" postName = "${item.postName }" title="${item.postId }"/>
			      		${item.postName }
			      	</td>
			      	<td>${item.postDuties }</td>
			    </tr>
			</c:forEach>
			</tbody>
			<tfoot>
			<c:if test="${param.closeAble==0}">
			<tr>
				<td  colspan="5">
				 <div class="OptBar">
					 <a class="btn" href="#" id="selectButton_post"><span class="r">选择</span></a>
				</div>
				</td>
	        </tr>
	        </c:if>
			
	        </tfoot>
		</table>
  		</div>
		</form>
		</div>
		</c:if>
    </div>
  </body>
</html>
