/**
 * 
 * @authors csy (caosiyuan@ucpaas.com)
 * @date    2016-12-19 15:56:41
 * @version $Id$
 */

/*
 *  通道检测页面图表控件
 * 
 *
 *
 *  方法说明
 *  @module  Channel_Chart
 *	@method  init 				设置填充容器
 *	@method  setOption			初始化插件内容，传入参数 
 *  @method	 draw 				填充html到填充容器
 *	@method  _compute 			计算padding height
 *	@method  _paint 			绘制li颜色
 *
 *  调用方法
 * 	var  variable = Channel_Chart("selector");
 *	variable.setOption(options);
 *	参数说明 
 *  @param 	options 			Object
 *  {
 *		color 	: ['#0099ff', '#FF0033','#009900', '#ff33cc', '#990033'],		//图标颜色，对应data.type(选填)
 *		column  : 24,															//横坐标分栏(必填，默认为24)
 *		cell 	: 12,															//坐标单元格
 *		great	: '#009900',													//通道极好的颜色(选填)
 *		normal  : '#ffcc33',													//通道一般的颜色(选填)
 *		bad 	: '#ff3333',													//通道很差的颜色(选填)
 *		data    ： {}															//数据(必填)
 *	}
 *
 *  @method setOption @param option.data  数据格式约定  
 *  @param {name}  			String 
 *  @param {type}  			String  节点类型 约定 1表示移动 2表示联通 3表示自有 4表示直连 5表示第三方 6表示最后子节点
 *  @param {channelId} 		String
 *  @param {channelName} 		String
 *  @param {list} 				Array   数据列表
 *  @param {list[].dateTime}	String
 *  @param {list[].status}	Number	通道状态 约定 1 表示良好 2表示一般 3表示糟糕
 *  @param {list[].desp}		String
 *  @param {children}			Array   数组元素为data结构对象，表示下一级，最多只有是三级
 *	 data =   {
 *				name :  
 *				type :
 * 				ownerType ： 
 *				channelId : 
 *				channelName: 
 *				list : [{
 *					dateTime : 
 *					status : 
 *					desp : 
 *				},{},{},{}...]
 *				children : []
 *			}
 */
