
// 禁用Select2控件多选自动排序
function disableSelect2MultiSelectSort($obj){
	$obj.on("select2:select", function (evt) {
		var element = evt.params.data.element;
		var $element = $(element);

		$element.detach();
		$(this).append($element);
		$(this).trigger("change");
	});
}

// 初始化Select2多选，解决初始化赋值自动排序问题
function initSelect2Value(objParent, select2, select2InitValue){
	select2.val(select2InitValue).trigger("change");

	var ul = objParent.find('.select2-selection__rendered');
	var liList = ul.find("li")
	var liListTemp = [];
	for(var i = 0; i < select2InitValue.length; i++){
		for(var j = 0; j < liList.length; j++){
			var li = liList[j];
			removeElement(li); // li.remove()的兼容写法
			var li_value = li.title.split('（')[0];
			if(li_value == select2InitValue[i]){
				liListTemp.push(li);
			}
//			if(li.title.indexOf(select2InitValue[i]) != -1){
//				liListTemp.push(li);
//			}
		}
	}
	liListTemp.push(liList[liList.length - 1]);
	ul.append(liListTemp);
}


// IE、360不兼容jQeury remove 使用js原生方法
function removeElement(_element){
    var _parentElement = _element.parentNode;
    if(_parentElement){
           _parentElement.removeChild(_element);
    }
}


// 获取url参数
function getUrlParam(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null) 
	return unescape(r[2]); 
	return null; 
}

// 页面跳转（避免IE浏览器下无法获取referer问题）
function go2URL(url){
	if (/MSIE (\d+\.\d+);/.test(navigator.userAgent) || /MSIE(\d+\.\d+);/.test(navigator.userAgent)){
	    var referLink = document.createElement('a');
	    referLink.href = url;
	    document.body.appendChild(referLink);
	    referLink.click();
	} else {
	    location.href = url;
	}
}

/*
判断浏览器名称和版本
目前只能判断:ie/firefox/chrome/opera/safari

浏览器内核UA:$.getBrowser('ua');或$.getBrowser('UA');
浏览器内核名称:$.getBrowser('name');或$.getBrowser();
浏览器内核版本:$.getBrowser('version');
浏览器外壳名称:$.getBrowser('shell');
*/
(function($)
{
	$.extend(
	{
		getBrowser:function(name)
		{
			var NV = {};
			var UA = navigator.userAgent.toLowerCase();
			try
			{
				NV.name=!-[1,]?'ie':
				(UA.indexOf("firefox")>0)?'firefox':
				(UA.indexOf("chrome")>0)?'chrome':
				window.opera?'opera':
				window.openDatabase?'safari':
				'unkonw';
			}catch(e){};
			try
			{
				NV.version=(NV.name=='ie')?UA.match(/msie ([\d.]+)/)[1]:
				(NV.name=='firefox')?UA.match(/firefox\/([\d.]+)/)[1]:
				(NV.name=='chrome')?UA.match(/chrome\/([\d.]+)/)[1]:
				(NV.name=='opera')?UA.match(/opera.([\d.]+)/)[1]:
				(NV.name=='safari')?UA.match(/version\/([\d.]+)/)[1]:
				'0';
			}catch(e){};
			try
			{
				NV.shell=(UA.indexOf('360ee')>-1)?'360极速浏览器':
				(UA.indexOf('360se')>-1)?'360安全浏览器':
				(UA.indexOf('se')>-1)?'搜狗浏览器':
				(UA.indexOf('aoyou')>-1)?'遨游浏览器':
				(UA.indexOf('theworld')>-1)?'世界之窗浏览器':
				(UA.indexOf('worldchrome')>-1)?'世界之窗极速浏览器':
				(UA.indexOf('greenbrowser')>-1)?'绿色浏览器':
				(UA.indexOf('qqbrowser')>-1)?'QQ浏览器':
				(UA.indexOf('baidu')>-1)?'百度浏览器':
				'未知或无壳';
			}catch(e){}
			switch(name)
			{
				case 'ua':
				case 'UA':br=UA;break;
				case 'name':br=NV.name;break;
				case 'version':br=NV.version;break;
				case 'shell':br=NV.shell;break;
				default:br=NV.name;
			}
			return br;
		}
	});
})(jQuery);

var yzmNum = 0;
var yzmNumSending = 0;
var yzmLockNum = 0;
var majorNum = 0;
var majorNumSending = 0;
var majorLockNum = 0;
var auditNum = 0;
var sendNum = 0;
var ordinaryNum = 0;
var ordinaryNumSending = 0;
var ordinaryLockNum = 0;


