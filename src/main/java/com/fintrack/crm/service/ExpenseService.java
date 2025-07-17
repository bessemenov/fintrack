package com.fintrack.crm.service;

import com.fintrack.crm.entity.ExpenseEntity;
import com.fintrack.crm.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final WalletService walletService;

    public ExpenseService(ExpenseRepository expenseRepository, WalletService walletService) {
        this.expenseRepository = expenseRepository;
        this.walletService = walletService;
    }

    public ExpenseEntity addExpense(ExpenseEntity expense) {

        expense.setCreatedAt(LocalDateTime.now());

        ExpenseEntity savedExpense = expenseRepository.save(expense);

        walletService.decreaseBalance(expense.getUserId(), expense.getAmount());

        return savedExpense;
    }

    public List<ExpenseEntity> getExpensesByUserId(Long userId) {
        return expenseRepository.findByUserId(userId);
    }
}
