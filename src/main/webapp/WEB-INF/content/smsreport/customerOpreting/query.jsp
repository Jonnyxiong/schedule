<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>客户运营报表查询</title>
</head>
<body menuId="74">
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
						<div class="search">
						 <form method="post" id="mainForm" action="${ctx}/customerOpreting/query" onsubmit="return query()">
						 	<ul>
								<li>
									<u:select id="stat_type" value="${stat_type}" placeholder="统计类型" dictionaryType="consume_stat_type" onChange="changeStatType" />
								</li>
								<li id="li_day" ${stat_type==0 ? "" : "style='display:none'"}>
									<u:date id="start_time_day" value="${start_time_day}" placeholder="开始时间" dateFmt="yyyyMMdd" params="" />
									<span>至</span>
            						<u:date id="end_time_day" value="${end_time_day}" placeholder="结束时间" dateFmt="yyyyMMdd" params="" />
								</li>
								<li id="li_month" ${stat_type==1 ? "" : "style='display:none'"}>
									<u:date id="start_time_month" value="${start_time_month}" placeholder="开始时间" dateFmt="yyyyMM" params="minDate:'{%y-1}-%M-%d', maxDate:'#F{$dp.$D(\\'end_time_month\\')||\\'%y-%M-%d\\'}'" />
									<span>至</span>
            						<u:date id="end_time_month" value="${end_time_month}" placeholder="结束时间" dateFmt="yyyyMM" params="minDate:'#F{$dp.$D(\\'start_time_month\\')||\\'{%y-1}-%M-%d\\'}', maxDate:'%y-%M-%d'" />
								</li>
								<li>
									<input type="text" name="text" value="<s:property value="#parameters.text"/>" placeholder="用户ID/用户名称/代理商ID" />
								</li>
								<li>
									<u:select id="paytype"   value="${param.paytype}" data="[
											{value:'-1',text:'付费类型:所有'}, {value:'0',text:'预付费'}, {value:'1',text:'后付费'}]" 
               						/>
								</li>
								<li>
									<u:select id="operatorstype" value="${param.operatorstype}" placeholder="运营商类型" dictionaryType="channel_operatorstype"  />
								</li>
								
								<li>
									<u:select id="channel_id" value="${param.channel_id}" placeholder="通道号" sqlId="channel" />
								</li>
								<li>
									<u:select id="belong_sale" sqlId="findSalesman" placeholder="所属销售：所有" value="${belong_sale}" showAll="false"/>
								</li>
								<li>
									<input type="submit" value="搜索" />
								</li>
								<li>
									<a href="javascript:;" class=" label label-info" onclick="exportExcel()">导出Excel文件</a>
								</li>
							</ul>
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									 <th>序号</th>
					                 <th>时间</th>
					                 <th>用户ID</th>
					                 <th>用户名称</th>
					                 <th>代理商ID</th>
					                 <th>归属销售</th>
					                 <th>付费类型</th>
					                 <th>运营商类型</th>
					                 <th>通道号</th>
					                 <th>通道备注</th>
									 <th>计费规则</th>
					                 <th title="1+3+4+6+10">计费条数</th>
					                 <th>通道预估价(元)</th>
					                 <th>通道成本价(元)</th>
					                 <th>客户购买价(元)</th>
					                 <th>代理商成本价(元)</th>
					                 <th>代理商佣金(元)</th>
					                 <th>毛利(元)</th>
					                 <th>毛利率</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="data.page.list" var="v">
								<tr>
								  <td>${v.rownum}</td>
								  <td>${v.date}</td>
							      <td>${v.clientid}</td>
							      <td>${v.name}</td>
							      <td>${v.agent_id}</td>
							      <s:if test="belongSaleName == null || belongSaleName == ''">
								  		<td> - </td>
							      </s:if>
							      <s:else>
							      		<td>${v.belongSaleName}</td>
							      </s:else>
							      <td>${v.paytypeText}</td>
							      <td>${v.operatorstype_name}</td>
							      <td>${v.channelid}</td>
							      <td>${v.remark}</td>
									<td>${v.chargeRuleStr}</td>
							      <td>${v.chargetotal}</td>
							      <td>${v.costfee}</td>
							      <td>${v.realCost}</td>
							      <td>${v.salefee}</td>
							      <td>${v.productfee}</td>
							      <td>${v.agent_commission}</td>
							      <td>${v.grossprofit}</td>
							      <td>${v.grossmargin}</td>
							     </tr>
							    </s:iterator>
							    <s:if test="data.total != null && data.page.list.size != 0">
									<tr style="font-weight: bold;">
									  <td colspan="8" style="text-align: center;">总计</td>
								      <td>-</td>
								      <td>-</td>
										<td>-</td>
								      <td>${data.total.chargetotal}</td>
								      <td>${data.total.costfee}</td>
								      <td>${data.total.realCost}</td>
								      <td>${data.total.salefee}</td>
								      <td>${data.total.productfee}</td>
								      <td>${data.total.agent_commission}</td>
								      <td>${data.total.grossprofit}</td>
								      <td>${data.total.grossmargin}</td>
								     </tr>
							    </s:if>
							   
							</tbody>
						</table>
					</div>
						<u:page page="${data.page}" formId="mainForm" />
				</div>
			</div>
		</div>
	</div>

<script type="text/javascript">
$(function(){
	$("#stat_type option:first").attr("disabled", true);//禁用“统计类型：所有”
});

//选择统计类型
function changeStatType(value, text){
	if(value==1){
		$("#li_day").show();
		$("#li_month").hide();
	}else{
		$("#li_day").hide();
		$("#li_month").show();
	}
}

function query() {
	var start_time_day = new Date(getDay($("#start_time_day").val())).getTime();
	var end_time_day = new Date(getDay($("#end_time_day").val())).getTime();

	var time = (end_time_day - start_time_day) / (1000 * 3600 * 24);
	if(time > 45) {
		alert("最多可以查询45天的数据");
		return false;
	}
}

function getDay(source) {
	var year = source.substring(0,4);
	var month = source.substring(4,6);
	var day = source.substring(6,8);
	return year + "/" + month + "/" + day;
}

//导出Excel文件
function exportExcel() {
	var totalCount = ${data.page.totalCount};
	if (totalCount == 0) {
		alert("共0条记录，导出Excel文件失败");
		return;
	}
	if(totalCount>20000){
		alert("导出Excel文件条数大于20000条");
		return;
		
	}
	var mainForm = $("#mainForm");
	var action = mainForm.attr("action");
	
	mainForm.attr("action", "${ctx}/customerOpreting/exportExcel").submit();
	mainForm.attr("action", action);
}
</script>
</body>
</html>