package com.fintrack.crm.service;

import com.fintrack.crm.entity.WalletEntity;
import java.math.BigDecimal;

public interface IWalletService {
    void increaseBalance(Long userId, BigDecimal amount);
    void decreaseBalance(Long userId, BigDecimal amount);
    WalletEntity getWallet(Long userId);
}
