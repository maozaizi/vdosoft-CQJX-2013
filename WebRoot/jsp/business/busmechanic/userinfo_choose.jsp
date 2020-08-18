<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>选择人员</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<style>
	.sticky {
	position: fixed;
	bottom: 0;
	left: 0;
	z-index: 9999;
	width: 100%;
}


.sticky {
	height: 30px;
	padding:10px;
	background: #fefefe;
	border-top: 1px solid #DDDDDD;
	box-shadow: 1px 0px 2px rgba(0,0,0,0.2);
}
.sticky_opt {
	margin-left:3em
}
</style>
<script type="text/javascript">
	function saveChoose(receiveName,receiveCode,verify){
			var checkValueName="";//人员姓名
			var checkValueId="";//人员id
            var checks=$(":checked");
              var num=1;
                 for ( var i = 0; i < checks.length; i++){
                   var str=checks[i].value.split("@");
                   if(num<checks.length){
                    checkValueName+=str[0]+",";
                    checkValueId+=str[1]+",";
                    num++;
                   }else{
                    checkValueName+=str[0];
                    checkValueId+=str[1];
                   }
			     }
		         window.opener.document.getElementById(receiveName).value=checkValueName;
		         window.opener.document.getElementById(receiveCode).value=checkValueId;
		         if(verify=="0"){
		         if(checkValueName==""){
			     window.opener.document.getElementById(receiveName).style["borderColor"]="red";
			     window.opener.document.getElementById("Span"+receiveName).innerHTML="<ul role='alert'><li>必填项</li></ul>";
				    }else{
				     window.opener.document.getElementById(receiveName).style=null;
				     window.opener.document.getElementById("Span"+receiveName).innerHTML=null;
				    }
		         }
		         window.close();
	}
</script>
</head>
<body>
<!-- 人员列表开始  -->
<div class="container-fluid" style="overflow:scroll; height:350px;">
		<div class="row-fluid">
			<div class="span9" style="margin-bottom:40px;">
			  <div class="page-header">
   				<h4>人员选择</h4>
		            <div class="row-fluid" >
		            	<table class="table table-striped table-bordered">
		                	<thead>
		                    	<tr>
									<th>选择</th>
			                        <th>人员姓名</th>
		                        </tr>
		                    </thead>
		                    <tbody>
		                       <c:if test="${empty userinfoList}">
		                    		<tr>
			                            <td colspan="10" style="text-align: center;"><font color='red'>暂无数据！</font></td>
			                         </tr>   
		                    	</c:if>
		                    	<c:if test="${not empty userinfoList}">
			                    	<c:forEach items="${userinfoList}" var="user" varStatus="i">
				                        <tr>
				                            <td><input type="checkbox" name="checkbox" value="${user.name}@${user.userId}" ></input></td>
				                            <td>${user.name}</td>
				                        </tr>
			                        </c:forEach>
		                        </c:if>
		                    </tbody>
		                </table>
		                
		            </div>
		        </div>
<!-- 列表结束 end -->    
	</div>
	</div>
	</div>
	<div id="sticky" class="sticky">	
			<div class="sticky_opt">
	  	<a onclick="saveChoose('${receiveName}','${receiveCode}','${verify}');" href="javascript:void(0);" class="btn">确  定</a>
	  	<a onclick="window.close();"  class="btn">取消</a> 
	  	</div>
	  </div>
</body>
</html>