$(function(){
    var tip = createTip();
    //hover 跟随鼠标弹出框
    $(".jsms-hover").mousemove(function(e){
        e = e || event;
        var title = $(this).attr("data-hover");
        tip.innerHTML = title;
        tip.style.color = "#FFF";
        tip.style.display = 'block';
        tip.style.position = 'absolute';
        tip.style.background="rgba(0,0,0,.6)";
        tip.style.padding="5px 10px";
        tip.style.left = e.pageX + 10 + 'px';
        tip.style.top = e.pageY + 15 + 'px';
    })
    $(".jsms-hover").mouseleave(function(e){
        tip.style.display = "none";
    })
    function createTip(){
        var div = document.createElement('div');
        div.className = 'tip';
        return document.body.appendChild(div);
    }

    //自动轮寻， 验证码 审核 审核条数
    if($(".yzmNum").size() > 0 || $(".majorNum").size() > 0 || $(".ordinaryNum").size() > 0){
        getAuditNum();
        var _getAuditNum = window.setInterval(function () {// 初始化定时器
            getAuditNum();
        }, 5000)

    }
    function getAuditNum() {
        $.ajax({
            type: "post",
            url: "/smsaudit/getKindsAuditNum",
            success: function (data) {

                yzmNum = (data.yzmNum) || 0;
                yzmNumSending = (data.yzmNumSending) || 0;
                yzmLockNum = (data.yzmLockNum) || 0;
                majorNum = data.majorNum || 0;
                majorNumSending = data.majorNumSending || 0;
                majorLockNum = data.majorLockNum || 0;
                auditNum = data.auditNum || 0;
                sendNum = data.sendNum || 0;
                ordinaryNum = data.ordinaryNum || 0;
                ordinaryNumSending = data.ordinaryNumSending || 0;
                ordinaryLockNum = data.ordinaryLockNum || 0;

                var span_yzmNum = $(".yzmNum").find("span");
                if(span_yzmNum.size() > 0){
                    span_yzmNum.text("（" + yzmNum + "）");
                } else {
                    $(".yzmNum").append("<span style='color:white;'></span>");
                    $(".yzmNum").find("span").text("（" + yzmNum + "）");
                }
                var span_majorNum = $(".majorNum").find("span");
                if(span_majorNum.size() > 0){
                    span_majorNum.text("（" + majorNum + "）");
                } else {
                    $(".majorNum").append("<span style='color:white;'></span>");
                    $(".majorNum").find("span").text("（" + majorNum + "）");
                }
                var span_ordinaryNum = $(".ordinaryNum").find("span");
                if(span_ordinaryNum.size() > 0){
                    span_ordinaryNum.text("（" + ordinaryNum + "）");
                } else {
                    $(".ordinaryNum").append("<span style='color:white;'></span>");
                    $(".ordinaryNum").find("span").text("（" + ordinaryNum + "）");
                }

            }
        });
    }

})

var DateFormat = function(bDebug){    
    this.isDebug = bDebug || false;    
    this.curDate = new Date();    
};

