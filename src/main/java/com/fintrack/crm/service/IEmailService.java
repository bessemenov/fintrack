package com.fintrack.crm.service;

import java.time.LocalDateTime;

public interface IEmailService {
    void sendVerificationCode(String toEmail, String code, LocalDateTime expirationTime);
}
