package com.fintrack.crm.controller;

import com.fintrack.crm.dto.ExpenseRequest;
import com.fintrack.crm.dto.IncomeRequest;
import com.fintrack.crm.service.WalletTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet-transactions")
public class WalletTransactionController {

    private final WalletTransactionService walletTransactionService;

    public WalletTransactionController(WalletTransactionService walletTransactionService) {
        this.walletTransactionService = walletTransactionService;
    }

    // 💰 GELİR EKLEME
    @PostMapping("/income")
    public ResponseEntity<String> addIncomeToWallet(@RequestBody IncomeRequest request) {
        walletTransactionService.addIncomeToWallet(request);
        return ResponseEntity.ok("💸 Gelir başarıyla cüzdana eklendi");
    }

    // 💸 GİDER EKLEME
    @PostMapping("/expense")
    public ResponseEntity<String> addExpenseToWallet(@RequestBody ExpenseRequest request) {
        walletTransactionService.addExpenseToWallet(request);
        return ResponseEntity.ok("📉 Gider başarıyla cüzdandan düşüldü");
    }

    // 🧾 CÜZDANA AİT TÜM TRANSACTİONLARI GETİR
    @GetMapping("/{walletId}")
    public ResponseEntity<?> getTransactionsByWalletId(@PathVariable Long walletId) {
        return ResponseEntity.ok(walletTransactionService.getTransactionsByWalletId(walletId));
    }
}

