<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html lang="zh-cn">
<head>
    <title>用户关键字配置</title>
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
    </style>
</head>
<body menuId="3301" >
    <!--Action boxes-->
    <div class="container-fluid">
    <hr>
    <div class="row-fluid">
        <div class="span12">

            <div class="widget-box">
                <div class="search">

                    <form method="post" id="mainForm" action="${ctx}/keywordsconfiguration/list">
                        <div class="search">
                            <ul style="float: right;margin:10px 0 0 0;">
                                <li>
                                    <span class="control-label">组别：&nbsp;&nbsp;</span>
                                    <select id="modelId" name="modelId">
                                    </select>
                                </li>
                                <li style="margin-left: 15px;margin-right: 15px;">

                                    <input type="text" id="mistiness" name="mistiness"  maxlength="40"
                                           placeholder="用户ID/用户名称/用户组名称/类别名称" class="txt_250" />
                                </li>
                                <li><a id="searchBtn" class="btn btn-info btn-small">查询</a>&nbsp;&nbsp;</li>
                                <li><a href="javascript:;" class="btn btn-warning btn-small"  id="newTemp" >添加用户组</a>&nbsp;&nbsp;</li>
                            </ul>
                        </div>
                    </form>
                </div>
                <div class="widget-content nopadding">
                    <table id="auditTable" class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th style="width:5%">用户组ID</th>
                            <th style="width:5%">用户组名称</th>
                            <th>用户组内用户</th>
                            <th style="width:13%">审核关键字组别</th>
                            <th>组内审核关键字类别</th>
                            <th style="width:5.5%">备注</th>
                            <th style="width:5%">操作人</th>
                            <th style="width:10.5%">更新时间</th>
                            <th style="width:11.5%">操作</th>
                        </tr>
                        </thead>
                        <tbody id="auditBody">
                        <s:iterator value="page.list" status="st">
                            <tr>
                                <td>${st.index + 1}</td>
                                <td>${cgroupId}</td>
                                <td>
                                    <c:choose>
                                       <c:when test="${cgroupName =='*'}">默认</c:when>
                                        <c:otherwise>${cgroupName}</c:otherwise>
                                    </c:choose>
                                </td>
                                <td class='sms_content'>
                                    <c:choose>
                                        <c:when test="${cgroupName =='*'}">默认</c:when>
                                        <c:otherwise>${name}</c:otherwise>
                                    </c:choose>
                                </td>
                                <td class='sms_content'>
                                    <s:if test="isDefault == 1">
                                        ${kgroupName} <span style="color: skyblue;">(默认组别)</span>
                                    </s:if>
                                    <s:if test="isDefault == 0">
                                        ${kgroupName}
                                    </s:if>
                                </td>
                                <td><c:out value="${categoryName}"/></td>
                                <td class="sms_status status">${remark}</td>
                                <td>${userName}</td>
                                <td>${updateTimeStr}</td>
                                <td class="operation">
                                    <s:if test="isDefault == 0">
                                    <a href="javascript:;" onclick="configure('${cgroupId}')" title="配置用户组内成员及对应关键字级别">配置</a>
                                    | <a href="javascript:;" onclick="edit('${cgroupId}')">编辑</a>
                                    | <a href="javascript:;" onclick="del('${cgroupId}')">删除</a>
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
        <input type="hidden" id="mistiness_ID"  value="${data.mistiness}"  >
        <input type="hidden" id="kgroupId_ID"  value="${data.kgroupId}"  >
</div>
    <script src="${ctx}/js/doublebox-bootstrap.js"></script>
    <script type="text/javascript">
        $(function () {
            $("#mistiness").val($("#mistiness_ID").val());
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
        $("#newTemp").click(function(){
            layer.open({
                type: 2,
                title: '添加用户组',
                shadeClose: true,
                shade: 0.8,
                area: ['700px', '50%'],
                content: '/keyWordSconfiguration/add'
            });
        })
        $("#searchBtn").click(function(){
            $("#mainForm").submit();
        })
        function edit(cgroupId){
            layer.open({
                type: 2,
                title: '编辑用户组',
                shadeClose: true,
                shade: 0.8,
                area: ['700px', '50%'],
                content: '/keyWordSconfiguration/add?cgroupId='+cgroupId
            });

        }
        function configure(cgroupId,cgroupName){
            layer.open({
                type: 2,
                title: '审核关键字配置',
                shadeClose: true,
                shade: 0.8,
                area: ['850px', '80%'],
                content: '/keyWordSconfiguration/configure?cgroupId='+cgroupId,
                end: function () {
                    location.reload();
                }
            });
        }



        function del(cgroupId){
            layer.confirm('是否确认删除？', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    url : "${ctx}/keyWordSconfiguration/del",
                    type: 'POST',
                    dataType : 'json',
                    data : {
                        cgroupId : cgroupId
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
                        });
                        parent.window.location.reload();
                    }
                })
            });
        }



    </script>
</body>
</html>
