<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>客户运维报表查询</title>
<style type="text/css">
	.widget-title .search,.widget-title .search ul { float: left; display: inline; /*height: 36px;*/ margin-bottom: 0px; }
	.pagenum{
		text-align: left !important;
	}
</style>
</head>
<body menuId="75">
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
						 <form method="post" id="mainForm" action="${ctx}/customerOpretion/query" onsubmit="return query()">
						 	<div>
							 	<ul>
									<li>
										<u:select id="stat_type" value="${stat_type}" placeholder="统计类型" dictionaryType="consume_stat_type" onChange="changeStatType" />
									</li>
									<li id="li_day" ${stat_type==1 ? "" : "style='display:none'"}>
										<u:date id="start_time_day" value="${start_time_day}" placeholder="开始时间" dateFmt="yyyyMMdd" params="" />
										<span>至</span>
	            						<u:date id="end_time_day" value="${end_time_day}" placeholder="结束时间" dateFmt="yyyyMMdd" params="" />
									</li>
									<li id="li_month" ${stat_type==2 ? "" : "style='display:none'"}>
										<u:date id="start_time_month" value="${start_time_month}" placeholder="开始时间" dateFmt="yyyyMM" params="minDate:'{%y-1}-%M-%d', maxDate:'#F{$dp.$D(\\'end_time_month\\')||\\'%y-%M-%d\\'}'" />
										<span>至</span>
	            						<u:date id="end_time_month" value="${end_time_month}" placeholder="结束时间" dateFmt="yyyyMM" params="minDate:'#F{$dp.$D(\\'start_time_month\\')||\\'{%y-1}-%M-%d\\'}', maxDate:'%y-%M-%d'" />
									</li>
									<li>
										<input type="text" name="text" value="<s:property value="#parameters.text"/>" placeholder="用户ID/用户名称/代理商ID" />
									</li>
									<li>
										<u:select id="operatorstype" value="${param.operatorstype}" placeholder="运营商类型" dictionaryType="channel_operatorstype"  />
									</li>
								</ul>
						 	</div>
						 	<div>
						 		<ul>
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
						 	</div>
						  </form>
						</div>
					</div>
					<div class="widget-content nopadding">
					<u:page page="${data.page}" formId="mainForm" />
						<table class="table table-bordered table-striped" id="userbody">
							<thead>
								<tr>
									 <th>序号</th>
					                 <th>时间</th>
					                 <th>用户ID</th>
					                 <th>用户名称</th>
					                 <th>代理商ID</th>
					                 <th>归属销售</th>
					                 <th>运营商类型</th>
					                 <th>通道号</th>
					                 <th>通道备注</th>
					                 <th title="0+1+3+4+5+6+7+8+9+10">用户短信总量</th>
					                 <th title="1+3+6">总发送量</th>
					                 <th>成功率(3/总发送量)</th>
					                 <th>成功量(1+3)</th>
					                 <th>未发送(0)</th>
					                 <th>提交成功(1)</th>
					                 <th>明确成功(3)</th>
					                 <th>提交失败(4)</th>
					                 <th>发送失败(5)</th>
					                 <th>明确失败(6)</th>
					                 <th>审核不通过(7)</th>
					                 <th>网关接收拦截(8)</th>
					                 <th>网关发送拦截(9)</th>
					                 <th>超频拦截(10)</th>
									 <th>计费规则</th>
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
								      <td>${v.operatorstype_name}</td>
								      <td>${v.channelid}</td>
								      <td>${v.remark}</td>
								      <td>${v.usersmstotal}</td>
								      <td>${v.sendtotal}</td>
								      <td>${v.successrate}</td>
								      <td>${v.successtotal}</td>
								      <td>${v.notsend}</td>
								      <td>${v.submitsuccess}</td>
								      <td>${v.reportsuccess}</td>
								      <td>${v.submitfail}</td>
								      <td>${v.subretfail}</td>
								      <td>${v.reportfail}</td>
								      <td>${v.auditfail}</td>
								      <td>${v.recvintercept}</td>
								      <td>${v.sendintercept}</td>
								      <td>${v.overrateintercept}</td>
										<td>${v.chargeRuleStr}</td>
									</tr>
							    </s:iterator>
							    
							    <s:if test="data.total != null && data.page.list.size != 0">
									<tr style="font-weight: bold;">
									  <td colspan="9" style="text-align: center;">总计</td>
								      <td>${data.total.usersmstotal}</td>
								      <td>${data.total.sendtotal}</td>
								      <td>${data.total.successrate}</td>
								      <td>${data.total.successtotal}</td>
								      <td>${data.total.notsend}</td>
								      <td>${data.total.submitsuccess}</td>
								      <td>${data.total.reportsuccess}</td>
								      <td>${data.total.submitfail}</td>
								      <td>${data.total.subretfail}</td>
								      <td>${data.total.reportfail}</td>
								      <td>${data.total.auditfail}</td>
								      <td>${data.total.recvintercept}</td>
								      <td>${data.total.sendintercept}</td>
								      <td>${data.total.overrateintercept}</td>
										<td>-</td>
								     </tr>
							    </s:if>
							   
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>

