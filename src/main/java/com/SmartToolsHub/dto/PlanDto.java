package com.SmartToolsHub.dto;
public class PlanDto {
    private String name;
    private Integer amount;
    public PlanDto(){}
    public PlanDto(String name, Integer amount){this.name=name; this.amount=amount;}
    public String getName(){return name;} public void setName(String n){this.name=n;}
    public Integer getAmount(){return amount;} public void setAmount(Integer a){this.amount=a;}
}
