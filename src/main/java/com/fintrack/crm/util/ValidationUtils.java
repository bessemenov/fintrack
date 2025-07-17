package com.fintrack.crm.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ValidationUtils {

    public static void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Tutar pozitif bir değer olmalıdır");
        }
    }

    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " alanı boş olamaz");
        }
    }

    public static void validateNotFutureDate(LocalDateTime date, String fieldName) {
        if (date != null && date.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(fieldName + " gelecekte olamaz");
        }
    }
}
