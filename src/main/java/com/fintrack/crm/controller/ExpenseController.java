package com.fintrack.crm.controller;

import com.fintrack.crm.dto.ExpenseRequest;
import com.fintrack.crm.entity.ExpenseEntity;
import com.fintrack.crm.service.ExpenseService;
import com.fintrack.crm.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final JwtUtil jwtUtil;

    public ExpenseController(ExpenseService expenseService, JwtUtil jwtUtil) {
        this.expenseService = expenseService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<ExpenseEntity> addExpense(
            @RequestBody ExpenseRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Long currentUserId = jwtUtil.extractUserId(token);

        if (!currentUserId.equals(request.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ExpenseEntity expense = new ExpenseEntity();
        expense.setUserId(request.getUserId());
        expense.setExpenseType(request.getExpenseType());
        expense.setAmount(request.getAmount());
        expense.setPeriodType(request.getPeriodType());

        ExpenseEntity saved = expenseService.addExpense(expense);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ExpenseEntity>> getExpenses(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Long currentUserId = jwtUtil.extractUserId(token);

        if (!currentUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<ExpenseEntity> expenses = expenseService.getExpensesByUserId(userId);
        return ResponseEntity.ok(expenses);
    }
}
