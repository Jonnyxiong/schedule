<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
	<title>${data.channelgroupid==null?'添加':'修改'}通道组</title>
	<style type="text/css">
		.hide {
			display: none;
		}		
		#channelGroupDiv .el-select{
			width: 700px;
		}
		
		#channelGroupDiv .el-select .el-tag {
		    height: 32px;
		    line-height: 32px;
		    box-sizing: border-box;
		    margin: 3px 0 3px 6px;
		}
		
		#channelGroupDiv .controls span {
		    display: inline-block;
		    font-size: 14px;
		    margin-top: 0px;
		}
		
		#channelGroupDiv .el-input__inner{
			color: #fff;
		}
		#channelGroupDiv input[readonly]{
			background-color: #fff;
		}
		#channelGroupDiv .el-select__tags input[type="text"]{
 			border: none;
			background-color: rgba(255,255,255,0);
			outline:none !important;
			padding: 0px 0px 0px 0px;
		}
		
		#channelGroupDiv .el-tag--primary {
		    background-color: #fff;
		    border-color: #28b779;
		    color: #28b779;
		}
		.el-select-dropdown.is-multiple .el-select-dropdown__item.selected {
		    color: #13ce66 !important;
		    background-color: #fff !important;
		}
        .pop{
            z-index:999;
            position:fixed;
            top:0;
            left:0;
            width:100%;
            height:100%;
            background-color: rgba(0,0,0,.8);
        }
        .pop .ctx{
            padding:30px;
            background-color: #F4F4F4;
            position: absolute;
            width:700px;
            top:50%;
            left:50%;
            margin-left:-350px;
            transform: translateY(-50%);
        }
        .weight-input{
            width:40px;
        }
	</style>
	
<!-- 引入element-ui样式 -->
<link rel="stylesheet" href="${ctx}/js/element-ui/element-ui.css" />
<!-- 引入element-ui组件库 -->
<script src="${ctx}/js/element-ui/vue.min.js"></script>
<script src="${ctx}/js/element-ui/element-ui.js"></script>

</head>

<body menuId="76">
<!--Action boxes-->
  <div class="container-fluid" id="app">
    <hr>
    <div class="row-fluid">
      <div class="span12">
        <div class="widget-box">
          <div class="widget-title"> <span class="icon"> <i class="fa fa-list"></i> </span>
            <h5></h5>
          </div>
          <div class="widget-content nopadding">
            <form class="form-horizontal" method="post" action="#" id="mainForm">
              <div class="control-group">
                <label class="control-label">通道组名称</label>
                <div class="controls">
                  <input type="text" name="channelgroupname" value="<c:out value="${data.channelgroupname}"/>"/>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">运营商类型</label>
                <div class="controls" name="operatorstype">
					<u:select id="operatorstype" value="${data.operater}" excludeValue="" defaultValue="0" placeholder="运营商类型" dictionaryType="channel_operatorstype" />
                </div>
              </div>
              
               <div class="control-group">
                <label class="control-label">通道组类型</label>
                <div class="controls">
                	<u:select id="flag" data="[{value:'1',text:'普通通道组'},{value:'2',text:'自动路由通道池'}]" value="${data.flag}" defaultValue="1" showAll="false"/>
                </div>
              </div>
                <input type="hidden" name="channelid">
                <input type="hidden" name="ydchannelid">
                <input type="hidden" name="ltchannelid">
                <input type="hidden" name="dxchannelid">
                <input type="hidden" name="gjchannelid">
              <div id="channelGroupDiv" class="control-group">
              		<div id="quanwang">
						<label class="control-label" id="channelname">{{text}}</label>
						<div class="controls">
                            <div v-for="(item, index) in channel" v-if="channel.length > 0">
                                <p>通道号{{item.val}},权重: <input type="text" class="weight-input" v-model="item.weight" name="weight">&nbsp;&nbsp;<a href="javascript:;" class="btn btn-default" @click="del(index)">删除</a></p>
                            </div>
							<a href="javascript:;" class="btn btn-success" id="add" @click="open">添加</a>
						</div>
              		</div>
			  </div>
				<div class="control-group">
                <label class="control-label">备注</label>
                <div class="controls">
                  <input type="text" name="remarks" value="<c:out value="${data.remarks}"/>" maxlength="50" />
                </div>
              </div>
              <div class="control-group">
                <label class="control-label">&nbsp;</label>
                <div class="controls">
                  <span id="msg" class="error" style="display:none;"></span>
                </div>
              </div>
              <div class="form-actions">
              	<input type="hidden" name="channelgroupid" value="${data.channelgroupid}">
                <input type="button" onclick="save(this);" value="保  存" class="btn btn-success">
                <input type="button" value="取 消" class="btn btn-error" onclick="back()"/>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
	  <div class="pop" style="display: none;">
		  <div class="ctx">
			  <div class="title">选择通道</div>
              <div class="form-horizontal">
                  <div id="" class="control-group">
                      <label class="control-label">{{text}}</label>
                      <div class="controls">
                          <el-select v-model="value" filterable size="large" placeholder="请选择" >
                              <el-option
                                      v-for="item in options"
                                      :label="item.label"
                                      :value="item.value"
                                      :disabled="item.disabled">
                              </el-option>
                          </el-select>
                      </div>
                  </div>
                  <div id="" class="control-group">
                      <label class="control-label">输入权重</label>
                      <div class="controls">
                          <input type="text" v-model="weight" name="weight">
                      </div>
                  </div>
                  <div id="" class="control-group">
                      <label class="control-label"></label>
                      <div class="controls">
                          <a href="javascript:;" class="btn btn-success" @click="saveChannel">保存</a>
                          <a href="javascript:;" class="btn btn-default js-close" @click="close">取消</a>
                      </div>
                  </div>
              </div>
		  </div>
	  </div>
  </div>



