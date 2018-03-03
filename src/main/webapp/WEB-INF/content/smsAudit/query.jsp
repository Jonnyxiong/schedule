<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>短信审核查询</title>
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
	.ml{
		margin-left: 8%;
	}
	.select2-dropdown{
		z-index: 20170913 !important;
		width: 220px !important;
	}
	.select2-container--default .select2-selection--single .select2-selection__arrow {
		height: 26px;
		position: absolute;
		top: 1px;
		right: 85px;
		width: 20px;
	}
</style>
</head>

<body menuId="82">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<!-- 弹出框 -->
					<div class="modal hide fade" id="updateAccountBox" tabindex="-1" role="dialog">
						<div class="modal-header"><!-- <button class="close" type="button" data-dismiss="modal">X</button> -->
							<h3 id="myModalLabel"></h3>
						</div>
						<div class="modal-body text-center">
							<div class="control-group">
								备注&nbsp&nbsp&nbsp
								<textarea id="remarks" name="remarks" rows="3" cols="60"
											style="height: auto; width: auto;" maxlength="512"
											class="checkRemarks"></textarea>
								<div class="controls">
									<span id="msg" style="color:red;" style="display:none;"></span>
								</div>
							</div>
						</div>
						<div class="modal-footer">
						<a href="#" class="btn" data-dismiss="modal" onclick="closBox()">关闭</a>
						<a href="#" class="btn btn-primary" onclick="updateAccount(this)"></a>
						</div>
					</div>

					<!-- 审核不通过弹出框 -->
					<div class="modal fade" id="AuditnopassBox" tabindex="-1" role="dialog"
						 aria-labelledby="myModalLabel" aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<form method="post" id="AuditnoPassForm">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal"
												aria-hidden="true">&times;</button>
										<h4 class="modal-title" id="myModalLabel1">短信审核确定</h4>
									</div>
									<div class="modal-body form-horizontal">
										<div class="control-group">
											<label class="control-label">确定不通过该短信审核?</label>
										</div>
										<div class="control-group">
											<label class="control-label">不通过原因：</label>
											<div class="controls" name="handOverToId">
												<select  name="opt_remark" id="opt_remark"  placeholder="请选择" maxlength="40"  class="txt_250">

												</select>
											</div>

										</div>
									</div>
									<div class="controls text-center">
										<span id="msg1" style="color:red;" style="display:none;"></span>
									</div>
									<div class="modal-footer">
										<button id="btnTransferAudit" type="button" class="btn btn-primary" onclick="updateStatus(2)">确定</button>
										<button type="button" class="btn btn-default" data-dismiss="modal" onclick="close()">关闭
										</button>

									</div>
								</form>
							</div>

						</div>

					</div>


					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						<h5></h5>
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/smsaudit/query">
						 	 	<div class="search">
							 	 	<ul class="ul_left">
										<li class="span_num">
											<span id="auditNum" class="auditlabel label-danger" style="display:none;">待审核短信数量</span>
											<span id="sendNum" class="auditlabel label-danger" style="display:block;">待发送数量</span>
											<span id="lockNum" class="auditlabel label-danger" style="display:block;">锁定审核数量</span>
										</li>
									</ul>
									<ul>
										<li><input type="text" name="clientid" value="<s:property value="#parameters.clientid"/>" maxlength="40"
										placeholder="用户账号" class="txt_250" /></li>
										<li><input type="text" name="auditid" value="<s:property value="#parameters.auditid"/>" maxlength="40"
										placeholder="审核ID" class="txt_250" /></li>
										<li><input type="text" name="content" value="<s:property value="#parameters.content"/>" maxlength="100"
										placeholder="包含内容" class="txt_250" /></li>
										<li><input type="text" name="sign" value="<s:property value="#parameters.sign"/>" maxlength="100"
																							 placeholder="包含签名" class="txt_250" /></li>
										<li name="testhight1">
											<u:date id="start_time" value="${s_start_time}" placeholder="开始时间" params="minDate:'%y-%M-{%d-45} 00:00:00', maxDate:'#F{$dp.$D(\\'end_time\\')||\\'%y-%M-%d %H:%m:%\\'}'" />
											<span>至</span>
		            						<u:date id="end_time" value="${s_end_time}" placeholder="结束时间" params="minDate:'#F{$dp.$D(\\'start_time\\')||\\'%y-%M-{%d-45} 00:00:00\\'}', maxDate:'%y-%M-%d %H:%m:%s'" />
										</li>
										<li name="testhight">
											<u:select id="status" value="${param.status}" data="[
													{value:'-1',text:'所有状态'},
													{value:'0',text:'待审核'},
													{value:'1',text:'通过'},
		               							    {value:'2',text:'不通过'},
		               							    {value:'3',text:'转审'}]"  />
										</li>
										<li>
											<u:select id="smsType" value="${param.smsType}" data="[
													{value:'',text:'短信类型：所有'},
													{value:'0',text:'通知'},
													{value:'4',text:'验证码'},
		               							    {value:'5',text:'营销'},
		               							    {value:'6',text:'告警'},
		               							    {value:'7',text:'USSD'},
		               							    {value:'8',text:'闪信'}]"/>
										</li>
										<li>
											<label class="control-label">发送数量&nbsp;&nbsp;</label>
											<input type="text" name="greaterNum" value="<s:property value="#parameters.greaterNum"/>" placeholder="大于等于"
												   onkeyup='this.value=this.value.replace(/\D/gi,"")' maxlength="40" class="txt_250" />
											<input type="text" name="lessNum" value="<s:property value="#parameters.lessNum"/>" placeholder="小于等于"
												   onkeyup='this.value=this.value.replace(/\D/gi,"")' maxlength="40" class="txt_250" />
										</li>
										<%--<li><input id="searchBtn" type="submit" value="搜索" onsubmit="return releaseSmsAudit()"/></li>--%>
										<li><input id="searchBtn" type="submit" value="搜索" /></li>
										<%--<li><input type="button" value="批量审核通过" onclick="updateStatusBatch(1)"/></li>--%>
										<%--<li><input type="button" value="批量审核不通过" onclick="updateStatusBatch(2)"/></li>--%>
									</ul>
						 	 	</div>
						  </form>
						</div>
					</div>
					<u:page page="${page}" formId="mainForm" />
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									<th>审核ID</th>
									<th width="62px;">用户账号</th>
									<th>短信类型</th>
									<th>短信内容</th>
									<th>签名</th>
									<th>状态</th>
									<th>不通过原因</th>
									<th>发送数量</th>
									<th>审核人</th>
									<th>审核时间</th>
									<th>创建时间</th>
									<th>锁定人</th>
									<th width="100px">操作</th>
									<%--<th><input type="checkbox" onclick="selectAll(this)" />全选</th>--%>
								</tr>
							</thead>
							<tbody id="auditBody">
								<s:iterator value="page.list">
									<tr>
										<td>${auditid}</td>
										<c:if test="${!empty  clientLabel}">
											<td data-hover="${clientLabel}" class="jsms-hover">
												<span style="color:#FF9600">${clientid}</span>
												<span class="icon-question-sign " style="color:red;"></span>
											</td>
										</c:if>
										<c:if test="${empty clientLabel}">
											<td>${clientid}</td>
										</c:if>
										<%--<td>${clientid}</td>--%>
										<td>
											<s:if test="smstype == 0">通知</s:if>
											<s:elseif test="smstype == 4">验证码</s:elseif>
											<s:elseif test="smstype == 5">营销</s:elseif>
											<s:elseif test="smstype == 6">告警</s:elseif>
											<s:elseif test="smstype == 7">USSD</s:elseif>
											<s:elseif test="smstype == 8">闪信</s:elseif>
										</td>
										<td class="sms_content">${contentWithmarked}</td>
										<%--<td>${sign}</td>--%>
										<td><c:out value="${sign}"/></td>
										<td class="sms_status">
											<s:if test="status == 0">待审核</s:if>
											<s:if test="status == 1">通过</s:if>
											<s:if test="status == 2">不通过</s:if>
											<s:if test="status == 3">转审</s:if>
											<s:if test="status == 3 and opt_remark!=null">
											备注：${opt_remark}
											</s:if>

										</td>

										<td>
											<s:if test="status == 2">${opt_remark}</s:if>
											<s:else>
											-
											</s:else>
										</td>
										<td>${sendnum}</td>
										<td>
										<s:if test="status==3">
											${transferpersonName}
										</s:if>
										<s:elseif test="status==0">
											-
										</s:elseif>
										<s:else>
											${auditperson}
										</s:else>
										</td>
										<td>${audittime}</td>
										<td>${createtime}</td>
										<td>
											${lockByUserName}
										</td>
										<td class="operation">
											<%--<c:if test="${sessionScope.LOGIN_USER_ID eq lockByUserId}">
												<a href="javascript:;" onclick="updateStatus(this, '${auditid}', 1)">通过</a><br>
										  		<a href="javascript:;" onclick="updateStatus(this, '${auditid}', 2)">不通过</a><br>
											</c:if>--%>
											<c:if test="${(status eq 1) or (status eq 2)}">
												<a href="javascript:;" onclick="setAuditExpired(this, '${auditid}')">审核过期</a><br>
											</c:if>
											<u:authority menuId="283">
												<c:if test="${(smstype eq 0) or (smstype eq 4) or (smstype eq 5)}">
													<span href="javascript:;" style="cursor:pointer;" onclick="addTemplate('${clientid}', '<c:out value="${sign}"/>', '<c:out value="${content}"/>', '${smstype}',82)">添加智能模板</span><br>
												</c:if>
											</u:authority>
											<span id="expiredTag_${auditid}" style="display: none; cursor:pointer;" onclick="setAuditExpired(this, '${auditid}')">审核过期</span><br>
										</td>
										<%--<td>
											<c:if test="${sessionScope.LOGIN_USER_ID eq lockByUserId}">
												<input type="checkbox" name="auditid" value="${auditid}"/>
											</c:if>
										</td>--%>
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
	<div class="control-group" id="auditNoPass" style="display: none">
		<label class="control-label ml" for="noPass">确定不通过该短信审核？</label>
		<label class="control-label ml" for="noPass"><span class="red">*</span>不通过原因：<select  id="noPass" name="remark" placeholder="请选择"></select></label>
		<label class="control-label ml" for="another" style="display: none" id="anotherreason">&nbsp;&nbsp;其他原因&nbsp;：&nbsp;&nbsp;<input type="text" id="another" name="remark"  placeholder="最多可输入10个字符"></label>
	</div>
	<script type="text/javascript">

		var AUDIT_EXPIRE_TIME = 15 * 60 * 1000;
