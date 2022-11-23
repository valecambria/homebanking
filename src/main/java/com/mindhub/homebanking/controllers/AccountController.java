package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccounts();
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccounts(@PathVariable long id) {
        return accountService.findByID(id);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> currentAccount(Authentication authentication, @RequestParam AccountType accountType) {
        Client currentClient = clientService.findByEmail(authentication.getName());
        if (currentClient.getAccounts().size() >= 3) {
            return new ResponseEntity<>("You can not have more than 3 accounts", HttpStatus.FORBIDDEN);
        } else {
            accountService.saveAccount(new Account("VIN-"+ getRandomNumber(11111111, 99999999), LocalDateTime.now(), 0.0, currentClient, accountType, true)); //Ver como agregar los 0 antes del numero random
            return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
        }
    }

    @PatchMapping("/clients/current/accounts/delete")
    public ResponseEntity<Object> deleteAccount(@RequestParam String number){
        Account deletedAccount = accountService.findBynumber(number);
        if (deletedAccount.getBalance() > 0) {
            return new ResponseEntity<>("Account balance is bigger than $0", HttpStatus.FORBIDDEN);
        }
        deletedAccount.setActive(false);
        accountService.saveAccount(deletedAccount);
        return new ResponseEntity<>("Account deleted", HttpStatus.OK);
    }



    public int getRandomNumber (int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }
}