DateFormat.prototype = {
    //定义一些常用的日期格式的常量     
    DEFAULT_DATE_FORMAT: 'yyyy-MM-dd',    
    DEFAULT_MONTH_FORMAT: 'yyyy-MM',    
    DEFAULT_YEAR_FORMAT: 'yyyy',    
    DEFAULT_TIME_FORMAT: 'HH:mm:ss',    
    DEFAULT_DATETIME_FORMAT: 'yyyy-MM-dd HH:mm:ss',    
    DEFAULT_YEAR: 'YEAR',    
    DEFAULT_MONTH: 'MONTH',    
    DEFAULT_DATE: 'DATE',    
    DEFAULT_HOUR: 'HOUR',    
    DEFAULT_MINUTE: 'MINUTE',    
    DEFAULT_SECOND: 'SECOND',    
        
    /**  
     * 根据给定的日期时间格式，格式化当前日期  
     * @param strFormat 格式化字符串， 如："yyyy-MM-dd" 默认格式为：“yyyy-MM-dd HH:mm:ss”  
     * @return 返回根据给定格式的字符串表示的时间日期格式<br>  
     *         如果传入不合法的格式，则返回日期的字符串格式{@see Date#toLocaleString()}  
     */    
     formatCurrentDate: function(strFormat){    
       try{    
          var tempFormat = strFormat == undefined? this.DEFAULT_DATETIME_FORMAT: strFormat;    
          var dates = this.getDateObject(this.curDate);    
          if(/(y+)/.test(tempFormat)){debugger;    
             var fullYear = this.curDate.getFullYear() + '';    
             var year = RegExp.$1.length == 4? fullYear: fullYear.substr(4 - RegExp.$1.length);    
             tempFormat = tempFormat.replace(RegExp.$1, year);    
          }    
          for(var i in dates){    
             if(new RegExp('(' + i + ')').test(tempFormat)){    
                var target = RegExp.$1.length == 1? dates[i]: ('0' + dates[i]).substr(('' + dates[i]).length - 1);    
                tempFormat = tempFormat.replace(RegExp.$1, target);    
             }    
          }    
          return tempFormat === strFormat? this.curDate.toLocaleString(): tempFormat;    
       }catch(e){    
          this.debug('格式化日期出现异常：' + e.message);    
       }    
    },    
        
        
    /**  
     * 根据给定的格式，把给定的时间进行格式化  
     * @param date 要格式化的日期  
     * @param strFormat 要得到的日期的格式的格式化字符串，如：'yyyy-MM-dd'，默认：yyyy-MM-dd HH:mm:ss  
     * @return 根据规定格式的时间格式  
     *    
     */    
    format: function(date, strFormat){    
     try{    
        if(date == undefined){    
           this.curDate = new Date();    
        }else if(!(date instanceof Date)){    
           this.debug('你输入的date:' + date + '不是日期类型');    
           return date;    
        }else{    
           this.curDate = date;    
        }    
        return this.formatCurrentDate(strFormat);    
     }catch(e){    
        this.debug('格式化日期出现异常：' + e.message);    
     }    
    },    
        
    /**  
     * 根据给定的格式对给定的字符串日期时间进行解析，  
     * @param strDate 要解析的日期的字符串表示,此参数只能是字符串形式的日期，否则返回当期系统日期  
     * @param strFormat 解析给定日期的顺序, 如果输入的strDate的格式为{Date.parse()}方法支持的格式，<br>  
     *         则可以不传入，否则一定要传入与strDate对应的格式, 若不传入格式则返回当期系统日期。  
     * @return 返回解析后的Date类型的时间<br>  
     *        若不能解析则返回当前日期<br>  
     *        若给定为时间格式 则返回的日期为 1970年1月1日的日期  
     *  
     * bug: 此方法目前只能实现类似'yyyy-MM-dd'格式的日期的转换，<br>  
     *       而'yyyyMMdd'形式的日期，则不能实现  
     */    
         
    parseDate: function(strDate, strFormat){    
       if(typeof strDate != 'string'){    
            return new Date();    
       }    
      var longTime = Date.parse(strDate);    
      if(isNaN(longTime)){    
          if(strFormat == undefined){    
              this.debug('请输入日期的格式');    
             return new Date();    
          }    
          var tmpDate = new Date();    
          var regFormat = /(\w{4})|(\w{2})|(\w{1})/g;    
          var regDate = /(\d{4})|(\d{2})|(\d{1})/g;    
          var formats = strFormat.match(regFormat);    
          var dates = strDate.match(regDate);    
          if( formats != undefined &&  dates != undefined && formats.length == dates.length){    
            for(var i = 0; i < formats.length; i++){    
              var format = formats[i];    
              if(format === 'yyyy'){    
                tmpDate.setFullYear(parseInt(dates[i], 10));    
              }else if(format == 'yy'){    
                var prefix = (tmpDate.getFullYear() + '').substring(0, 2);    
                var year = (parseInt(dates[i], 10) + '').length == 4? parseInt(dates[i], 10): prefix + (parseInt(dates[i], 10) + '00').substring(0, 2);    
                var tmpYear = parseInt(year, 10);    
                tmpDate.setFullYear(tmpYear);    
              }else if(format == 'MM' || format == 'M'){    
                tmpDate.setMonth(parseInt(dates[i], 10) - 1);    
              }else if(format == 'dd' || format == 'd'){    
                tmpDate.setDate(parseInt(dates[i], 10));    
              }else if(format == 'HH' || format == 'H'){    
                tmpDate.setHours(parseInt(dates[i], 10));    
              }else if(format == 'mm' || format == 'm'){    
                tmpDate.setMinutes(parseInt(dates[i], 10));    
              }else if(format == 'ss' || format == 's'){    
                tmpDate.setSeconds(parseInt(dates[i], 10));    
              }    
            }    
           return tmpDate;    
         }    
          return tmpDate;    
        }else{    
          return new Date(longTime);    
        }    
    },    
        
        
    /**  
     * 根据给定的时间间隔类型及间隔值，以给定的格式对给定的时间进行计算并格式化返回  
     * @param date 要操作的日期时间可以为时间的字符串或者{@see Date}类似的时间对象，  
     * @param interval 时间间隔类型如："YEAR"、"MONTH"、 "DATE", 不区分大小写  
     * @param amount 时间间隔值，可以正数和负数, 负数为在date的日期减去相应的数值，正数为在date的日期上加上相应的数值  
     * @param strFormat 当输入端的date的格式为字符串是，此项必须输入。若date参数为{@see Date}类型是此项会作为最终输出的格式。  
     * @param targetFormat 最终输出的日期时间的格式，若没有输入则使用strFormat或者默认格式'yyyy-MM-dd HH:mm:ss'  
     * @return 返回计算并格式化后的时间的字符串  
     */    
    changeDate: function(date, interval, amount, strFormat, targetFormat){    
        var tmpdate = new Date();    
        if(date == undefined){    
           this.debug('输入的时间不能为空!');    
           return new Date();    
        }else if(typeof date == 'string'){    
            tmpdate = this.parseDate(date, strFormat);    
        }else if(date instanceof Date){    
          tmpdate = date;    
        }    
        var field  =  (typeof interval == 'string')? interval.toUpperCase(): 'DATE';    
            
        try{    
          amount = parseInt(amount + '', 10);    
          if(isNaN(amount)){    
             amount = 0;    
          }    
        }catch(e){    
           this.debug('你输入的[amount=' + amount + ']不能转换为整数');    
           amount = 0;    
        }    
        switch(field){    
           case this.DEFAULT_YEAR:    
             tmpdate.setFullYear(tmpdate.getFullYear() + amount);    
             break;    
           case this.DEFAULT_MONTH:    
             tmpdate.setMonth(tmpdate.getMonth() + amount);    
             break;    
           case this.DEFAULT_DATE:    
             tmpdate.setDate(tmpdate.getDate() + amount);    
             break;    
           case this.DEFAULT_HOUR:    
             tmpdate.setHours(tmpdate.getHours() + amount);    
             break;    
           case this.DEFAULT_MINUTE:    
             tmpdate.setMinutes(tmpdate.getMinutes() + amount);    
             break;    
           case this.DEFAULT_SECOND:    
              tmpdate.setSeconds(tmpdate.getSeconds() + amount);    
             break;    
           default:    
              this.debug('你输入的[interval:' + field + '] 不符合条件!');            
        }    
            
        this.curDate = tmpdate;    
        return this.formatCurrentDate(targetFormat == undefined? strFormat: targetFormat);    
    },    
        
    /**  
     * 比较两个日期的差距  
     * @param date1 Date类型的时间  
     * @param date2 Dete 类型的时间  
     * @param isFormat boolean 是否对得出的时间进行格式化,<br>   
     *       false:返回毫秒数，true：返回格式化后的数据  
     * @return 返回两个日期之间的毫秒数 或者是格式化后的结果  
     */    
    compareTo: function(date1, date2, isFormat){    
      try{    
            var len = arguments.length;    
            var tmpdate1 = new Date();    
            var tmpdate2 = new Date();    
            if(len == 1){    
               tmpdate1 = date1;    
            }else if(len >= 2){    
              tmpdate1 = date1;    
              tmpdate2 = date2;    
            }    
        if(!(tmpdate1 instanceof Date) || !(tmpdate2 instanceof Date)){    
           return 0;    
        }else{    
            var time1 = tmpdate1.getTime();     
            var time2 = tmpdate2.getTime();    
            var time = Math.max(time1, time2) - Math.min(time1, time2);    
            if(!isNaN(time) && time > 0){    
               if(isFormat){    
                  var date = new Date(time);    
                  var result = {};    
                  result['year']   = (date.getFullYear() - 1970) > 0? (date.getFullYear() - 1970): '0';    
                  result['month']  = (date.getMonth() - 1) > 0? (date.getMonth() - 1): '0';    
                  result['day']    = (date.getDate() - 1) > 0? (date.getDate() - 1): '0';    
                  result['hour']   = (date.getHours() - 8) > 0? (date.getHours() - 1): '0';    
                  result['minute'] = date.getMinutes() > 0? date.getMinutes(): '0';    
                  result['second'] = date.getSeconds() > 0? date.getSeconds(): '0';    
                  return result;    
                }else {    
                 return time;    
                }    
            }else{    
              return 0;    
            }    
        }    
      }catch(e){    
        this.debug('比较时间出现异常' + e.message);    
      }    
    },    
        
    /**  
     * 根据给定的日期得到日期的月，日，时，分和秒的对象  
     * @param date 给定的日期 date为非Date类型， 则获取当前日期  
     * @return 有给定日期的月、日、时、分和秒组成的对象  
     */    
    getDateObject: function(date){    
         if(!(date instanceof Date)){    
           date = new Date();    
         }    
        return {    
            'M+' : date.getMonth() + 1,     
            'd+' : date.getDate(),       
            'H+' : date.getHours(),       
            'm+' : date.getMinutes(),     
            's+' : date.getSeconds()    
         };    
    },    
        
    /**  
     *在控制台输出日志  
     *@param message 要输出的日志信息  
     */    
    debug: function(message){    
       try{    
           if(!this.isDebug){    
             return;    
           }    
           if(!window.console){    
               window.console = {};    
               window.console.log = function(){    
                  return;    
               }    
           }    
           window.console.log(message + ' ');    
       }catch(e){  
    	   
       }    
    }    
}


