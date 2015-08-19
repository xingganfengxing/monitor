$(document).ready(function() {
    $(".section,.main").css("min-height", document.documentElement.clientHeight - 170);
});

function showLoad() {
	$("#load").css({ width:document.documentElement.clientWidth,height:document.documentElement.clientHeight,background:'#333',opacity:'0.6',position:'absolute',top:0,left:0,display:"block",'z-index':1001});
    $("#loadTxt").css("top", document.documentElement.clientHeight / 2);
    $("#loadTxt").css("left", document.documentElement.clientWidth / 2 - 100);
    $("#loadTxt").css({position:'absolute',display:'inline-block','z-index':1002});
    
}

function hideLoad() {
   $("#load").css("display", "none");
	$("#loadTxt").css("display", "none");
    var t = $("#dkt svg tspan:last");
    t.hide();
}


// 去除字符串两端空格 by Chen Hao
String.prototype.trim = function() {
    return this.replace(/(^\s*)|(\s*$)/g, "");
};
// 去除字符串做空格 by Chen Hao
String.prototype.ltrim = function() {
    return this.replace(/(^\s*)/g,"");
};
// 去除字符串右空格 by Chen Hao
String.prototype.rtrim = function() {
    return this.replace(/(\s*$)/g,"");
};

Date.prototype.format = function (format) {    
    var o = {    
        "M+": this.getMonth() + 1,    
        "d+": this.getDate(),    
        "h+": this.getHours(),    
        "m+": this.getMinutes(),    
        "s+": this.getSeconds(),    
        "q+": Math.floor((this.getMonth() + 3) / 3),    
        "S": this.getMilliseconds()    
    };
    if (/(y+)/.test(format)) {    
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));    
    }    
    for (var k in o) {    
        if (new RegExp("(" + k + ")").test(format)) {    
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));    
        }    
    }    
    return format;    
};
/**     
*转换日期对象为日期字符串     
* @param date 日期对象     
* @param isFull 是否为完整的日期数据,     
*               为true时, 格式如"2000-03-05 01:05:04"     
*               为false时, 格式如 "2000-03-05"     
* @return 符合要求的日期字符串     
*/      
function getSmpFormatDate(date, isFull) {    
    var pattern = "";    
    if (isFull == true || isFull == undefined) {    
        pattern = "yyyy-MM-dd hh:mm:ss";    
    } else {    
        pattern = "yyyy-MM-dd";    
    }    
    return getFormatDate(date, pattern);    
}    
/**     
*转换当前日期对象为日期字符串     
* @param date 日期对象     
* @param isFull 是否为完整的日期数据,     
*               为true时, 格式如"2000-03-05 01:05:04"     
*               为false时, 格式如 "2000-03-05"     
* @return 符合要求的日期字符串     
*/      

function getSmpFormatNowDate(isFull) {    
    return getSmpFormatDate(new Date(), isFull);    
}    
/**     
*转换long值为日期字符串     
* @param l long值     
* @param isFull 是否为完整的日期数据,     
*               为true时, 格式如"2000-03-05 01:05:04"     
*               为false时, 格式如 "2000-03-05"     
* @return 符合要求的日期字符串     
*/      

function getSmpFormatDateByLong(l, isFull) {    
    return getSmpFormatDate(new Date(l), isFull);    
}    
/**     
*转换long值为日期字符串     
* @param l long值     
* @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss     
* @return 符合要求的日期字符串     
*/      

function getFormatDateByLong(l, pattern) {    
    return getFormatDate(new Date(l), pattern);    
}    

/**     
*转换日期对象为日期字符串     
* @param l long值     
* @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss     
* @return 符合要求的日期字符串     
*/      
function getFormatDate(date, pattern) {    
    if (date == undefined) {    
        date = new Date();    
    }    
    if (pattern == undefined) {    
        pattern = "yyyy-MM-dd hh:mm:ss";    
    }    
    return date.format(pattern);    
}    
 

/**
 * 判断一个对象是不是json对象
 * @param obj
 * @returns {Boolean}
 */
function isJson(obj) {
	return typeof(obj) == "object" && Object.prototype.toString.call(obj).toLowerCase() == "[object object]" && !obj.length;
}
function closeLayer() {
	if(parent.LAYER){
		var index = parent.LAYER.getFrameIndex();
	    parent.LAYER.close(index);
	}else{
		window.opener=null;					//防止弹出确认提示
		window.open('', '_self', '');
		window.close(); 
	}
}

/**
 * 将数据转化为带千分符的形式
 * @param s 数字
 * @param isFloat 是否有小数
 * @returns
 */
function formatNumber(s, isFloat) {
    s += "";
    if(/[^0-9\.]/.test(s)) {
        return "invalid value";
    }
    s = s.replace(/^(\d*)$/, "$1.");
    s = (s + "0").replace(/(\d*\.\d)\d*/, "$1");
    s = s.replace(".", ",");
    var re = /(\d)(\d{3},)/;
    while(re.test(s)) {
        s = s.replace(re, "$1,$2");
    }
    s = s.replace(/,(\d)$/, ".$1");
    if(!isFloat) {
        s = s.replace(/([0-9,]*).\d*$/, "$1");
    }
    return s.replace(/^\./, "0.");
}

//比较两个日期大小
function compareDate(d1,d2)
{
	var date1 = new Date(d1.replace(/-/g,"\/"));
	var date2 = new Date(d2.replace(/-/g,"\/"));
	return date1.getTime() - date2.getTime();
}
//全局的ajax访问，处理ajax请求sesion超时的情况
$.ajaxSetup({
	contentType:"application/x-www-form-urlencoded;charset=utf-8",
	complete:function(XMLHttpRequest,textStatus){
		//通过XMLHttpRequest取得响应头，sessionstatus，
		var sessionstatus=XMLHttpRequest.getResponseHeader("sessionstatus");
		if(sessionstatus=="timeout"){
			//如果超时就处理 ，指定要跳转的页面
			window.location.replace("/");
		}
	}
})