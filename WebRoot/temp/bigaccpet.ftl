<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style>
<!--
body{margin:0;font-size:10.5pt;font-family:SimSun,SimHei;color:black}
@page{size:841.9pt 595.3pt;margin:32.55pt 66pt 42.55pt 76pt}
h2{text-align:center;font-size:11pt;font-family:SimHei;line-height:12pt;font-weight:800}
div.sected{margin:0 auto;margin-top:32.9pt}
.sected td p{margin:0}
.std1{text-align:center;line-height:22.65pt}
.std2{text-align:left;padding:0 5.4pt;height:22.65pt;line-height:22.65pt}
.std3{text-align:center;padding:0 5.4pt;height:22.65pt;font-weight:800}
.s_p{text-indent:21.0pt;margin:5pt 0;line-height:225%}
.s_p span{line-height:150%}
.s_p1{line-height:128%;text-align:left}
.s_p1 span{margin-right:3pt;line-height:150%}
div.sected td{border-right:1px solid #000;border-bottom:1px solid #000}
-->
</style>
</head>
<body>
<h2>2013万元大件材料审批明细</h2>
<#assign wordwrap = "com.u2a.framework.util.freemarker.WordWrap"?new()>
<#assign contains = "com.u2a.framework.util.freemarker.StrContains"?new()>
<div class="sected">
<p style="text-align:left;">填表单位：机修公司<span style="float:right;">工卡号：${cardNo}</span></p>

	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="border-collapse:collapse;border:1px solid #000;">
		<tr>
			<td width="10%" class="std3"><h5>序号</h5></td>
			<td width="10%" class="std3"><h5>物资编码</h5></td>
			<td width="18%" class="std3"><h5>规格名称及型号</h5></td>
			<td width="4%" class="std3"><h5>单位</h5></td>
			<td width="8%" class="std3"><h5>单价</h5></td>
			<td width="4%" class="std3"><h5>需要数量</h5></td>
			<td width="8%" class="std3"><h5>金额</h5></td>
			<td width="6%" class="std3"><h5>计划时间</h5></td>
			<td width="6%" class="std3"><h5>要求到货时间</h5></td>
			<td width="10%" class="std3"><h5>申请单位</h5></td>
			<td width="16%" class="std3"><h5>备注</h5></td>
		</tr>
		<#list materiallist as ls>
		<tr>
			<td class="std2">${ls_index+1}</td>
			<td class="std2">${ls.materialCode}</td>
			<td class="std2">	
<#assign projectName_out=wordwrap(ls.materialDetail,6)/>${projectName_out}</td>
			<td class="std2">${ls.unity}</td>
			<td class="std2">${ls.estimatePrice}</td>
			<td class="std2">${ls.ratifyNum}</td>
			<td class="std2">${ls.totalPrice}</td>
			<td class="std2"></td>
			<td class="std2"></td>
			<td class="std2">${deptname}</td>
			<td class="std2"></td>
		</tr>
		</#list>		
					
		<tr>
			<td  width="20%" colspan="2" class="std1">领用材料原因</td>
			<td  width="80%" colspan="9" class="std2"><br />${bigRemark}<br /></td>
		</tr>
		<tr>
			<td colspan="2" class="std1">机修公司生产副经理意见</td>
			<td colspan="9" class="std2"><br />${scRemark}<br /></td>
		</tr>
		<tr>
			<td colspan="2" class="std1">机修公司经营副经理意见</td>
			<td colspan="9" class="std2"><br />${jyRemark}<br /></td>
		</tr>
		<tr>
			<td colspan="2" class="std1">机修公司经理意见</td>
			<td colspan="9" class="std2"><br />${jlRemark}<br /></td>
		</tr>
		<tr>
			<td colspan="2" class="std1">装备部意见</td>
			<td colspan="9" class="std2"><br /><br /></td>
		</tr>
		<tr>
			<td colspan="2" class="std1">总公司主管领导意见</td>
			<td colspan="9" class="std2"><br /><br /></td>
		</tr>
	</table>
	<p style="text-align:right;">打印日期：${printDate}</p>
</div>
</body>
</html>
