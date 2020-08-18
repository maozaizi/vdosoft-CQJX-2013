<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/config/taglib.jsp" %>
<%@include file="/jsp/public/limit_top.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>材料信息管理列表</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
<script type="text/javascript">
	var xuhao=1;
	//选择物料，添加行
	function selectMaterial(materialClass,materialGroup,materialCode,materialDetail,unity,unitPrice){
		if(materialDetail.indexOf("@")>-1){
			materialDetail = materialDetail.replace('@','\"');
		}
		var selectTable=document.getElementById( "selectTable" );
		//判断选择列表selectTable，是否存在 暂无数据，删除该行
   		if(selectTable.rows.length==2&&selectTable.rows[1].cells[0].innerHTML=='暂无数据！'){
			selectTable.deleteRow(1);
			xuhao=1;
   		}
		//添加行 
		var newTR = selectTable.insertRow(xuhao);
		newTR.id ="materialTr_"+xuhao; 
		//添加列:材料描述
		var materialDetailTD = newTR.insertCell(0);
		materialDetailTD.innerHTML=materialDetail+"<input type='hidden' name='materialDetail' value='"+materialDetail+"'>";
		//添加列:大类
		//var materialClassTD = newTR.insertCell(1);
		//materialClassTD.innerHTML=materialClass+"<input type='hidden' name='materialClass' value='"+materialClass+"'>";
		//添加列:物料组
		var materialGroupTD = newTR.insertCell(1);
		materialGroupTD.innerHTML=materialGroup+"<input type='hidden' name='materialGroup' value='"+materialGroup+"'>";
		//添加列:物料编码
		var materialCodeTD = newTR.insertCell(2);
		materialCodeTD.innerHTML=materialCode+"<input type='hidden' name='materialCode' value='"+materialCode+"'>";
		//添加列:单位
		var unityTD = newTR.insertCell(3);
		unityTD.innerHTML=unity+"<input type='hidden' name='unity' value='"+unity+"'>";
		//添加列:参考价
		var unitPriceTD = newTR.insertCell(4);
		unitPriceTD.innerHTML=unitPrice+"<input type='hidden' name='referencePrice' value='"+unitPrice+"'>";
		//添加列:数量
		var numTD = newTR.insertCell(5);
		numTD.innerHTML='<input id="num" name="num" class="input-mini" type="text"/>';
		//添加列:单价
		var numTD = newTR.insertCell(6);
		numTD.innerHTML='<input id="unitPrice" name="unitPrice" class="input-mini" type="text"/>';
		//添加列:类型
		var typeTD = newTR.insertCell(7);
		typeTD.innerHTML='<select class="input-small" name="materialType"><option value="1">材料1</option><option value="2">材料2</option><option value="3">材料3</option><option value="4">材料4</option><option value="5">材料5</option></select>';
		//添加列:操作
		var materialClassTd = newTR.insertCell(8);
		materialClassTd.innerHTML='<a href="javascript:void(0);" onclick="delRow('+xuhao+')">删除</a>';
		xuhao++;
	}
	//删除行
	function delRow(rowId){
		var selectTable=document.getElementById("selectTable");
		var signItem = document.getElementById( "materialTr_"+rowId);
		//获取将要删除的行的Index
		var rowIndex = signItem.rowIndex;
		selectTable.deleteRow(rowIndex);
		//selectTable列表，删除所有选择信息，显示暂无数据
		if(selectTable.rows.length==1){
			var newTR = selectTable.insertRow(1);
			var mark = newTR.insertCell(0);
			mark.colSpan=10;
			mark.style="text-align:center;color:red;";
			mark.innerHTML="暂无数据！";
		}
		//重新绘制ID
		repaintNumber();
	}
	
	//重新绘制ID
	function repaintNumber(){
		var ptab = document.getElementById("selectTable");
		for (var i = 1; i<ptab.rows.length; i++) {
			ptab.rows[i].id="materialTr_"+i;
			ptab.rows[i].cells[8].innerHTML = '<a href="javascript:void(0);" onclick="delRow('+i+')">删除</a>';
		}
		xuhao=ptab.rows.length;    
	}
	
	function saveStoreDetail(){
		if(checkForm()){
			document.myform.action="${pageContext.request.contextPath}/api/storedetail/saveStoreDetail";
			document.myform.submit();
		}
	}
	function back(){
		document.myform.action="${pageContext.request.contextPath}/api/storedetail/getStoreDetailList";
		document.myform.submit();
	}
	function checkForm(){
			var materialNums = document.getElementsByName("num");
			var unitPrices = document.getElementsByName("unitPrice");
			var flag = true;
			var reNum =/^[0-9]*$/;
			var rePrice=/^[0-9]+(.[0-9]{2})?$/;
			for(var i=0;i<materialNums.length;i++){
				//验证数量
				if(materialNums[i].value==""){
					materialNums[i].style["borderColor"]="red";
					flag=false;
				}else{
					if(!reNum.test(materialNums[i].value)){
						materialNums[i].style["borderColor"]="red";
						flag=false;
					}else{
						materialNums[i].style["borderColor"]="";
					}
				}
				//验证单价
				if(unitPrices[i].value==""){
					unitPrices[i].style["borderColor"]="red";
					flag=false;
				}else{
					if(!rePrice.test(unitPrices[i].value)){
						unitPrices[i].style["borderColor"]="red";
						flag=false;
					}else{
						unitPrices[i].style["borderColor"]="";
					}
				}
				
			}
			return flag;
		}
</script>
</head>
<body>
<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span12">
    			c<!-- iframe 材料选择列表 start -->
    			<div class="row-fluid">
					<iframe style="height:700px;width:100%;" frameborder="0" name="listFrame" id="listFrame" src="${pageContext.request.contextPath}/api/storedetail/toMaterialInfoList"></iframe>
				</div>
				<!-- iframe 材料选择列表 end -->
				<div class="row-fluid">
					<div class="span9">
						<!-- table 材料选择结果 start -->
						<form action="" name="myform">
						<input type="hidden" id="url" name="url" value="${pageContext.request.contextPath}/api/storedetail/getStoreDetailList"/>
				        <table id="selectTable" class="table table-striped table-bordered">
				          <tbody>
				            <tr>
							  <th>物料描述</th>
				              <th>物料组</th>
				              <th>物料编码</th>
							  <th>单位</th>
							  <th>参考价（元）</th>
							  <th>数量</th>
							  <th>单价（元）</th>
							  <th>类型</th>
							  <th>操作</th>
				            </tr>
				            <tr>
				            	<td colspan="9" style="text-align: center;color:red;">暂无数据！</td>
				            </tr>
				          </tbody>
				        </table>
				        </form>
				    </div>
				  </div>
			    <div class="row-fluid">
					<div class="span9">
						<div class="form-actions">
						<span style="color:red">(单价请保留小数点后两位数字或整数)</span>
							<a class="btn btn-primary" onclick="saveStoreDetail();" href="javascript:void(0);">保 存</a>
							<a class="btn" onclick="back();" href="javascript:void(0);" >返  回</a>
						</div>
					</div>
				</div>
			  </div>
		  </div>
	</div>
<!-- 选择结果 end -->
</body>
</html>
