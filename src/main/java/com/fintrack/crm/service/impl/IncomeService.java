package com.fintrack.crm.service;

import com.fintrack.crm.dto.IncomeRequest;
import com.fintrack.crm.entity.IncomeEntity;
import com.fintrack.crm.repository.IncomeRepository;
import com.fintrack.crm.util.ValidationUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncomeService implements IIncomeService {

    private final IncomeRepository incomeRepository;
    private final IWalletService walletService;

    public IncomeService(IncomeRepository incomeRepository, IWalletService walletService) {
        this.incomeRepository = incomeRepository;
        this.walletService = walletService;
    }

    @Override
    public IncomeEntity addIncome(IncomeEntity income) {
        ValidationUtils.validateAmount(income.getAmount());
        IncomeEntity savedIncome = incomeRepository.save(income);
        walletService.increaseBalance(income.getUserId(), income.getAmount());
        return savedIncome;
    }

    @Override
    public List<IncomeEntity> getIncomesByUserId(Long userId) {
        return incomeRepository.findByUserId(userId);
    }

    @Override
    public IncomeEntity addIncomeFromRequest(IncomeRequest request) {
        IncomeEntity income = new IncomeEntity();
        income.setUserId(request.getUserId());
        income.setIncomeType(request.getIncomeType());
        income.setPeriodType(request.getPeriodType());
        income.setAmount(request.getAmount());
        income.setCreatedAt(LocalDateTime.now());

        return addIncome(income);
    }
}









