<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
    <title>审核关键字配置</title>
    <link rel="stylesheet" href="${ctx}/css/doublebox-bootstrap.css" />
    <style>
        body{
            background-color: #F8F8F8;
        }
        .search ul li {
            float: left;
        }
        .ue-container {
            margin: 0 auto;
            margin-top: 3%;
            padding: 20px 40px;
            border: 1px solid #ddd;
            background: #fff;
        }
        /*.bootstrap-duallistbox-container .btn-box{*/
        /*margin-top: 39px !important;*/
        /*}*/
        .modal-content{
            opacity: 1 !important;
            border:none;
        }
        .bootstrap-duallistbox-container label{
            text-align: center;
        }
        .filter{
            text-align: center;
        }
        .demo{
            height: 322px !important;
        }
        .box1,.box2 {
            padding-left: 52px !important;
            width:300px !important;
        }
        .btn-box .btn{
            width: 40px !important;
            height: 30px !important;
            margin-left: 35px !important;

        }
        option:hover{
            background-color: #0099FF;
        }
    </style>
</head>
<body menuId="3301">
<div id="configure" >
    <div>
        <h4 style="text-align: center">【<span id="clientName"  style="color: #003399;"></span>】配置</h4>
        <select multiple="multiple" size="100" name="doublebox" class="demo" style="display: none"></select>
        <div class="control-group" style="margin-top: 20px;border-top: 1px dashed #ccc;padding-top: 15px;">
            <span class="control-label"><span style="color: red;margin-left: 15px;">*</span>审核关键字组别：</span>
            <select id="modelId" name="kgroupId">
            </select>
        </div>
    </div>
    <div class="control-group fr" style="margin-right: 50px;">
        <div class="controls">
            <%--<button type="submit" class="btn">Sign in</button>--%>
            <a href="javascript:;" class="btn btn-primary   btn-small js-save">确定</a>
            <a href="javascript:;" class="btn btn-small  js-back">取消</a>
        </div>
    </div>
    <input type="hidden" id="cgroupName_ID"  value="${data.cgroupName}"  >
    <input type="hidden" id="cgroup_ID"  value="${data.cgroupId}" >
    <input type="hidden" id="kgroupId_ID"  value="${data.kgroupId}" >
