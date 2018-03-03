<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
    <title>添加组别</title>
    <style type="text/css">
        body{
            background-color: #F8F8F8;
        }
        .form-horizontal .controls span{
            margin-top:0;
        }
        textarea{
            resize:none;
            width:400px;
            height:300px;
        }
        .example{
            line-height: 30px;
        }
        .select2 span{
            display:block !important;
            margin-top: 0px !important;
        }
        .control-group .select2-container {
            width: 216px !important;
        }
        .select2-dropdown {
            width: 220px !important;
        }
        .hover-tip{
            position: relative;
            display: inline-block;
            width:15px;
            height:15px;

        }
        .hover-tip .tips{
            display: none;
            text-align: left;
            color:#FFF;
            position: absolute;
            width:200px;
            padding:10px;
            left:-96px;
            top:21px;
            border-radius:6px;
            background-color: #fe6633;
        }
        .hover-tip:hover .tips{
            display: block;
        }
        .el-select-dropdown{
            z-index: 20171101 !important;
        }

        #keyword .el-select{
            width: 300px;
        }

        #keyword .el-select .el-tag {
            height: 22px;
            line-height: 22px;
            box-sizing: border-box;
            margin: 3px 0 3px 6px;
        }

        #keyword .controls span {
            display: inline-block;
            font-size: 14px;
            margin-top: 0px;
        }

        #keyword .el-input__inner{
            color: #fff;
        }
        #keyword input[readonly]{
            background-color: #fff;
        }
        #keyword .el-select__tags input[type="text"]{
            border: none;
            background-color: rgba(255,255,255,0);
            outline:none !important;
            padding: 0px 0px 0px 0px;
        }

        #keyword .el-tag--primary {
            background-color: #fff;
            border-color: #28b779;
            color: #28b779;
        }
        .el-select-dropdown.is-multiple .el-select-dropdown__item.selected {
            color: #13ce66 !important;
            background-color: #fff !important;
        }
        .el-select{
            width: 400px !important;
        }
        .el-select__input{
            width: 60px !important;
        }
        .el-select__tags{
            max-width: 536px !important;
            width: 400px !important;
        }
        .control-group{
            margin-top: 20px;
            margin-bottom: 20px;
        }
    </style>
    <!-- 引入element-ui样式 -->
    <link rel="stylesheet" href="${ctx}/js/element-ui/element-ui.css" />
    <!-- 引入element-ui组件库 -->
    <script src="${ctx}/js/element-ui/vue.min.js"></script>
    <script src="${ctx}/js/element-ui/element-ui.js"></script>
</head>

<body menuId="3300">
<!--Action boxes-->
<div id="editGroup" class="container-fluid" >
    <div class="row">
        <div>
            <form id="Form" class="form-horizontal" style="margin-top: 20px;">
                <input type="hidden" name="kgroupId" id="kgroup_Id_Value">
                <div class="control-group" id="kgroupID">
                    <label class="control-label"><span style="color: red">*</span>组别ID：</label>
                    <div class="controls">
                        <span id="kgroupIDvalue"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><span style="color: red">*</span>组别名称：</label>
                    <div class="controls">
                        <input id="kgroupName" name="kgroupName" type="text"  placeholder="10个字符以内" />
                    </div>
                </div>
                <div id="keyword" class="control-group">
                    <label class="control-label"><span style="color: red">*</span>审核关键字类别：</label>
                    <div class="channelid controls">
                        <el-select v-model="value1" name="categoryNameList" multiple filterable  size="large" placeholder="输入可搜索" >
                            <el-option
                                    v-for="item in options1"
                                    :label="item.categoryName"
                                    :value="item.categoryId">
                            </el-option>
                        </el-select>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">备注：</label>
                    <div class="controls">
                        <input id="remark" name="remark" type="text"  placeholder="20个字符以内" />
                    </div>
                </div>
                <div class="control-group" style="margin-top: 100px;">
                    <div class="controls">
                        <a href="javascript:;" class="btn btn-primary btn-small js-save">确定</a>
                        <a href="javascript:;" class="btn btn-small js-back" style="margin-left: 50px;">取消</a>
                    </div>

                </div>
            </form>
            <input type="hidden" id="kgroup_ID"  value="${data.kgroupId}"  >
            <input type="hidden" id="remark_ID"  value="${data.remark}" >
            <input type="hidden" id="kgroupName_ID"  value="${data.kgroupName}" >
            <input type="hidden" id="categoryNameList_ID"  value="${data.categoryNameList}" >

        </div>
    </div>
