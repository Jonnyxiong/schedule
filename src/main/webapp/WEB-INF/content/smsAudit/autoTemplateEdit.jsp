<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
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
										placeholder="用户名" class="txt_250" /></li>
										<li><input type="text" name="auditid" value="<s:property value="#parameters.auditid"/>" maxlength="40"
										placeholder="批号" class="txt_250" /></li>
										<li><input type="text" name="content" value="<s:property value="#parameters.content"/>" maxlength="40"
										placeholder="包含内容" class="txt_250" /></li>
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
											<input type="text" name="greaterNum" value="<s:property value="#parameters.greaterNum"/>" placeholder="大于等于" maxlength="40" class="txt_250" />
											<input type="text" name="lessNum" value="<s:property value="#parameters.lessNum"/>" placeholder="小于等于" maxlength="40" class="txt_250" />
										</li>
										<li><input id="searchBtn" type="submit" value="搜索" onsubmit="return releaseSmsAudit()"/></li>
										<li><input type="button" value="批量审核通过" onclick="updateStatusBatch(1)"/></li>
										<li><input type="button" value="批量审核不通过" onclick="updateStatusBatch(2)"/></li>
									</ul>
						 	 	</div>
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									<th>审核ID</th>
									<th>用户账号</th>
									<th>短信类型</th>
									<th>短信内容</th>
									<th>签名</th>
									<th>状态</th>
									<th>发送数量</th>
									<th>审核人</th>
									<th>审核时间</th>
									<th>创建时间</th>
									<th>锁定人</th>
									<th>审核操作</th>
									<th><input type="checkbox" onclick="selectAll(this)" />全选</th>
								</tr>
							</thead>
							<tbody id="auditBody">
								<s:iterator value="page.list">
									<tr>
										<td>${auditid}</td>
										<td>${clientid}</td>
										<td>
											<s:if test="smstype == 0">通知</s:if>
											<s:elseif test="smstype == 4">验证码</s:elseif>
											<s:elseif test="smstype == 5">营销</s:elseif>
											<s:elseif test="smstype == 6">告警</s:elseif>
											<s:elseif test="smstype == 7">USSD</s:elseif>
											<s:elseif test="smstype == 8">闪信</s:elseif>
										</td>
										<td class="sms_content">${content}</td>
										<td>${sign}</td>
										<td class="sms_status">
											<s:if test="status == 0">待审核</s:if>
											<s:if test="status == 1">通过</s:if>
											<s:if test="status == 2">不通过</s:if>
											<s:if test="status == 3">转审</s:if>
											<s:if test="status == 3 and opt_remark!=null">
											备注：${opt_remark} 
											</s:if>
											
										</td>
										<td>${sendnum}</td>
										<s:if test="status==3">
											<td>${transferpersonName}</td>
										</s:if>
										<s:else>
											<td>${auditperson}</td>
										</s:else>
										<td>${audittime}</td>
										<td>${createtime}</td>
										<td>
											${lockByUserName}
										</td>
										<td class="operation">
											<c:if test="${sessionScope.LOGIN_USER_ID eq lockByUserId}">
												<a href="javascript:;" onclick="updateStatus(this, '${auditid}', 1)">通过</a><br>
										  		<a href="javascript:;" onclick="updateStatus(this, '${auditid}', 2)">不通过</a> 
											</c:if>
										</td>
										<td>
											<c:if test="${sessionScope.LOGIN_USER_ID eq lockByUserId}">
												<input type="checkbox" name="auditid" value="${auditid}"/> 
											</c:if>
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
	

		

		
	</script>
</body>
</html>