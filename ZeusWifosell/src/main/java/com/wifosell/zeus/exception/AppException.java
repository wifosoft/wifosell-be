package com.wifosell.zeus.exception;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.payload.GApiErrorBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AppException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    GApiErrorBody errorBody;

    public AppException(String message) {
        super(message);
        this.errorBody  = GApiErrorBody.makeErrorBody(EAppExceptionCode.UNEXPECTED_ERROR, message);
    }
    public AppException(GApiErrorBody errorBody) {
        super(errorBody.getErrorMessage());
        this.errorBody = errorBody;
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public GApiErrorBody getErrorBody() {
        return this.errorBody;
    }
}

