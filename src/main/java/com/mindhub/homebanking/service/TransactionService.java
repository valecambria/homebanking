package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Transaction;

import java.util.List;

public interface TransactionService {

    public List<TransactionDTO> getTransactions();

    public TransactionDTO findById(long id);
    public void saveTransaction(Transaction transaction);
}
