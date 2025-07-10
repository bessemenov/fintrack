package com.fintrack.crm.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("FinTrack Email Doğrulama Kodu");
            message.setText("Doğrulama Kodunuz: " + code);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("An error occurred while sending the mail: " + e.getMessage());
        }
    }
}

