package com.castellan.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 *  一个高服用的返回对象，用于接收处理接口的返回。
 * @param <T>
 */

// 目的是在序列化的时候如果有null，该key不会序列化
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL )
public class ServerResponse <T> implements Serializable {

    private int status;

    private T data;

    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    private ServerResponse() {
    }

    public ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status, T data, String msg) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }


    public static ServerResponse createBySuccess(){
        return new ServerResponse(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse createBySuccess(T data){
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse createBySuccess(T data, String msg){
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), data, msg);
    }


    public static ServerResponse createByError(){
        return new ServerResponse(ResponseCode.ERROR.getCode());
    }

    public static ServerResponse createByErrorMessage(String msg){
        return new ServerResponse(ResponseCode.ERROR.getCode(), msg);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }

    // 序列化的时候忽略
    @JsonIgnore
    public boolean isSuccess(){
        return this.getStatus() == ResponseCode.SUCCESS.getCode();
    }

}
