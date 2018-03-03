<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
<title>分享通道测试报告</title>
<style type="text/css">
	body{
		background: #F8F8F8 !important;
	}
	.fixRow {
		margin-left: 0px;
	}
	.clear{ clear:both }
	.fixRow2 {
	    margin-left: -70px;
	}
</style>
<script src="${ctx}/js/echart/echarts.min.js"></script>
</head>

<body menuId="${data.menuId}">
	<!--Action boxes-->
	<div class="container-fluid">
		<hr>
		<div class="row-fluid">
			<div class="span12">
				
				<!-- 这里的通道基本属性是分享表中的 -->
				<s:if test="data.reportMap.report_type eq 2">
						<div class="widget-box">
						<div class="widget-title">
							<span class="icon"> <i class="fa fa-list"> 通道基本属性  </i>
							</span>
						</div>
						<div class="widget-content nopadding">
							<form class="form-horizontal" method="post" action="#" id="mainForm">
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">通道账号：</label>
											<div class="controls">
												<span>${data.channelInfo.account}</span>
											</div>
										</div>
										<div class="span5">
											<label class="control-label">通道密码：</label>
											<div class="controls">
												<span>${data.channelInfo.password}</span>
											</div>
										</div>
									</div>
								</div>
								
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">运营商类型：</label>
											<div class="controls">
												<span>
													<s:if test="data.channelInfo.operators_type==0">全网</s:if>
													<s:elseif test="data.channelInfo.operators_type==1">移动</s:elseif>
													<s:elseif test="data.channelInfo.operators_type==2">联通</s:elseif>
													<s:elseif test="data.channelInfo.operators_type==3">电信</s:elseif>
													<s:elseif test="data.channelInfo.operators_type==4">国际</s:elseif>
												</span>
											</div>
										</div>
										
										<div class="span5">
											<label class="control-label">接入号：</label>
											<div class="controls">
												<span>${data.channelInfo.access_id}</span>
											</div>
										</div>
									</div>
								</div>
								
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">接入协议：</label>
											<div class="controls">
												<span>
													<s:if test="data.channelInfo.protocol_type==3">CMPP</s:if>
													<s:elseif test="data.channelInfo.protocol_type==4">SMGP</s:elseif>
													<s:elseif test="data.channelInfo.protocol_type==5">SGIP</s:elseif>
												</span>
											</div>
										</div>
										
										<div class="span5">
											<label class="control-label">自扩展位数：</label>
											<div class="controls">
												<span>${data.channelInfo.extend_size}</span>
											</div>
										</div>
									</div>
								</div>
								
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">登陆状态：</label>
											<div class="controls">
												<span>
													<s:if test="data.channelInfo.login_status eq 0">未登陆</s:if>
													<s:elseif test="data.channelInfo.login_status eq 1">登陆成功</s:elseif>
													<s:elseif test="data.channelInfo.login_status eq 2">登陆失败</s:elseif>
												</span>
											</div>
										</div>
										<div class="span5">
											<label class="control-label">登陆描述：</label>
											<div class="controls">
												<span>${data.channelInfo.login_desc}</span>
											</div>
										</div>
									</div>
								</div>
								
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">接入IP：</label>
											<div class="controls">
												<span>${data.channelInfo.mt_ip}</span>
											</div>
										</div>
										<div class="span5">
											<label class="control-label">接入端口：</label>
											<div class="controls">
												<span>${data.channelInfo.mt_port}</span>
											</div>
										</div>
									</div>
								</div>
								
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">企业代码：</label>
											<div class="controls">
												<span>${data.channelInfo.sp_id}</span>
											</div>
										</div>
										<div class="span5">
											<label class="control-label">服务代码：</label>
											<div class="controls">
												<span>${data.channelInfo.service_id}</span>
											</div>
										</div>
									</div>
								</div>
								
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">状态报告：</label>
											<div class="controls">
												<span>
													<s:if test="data.channelInfo.is_report eq 0">不支持</s:if>
													<s:elseif test="data.channelInfo.is_report eq 1">支持</s:elseif>
												</span>
											</div>
										</div>
										<div class="span5">
											<label class="control-label">长短信：</label>
											<div class="controls">
												<span>
													<s:if test="data.channelInfo.is_long_sms eq 0">不支持</s:if>
													<s:elseif test="data.channelInfo.is_long_sms eq 1">支持</s:elseif>
												</span>
											</div>
										</div>
									</div>
								</div>
								
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">上行短信：</label>
											<div class="controls">
												<span>
													<s:if test="data.channelInfo.is_mo eq 0">不支持</s:if>
													<s:elseif test="data.channelInfo.is_mo eq 1">支持</s:elseif>
												</span>
											</div>
										</div>
										<div class="span5">
											<label class="control-label">创建时间：</label>
											<div class="controls">
												<span>
													<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${data.channelInfo.create_time}" />
												</span>
											</div>
										</div>
									</div>
								</div>
								
							</form>
						</div>
					</div>
				</s:if>
				
				
				<!-- 1：智能测试系统生成测试报告，2：未提交智能测试直接写入测试结论的测试报告  只有report_type等于1的时候才展示通道质量统计 -->
				<s:if test="data.reportMap.report_type eq 1">
						<div class="widget-box">
						<div class="widget-title">
							<!-- 这里的通道基本属性是测试报告中的 -->
							<span class="icon"> <i class="fa fa-list"> 通道基本属性  </i>
							</span>
						</div>
						<div class="widget-content nopadding">
							<form class="form-horizontal" method="post" action="#" id="mainForm">
								
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">通道账号：</label>
											<div class="controls">
												<span>${data.reportMap.account}</span>
											</div>
										</div>
										<div class="span5">
											<label class="control-label">通道密码：</label>
											<div class="controls">
												<span>${data.reportMap.password}</span>
											</div>
										</div>
									</div>
								</div>
								
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">运营商类型：</label>
											<div class="controls">
												<span>
													<s:if test="data.reportMap.operators_type==0">全网</s:if>
													<s:elseif test="data.reportMap.operators_type==1">移动</s:elseif>
													<s:elseif test="data.reportMap.operators_type==2">联通</s:elseif>
													<s:elseif test="data.reportMap.operators_type==3">电信</s:elseif>
													<s:elseif test="data.reportMap.operators_type==4">国际</s:elseif>
												</span>
											</div>
										</div>
										
										<div class="span5">
											<label class="control-label">接入号：</label>
											<div class="controls">
												<span>${data.reportMap.access_id}</span>
											</div>
										</div>
									</div>
								</div>
								
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">接入协议：</label>
											<div class="controls">
												<span>
													<s:if test="data.reportMap.protocol_type==3">CMPP</s:if>
													<s:elseif test="data.reportMap.protocol_type==4">SMGP</s:elseif>
													<s:elseif test="data.reportMap.protocol_type==5">SGIP</s:elseif>
												</span>
											</div>
										</div>
										
										<div class="span5">
											<label class="control-label">自扩展位数：</label>
											<div class="controls">
												<span>${data.reportMap.extend_size}</span>
											</div>
										</div>
									</div>
								</div>
								
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">登陆状态：</label>
											<div class="controls">
												<span>
													<s:if test="data.reportMap.login_status eq 0">未登陆</s:if>
													<s:elseif test="data.reportMap.login_status eq 1">登陆成功</s:elseif>
													<s:elseif test="data.reportMap.login_status eq 2">登陆失败</s:elseif>
												</span>
											</div>
										</div>
										<div class="span5">
											<label class="control-label">登陆描述：</label>
											<div class="controls">
												<span>${data.reportMap.login_desc}</span>
											</div>
										</div>
									</div>
								</div>
								
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">接入IP：</label>
											<div class="controls">
												<span>${data.reportMap.mt_ip}</span>
											</div>
										</div>
										<div class="span5">
											<label class="control-label">接入端口：</label>
											<div class="controls">
												<span>${data.reportMap.mt_port}</span>
											</div>
										</div>
									</div>
								</div>
								
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">企业代码：</label>
											<div class="controls">
												<span>${data.reportMap.sp_id}</span>
											</div>
										</div>
										<div class="span5">
											<label class="control-label">服务代码：</label>
											<div class="controls">
												<span>${data.reportMap.service_id}</span>
											</div>
										</div>
									</div>
								</div>
								
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">状态报告：</label>
											<div class="controls">
												<span>
													<s:if test="data.reportMap.is_report eq 0">不支持</s:if>
													<s:elseif test="data.reportMap.is_report eq 1">支持</s:elseif>
												</span>
											</div>
										</div>
										<div class="span5">
											<label class="control-label">长短信：</label>
											<div class="controls">
												<span>
													<s:if test="data.reportMap.is_long_sms eq 0">不支持</s:if>
													<s:elseif test="data.reportMap.is_long_sms eq 1">支持</s:elseif>
												</span>
											</div>
										</div>
									</div>
								</div>
								
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">上行短信：</label>
											<div class="controls">
												<span>
													<s:if test="data.reportMap.is_mo eq 0">不支持</s:if>
													<s:elseif test="data.reportMap.is_mo eq 1">支持</s:elseif>
												</span>
											</div>
										</div>
										<div class="span5">
											<label class="control-label">创建时间：</label>
											<div class="controls">
												<span>
													<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${data.channelInfo.create_time}" />
												</span>
											</div>
										</div>
									</div>
								</div>
								
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">测试报告单号：</label>
											<div class="controls">
												<span>${data.reportMap.report_id}</span>
											</div>
										</div>
										<div class="span5">
											<label class="control-label">测试人员：</label>
											<div class="controls">
												<span>${data.reportMap.admin_name}</span>
											</div>
										</div>
									</div>
								</div>
								
								
							</form>
						</div>
					</div>
				
					<div class="widget-box">
						<div class="widget-title">
							<span class="icon"> <i class="fa fa-th"> 短信质量统计 </i>
							</span>
						</div>
						<div class="col-sm-6" style="">
		    					<div id="successRateChart" style="width:50%; height:300px; float:left; margin-top:40px;"></div>
								<div id="errorRateChart" style="width:50%; height:300px; float:left; margin-top:40px;"></div>
								<div class="clear"></div>
		    					<div id="respRateChart" style="width: 50%; height:300px; float:left; margin-top:40px;"></div>
		    					<div id="reportRateChart" style="width:50%; height:300px; float:left; margin-top:40px;"></div>
								<div class="clear"></div>
						</div>
					</div>
				</s:if>
				
				<div class="widget-box">
					
					<input id="report_id" type="hidden" value="${data.reportMap.report_id}"></input>
					<div class="widget-title">
						<span class="icon"> <i class="fa fa-list"> 测试结论 </i>
						</span>
					</div>
					<div class="widget-content nopadding">
						<form class="form-horizontal" method="post" action="#" id="testComment">
							<%-- <s:if test="data.reportMap.is_test_paas == null">
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">是否通过：</label>
											<div class="controls">
												<span>
													<s:if test="data.reportMap.is_test_paas eq 0">不通过</s:if>
													<s:elseif test="data.reportMap.is_test_paas eq 1">通过</s:elseif>
													<s:else>无结论</s:else>
												</span>
											</div>
										</div>
									</div>
									<s:if test="data.reportMap.is_test_paas eq 0 or data.reportMap.is_test_paas eq 1">
										<div class="row fixRow">
											<div class="span5">
												<label class="control-label">结论：</label>
												<div class="controls">
													<s:if test="data.channelInfo.state == 2">
														<span>${data.reportMap.test_comment}</span>
													</s:if>
													<s:else>
														<textarea id="test_comment" name="test_comment" rows="3" cols="90"
															style="height: auto; width: auto;" maxlength="200"
															class="checkWhiteList">${data.reportMap.test_comment}</textarea>
													</s:else>
												</div>
											</div>
										</div>
									</s:if>
								</div>
							</s:if>
							<s:elseif test="data.reportMap.is_test_paas != null">
								<div class="control-group">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">是否通过：</label>
											<div class="controls">
												<span>
													<s:if test="data.reportMap.is_test_paas eq 0">不通过</s:if>
													<s:else>通过</s:else>
												</span>
											</div>
										</div>
									</div>
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">结论：</label>
											<div class="controls">
												<span>${data.reportMap.test_comment}</span>
											</div>
										</div>
									</div>
								</div>
							</s:elseif> --%>
							
							
							<div class="control-group">
								<s:if test="data.channelInfo.state!=0">
									<div class="row fixRow">
										<div class="span5">
											<label class="control-label">是否通过：</label>
											<div class="controls">
												<span>
													<s:if test="data.reportMap.is_test_paas eq 0">不通过</s:if>
													<s:elseif test="data.reportMap.is_test_paas eq 1">通过</s:elseif>
													<s:else> - </s:else>
												</span>
											</div>
										</div>
									</div>
								</s:if>
								<div class="row fixRow">
									<div class="span5">
										<label class="control-label">结论：</label>
										<div class="controls">
											<s:if test="data.channelInfo.state==0">
												<textarea id="test_comment" name="test_comment" rows="3" cols="90"
													style="height: auto; width: auto;" maxlength="200"
													class="checkWhiteList">${data.reportMap.test_comment}</textarea>
											</s:if>
											<s:else>
												<!-- 没有判断测试报告是否通过直接关闭测试通道 -->
												<s:if test="data.reportMap.is_test_paas==null">
													<span> - </span>
												</s:if>
												<s:else>
													<span>${data.reportMap.test_comment}</span>
												</s:else>
											</s:else>
										</div>
									</div>
								</div>
							</div>
							
							
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>



