package com.fintrack.crm.controller;

import com.fintrack.crm.dto.ExpenseRequest;
import com.fintrack.crm.entity.ExpenseEntity;
import com.fintrack.crm.entity.UserEntity;
import com.fintrack.crm.service.IExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final IExpenseService expenseService;

    public ExpenseController(IExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseEntity> addExpense(@RequestBody ExpenseRequest request,
                                                    @AuthenticationPrincipal UserEntity user) {
        ExpenseEntity saved = expenseService.addExpenseFromRequest(request, user.getId());
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseEntity>> getExpenses(@AuthenticationPrincipal UserEntity user) {
        List<ExpenseEntity> expenses = expenseService.getExpensesByUserId(user.getId());
        return ResponseEntity.ok(expenses);
    }
}