var KeyWordSerch = function(){
};

KeyWordSerch.prototype = {
		/**  
	     * 根据给定的关键字数组生成用于匹配关键的树结构
	     * @param keyWordArray 给定的关键字数组 
	     * @return 用于匹配关键的树结构 
	     */ 
		generateKeyWordsTree: function(keyWordArray) {
	        "use strict";
	        var tblCur = {},
	            tblRoot,
	            key,
	            str_key,
	            Length,
	            j,
	            i
	            ;
	        tblRoot = tblCur;
	        for ( j = keyWordArray.length - 1; j >= 0; j -= 1) {
	            str_key = keyWordArray[j];
	            Length = str_key.length;
	            for ( i = 0; i < Length; i += 1) {
	                key = str_key.charAt(i);
	                if (tblCur.hasOwnProperty(key)) { //生成子节点
	                    tblCur = tblCur[key];
	                } else {
	                    tblCur = tblCur[key] = {};
	                }
	            }
	            tblCur.end = true; //最后一个关键字没有分割符
	            tblCur = tblRoot;
	        }
	        return tblRoot;
	    },
	    
	    /**  
	     * 根据给定内容和关键字的树结构校验关键字
	     * @param content 待查询关键字的内容
	     * @param keyWordsTree 关键字数组生成的树
	     * @return 匹配到的关键字及其在内容中的位置 
	     */ 
	    search: function(content, keyWordsTree) {
	        "use strict";
	        var tree,
	            p_star = 0,
	            n = content.length,
	            p_end,
	            match,  //是否找到匹配
	            match_key,
	            match_str,
	            arrMatch = [],  //存储结果
	            arrLength = 0   //arrMatch的长度索引
	            ;
	 
	        while (p_star < n) {
	            tree = keyWordsTree; //回溯至根部
	            p_end = p_star;
	            match_str = "";
	            match = false;
	            do {
	                match_key = content.charAt(p_end);
	                if (!(tree = tree[match_key])) { //本次匹配结束
	                    p_star += 1;
	                    break;
	                }else{
	                    match_str += match_key;
	                }
	                p_end += 1;
	                if (tree.end === true) //是否匹配到尾部  //找到匹配关键字
	                {
	                    match = true;
	                }
	            } while (true);
	 
	            if (match === true) { //最大匹配
	                arrMatch[arrLength] = { //增强可读性
	                    key: match_str,
	                    begin: p_star - 1,
	                    end: p_end
	                };
	                arrLength += 1;
	                p_star = p_end;
	            }
	            
	        }
	        return arrMatch;
	    },
	    
	    /**  
	     * 根据给定内容和关键字数组检验关键字
	     * @param content 待查询关键字的内容
	     * @param keyWordsTree 关键字数组生成的树
	     * @return 匹配到的关键字及其在内容中的位置
	     */ 
	    searchDirect: function(content, keyWordArray, isNeedPos) {
	        "use strict";
	        var tree,
	            p_star = 0,
	            n = content.length,
	            p_end,
	            match,  //是否找到匹配
	            match_key,
	            match_str,
	            arrMatch = [],  //存储结果
	            arrLength = 0   //arrMatch的长度索引
	            ;
	        
	        // 生成关键字树
	        var keyWordsTree = this.generateKeyWordsTree(keyWordArray);
	 
	        while (p_star < n) {
	            tree = keyWordsTree; //回溯至根部
	            p_end = p_star;
	            match_str = "";
	            match = false;
	            do {
	                match_key = content.charAt(p_end);
	                if (!(tree = tree[match_key])) { //本次匹配结束
	                    p_star += 1;
	                    break;
	                }else{
	                    match_str += match_key;
	                }
	                p_end += 1;
	                if (tree.end === true) //是否匹配到尾部  //找到匹配关键字
	                {
	                    match = true;
	                }
	            } while (true);
	 
	            if (match === true) { //最大匹配
	                arrMatch[arrLength] = { //增强可读性
	                    key: match_str,
	                    begin: p_star - 1,
	                    end: p_end
	                };
	                arrLength += 1;
	                p_star = p_end;
	            }
	            
	        }
	        
	        if(!isNeedPos){
            	var hash = {},
	            len = arrMatch.length,
	            temp = [];

		        for (var i = 0; i < len; i++){
		            if (!hash[arrMatch[i].key]){
		                hash[arrMatch[i].key] = true;
		                temp.push(arrMatch[i].key);
		            } 
		        }
		        arrMatch = temp;
            }
	        
	        return arrMatch;
	    }
}


