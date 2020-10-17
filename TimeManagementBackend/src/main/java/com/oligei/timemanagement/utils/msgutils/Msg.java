package com.oligei.timemanagement.utils.msgutils;

public class Msg<T> {
    private int status;
    private String msg;
    private T data;

    public Msg(MsgCode msgCode) {
        this.status = msgCode.getStatus();
        this.msg = msgCode.getMsg();
    }
    public Msg(MsgCode msgCode, T data){
        this.status = msgCode.getStatus();
        this.msg = msgCode.getMsg();
        this.data = data;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}