package com.fintrack.crm.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WalletTransactionResponse {

    private Long id;
    private BigDecimal amount;
    private String tagName;
    private String description;
    private LocalDateTime transactionDate;
    private String groupName;
    private String type;

    public Long getId() {
        return id != null ? id : 0L;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount != null ? amount : BigDecimal.ZERO;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTagName() {
        return tagName != null ? tagName : "UNKNOWN";
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate != null ? transactionDate : LocalDateTime.now();
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getGroupName() {
        return groupName != null ? groupName : "UNKNOWN";
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getType() {
        return type != null ? type : "UNKNOWN";
    }

    public void setType(String type) {
        this.type = type;
    }
}

