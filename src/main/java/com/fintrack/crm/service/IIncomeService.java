package com.fintrack.crm.service;

import com.fintrack.crm.dto.IncomeRequest;
import com.fintrack.crm.entity.IncomeEntity;

import java.util.List;


public interface IIncomeService {
    IncomeEntity addIncome(IncomeEntity income);

    IncomeEntity addIncomeFromRequest(IncomeRequest request); // Bunu ekle

    List<IncomeEntity> getIncomesByUserId(Long userId);

}