(function($, window, undefined, document){
	var js 	 = document.scripts, script = js[js.length - 1], jsPath = script.src;
  	var path = jsPath.substring(0, jsPath.lastIndexOf("/") + 1);
  	var head = document.head || document.getElementsByTagName('head')[0];
	
	head.appendChild(function(){
	    var link 	= document.createElement('link');
	    link.href 	= path + 'channelChart-res/channel_chart.css';
	    link.type 	= 'text/css';
	    link.rel 	= 'styleSheet';
	    return link;
	}());

	head.appendChild(function(){
	    var link 	= document.createElement('link');
	    link.href 	= path + 'channelChart-res/iconfont.css';
	    link.type 	= 'text/css';
	    link.rel 	= 'styleSheet';
	    return link;
	}());

	var option = {
		color 	: ['#0099ff', '#FF0033','#0066cc', '#009900', '#ff33cc', '#990033'], //移动，联通，电信，自有，直连，第三方
		column  : 24,
		cell 	: 12,
		great	: '#009900',
		normal  : '#ffcc33',
		bad 	: '#ff3333'
//		bad : 'red'
	}
	function Channel_Chart(){
	 	return new Channel_Chart.prototype.init(arguments);
	}

	Channel_Chart.prototype = {
	 	init : function(selector){
	 		var wrap = $(selector)[0];
	 		this.wrap = wrap;
	 		this.id   = $('.channel-box').length + 1;
	 	},
	 	//根据类型获取对应图标
	 	_switchType : function(type){
	 		if(type == 0){
	 			return '<span class="iconfont icon-yidong"></span>'
	 		} else if (type == 1){
	 			return '<span class="iconfont icon-liantong"></span>'
	 		} else if (type == 2){
	 			return '<span class="iconfont icon-dianxin"></span>'
	 		} else if (type == 3){
	 			return '<span class="iconfont icon-ziyou"></span>'
	 		} else if (type == 4){
	 			return '<span class="iconfont icon-zhilian"></span>'
	 		} else if (type == 5){
	 			return '<span class="iconfont icon-disanfang"></span>'
	 		} else {
	 			return '<span class="iconfont icon-other"></span>'
	 		}
	 	},
	 	_compute : function(){
	 		var that  		= this, 
	 			child 		= that.children,
	 			root_line 	= 0, 
	 			root_top 	= 0,
	 			column 		= that.opt.column,
	 			cell 		= that.opt.cell;
	 		var	root_height, root_width, child_width, child_height, ul_width, rootPadding, childPadding, scaleHeight;


	 		var root  		= $(".root" + that.id), 									//跟元素
	 			childNode 	= $(".child" + that.id),									//第二级
	 			lastWrap	= $(".channel-content" + that.id + " .channel-status");		//最后一级
	 			
	 		root_height = root.height();
	 		root_width  = root.width() + 61;
	 		rootPadding = $(".channel-content" + that.id).height() / 2 - (root_height/2); 

	 		child_width = childNode.width() + 61;

	 		//循环二级目录
	 		for(var j = 0;j < childNode.length; j++){
	 			var padding = $(lastWrap[j]).height()/2 - $(childNode[j]).height()/2;
	 			$(childNode[j]).css('padding', padding + "px 0");

	 			var li_len = $(lastWrap[j]).find(".channel-list").length;
	 			var v_link_height = $(lastWrap[j]).height() * (li_len - 1) / li_len;
	 			$(childNode[j]).find(".line-v").height(v_link_height + "px");

	 			if(j == 0 || j === childNode.length - 1){
	 				root_line += childNode[j].offsetHeight/2;
	 			} else {
	 				root_line += childNode[j].offsetHeight
	 			}	
	 			if(childNode.length ===  1){
	 				root_line = 0;
	 			}
	 			if(j == 0){
	 				root_top = childNode[j].offsetHeight/2;
	 			}
	 		}

	 		//数字
	 		var numHtml = ""
	 		for(var i = 0; i < column; i++ ){
	 			numHtml +=  '<div class="number">' + (i + 1) + '<span class="line"></span></div>';

	 		}
	 		$(".scale" + that.id).append(numHtml);


	 		ul_width 	= lastWrap.find(".status-list ul").width();
	 		scaleHeight = $(".channel-box"+that.id+" .frame").height() - 40;
	 		
	 		lastWrap.find(".status-list li").css("width", ul_width/(column*cell) + "px");
	 		$(".scale" + that.id + " .number").css("marginLeft", ul_width/column - 20 + "px")
	 		//画线
	 		$(".scale" + that.id + " .number .line").height(scaleHeight + "px");

	 		root.find(".line-v").height(root_line + "px").css("top", root_top + "px");
	 		root.css('padding', rootPadding + "px 0");
	 		root.width(root_width + "px");
	 		childNode.width(child_width + "px");
	 	},
	 	_getType : function(obj){
	 		var t;
	 		if(!!obj.type){
	 			t = parseInt(obj.type, 10) - 1;
	 		} else {
	 			t = parseInt(obj.ownerType, 10) + 3 - 1;
	 		}

	 		return t;
	 	},
	 	paint : function(){
	 		var that = this,
	 		tag = $(".channel-content" + that.id + " .channel-list li[data-status]");

	 		tag.each(function() {
		        var status = $(this).attr('data-status'); 
		        if (status == -1) {
		            $(this).css('backgroundColor', '#cccccc');
		        } else if(status == 0) {
		            $(this).css('backgroundColor', that.opt.great);
		        } else if(status == 1) {
		            $(this).css('backgroundColor', that.opt.normal);
		        } else if(status == 2) {
		            $(this).css('backgroundColor', that.opt.bad);
		        }
		    });

	 	},
	 	setOption : function(options){
	 		this.opt = $.extend(option, options);
	 		this.Data = this.opt.data;
	 		if(this.Data.children) {
	 			this.children = this.Data.children;
	 		}
	 		this.draw();
	 	},
	 	draw : function(){
	 		var that = this;
	 		var ctx  = '<div class="channel-box channel-box'+ that.id +'" >' + 
							'<div class="mobile-market frame clearfix">' + that.drawRoot() + that.drawChild() + that.drawLast() + '</div>' + 
						'</div>'; 
	 		tag = $(that.wrap);

	 		tag.html(ctx);
	 		this.paint();
	 		this._compute(); 
	 	},
	 	drawRoot : function(){
	 		var type   = this._getType(this.Data),
	 			name   = this.Data.name,
	 			title  = this._switchType(type),
	 			color  = this.opt.color[type];

	 		var html  = '<div class="menu-box ft-l">' + 
							'<div class="first-menu root'+ this.id +'">' +
								'<div class="name" style="border-color:'+ color +'; color:'+ color +';">'+ 
								'<i class="after" style="background-color:'+ color +'"></i>'+ 
									title + name +
								'</div>' +
								'<div class="line-v" style="background-color:'+ color +';"></div>' +
							'</div>' +
						'</div>';
			return html;
	 	},
	 	drawChild : function(){
	 		var child = this.children,
	 			li 	  = "";
	 			
	 		for(var i = 0; i < child.length; i++){
	 			
	 			var type  = this._getType(child[i]);
	 			var title = this._switchType(type);
	 			var color = this.opt.color[type];
	 			
	 			li += '<li class="second-self child'+ this.id +'">' + 
							'<div class="name self" style="color:'+ color +'; border-color:'+ color +';">' +
								'<i class="before" style="background-color:'+ color +'"></i><i class="after" style="background-color:'+ color +'"></i>'
								+ title + child[i].name +
							'</div>' +
							'<div class="line-v" style="background-color:'+ color +';"></div>' + 
						'</li>';
	 		}
	 		var html  = '<div class="menu-box ft-l">' +
							'<ul class="second-menu">' +
								li + 
							'</ul>' + 
						'</div>';
			return html;
	 	},
	 	drawLast : function(){
	 		var opt 	= this.opt;
	 		var child 	= this.children;
	 		var content = "";
	 		
	 		for(var f = 0; f < child.length; f++){
	 			
	 			var type  = this._getType(child[f]),
	 				color = this.opt.color[type],
	 				li 	  = "";
	 			
	 			for(var i = 0; i < child[f].children.length; i++){
		 			var data 		= child[f].children[i],
						statusHtml  = "",
						iconType 	= this._getType(data),
						title 		= this._switchType(iconType);
					for(var k = 0; k < data.list.length; k++){
						if (k > opt.column * opt.cell) continue;
						statusHtml += '<li data-status="' + data.list[k].status + '">' + 
											'<em class="desp">' + 
												'<p>时间:' + data.list[k].dataTime + '</p>' + 
												'<p>描述:' + data.list[k].desp + '</p>' + 
											'</em>' +
										'</li>';
					}
					li += '<li class="channel-list clearfix">' +
		                        '<div class="name ft-l clearfix" style="color:'+ color +'; border-color:'+ color +';">'+ 
		                        	'<i class="before" style="background-color:'+ color +'"></i>' + title +
		                        	'<div class="desp-box">' + 
			                            '<p class="id">' + data.channelId + '</p>' +
			                            '<p class="channel-name">' + data.channelName + '</p>' +
			                        '</div>' + 
		                        '</div>' +
		                        '<div class="status-list ft-l">' +
		                            '<ul class="clearfix">' + statusHtml +'</ul>' +
		                        '</div>' +
		                    '</li>';
					
		 		}
		 		content += '<div class="channel-status clearfix">' + 
									'<ul class="self">' + li +
									'</ul>' + 
								'</div>';
	 		}
	 		var html = '<div class="content channel-content'+ this.id +' ft-l">' + content + 
							'<div class="scale-v scale'+ this.id +'"></div>' +
						'</div>';
			return html;
	 	},


	 	clear : function(){
	 		var that = this;
	 		$(".channel-box" + that.id).remove();
	 	}


	}

	Channel_Chart.prototype.init.prototype = Channel_Chart.prototype;

	if (typeof window.Channel_Chart === 'undefined') {
        window.Channel_Chart = Channel_Chart;
    }
})(jQuery, window, undefined,document)