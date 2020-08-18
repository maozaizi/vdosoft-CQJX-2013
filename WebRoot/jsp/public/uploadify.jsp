<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<div class="page-header">
	<h4>上传文件</h4>
	<p><input type="file" name="uploadify" id="uploadify" /></p>
	<!--
	<div class="btn-group">
	 <a class="btn" href="javascript:jQuery('#uploadify').uploadify('upload','*')">开始上传</a>
	 <a class="btn" href="javascript:jQuery('#uploadify').uploadify('cancel','*')">取消所有上传</a>
	</div>
	-->
</div>

<div class="page-header">
	<table class="table table-striped table-bordered" id="fileTable">
		<thead>
			<tr>
				<th>文件名</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td style="text-align: center; color: red;" colspan="2">暂无数据！</td>
			</tr>
		</tbody>
	</table>
</div>

<script type="text/javascript">
		
		var xuhao=1;
		$(document).ready(function() {
            $("#uploadify").uploadify({
               	height         :  30,
               	buttonClass    :  'btn',
               	buttonText     :  '选择上传文件',
			   	swf            :  '${pageContext.request.contextPath}/js/uploadify/uploadify.swf',
				uploader       :  '${pageContext.request.contextPath}/ajaxapi/fileUpload/upload',
				width          :  120,
				auto           : true,//是否选取文件后自动上传
                multi          : true,//是否支持多文件上传
				onUploadSuccess: function (file, data, response){
                  	var jsondata =  JSON.parse(data);  //以JSON的形式处理文件信息包括：路径filepath,名称name等
                	var filelist = jsondata.filelist;
    				for(var i=0;i<filelist.length;i++){
    					insertRow(filelist[i].filename,filelist[i].filesize,filelist[i].fileext,filelist[i].filepath);
    				}
                   }
            });
        });
        
        function insertRow(name,size,type,filepath){
			var fileTable=document.getElementById( "fileTable" );
			//删除暂无数据行
	   		if(fileTable.rows.length==2&&fileTable.rows[1].cells[0].innerHTML=='暂无数据！'){
				fileTable.deleteRow(1);
				xuhao=1;
	   		}
			//添加行 
			var newTR = fileTable.insertRow(xuhao);
			newTR.id ="file_"+xuhao; 
			//添加列:文件名
			var fileNameTd = newTR.insertCell(0);
			fileNameTd.innerHTML="<a href='javascript:void(0);'  onclick=downLoad('"+filepath+"')>"+name+"</a><input type='hidden' name='docName' value='"+name+"'><input type='hidden' name='docType' value='"+type+"'><input type='hidden' name='docSize' value='"+size+"'><input type='hidden' name='docUrl' value='"+filepath+"'>";
			//添加列:操作
			var operTD = newTR.insertCell(1);
			operTD.innerHTML='<a href="javascript:void(0);" onclick="delRow('+xuhao+')">删除</a>';
			xuhao++;
		}
		function downLoad(file){
			window.open("${pageContext.request.contextPath}/api/fileUpload/downLoad?file="+file);
		}
		//删除行
		function delRow(rowId){
			var fileTable=document.getElementById("fileTable");
			var signItem = document.getElementById( "file_"+rowId);
			//获取将要删除的行的Index
			var rowIndex = signItem.rowIndex;
			fileTable.deleteRow(rowIndex);
			//selectTable列表，删除所有选择信息，显示暂无数据
			if(fileTable.rows.length==1){
				var newTR = fileTable.insertRow(1);
				var mark = newTR.insertCell(0);
				mark.colSpan=2;
				mark.style="text-align:center;color: red;";
				mark.innerHTML="暂无数据！";
			}
		}
</script>



