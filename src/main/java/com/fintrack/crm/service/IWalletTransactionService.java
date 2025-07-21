package com.fintrack.crm.service;

import com.fintrack.crm.dto.ExpenseRequest;
import com.fintrack.crm.dto.IncomeRequest;
import com.fintrack.crm.entity.WalletTransactionEntity;

import java.util.List;

public interface IWalletTransactionService {
    void addIncomeToWallet(IncomeRequest request);
    void addExpenseToWallet(ExpenseRequest request);
    List<WalletTransactionEntity> getTransactionsByWalletId(Long walletId);
}
