package com.fintrack.crm.controller;

import com.fintrack.crm.dto.WalletCreateRequest;
import com.fintrack.crm.entity.WalletEntity;
import com.fintrack.crm.service.impl.WalletService;
import com.fintrack.security.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    private final WalletService walletService;
    private final JwtUtil jwtUtil;

    public WalletController(WalletService walletService, JwtUtil jwtUtil) {
        this.walletService = walletService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<WalletEntity>> getUserWallets(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        List<WalletEntity> wallets = walletService.getWalletsByUserId(userId);
        return ResponseEntity.ok(wallets);
    }

    @PostMapping
    public ResponseEntity<WalletEntity> createWallet(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody WalletCreateRequest request) {

        String token = authHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        WalletEntity wallet = walletService.createWallet(request, userId);
        return ResponseEntity.ok(wallet);
    }
}




