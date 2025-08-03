package com.fintrack.crm.service.impl;

import com.fintrack.crm.dto.ExpenseRequest;
import com.fintrack.crm.dto.GroupedTransactionResponse;
import com.fintrack.crm.dto.IncomeRequest;
import com.fintrack.crm.dto.WalletTransactionResponse;
import com.fintrack.crm.entity.*;
import com.fintrack.crm.repository.*;
import com.fintrack.crm.service.IWalletTransactionService;
import com.fintrack.exception.BusinessException;
import com.fintrack.exception.enums.ErrorResultCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WalletTransactionService implements IWalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletRepository walletRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final TagRepository tagRepository;

    public WalletTransactionService(WalletTransactionRepository walletTransactionRepository,
                                    WalletRepository walletRepository,
                                    IncomeRepository incomeRepository,
                                    ExpenseRepository expenseRepository,
                                    TagRepository tagRepository) {
        this.walletTransactionRepository = walletTransactionRepository;
        this.walletRepository = walletRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<WalletTransactionResponse> getTransactionsByWalletId(Long walletId) {
        List<WalletTransactionEntity> entities = walletTransactionRepository.findByWalletId(walletId);
        return entities.stream().map(this::mapToDto).toList();
    }

    @Override
    public List<WalletTransactionResponse> getFilteredTransactions(Long userId, LocalDateTime start, LocalDateTime end) {
        List<WalletEntity> wallets = walletRepository.findAllByUserId(userId);
        if (wallets.isEmpty()) {
            throw new BusinessException(ErrorResultCode.WALLET_NOT_FOUND, "Wallet not found.");
        }

        WalletEntity wallet = wallets.get(0);

        LocalDateTime now = LocalDateTime.now();
        if (end != null && end.isAfter(now)) {
            throw new BusinessException(ErrorResultCode.END_DATE_INVALID, "End date cannot be in the future.");
        }

        List<WalletTransactionEntity> transactions = walletTransactionRepository.findByWalletUserId(userId);

        return transactions.stream()
                .filter(tx -> {
                    LocalDateTime txDate = tx.getTransactionDate();
                    if (start != null && txDate.isBefore(start)) return false;
                    if (end != null && txDate.isAfter(end)) return false;
                    return true;
                })
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<GroupedTransactionResponse> getGroupedTransactions(Long userId, LocalDateTime start, LocalDateTime end) {
        List<WalletTransactionResponse> filtered = getFilteredTransactions(userId, start, end);

        return filtered.stream()
                .collect(Collectors.groupingBy(WalletTransactionResponse::getTagName))
                .entrySet().stream()
                .map(entry -> new GroupedTransactionResponse(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Transactional
    public void addIncomeFromRequest(IncomeRequest request, Long userId) {
        WalletEntity wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new BusinessException(ErrorResultCode.WALLET_NOT_FOUND, "Wallet not found"));

        TagEntity tag = tagRepository.findById(request.getTagId())
                .orElseThrow(() -> new BusinessException(ErrorResultCode.TAG_NOT_FOUND, "Tag not found"));

        IncomeEntity income = new IncomeEntity();
        income.setUserId(userId);
        income.setWalletId(request.getWalletId());
        income.setTagId(request.getTagId());
        income.setAmount(request.getAmount());
        income.setIncomeType(request.getIncomeType());
        income.setPeriodType(request.getPeriodType());
        income.setCreatedAt(LocalDateTime.now());
        income.setTransactionDateTime(request.getTransactionDateTime());
        income.setDescription(request.getDescription());

        incomeRepository.save(income);

        BigDecimal currentBalance = wallet.getBalance();
        wallet.setBalance(currentBalance.add(request.getAmount()));
        walletRepository.save(wallet);

        WalletTransactionEntity transaction = new WalletTransactionEntity();
        transaction.setWallet(wallet);
        transaction.setTagId(tag.getId());
        transaction.setTransactionDate(request.getTransactionDateTime());
        transaction.setDescription(request.getDescription());
        transaction.setIncome(income);

        walletTransactionRepository.save(transaction);
    }


    @Transactional
    public void addExpenseFromRequest(ExpenseRequest request, Long userId) {
        WalletEntity wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new BusinessException(ErrorResultCode.WALLET_NOT_FOUND, "Wallet not found"));

        TagEntity tag = tagRepository.findById(request.getTagId())
                .orElseThrow(() -> new BusinessException(ErrorResultCode.TAG_NOT_FOUND, "Tag not found"));

        ExpenseEntity expense = new ExpenseEntity();
        expense.setUserId(userId);
        expense.setWalletId(request.getWalletId());
        expense.setTagId(request.getTagId());
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setTransactionDateTime(request.getTransactionDateTime());
        expense.setPeriodType(request.getPeriodType());
        expense.setExpenseType(request.getExpenseType());
        expense.setCreatedAt(LocalDateTime.now());

        expenseRepository.save(expense);

        BigDecimal currentBalance = wallet.getBalance();
        wallet.setBalance(currentBalance.subtract(request.getAmount()));
        walletRepository.save(wallet);

        WalletTransactionEntity transaction = new WalletTransactionEntity();
        transaction.setWallet(wallet);
        transaction.setTagId(tag.getId());
        transaction.setTransactionDate(request.getTransactionDateTime());
        transaction.setDescription(request.getDescription());
        transaction.setExpense(expense);

        walletTransactionRepository.save(transaction);
    }


    private WalletTransactionResponse mapToDto(WalletTransactionEntity entity) {
        WalletTransactionResponse dto = new WalletTransactionResponse();
        dto.setId(entity.getId());

        if (entity.getIncome() != null) {
            dto.setAmount(entity.getIncome().getAmount());
            dto.setType("Gelir");
        } else if (entity.getExpense() != null) {
            dto.setAmount(entity.getExpense().getAmount().negate());
            dto.setType("Gider");
        } else {
            dto.setAmount(BigDecimal.ZERO);
            dto.setType("Bilinmiyor");
        }

        dto.setTransactionDate(entity.getTransactionDate());
        dto.setDescription(entity.getDescription());

        Long tagId = entity.getTagId();
        if (tagId != null) {
            tagRepository.findById(tagId).ifPresentOrElse(tag -> {
                dto.setTagName(tag.getTagName());
                TagGroupEntity group = tag.getGroup();
                dto.setGroupName(group != null ? group.getTagGroupName() : "Unknown");
            }, () -> {
                dto.setTagName("Unknown");
                dto.setGroupName("Unknown");
            });
        } else {
            dto.setTagName("Unknown");
            dto.setGroupName("Unknown");
        }

        return dto;
    }
}