<script type="text/javascript">
var reportId = "${data.reportId==null?'':data.reportId}";
var reportType = "${data.reportMap==null?'':data.reportMap.report_type}";

var successRateChart;
var errorRateChart;
var respRateChart;
var reportRateChart;

var validate;
$(function(){
	
	//  1：智能测试系统生成测试报告，2：未提交智能测试直接写入测试结论的测试报告  只有report_type等于1的时候才展示通道质量统计
	if(reportType == 1){
		
		successRateChart = echarts.init(document.getElementById('successRateChart'));
		errorRateChart = echarts.init(document.getElementById('errorRateChart'));
		respRateChart = echarts.init(document.getElementById('respRateChart'));
		reportRateChart = echarts.init(document.getElementById('reportRateChart'));
		
		echartShowLoading();
		
		var options = echartOptionInit();
		
		successRateChart.setOption(options.succRatePieOption);
		respRateChart.setOption(options.respRatePieOption);
		reportRateChart.setOption(options.reportRatePieOption);
		errorRateChart.setOption(options.errorRatePieOption);
		
		
		loadEchartData();
	}
	
	
	$.validator.defaults.ignore = "";
	validate = $("#testComment").validate({
		invalidHandler: function(form, validator) {
	        var errors = validator.numberOfInvalids();
	        if (errors) {                    
	            validator.errorList[0].element.focus();
	        }
	    },
		rules: {
			test_comment:{
				required: true
			}
		}
		
	});
});

