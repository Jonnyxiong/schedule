package com.ucpaas.sms.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("ClientSuccessRateRealtime")
public class ClientSuccessRateRealtime {

	//
	private Long id;
	// 用户id
	private String clientId;
	// 用户名称
	private String clientName;
	// 发送总数
	private Integer sendTotal;
	// 明确成功 3
	private Integer reallySuccessTotal;
	// 成功待定 1
	private Integer fakeSuccessFail;
	// 计费条数 1+3+4+6
	private Integer charge1;
	// 计费条数 10
	private Integer charge2;
	// 明确失败 6
	private Integer reallyFailTotal;
	// 审核不通过 7
	private Integer auditFailTotal;
	// 提交失败 5
	private Integer submitFailTotal;
	// 拦截条数 8 9 10
	private Integer interceptTotal;
	// 未发送 0
	private Integer nosend;
	// 提交SEND失败 4
	private Integer sendFailToatl;
	// 发送到SEND总数 1 3 6
	private Integer sendAll;
	// 成功率
	private BigDecimal successRate;
	// 未知率
	private BigDecimal fakeSuccessRate;
	// 失败率
	private BigDecimal reallyFailRate;
	// 数据时间
	private Date dataTime;
	// 数据采集时间
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Integer getSendTotal() {
		return sendTotal;
	}

	public void setSendTotal(Integer sendTotal) {
		this.sendTotal = sendTotal;
	}

	public Integer getReallySuccessTotal() {
		return reallySuccessTotal;
	}

	public void setReallySuccessTotal(Integer reallySuccessTotal) {
		this.reallySuccessTotal = reallySuccessTotal;
	}

	public Integer getFakeSuccessFail() {
		return fakeSuccessFail;
	}

	public void setFakeSuccessFail(Integer fakeSuccessFail) {
		this.fakeSuccessFail = fakeSuccessFail;
	}

	public Integer getCharge1() {
		return charge1;
	}

	public void setCharge1(Integer charge1) {
		this.charge1 = charge1;
	}

	public Integer getCharge2() {
		return charge2;
	}

	public void setCharge2(Integer charge2) {
		this.charge2 = charge2;
	}

	public Integer getReallyFailTotal() {
		return reallyFailTotal;
	}

	public void setReallyFailTotal(Integer reallyFailTotal) {
		this.reallyFailTotal = reallyFailTotal;
	}

	public Integer getAuditFailTotal() {
		return auditFailTotal;
	}

	public void setAuditFailTotal(Integer auditFailTotal) {
		this.auditFailTotal = auditFailTotal;
	}

	public Integer getSubmitFailTotal() {
		return submitFailTotal;
	}

	public void setSubmitFailTotal(Integer submitFailTotal) {
		this.submitFailTotal = submitFailTotal;
	}

	public Integer getInterceptTotal() {
		return interceptTotal;
	}

	public void setInterceptTotal(Integer interceptTotal) {
		this.interceptTotal = interceptTotal;
	}

	public Integer getNosend() {
		return nosend;
	}

	public void setNosend(Integer nosend) {
		this.nosend = nosend;
	}

	public Integer getSendFailToatl() {
		return sendFailToatl;
	}

	public void setSendFailToatl(Integer sendFailToatl) {
		this.sendFailToatl = sendFailToatl;
	}

	public Integer getSendAll() {
		return sendAll;
	}

	public void setSendAll(Integer sendAll) {
		this.sendAll = sendAll;
	}

	public BigDecimal getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(BigDecimal successRate) {
		this.successRate = successRate;
	}

	public BigDecimal getFakeSuccessRate() {
		return fakeSuccessRate;
	}

	public void setFakeSuccessRate(BigDecimal fakeSuccessRate) {
		this.fakeSuccessRate = fakeSuccessRate;
	}

	public BigDecimal getReallyFailRate() {
		return reallyFailRate;
	}

	public void setReallyFailRate(BigDecimal reallyFailRate) {
		this.reallyFailRate = reallyFailRate;
	}

	public Date getDataTime() {
		return dataTime;
	}

	public void setDataTime(Date dataTime) {
		this.dataTime = dataTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}