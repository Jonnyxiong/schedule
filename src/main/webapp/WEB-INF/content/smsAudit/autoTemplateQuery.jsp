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
							<li class="active"><a href="${ctx}/smsaudit/autoTemplate/query">已审核</a></li>
							<li><a href="${ctx}/smsaudit/autoTemplate/waitquery">待审核</a></li>
						</ul>
					</nav>
				</div>
				<div class="widget-box">
					<!-- 批量添加模板弹出框 -->
					<div class="modal hide fade" id="importExcelBox" tabindex="-1" role="dialog">
						<div class="modal-header"><!-- <button class="close" type="button" data-dismiss="modal">X</button> -->
							<h3 id="myModalLabel">批量添加智能模板</h3>
						</div>
						<div class="modal-body text-center">
							<form class="form-horizontal" method="post" id="importForm">
								<input type="file" id="importInput" name="upload" accept=".xls"/>
							</form>
							<div class="controls">
								<span id="msg" style="display:none;"></span>
								<span id="tips" style="display:none;">点击<a href='${ctx}/smsaudit/exportImportResult'><font color="green">下载</font></a>导入结果列表</span>
							</div>
						</div>
						<div class="modal-footer">
							<a href="#" class="btn" onclick="downloadExcelTemplate()">下载Excel模板</a>
							<a href="#" class="btn" data-dismiss="modal" onclick="closeImport()">关闭</a>
							<a href="#" class="btn btn-primary" onclick="importExcel(this)">导入</a>
						</div>
					</div>

					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						<h5></h5>
						<div class="search">

						 	 <form method="post"  id="mainForm" action="${ctx}/smsaudit/autoTemplate/query">
						 	 	<div class="search">
									<ul>
										<li>
											<label class="control-label">审核状态&nbsp;&nbsp;</label>
											<u:select id="state" value="${param.state}" data="[
													{value:'99',text:'所有状态'},
													{value:'1',text:'审核通过'},
		               							    {value:'3',text:'审核不通过'}]"/>
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
											<label class="control-label">用户账号&nbsp;&nbsp;</label>
											<select  name="clientId" id="clientId"  maxlength="40"  class="txt_250">
											</select>
											<input type="hidden" id="clientIdHidden" value="${clientIdHidden}">
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
										<li>
											<label class="control-label">审核人&nbsp;&nbsp;</label>
											<input type="text" name="adminName" value="<s:property value="#parameters.adminName"/>" maxlength="40" placeholder="审核人姓名" class="txt_250" />
										</li>
										<li name="">
											<label class="control-label">更新时间&nbsp;&nbsp;</label>
											<u:date id="startTime" value="${startTime}" placeholder="请选择开始时间" params="minDate:'%y-%M-{%d-90} 00:00:00', maxDate:'#F{$dp.$D(\\'endTime\\')||\\'%y-%M-%d %H:%m:%\\'}'" />
											<span>至</span>
											<u:date id="endTime" value="${endTime}" placeholder="请选择结束时间" params="minDate:'#F{$dp.$D(\\'startTime\\')||\\'%y-%M-{%d-90} 00:00:00\\'}', maxDate:'%y-%M-%d %H:%m:%s'" />
										</li>
										<li style="clear:both;"></li>
										<div style="float: right">
										<li><button id="searchBtn" type="submit" class="btn btn-info btn-small">搜索</button></li>
										<li><a href="javascript:;" class="btn btn-warning btn-small" id="reset">重置</a></li>
										<li><a href="javascript:;" class="btn btn-success btn-small" id="newTemp">添加模板</a></li>
										<li><a href="javascript:;" class="btn btn-success btn-small" data-toggle="modal" data-target="#importExcelBox">批量添加模板</a></li>
                                        <li><a href="javascript:;" class="btn btn-success btn-small" style="background: #00b396" id="exportExcel" onclick="exportExcel()">导出报表</a></li>
										</div>
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
									<th>提交来源</th>
									<th>创建者</th>
									<th>创建时间</th>
									<th>审核状态</th>
									<th>原因</th>
									<th>审核人</th>
									<th>更新时间</th>
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
											<s:if test="submitType == 3">系统自动提交</s:if>
											<s:if test="submitType == 0">客户提交</s:if>
											<s:if test="submitType == 1">代理商提交</s:if>
											<s:if test="submitType == 2">平台提交</s:if>

										</td>
										<td>
											<s:if test="submitType == 3">-</s:if>
											<s:else>${userName}</s:else>

										</td>
										<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${createTime}" /></td>
										<td>
											<s:if test="state == 1">审核通过</s:if>
											<s:elseif test="state == 3">审核不通过</s:elseif>
										</td>
										<td><s:if test="state == 3">${remark}</s:if>
											<s:else>-</s:else></td>
										<td>${adminName}
										</td>
										<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${updateTime}" /></td>
										<td class="operation_${templateId}">
											<a href="javascript:;" onclick="edit('${templateId}', '${clientId}', '${smsType}' ,'${templateType}','${submitType}','${userName}')">审核</a><br>
											<a href="javascript:;" onclick="del('${templateId}')">删除</a>
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
        var max_export_excel_num = '${max_export_excel_num}';
        window.onload=function(){
            //定时器每分钟调用一次getNowFormatDate()
            setInterval(function(){
                $("#endTime").val(getNowFormatDate());
            },1000);
        }




        //获取当前时间
        function getNowFormatDate() {
            var date = new Date();
            var seperator1 = "-";
            var seperator2 = ":";
            var month = date.getMonth() + 1;
            var strDate = date.getDate();
            if (month >= 1 && month <= 9) {
                month = "0" + month;
            }
            if (strDate >= 0 && strDate <= 9) {
                strDate = "0" + strDate;
            }
            var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
                + " " + date.getHours() + seperator2 + date.getMinutes()+seperator2+ date.getSeconds();
            return currentdate;
        }
	$(function(){
	    if($("#submitType").val()=='0'||$("#submitType").val()==1||$("#submitType").val()==2){
            $("#userNameID").attr("disabled",false);
		}else{
            $("#userNameID").attr("disabled",true);
		}
        var select2Date;

        $("#newTemp").click(function(){
            layer.open({
                type: 2,
                title: '新增模板',
                shadeClose: true,
                shade: 0.8,
                area: ['700px', '90%'],
                content: '/smsaudit/autoTemplate/add' //iframe的url
            });
		})
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
            var smonth = d.getMonth();
            var emonth = d.getMonth() + 1;
            var stime=d.getFullYear()+"-"+smonth+"-"+d.getDate()+" 00:00:00"
            var etime=d.getFullYear()+"-"+emonth+"-"+d.getDate()+" "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();

            $('#startTime').val(stime);
            $('#endTime').val(etime);
            $("#clientId").select2("val", "");


        })


    })

    function edit(templateId, clientId, smsType,templateType, submit_type, userName){
	    if(submit_type == 3){
            var url = '${ctx}/smsaudit/autoTemplate/add?templateId=' + templateId + "&clientId=" + clientId + "&smsType=" + smsType
                 + "&templateType=" + templateType + "&submitType=" + '系统自动提交'  + "&userName=" + userName;
//        url = encodeURIComponent(url);
		}else if(submit_type == 2){
            var url = '${ctx}/smsaudit/autoTemplate/add?templateId=' + templateId + "&clientId=" + clientId + "&smsType=" + smsType
                + "&templateType=" + templateType + "&submitType=" + '平台提交'  + "&userName=" + userName ;
//        url = encodeURIComponent(url);
        }else if(submit_type == 0){
            var url = '${ctx}/smsaudit/autoTemplate/add?templateId=' + templateId + "&clientId=" + clientId + "&smsType=" + smsType
                + "&templateType=" + templateType + "&submitType=" + '客户提交'  + "&userName=" + userName ;
//        url = encodeURIComponent(url);
        }else if(submit_type == 1){
            var url = '${ctx}/smsaudit/autoTemplate/add?templateId=' + templateId + "&clientId=" + clientId + "&smsType=" + smsType
                + "&templateType=" + templateType + "&submitType=" + '代理商提交'  + "&userName=" + userName ;
//        url = encodeURIComponent(url);
        }

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

        //导出Excel文件
        function exportExcel() {
            var totalCount = ${page.totalCount};
            if (totalCount == 0) {
                layer.alert("共0条记录，导出Excel文件失败");
                return;
            }
            if(max_export_excel_num  && totalCount > max_export_excel_num){
                layer.alert("导出Excel文件条数大于"+max_export_excel_num+"条");
                return;
            }else if (totalCount > 6000) {
                layer.alert("导出Excel文件条数大于6000条");
                return;
            }
            var msg = "正在生成报表，请稍后...";
            layer.msg('<div style="color:#506470;font-family: "microsoft yahei","Arial Narrow",HELVETICA;">'+msg+'</div>', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto',time: 600000}) ;
            var options = {
                url:"${ctx}/smsaudit/autoTemplate/export",
                data:{
                    _state:$("#state").find("option:selected").text(),
                    _smsType:$("#smsType").find("option:selected").text(),
                    _templateType:$("#templateType").find("option:selected").text(),
                    _submitType:$("#submitType").find("option:selected").text()
                },
                success : function(result) {
                    layer.closeAll(); //疯狂模式，关闭所有层
                    if(result == null){
                        layer.msg("导出错误，请稍后重试...", {icon:2});
                    }else if(result.success == true){
                        var exportFileName = result.data;
                        var turnForm = document.createElement("form");
                        document.body.appendChild(turnForm);
                        turnForm.method = 'post';
                        turnForm.action = "${ctx}/file/downExcel";
                        //创建表单
                        var newElement = document.createElement("input");
                        newElement.setAttribute("name","exportFileName");
                        newElement.setAttribute("type","hidden");
                        newElement.setAttribute("value",exportFileName);
                        turnForm.appendChild(newElement);
                        turnForm.submit();
                    }else{
                        layer.msg(result.msg, {icon:2},1500);

                    }
                },
                complete : function(XMLHttpRequest,status){
                    console.log(status);
                },
                type : "post",
                async: true,
                timeout:30000
            };
            $("#mainForm").ajaxSubmit(options);
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

    function downloadExcelTemplate(){
        var url = "${ctx}/smsaudit/downloadExcelTemplate";
        jQuery('<form action="'+url+'" method="post"></form>').appendTo('body').submit().remove();
    }

    //Excel批量添加智能模板
    function importExcel(btn) {
        $("#msg").hide();
        $("#tips").hide();
        var index = "" ;
        var options = {
            beforeSubmit : function() {
                $(btn).attr("disabled", true);
                index = layer.load(1, {
                    shade: [0.5,'#fff'] //0.5透明度的白色背景
                });
            },
            success : function(data) {
                $(btn).attr("disabled", false);
                layer.close(index);
                if(data == null){
                    $("#msg").text("服务器错误，请联系管理员").show();
                    return;
                }

                if(data.code == 500){
                    $("#msg").text(data.msg).show();
				}else{
					$("#tips").show();
                	$("#msg").text(data.msg).show();
				}

            },
            url : "${ctx}/smsaudit/addAutoTemplateBatch",
            type : "post",
//            async : false,
            timeout:30000
        };
        $("#importForm").ajaxSubmit(options);
    }

    function closeImport(){
        $("#msg").hide();
        window.location.reload();
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