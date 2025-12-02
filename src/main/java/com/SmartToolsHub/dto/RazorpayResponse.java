package com.SmartToolsHub.dto;
public class RazorpayResponse {
    private String razorpay_payment_id;
    private String razorpay_order_id;
    private String razorpay_signature;
    public String getRazorpay_payment_id(){return razorpay_payment_id;} public void setRazorpay_payment_id(String v){this.razorpay_payment_id=v;}
    public String getRazorpay_order_id(){return razorpay_order_id;} public void setRazorpay_order_id(String v){this.razorpay_order_id=v;}
    public String getRazorpay_signature(){return razorpay_signature;} public void setRazorpay_signature(String v){this.razorpay_signature=v;}
}