</div>
<script src="${ctx}/js/doublebox-bootstrap.js"></script>
<script type="text/javascript">
    $(function () {
        $("#clientName").text($("#cgroupName_ID").val())
        var option;
        $("select[name=kgroupId]").empty();      //清空
        $.ajax({url:'/keyWordSconfiguration/getGroup',
            type:"post",
            cache: false,
            error:function(){
            },
            success:function(data){
                var modelList = data;
                if(modelList && modelList.length != 0){
                    for(var i=0; i<modelList.length; i++){
                        if(modelList[i].kgroupId == $("#kgroupId_ID").val()){
                            option="<option value=\""+modelList[i].kgroupId+"\"  selected=\"selected\"";
                            option += ">"+modelList[i].kgroupName+"</option>";  //动态添加数据
                        }else{
                            option="<option value=\""+modelList[i].kgroupId+"\"";
                            option += ">"+modelList[i].kgroupName+"</option>";  //动态添加数据
                        }
                        $("select[name=kgroupId]").append(option);
                    }
                }
            }
        });
        $.ajax({url:'/keyWordSconfiguration/getAccount',
            type:"post",
            data:{cgroupId:$("#cgroup_ID").val()},
            cache: false,
            error:function(){
            },
            success:function(data){
                $('.demo').doublebox({
                    nonSelectedListLabel: '未添加用户',
                    selectedListLabel: '已添加用户',
                    preserveSelectionOnMove: 'moved',
                    moveOnSelect: false,
                    nonSelectedList:data.jsmsAccountList,
                    selectedList:data.jsmsAccountExistList,
                    optionValue:"clientid",
                    optionText:"name",
                    doubleMove:true
                });
                $(".moveall").remove();
                $(".ue-form").show();
                $(".removeall").remove();
                $(".upBtn").remove();
                $(".downBtn").remove();
            }
        });

    })



    //获得左右移动后的items
    function getSelectedOptions() {
        var items = $("#bootstrap-duallistbox-selected-list_doublebox>option")
            .map(function() {
                return $(this).val();
            })
            .get()
            .join(",")
            .split(",");
        return items;
    };

    $(".js-save").click(function () {
        var clientid = getSelectedOptions();
        var list = [];
        for(var i = 0 ; i<clientid.length;i++){
            var obj = {};
            obj.clientid = clientid[i];
            list.push(obj);
        }

        $.ajax({
            url:'/keyWordSconfiguration/save',
            type:"POST",
            data:{list:list,cgroupId:$("#cgroup_ID").val(),kgroupId:$("#modelId").val()},
            success:function(data) {
                debugger
                if(data.code == 500){
                    layer.msg(data.msg, {icon:2,time: 1500},function () {
                    })
                    return;
                }else if(data.code == 200){
                    layer.msg(data.msg, {icon:1,time: 1500},function () {
                        parent.layer.closeAll();
                    });
                    return;
                }
                layer.msg(data.msg, {icon: 1,time: 1500},function(){
                    parent.window.location.reload();
                });
            }
        });
    });
    $(".js-back").click(function () {
        parent.layer.closeAll()
    })
    var last;
    $("body").on("keyup",'input.ue-left',function (event) {
        console.log('左边输入了>>>>>>>>>>>>>>');
        last = event.timeStamp;
        setTimeout(function(){    //设时延迟0.5s执行
            if(last-event.timeStamp==0)//如果时间差为0（也就是你停止输入0.5s之内都没有其它的keyup事件发生）则做你想要做的事
            {
                $('.demo').html('')
                $.ajax({url:'/keyWordSconfiguration/getAccount',
                    type:"post",
                    data:{cgroupId:$("#cgroup_ID").val(),flag:'0',content:$('.ue-left').val()},
                    cache: false,
                    error:function(){
                    },
                    success:function(data){
                        $('.demo').doublebox({
                            nonSelectedListLabel: '未添加用户',
                            selectedListLabel: '已添加用户',
                            preserveSelectionOnMove: 'moved',
                            moveOnSelect: false,
                            nonSelectedList:data.jsmsAccountList,
                            selectedList:data.jsmsAccountExistList,
                            optionValue:"clientid",
                            optionText:"name",
                            doubleMove:true
                        });
                        $(".moveall").remove();
                        $(".ue-form").show();
                        $(".removeall").remove();
                        $(".upBtn").remove();
                        $(".downBtn").remove();
                    }
                });
            }
        },500);

    });
    $("body").on("change keyup",'input.ue-right',function (event) {
        console.log('右边输入了>>>>>>>>>>>>>>');
        last = event.timeStamp;
        setTimeout(function(){    //设时延迟0.5s执行
            if(last-event.timeStamp==0)//如果时间差为0（也就是你停止输入0.5s之内都没有其它的keyup事件发生）则做你想要做的事
            {
                $('.demo').html('')
                $.ajax({url:'/keyWordSconfiguration/getAccount',
                    type:"post",
                    data:{cgroupId:$("#cgroup_ID").val(),flag:'1',content:$('.ue-right').val()},
                    cache: false,
                    error:function(){
                    },
                    success:function(data){
                        $('.demo').doublebox({
                            nonSelectedListLabel: '未添加用户',
                            selectedListLabel: '已添加用户',
                            preserveSelectionOnMove: 'moved',
                            moveOnSelect: false,
                            nonSelectedList:data.jsmsAccountList,
                            selectedList:data.jsmsAccountExistList,
                            optionValue:"clientid",
                            optionText:"name",
                            doubleMove:true
                        });
                        $(".moveall").remove();
                        $(".ue-form").show();
                        $(".removeall").remove();
                        $(".upBtn").remove();
                        $(".downBtn").remove();
                    }
                });
            }
        },500);

    });


</script>
</body>

</html>
