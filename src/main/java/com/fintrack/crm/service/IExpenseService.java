package com.fintrack.crm.service;

import com.fintrack.crm.entity.ExpenseEntity;
import java.util.List;

public interface IExpenseService {
    ExpenseEntity addExpense(ExpenseEntity expense);
    List<ExpenseEntity> getExpensesByUserId(Long userId);
}
