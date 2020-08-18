<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>月计划</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script src="${pageContext.request.contextPath}/js/webcalendar.js" type="text/javascript"></script>
<script type="text/javascript">
	function doSearch(){
		document.myform.action="${pageContext.request.contextPath}/api/report/importCompleteMonthReport";
		document.myform.submit();
	}
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span9">
      			<div class="page-header">
			        <h3>生产月报</h3>
     			</div>
				<!-- 搜索框开始 start -->
				<div class="row-fluid">
	    			<form class="form-inline" name="myform">
	    			<input type="hidden" name="id" id="id"/>
    	 				<fieldset>
				            <div class="span4">
				              <p class="control-group">
				                <label class="control-label" for="factory">厂区</label>
				              	<select class="input-small" name=factory id="factory">
					              <option value="">机修公司</option>
					              <option value="1">银川厂</option>
								  <option value="2">庆阳厂</option>
					            </select>
				              </p>
				            </div>
				            <div class="span8">
				            	<p class="control-group">
				                <label for="">时间</label>
				                <input id="inDate" name="inDate" class="input-small" type="text"  value="${param.endDate }"
						            	onclick="SelectDate(this,'yyyy-MM',0,0)" onfocus="SelectDate(this,'yyyy-MM',0,0)" readonly="true" />
				              </p>
				            </div>
            				<div class="span10"> 
            					<a href="javascript:void(0);" class="btn btn-info" onclick="doSearch();"><i class="icon-search icon-white" ></i>生成报表</a> 
            				</div>	
          				</fieldset>
     				</form>     
  				</div>

			</div>
		</div>
	</div>	         	
</body>
</html>
