package com.mindhub.homebanking.dtos;

import java.util.ArrayList;
import java.util.List;

public class CreateLoanDTO {

    private Long loanId;
    private String name;
    private double maxAmount;
    private List<Integer> payments = new ArrayList<>();

    public Long getLoanId() {
        return loanId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }
}
