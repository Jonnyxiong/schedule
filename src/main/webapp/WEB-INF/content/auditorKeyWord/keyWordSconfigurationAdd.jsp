<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
    <title>添加用户组</title>
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
    </style>
</head>

<body menuId="3301">
<!--Action boxes-->
<div class="container-fluid">
    <hr>
    <div class="row-fluid">
        <div class="span12">
            <form class="form-horizontal" id="form">
                <input type="hidden" name="cgroupId" id="cgroupId">
                <div class="control-group">
                    <label class="control-label" for="cgroupName"><span class="red">*</span>用户组名称</label>
                    <div class="controls select2">
                        <input type="text" id="cgroupName" name="cgroupName"  placeholder="20个字符以内">
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="remark">备注</label>
                    <div class="controls">
                        <input type="text" id="remark" name="remark"  placeholder="20个字符以内">
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <%--<button type="submit" class="btn">Sign in</button>--%>
                        <a href="javascript:;" class="btn btn-primary btn-small js-save">确定</a>
                        <a href="javascript:;" class="btn btn-small js-back">取消</a>
                    </div>

                </div>
            </form>
            <input type="hidden" id="cgroupName_ID"  value="${data.cgroupName}"  >
            <input type="hidden" id="remark_ID"  value="${data.remark}" >
            <input type="hidden" id="cgroup_ID"  value="${data.cgroupId}" >
        </div>
    </div>
</div>

<script type="text/javascript">
    $(function(){
        var cgroupName = $("#cgroupName_ID").val();
        var remark = $("#remark_ID").val();
        var cgroupId = $("#cgroup_ID").val();
        var select2Date;
        if(cgroupId != "" && cgroupId != undefined && cgroupId != null){
            //如果是编辑页面
            $("#cgroupName").val(cgroupName);
            $("#remark").val(remark);
            $("#cgroupId").val(cgroupId);
        }
        //提交
        $(".js-save").click(function(){
            var remark = $.trim($("#remark").val());
            var remark_length = remark.length*1;
            var cgroupName =  $.trim($("#cgroupName").val());
            var cgroupName_length = cgroupName.length*1;
            if(remark_length> 20){
                    parent.layer.msg("备注最多可输入20个字符", {icon:2});
                    return false;
            }
            if(cgroupName_length> 20){
                parent.layer.msg("用户组名称最多可输入20个字符", {icon:2});
                return false;
            }
            if(cgroupName_length <= 0){
                parent.layer.msg("请填写用户组名称", {icon:2});
                return false;
            }
            var index = layer.confirm('确认提交？', {
                btn: ['确定','返回修改'] //按钮
            }, function(){
                var params = $("#form").serialize();
                var _p = parent;
                $.ajax( {
                    type : "POST",
                    url : "${ctx}/keyWordSconfiguration/Save",
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

       /* function getQueryString(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var url = decodeURI(window.location.search);
            var r = url.substr(1).match(reg);
            if (r != null) return unescape(r[2]); return "";
        }*/
    });
</script>
</body>
</html>
