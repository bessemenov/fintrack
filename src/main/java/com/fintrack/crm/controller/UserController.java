package com.fintrack.crm.controller;

import com.fintrack.crm.dto.RegisterRequest;
import com.fintrack.crm.dto.ConfirmRegisterRequest;
import com.fintrack.crm.service.EmailService;
import com.fintrack.crm.service.UserService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fintrack.crm.dto.LoginRequest;

import java.util.Random;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final EmailService emailService;
    private final UserService userService;

    public UserController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    //Kayıt (Register) endpoint
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest request) {
        // Kod üret
        String verificationCode = String.valueOf(new Random().nextInt(900000) + 100000);

        //Servise gönder (DB'ye kaydet)
        userService.registerUser(
                request.getEmail(),
                request.getUsername(),
                request.getPassword(),
                verificationCode
        );

        //Mail gönder
        emailService.sendVerificationCode(request.getEmail(), verificationCode);

        return ResponseEntity.ok("Verification code sent!");
    }

    //Email + kod doğrulama endpoint
    @PostMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestBody ConfirmRegisterRequest request) {
        userService.confirmUser(request.getEmail(), request.getVerificationCode());
        return ResponseEntity.ok("Kullanıcı kaydedildi!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok("Giriş başarılı!");
    }

    //Test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("çalışıyor");
    }
}






