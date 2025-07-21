package com.fintrack.crm.service.impl;

import com.fintrack.crm.entity.WalletEntity;
import com.fintrack.crm.repository.WalletRepository;
import com.fintrack.crm.service.IWalletService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService implements IWalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
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

    @Override
    public void decreaseBalance(Long userId, BigDecimal amount) {
        WalletEntity wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found."));

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
    }

    @Override
    public WalletEntity getWallet(Long userId) {
        return walletRepository.findByUserId(userId).orElse(null);
    }
}


