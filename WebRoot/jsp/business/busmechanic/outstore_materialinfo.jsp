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
		//添加列:单价
		var unitPriceTD = newTR.insertCell(4);
		unitPriceTD.innerHTML=unitPrice+"<input type='hidden' name='estimatePrice' value='"+unitPrice+"'>";
		//添加列:数量
		var numTD = newTR.insertCell(5);
		numTD.innerHTML='<input id="materialNum" name="materialNum" class="input-mini" type="text"/>';
		//添加列:操作
		var materialClassTd = newTR.insertCell(6);
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
		for (var i = 2; i<ptab.rows.length; i++) {
			ptab.rows[i].id="materialTr_"+i;
			ptab.rows[i].cells[8].innerHTML = '<a href="javascript:void(0);" onclick="delRow('+i+')">删除</a>';
		}
		xuhao=ptab.rows.length;
	}
	//确认选择材料
	function saveSelect(){
		//材料计划页面 选择材料table
		var useType= window.opener.document.getElementById("useType").value;
	   	var outStoreTable = window.opener.document.getElementById("outStoreTable"+useType);
	   	//本页面 选择材料table
	   	var selectTable=document.getElementById("selectTable");
	   	//判断材料计划页面，是否存在 暂无数据，删除该行
   		if(outStoreTable.rows.length==2&&outStoreTable.rows[1].cells[0].innerHTML=='暂无数据！'){
			outStoreTable.deleteRow(1);
   		}
   		var tableLength=outStoreTable.rows.length;
	   	for (var i = 1; i<selectTable.rows.length; i++) {
	   		//页面选择后所填值  材料数量，备注，领料类型
	   		var materialNums = document.getElementsByName("materialNum");
	   		var remark = document.getElementsByName("remark");
	   		var type = document.getElementsByName("type");
	   		//将本页面 选择材料table 动态添加 到 材料计划页面
			var newTR =outStoreTable.insertRow();
			newTR.id="materialPlanTr_"+(tableLength+i-2);
			newTR.insertCell(0).innerHTML=selectTable.rows[i].cells[0].innerHTML;
			newTR.insertCell(1).innerHTML=selectTable.rows[i].cells[1].innerHTML;
			newTR.insertCell(2).innerHTML=selectTable.rows[i].cells[2].innerHTML;
			newTR.insertCell(3).innerHTML=selectTable.rows[i].cells[3].innerHTML;
			//newTR.insertCell(4).innerHTML=selectTable.rows[i].cells[4].innerHTML+"<input name='useType' type='hidden' value='"+useType+"'>";
			//循环将本页面填写值，赋值给父页面（材料计划页面）
			newTR.insertCell(4).innerHTML='<input id="storeNum" name="storeNum" class="input-mini" value="'+materialNums[i-1].value+'" type="text"/>';
			newTR.insertCell(5).innerHTML='<a href="javascript:void(0);" onclick="delRow('+(tableLength+i-2)+','+useType+')">删除</a>';
		}
		window.close();
	}
</script>
</head>
<body>
	<div class="container-fluid">
  		<div class="row-fluid">
    		<div class="span12">
    			<!-- iframe 材料选择列表 start -->
    			<div class="row-fluid">
					<iframe style="height:670px;width:100%;" frameborder="0" name="listFrame" id="listFrame" src="${pageContext.request.contextPath}/api/busmechanic/chooseMaterialInfo"></iframe>
				</div>
				<!-- iframe 材料选择列表 end -->
				<div class="row-fluid">
					<div class="span11">
						<!-- table 材料选择结果 start -->
				        <table id="selectTable" class="table table-striped table-bordered">
				          <tbody>
				            <tr>
							  <th>物料描述</th>
				              <th>物料组</th>
				              <th>物料编码</th>
							  <th>单位</th>
							  <th>参考价（元）</th>
							  <th>数量</th>
							  <th>操作</th>
				            </tr>
				            <tr>
								<td colspan="7" style="text-align: center;color:red;">暂无数据！</td>
							</tr>
				          </tbody>
				        </table>
					    <!-- table 材料选择结果 end -->
					    <!-- 操作按钮 start -->
						<div class="form-actions">
								<a class="btn btn-primary" href="javascript:void(0);"  onclick="saveSelect();" >确  定</a>
								<a class="btn"  href="javascript:void(0);" onclick="window.close();">关  闭</a>
						</div>
					</div>
					<!-- 操作按钮 end -->
				</div>
			</div>
		</div>	
	</div>
</body>	
</html>
