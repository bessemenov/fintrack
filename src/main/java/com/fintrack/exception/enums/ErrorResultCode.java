package com.fintrack.exception.enums;

public enum ErrorResultCode {
    EMAIL_ALREADY_EXISTS("0001"),
    EMAIL_USER_RELATION_NOT_FOUND("0002"),
    YENI_HATA("0003"),
    INVALID_VERIFICATION_CODE("0004"),
    EXPIRED_VERIFICATION_CODE("0005"),
    USER_NOT_FOUND("0006"),
    USER_NOT_VERIFIED("0007"),
    INVALID_PASSWORD("0008"),
    BALANCE_CANNOT_BE_NULL("0009");



    private final String resultCode;

    ErrorResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultCode() {
        return this.resultCode;
    }
}
