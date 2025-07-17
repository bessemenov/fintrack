package com.fintrack.crm.controller;

import com.fintrack.crm.entity.WalletEntity;
import com.fintrack.crm.service.WalletService;
import com.fintrack.crm.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    private final WalletService walletService;
    private final JwtUtil jwtUtil;

    public WalletController(WalletService walletService, JwtUtil jwtUtil) {
        this.walletService = walletService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<WalletEntity> getWallet(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7); // "Bearer " kısmını sil
        Long currentUserId = jwtUtil.extractUserId(token);

        if (!currentUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        WalletEntity wallet = walletService.getWallet(userId);
        return wallet != null ? ResponseEntity.ok(wallet) : ResponseEntity.notFound().build();
    }
}