$.fn.extend({
	stopWatch : function(){
		
		var time = $(this).data("time");
		if(!!time){
			var timeArr = time.split(':');
		} else {
			var date 	 = new Date();
			var hour_ 	 = date.getHours();
			var minute_  = date.getMinutes();
			var second_  = date.getSeconds();
			var timeArr  = [hour_, minute_, second_];
		}
		
		var h = parseInt(timeArr[0]),
			m = parseInt(timeArr[1]),
			s = parseInt(timeArr[2]);
	  
	    var $h = $(this).find(".h");
	    var $m = $(this).find(".m");
	    var $s = $(this).find(".s");
	    
	    $h.text(h<10?'0'+ h:h);
	    $m.text(m<10?'0'+ m:m);
	    $s.text(s<10?'0'+ s:s);
	   
	    var timer = setInterval(second, 1000);
	    
	    function second() {
	        s++;
	        if (s > 59) {
	            s = 0;
	            minute();
	        }
	        $s.text(s < 10 ? "0" + s : s);
	    }

	    function minute() {
	        m++;
	        if (m > 59) {
	            m = 0;
	            hour();
	        }
	        $m.text(m < 10 ? "0" + m : m);
	    }

	    function hour() {
	        h++;
	        if (h == 24) {
	            h = 0;
	        }
	        $h.text(h < 10 ? "0" + h : h);
	    }
	   
	}
})