<script type="text/javascript">
var validate;
var channelid = "${data.channelid}";
var operater = "${data.operater}";
console.log(channelid)
// vue实现多选通道
vm = new Vue({
	el : "#app",
	mounted: function(){
		var that = this;
		$.ajax({
			type : "post",
			async: false,
			url : "${ctx}/channelGroup/queryChannelGroupByOperator",
			success : function(data) {
                //填数据
                var arr = channelid.split(",");
                var arr_channel= [];
                for(var i = 0;i < arr.length; i++){
                    if(arr[i] === "" || arr[i] === undefined || arr[i] === null){
                        break;
                    }
                    arr[i] = arr[i].split("|");
                    arr_channel[i] = {};
                    arr_channel[i].val = arr[i][0];
                    arr_channel[i].weight = arr[i][1];
                }
                that.channel = arr_channel;
                console.log("====",arr)
				if (data != null) {
					that.options0 = data.quanwang;
					that.options1 = data.yidong;
					that.options2 = data.liantong;
					that.options3 = data.dianxin;
					that.options4 = data.guoji;

                    that.handleChange();
                    that.disableData(); //已经选择的 禁止再选
					if(channelid.trim().length > 0){
                        that.value = channelid.split(",");
					}
//						console.log("查询通道组：" + data)
				}else{
					console.log("查询通道组时出错！");
				}
			}
		});

		//事件绑定
		$("#operatorstype").change(function(){
            that.handleChange()
            that.channel = [];
		})


	},
	data : {
	    text :"",
	    options : [],
		options0 : [],
		options1 : [],
		options2 : [],
		options3 : [],
		options4 : [],
		value: '',
        weight:'',
        channel : []
	},
    methods : {
        disableData : function(){
            var option = this.options;
            var checked = this.channel;
            for(var i = 0; i < option.length; i++){
                var now_option = option[i];
                for(var j = 0; j < checked.length; j++){
                    if(now_option.value == checked[j].val){
                        now_option.disabled = true;
                    }
                }
            }
            console.log(this.options);
        },
        ableData : function(index){
            var option = this.options;
            var checked = this.channel;
            //已选择的禁用
            for(var i = 0; i < option.length; i++){
                var now_option = option[i];

                if(now_option.value == checked[index].val){
                    now_option.disabled = false;
                    this.channel.splice(index,1);
                    break;
                }

            }
        },
	  	handleChange : function(val){
            var value = $("#operatorstype").val();
            this.value = "";
            if(value == 0){
                this.options = this.options0;
                this.text = "全网组内通道"
            }else if(value == 1){
                this.options = this.options1;
                this.text = "移动组内通道"
            }else if(value == 2){
                this.options = this.options2;
                this.text = "联通组内通道"
            }else if(value == 3){
                this.options = this.options3;
                this.text = "电信组内通道"
            }else if(value == 4){
                this.options = this.options4;
                this.text = "国际组内通道"
            }
        },
        close : function(){
            $(".pop").hide();
        },
        open : function(){
            $(".pop").show();
        },
        //保存通道
        saveChannel : function(){
            var o = {
                val : this.value,
                weight : this.weight
            }
            if(!/^[0-9]+?$/.test(o.weight)){
                layer.msg("权重只能整数且不能为空",{icon:2})
                return;
            }
            if(o.weight > 100){
                layer.msg("权重不能大于100",{icon:2})
                return;
            }
            this.channel = (this.channel).concat(o)
            this.weight = "";
            this.value = "";
            $(".pop").hide();
            this.disableData();
        },
        del : function(i){
            this.ableData(i)
//            this.channel.splice(i,1);
//            this.disableData();
        }
    }
});


