package com.fintrack.crm.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String toEmail, String code, LocalDateTime expirationTime) {
        logger.info("Doğrulama kodu gönderme işlemi başladı - Email: {}, Kod: {}", toEmail, code);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("FinTrack Email Doğrulama Kodu");

            String formattedTime = expirationTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String body = "Doğrulama Kodunuz: " + code + "\n" +
                    "Bu kod " + formattedTime + " saatine kadar geçerlidir.";

            message.setText(body);
            mailSender.send(message);

            logger.info("Doğrulama kodu başarıyla gönderildi - Email: {}", toEmail);
        } catch (Exception e) {
            logger.error("Mail gönderme hatası - Email: {}, Hata: {}", toEmail, e.getMessage());
            throw new RuntimeException("Mail gönderme hatası: " + e.getMessage());
        }
    }
}



