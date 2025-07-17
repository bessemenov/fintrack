package com.fintrack.crm.service;

import com.fintrack.crm.entity.IncomeEntity;
import com.fintrack.crm.exception.InvalidIncomeException;
import com.fintrack.crm.repository.IncomeRepository;
import com.fintrack.crm.util.ValidationUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final WalletService walletService;

    public IncomeService(IncomeRepository incomeRepository, WalletService walletService) {
        this.incomeRepository = incomeRepository;
        this.walletService = walletService;
    }

    public IncomeEntity addIncome(IncomeEntity income) {

        ValidationUtils.validateAmount(income.getAmount());
        IncomeEntity savedIncome = incomeRepository.save(income);
        walletService.increaseBalance(income.getUserId(), income.getAmount());
        return savedIncome;
    }

    public List<IncomeEntity> getIncomesByUserId(Long userId) {
        return incomeRepository.findByUserId(userId);
    }
}