<script type="text/javascript">
$(function(){
	/* var row1 = getRowByPos(1);
	console.log(row1);
	var row2 = getRowByPos(2);
	console.log(row2);
	var row3 = getRowByPos(3);
	console.log(row3);
	var rowSpanPos1 = getRowSpanPosArray( row1, row3 );
	var rowSpanPos2 = getRowSpanPosArray( row2, row3 );
	rowSpanManger( 1, rowSpanPos1);
	rowSpanManger( 2, rowSpanPos2); */
	/* rowSpanManger( 11, rowSpanPos1);
	rowSpanManger( 12, rowSpanPos1);
	rowSpanManger( 13, rowSpanPos1);
	rowSpanManger( 14, rowSpanPos1); */
	/* rowSpanManger( 15, rowSpanPos1);
	rowSpanManger( 16, rowSpanPos1); */
	/* rowSpanManger( 10, rowSpanPos2); */
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


var rowSpanManger = function( row, rowSpanPos ){
	var element;
	var start;
	var end;
	for(var i = 0; i < rowSpanPos.length; i ++){
		// 获得显示元素及其开始结束位置
		var splitTemp = rowSpanPos[i].split(",");
		element = splitTemp[0];
		start = Number(splitTemp[1]);
		end = Number(splitTemp[2]);
		if(start != end){
			rowSpanProcesser( row, start, end );
		}
	}
}

// 合并某一列中指定区间
var rowSpanProcesser = function ( row, start, end ){
	var trArray = $("#userbody tr");//页面上所有的tr(行)
	var tr;
	var td;
	if( start > trArray.length || end > trArray.length || start < 0 || end < 0 ){
		console.log("合并列失败，请联系管理员");
		return;
	}
	for(var i = start; i <= end; i ++){
		tr = trArray[i];
		td = tr.cells[row];
		if( i == start ){
			td.setAttribute("rowspan", end - start + 1); // 第一个td设置rowspan
		}else{
			td.setAttribute("style", "display:none;"); //其余的td全部删除
		}
	}
}

var getRowSpanPosArray = function ( a, b) {
     var posStartA = 0;
     var posStartB = 0;
     var element;
     var element2;
     var posEndA = -1;
     var posEndA2 = -1; 
     var posEndB = -1; 
     var isSearchB = false;
     var posArray = [];
     for (var i = 1; i < a.length; i++){
    	 if(a[i] != a[posStartA]){
    		 element = a[posStartA];
    		 posEndA = i - 1;
    		 posStartB = posStartA;
	         posStartA = i;
	         isSearchB = true;
	         if( i == a.length -1 && posEndA2 == -1){
	        	 element2 = a[i];
	        	 posEndA2 = i;
	        	 posArray.push(a[i] + "," + posStartA + "," + i);
	         }
    	 }else{
    		 if( i == a.length -1){
        		 element = a[posStartA];
        		 posEndA = i;
        		 posStartB = posStartA;
        		 isSearchB = true;
        	 }
    	 }
    	 
    	 if(isSearchB){
    		 for (var j = posStartB; j < b.length; j++){
            	 if(b[j] != b[posStartB]){
            		 posEndB = j - 1;
        	         break;
            	 }else{
            		 if( j == b.length -1){
                		 posEndB = j;
                		 break;
                	 }
            	 }
             }
    		 
    		 if(posEndA > posEndB){
    			 posEndA = posEndB;
        		 if(posEndA < a.length - 1){
        			 posStartA = posEndA + 1;
        			 i = posStartA;
        		 }
    		 }
        	 
        	 posArray.push(element + "," + posStartB + "," + posEndA);
    	 }
    	 
    	 isSearchB = false;
     }
     return posArray;
}

// 获得页面表格中的某一列
var getRowByPos = function( pos ){
	var trArray = $("#userbody tr");//页面上所有的tr
	var rowArray = [];
	var tr;
	var td;
	for( var i = 0; i < trArray.length; i++ ){
		var tr = trArray[i];
		var td = tr.cells[pos];
		rowArray.push( td.textContent );
	}
	return rowArray;
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
	
	mainForm.attr("action", "${ctx}/customerOpretion/exportExcel").submit();
	mainForm.attr("action", action);
}

</script>
</body>
</html>