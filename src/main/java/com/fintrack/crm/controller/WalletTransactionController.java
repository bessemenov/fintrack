package com.fintrack.crm.controller;

import com.fintrack.crm.dto.ExpenseRequest;
import com.fintrack.crm.dto.GroupedTransactionResponse;
import com.fintrack.crm.dto.IncomeRequest;
import com.fintrack.crm.dto.WalletTransactionResponse;
import com.fintrack.security.service.UserDetailsImpl;
import com.fintrack.crm.service.impl.WalletTransactionService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/wallet-transactions")
public class WalletTransactionController {

    private final WalletTransactionService walletTransactionService;

    public WalletTransactionController(WalletTransactionService walletTransactionService) {
        this.walletTransactionService = walletTransactionService;
    }

    @GetMapping
    public ResponseEntity<List<WalletTransactionResponse>> getFilteredTransactions(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = false, name = "start") LocalDateTime startDateTime,
            @RequestParam(required = false, name = "end") LocalDateTime endDateTime) {

        Long userId = userDetails.getId();
        List<WalletTransactionResponse> results = walletTransactionService.getFilteredTransactions(userId, startDateTime, endDateTime);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/grouped")
    public ResponseEntity<List<GroupedTransactionResponse>> getGroupedTransactions(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = false, name = "start") LocalDateTime startDateTime,
            @RequestParam(required = false, name = "end") LocalDateTime endDateTime) {

        Long userId = userDetails.getId();
        List<GroupedTransactionResponse> results = walletTransactionService.getGroupedTransactions(userId, startDateTime, endDateTime);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/incomes")
    public ResponseEntity<?> addIncome(
            @RequestBody IncomeRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(403).body("Kullanıcı doğrulanamadı.");
        }

        Long userId = userDetails.getId();
        walletTransactionService.addIncomeFromRequest(request, userId);
        return ResponseEntity.ok("Gelir işlemi başarıyla eklendi.");
    }

    @PostMapping("/expenses")
    public ResponseEntity<?> addExpense(
            @RequestBody ExpenseRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(403).body("Kullanıcı doğrulanamadı.");
        }

        Long userId = userDetails.getId();
        walletTransactionService.addExpenseFromRequest(request, userId);
        return ResponseEntity.ok("Gider işlemi başarıyla eklendi.");
    }
}






