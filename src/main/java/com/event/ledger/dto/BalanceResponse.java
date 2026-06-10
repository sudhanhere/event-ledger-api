package com.event.ledger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BalanceResponse {

    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("netBalance")
    private Double netBalance;

    @JsonProperty("creditSum")
    private Double creditSum;

    @JsonProperty("debitSum")
    private Double debitSum;

    @JsonProperty("currency")
    private String currency;

    public BalanceResponse() {
    }

    public BalanceResponse(String accountId, Double netBalance, Double creditSum, Double debitSum, String currency) {
        this.accountId = accountId;
        this.netBalance = netBalance;
        this.creditSum = creditSum;
        this.debitSum = debitSum;
        this.currency = currency;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Double getNetBalance() {
        return netBalance;
    }

    public void setNetBalance(Double netBalance) {
        this.netBalance = netBalance;
    }

    public Double getCreditSum() {
        return creditSum;
    }

    public void setCreditSum(Double creditSum) {
        this.creditSum = creditSum;
    }

    public Double getDebitSum() {
        return debitSum;
    }

    public void setDebitSum(Double debitSum) {
        this.debitSum = debitSum;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "BalanceResponse{" +
                "accountId='" + accountId + '\'' +
                ", netBalance=" + netBalance +
                ", creditSum=" + creditSum +
                ", debitSum=" + debitSum +
                ", currency='" + currency + '\'' +
                '}';
    }
}
