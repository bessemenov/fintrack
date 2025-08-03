package com.fintrack.crm.service;

import com.fintrack.crm.dto.ExpenseRequest;
import com.fintrack.crm.entity.ExpenseEntity;

import java.util.List;

public interface IExpenseService {
    ExpenseEntity addExpense(ExpenseEntity expense);
    ExpenseEntity addExpenseFromRequest(ExpenseRequest request, Long userId);
    List<ExpenseEntity> getExpensesByUserId(Long userId);
}
