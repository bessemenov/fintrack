package com.fintrack.crm.service.impl;

import com.fintrack.crm.entity.ExpenseEntity;
import com.fintrack.crm.repository.ExpenseRepository;
import com.fintrack.crm.service.IExpenseService;
import com.fintrack.crm.service.IWalletService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpenseService implements IExpenseService {

    private final ExpenseRepository expenseRepository;
    private final IWalletService walletService; // DI için interface

    public ExpenseService(ExpenseRepository expenseRepository, IWalletService walletService) {
        this.expenseRepository = expenseRepository;
        this.walletService = walletService;
    }

    @Override
    public ExpenseEntity addExpense(ExpenseEntity expense) {
        expense.setCreatedAt(LocalDateTime.now());
        ExpenseEntity savedExpense = expenseRepository.save(expense);
        walletService.decreaseBalance(expense.getUserId(), expense.getAmount());
        return savedExpense;
    }

    @Override
    public List<ExpenseEntity> getExpensesByUserId(Long userId) {
        return expenseRepository.findByUserId(userId);
    }
}
