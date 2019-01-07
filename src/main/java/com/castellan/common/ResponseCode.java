package com.castellan.common;

public enum ResponseCode {

    SUCCESS(0,"SUCCESS"),
    NEED_LOGIN(10,"NEED_LOGIN"),
    ERROR(100,"ERROR"),
    ILLEGAL_PARAMETER(2,"ILLEGAL_PARAMETER");


    private final int code;

    private final String desc;

    ResponseCode(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


}
