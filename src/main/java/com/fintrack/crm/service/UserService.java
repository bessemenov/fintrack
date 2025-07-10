package com.fintrack.crm.service;

import com.fintrack.crm.entity.UserEntity;
import com.fintrack.crm.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Kullanıcıyı veritabanına kayıt
    public void registerUser(String email, String username, String password, String verificationCode) {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // Şifre hashlendi ✅
        user.setVerificationCode(verificationCode);
        user.setCodeSentAt(LocalDateTime.now());
        user.setEnabled(false);

        userRepository.save(user);
    }


    public void confirmUser(String email, String verificationCode) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Bu email adresine ait kullanıcı bulunamadı: " + email));

        if (user.getVerificationCode() == null || !user.getVerificationCode().equals(verificationCode)) {
            throw new RuntimeException("Kod hatalı veya eksik");
        }

        if (user.getCodeSentAt() == null || user.getCodeSentAt().plusMinutes(15).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Kod süresi dolmuş");
        }

        // Her şey doğruysa kullanıcıyı aktif hale getir
        user.setEnabled(true);
        userRepository.save(user);
    }

    //Login işlemi
    public void login(String email, String password) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Kullanıcı henüz doğrulanmamış");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Şifre hatalı");
        }

        // Giriş başarılı
    }
}






