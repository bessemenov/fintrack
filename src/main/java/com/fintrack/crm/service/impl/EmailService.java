package com.fintrack.crm.service.impl;

import com.fintrack.crm.service.IEmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService implements IEmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationCode(String toEmail, String code, LocalDateTime expirationTime) {
        logger.info("Verification code sending process has started - Email: {}, Kod: {}", toEmail, code);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("FinTrack Email Verification Code");

            String formattedTime = expirationTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String body = "Your Verification Code: " + code + "\n" +
                    "This code " + formattedTime + " valid until.";

            message.setText(body);
            mailSender.send(message);

            logger.info("Verification code sent successfully - Email: {}", toEmail);
        } catch (Exception e) {
            logger.error("Email sending error - Email: {}, Hata: {}", toEmail, e.getMessage());
            throw new RuntimeException("Email sending error: " + e.getMessage());
        }
    }
}



