package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @RequestMapping("/transactions")
    public List<TransactionDTO> getTransactions(){
        return transactionService.getTransactions();
    }
    @RequestMapping("/transactions/{id}")
    public TransactionDTO getTransaction(@PathVariable long id){
        return transactionService.findById(id);
    }


    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction (Authentication authentication,
                                                     @RequestParam double amount, @RequestParam String description, @RequestParam String accountO, @RequestParam String accountD) {

        Client clientCurrent = clientService.findByEmail(authentication.getName());
        Account sourceAccount = accountService.findBynumber(accountO);
        Account destinationAccount = accountService.findBynumber(accountD);
        Set<Account> accountExist = clientCurrent.getAccounts().stream().filter(account -> account.getNumber().equals(accountO)).collect(Collectors.toSet());



        if (amount <= 0) {
            return new ResponseEntity<>("Missing amount", HttpStatus.EXPECTATION_FAILED);
        }
        if (description.isEmpty()) {
            return new ResponseEntity<>("Missing description", HttpStatus.FORBIDDEN);
        }
        if (accountO.isEmpty() ) {
            return new ResponseEntity<>("Missing source account", HttpStatus.FORBIDDEN);
        }
        if (accountD.isEmpty()) {
            return new ResponseEntity<>("Missing destination account", HttpStatus.FORBIDDEN);
        }

        if (accountO.equals(accountD)){
            return new ResponseEntity<>("Source account cannot be the destination account", HttpStatus.FORBIDDEN);
        }

        if(accountExist.isEmpty()){
            return new ResponseEntity<>("Your account does not exist", HttpStatus.FORBIDDEN);
        }

        if(destinationAccount == null){
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }

        if(sourceAccount.getBalance() < amount){
            return new ResponseEntity<>("You have not enough founds", HttpStatus.FORBIDDEN);
        }

        Transaction transactionOrigin = new Transaction(TransactionType.DEBIT, amount, description + " " + destinationAccount.getNumber(), LocalDateTime.now(), sourceAccount);
        Transaction transactionDestin = new Transaction(TransactionType.CREDIT, amount, description + " " + sourceAccount.getNumber(), LocalDateTime.now(), destinationAccount);

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        transactionService.saveTransaction(transactionOrigin);
        transactionService.saveTransaction(transactionDestin);

        accountService.saveAccount(sourceAccount);
        accountService.saveAccount(destinationAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}



