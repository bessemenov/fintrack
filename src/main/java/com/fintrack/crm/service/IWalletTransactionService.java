package com.fintrack.crm.service;

import com.fintrack.crm.dto.ExpenseRequest;
import com.fintrack.crm.dto.GroupedTransactionResponse;
import com.fintrack.crm.dto.IncomeRequest;
import com.fintrack.crm.dto.WalletTransactionRequest;
import com.fintrack.crm.dto.WalletTransactionResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface IWalletTransactionService {

    List<WalletTransactionResponse> getTransactionsByWalletId(Long walletId);

    List<WalletTransactionResponse> getFilteredTransactions(Long userId, LocalDateTime start, LocalDateTime end);

    List<GroupedTransactionResponse> getGroupedTransactions(Long userId, LocalDateTime start, LocalDateTime end);
}

