package com.SmartToolsHub.dto;
public class PaymentResponse {
    private String key;
    private String orderId;
    private Integer amount;
    private String transactionId;
    public PaymentResponse() {}
    public PaymentResponse(String key, String orderId, Integer amount, String transactionId){
        this.key=key; this.orderId=orderId; this.amount=amount; this.transactionId=transactionId;
    }
    public String getKey(){return key;} public String getOrderId(){return orderId;}
    public Integer getAmount(){return amount;} public String getTransactionId(){return transactionId;}
}
