<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html lang="zh-cn">
<head>
    <title>审核关键字分组</title>
    <link rel="stylesheet" href="${ctx}/css/doublebox-bootstrap.css" />
    <style>
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
        .el-select-dropdown{
            z-index: 20171101 !important;
        }

        #keyword .el-select{
            width: 500px;
        }

        #keyword .el-select .el-tag {
            height: 32px;
            line-height: 32px;
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

        .title-word{
            position: absolute;
            top: 20px;
        }
    </style>
    <!-- 引入element-ui样式 -->
    <link rel="stylesheet" href="${ctx}/js/element-ui/element-ui.css" />
    <!-- 引入element-ui组件库 -->
    <script src="${ctx}/js/element-ui/vue.min.js"></script>
    <script src="${ctx}/js/element-ui/element-ui.js"></script>
</head>
<body menuId="3300" >
<!--Action boxes-->
<div class="container-fluid">
    <hr>
    <div class="row-fluid">
        <div class="span12">

            <div class="widget-box">
                <div class="search">
                    <form method="post" id="mainForm" action="${ctx}/keywordgroup/list">
                        <div class="search">
                            <ul style="float: right;margin:10px 0 0 0;">
                                <li>
                                    <span class="control-label">组别：&nbsp;&nbsp;</span>
                                    <select id="modelId" name="modelId">
                                    </select>
                                </li>
                                <li style="margin-left: 15px;margin-right: 15px;">
                                    <input type="text" id="categoryName" name="categoryName"  maxlength="40"
                                           placeholder="类别名称" class="txt_250" />
                                </li>
                                <li><a id="searchBtn"  class="btn btn-info btn-small">查询</a>&nbsp;&nbsp;</li>
                                <li><a href="javascript:;" class="btn btn-warning btn-small"  id="newTemp">创建组别</a>&nbsp;&nbsp;</li>
                            </ul>
                        </div>
                    </form>
                    <div class="title-word">&nbsp;&nbsp;注：默认组别适用于未配置关键字组别的用户，且第一个创建的组别为默认组别。</div>
                </div>
                <div class="widget-content nopadding">
                    <table id="auditTable" class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th style="width:5%">组别ID</th>
                            <th style="width:14%">组别名称</th>
                            <th>组内审核关键字类别</th>
                            <th>备注</th>
                            <th>操作人</th>
                            <th style="width:10.5%">更新时间</th>
                            <th style="width:9.5%">操作</th>
                        </tr>
                        </thead>
                        <tbody id="auditBody">
                        <s:iterator value="page.list" status="st">
                            <tr>
                                <td>${st.index + 1}</td>
                                <td>${kgroupId}</td>
                                <td>
                                    <s:if test="isDefault == 1">
                                        ${kgroupName} <span style="color: skyblue;">(默认组别)</span>
                                    </s:if>
                                    <s:if test="isDefault == 0">
                                        ${kgroupName}
                                    </s:if>
                                </td>
                                <td>${categoryName}</td>
                                <td >${remark}</td>
                                <td>${userName}</td>
                                <td>${updateTimeStr}</td>
                                <td class="operation">
                                    <a href="javascript:;" onclick="edit('${kgroupId}')">编辑</a>
                                    <s:if test="isDefault == 0">
                                        |
                                    <a href="javascript:;" onclick="del('${kgroupId}')">删除</a>
                                    </s:if>
                                </td>
                            </tr>
                        </s:iterator>
                        </tbody>
                    </table>
                </div>
                <u:page page="${page}" formId="mainForm" />
            </div>
        </div>
    </div>
    <input type="hidden" id="categoryName_ID"  value="${data.categoryName}"  >
    <input type="hidden" id="kgroupId_ID"  value="${data.kgroupId}"  >
</div>


<script type="text/javascript">
    $(function () {
        $("#categoryName").val($("#categoryName_ID").val());
        var option;
        $("select[name=modelId]").empty();      //清空
        $.ajax({url:'/keyWordSconfiguration/getGroup',
            type:"post",
            cache: false,
            error:function(){
            },
            success:function(data){
                var modelList = data;
                if($("#kgroupId_ID").val()==''|| $("#kgroupId_ID").val() == undefined || $("#kgroupId_ID").val() == null) {
                    option = "<option value=\"" + '' + "\" selected=\"selected\"";
                    option += ">全部</option>";
                }else{
                    option = "<option value=\"" + '' + "\"";
                    option += ">全部</option>";
                }
                $("select[name=modelId]").append(option);
                if(modelList && modelList.length != 0){
                    for(var i=0; i<modelList.length; i++){
                        if($("#kgroupId_ID").val()!=''&& $("#kgroupId_ID").val() != undefined && $("#kgroupId_ID").val() != null){
                            if(modelList[i].kgroupId == $("#kgroupId_ID").val()!=''){
                                option="<option value=\""+modelList[i].kgroupId+"\" selected=\"selected\"";
                                option += ">"+modelList[i].kgroupName+"</option>";  //动态添加数据
                                $("select[name=modelId]").append(option);
                            }else{
                                option="<option value=\""+modelList[i].kgroupId+"\"";
                                option += ">"+modelList[i].kgroupName+"</option>";  //动态添加数据
                                $("select[name=modelId]").append(option);
                            }
                        }else{
                            option="<option value=\""+modelList[i].kgroupId+"\"";
                            option += ">"+modelList[i].kgroupName+"</option>";  //动态添加数据
                            $("select[name=modelId]").append(option);
                        }
                    }
                }
            }
        });
    })
    $("#searchBtn").click(function(){
        $("#mainForm").submit();
    })

    function edit(kgroupId) {
        layer.open({
            type: 2,
            title: '编辑审核关键字组别',
            shadeClose: true,
            shade: 0.8,
            scrollbar: false,
            area: ['700px', '600px'],
            content: '/keyWordGroup/add?kgroupId='+kgroupId,
            end:function(){
                location.reload();
            }
        });

    }
    $("#newTemp").click(function(){
        layer.open({
            type: 2,
            title: '创建审核关键字组别',
            shadeClose: true,
            shade: 0.8,
            scrollbar: false,
            area: ['700px', '600px'],
            content: '/keyWordGroup/add',
            end:function(){
                location.reload();
            }
        });
    })
    function del(kgroupId){
        layer.confirm('是否确认删除？', {
            btn: ['确定','取消'] //按钮
        }, function(){
            $.ajax({
                url : "${ctx}/keyWordGroup/del",
                type: 'POST',
                dataType : 'json',
                data : {
                    kgroupId : kgroupId
                },
                success : function(res){
                    if(res.code == 500){
                        layer.msg(res.msg, {icon:2,time: 1500},function () {
                        })
                        return;

                    }else if(res.code == 200){
                        layer.msg(res.msg, {icon:1,time: 1500},function () {
                            parent.layer.closeAll();
                            parent.window.location.reload();

                        })
                        return;

                    }
                    layer.msg(res.msg, {icon: 1,time: 1500},function(){
                        parent.window.location.reload();
                    });
                    parent.window.location.reload();
                }
            })
        });
    }
</script>
</body>
</html>
