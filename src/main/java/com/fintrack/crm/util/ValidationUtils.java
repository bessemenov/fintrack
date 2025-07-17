package com.fintrack.crm.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ValidationUtils {

    // ✅ Gelir veya gider miktarı negatif veya sıfır olamaz
    public static void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Tutar pozitif bir değer olmalıdır");
        }
    }

    // ✅ Herhangi bir field null olmasın diye kontrol
    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " alanı boş olamaz");
        }
    }

    // ✅ Tarih gelecekte olamaz
    public static void validateNotFutureDate(LocalDateTime date, String fieldName) {
        if (date != null && date.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(fieldName + " gelecekte olamaz");
        }
    }
}
