package com.fintrack.crm.controller;

import com.fintrack.crm.dto.IncomeRequest;
import com.fintrack.crm.entity.IncomeEntity;
import com.fintrack.crm.entity.WalletEntity;
import com.fintrack.crm.service.IIncomeService;
import com.fintrack.crm.service.IWalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incomes")
public class IncomeController {

    private final IIncomeService incomeService;
    private final IWalletService walletService;

    public IncomeController(IIncomeService incomeService, IWalletService walletService) {
        this.incomeService = incomeService;
        this.walletService = walletService;
    }
    @PostMapping
    public ResponseEntity<IncomeEntity> addIncome(@RequestBody IncomeRequest request) {
        IncomeEntity saved = incomeService.addIncomeFromRequest(request);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<IncomeEntity>> getIncomes(@RequestParam Long userId) {
        List<IncomeEntity> incomes = incomeService.getIncomesByUserId(userId);
        return ResponseEntity.ok(incomes);
    }

    @GetMapping("/wallet")
    public ResponseEntity<WalletEntity> getWallet(@RequestParam Long userId) {
        WalletEntity wallet = walletService.getWallet(userId);
        return wallet != null ? ResponseEntity.ok(wallet) : ResponseEntity.notFound().build();
    }
}




