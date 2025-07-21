package com.fintrack.crm.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_verifications")
public class UserVerificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false)
    private String email;

    @Column(name = "code_sent_at")
    private LocalDateTime codeSentAt;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_code_expiration")
    private LocalDateTime verificationCodeExpiration;

    public Long getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCodeSentAt() {
        return codeSentAt;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public LocalDateTime getVerificationCodeExpiration() {
        return verificationCodeExpiration;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCodeSentAt(LocalDateTime codeSentAt) {
        this.codeSentAt = codeSentAt;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public void setVerificationCodeExpiration(LocalDateTime verificationCodeExpiration) {
        this.verificationCodeExpiration = verificationCodeExpiration;
    }
}


