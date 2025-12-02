package com.SmartToolsHub.dto;

public class BillFetchRequest {
    private String service;     // "electricity", "water", "gas", "broadband", etc.
    private String provider;    // provider code/name
    private String accountNumber;
    // getters/setters
    public String getService(){return service;} public void setService(String s){this.service=s;}
    public String getProvider(){return provider;} public void setProvider(String p){this.provider=p;}
    public String getAccountNumber(){return accountNumber;} public void setAccountNumber(String a){this.accountNumber=a;}
}