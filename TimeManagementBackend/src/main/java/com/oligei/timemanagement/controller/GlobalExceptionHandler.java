package com.oligei.timemanagement.controller;

import com.oligei.timemanagement.TimemanagementApplication;
import com.oligei.timemanagement.utils.msgutils.Msg;
import com.oligei.timemanagement.utils.msgutils.MsgCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(TimemanagementApplication.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public Msg<Boolean> handle(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> item : violations) {
            switch (item.getMessage()) {
                case "204":
                    return new Msg<>(MsgCode.ILLEGAL_EMAIL);
                case "205":
                    return new Msg<>(MsgCode.ILLEGAL_PHONE);
                case "221":
                    return new Msg<>(MsgCode.SHORT_PASSWORD);
                case "222":
                    return new Msg<>(MsgCode.WEAK_PASSWORD);
                default:
                    break;
            }
        }
        logger.error("ConstraintViolationException", e);
        return new Msg<>(MsgCode.WRONG_ARGUMENT);
    }

    @ExceptionHandler(NullPointerException.class)
    public Msg<Boolean> handle(NullPointerException e) {
        logger.error("NullPointerException", e);
        return new Msg<>(MsgCode.NULL_ARGUMENT);
    }
}