</div>

<script type="text/javascript">
    var dataArr = [];
    $(function(){
        var kgroupName = $("#kgroupName_ID").val();
        var remark = $("#remark_ID").val();
        var kgroupId = $("#kgroup_ID").val();
        var select2Date;

        if(kgroupId != "" && kgroupId != undefined && kgroupId != null){
            //如果是编辑页面
            $("#kgroupName").val(kgroupName);
            $("#remark").val(remark);
            $("#kgroup_Id_Value").val(kgroupId);
            $("#kgroupIDvalue").text(kgroupId);
            $("#kgroupID").show();
            $.ajax({
                type : "post",
                data:{
                    kgroupId:kgroupId
                },
                async: false,
                url : "${ctx}/keyWordSconfiguration/getAuditKeywordCategoryExit",
                success : function(data) {
                    console.log(data)
                    for(var i = 0;i<data.length;i++){
                        dataArr.push(data[i].categoryId);
                    }

                }
            });
        }else{
            $("#kgroupID").hide();
        }
        //提交
        $(".js-save").click(function(){
            var remark = $.trim($("#remark").val());
            var remark_length = remark.length*1;
            var kgroupName =  $.trim($("#kgroupName").val());
            var kgroupName_length = kgroupName.length*1;
            if(remark_length> 20){
                parent.layer.msg("备注最多可输入20个字符", {icon:2});
                return false;
            }
            if(kgroupName_length> 10){
                parent.layer.msg("组别名称最多可输入10个字符", {icon:2});
                return false;
            }
            if(kgroupName_length <= 0){
                parent.layer.msg("请填写组别名称", {icon:2});
                return false;
            }
            var index = layer.confirm('确认提交？', {
                btn: ['确定','返回修改'] //按钮
            }, function(){
                var params = $("#Form").serialize();
                var _p = parent;
                if($("input[name='categoryNameList']").val() == ''){
                    parent.layer.msg("关键字类别不能为空", {icon:2});
                    return ;
                }
                $.ajax( {
                    type : "POST",
                    url : "${ctx}/keyWordGroup/Save",
                    data : params,
                    success : function(res) {
                        if(res.code == 500){
                            layer.msg(res.msg, {icon:2,time: 1500},function () {

                            })
                            return;

                        }else if(res.code == 200){
                            layer.msg(res.msg, {icon:1,time: 1500},function () {
                                parent.layer.closeAll();
                                _p.location.reload();
                            })
                            return;

                        }
                        layer.msg(res.msg, {icon: 1,time: 1500},function(){
                            _p.location.reload();
                        });
                        closePage()
                        parent.window.location.reload();

                    }
                });
            },function () {
                layer.close(index)
            });
        })
        //取消
        $(".js-back").click(function(){
            closePage()
        })
        function closePage() {
            var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
            parent.layer.close(index); //执行关闭
        }
    });
    vm = new Vue({
        el : "#keyword",
        mounted: function(){
            var that = this;
            $.ajax({
                type : "post",
                async: false,
                url : "${ctx}/keyWordSconfiguration/getAuditKeywordCategory",
                success : function(data) {

                    if (data != null) {
                        that.options1 = data;
                        that.value1 = dataArr;
                        $("input[name='categoryNameList']").val(dataArr)
                    }else{
                        console.log("查询出错！");
                    }
                }
            });
        },
        data : {
            options1 : [],
            value1: '',
        },
        watch : {
            value1 : function(value){
                this.$nextTick(function(){
                    $("input[name='categoryNameList']").val(value);
                    console.log("watch value1 :" + $("input[name='categoryNameList']").val());
                })
            }
        }
    });
</script>
</body>
</html>
