package com.fintrack.crm.service;

import com.fintrack.crm.dto.UserResponseDTO;
import com.fintrack.crm.entity.UserEntity;
import com.fintrack.crm.entity.UserVerificationEntity;
import com.fintrack.crm.enums.UserStatus;
import com.fintrack.crm.exception.*;
import com.fintrack.crm.repository.UserRepository;
import com.fintrack.crm.repository.UserVerificationRepository;
import com.fintrack.crm.util.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserVerificationRepository userVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       UserVerificationRepository userVerificationRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userVerificationRepository = userVerificationRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void registerUser(String email, String username, String password, String verificationCode, LocalDateTime expirationTime, String phoneNumber) {

        logger.info("Gelen parametreler - verificationCode: {}, expirationTime: {}", verificationCode, expirationTime);

        if (userVerificationRepository.findByEmail(email).isPresent()) {
            logger.warn("[Register] Email zaten kayıtlı - Email: {}", email);
            throw new EmailAlreadyExistsException("Bu email adresi zaten kayıtlı");
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhoneNumber(phoneNumber);
        user.setStatus(UserStatus.WAITING);

        UserEntity savedUser = userRepository.save(user);

        UserVerificationEntity verification = new UserVerificationEntity();
        verification.setUser(savedUser); // Kaydedilmiş user'i kullan
        verification.setEmail(email);
        verification.setVerificationCode(verificationCode);
        verification.setVerificationCodeExpiration(expirationTime);
        verification.setCodeSentAt(LocalDateTime.now()); // Burada set et

        logger.info("BEFORE SAVE: verificationCode = {}, expirationTime = {}",
                verification.getVerificationCode(),
                verification.getVerificationCodeExpiration());

        UserVerificationEntity savedVerification = userVerificationRepository.save(verification);

        logger.info("AFTER SAVE: verificationCode = {}, expirationTime = {}",
                savedVerification.getVerificationCode(),
                savedVerification.getVerificationCodeExpiration());

        logger.info("[Register] Kullanıcı ve doğrulama başarıyla kaydedildi - Email: {}, Kod: {}", email, verificationCode);
    }

    @Transactional
    public void verifyUser(String email, String verificationCode) {
        logger.info("[Verify] Doğrulama süreci başladı - Email: {}", email);

        UserVerificationEntity verification = userVerificationRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("[Verify] Email bulunamadı - Email: {}", email);
                    return new UserNotFoundException("Bu email adresine ait kullanıcı bulunamadı: " + email);
                });

        if (verification.getVerificationCode() == null || !verification.getVerificationCode().equals(verificationCode)) {
            logger.warn("[Verify] Kod hatalı - Email: {}", email);
            throw new InvalidVerificationCodeException("Kod hatalı veya eksik");
        }

        if (verification.getVerificationCodeExpiration() == null || verification.getVerificationCodeExpiration().isBefore(LocalDateTime.now())) {
            logger.warn("[Verify] Kod süresi dolmuş - Email: {}", email);
            throw new ExpiredVerificationCodeException("Kod süresi dolmuş");
        }

        UserEntity user = verification.getUser();
        user.setStatus(UserStatus.DONE);
        userRepository.save(user);

        verification.setVerificationCode(null);
        verification.setVerificationCodeExpiration(null);
        userVerificationRepository.save(verification);

        logger.info("[Verify] Kullanıcı doğrulandı ve aktif hale getirildi - Email: {}", email);
    }

    public String login(String email, String password) {
        logger.info("[Login] Giriş işlemi başladı - Email: {}", email);

        UserVerificationEntity verification = userVerificationRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("❌ [Login] Email bulunamadı - Email: {}", email);
                    return new UserNotFoundException("Email ile ilişkilendirilmiş kullanıcı bulunamadı: " + email);
                });

        UserEntity user = verification.getUser();

        if (user.getStatus() != UserStatus.DONE) {
            logger.warn("[Login] Kullanıcı doğrulanmamış - Email: {}", email);
            throw new LoginFailedException("Kullanıcı henüz doğrulanmamış");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("[Login] Şifre hatalı - Email: {}", email);
            throw new LoginFailedException("Şifre hatalı");
        }

        logger.info("[Login] Giriş başarılı - Email: {}", email);
        return jwtUtil.generateToken(email);
    }

    public List<UserResponseDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        return users.stream()
                .map(user -> {
                    String email = user.getVerification() != null ? user.getVerification().getEmail() : null;
                    return new UserResponseDTO(
                            user.getId(),
                            email,
                            user.getUsername(),
                            user.getPhoneNumber(),
                            user.getStatus().name()
                    );
                })
                .collect(Collectors.toList());
    }
}












