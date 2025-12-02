package com.SmartToolsHub.dto;

public class AggregatorPayResponse {
    private boolean success;
    private String aggregatorTxnId;
    private String message;
    // getters/setters
    public boolean isSuccess(){return success;} public void setSuccess(boolean s){this.success=s;}
    public String getAggregatorTxnId(){return aggregatorTxnId;} public void setAggregatorTxnId(String t){this.aggregatorTxnId=t;}
    public String getMessage(){return message;} public void setMessage(String m){this.message=m;}
}
