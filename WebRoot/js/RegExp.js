  //给个焦点
function setfocus(val) {
	document.getElementById(val).focus();
}
   //得到字符串的绝对长度
function getlength(str) {
	return str.match(/[^ -~]/g) == null ? str.length : str.length + str.match(/[^ -~]/g).length;
} 


 //光标位置：
function endpoint(val) {
	var obj = document.getElementById(val);
	var rng = obj.createTextRange();
	rng.setEndPoint("StartToEnd", rng);
	rng.select();
}
//是否为空
function ifnull(val) {
	var renull = new RegExp(/^[ ]*$/);
	if (renull.test(val) || val == null || val == undefined) {
		return true;
	} else {
		return false;
	}
}
//是否数字
function isnumber(val) {
	var renumber = new RegExp(/^[0-9]+\.?[0-9]{0,3}$/);
	if (renumber.test(val)) {
		return true;
	} else {
		return false;
	}
}

// 判断输入是否是一个整数
function isint(str) {
	var result = str.match(/^(-|\+)?\d+$/);
	if (result == null) {
		return false;
	}
	return true;
}


//验证带汉字的用户名
function usernamevalidate(val) {
	var name = new RegExp(/^(\w|[\u4e00-\u9fa5])*$/);
	if (name.test(val)) {
		return true;
	}
	return false;
}

//验证用户名
function usernamevali(val, int1, int2) {
	var par = "/^\\w{" + int1 + "," + int2 + "}$/";
	if (!(eval(par).test(val))) {
		alert("\u7528\u6237\u540d\u4e0d\u5408\u6cd5\uff01" + int1 + "-" + int2 + "\u4f4d\uff0c\u5b57\u6bcd\uff0c\u6570\u5b57\uff0c\u4e0b\u5212\u7ebf");
		return false;
	}
	return true;
}
//验证用户名
function usernamevali2(str) {
	if (getlength(str) > 15 || getlength(str) < 3) {
		alert("\u7528\u6237\u540d\u957f\u5ea6\u4e0d\u5408\u6cd5\uff01\u7edd\u5bf9\u957f\u5ea63-15\u4f4d(\u6c49\u5b57\u7b97\u4e24\u4f4d)");
		return false;
	}
	if (!/^[\u4e00-\u9fa5\w]+$/.test(str)) {
		alert("\u7528\u6237\u540d\u4e0d\u5408\u6cd5\uff013-15\u4f4d\uff0c\u5b57\u6bcd\uff0c\u6570\u5b57\uff0c\u4e0b\u5212\u7ebf\uff0c\u6c49\u5b57(\u6c49\u5b57\u7b97\u4e24\u4f4d)");
		return false;
	}
	return true;
}

//验证用户名
function usernamevaliLogin(val) {
	if (!/^\w{3,16}$/.test(val)) {
		alert("\u7528\u6237\u540d\u4e0d\u5408\u6cd5\uff013-16\u4f4d\uff0c\u5b57\u6bcd\uff0c\u6570\u5b57\uff0c\u4e0b\u5212\u7ebf");
		return false;
	}
	return true;
}
//验证用户名
function usernamevaliLogin2(str) {
	if (getlength(str) > 16 || getlength(str) < 3) {
		alert("\u7528\u6237\u540d\u957f\u5ea6\u4e0d\u5408\u6cd5\uff01\u7edd\u5bf9\u957f\u5ea63-16\u4f4d(\u6c49\u5b57\u7b97\u4e24\u4f4d)");
		return false;
	}
	if (!/^[\u4e00-\u9fa5\w]+$/.test(str)) {
		alert("\u7528\u6237\u540d\u4e0d\u5408\u6cd5\uff013-16\u4f4d\uff0c\u5b57\u6bcd\uff0c\u6570\u5b57\uff0c\u4e0b\u5212\u7ebf\uff0c\u6c49\u5b57(\u6c49\u5b57\u7b97\u4e24\u4f4d)");
		return false;
	}
	return true;
}

//验证url
function urlValidate(url) {
	var obj = new RegExp(/^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/);
	if (!obj.test(url)) {
		return false;
	}
	return true;
}

