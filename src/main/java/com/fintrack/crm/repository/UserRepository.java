package com.fintrack.crm.repository;

import com.fintrack.crm.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // Email alanı artık burada yok, tüm email işlemleri UserVerificationEntity üzerinden yapılacak.
    Optional<UserEntity> findByUsername(String username);
}

