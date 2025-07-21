package com.fintrack.exception;

import com.fintrack.exception.enums.ErrorResultCode;
import jakarta.validation.ValidationException;

public class BusinessException extends ValidationException {

    private ErrorResultCode errorCode;
    private String errorMessage;

    public BusinessException(ErrorResultCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorResultCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorResultCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
