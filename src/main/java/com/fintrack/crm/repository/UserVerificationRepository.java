package com.fintrack.crm.repository;

import com.fintrack.crm.entity.UserEntity;
import com.fintrack.crm.entity.UserVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerificationEntity, Long> {

    Optional<UserVerificationEntity> findByEmail(String email);

    Optional<UserVerificationEntity> findByVerificationCode(String code);

    Optional<UserVerificationEntity> findByUser(UserEntity user);

    @Query("SELECT u FROM UserVerificationEntity u WHERE u.verificationCodeExpiration < :now AND u.user.status = 'WAITING'")
    List<UserVerificationEntity> findExpiredUnverifiedUsers(@Param("now") LocalDateTime now);

}


