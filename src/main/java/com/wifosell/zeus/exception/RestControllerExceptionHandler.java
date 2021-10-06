package com.wifosell.zeus.exception;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.exception.ValidationErrorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Locale;


@ControllerAdvice
public class RestControllerExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(AppException.class)
    @ResponseBody
    public ResponseEntity<GApiResponse> resolveAppException(AppException exception) {
        String message = exception.getMessage();
        return new ResponseEntity<>(GApiResponse.fail(exception.getErrorBody()), HttpStatus.OK);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<GApiResponse> resolveSQLIntegrityConstrainViolationException(SQLIntegrityConstraintViolationException exception) {
        String message = exception.getMessage();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        GApiResponse apiResponse = new GApiResponse();
        apiResponse.setSuccess(Boolean.FALSE);
        apiResponse.setMessage(message);
        return new ResponseEntity<>(apiResponse, status);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<GApiResponse> resolveDataIntegrityViolationException(DataIntegrityViolationException exception) {
        String message = exception.getMessage();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        GApiResponse apiResponse = new GApiResponse();
        apiResponse.setSuccess(Boolean.FALSE);
        apiResponse.setMessage("Data Integrity ERROR : " +  message);
        return new ResponseEntity<>(apiResponse, status);
    }

    @ExceptionHandler(ZeusGlobalException.class)
    @ResponseBody
    public ResponseEntity<GApiResponse> resolveException(ZeusGlobalException exception) {
        String message = exception.getMessage();
        HttpStatus status = exception.getStatus();

        GApiResponse apiResponse = new GApiResponse();

        apiResponse.setSuccess(Boolean.FALSE);
        apiResponse.setMessage(message);

        return new ResponseEntity<>(apiResponse, status);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<GApiResponse> handleBadCredentials(Exception ex) {
        GApiResponse apiResponse = new GApiResponse(Boolean.FALSE, ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<GApiResponse> resolveException(UnauthorizedException exception) {
        GApiResponse apiResponse = exception.getApiResponse();
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }


    /*
       Handle for org.springframework.security.access.AccessDeniedException in cases cannot have the permisison to this resource
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<GApiResponse> resolveException(org.springframework.security.access.AccessDeniedException exception) {
        GApiResponse apiResponse = new GApiResponse(Boolean.FALSE, exception.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }


    /*
       Handle for org.springframework.security.access.AccessDeniedException in case throw custom CustomAccessDeniedException
     */
    @ExceptionHandler(CustomAccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<GApiResponse> resolveException(CustomAccessDeniedException exception) {
        GApiResponse apiResponse = exception.getApiResponse();
        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<GApiResponse> resolveArgMismatch(MethodArgumentTypeMismatchException exception) {
        return ResponseEntity.ok(
                GApiResponse.fail(exception.getMessage())
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<GApiResponse> processValidationError(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        ValidationErrorDTO dto = new ValidationErrorDTO();

        for (FieldError fieldError : fieldErrors) {
//            dto.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
            String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
            dto.addFieldError(fieldError.getField(), localizedErrorMessage);
        }

        GApiResponse apiResponse = new GApiResponse(Boolean.FALSE, "Validation fail", dto);

        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    private String resolveLocalizedErrorMessage(FieldError fieldError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);

        //If the message was not found, return the most accurate field error code instead.
        //You can remove this check if you prefer to get the default error message.
        if (localizedErrorMessage.equals(fieldError.getDefaultMessage())) {
            String[] fieldErrorCodes = fieldError.getCodes();
            localizedErrorMessage = fieldErrorCodes[0];
        }

        return localizedErrorMessage;
    }

}