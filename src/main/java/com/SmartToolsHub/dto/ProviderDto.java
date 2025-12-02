package com.SmartToolsHub.dto;
public class ProviderDto {
    private String code;
    private String name;
    public ProviderDto() {}
    public ProviderDto(String code, String name) { this.code = code; this.name = name; }
    public String getCode(){return code;} public void setCode(String c){this.code=c;}
    public String getName(){return name;} public void setName(String n){this.name=n;}
}
