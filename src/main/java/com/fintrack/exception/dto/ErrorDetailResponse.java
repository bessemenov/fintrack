package com.fintrack.exception.dto;

public class ErrorDetailResponse {

    private String errorCode;
    private String errorMessage;

    public ErrorDetailResponse() {
    }

    public ErrorDetailResponse(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
