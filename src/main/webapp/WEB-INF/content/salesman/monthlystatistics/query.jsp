<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>销售统计报表</title>
</head>
<body menuId="57">
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
						<span class="label label-info"> <a href="javascript:;"
							onclick="exportExcel()">导出Excel文件</a>
						</span>


						<div class="search">
							<%-- <form method="post" id="mainForm" action="${ctx}/salesman/mothlystatistics/query" onsubmit="return query()"> --%>
							<form method="post" id="mainForm" action="${ctx}/salesman/monthlystatistics/query">
								<ul>
									<li>
										<u:date id="start_time" value="${start_time}" placeholder="开始时间" dateFmt="yyyy-MM" params="" />
										<span>至</span>
	            						<u:date id="end_time" value="${end_time}" placeholder="结束时间" dateFmt="yyyy-MM" params="" />
									</li>
									<li><input type="text" name="name" value="<s:property value="#parameters.name"/>"  maxlength="50" placeholder="销售姓名/客户名称" class="txt_250" /></li>
									<li><input type="submit" value="搜索" /></li>
								</ul>
							</form>
						</div>
					</div>
					<div class="widget-content nopadding">
						<table class="table table-bordered table-striped">
							<thead>
								<tr>
									<th>序号</th>
									<th>销售人员</th>
									<th>客户名称</th>
									<th style="width: 50px;">月份</th>
									<th>备注</th>
									<th>充值金额</th>
									<th>合同单价</th>
									<th>成本价</th>
									<th>利润</th>
									<th>提成</th>
									<th>该客户月提成</th>
									<th>销售人员月提成</th>
									<th>月回款任务额</th>
									<th>月实际回款</th>
									<th>指标完成比率</th>
									<th>指标完成系数</th>
									<th>月实际提成</th>
								</tr>
							</thead>
							<tbody id="userbody">
								<s:iterator value="page.list">
									<tr>
										<td>${rownum}</td>
										<td>${salesman_name}</td>
										<td>${customer_name}</td>
										<td>${recharge_time}</td>
										<td>${rechargemark}</td>
										<td>${rechargemoney}</td>
										<td>${recharge_unit_price}</td>
										<td>${recharge_cost_price}</td>
										<td>${recharge_profit}</td>
										<td>${recharge_royalty}</td>
										<td>${month_customer_royalty}</td>
										<td>${month_salesman_royalty}</td>
										<td>${month_returned_money_task}</td>
										<td>${month_actual_returned_money}</td>
										<td>${target_finished_ratio}</td>
										<td>${coefficient_of_individual_targets}</td>
										<td>${month_actual_royalty}</td>
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
		$(document).ready(function(){

			var row1 = getRowByPos(1);
			var row2 = getRowByPos(2);
			var row3 = getRowByPos(3);
			var rowSpanPos1 = getRowSpanPosArray( row1, row3 );
			var rowSpanPos2 = getRowSpanPosArray( row2, row3 );
			rowSpanManger( 1, rowSpanPos1);
// 			rowSpanManger( 3, rowSpanPos1);
			rowSpanManger( 11, rowSpanPos1);
			rowSpanManger( 12, rowSpanPos1);
			rowSpanManger( 13, rowSpanPos1);
			rowSpanManger( 14, rowSpanPos1);
			rowSpanManger( 15, rowSpanPos1);
			rowSpanManger( 16, rowSpanPos1);
			rowSpanManger( 2, rowSpanPos2);
			rowSpanManger( 10, rowSpanPos2);
			
		});
		
		// 
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
// 					td.remove(); //这里不能移除td，将元素隐藏不占用页面显示位置
					td.setAttribute("style", "display:none;"); //其余的td全部删除
				}
			}
		}

		/*根据a,b获得a中连续元素可以合并的位置
		* a = [1,1,1,1,2,2,2,5,5,5,6,6,6]
		* b = [1,1,3,3,2,2,2,5,9,5,6,6,6]
		* 返回 ["1,0,1", "1,2,3", "2,4,6", "5,7,7", "5,8,8", "5,9,9", "6,10,12"]
		*/ 
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
// 		     var a = [1,1,1,1,2,2,2,5,5,5,6,6,6];
// 		     var b = [1,1,3,3,2,2,2,5,9,5,6,6,6];
// 		     var a = ["test2","张三","张三","张三","张三","张三","张三","李四","李四","张三","张三","test2"];
// 		     var b = ["2016-09","2016-09","2016-09","2016-09","2016-09","2016-09","2016-09","2016-09","2016-09","2016-08","2016-07","2016-03"];
		     for (var i = 1; i < a.length; i++){
		    	 if(a[i] != a[posStartA]){
// 		    		 console.log("A数组：" + a[posStartA] + "从" + posStartA + "开始，到" + (i-1) + "结束");
		    		 element = a[posStartA];
		    		 posEndA = i - 1;
		    		 posStartB = posStartA;
			         posStartA = i;
			         isSearchB = true;
			         if( i == a.length -1 && posEndA2 == -1){
// 			        	 console.log("A数组：" + a[i] + "从" + posStartA + "开始，到" + i + "结束" + "i =" + i);
			        	 element2 = a[i];
			        	 posEndA2 = i;
			        	 posArray.push(a[i] + "," + posStartA + "," + i);
			         }
		    	 }else{
		    		 if( i == a.length -1){
// 		        		 console.log("A数组：" + a[posStartA] + "从" + posStartA + "开始，到" + i + "结束");
		        		 element = a[posStartA];
		        		 posEndA = i;
		        		 posStartB = posStartA;
		        		 isSearchB = true;
		        	 }
		    	 }
		    	 
		    	 if(isSearchB){
		    		 for (var j = posStartB; j < b.length; j++){
		            	 if(b[j] != b[posStartB]){
// 		            		 console.log("B数组：" + b[posStartB] + "从" + posStartB + "开始，到" + (j-1) + "结束");
		            		 posEndB = j - 1;
		        	         break;
		            	 }else{
		            		 if( j == b.length -1){
// 		                		 console.log("B数组：" + b[posStartB] + "从" + posStartB + "开始，到" + j + "结束");
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
			var totalCount = ${page.totalCount};
			if (totalCount == 0) {
				alert("共0条记录，导出Excel文件失败");
				return;
			}
			if(totalCount>6000){
				alert("导出Excel文件条数大于6000条");
				return;
				
			}
			var mainForm = $("#mainForm");
			var action = mainForm.attr("action");
			mainForm.attr("action", "${ctx}/salesman/monthlystatistics/exportExcel").submit();
			mainForm.attr("action", action);
		}
		
	</script>
</body>
</html>