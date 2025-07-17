package com.fintrack.crm.service;

import com.fintrack.crm.dto.ExpenseRequest;
import com.fintrack.crm.dto.IncomeRequest;
import com.fintrack.crm.entity.*;
import com.fintrack.crm.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletRepository walletRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    public WalletTransactionService(WalletTransactionRepository walletTransactionRepository,
                                    WalletRepository walletRepository,
                                    IncomeRepository incomeRepository,
                                    ExpenseRepository expenseRepository) {
        this.walletTransactionRepository = walletTransactionRepository;
        this.walletRepository = walletRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
    }

    @Transactional
    public void addIncomeToWallet(IncomeRequest request) {
        IncomeEntity income = incomeRepository.findById(request.getIncomeId())
                .orElseThrow(() -> new RuntimeException("Gelir bulunamadı"));

        WalletEntity wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new RuntimeException("Cüzdan bulunamadı"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime userDate = request.getTransactionDate() != null ? request.getTransactionDate() : now;

        WalletTransactionEntity transaction = new WalletTransactionEntity();
        transaction.setWallet(wallet);
        transaction.setIncome(income);
        transaction.setCreatedAt(now);
        transaction.setTransactionDate(userDate);

        walletTransactionRepository.save(transaction);

        wallet.setBalance(wallet.getBalance().add(income.getAmount()));
        walletRepository.save(wallet);
    }

    @Transactional
    public void addExpenseToWallet(ExpenseRequest request) {
        ExpenseEntity expense = expenseRepository.findById(request.getExpenseId())
                .orElseThrow(() -> new RuntimeException("Gider bulunamadı"));

        WalletEntity wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new RuntimeException("Cüzdan bulunamadı"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime userDate = request.getTransactionDate() != null ? request.getTransactionDate() : now;

        WalletTransactionEntity transaction = new WalletTransactionEntity();
        transaction.setWallet(wallet);
        transaction.setExpense(expense);
        transaction.setCreatedAt(now);
        transaction.setTransactionDate(userDate);

        walletTransactionRepository.save(transaction);

        wallet.setBalance(wallet.getBalance().subtract(expense.getAmount()));
        walletRepository.save(wallet);
    }

    public List<WalletTransactionEntity> getTransactionsByWalletId(Long walletId) {
        return walletTransactionRepository.findByWalletId(walletId);
    }
}


