package com.wifosell.zeus.exception;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;

public class AppExceptionErrorData <T>{
    private EAppExceptionCode appExceptionCode;
    private String error_message;
    private T error_data;
    public  AppExceptionErrorData(EAppExceptionCode code_error,String message ){
        this.appExceptionCode = code_error;
        this.error_message = message;
    }

}
