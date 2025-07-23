package com.fintrack.crm.service.impl;

import com.fintrack.crm.dto.ExpenseRequest;
import com.fintrack.crm.dto.IncomeRequest;
import com.fintrack.crm.dto.WalletTransactionRequest;
import com.fintrack.crm.entity.*;
import com.fintrack.crm.repository.*;
import com.fintrack.crm.service.IWalletTransactionService;
import com.fintrack.exception.BusinessException;
import com.fintrack.exception.enums.ErrorResultCode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletTransactionService implements IWalletTransactionService {

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

    @Override
    @Transactional
    public void addIncomeToWallet(IncomeRequest request) {
        IncomeEntity income = incomeRepository.findById(request.getIncomeId())
                .orElseThrow(() -> new RuntimeException("No income found."));

        WalletEntity wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new RuntimeException("Wallet not found."));

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

    @Override
    @Transactional
    public void addExpenseToWallet(ExpenseRequest request) {
        ExpenseEntity expense = expenseRepository.findById(request.getExpenseId())
                .orElseThrow(() -> new RuntimeException("Expense not found."));

        WalletEntity wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new RuntimeException("Wallet not found."));

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

    @Override
    public List<WalletTransactionEntity> getTransactionsByWalletId(Long walletId) {
        return walletTransactionRepository.findByWalletId(walletId);
    }

    @Override
    @Transactional
    public void createTransaction(WalletTransactionRequest request, Long userId) {

        if (request.getDescription() != null && request.getDescription().length() > 30) {
            throw new RuntimeException("Description cannot be longer than 30 characters.");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime txDate = request.getTransactionDateTime() != null
                ? request.getTransactionDateTime()
                : now;

        if (txDate.isBefore(now.minusMonths(1))) {
            throw new RuntimeException("Transaction date cannot be more than 1 month ago.");
        }

        WalletEntity wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new RuntimeException("Wallet not found."));

        WalletTransactionEntity transaction = new WalletTransactionEntity();
        transaction.setWallet(wallet);
        transaction.setTransactionDate(txDate);
        transaction.setCreatedAt(now);
        transaction.setDescription(request.getDescription());

        if (request.getIncomeId() != null) {
            IncomeEntity income = incomeRepository.findById(request.getIncomeId())
                    .orElseThrow(() -> new RuntimeException("Income not found."));
            transaction.setIncome(income);
            wallet.setBalance(wallet.getBalance().add(income.getAmount()));
        }

        if (request.getExpenseId() != null) {
            ExpenseEntity expense = expenseRepository.findById(request.getExpenseId())
                    .orElseThrow(() -> new RuntimeException("Expense not found."));
            transaction.setExpense(expense);
            wallet.setBalance(wallet.getBalance().subtract(expense.getAmount()));
        }

        if (request.getTagId() != null) {
            transaction.setTagId(request.getTagId());
        }

        walletTransactionRepository.save(transaction);
        walletRepository.save(wallet);
    }

    @Override
    public List<WalletTransactionEntity> getFilteredTransactions(Long userId, LocalDateTime start, LocalDateTime end) {

        WalletEntity wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BusinessException(ErrorResultCode.WALLET_NOT_FOUND, "Wallet not found for user: " + userId)


                );

        LocalDateTime now = LocalDateTime.now();

        if (end != null && end.isAfter(now)) {
            throw new BusinessException(ErrorResultCode.END_DATE_INVALID, "End date cannot be in the future.");


        }

        List<WalletTransactionEntity> transactions = walletTransactionRepository.findByWalletUserId(userId);

        return transactions.stream()
                .filter(tx -> {
                    LocalDateTime txDate = tx.getTransactionDate();
                    if (start != null && txDate.isBefore(start)) return false;
                    if (end != null && txDate.isAfter(end)) return false;
                    return true;
                })
                .toList();
    }
}




