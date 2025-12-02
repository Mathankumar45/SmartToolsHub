package com.SmartToolsHub.dto;
public class StatusResponse {
    private String status;
    private String transactionId;
    public StatusResponse() {}
    public StatusResponse(String status, String transactionId){ this.status=status; this.transactionId=transactionId; }
    public String getStatus(){return status;} public String getTransactionId(){return transactionId;}
}
