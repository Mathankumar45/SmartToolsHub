package com.SmartToolsHub.dto;
public class PaymentRequest {
    private Integer amount; // rupees
    private String service;
    private String number;
    private String provider;
    private String planName;
 // Add this field to your PaymentRequest class
    private String email;

    // Add getter and setter
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    // getters/setters
    public Integer getAmount(){return amount;} public void setAmount(Integer a){this.amount=a;}
    public String getService(){return service;} public void setService(String s){this.service=s;}
    public String getNumber(){return number;} public void setNumber(String n){this.number=n;}
    public String getProvider(){return provider;} public void setProvider(String p){this.provider=p;}
    public String getPlanName(){return planName;} public void setPlanName(String p){this.planName=p;}
}
