package com.fintrack.crm.controller;

import com.fintrack.crm.dto.RegisterRequest;
import com.fintrack.crm.dto.UserResponseDTO;
import com.fintrack.crm.dto.VerifyRegisterRequest;
import com.fintrack.crm.service.EmailService;
import com.fintrack.crm.service.UserService;
import com.fintrack.crm.dto.LoginRequest;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final EmailService emailService;
    private final UserService userService;

    public UserController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest request) {
        logger.info("Register isteği alındı - Email: {}", request.getEmail());

        try {

            String verificationCode = String.valueOf(new Random().nextInt(900000) + 100000);
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

            logger.info("Üretilen Doğrulama Kodu: {} | Süresi: {}", verificationCode, expirationTime);

            userService.registerUser(
                    request.getEmail(),
                    request.getUsername(),
                    request.getPassword(),
                    verificationCode,
                    expirationTime,
                    request.getPhoneNumber()
            );


            emailService.sendVerificationCode(request.getEmail(), verificationCode, expirationTime);

            logger.info("Kayıt ve doğrulama kodu işlemi tamamlandı - Email: {}", request.getEmail());
            return ResponseEntity.ok("Doğrulama kodu e-mail adresinize gönderildi.");
        } catch (Exception e) {
            logger.error("Kayıt sırasında hata oluştu - Email: {}, Hata: {}", request.getEmail(), e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Kayıt sırasında bir hata oluştu: " + e.getMessage());
        }
    }


    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody VerifyRegisterRequest request) {
        logger.info("📩 Kullanıcı doğrulama isteği alındı - Email: {}, Kod: {}", request.getEmail(), request.getVerificationCode());

        try {
            userService.verifyUser(request.getEmail(), request.getVerificationCode());
            logger.info("Kullanıcı başarıyla doğrulandı - Email: {}", request.getEmail());
            return ResponseEntity.ok("Kullanıcı başarıyla doğrulandı.");
        } catch (Exception e) {
            logger.error("Doğrulama hatası - Email: {}, Hata: {}", request.getEmail(), e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Doğrulama hatası: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Giriş isteği - Email: {}", request.getEmail());

        try {
            String token = userService.login(request.getEmail(), request.getPassword());
            logger.info("Giriş başarılı - Email: {}", request.getEmail());
            return ResponseEntity.ok("Bearer " + token);
        } catch (Exception e) {
            logger.error("Giriş hatası - Email: {}, Hata: {}", request.getEmail(), e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Giriş hatası: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        logger.info("Test endpoint çağrıldı");
        return ResponseEntity.ok("çalışıyor");
    }

    @GetMapping("/details")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        logger.info("Kullanıcı detayları istendi");
        return ResponseEntity.ok(userService.getAllUsers());
    }
}








