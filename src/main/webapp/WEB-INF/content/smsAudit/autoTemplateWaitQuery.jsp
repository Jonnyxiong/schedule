<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<title>智能模板</title>
	<style type="text/css">
		.widget-title .search,.widget-title .search .ul_left { float: left; display: inline; /*height: 36px;*/ margin-bottom: 0px; margin-left: 0px; }
		.widget-title .search,.widget-title .search .ul_right { float: right; display: inline; /*height: 36px;*/ margin-bottom: 0px; margin-left: 0px; }
		.sms_content{  word-wrap: break-word;
			word-break: break-all;
			overflow: hidden;
			max-height: 320px;
			min-height: 20px;
			display: block;
		}
		.sms_status{  word-wrap: break-word;
			word-break: break-all;
		}
		.auditlabel {
			display: inline;
			padding: .2em .6em .3em;
			/* 	    font-size: 75%; */
			font-weight: bold;
			line-height: 1.5em;
			color: #fff;
			text-align: center;
			white-space: nowrap;
			vertical-align: baseline;
			border-radius: .25em;
			float: left;
			margin-right: 20px;
			margin-top: 7px;
		}
		.label-danger {
			background-color: #d9534f;
		}
		.widget-title .search input[type='button'],.widget-title .search input[type='submit'] { background: #3a87ad;}
	</style>
</head>
<body menuId="283">
<!--Action boxes-->
<div class="container-fluid">
	<hr>

	<div class="row-fluid">
		<div class="span12">
			<div class="widget-title">
				<hr>
				<nav class="navbar navbar-success" role="navigation">
					<ul class="nav nav-tabs nav-justified" role="tablist">
						<li ><a href="${ctx}/smsaudit/autoTemplate/query">已审核</a></li>
						<li class="active"><a href="${ctx}/smsaudit/autoTemplate/waitquery">待审核</a></li>
					</ul>
				</nav>
			</div>
			<div class="widget-box">
				<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
					<h5></h5>
					<div class="search">
						<form method="post" id="mainForm" action="${ctx}/smsaudit/autoTemplate/waitquery">
							<div class="search">
								<ul class="ul_left">
									<li class="span_num">
										<span id="auditNum" class="auditlabel label-danger" style="display:block;">待审核总记录数</span>
										<span id="sendNum" class="auditlabel label-danger" style="display:block;">平台提交待审核记录</span>
										<span id="lockNum" class="auditlabel label-danger" style="display:block;">系统自动提交待审核记录</span>
										<span id="OEMNum" class="auditlabel label-danger" style="display:block;">客户提交待审核记录</span>
										<span id="OEMAgentNum" class="auditlabel label-danger" style="display:block;">代理商平台提交待审核记录</span>
									</li>
								</ul>
								<ul>
									<li>
										<label class="control-label">用户账号&nbsp;&nbsp;</label>
										<select  name="clientId" id="clientId"  maxlength="40"  class="txt_250">
										</select>
										<input type="hidden" id="clientIdHidden" value="${clientIdHidden}">
									</li>
									<li>
										<label class="control-label">模板属性&nbsp;&nbsp;</label>
										<u:select id="smsType" value="${param.smsType}" data="[
													{value:'',text:'全部类型'},
													{value:'10',text:'行业'},
		               							    {value:'11',text:'营销'}]"/>
									</li>
									<li>
										<label class="control-label">模板类型&nbsp;&nbsp;</label>
										<u:select id="templateType" value="${param.templateType}" data="[
													{value:'',text:'全部类型'},
													{value:'0',text:'固定模板'},
													{value:'1',text:'变量模板'}]"/>
									</li>
									<li>
										<label class="control-label">模板ID&nbsp;&nbsp;</label>
										<input type="text" name="templateId" value="<s:property value="#parameters.templateId"/>" maxlength="40" placeholder="模板ID" class="txt_250" />
									</li>
									<li>
										<label class="control-label">签名&nbsp;&nbsp;</label>
										<input type="text" name="sign" value="<s:property value="#parameters.sign"/>" maxlength="40" placeholder="签名" class="txt_250" />
									</li>
									<li>
										<label class="control-label">模板内容&nbsp;&nbsp;</label>
										<input type="text" name="content" value="<s:property value="#parameters.content"/>" maxlength="40"
											   placeholder="模板内容" class="txt_250" />
									</li>
									<li>
										<label class="control-label">提交来源&nbsp;&nbsp;</label>
										<u:select id="submitType" value="${param.submitType}" data="[
													{value:'',text:'全部类型'},
													{value:'0',text:'客户提交'},
		               							    {value:'1',text:'代理商提交'},
		               							    {value:'2',text:'平台提交'},
		               							    {value:'3',text:'系统自动提交'}]"/>
									</li>
									<li>
										<label class="control-label">创建者&nbsp;&nbsp;</label>
										<input type="text" id="userNameID" disabled="disabled" name="userName" value="<s:property value="#parameters.userName"/>" maxlength="40" placeholder="创建者" class="txt_250" />
									</li>
									<li name="">
										<label class="control-label">创建时间&nbsp;&nbsp;</label>
										<u:date id="startTime" value="${startTime}" placeholder="请选择开始时间" params="minDate:'%y-%M-{%d-90} 00:00:00', maxDate:'#F{$dp.$D(\\'endTime\\')||\\'%y-%M-%d %H:%m:%\\'}'" />
										<span>至</span>
										<u:date id="endTime" value="${endTime}" placeholder="请选择结束时间" params="minDate:'#F{$dp.$D(\\'startTime\\')||\\'%y-%M-{%d-90} 00:00:00\\'}', maxDate:'%y-%M-%d %H:%m:%s'" />
									</li>
									<li>
										<label class="control-label">审核排序&nbsp;&nbsp;</label>
										<u:select id="querydesc" value="${param.querydesc}" data="[
													{value:'2',text:'按匹配发送量由高到低'},
													{value:'1',text:'按创建时间倒序'}]"/>
									</li>
									<li><button id="searchBtn" type="submit" class="btn btn-info btn-small">搜索</button></li>
									<li><a href="javascript:;" class="btn btn-warning btn-small" id="reset">重置</a></li>
								</ul>
							</div>
						</form>
					</div>
				</div>
				<div class="widget-content nopadding">
					<table class="table table-bordered table-striped">
						<thead>
						<tr>
							<th>序号</th>
							<th>模板ID</th>
							<th>用户账号</th>
							<th>模板属性</th>
							<th>模板类型</th>
							<th style="width: 25%">模板内容</th>
							<th>短信签名</th>
							<th>匹配发送量(条)</th>
							<th>提交来源</th>
							<th>创建者</th>
							<th>创建时间</th>
							<th>操作</th>
						</tr>
						</thead>
						<tbody id="auditBody">
						<s:iterator value="page.list" status="st">
							<tr>
								<td>${st.index + 1}</td>
								<td>${templateId}</td>
								<td>${clientId}</td>
								<td>
									<s:if test="smsType == 10">行业</s:if>
									<s:elseif test="smsType == 11">营销</s:elseif>
								</td>
								<td>
									<s:if test="templateType == 0">固定模板</s:if>
									<s:elseif test="templateType == 1">变量模板</s:elseif>
								</td>
								<td style="width: 25%"><c:out value="${content}"/></td>
								<td><c:out value="${sign}"/></td>
								<td>
									<s:if test="submitType == 3">
										${matchAmount}
										<input type="hidden" value="${smsContent}">
									</s:if>
									<s:else>
										-
									</s:else>
								</td>
								<td>
									<s:if test="submitType == 0">客户提交</s:if>
									<s:if test="submitType == 1">代理商提交</s:if>
									<s:elseif test="submitType == 2">平台提交</s:elseif>
									<s:elseif test="submitType == 3">系统自动提交</s:elseif>
								</td>
								<td>
									<s:if test="submitType == 3">
										-
									</s:if>
									<s:else>
										${userName}
									</s:else>
								</td>
								<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${createTime}"/></td>
								<td class="operation_${templateId}">
									<a href="javascript:;" onclick="edit('${templateId}','${clientId}','${smsType}','${templateType}','${submitType}','${userName}')">审核</a><br>
									| <a href="javascript:;" onclick="del('${templateId}')">删除</a>
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
</div>
<script type="text/javascript">

    var _getAuditNum; // 查询审核数量定时器
    $(function(){
        if($("#submitType").val()=='0'||$("#submitType").val()==1||$("#submitType").val()==2){
            $("#userNameID").attr("disabled",false);
        }else{
            $("#userNameID").attr("disabled",true);
        }

        // 查询待审核记录数
        startGetAuditNum();
        var select2Date;

        // 离开页面时释放审核记录（退出按钮时无效）
		/* window.onunload = function(){
		 try{
		 clearInterval(_getAuditNum);
		 }catch(e){
		 console.error(e);
		 }

		 if(getcookie('auditPage')==1){
		 releaseSmsAudit();
		 }
		 } */

        function releaseSmsAudit(){
            $.ajax({
                type : "post",
                url : "${ctx}/smsaudit/updateStatus",
                async: false,
                data : {
                    auditIds : getAllSelected().notReleaseIds,
                    status : 0
                },
                success : function(data) {
                    addcookie('auditPage', 0, 1);
                    $("#auditBody").find("tr").remove(); // 清除页面记录
                    if (data.result == null) {
                        $("#msg").text("服务器错误，请联系管理员").show();
                        return;
                    }
                }
            });

        }

        //获取用户账号
        $.ajax({
            url : '${ctx}/smsaudit/autoTemplate/accounts',
            dataType : 'json',
            async : false,
            success : function(res){
                for(var i = 0; i < res.length; i++){
                    res[i].id = res[i].clientid;
                    res[i].text = res[i].clientid + "-" + res[i].name;
                }
                res.unshift({id:"",text:""})
                select2Date = res;
            }
        })
        $("#clientId").select2({
            data : select2Date
        })
        // select2查询后的值回显
        var clientIdValueFromServer = $("#clientIdHidden").val();
        $("#clientId").val(clientIdValueFromServer).trigger("change");
        $("#reset").click(function(){
            $('.search input').val("");
            $('.search select').val("");
            var d = new Date();
            var emonth = d.getMonth();
            var smonth = d.getMonth() + 1;
            var stime=d.getFullYear()+"-"+emonth+"-"+d.getDate()+" 00:00:00"
            var etime=d.getFullYear()+"-"+smonth+"-"+d.getDate()+" "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
            $('#startTime').val(stime);
            $('#endTime').val(etime);
            $("#clientId").select2("val", "");
        })
    })
    function edit(templateId, clientId, smsType, templateType, submitType, userName ){
        if(submitType == 3){
            //   content=content.replace(/[\r\n]/g,"");//去掉回车换行
            var url = '${ctx}/smsaudit/autoTemplate/add?templateId=' + templateId + "&clientId=" + clientId + "&smsType=" + smsType
                + "&templateType=" + templateType + "&submitType=" + '系统自动提交'  + "&userName=" + userName;
//        url = encodeURIComponent(url);
            url = encodeURI(url);
            layer.open({
                type: 2,
                title: '编辑模板',
                shadeClose: true,
                shade: 0.8,
                area: ['1300px', '90%'],
                content: url //iframe的url
            });
        }else if(submitType == 2){
            //   content=content.replace(/[\r\n]/g,"");//去掉回车换行
            var url = '${ctx}/smsaudit/autoTemplate/add?templateId=' + templateId + "&clientId=" + clientId + "&smsType=" + smsType
                + "&templateType=" + templateType + "&submitType=" + '平台提交'  + "&userName=" + userName ;
//        url = encodeURIComponent(url);
            url = encodeURI(url);
            layer.open({
                type: 2,
                title: '编辑模板',
                shadeClose: true,
                shade: 0.8,
                area: ['700px', '90%'],
                content: url //iframe的url
            });
        }else if(submitType == 0){
            //   content=content.replace(/[\r\n]/g,"");//去掉回车换行
            var url = '${ctx}/smsaudit/autoTemplate/add?templateId=' + templateId + "&clientId=" + clientId + "&smsType=" + smsType
                + "&templateType=" + templateType + "&submitType=" + '客户提交'  + "&userName=" + userName ;
//        url = encodeURIComponent(url);
            url = encodeURI(url);
            layer.open({
                type: 2,
                title: '编辑模板',
                shadeClose: true,
                shade: 0.8,
                area: ['700px', '90%'],
                content: url //iframe的url
            });
        }else if(submitType == 1){
            //   content=content.replace(/[\r\n]/g,"");//去掉回车换行
            var url = '${ctx}/smsaudit/autoTemplate/add?templateId=' + templateId + "&clientId=" + clientId + "&smsType=" + smsType
                + "&templateType=" + templateType + "&submitType=" + '代理商提交'  + "&userName=" + userName ;
//        url = encodeURIComponent(url);
            url = encodeURI(url);
            layer.open({
                type: 2,
                title: '编辑模板',
                shadeClose: true,
                shade: 0.8,
                area: ['700px', '90%'],
                content: url //iframe的url
            });
        }
    }
    function del(id){
        layer.confirm('确认删除模板？', {
            btn: ['确定','取消'] //按钮
        }, function(){
            $.ajax({
                url : "${ctx}/smsaudit/autoTemplate/del",
                type: 'POST',
                dataType : 'json',
                data : {
                    templateId : id
                },
                success : function(res){
                    if(res.code != 0){
                        layer.msg(res.msg, {icon:2});
                        return;
                    }
                    layer.msg("删除成功", {icon: 1,time: 1500},function(){
                      //  location.reload();
						console.log("--------------执行清空操作"+id);
						$(".operation_"+id).empty();
						 $(".operation_"+id).html("<span style='color: red'>已删除</span>");
                        $(".operation_"+id).siblings().css("color","red").css("text-decoration","line-through");
                        console.log("--------------执行清空操作完毕"+id);
                    });
                }
            })
        });
    }

    function getAuditNum(){
        $.ajax({
            type : "post",
            url : "${ctx}/smsaudit/autoTemplate/getTemplateAuditNum",
            success : function(data) {
                var auditNum = 0;
                var sendNum = 0;
                var lockNum = 0;
                var OEMAgentNum =0;
                var OEMNum = 0;
                if(data != null){
                    auditNum = data.auditNum;
                    sendNum = data.sendNum;
                    lockNum = data.lockNum;
                    OEMAgentNum = data.OEMAgentNum;
                    OEMNum = data.OEMNum;
                }

                // 待审核提示信息
                if(auditNum > 0){
                    var text1 = "待审核总记录数(" + auditNum + ")";
                    var text2 = "平台提交待审核记录(" + sendNum + ")";
                    var text3 = "系统自动提交待审核记录(" + lockNum + ")";
                    var text4 = "客户提交待审核记录(" + OEMNum + ")";
                    var text5 = "代理商平台提交待审核记录(" + OEMAgentNum + ")";
                    $("#auditNum").text(text1);
                    $("#sendNum").text(text2);
                    $("#lockNum").text(text3);
                    $("#OEMNum").text(text4);
                    $("#OEMAgentNum").text(text5);

                    $("#auditNum").show();
                    $("#sendNum").show();
                    $("#lockNum").show();
                    $("#OEMNum").show();
                    $("#OEMAgentNum").show();
                }else{
                    $("#auditNum").hide();
                    $("#sendNum").hide();
                    $("#lockNum").hide();
                    $("#OEMNum").hide();
                    $("#OEMAgentNum").hide();
                }
            }
        });
    }

    // 定时查询待审核记录数
    function startGetAuditNum(){
        getAuditNum();
        var _getAuditNum = window.setInterval(function(){// 初始化定时器
            getAuditNum();
        }, 5000)
    }
    $("#submitType").change(function () {
        var mark = $(this).val();
        var userNameID = $("#userNameID").val();
        if(mark == "2" || mark == "0" || mark == "1" ){
            $("#userNameID").attr("disabled",false);
        }else if(!mark){
            $("#userNameID").attr("disabled",true);
        }else {
            $("#userNameID").attr("disabled",true);
        }
        if(userNameID !='' && (mark == "" || mark == "3")){
            $("#userNameID").val("");
            layer.msg("只有客户提交、代理商提交和平台提交可以搜索创建者！！");
        }
    })
</script>
</body>
</html>