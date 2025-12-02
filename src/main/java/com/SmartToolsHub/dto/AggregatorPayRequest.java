package com.SmartToolsHub.dto;

public class AggregatorPayRequest {
    private String service;
    private String provider;
    private String accountNumber;
    private Integer amount;
    private String extra; // optional payload e.g. billId, planId
    // getters/setters
    public String getService(){return service;} public void setService(String s){this.service=s;}
    public String getProvider(){return provider;} public void setProvider(String p){this.provider=p;}
    public String getAccountNumber(){return accountNumber;} public void setAccountNumber(String a){this.accountNumber=a;}
    public Integer getAmount(){return amount;} public void setAmount(Integer a){this.amount=a;}
    public String getExtra(){return extra;} public void setExtra(String e){this.extra=e;}
}
