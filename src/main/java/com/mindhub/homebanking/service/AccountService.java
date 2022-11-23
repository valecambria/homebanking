package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {

   public List<AccountDTO> getAccounts();

   public AccountDTO findByID(long id);

   public Account findBynumber(String number);

   Account findById(long id);

   public void saveAccount(Account account);
}
