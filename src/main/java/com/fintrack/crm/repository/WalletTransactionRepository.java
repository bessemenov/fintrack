package com.fintrack.crm.repository;

import com.fintrack.crm.entity.WalletTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransactionEntity, Long> {
    List<WalletTransactionEntity> findByWalletId(Long walletId);

    List<WalletTransactionEntity> findByWalletUserId(Long userId);
}

