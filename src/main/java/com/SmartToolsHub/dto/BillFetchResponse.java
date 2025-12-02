package com.SmartToolsHub.dto;

public class BillFetchResponse {
    private boolean success;
    private String billId;
    private Integer dueAmount;
    private String dueDate;
    private String message;
    // getters/setters
    public boolean isSuccess(){return success;} public void setSuccess(boolean s){this.success=s;}
    public String getBillId(){return billId;} public void setBillId(String b){this.billId=b;}
    public Integer getDueAmount(){return dueAmount;} public void setDueAmount(Integer a){this.dueAmount=a;}
    public String getDueDate(){return dueDate;} public void setDueDate(String d){this.dueDate=d;}
    public String getMessage(){return message;} public void setMessage(String m){this.message=m;}
}
