package com.fintrack.crm.service.impl;

import com.fintrack.crm.dto.UserResponseDTO;
import com.fintrack.crm.entity.UserEntity;
import com.fintrack.crm.entity.UserVerificationEntity;
import com.fintrack.crm.enums.UserVerificationStatus;
import com.fintrack.crm.repository.UserRepository;
import com.fintrack.crm.repository.UserVerificationRepository;
import com.fintrack.crm.service.impl.EmailService;
import com.fintrack.crm.service.UserService;
import com.fintrack.exception.BusinessException;
import com.fintrack.exception.enums.ErrorResultCode;
import com.fintrack.security.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserVerificationRepository userVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository,
                           UserVerificationRepository userVerificationRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil, EmailService emailService) {
        this.userRepository = userRepository;
        this.userVerificationRepository = userVerificationRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    @Override
    public void registerUser(String email, String username, String password, String verificationCode, LocalDateTime expirationTime, String phoneNumber) {

        logger.info("Incoming parameters - verificationCode: {}, expirationTime: {}", verificationCode, expirationTime);

        if (userVerificationRepository.findByEmail(email).isPresent()) {
            logger.warn("[Register] Email is already registered - Email: {}", email);
            throw new BusinessException(ErrorResultCode.EMAIL_ALREADY_EXISTS, "Email already exists.");
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhoneNumber(phoneNumber);
        user.setStatus(UserVerificationStatus.WAITING);

        UserEntity savedUser = userRepository.save(user);

        UserVerificationEntity verification = new UserVerificationEntity();
        verification.setUser(savedUser);
        verification.setEmail(email);
        verification.setVerificationCode(verificationCode);
        verification.setVerificationCodeExpiration(expirationTime);
        verification.setCodeSentAt(LocalDateTime.now());

        logger.info("BEFORE SAVE: verificationCode = {}, expirationTime = {}",
                verification.getVerificationCode(),
                verification.getVerificationCodeExpiration());

        UserVerificationEntity savedVerification = userVerificationRepository.save(verification);

        logger.info("AFTER SAVE: verificationCode = {}, expirationTime = {}",
                savedVerification.getVerificationCode(),
                savedVerification.getVerificationCodeExpiration());

        logger.info("[Register] User and verification successfully saved - Email: {}, Kod: {}", email, verificationCode);
    }

    @Transactional
    public void verifyUser(String email, String verificationCode) {
        logger.info("[Verify] Verification process has started - Email: {}", email);

        UserVerificationEntity verification = userVerificationRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("[Verify] Email not found - Email: {}", email);
                    throw new BusinessException(ErrorResultCode.USER_NOT_FOUND, "No user found for this email address: " + email);
                });

        if (verification.getVerificationCode() == null || !verification.getVerificationCode().equals(verificationCode)) {
            logger.warn("[Verify] The code is incorrect - Email: {}", email);
            throw new BusinessException(ErrorResultCode.INVALID_VERIFICATION_CODE, "Code is incorrect or missing");
        }

        if (verification.getVerificationCodeExpiration() == null || verification.getVerificationCodeExpiration().isBefore(LocalDateTime.now())) {
            logger.warn("[Verify] Code expired - Email: {}", email);
            throw new BusinessException(ErrorResultCode.EXPIRED_VERIFICATION_CODE, "Code expired.");
        }

        UserEntity user = verification.getUser();
        user.setStatus(UserVerificationStatus.DONE);
        userRepository.save(user);

        verification.setVerificationCode(null);
        verification.setVerificationCodeExpiration(null);
        userVerificationRepository.save(verification);

        logger.info("[Verify] User verified and activated - Email: {}", email);
    }

    public String login(String email, String password) {
        logger.info("[Login] Login process started - Email: {}", email);

        UserVerificationEntity verification = userVerificationRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("[Login] Email not found - Email: {}", email);
                    return new BusinessException(ErrorResultCode.USER_NOT_FOUND, "No user associated with email found.");
                });

        UserEntity user = verification.getUser();

        if (user.getStatus() != UserVerificationStatus.DONE) {
            logger.warn("[Login] User is not verified - Email: {}", email);
            throw new BusinessException(ErrorResultCode.USER_NOT_VERIFIED, "User not yet verified.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("[Login] Password is incorrect - Email: {}", email);
            throw new BusinessException(ErrorResultCode.INVALID_PASSWORD, "Password is incorrect.");
        }

        logger.info("[Login] Login successful- Email: {}", email);
        return jwtUtil.generateToken(email, user.getId());
    }

    public List<UserResponseDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        return users.stream()
                .map(user -> {
                    String email = null;

                    List<UserVerificationEntity> verifications = user.getVerifications();
                    if (verifications != null && !verifications.isEmpty()) {
                        UserVerificationEntity lastVerification = verifications.get(verifications.size() - 1);
                        email = lastVerification.getEmail();
                    }

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

    public void sendPasswordResetCode(String email) {
        UserVerificationEntity verification = userVerificationRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorResultCode.USER_NOT_FOUND, "No user found for this email address."));

        UserEntity user = verification.getUser();

        String code = String.valueOf(new Random().nextInt(900000) + 100000); // 6 haneli

        verification.setVerificationCode(code);
        verification.setVerificationCodeExpiration(LocalDateTime.now().plusMinutes(10));
        verification.setCodeSentAt(LocalDateTime.now());

        userVerificationRepository.save(verification);

        logger.info("[Şifre Sıfırlama] Kod gönderildi - Email: {}, Kod: {}", email, code);

        emailService.sendVerificationCode(email, code, verification.getVerificationCodeExpiration());
    }

    public void resetPassword(String email, String verificationCode, String newPassword) {
        UserVerificationEntity verification = userVerificationRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorResultCode.USER_NOT_FOUND, "User not found."));

        if (verification.getVerificationCode() == null ||
                !verification.getVerificationCode().equals(verificationCode)) {
            throw new BusinessException(ErrorResultCode.INVALID_VERIFICATION_CODE, "Code is incorrect or missing.");
        }

        if (verification.getVerificationCodeExpiration() == null ||
                verification.getVerificationCodeExpiration().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorResultCode.EXPIRED_VERIFICATION_CODE, "Code expired.");
        }

        UserEntity user = verification.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        verification.setVerificationCode(null);
        verification.setVerificationCodeExpiration(null);
        userVerificationRepository.save(verification);
    }

    @Scheduled(cron = "0 * * * * *")
    public void expireUnverifiedUsers() {
        List<UserVerificationEntity> expiredVerifications = userVerificationRepository.findExpiredUnverifiedUsers(LocalDateTime.now());

        for (UserVerificationEntity verification : expiredVerifications) {
            UserEntity user = verification.getUser();
            if (user.getStatus() == UserVerificationStatus.WAITING) {
                user.setStatus(UserVerificationStatus.EXPIRED);
                userRepository.save(user);
                logger.info("[Expire] User Expired - Email: {}", verification.getEmail());
            }
        }
    }
}














