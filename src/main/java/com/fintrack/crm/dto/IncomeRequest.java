package com.fintrack.crm.dto;

import com.fintrack.crm.enums.IncomeType;
import com.fintrack.crm.enums.PeriodType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class IncomeRequest {
    private Long userId;
    private IncomeType incomeType;
    private PeriodType periodType;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private Long incomeId;
    private Long walletId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public IncomeType getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(IncomeType incomeType) {
        this.incomeType = incomeType;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public void setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Long getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(Long incomeId) {
        this.incomeId = incomeId;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

}
