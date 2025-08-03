package com.fintrack.crm.dto;

import java.util.List;

public class GroupedTransactionResponse {

    private String groupName;
    private List<WalletTransactionResponse> transactions;

    public GroupedTransactionResponse(String groupName, List<WalletTransactionResponse> transactions) {
        this.groupName = groupName;
        this.transactions = transactions;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<WalletTransactionResponse> getTransactions() {
        return transactions;
    }
}
