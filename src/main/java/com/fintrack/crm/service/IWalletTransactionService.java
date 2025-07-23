package com.fintrack.crm.service;

import com.fintrack.crm.dto.ExpenseRequest;
import com.fintrack.crm.dto.IncomeRequest;
import com.fintrack.crm.dto.WalletTransactionRequest;
import com.fintrack.crm.entity.WalletTransactionEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface IWalletTransactionService {
    void addIncomeToWallet(IncomeRequest request);
    void addExpenseToWallet(ExpenseRequest request);
    List<WalletTransactionEntity> getTransactionsByWalletId(Long walletId);

    void createTransaction(WalletTransactionRequest request, Long userId);
    List<WalletTransactionEntity> getFilteredTransactions(Long userId, LocalDateTime start, LocalDateTime end);

}
