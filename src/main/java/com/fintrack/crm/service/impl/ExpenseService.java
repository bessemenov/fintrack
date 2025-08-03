package com.fintrack.crm.service.impl;

import com.fintrack.crm.dto.ExpenseRequest;
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
    private final IWalletService walletService;

    public ExpenseService(ExpenseRepository expenseRepository, IWalletService walletService) {
        this.expenseRepository = expenseRepository;
        this.walletService = walletService;
    }

    @Override
    public ExpenseEntity addExpense(ExpenseEntity expense) {
        ExpenseEntity savedExpense = expenseRepository.save(expense);
        walletService.decreaseBalance(expense.getWalletId(), expense.getAmount());
        return savedExpense;
    }

    @Override
    public ExpenseEntity addExpenseFromRequest(ExpenseRequest request, Long userId) {
        ExpenseEntity expense = new ExpenseEntity();
        expense.setUserId(userId);
        expense.setWalletId(request.getWalletId());
        expense.setAmount(request.getAmount());
        expense.setTagId(request.getTagId());
        expense.setTransactionDateTime(request.getTransactionDateTime());
        expense.setCreatedAt(LocalDateTime.now());
        expense.setDescription(request.getDescription());

        return addExpense(expense);
    }

    @Override
    public List<ExpenseEntity> getExpensesByUserId(Long userId) {
        return expenseRepository.findByUserId(userId);
    }
}

