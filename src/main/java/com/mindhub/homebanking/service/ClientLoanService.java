package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;

public interface ClientLoanService {

    public void saveClientLoan(ClientLoan clientLoan);
}
