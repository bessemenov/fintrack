package com.fintrack.crm.controller;

import com.fintrack.crm.dto.ExpenseRequest;
import com.fintrack.crm.dto.IncomeRequest;
import com.fintrack.crm.dto.WalletTransactionRequest;
import com.fintrack.crm.service.impl.WalletTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/wallet-transactions")
public class WalletTransactionController {

    private final WalletTransactionService walletTransactionService;

    public WalletTransactionController(WalletTransactionService walletTransactionService) {
        this.walletTransactionService = walletTransactionService;
    }

    // ✅ 1. Income ekleme
    @PostMapping("/income")
    public ResponseEntity<?> addIncomeToWallet(@RequestBody IncomeRequest request) {
        walletTransactionService.addIncomeToWallet(request);
        return ResponseEntity.ok("Income successfully added to wallet.");
    }

    // ✅ 2. Expense çıkarma
    @PostMapping("/expense")
    public ResponseEntity<?> addExpenseToWallet(@RequestBody ExpenseRequest request) {
        walletTransactionService.addExpenseToWallet(request);
        return ResponseEntity.ok("The expense was successfully deducted from the wallet.");
    }

    // ✅ 3. Belirli bir wallet'ın tüm transaction'ları
    @GetMapping("/{walletId}")
    public ResponseEntity<?> getTransactionsByWalletId(@PathVariable Long walletId) {
        return ResponseEntity.ok(walletTransactionService.getTransactionsByWalletId(walletId));
    }

    // ✅ 4. Genel transaction ekleme
    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody WalletTransactionRequest request,
                                               @RequestHeader("X-USER-ID") Long userId) {
        walletTransactionService.createTransaction(request, userId);
        return ResponseEntity.ok("Transaction created successfully.");
    }

    // ✅ 5. Filtreli transaction listesi (start-end)
    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredTransactions(@RequestHeader("X-USER-ID") Long userId,
                                                     @RequestParam(required = false) LocalDateTime startDateTime,
                                                     @RequestParam(required = false) LocalDateTime endDateTime) {
        return ResponseEntity.ok(walletTransactionService.getFilteredTransactions(userId, startDateTime, endDateTime));
    }
    @GetMapping("/grouped")
    public ResponseEntity<?> getGroupedTransactions(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam(required = false) LocalDateTime startDateTime,
            @RequestParam(required = false) LocalDateTime endDateTime) {

        return ResponseEntity.ok(
                walletTransactionService.getFilteredTransactions(userId, startDateTime, endDateTime)
        );
    }

}