function validateTestComment(){
	var result = {};
	if (!validate.form()) {
		result.isComment = false;
		result.commentText = "";
		result.report_id = "";
		return false;
	}else{
		result.isComment = true;
		result.commentText = $("#test_comment").val();
		result.report_id = $("#report_id").val();
	}
	return result;
}


// 加载Echart数据
function loadEchartData(){
	
	console.log("测试报告ID：" + reportId);
	if(reportId != null && reportId != ""){
		$.ajax({
			type : "post",
			url : "${ctx}/channelTest/queryTestReportEchartDataByReportId",
			data : {
				reportId : reportId,
			},
			success : function(data) {
				if (data != null) {
					setPieData(successRateChart, data.smsStateList);
					setPieData(respRateChart, data.reportIndexesList);
					setPieData(reportRateChart, data.respIndexesList);
					setErrorRatePieData(errorRateChart, data.cloudShareErrorMap);
				}
			}
		});
		
	}
	
}

//设置Echart饼图数据（发送成功率、应答率、回执率）
function setPieData(myChart, data){
	myChart.hideLoading();
	if (data != null) {
		myChart.setOption({
			series : [
				        {
				            data : data
				        }
				    ]
 	    });
	}
}

function setErrorRatePieData(myChart, data){
	myChart.hideLoading();
	if (data != null) {
		myChart.setOption({
			/* legend: {
 		        data: data.legendList
		    }, */
			series : [
				        {
				            data : data.pieDataList
				        }
				    ]
 	    });
	}
}

