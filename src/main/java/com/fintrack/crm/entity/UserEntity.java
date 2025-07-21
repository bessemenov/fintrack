package com.fintrack.crm.entity;

import com.fintrack.crm.enums.UserVerificationStatus;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserVerificationStatus status;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserVerificationEntity> verifications = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserVerificationStatus getStatus() {
        return status;
    }

    public void setStatus(UserVerificationStatus status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<UserVerificationEntity> getVerifications() {
        return verifications;
    }

    public void addVerification(UserVerificationEntity verification) {
        verifications.add(verification);
        verification.setUser(this);
    }

    public void removeVerification(UserVerificationEntity verification) {
        verifications.remove(verification);
        verification.setUser(null);
    }
}





