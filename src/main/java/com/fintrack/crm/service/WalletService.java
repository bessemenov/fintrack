package com.fintrack.crm.service;

import com.fintrack.crm.entity.WalletEntity;
import com.fintrack.crm.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public void increaseBalance(Long userId, BigDecimal amount) {
        WalletEntity wallet = walletRepository.findByUserId(userId).orElseGet(() -> {
            WalletEntity newWallet = new WalletEntity();
            newWallet.setUserId(userId);
            newWallet.setBalance(BigDecimal.ZERO);
            return newWallet;
        });

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }

    public WalletEntity getWallet(Long userId) {
        return walletRepository.findByUserId(userId).orElse(null);
    }

    public void decreaseBalance(Long userId, BigDecimal amount) {
        WalletEntity wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cüzdan bulunamadı"));

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
    }

}