// 		var AUDIT_EXPIRE_TIME = 5000;
		var _auditTimeout; // 审核超时定时器

		$(function(){
			/*getAuditExpireTime();

			console.log("短信审核超时时长：" + AUDIT_EXPIRE_TIME);
			_auditTimeout = setTimeout(auditExpireAction, AUDIT_EXPIRE_TIME);

			// 审核超时计数器刷新器
			expireTimerLitener();*/

			// 退出时释放短信审核记录
			/*$(".logout").on("click",function(){
				releaseSmsAudit();
			});*/

//			addcookie('queryAuditPage', 1, 1);

			document.onkeydown = function(event){
				var ev = event || window.event;
			    if(ev.keyCode == 116){
			    	$("#searchBtn").click();
			    }
			}

			// 查询待审核记录数
			startGetAuditNum();

            var select2Date;
            //获取审核不通过原因表
            $.ajax({
                url : '${ctx}/smsaudit/audit/conclusion',
                dataType : 'json',
                async : false,
                success : function(res){
                    for(var i = 0; i < res.length; i++){
                        res[i].id = res[i].conclusionDesc;
                        res[i].text = res[i].conclusionDesc;
                    }
                    res.unshift({id:"",text:""})
                    select2Date = res;
                }
            })
            $("#noPass").select2({
                data : select2Date,
                placeholder: "请选择"
            })
        });


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
					addcookie('queryAuditPage', 0, 1);
					if (data.result == null) {
						$("#msg").text("服务器错误，请联系管理员").show();
						return;
					}
				}
			});

		}

		// 离开页面时释放审核记录（退出按钮时无效）
		/*window.onbeforeunload = function(){
			if(getcookie('queryAuditPage')==1){
				console.log("离开页面时释放");
				releaseSmsAudit();
			}
		}*/


		function getAllSelected(){
			var allIds = "";
			var auditIds = "";
			var notReleaseIds = "";
			var btns = [];
			var selected = {};
		    $("[name='auditid']:checkbox").each(function(){
		    	allIds +=$(this).val()+",";
		        if ($(this).attr('checked')) {
		        	auditIds +=$(this).val()+",";
		            btns.push($(this).parents('td'));
		        }else{
		        	notReleaseIds +=$(this).val()+",";
		        }
		    });
		    auditIds = auditIds.length > 0 ? auditIds.substr(0, auditIds.length-1) : '';
		    allIds = allIds.length > 0 ? allIds.substr(0, allIds.length-1) : '';
		    notReleaseIds = notReleaseIds.length > 0 ? notReleaseIds.substr(0, notReleaseIds.length-1) : '';
		    selected.allIds = allIds;
		    selected.auditIds = auditIds;
		    selected.notReleaseIds = notReleaseIds;
		    selected.btns = btns;

		    return selected;
		}

        var status_text = ["","通过","不通过","转审"];
		function updateStatus(btns, auditIds, status){
            var remark ;
			var operation = status_text[status];
			if(status ==1 ){
                //询问框
                layer.confirm('您确定' + operation + '该短信审核？', {
                    btn: [operation,'取消']
                }, function(){
                    $.ajax({
                        type : "post",
                        url : "${ctx}/smsaudit/updateStatus",
                        async: false,
                        data : {
                            auditIds : auditIds,
                            status : status
                        },
                        success : function(data) {
                            if(data.result == "success"){
                                layer.msg('操作成功', {icon: 1,time: 1000,});
                                if(isArray(btns)){
                                    changeStyleBatch(btns, status);

                                    $.each( auditIds.split(","), function(index, value){
                                        $("#expiredTag_" + value).show();
                                    });

                                }else{
                                    changeStyle(btns, status);

                                    $("#expiredTag_" + auditIds).show();
                                }

                            }else{
                                layer.alert(data.msg);
                            }

                        }
                    });

                });
			}else if(status == 2){
                $("#another").val('')
                $("#noPass").val('')
                $("#noPass").change(function () {
                    if($('#noPass').val() == '其他'){
                        $("#anotherreason").show();
                        remark = $("#another").val()
                    }else {
                        $("#anotherreason").hide();
                        remark = $("#noPass").val()
                    }

                });
                layer.open({
                    type: 1,
                    title:'短信审核确认',
					area:['600px','250px'],
                    btn: ['确定', '取消'],
                    content: $('#auditNoPass')
                    ,yes: function(index, layero){
                        if($('#noPass').val() == '其他'){
                            $("#anotherreason").show();
							if($("#another").val().length >10){
                                layer.msg('最多输入10个字符', {icon: 2,time: 1000,});
                                return;
							}
                            if(!$("#another").val()){
                                layer.msg('请输入其他原因', {icon: 2,time: 1000,});
                                return;
                            }
                            remark = '其他：' + $("#another").val();
                        }else {
                            if(!$("#noPass").val()){
                                layer.msg('请选择不通过原因', {icon: 2,time: 1000,});
                                return;
                            }
                            $("#anotherreason").hide();
                            remark = $("#noPass").val()
                        }
                        $.ajax({
                            type : "post",
                            url : "${ctx}/smsaudit/updateStatus",
                            async: false,
                            data : {
                                auditIds : auditIds,
                                status : status,
								remark : remark
                            },
                            success : function(data) {
                                if(data.result == "success"){
                                    layer.msg('操作成功', {icon: 1,time: 1000,},function () {
                                        layer.closeAll()
                                    });
                                    if(isArray(btns)){
                                        changeStyleBatch(btns, status);

                                        $.each( auditIds.split(","), function(index, value){
                                            $("#expiredTag_" + value).show();
                                        });

                                    }else{
                                        changeStyle(btns, status);

                                        $("#expiredTag_" + auditIds).show();
                                    }

                                }else{
                                    layer.alert(data.msg);
                                }

                            }
                        });

                    },
                    end: function(){
                        // 清空审核不通过的下拉框和其他理由输入框
                        var $noPass = $("#noPass").select2();
                        $noPass.val("").trigger("change");
//                        $noPass.val("");
                        $("#another").val("");
                    }
                })
            }
		}


		function updateStatusBatch(status){
			var recordSelected = getAllSelected();
			if(recordSelected.auditIds.length == 0){
				layer.msg('请至少选择一条记录', {icon: 0,time: 1000,});
				return;
			}

            if(status==2){

                $("#noPass").change(function () {
                    if($('#noPass').val() == '其他'){
                        $("#anotherreason").show();
                        remark = $("#another").val()
                    }else {
                        $("#anotherreason").hide();
                        remark = $("#noPass").val()
                    }

                });
                layer.open({
                    type: 1,
                    title:'短信审核确认',
                    area:['600px','250px'],
                    btn: ['确定', '取消'],
                    content: $('#auditNoPass')
                    ,yes: function(index, layero){
                        /*if($('#noPass').val() == '其他'){
                            $("#anotherreason").show();
                            remark = $("#another").val()
                        }else {
                            $("#anotherreason").hide();
                            remark = $("#noPass").val()
                        }*/
                        if($('#noPass').val() == '其他'){
                            $("#anotherreason").show();
                            if($("#another").val().length >10){
                                layer.msg('最多输入10个字符', {icon: 2,time: 1000,});
                                return;
                            }
                            if(!$("#another").val()){
                                layer.msg('请输入其他原因', {icon: 2,time: 1000,});
                                return;
                            }
                            remark = '其他：' + $("#another").val();
                        }else {
                            if(!$("#noPass").val()){
                                layer.msg('请选择不通过原因', {icon: 2,time: 1000,});
                                return;
                            }
                            $("#anotherreason").hide();
                            remark = $("#noPass").val()
                        }

                        $.ajax({
                            type : "post",
                            url : "${ctx}/smsaudit/updateStatus",
                            async: false,
                            data : {
                                auditIds : recordSelected.auditIds,
                                status : status,
                                remark : remark
                            },
                            success : function(data) {
                                if(data.result == "success"){
                                    layer.msg('操作成功', {icon: 1,time: 1000,},function () {
                                        layer.closeAll()
                                    });
                                    if(isArray(recordSelected.btns)){
                                        changeStyleBatch(recordSelected.btns, status);

                                        $.each( recordSelected.auditIds.split(","), function(index, value){
                                            $("#expiredTag_" + value).show();
                                        });

                                    }else{
                                        changeStyle(recordSelected.btns, status);

                                        $("#expiredTag_" + auditIds).show();
                                    }

                                }else{
                                    layer.alert(data.msg);
                                }

                            }
                        });

                    }
                })
            }else {
                updateStatus(recordSelected.btns, recordSelected.auditIds, status);
			}

		}


		function changeStyleBatch(btns, status){
			$(btns).each(function(){
				var tag_a = $(this).parent().children(".operation").find('a')[status-1];
				$(tag_a).attr("style","color:#08c;cursor:default;");
				$(tag_a).removeAttr('href');
				$(tag_a).removeAttr('onclick');

				$(tag_a).parent().siblings(".status").text(status_text[status]);
				var checker = $(tag_a).parent().siblings().find(".checker");
				$(checker).remove();
				$(tag_a).siblings().each(function(){
					$(this).attr("style","text-decoration:line-through;cursor:default;");
					$(this).removeAttr('href');
					$(this).removeAttr('onclick');
				})

				$("#selectAll").parent().removeClass("checked", false);

			})

		}

		function changeStyle(btn, status){
			$(btn).attr("style","color:#08c;cursor:default;");
			$(btn).removeAttr('href');
			$(btn).removeAttr('onclick');

			$(btn).parent().siblings(".status").text(status_text[status]);
			var checker = $(btn).parent().siblings().find(".checker");
			$(checker).remove();
			$(btn).siblings("a").each(function(){
				$(this).attr("style","text-decoration:line-through;cursor:default;");
				$(this).removeAttr('href');
				$(this).removeAttr('onclick');
			})

		}

		function isArray(obj) {
		  return Object.prototype.toString.call(obj) === '[object Array]';
		}

		//审核记录全选
		function selectAll(box) {
			var select = $(box).is(":checked");
			$("#auditBody tr").each(function() {
				$(this).find(":checkbox").prop("checked", select);
				var myspan=$(this).find(":checkbox").parent();
				if(select){
					myspan.attr("class","checked");
				}else{
					myspan.attr("class","");
				}
			});

		}

		function getAuditExpireTime(){
			$.ajax({
				type : "post",
				url : "${ctx}/smsaudit/getAuditExpireTime",
				async : false,
				success : function(data) {
					if(null != data & data.auditExpireTime != null){
						AUDIT_EXPIRE_TIME = data.auditExpireTime * 60000;
					}
				}
			});
		}

		function expireTimerLitener(){
			$(document).mouseup(function(e){
				console.log("mouseup");
				clearTimeout(_auditTimeout);
				_auditTimeout = setTimeout(auditExpireAction, AUDIT_EXPIRE_TIME);
			});
			$(document).keydown(function(e){
				console.log("keydown");
				clearTimeout(_auditTimeout);
				_auditTimeout = setTimeout(auditExpireAction, AUDIT_EXPIRE_TIME)
			});
		}

		function auditExpireAction(){
			console.log("审核超时");
			releaseSmsAudit();
			$("#auditBody").find("tr").remove(); // 清除页面记录
		}

		function showAuditNum(){
             var lockNum = yzmLockNum + ordinaryLockNum+ majorLockNum;

            var text1 = "待审核记录数(" + auditNum + ")";
            var text2 = "审核待发送数(" + sendNum + ")";
            var text3 = "总锁定审核数(" + lockNum + ")";
            $("#auditNum").text(text1);
            $("#sendNum").text(text2);
            $("#lockNum").text(text3);

            $("#auditNum").show();
            $("#sendNum").show();
            $("#lockNum").show();
        }

		function getAuditNum(){
			$.ajax({
				type : "post",
				<%--url : "${ctx}/smsaudit/getAuditNum",--%>
                url : "${ctx}/smsaudit/getKindsAuditNum",
                success : function(data) {
                    var auditNum = 0;
                    var sendNum = 0;
                    var lockNum = 0;
                    if(data != null){
                        auditNum = data.auditNum;
                        sendNum = data.sendNum;
						/*auditNum = data.AUDIT_NUM;
						 sendNum = data.SEND_NUM;
						 public final static String MAJOR_LOCK_NUM = "majorLockNum";
						 public final static String YZM_LOCK_NUM = "yzmLockNum";
						 public final static String ORDINARY_LOCK_NUM = "ordinaryLockNum";
						 */
                        lockNum = data.yzmLockNum + data.ordinaryLockNum+ data.majorLockNum;
                    }
					// 待审核提示信息
					/*if(auditNum > 0){*/
						var text1 = "待审核记录数(" + auditNum + ")";
						var text2 = "审核待发送数(" + sendNum + ")";
						var text3 = "总锁定审核数(" + lockNum + ")";
						$("#auditNum").text(text1);
						$("#sendNum").text(text2);
						$("#lockNum").text(text3);

						$("#auditNum").show();
						$("#sendNum").show();
						$("#lockNum").show();
					/*}else{
						$("#auditNum").hide();
						$("#sendNum").hide();
//						$("#lockNum").hide();
					}*/
				}
			});
		}

		// 定时查询待审核记录数
		function startGetAuditNum(){
            showAuditNum();
			var _getAuditNum = window.setInterval(function(){// 初始化定时器
                showAuditNum();
			}, 5000)
		}

        function toReset() {
            var inputArr = $("._reset");
            for(var i = 0;i < inputArr.length;i++){
                inputArr[i].value = '';
            }
            var date = new DateFormat();
            $("#start_time").val(date.format(new Date(),"yyyy-MM-dd")+ " 00:00:00");
            $("#end_time").val(date.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            $("#smsType").val("");
        }

		function setAuditExpired(btns, auditId) {
            layer.confirm('确认要设置当前审核记录过期吗？', function(index){
                $.ajax({
                    type : "post",
                    url : "${ctx}/smsaudit/setAuditExpired",
                    data : {
                        auditId : auditId
					},
                    success : function(data) {
                        if (data == null || data.result == null) {
                            layer.alert("服务器错误，请联系管理员");
                            return;
                        }
                        if (data.result == "success") {
                            layer.msg(data.msg);
                        }else{
                            layer.alert(data.msg);
                        }
                    }
                });
            }, function(index){
                layer.close(index);
            });
        }

        function addTemplate(clientId, sign, content, smsType,menuId) {
            <%--var url = '${ctx}/smsaudit/autoTemplate/add?clientId=' + clientId + "&smsType=" + smsType--%>
            var url = '${ctx}/smsaudit/template/add?clientId=' + clientId + "&smsType=" + smsType
                + "&sign=" + sign + "&content=" + content + "&menuId=" + 82;
            url = encodeURI(url);

            layer.open({
                type: 2,
                title: '新增模板',
                shadeClose: true,
                shade: 0.8,
                area: ['700px', '90%'],
                content: url //iframe的url
            });
        }

        //@SourceURL=auditQuery.js
	</script>
</body>
</html>