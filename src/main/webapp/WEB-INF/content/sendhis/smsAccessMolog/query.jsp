<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>客户上行明细</title>
<style type="text/css">
	.widget-title .search, .widget-title .search ul { float: none;}
	.widget-title .search ul.list-inline { margin: 10px 0px; width: 100%; display: block; overflow: hidden;}
	.widget-title .search ul li.li-num	{ margin: 0px;}
	.widget-title .search ul li span.span-num { background: red; padding: 5px 10px; line-height: 30px;}
	.widget-title .search ul.ul2 { margin-left: 20px; display: inherit; white-space: normal; width: 1400px;}
	button.button-num {border: 0; padding: 5px 10px; line-height: 30px;}
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
	 .widget-title .search input[type='button'],.widget-title .search input[type='submit'] { background: #3a87ad;}
</style>
</head>
<body menuId="78">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
				
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-th"></i>
						</span>
						<h5></h5>
						
						<div class="search" style="height:auto;">
						 <form method="post" id="mainForm" action="${ctx}/smsAccessMolog/query">
							<ul class="ul2">
								<li>
									<u:select id="channelid" value="${param.channelid}" placeholder="通道号" sqlId="channel" />
								</li>
								<li>
									<input type="text" name="phone" value="<s:property value="#parameters.phone"/>" onfocus="inputControl.setNumber(this, 20, 0, false)" placeholder="手机号码" class="txt_250" />
								</li>
								<li>
									<input type="text" name="tophone" value="<s:property value="#parameters.tophone"/>" onfocus="inputControl.setNumber(this, 50, 0, false)" placeholder="目的号码" class="txt_250" />
								</li>
								<li>
									<input type="text" name="content" value="${param.content}" placeholder="短信内容"  maxlength="100"/>
								</li>
								<li name="testhight1">
									<u:date id="start_time" value="${start_time}" placeholder="上行接收开始时间" params="minDate:'%y-%M-{%d-45} 00:00:00', maxDate:'#F{$dp.$D(\\'end_time\\')||\\'%y-%M-%d %H:%m:%\\'}'" />
									<span>至</span>
            						<u:date id="end_time" value="${end_time}" placeholder="上行接收结束时间" params="minDate:'#F{$dp.$D(\\'start_time\\')||\\'%y-%M-{%d-45} 00:00:00\\'}', maxDate:'%y-%M-%d %H:%m:%s'" />
								</li>
								<li>
									<input type="text" name="sendmoid" value="${param.sendmoid}" placeholder="send上行ID" maxlength="100"/>
								</li>
								<li>
									<input type="text" name="clientid" value="${param.clientid}" placeholder="客户ID/确认客户ID" maxlength="100"/>
								</li>
								<li>
									<input type="submit" value="搜索"/>
								</li>
							</ul>
						  </form>
						</div>
					</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th style='width:5%'>序号</th>
									<th>上行ID</th>
									<th>通道ID</th>
									<th>上行接收时间</th>
									<th>手机号码</th>
									<th>目的号码</th>
									<th>短信内容</th>
									<th>send上行ID</th>
									<th>客户ID</th>
									<th>确认客户ID</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.list">
								<tr>
								  <td>${rownum}</td>
							      <td>${moid}</td>
							      <td>${channelid}</td>
							      <td><s:date name="%{receivedate}" format="yyyy-MM-dd HH:mm:ss" /></td>
							      <td>${phone}</td>
							      <td>${tophone}</td>
							      <td class="sms_content">${content}</td>
							      <td>${sendmoid}</td>
							      <td>${clientid}</td>
							      <td>${userid}</td>
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
		$(function(){
		});
	</script>
</body>
</html>