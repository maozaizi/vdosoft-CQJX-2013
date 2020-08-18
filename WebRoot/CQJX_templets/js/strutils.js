String.prototype.trim = function() {  
	return this.replace(/(^\s*)|(\s*$)/g, '');
}
function ErrMsg(ErrInfo){
  if (ErrInfo.trim()!="")
    alert(ErrInfo);
  window.history.go(-1);
}
function InfoMsg(Info,Url){  
  if (Info.trim()!="")
    alert(Info);
  if (Url!="")
    window.location.href=Url;
}
function showDelReason(){
	document.getElementById('delete_reason').style.display='block';
	document.getElementById('fade').style.display='block'; 
}
function closeDelReason(){
    document.getElementById('delete_reason').style.display='none';
	document.getElementById('fade').style.display='none';
}