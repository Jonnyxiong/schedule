<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>审核历史查询</title>
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

<body menuId="282">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>

		<div class="row-fluid">
			<div class="span12">
				<div class="widget-title">
					<hr>
					<nav class="navbar navbar-success" role="navigation">
						<ul class="nav nav-tabs nav-justified" role="tablist">
							<li ><a href="${ctx}/smsaudit/hisquery">过期短信查询</a></li>
							<li class="active"><a href="${ctx}/smsaudit/hisbakquery">审核历史查询</a></li>

						</ul>
					</nav>
				</div>
				<div class="widget-box">

					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						<h5></h5>
						<div class="search">
						 	 <form method="post" id="mainForm" action="${ctx}/smsaudit/hisbakquery">
						 	 	<div class="search">
									<ul>
										<li name="testhight1">
											<span>审核时间:</span>
											<u:date id="start_time"   value="${s_start_time}" placeholder="开始时间" params="minDate:'%y-%M-{%d-90} 00:00:00', maxDate:'#F{$dp.$D(\\'end_time\\')||\\'%y-%M-%d %H:%m:%\\'}'" />
											<span>至</span>
											<u:date id="end_time"  value="${s_end_time}" placeholder="结束时间" params="minDate:'#F{$dp.$D(\\'start_time\\')||\\'%y-%M-{%d-90} 00:00:00\\'}', maxDate:'%y-%M-%d %H:%m:%s'" />
										</li>
										<li><input type="text" name="aduitor" value="${aduitor}" maxlength="40"
												  placeholder="审核人" class="txt_250" /></li>
										<li><input type="text" name="clientid" value="${clientid1}" maxlength="40"
										placeholder="用户账号" class="txt_250" /></li>
										<li><input type="text" name="content" value="${content1}" maxlength="40"
										placeholder="短信内容" class="txt_250" /></li>
										<li><input type="text" name="sign" value="${sign1}" maxlength="40"
												   placeholder="签名" class="txt_250" /></li>
										<li>
											<u:select id="smstype" value="${smstype1}" data="[
													{value:'',text:'短信类型：所有'},
													{value:'0',text:'通知'},
													{value:'4',text:'验证码'},
		               							    {value:'5',text:'营销'},
		               							    {value:'6',text:'告警'},
		               							    {value:'7',text:'USSD'},
		               							    {value:'8',text:'闪信'}]"/>
										</li>
										<li name="testhight">
											<u:select id="status" value="${status1}" data="[
													{value:'',text:'所有状态'},
													{value:'1',text:'通过'},
		               							    {value:'2',text:'不通过'}]"  />
										</li>


										<li><button id="searchBtn" type="submit" onsubmit="return releaseSmsAudit()" class="btn btn-info btn-mini">搜索</button></li>
										<li><a id="restBtn" class="btn btn-info btn-mini ">重置</a></li>
										<li> <a   class="btn btn-info btn-mini " href="javascript:;" onclick="exportExcel()">导出Excel</a></li>
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
									<th>用户账号</th>
									<th>短信类型</th>
									<th>短信内容</th>
									<th>签名</th>
									<th>创建时间</th>
									<th>发送数量</th>
									<th>审核状态</th>
									<th>不通过原因</th>
									<th>审核人</th>
									<th>审核时间</th>
								</tr>
							</thead>
							<tbody id="auditBody">
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${clientid}</td>
										<td>${smsTypeName}</td>
										<td >${content}</td>
										<td>${sign}</td>
										<td>${createtimeStr}</td>
										<td>${sendnum}</td>
										<td class="sms_status">
											<s:if test="status == 0">待审核</s:if>
											<s:if test="status == 1">通过</s:if>
											<s:if test="status == 2">不通过</s:if>
											<s:if test="status == 3">转审</s:if>
											<s:if test="status == 3 and optRemark!=null">
												备注：${optRemark}
											</s:if>

										</td>
										<td>
											<s:if test="status==2">
												${optRemark}
											</s:if>
											<s:else>
											-
											</s:else>
										</td>
										<td>
										<s:if test="status==3">
											${transferpersonName}
										</s:if>
										<s:else>
											${auditpersonName}
										</s:else>
										</td>
										<td>${audittimeStr}</td>
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
	
//		var AUDIT_EXPIRE_TIME = 15 * 60 * 1000;
//// 		var AUDIT_EXPIRE_TIME = 5000;
//		var _auditTimeout; // 审核超时定时器
//
		$(function(){
//			getAuditExpireTime();
//
//			console.log("短信审核超时时长：" + AUDIT_EXPIRE_TIME);
//			_auditTimeout = setTimeout(auditExpireAction, AUDIT_EXPIRE_TIME);
//
//			// 审核超时计数器刷新器
//			expireTimerLitener();
//
//			// 退出时释放短信审核记录
//			$(".logout").on("click",function(){
//				releaseSmsAudit();
//			});
//
//			addcookie('queryAuditPage', 1, 1);
			
			document.onkeydown = function(event){ 
				var ev = event || window.event;
			    if(ev.keyCode == 116){
			    	$("#searchBtn").click();
			    }
			}

			//重置表单
			$("#restBtn").click(function(){
			    $('.search input').val("");
			    $('.search select').val("");

                var d = new Date();
                var nowtime=d.toLocaleString();
                var month = d.getMonth() + 1;
                var stime=d.getFullYear()+"-"+month+"-"+d.getDate()+" 00:00:00"
                var etime=d.getFullYear()+"-"+month+"-"+d.getDate()+" "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();

				$('#start_time').val(stime);
                $('#end_time').val(etime);
			})
			// 查询待审核记录数
	//		startGetAuditNum();
		});



//导出Excel文件
function exportExcel() {

    var totalCount = 0;
    if($("#totalCount").val() != undefined && $("#totalCount")!= null){
        totalCount = $("#totalCount").val();
    }

    if (totalCount == 0) {
        alert("共0条记录，导出Excel文件失败");
        return;
    }
    if(totalCount>60000){
        alert("导出Excel文件条数大于60000条");
        return;

    }

    var start_time = $("#start_time").val();
    var end_time = $("#end_time").val();
    if(start_time == "" || end_time == ""){
        layer.msg("请输入：开始时间和结束时间", {icon: 1,time: 1500});
        return;
    }


    var mainForm = $("#mainForm");
    var action = mainForm.attr("action");

    mainForm.attr("action", "${ctx}/smsaudit/hisbakExportExcel").submit();
    mainForm.attr("action", action);
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
			$(btn).siblings().each(function(){
				$(this).attr("style","text-decoration:line-through;cursor:default;");
				$(this).removeAttr('href');
				$(this).removeAttr('onclick');
			})
		}

		function isArray(obj) {
		  return Object.prototype.toString.call(obj) === '[object Array]';
		}


	</script>
</body>
</html>