// 根据数组中的内容删除元素，只删除第一个匹配到的元素
Array.prototype.removeByValue = function(val) {
  for(var i=0; i<this.length; i++) {
    if(this[i] == val) {
      this.splice(i, 1);
      break;
    }
  }
}

Array.prototype.contains = function(val) {
  for(var i=0; i<this.length; i++) {
    if(this[i] == val) {
    	return true;
    }
  }
  
  return false;
}


/**
 * 获得运营商类型
 * 1: 移动  2:联通  3:电信  4:国际
 * @param mobile
 * @returns {number}
 */
function getMobileOperator(mobile) {
    // 移动手机号码正则
    var chinaMobileRegex = /^(((13[4-9])|(147)|(15[0-2,7-9])|(178)|(18[2-4,7-8]))\d{8})|((170[3,5,6])\d{7})$/;
    // 联通手机号码正则
    var chinaUnicomRegex = /^(((13[0-2])|(145)|(15[5,6])|(17[1,5,6])|(18[5,6]))\d{8}$)|(170[4,7,8,9]\d{7})$/;
    // 电信手机号码正则
    var chinaTelecomRegex = /^(((133)|(149)|(153)|(17[3,7])|(18[0,1,9]))\d{8})|((170[0,1,2])|(1731)\d{7})$/;
    // 国际手机号码
    var internationRegex = /^0{2}\d{8,18}$/;

    if(chinaMobileRegex.test(mobile)){
        return 1;
    }

    if(chinaUnicomRegex.test(mobile)){
        return 2;
    }

    if(chinaTelecomRegex.test(mobile)){
        return 3;
    }

    if(internationRegex.test(mobile)){
        return 4;
    }

    // 判断不出来是默认是移动
    return 1;
}