$(function(){



	$.validator.defaults.ignore = "";
	//表单验证规则
	validate = $("#mainForm").validate({
		rules: {
			channelgroupname: {
				required: true,
				maxlength: 40,
			},
            weight:{
                digits:true,
                range:[0,100]
            },
			operatorstype : "required"
		},
		messages: {
			channelgroupname: {
				required:"请输入通道组名称",
				maxlength:" 输入长度最多是40的字符串(汉字算一个字符)"
			},
            weight: {
                range:"请输入0~100区间值",
            },
			operatorstype : "请选择运营商"
		}
	});
	
});

function save(btn){
	$("#msg").hide();
	$("#channelid-error").remove();
    if(!validate.form()){
        return;
    }
	var channel = vm.channel;
	if(channel=="" ||channel==undefined){
        layer.msg("请选择对应通道及配置权重！",{icon:2})
        return;
    }
    var arr = [], total = 0;

	for(var i = 0; i < channel.length; i++){
        var weight = channel[i].weight;
        if(!/^[0-9]+?$/.test(weight)){
            layer.msg("权重只能整数且不能为空",{icon:2})
            return;
        }
        if(weight > 100){
            layer.msg("单个通道权重不能大于100",{icon:2})
            return;
        }
        arr[i] = channel[i].val + "|" + weight;
        total += Number(weight);
    }
//    if(total > 100){
//        layer.msg("权重之和不能大于100",{icon:2})
//        return;
//    }
    var value = $("#operatorstype").val();

    if(value == 0){
        $("input[name='channelid']").val(arr.join(","));
    }else if(value == 1){
        $("input[name='ydchannelid']").val(arr.join(","));
    }else if(value == 2){
        $("input[name='ltchannelid']").val(arr.join(","));
    }else if(value == 3){
        $("input[name='dxchannelid']").val(arr.join(","));
    }else if(value == 4){
        $("input[name='gjchannelid']").val(arr.join(","));
    }

	
	var options = {
		beforeSubmit : function() {
			$(btn).attr("disabled", true);
		},
		success : function(data) {
			$(btn).attr("disabled", false);
			
			if(data.result==null){
				$("#msg").text("服务器错误，请联系管理员").show();
				return;
			}
			
			$("#msg").text(data.msg).show();
		},
		url : "${ctx}/channelGroup/save",
		type : "post",
		timeout:30000
	};
	$("#mainForm").ajaxSubmit(options);
};
</script>
</body>
</html>