<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<title>${data.id==null?'添加':'修改'}短信通道</title>
</head>

<body menuId="16">
<!--Action boxes-->
  <div class="container-fluid">
    <hr>
    <div class="row-fluid">
        <div class="span12">
            <div class="widget-box">
                <div class="widget-title"><span class="icon"> <i class="fa fa-list"></i> </span>
                    <h5></h5>
                </div>
                <div class="widget-content nopadding">
                    <form class="form-horizontal" method="post" action="#" id="mainForm">
                        <div class="control-group">
                            <label class="control-label">通道号</label>
                            <div class="controls">
                                <s:if test="data.id != null">
                                    <input type="text" name="cid" value="${data.cid}" readonly="readonly"/>
                                </s:if>
                                <s:else>
                                    <input type="text" name="cid" value="${data.cid}"
                                           onfocus="inputControl.setNumber(this, 4, 0, false)"/>
                                </s:else>
                                <span class="help-block">* 1-4位数字</span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">通道账号</label>
                            <div class="controls">
                                <input type="text" name="clientid" value="${data.clientid}" maxlength="60"/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">选择标识</label>
                            <div class="controls">
                                <select id="identify" name="identify"></select>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">密码</label>
                            <div class="controls">
                                <input type="text" name="password" value="${data.password}" maxlength="60"/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">通道名称</label>
                            <div class="controls">
                                <input type="text" name="channelname" value="${data.channelname}" maxlength="200"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">通道成功率预警值</label>
                            <div class="controls">
                                <input type="text" name="warn_succ_rate" value="${data.warn_succ_rate}" maxlength="50"/>
                                <span class="help-block">* 指定通道成功率预警阈值</span>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">通道及时率预警值</label>
                            <div class="controls">
                                <input type="text" name="warn_time_rate" value="${data.warn_time_rate}" maxlength="50"/>
                                <span class="help-block">* 指定通道及时率预警阈值</span>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">通道运营商类型</label>
                            <div class="controls">
                                <u:select id="operatorstype" dictionaryType="channel_operatorstype"
                                          value="${data.operatorstype}" placeholder="类型" excludeValue="5,6,7"/>
                                <span class="help-block">* 指定用户的短信通过哪个电信商发送出去</span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">长短信</label>
                            <div class="controls">
                                <select name="longsms">
                                    <option value="0" ${0==data.longsms?'selected="selected"':'' }>不支持</option>
                                    <option value="1" ${1==data.longsms?'selected="selected"':'' }>支持</option>
                                </select>
                                <span class="help-block">* 如果支持长短信则不用拆分短信</span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">扩展属性</label>
                            <div class="controls">
                                <input name="exvalue" class="cn" type="checkbox" value="1"/>中文&nbsp;&nbsp;&nbsp;&nbsp;
                                <input name="exvalue" class="en" type="checkbox" value="2"/>英文
                                <input name="exvalue" class="mq" type="checkbox" value="4"/>是否缓存状态报告到MQ队列
                                <label id="exvalue-error" class="error" for="exvalue" style="display: none;"></label>
                            </div>

                        </div>
                        <div class="control-group">
                            <label class="control-label">WAP PUSH</label>
                            <div class="controls">
                                <select name="wappush">
                                    <option value="0" ${!data.wappush?'selected="selected"':'' }>不支持</option>
                                    <option value="1" ${data.wappush?'selected="selected"':'' }>支持</option>
                                </select>
                                <span class="help-block">* 短信内容是否允许包含链接</span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">请求方式</label>
                            <div class="controls">
                                <u:select id="httpmode" dictionaryType="channel_http_mode" value="${data.httpmode}"
                                          excludeValue="" onChange="httpmodeChange"/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">接口地址</label>
                            <div class="controls">
                                <input type="text" name="url" value="${data.url}" maxlength="200"/>
                                <span class="help-block red">* 发送短信接口地址，手机号码值为%phone%，短信内容值为%content%，例如：http://sms.keepc.com/sms.php?user=abcd&passwd=123456&phone=%phone%&content=%content%</span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">短信编码</label>
                            <div class="controls">
                                <u:select id="coding" dictionaryType="channel_coding" value="${data.coding}"
                                          excludeValue=""/>
                                <span class="help-block">* 短信内容编码</span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">POST数据</label>
                            <div class="controls">
                                <input type="text" name="postdata"
                                       value="<s:property escape="true" value="data.postdata"/>" maxlength="1000"/>
                                <span class="help-block red">* 如果Http方式方法为POST，此处请填写参数格式</span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">成本价</label>
                            <div class="controls">
                                <input type="text" name="costprice" value="${data.costprice}"
                                       onfocus="inputControl.setNumber(this, 10, 4, false)"/>
                                <span class="help-block red">* 用于rest计费 格式:0.0000</span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">查询状态地址</label>
                            <div class="controls">
                                <input type="text" name="querystateurl" value="${data.querystateurl}" maxlength="200"/>
                                <span class="help-block red">例如：http://sms.keepc.com/sms.php?user=abcd&passwd=123456</span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">查询状态POST数据</label>
                            <div class="controls">
                                <input type="text" name="querystatepostdata"
                                       value="<s:property escape="true" value="data.querystatepostdata"/>"
                                       maxlength="500"/>
                                <span class="help-block red">* 如果Http方式方法为POST，此处请填写参数格式</span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">查询上行地址</label>
                            <div class="controls">
                                <input type="text" name="queryupurl" value="${data.queryupurl}" maxlength="500"/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">查询上行POST数据</label>
                            <div class="controls">
                                <input type="text" name="queryuppostdata"
                                       value="<s:property escape="true" value="data.queryuppostdata"/>"
                                       maxlength="500"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">认证地址</label>
                            <div class="controls">
                                <input type="text" name="oauth_url" value="${data.oauth_url}"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">认证POST数据</label>
                            <div class="controls">
                                <input type="text" name="oauth_post_data" value="${data.oauth_post_data}"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">增加模板地址</label>
                            <div class="controls">
                                <input type="text" name="add_temp_url" value="${data.add_temp_url}"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">增加模板POST数据</label>
                            <div class="controls">
                                <input type="text" name="add_temp_post_data" value="${data.add_temp_post_data}"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">查询模板审核状态地址</label>
                            <div class="controls">
                                <input type="text" name="get_temp_list_url" value="${data.get_temp_list_url}"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">查询模板审核状态POST数据</label>
                            <div class="controls">
                                <input type="text" name="get_temp_list_post_data"
                                       value="${data.get_temp_list_post_data}"/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">显示号码</label>
                            <div class="controls">
                                <input type="text" name="shownum" value="${data.shownum}"
                                       onfocus="inputControl.setNumber(this, 9, 0, false)"/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">显示签名</label>
                            <div class="controls">
                                <input type="text" name="showsign" value="${data.showsign}" maxlength="50"/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">显示签名的方式</label>
                            <div class="controls">
                                <u:select id="showsigntype" dictionaryType="showsigntype" value="${data.showsigntype}"
                                          excludeValue=""/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">接入号</label>
                            <div class="controls">
                                <input type="text" name="accessid" value="${data.accessid}" maxlength="50"/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">节点编码</label>
                            <div class="controls">
                                <input type="text" id="node" name="node" value="${data.node}"
                                       onfocus="inputControl.setNumber(this, 10, 0, false)"/>
                                <!--                   <span class="help-block">联通专有</span> -->
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">企业代码</label>
                            <div class="controls">
                                <input type="text" id="spid" name="spid" value="${data.spid}" maxlength="30"/>
                                <!--                   <span class="help-block">联通专有</span> -->
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">SMSP_SEND编号</label>
                            <div class="controls">
                                <u:select id="sendid" placeholder="请选择SMSP_SEND" value="${data.sendid}"
                                          sqlId="findAllSmspCompenentId"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">上行IP</label>
                            <div class="controls">
                                <input type="text" name="moip" value="${data.moip}" class="checkip"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">上行端口</label>
                            <div class="controls">
                                <input type="text" name="moport" value="${data.moport}"
                                       onfocus="inputControl.setNumber(this, 10, 0, false)"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">服务代码</label>
                            <div class="controls">
                                <input type="text" name="serviceid" value="${data.serviceid}" class="checkServiceId"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">通道发送速度</label>
                            <div class="controls">
                                <input type="text" name="speed" value="${data.speed}"
                                       onfocus="inputControl.setNumber(this, 10, 0, false)"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">路由速度</label>
                            <div class="controls">
                                <input type="text" name="access_speed" value="${data.access_speed}"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">通道类型</label>
                            <div class="controls">
                                <u:select id="channeltype"
                                          data="[{value:'0',text:'自签平台用户端口'},{value:'1',text:'固签无自扩展'},{value:'2',text:'固签有自扩展'},{value:'3',text:'自签通道用户端口'}]"
                                          value="${data.channeltype}" showAll="false"/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">是否支持170号段</label>
                            <div class="controls">
                                <u:select id="support_ohas" data="[{value:'0',text:'否'},{value:'1',text:'是'}]"
                                          value="${data.support_ohas}" showAll="false"/>
                            </div>
                        </div>
                        <div id="needprefix_div" class="control-group">
                            <label class="control-label">发送号码加"86"</label>
                            <div class="controls">
                                <u:select id="needprefix" data="[{value:'0',text:'否'},{value:'1',text:'是'}]"
                                          value="${data.needprefix}" showAll="false"/>
                                <span class="help-block red">* 仅限联通短信协议</span>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">白名单通道</label>
                            <div class="controls">
                                <u:select id="iswhitelist" data="[{value:'0',text:'否'}, {value:'1',text:'是'}]"
                                          value="${data.iswhitelist}" showAll="false"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">通道支持扩展位数</label>
                            <div class="controls">
                                <input type="text" name="extendsize" value="${data.extendsize}"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">通道生效时间范围</label>
                            <c:set var="sendtimearea_start_time" value="${fn:substringBefore(data.sendtimearea,'|')}"/>
                            <c:set var="sendtimearea_end_time" value="${fn:substringAfter(data.sendtimearea,'|')}"/>
                            <div class="controls">
                                <u:date id="sendtimearea_start_time" value="${sendtimearea_start_time}"
                                        placeholder="开始时间" maxId="sendtimearea_end_time,-1" dateFmt="HH:mm:ss"
                                        startDate="00:00:00"/>
                                <span>至</span>
                                <u:date id="sendtimearea_end_time" value="${sendtimearea_end_time}" placeholder="结束时间"
                                        minId="sendtimearea_start_time,1" dateFmt="HH:mm:ss"/>
                                <label id="sendtimearea_start_time-error" class="error" for="sendtimearea_start_time"
                                       style="display: none;"></label>
                                <label id="sendtimearea_end_time-error" class="error" for="sendtimearea_end_time"
                                       style="display: none;"></label>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">缓存队列告警阈值</label>
                            <div class="controls">
                                <input type="text" name="maxqueuesize"
                                       value="${empty data.maxqueuesize ? 10000 : data.maxqueuesize}"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">滑动窗口</label>
                            <div class="controls">
                                <input type="text" name="sliderwindow"
                                       value="${empty data.sliderwindow ? 16 : data.sliderwindow}"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">通道所属类型</label>
                            <div class="controls">
                                <u:select id="owner_type"
                                          data="[{value:'1',text:'自有'}, {value:'2',text:'直连'}, {value:'3',text:'第三方'}]"
                                          value="${data.owner_type}" showAll="false"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">发送短信类型</label>
                            <div class="controls">
                                <u:select id="industrytype" data="[{value:'0',text:'行业'}, {value:'1',text:'营销'}]"
                                          value="${data.industrytype}" showAll="false"/>
                                <span class="help-block ">行业短信包括验证码和通知，营销短信只包括营销</span>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">单条内容发送长度</label>
                            <div class="controls">
                                <input type="text" name="contentlen"
                                       value="${empty data.contentlen ? 600 : data.contentlen}"/>
                            </div>
                        </div>

                        <s:if test="data.id != null and data.state != 3">
                            <div class="control-group">
                                <label class="control-label">MQ_ID</label>
                                <div class="controls">
                                    <u:select id="mq_id" placeholder="请选择MQ_ID" value="${data.mq_id}"
                                              sqlId="findSendIoMqId"/>
                                </div>
                            </div>
                        </s:if>

                        <%-- <div class="control-group">
                             <label class="control-label">通道状态</label>
                           <div class="controls">
                             <u:select id="state" data="[{value:'0',text:'关闭'}, {value:'1',text:'开启'}, {value:'2',text:'暂停'},{value:'3',text:'下架'}]" value="${data.state}" showAll="false"/>
                             <span class="help-block ">关闭－路由不再选择此通道，已路由到此通道的数据需要做异常处理</span>
                             <span class="help-block ">开启－可路由到此通道，通道数据正常发送</span>
                             <span class="help-block ">暂停－重启通道时使用，暂时不路由此通道，已路由到此通道的数据保留</span>
                           </div>
                        </div> --%>

                        <div class="control-group">
                            <label class="control-label">号段类型</label>
                            <div class="controls">
                                <u:select id="segcode_type" data="[{value:'0',text:'全国'}, {value:'1',text:'省网'}]"
                                          value="${data.segcode_type}" showAll="false" onChange="segCodeTypeOnchange"/>
                            </div>
                        </div>

                        <div id="segcodeDiv" class="control-group">
                            <label class="control-label">路由省网</label>
                            <div class="controls">
                                <u:select id="segcode" placeholder="请选择路由省网" value="${data.segcode}"
                                          sqlId="findAllSegCode"/>
                            </div>
                        </div>

                        <div id="segcodeDiv" class="control-group">
                            <label class="control-label">归属商务</label>
                            <div class="controls">
                                <u:select id="belong_business" placeholder="请选择归属商务" value="${data.belong_business}"
                                          sqlId="findBusiness"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">是否支持群发</label>
                            <div class="controls">
                                <u:select id="cluster_type"
                                          data="[{value:'0',text:'不支持'}, {value:'1',text:'支持多号码单内容'}, {value:'2',text:'支持多号码多内容'}]"
                                          value="${data.cluster_type}" showAll="false"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">群发组包支持最大条数</label>
                            <div class="controls">
                                <s:if test="data.cluster_max_number == null">
                                    <input type="text" name="cluster_max_number" value="100" maxlength="10"/>
                                </s:if>
                                <s:else>
                                    <input type="text" name="cluster_max_number" value="${data.cluster_max_number}"
                                           maxlength="10"/>
                                </s:else>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">群发组包最长等待时间</label>
                            <div class="controls">
                                <s:if test="data.cluster_max_time == null">
                                    <input type="text" name="cluster_max_time" value="1000" maxlength="10"/>
                                </s:if>
                                <s:else>
                                    <input type="text" name="cluster_max_time" value="${data.cluster_max_time}"
                                           maxlength="10"/>
                                </s:else>
                                <span class="help-block">单位毫秒</span>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">协议子类型</label>
                            <div class="controls">
                                <u:select id="protocol_type" data="[{value:'0',text:'全网'}, {value:'1',text:'cmpp福建省网'}]"
                                          value="${data.protocol_type}" showAll="false"/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">调用库名</label>
                            <div class="controls">
                                <input type="text" name="lib_name" value="${data.lib_name}" maxlength="20"/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">连接节点数</label>
                            <div class="controls">
                                <input type="text" id="nodenum" name="nodenum"
                                       value="${data.nodenum==null?1:data.nodenum}"/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">上行前缀</label>
                            <div class="controls">
                                <input type="text" id="moprefix" name="moprefix" class="moprefix"
                                       value="${data.moprefix}"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">备注</label>
                            <div class="controls">
                                <input type="text" name="remark" value="${data.remark}" maxlength="50"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label">&nbsp;</label>
                            <div class="controls">
                                <span id="msg" class="error" style="display:none;"></span>
                            </div>
                        </div>
                        <div class="form-actions">
                            <input type="hidden" name="id" value="${data.id}">
                            <input type="button" onclick="saveConfirm(this);" value="保  存" class="btn btn-success">
                            <input type="button" value="取 消" class="btn btn-error" onclick="back()"/>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">
    var validate;
    $(function () {
	var flag = '${data.flag}';
	var max_identify = parseInt('${data.max_identify}');
	var opt = '<option>0</option>';
	for (var i = 1;i <= max_identify;i++){
		opt += "<option value='"+i+"'>"+i+"</otpion>";
	}
	$("#identify").html(opt);
	
	var identify = '${data.identify}';
	$("#identify option[value = "+identify+"]").attr("selected","selected");
	if(flag == 'update'){
		$("#identify").attr("disabled",true);
	}

        var exvalue1 = '${data.exvalue}'; //用el表达式获取在控制层存放的复选框的值为字符串类型
        if (exvalue1 == null || exvalue1 == '') {
            //  $("input[name='exvalue']").prop("checked","true");
            $(".cn").prop('checked', true);
            $(".en").prop('checked', true);
        } else if (exvalue1 == 1) {
            console.log(1);
            $('.cn').prop('checked', true);
        } else if (exvalue1 == 2) {
            console.log(2);
            $(".en").prop('checked', true);
        } else if (exvalue1 == 3) {
            console.log(3);
            $(".cn").prop('checked', true);
            $(".en").prop('checked', true);
        } else if (exvalue1 == 5) {
            console.log(5);
            $(".cn").prop('checked', true);
            $(".mq").prop('checked', true);
        } else if (exvalue1 == 6) {
            console.log(6);
            $(".en").prop('checked', true);
            $(".mq").prop('checked', true);
        } else if (exvalue1 == 7) {
            $(".cn").prop('checked', true);
            $(".en").prop('checked', true);
            $(".mq").prop('checked', true);
        }


        $(function () {//页面加载的时候触发

       // $("[name = exvalue]:checkbox").attr("checked", true);
            //  var boxObj = $("[name = exvalue]:checkbox");  //获取所有的复选框


        })
        $.validator.defaults.ignore = "";
        //表单验证规则
        validate = $("#mainForm").validate({
            rules: {
                cid: {
                    required: true,
                    digits: true,
                    max: 9999,
                    min: 1
                },
                maxwords: {
                    required: true,
                    digits: true,
                    max: 1000
                },
                exvalue: "required",
                operatorstype: "required",
                channelname: "required",
                returnvalue: "required",
                url: "required",
                querystateurl: "required",
                cache_report: "required",
                costprice: {
                    required: true,
                    range: [0, 1],
                    number: true
                },
                warn_succ_rate: {
                    required: true,
                    digits: true,
                    range: [0, 100]
                },
                warn_time_rate: {
                    required: true,
                    digits: true,
                    range: [0, 100]
                },
                limit_of_sigle_number: {
                    digits: true,
                    max: 999999999,
                    min: 0
                },
                speed: {
                    required: true,
                    digits: true,
                    range: [1, 5000]
                },
                extendsize: {
                    required: true,
                    digits: true,
                    range: [0, 20]
                },
                sendtimearea_start_time: "required",
                sendtimearea_end_time: "required",
                maxqueuesize: {
                    required: true,
                    digits: true,
                    range: [1, 1000000000]
                },
                nodenum: {
                    required: true,
                    digits: true,
                    range: [1, 60]
                },
                sliderwindow: {
                    required: true,
                    digits: true,
                    range: [1, 10000]
                },
                access_speed: {
                    required: true,
                    digits: true,
                    range: [0, 2000]
                },
                oauth_url: {
                    maxlength: 250
                },
                oauth_post_data: {
                    maxlength: 500
                },
                add_temp_url: {
                    maxlength: 500
                },
                add_temp_post_data: {
                    maxlength: 500
                },
                get_temp_list_url: {
                    maxlength: 500
                },
                get_temp_list_post_data: {
                    maxlength: 500
                },
                contentlen: {
                    digits: true,
                    range: [1, 600]
                },
                belong_business: {
                    required: true
                },
                segcode: {
                    checkSegCode: true
                },
                cluster_max_number: {
                    digits: true,
                    range: [0, 2147483647]
                },
                cluster_max_time: {
                    digits: true,
                    range: [0, 2147483647]
                },
                moprefix: {
                    maxlength: 21
                }
            },
            messages: {
                cid: {
                    required: "请输入通道号",
                    digits: "请按规则输入通道号",
                    max: "请按规则输入通道号",
                    min: "请按规则输入通道号"
                },
                maxwords: {
                    required: "请输入短信长度",
                    digits: "请正确输入短信长度",
                    max: "短信长度不能超过{0}",
                },
                exvalue: "请至少选择一项语言",
                channelname: "请输入通道名称",
                returnvalue: "请输入返回值",
                url: "请输入接口地址",
                querystateurl: "请输入查询接口地址",
                cache_report: "请选择是否缓存状态报告到MQ队列",
                costprice: {
                    required: "请输入成本价",
                    number: "请输入正确的成本价"
                },
                warn_succ_rate: {
                    required: "指定通道成功率预警阈值",
                    digits: "值只能为[0,100]之间的整数",
                    range: "请输入[0,100]之间的整数"
                },
                warn_succ_rate: {
                    required: "指定通道及时率预警阈值",
                    digits: "值只能为[0,100]之间的整数",
                    range: "请输入[0,100]之间的整数"
                },
                nodenum: {
                    digits: "值只能为[1,60]之间的整数",
                    max: "值只能为[1,60]之间的整数",
                    min: "值只能为[1,60]之间的整数"
                },
                limit_of_sigle_number: {
                    digits: "值只能为[0,9999999999]之间的整数",
                    max: "值只能为[0,9999999999]之间的整数",
                    min: "值只能为[0,9999999999]之间的整数"
                },
                extendsize: {
                    range: "请输入[0,20]之间的整数"
                },
                sendtimearea_start_time: "请选择时段开始时间",
                sendtimearea_end_time: "请选择时段结束时间",
                moprefix: {
                    maxlength: "上行前缀长度不能超过21个字符"
                }
            }
        });


        jQuery.validator.addMethod("checkip",
            function (value, element) {
                var reg = /^(((1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|[1-9])\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)\.(1\d{2}|2[0-4]\d|25[0-5]|[1-9]\d|\d)))$/;
                ;
                return this.optional(element) || (reg.test(value));
            }, "请输入合法的ip地址");

        jQuery.validator.addMethod("checkServiceId",
            function (value, element) {
                var reg = /^[a-zA-Z0-9]{1,10}$/;
                return this.optional(element) || (reg.test(value));
            }, "请输入1-10位的服务代码或者协议类型");


        jQuery.validator.addMethod("checkSegCode",
            function (value, element) {
                var segcodeType = $("#segcode_type").val();
                if (segcodeType == 1 && value == "") {
                    return false;
                }

                return true;
            }, "请选择路由省网");

        jQuery.validator.addMethod("moprefix",
            function (value, element) {
                var reg = /^\d{0,22}$/;
                ;
                return this.optional(element) || (reg.test(value));
            }, "上行前缀只能是数字");

        // MQ_ID 不能修改
        $("#mq_id").attr("disabled", true);
    });

    function segCodeTypeOnchange(value, text) {
        if (value == 0) {
            $("#segcodeDiv").hide();
        } else {
            $("#segcodeDiv").show();
        }
    }

    function httpmodeChange(value, text) {
        if (value == 5) {// 联通短信协议
            $("#needprefix_div").show();
        } else {
            $("#needprefix").val(0);
            $("#needprefix_div").hide();
        }
    }

    function saveConfirm(btn) {
        $("#exvalue-error").css("display", "none");
        $("#msg").hide();
        if (!validate.form()) {
            return;
        }
        console.log("进来判断前")
        if ($(".cn").is(':checked') == false && $(".en").is(':checked') == false) {
            console.log("进来判断")
            $("#exvalue-error").text("至少选择一种通道支持语言！");
            $("#exvalue-error").css("display", "block");
            return;
        }
        var isCreate = $("input[name='id']").val() == '' ? true : false;
        if (isCreate) {
            var channelId = $("input[name='cid']").val();
            layer.confirm('确认是否使用此通道号？', function (index) {
                layer.close(index);
                ajaxSubmit(btn);
            }, function (index) {
                layer.close(index);
            });
        } else {
            ajaxSubmit(btn);
        }

    }
    ;

    function ajaxSubmit(btn) {
        var options = {
            beforeSubmit: function () {
                $(btn).attr("disabled", true);
            },
            success: function (data) {
                $(btn).attr("disabled", false);

                if (data == null || data.result == null) {
                    $("#msg").text("服务器错误，请联系管理员").show();
                    return;
                }
                if (data.result == 'success') {
                    $("input[name='id']").val(data.id);
                }

                $("#msg").text(data.msg).show();
            },
            url: "${ctx}/channel/save",
            type: "post",
            timeout: 30000
        };
        $("#mainForm").ajaxSubmit(options);
    }
</script>
</body>
</html>