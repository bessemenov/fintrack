package com.fintrack.crm.service;

import com.fintrack.crm.dto.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {
    void registerUser(String email, String username, String password, String verificationCode, LocalDateTime expirationTime, String phoneNumber);
    String login(String email, String password);
    void verifyUser(String email, String verificationCode);
    void sendPasswordResetCode(String email);
    void resetPassword(String email, String verificationCode, String newPassword);
    List<UserResponseDTO> getAllUsers();
}

