package com.ucpaas.sms.model;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SendFailReturnPo implements Serializable{

    private String clientId;

    private List<FailReturn> failReturnList;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<FailReturn> getFailReturnList() {
        return failReturnList;
    }

    public void setFailReturnList(FailReturn failReturn) {
        if(failReturnList == null){
            failReturnList = new ArrayList<>();
        }
        failReturnList.add(failReturn);
    }

    public  class FailReturn implements Serializable {
        private String subId;
        private Integer id;

        public String getSubId() {
            return subId;
        }

        public void setSubId(String subId) {
            this.subId = subId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    public static void main(String[] args) throws IOException {

        String jsonStr = "{\"clientId\":\"b01593\",\"failReturnList\":[{\"subId\":\"2311111111199980012\",\"id\":\"3\"}]}";
        Gson gson = new Gson();
        SendFailReturnPo jsonObject1 = gson.fromJson(jsonStr, SendFailReturnPo.class);
        System.out.println(gson.toJson(jsonObject1.getFailReturnList()));
        List<FailReturn> failReturnList1 = jsonObject1.getFailReturnList();
        for (FailReturn failReturn : failReturnList1) {
            System.out.println(gson.toJson(failReturn));

        }

    }
}
