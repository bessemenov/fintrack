package com.fintrack.crm.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        logger.error("RuntimeException yakalandı: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Bir hata oluştu: " + ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        logger.warn("Validation hatası: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Geçersiz veri: " + errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        logger.error("Bilinmeyen hata yakalandı: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Beklenmeyen bir hata oluştu.");
    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        logger.warn("EmailAlreadyExistsException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Kayıt hatası: " + ex.getMessage());
    }
    @ExceptionHandler(InvalidVerificationCodeException.class)
    public ResponseEntity<String> handleInvalidCode(InvalidVerificationCodeException ex) {
        logger.warn("Kod hatası: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Kod geçersiz: " + ex.getMessage());
    }
    @ExceptionHandler(ExpiredVerificationCodeException.class)
    public ResponseEntity<String> handleExpiredCode(ExpiredVerificationCodeException ex) {
        logger.warn("Kod süresi dolmuş: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.GONE)
                .body("Kod süresi dolmuş: " + ex.getMessage());
    }
    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<String> handleLoginFailed(LoginFailedException ex) {
        logger.warn("Giriş hatası: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Giriş başarısız: " + ex.getMessage());
    }

    @ExceptionHandler(InvalidIncomeException.class)
    public ResponseEntity<String> handleInvalidIncome(InvalidIncomeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