//验证固定电话
function phoneValidate(phone) {
	var obj = new RegExp(/^(\d{3,4}\-)?\d{7,8}$/i);
	var obj1 = new RegExp(/^0(([1-9]\d)|([3-9]\d{2}))\d{8}$/);
	if (!obj.test(phone) && !obj1.test(phone)) {
		return false;
	}
	return true;
}

//去掉空格
function trim(str) {
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

//验证手机号码
function mobileValidate(mobile) {
	var obj = new RegExp(/^((\(\d{3}\))|(\d{3}\-))?13\d{9}$/);
	if (!obj.test(mobile)) {
		return false;
	}
	return true;
}
//验证手机号码
function mobile_validate(mobile) {
	var obj = new RegExp(/(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/);
	if (!obj.test(mobile)) {
		return false;
	}
	return true;
}
//验证邮政编码
function isZipCode(zipCode) {
	//var obj = new RegExp(/[1-9]d{5}(?!d)/);
	var obj = new RegExp(/\d{5}/);
	if (!obj.test(zipCode)) {
		return false;
	}
	return true;
}

//验证密码
function pwdValidate(val) {
	if (!/^.{6,16}$/.test(val)) {
		alert("\u5bc6\u7801\u4e0d\u5408\u6cd5\uff01(6-16\u4f4d)");
		return false;
	}
	return true;
}

// 判断输入是否是有效的长日期格式 - "YYYY-MM-DD HHSS" || "YYYY/MM/DD HHSS"
function isDateTime(str) {
	var obj = new RegExp(/^(\d{4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2})(\d{1,2})(\d{1,2})$/);
	if (!obj.test(str)) {
		return false;
	}
	var d = new Date(result[1], result[3] - 1, result[4], result[5], result[6], result[7]);
	return (d.getFullYear() == result[1] && (d.getMonth() + 1) == result[3] && d.getDate() == result[4] && d.getHours() == result[5] && d.getMinutes() == result[6] && d.getSeconds() == result[7]);
}
function isDateTime2(str) {
	var obj = new RegExp(/^\d{4}-(?:0\d|1[0-2])-(?:[0-2]\d|3[01])( (?:[01]\d|2[0-3])\:[0-5]\d\:[0-5]\d)?$/);
	if (obj.test(str)) {
		return true;
	} else {
		return false;
	}
}

// 检查是否为 YYYY-MM-DD || YYYY/MM/DD 的日期格式
function isdate(str) {
	var obj = new RegExp(/^(\d{4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
	if (!obj.test(str)) {
		return false;
	}
	var d = new Date(result[1], result[3] - 1, result[4]);
	return (d.getFullYear() == result[1] && d.getMonth() + 1 == result[3] && d.getDate() == result[4]);
}

//校验密码：只能输入6-20个字母、数字、下划线   
function isPasswd(pwd) {
	var patrn = /^(\w){6,20}$/;
	if (!patrn.exec(pwd)) {
		return false;
	}
	return true;
}    

//验证email
function email(val) {
	if (!ifnull(val)) {
		var eexp = new RegExp(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/);
		if (!eexp.test(val)) {
			return false;
		} else {
			return true;
		}
	} else {
		return false;
	}
}
//验证密码 6-16位
function checkpassword(obj1, obj2, reobj2) {
	if (obj1.value == "" || obj1.value.length < 6 || obj1.value.length > 16) {
		alert("\u8bf7\u8f93\u5165\u539f\u5bc6\u7801 (6-16\u4f4d)");
		obj1.focus();
		return false;
	}
	if (obj2.value == "" || obj2.value.length < 6 || obj2.value.length > 16) {
		alert("\u8bf7\u8f93\u5165\u65b0\u5bc6\u7801 (6-16\u4f4d)");
		obj2.focus();
		return false;
	}
	if (reobj2.value == "") {
		alert("\u8bf7\u91cd\u590d\u5bc6\u7801!");
		reobj2.focus();
		return false;
	}
	if (obj2.value != reobj2.value) {
		alert("\u5bc6\u7801\u4e0d\u4e00\u81f4!");
		return false;
	}
	return true;
}
 
//验证密码 6-16位
function passwordvali(obj1, obj2) {
	if (obj1.value == "" || obj1.value.length < 6 || obj1.value.length > 16) {
		alert("\u8bf7\u8f93\u5165\u5408\u6cd5\u7684\u5bc6\u7801! (6-16\u4f4d)");
		obj1.focus();
		return false;
	}
	if (obj2.value == "") {
		alert("\u8bf7\u91cd\u590d\u5bc6\u7801!");
		obj2.focus();
		return false;
	}
	if (obj1.value != obj2.value) {
		alert("\u5bc6\u7801\u4e0d\u4e00\u81f4!");
		return false;
	}
	return true;
}

//验证身份证号码
function isIdCardNo(num) {
	if (isNaN(num)) {
		//alert("u8f93u5165u7684u4e0du662fu6570u5b57uff01");
		return false;
	}
	var len = num.length, re;
	if (len == 15) {
		re = new RegExp(/^(\d{6})()?(\d{2})(\d{2})(\d{2})(\d{3})$/);
	} else {
		if (len == 18) {
			re = new RegExp(/^(\d{6})()?(\d{4})(\d{2})(\d{2})(\d{3})(\d)$/);
		} else {
			//alert("u8f93u5165u7684u6570u5b57u4f4du6570u4e0du5bf9uff01");
			return false;
		}
	}
	var a = num.match(re);
	if (a != null) {
		if (len == 15) {
			var D = new Date("19" + a[3] + "/" + a[4] + "/" + a[5]);
			var B = D.getYear() == a[3] && (D.getMonth() + 1) == a[4] && D.getDate() == a[5];
		} else {
			var D = new Date(a[3] + "/" + a[4] + "/" + a[5]);
			var B = D.getFullYear() == a[3] && (D.getMonth() + 1) == a[4] && D.getDate() == a[5];
		}
		if (!B) {
			//alert("u8f93u5165u7684u8eabu4efdu8bc1u53f7   " + a[0] + "   u91ccu51fau751fu65e5u671fu4e0du5bf9uff01");
			return;
			false;
		}
	}
	return true;
}

// 判断单选框或复选框是否有被选中
function isChecked(obj) {
	var checked = false;
	for (var i = 0; i < obj.length; i++) {
		if (obj[i].checked) {
			checked = true;
		}
	}
	return checked;
}
    
//验证图片    
function checkImgSize(val) {
	var imgSize = 1024 * 1024 * 2; //最大2M
	if (!ifnull(val)) {
        //检测类型
		if (!/^.+\.(jpg|bmp|png|gif)$/.test(val.toLowerCase())) {
			alert("\u53ea\u652f\u6301\u4e0a\u4f20jpg, bmp, png, gif  \u683c\u5f0f\u7684\u56fe\u7247 ");
			return false;
		}
		/* 
      var img = new Image();
      img.dynsrc=val; 
                if(img.fileSize > imgSize){
                 alert("图片大小超出了 "+imgSize/1024/1024+"M 的最大上传限制!  ");
                    return false;
                }else if(img.fileSize<=0){
                    alert("无效的路径！");
                    return false;
                }
                
       return true;
        */
	}
	return true;
}

  //图片缩放：
function imgcon(width, height, val) {
	var w = val.width;
	var h = val.height;
	if (w > width) {
		h = width / w * h;
		w = width;
		if (h > height) {
			w = height / h * w;
			h = height;
		}
	}
	if (h > height) {
		w = height / h * w;
		h = height;
		if (w > width) {
			h = width / w * h;
			w = width;
		}
	}
	val.width = w;
	val.height = h;
}
//验证文件    
function checkFileType(val, types) {
	if (!ifnull(val)) {
        //检测类型
		var expstr = "/^.+.(";
		var array = types.split(",");
		for (var i = 0; i < array.length; i++) {
			expstr = expstr + array[i];
			if (i < array.length - 1) {
				expstr = expstr + "|";
			}
		}
		expstr = expstr + ")$/";
		var exp = new RegExp(eval(expstr));
		if (!exp.test(val.toLowerCase())) {
			alert("\u53ea\u652f\u6301\u4e0a\u4f20 " + types + "  \u683c\u5f0f\u7684\u6587\u4ef6 ");
			return false;
		} else {
			return true;
		}
	} else {
		alert("\u8bf7\u9009\u62e9\u4e0a\u4f20\u7684\u6587\u4ef6\uff01");
		return false;
	}
	return false;
}

//时钟：
var timerID = null;
var timerRunning = false;
function MakeArray(size) {
	this.length = size;
	for (var i = 1; i <= size; i++) {
		this[i] = "";
	}
	return this;
}
function stopclock() {
	if (timerRunning) {
		clearTimeout(timerID);
	}
	timerRunning = false;
}
function showtime() {
	var now = new Date();
	var year = now.getYear();
	if (year < 1900) {
		year = year + 1900;
	}
	var month = now.getMonth() + 1;
	var date = now.getDate();
	var hours = now.getHours();
	var minutes = now.getMinutes();
	var seconds = now.getSeconds();
	var day = now.getDay();
	Day = new MakeArray(7);
	Day[0] = "\u661f\u671f\u5929";
	Day[1] = "\u661f\u671f\u4e00";
	Day[2] = "\u661f\u671f\u4e8c";
	Day[3] = "\u661f\u671f\u4e09";
	Day[4] = "\u661f\u671f\u56db";
	Day[5] = "\u661f\u671f\u4e94";
	Day[6] = "\u661f\u671f\u516d";
	var timeValue = "";
	timeValue += year + "-";
	timeValue += ((month < 10) ? "0" : "") + month + "-";
	timeValue += date + " ";
	timeValue += hours;
	timeValue += ((minutes < 10) ? ":0" : ":") + minutes;
	timeValue += ((seconds < 10) ? ":0" : ":") + seconds + "  ";
	timeValue += (Day[day]) + "  ";
	document.getElementById("clock").innerHTML = timeValue;
	timerID = setTimeout("showtime()", 1000);
	timerRunning = true;
}
function startclock() {
	stopclock();
	showtime();
} 

//剩余字数
function charcount(id, number, name) {
	var count = document.getElementById(name).value.length;
	var lastcount = number - count;
	if (lastcount < 0) {
		document.getElementById(id).innerHTML = "<span style='color:red'>\u5df2\u8d85\u51fa\u6700\u5927\u5b57\u6570!&nbsp;" + number + "&nbsp;\u5b57</span>";
	} else {
		document.getElementById(id).innerHTML = "\u5269\u4f59\u5b57\u6570:&nbsp;<span style='color:green'>" + lastcount + "</span>&nbsp;\u5b57&nbsp;(\u5b9e\u9645\u5185\u5bb9\u4ee5\u6e90\u7801\u4e3a\u51c6)";
	}
}

//Ajax验证验证码是否正确
var http_request = false;
function send_request(url) {
			//初始化、指定处理函数、发送请求的函数
	http_request = false;
				//开始初始化XMLHttpRequest对象
	if (window.XMLHttpRequest) { 
				//Mozilla 浏览器
		http_request = new XMLHttpRequest();
		if (http_request.overrideMimeType) {
					//设置MiME类别
			http_request.overrideMimeType("text/xml");
		}
	} else {
		if (window.ActiveXObject) { 
				// IE浏览器
			try {
				http_request = new ActiveXObject("Msxml2.XMLHTTP");
			}
			catch (e) {
				try {
					http_request = new ActiveXObject("Microsoft.XMLHTTP");
				}
				catch (e) {
				}
			}
		}
	}
	if (!http_request) { 
				// 异常，创建对象实例失败
		window.alert("\u4e0d\u80fd\u521b\u5efaXMLHttpRequest\u5bf9\u8c61\u5b9e\u4f8b.");
		return false;
	}
	http_request.onreadystatechange = processRequest;
				// 确定发送请求的方式和URL以及是否同步执行下段代码
	http_request.open("post", url, true);
	http_request.send(null);
}
			// 处理返回信息的函数
function processRequest() {
	if (http_request.readyState == 4) { // 判断对象状态
		if (http_request.status == 200) { // 信息已经成功返回，开始处理信息
			var res = http_request.responseText;
			callback(res);
		}
	}
}
function addFavo(title, url) {
	if (document.all) {
		try {
			window.external.addFavorite(url, title);
		}
		catch (e1) {
			try {
				window.external.addToFavoritesBar(url, title);
			}
			catch (e2) {
				alert("\u52a0\u5165\u6536\u85cf\u5931\u8d25,\u8bf7\u60a8\u4f7f\u7528ctrl+d\u8fdb\u884c\u624b\u5de5\u52a0\u5165");
			}
		}
	} else {
		if (window.external) {
			window.sidebar.addPanel(title, url, "");
		} else {
			alert("\u52a0\u5165\u6536\u85cf\u5931\u8d25,\u8bf7\u60a8\u4f7f\u7528ctrl+d\u8fdb\u884c\u624b\u5de5\u52a0\u5165");
		}
	}
}

//设为主页兼容firefox
function setHomepage(url) {
	if (document.all) {
		document.body.style.behavior = "url(#default#homepage)";
		document.body.setHomePage(url);
	} else {
		if (window.sidebar) {
			if (window.netscape) {
				if (confirm("\u662f\u5426\u5c06\u201chttp://www.xdzinfo.com\u201d\u8bbe\u4e3a\u4e3b\u9875?")) {
					try {
						netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
					}
					catch (e) {
						alert("\u8be5\u64cd\u4f5c\u88ab\u6d4f\u89c8\u5668\u62d2\u7edd\uff0c\u5982\u679c\u60f3\u542f\u7528\u8be5\u529f\u80fd\uff0c\u8bf7\u5728\u5730\u5740\u680f\u5185\u8f93\u5165 about:config,\u7136\u540e\u5c06\u9879 signed.applets.codebase_principal_support \u503c\u8be5\u4e3atrue");
					}
				}
			}
			var prefs = Components.classes["@mozilla.org/preferences-service;1"].getService(Components.interfaces.nsIPrefBranch);
			prefs.setCharPref("browser.startup.homepage", url);
		}
	}
}
/*
	检测是否为空，最大值，和是否为数字
*/
function checkInput(config) {
	var filed = $("#" + config.filed);
	if (ifnull(filed.val())) {
		filed.addClass("prompt_text");
		alert(config.nullMsg);
		return false;
	} else {
		if (config.ifNum) {
			if (!isnumber(filed.val())) {
				filed.addClass("prompt_text");
				alert(config.numMsg);
				return false;
			} else {
				filed.removeClass();
				return true;
			}
		} else {
			if (filed.val().length > config.maxValue) {
				filed.addClass("prompt_text");
				alert(config.maxMsg);
				return false;
			} else {
				filed.removeClass();
				return true;
			}
		}
	}
}
function hasClass(elm, className) {
	var classes = elm.className.split(" ");
	for (var a in classes) {
		if (classes[a] == className) {
			return a;
		}
	}
	return -1;
}
function addClass(elm, newClass) {
	var classes = elm.className.split(" ");
	var classIndex = hasClass(elm, newClass);
	if (classIndex == -1) {
		classes.push(newClass);
	}
	elm.className = classes.join(" ");
}
function delClass(elm, className) {
	var classes = elm.className.split(" ");
	var classIndex = hasClass(elm, className);
	if (classIndex != -1) {
		classes.splice(classIndex, 1);
	}
	elm.className = classes.join(" ");
}
/**
验证页面所有input必填、数字、电话
参数divId：获取div对象
需要验证input：
参数1.check="1"  自定义属性 必填 
2.checktype="number" 自定义属性 数字
3.checktype="3" 自定义属性 电话
*/
function checkInputText(divId) {
var selectlist = divId.getElementsByTagName("select");
var textarea = divId.getElementsByTagName("textarea");
var list = divId.getElementsByTagName("input");
         //对表单中所有的input进行遍历
	var result = "";
	var selectfiled;
	for (var i = 0; i < textarea.length; i++) {
	if(!ifnull(textarea[i].attributes["check"])){
	if (ifnull(textarea[i].value)) {
		result += "1";
	addClass(textarea[i], "textarea_null");
	}
	}
	}
	
	for (var i = 0; i < selectlist.length; i++) {
		selectfiled = selectlist[i];
		delClass(selectfiled, "select_null");
      		 //必填验证
		if ((!ifnull(selectfiled.attributes["check"])) && (selectfiled.attributes["check"].nodeValue == "1")) {
			if (ifnull(selectfiled.value)) {
				result += "1";
				addClass(selectfiled, "select_null");
			}
		}
	}
	var filed;
         //对表单中所有的input进行遍历
	for (var i = 0; i < list.length; i++) {
		filed = list[i];
		delClass(filed, "prompt_text"); 
		delClass(filed, "input_null"); 
      		 //必填验证
		if ((!ifnull(filed.attributes["check"])) && (filed.attributes["check"].nodeValue == "1")) {
			if (ifnull(filed.value)) {
				result += "1";
				addClass(filed, "input_null");
			}
		}
		if (!ifnull(filed.attributes["checktype"])) {
            // 判断输入是否是一个整数
			if ((filed.attributes["checktype"].nodeValue == "int")) {
				if (!ifnull(filed.value)) {
					if (!isint(filed.value)) {
						result += "2";
						addClass(filed, "prompt_text");
					}
				}
			}
            //数字验证
			if ((filed.attributes["checktype"].nodeValue == "number")) {
				if (!ifnull(filed.value)) {
					if (!isnumber(filed.value)) {
						result += "2";
						addClass(filed, "prompt_text");
					}
				}
			}
			
			
            //电话验证
			if ((filed.attributes["checktype"].nodeValue == "phone")) {
				if (!ifnull(filed.value)) {
					if (!phoneValidate(filed.value) && !mobile_validate(filed.value)) {
						result += "2";
						addClass(filed, "prompt_text");
					}
				}
			}
            //邮编验证
			if ((filed.attributes["checktype"].nodeValue == "zipCode")) {
				if (!ifnull(filed.value)) {
					if (!isZipCode(filed.value)) {
						result += "2";
						addClass(filed, "prompt_text");
					}
				}
			}
            	//email验证
			if ((filed.attributes["checktype"].nodeValue == "email")) {
				if (!ifnull(filed.value)) {
					if (!email(filed.value)) {
						result += "2";
						addClass(filed, "prompt_text");
					}
				}
			}
            	//身份证号码验证
			if ((filed.attributes["checktype"].nodeValue == "idCardNo")) {
				if (!ifnull(filed.value)) {
					if (!isIdCardNo(filed.value)) {
						result += "2";
						addClass(filed, "prompt_text");
					}
				}
			}
            	//日期验证
			if ((filed.attributes["checktype"].nodeValue == "date")) {
				if (!ifnull(filed.value)) {
					if (!isdate(filed.value)) {
						result += "2";
						addClass(filed, "prompt_text");
					}
				}
			}
            	//验证时间yyyy-MM-dd hh:mm:ss
			if ((filed.attributes["checktype"].nodeValue == "datetime")) {
				if (!ifnull(filed.value)) {
					if (!isDateTime2(filed.value)) {
						result += "2";
						addClass(filed, "prompt_text");
					}
				}
			}
		}
	}
	return result;
}
//function checkSelectText(divId) {
//	var list = divId.getElementsByTagName("select");
//         //对表单中所有的input进行遍历
//	var result = "";
//	var filed;
//	for (var i = 0; i < list.length; i++) {
//		filed = list[i];
//		delClass(filed, "prompt_text");
//     		 //必填验证
//		if ((!ifnull(filed.attributes["check"])) && (filed.attributes["check"].nodeValue == "1")) {
//			if (ifnull(filed.value)) {
//				result = "1";
//				addClass(filed, "prompt_text");
//			}
//		}
//	}
//	return result;
//}
function checkText(divId) {
	var bl1 = checkInputText(divId);
	//var bl2 = checkSelectText(divId);
	var context = "";
	var result = "";
	if (bl1.indexOf("1")!= -1){
		context = "\n红色提示表示需要填写的项目未填写";
	}
	if (bl1.indexOf("2")!= -1){
		context += "\n黄色提示表示信息填写格式不正确";
	}
	if(context != ""){
		result = "填写不完整或格式不正确"+context;
	}
	return result;
}

