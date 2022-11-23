package com.mindhub.homebanking.dtos;

import java.util.List;

public class LoanApplicationDTO {

    private Long loanId;
    private Double amount;
    private int payments;
    private String destinationAccount;

    public Long getLoanId() {
        return loanId;
    }

    public Double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }
}
