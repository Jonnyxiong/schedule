package com.ucpaas.sms.entity.message;

import org.apache.ibatis.type.Alias;

import java.util.Date;

@Alias("TestAccount")
public class TestAccount {
    
    // 序号, 自增长
    private Integer id;
    // 用户账号(子账号),关联t_sms_account表中的clientid字段
    private String clientid;
    // 创建时间
    private Date createtime;
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id ;
    }
    
    public String getClientid() {
        return clientid;
    }
    
    public void setClientid(String clientid) {
        this.clientid = clientid ;
    }
    
    public Date getCreatetime() {
        return createtime;
    }
    
    public void setCreatetime(Date createtime) {
        this.createtime = createtime ;
    }
    
}