function echartOptionInit(){
	var result = {};
	
	result.succRatePieOption = {
		    title : {
		        text: '短信状态分布',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    /* legend: {
		        orient: 'vertical',
		        left: 'right',
		        data: ['提交成功','发送成功','明确成功','提交失败','发送失败','明确失败']
		    }, */
		    color:['#6e7074','#61a0a8','#91c7ae','#ca8622','#2f4554','#c23531'],
		    series : [
		        {
		            name: '短信状态分布',
		            type: 'pie',
		            radius : '55%',
		            center: ['50%', '60%'],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
		            label: {
		                normal: {
		                    show: true,
		                    formatter: "{b} : {c}条"
		                },
		                emphasis: {
		                    show: true,
		                    textStyle: {
		                        fontSize: '30',
		                        fontWeight: 'bold'
		                    }
		                }
		            }
		        }
		    ]
		};
	
	result.respRatePieOption = {
		    title : {
		        text: '回执率',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/> {b} / {c} / {d}%"
		    },
		    /* legend: {
		        orient: 'vertical',
		        left: 'right',
		        data: ['0-2s','3-10s','11-300s','>300s','应答超时']
		    }, */
		    color:['#91c7ae','#61a0a8','#6e7074','#d48265','#c23531'],
		    series : [
		        {
		            name: '时间/条数/占比',
		            type: 'pie',
		            radius : '55%',
		            center: ['50%', '60%'],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
		            label: {
		                normal: {
		                    show: true,
		                    formatter: "{b} : {c}条"
		                },
		                emphasis: {
		                    show: true,
		                    textStyle: {
		                        fontSize: '30',
		                        fontWeight: 'bold'
		                    }
		                }
		            }
		        }
		    ]
		};
	
	
	result.reportRatePieOption = {
		    title : {
		        text: '应答率',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/> {b} / {c} / {d}%"
		    },
		    /* legend: {
		        orient: 'vertical',
		        left: 'right',
		        data: ['0-5s','6-10s','11-30s','31-60s','61-300s','>300s','回执未返回']
		    }, */
		    color:['#91c7ae','#61a0a8','#6e7074','#ca8622','#2f4554','#d48265','#c23531'],
		    series : [
		        {
		            name: '时间/条数/占比',
		            type: 'pie',
		            radius : '55%',
		            center: ['50%', '60%'],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
		            label: {
		                normal: {
		                    show: true,
		                    formatter: "{b} : {c}条"
		                },
		                emphasis: {
		                    show: true,
		                    textStyle: {
		                        fontSize: '30',
		                        fontWeight: 'bold'
		                    }
		                }
		            }
		        }
		    ]
		};
	
	result.errorRatePieOption = {
			title : {
		        text: '错误码',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} / {c}条 ({d}%)"
		    },
		    /* legend: {
		        orient: 'vertical',
		        left: 'right'
		    }, */
		    series : [
		        {
		            name: '错误类型',
		            type: 'pie',
		            radius : '55%',
		            center: ['50%', '60%'],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
		            label: {
		                normal: {
		                    show: true,
		                    formatter: "{b} / {c}条 ({d}%)"
		                },
		                emphasis: {
		                    show: true,
		                    textStyle: {
		                        fontSize: '30',
		                        fontWeight: 'bold'
		                    }
		                }
		            }
		        }
		    ]
	};
	
	return result;
}

function echartShowLoading(){
	successRateChart.showLoading({  
	    text: '正在努力加载中...'  
	});
	
	respRateChart.showLoading({  
	    text: '正在努力加载中...'  
	});
	
	reportRateChart.showLoading({  
	    text: '正在努力加载中...'  
	});
	
	errorRateChart.showLoading({  
	    text: '正在努力加载中...'  
	}); 
}


</script>
</body>
</html>