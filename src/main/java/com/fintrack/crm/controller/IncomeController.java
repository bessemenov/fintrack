package com.fintrack.crm.controller;

import com.fintrack.crm.dto.IncomeRequest;
import com.fintrack.crm.entity.IncomeEntity;
import com.fintrack.crm.entity.WalletEntity;
import com.fintrack.crm.service.IncomeService;
import com.fintrack.crm.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;
    private final WalletService walletService;

    public IncomeController(IncomeService incomeService, WalletService walletService) {
        this.incomeService = incomeService;
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<IncomeEntity> addIncome(@RequestBody IncomeRequest request) {
        IncomeEntity income = new IncomeEntity();
        income.setUserId(request.getUserId());
        income.setIncomeType(request.getIncomeType());
        income.setPeriodType(request.getPeriodType());
        income.setAmount(request.getAmount());
        income.setCreatedAt(LocalDateTime.now());

        IncomeEntity saved = incomeService.addIncome(income);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<IncomeEntity>> getIncomes(@PathVariable Long userId) {
        List<IncomeEntity> incomes = incomeService.getIncomesByUserId(userId);
        return ResponseEntity.ok(incomes);
    }

    @GetMapping("/wallet/{userId}")
    public ResponseEntity<WalletEntity> getWallet(@PathVariable Long userId) {
        WalletEntity wallet = walletService.getWallet(userId);
        return wallet != null ? ResponseEntity.ok(wallet) : ResponseEntity.notFound().build();
    }
}



