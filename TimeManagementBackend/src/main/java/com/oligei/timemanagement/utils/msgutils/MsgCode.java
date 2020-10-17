package com.oligei.timemanagement.utils.msgutils;

public enum MsgCode {

    SUCCESS(MsgConstant.SUCCESS, MsgConstant.SUCCESS_MESSAGE),
    WRONG_PASSWORD(MsgConstant.WRONG_PASSWORD, MsgConstant.WRONG_PASSWORD_MESSAGE),
    WRONG_CAPTCHA(MsgConstant.WRONG_CAPTCHA, MsgConstant.WRONG_CAPTCHA_MESSAGE),
    EXPIRED_CAPTCHA(MsgConstant.EXPIRED_CAPTCHA, MsgConstant.EXPIRED_CAPTCHA_MESSAGE),
    PHONE_NOT_FOUND(MsgConstant.PHONE_NOT_FOUND, MsgConstant.PHONE_NOT_FOUND_MESSAGE),
    PHONE_FOUND(MsgConstant.PHONE_FOUND, MsgConstant.PHONE_FOUND_MESSAGE),
    ILLEGAL_EMAIL(MsgConstant.ILLEGAL_EMAIL, MsgConstant.ILLEGAL_EMAIL_MESSAGE),
    ILLEGAL_PHONE(MsgConstant.ILLEGAL_PHONE, MsgConstant.ILLEGAL_PHONE_MESSAGE),
    SMS_SEND_FAILURE(MsgConstant.SMS_SEND_FAILURE, MsgConstant.SMS_SEND_FAILURE_MESSAGE),
    EMAIL_SEND_FAILURE(MsgConstant.EMAIL_SEND_FAILURE, MsgConstant.EMAIL_SEND_FAILURE_MESSAGE),
    NULL_ARGUMENT(MsgConstant.NULL_ARGUMENT, MsgConstant.NULL_ARGUMENT_MESSAGE);

    private int status;
    private String msg;
    private MsgCode(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {return status;}
    public String getMsg() {return msg;}
}
