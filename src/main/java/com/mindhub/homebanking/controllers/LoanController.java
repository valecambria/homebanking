package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CreateLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    LoanService loanService;
    @Autowired
    ClientService clientService;
    @Autowired
    private CardService cardService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private TransactionService transactionService;


    @GetMapping("/loan")
    public List<LoanDTO> loanDTOS(){
        return loanService.loanDTOS();
    }

    @Transactional
    @PostMapping(path = "/loans")
    public ResponseEntity<Object> addLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){

        Client currentClient = clientService.findByEmail(authentication.getName());
        Account currentAccount = accountService.findBynumber(loanApplicationDTO.getDestinationAccount());
        Loan selectedLoan = loanService.findById(loanApplicationDTO.getLoanId());

        if(currentClient != null){
            if(loanApplicationDTO.getAmount() <= 0){
                return new ResponseEntity<>("The amount must be higher than 0" ,HttpStatus.FORBIDDEN);
            }
            if(loanApplicationDTO.getPayments() == 0){
                return new ResponseEntity<>("Please select a number of payments", HttpStatus.FORBIDDEN);
            }
            if(loanApplicationDTO.getDestinationAccount().isEmpty()){
                return new ResponseEntity<>("The destination account does not exist", HttpStatus.FORBIDDEN);
            }
            if(loanApplicationDTO.getLoanId() == null){
                return new ResponseEntity<>("The loan you have selected is empty", HttpStatus.FORBIDDEN);
            }
            if(selectedLoan == null){
                return new ResponseEntity<>("The loan you are trying to choose does not exist", HttpStatus.FORBIDDEN);
            }
            if(loanApplicationDTO.getAmount() > selectedLoan.getMaxAmount()){
                return new ResponseEntity<>("You are exceeding the maximum amount", HttpStatus.FORBIDDEN);
            }
            if(!selectedLoan.getPayments().contains(loanApplicationDTO.getPayments())){
                return new ResponseEntity<>("The selected payments are not the established", HttpStatus.FORBIDDEN);
            }
            if(currentAccount == null){
                return new ResponseEntity<>("The selected account does not exist", HttpStatus.FORBIDDEN);
            }
            if(!currentClient.getAccounts().contains(currentAccount)){
                return new ResponseEntity<>("Destination account is not yours", HttpStatus.FORBIDDEN);
            }
            if(currentClient.getClientLoans().stream().filter(clientLoan -> clientLoan.getLoan().getName().equals(selectedLoan.getName())).toArray().length >= 1){
                return new ResponseEntity<>("You have already requested this loan", HttpStatus.FORBIDDEN);
            }
            if(loanApplicationDTO.getLoanId() == 9){
                ClientLoan clientLoan = new ClientLoan(currentClient, selectedLoan, loanApplicationDTO.getAmount()*1.2, loanApplicationDTO.getPayments());
                clientLoanService.saveClientLoan(clientLoan);

            }
            if(loanApplicationDTO.getLoanId() == 10){
                ClientLoan clientLoan = new ClientLoan(currentClient, selectedLoan, loanApplicationDTO.getAmount()*1.15, loanApplicationDTO.getPayments());
                clientLoanService.saveClientLoan(clientLoan);

            }
            if(loanApplicationDTO.getLoanId() == 11){
                ClientLoan clientLoan = new ClientLoan(currentClient, selectedLoan, loanApplicationDTO.getAmount()*1.1, loanApplicationDTO.getPayments());
                clientLoanService.saveClientLoan(clientLoan);

            }
            Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), selectedLoan.getName() + " Loan Approved ", LocalDateTime.now(), currentAccount);
            currentAccount.setBalance(currentAccount.getBalance() + loanApplicationDTO.getAmount());
            transactionService.saveTransaction(transaction);
            accountService.saveAccount(currentAccount);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    @Transactional
    @PostMapping("/loans/create")
    public ResponseEntity<Object> createLoan(@RequestBody CreateLoanDTO createLoanDTO){
        if(createLoanDTO.getName().isEmpty()){
            return new ResponseEntity<>("Loan name is empty", HttpStatus.BAD_REQUEST);
        }
        if(createLoanDTO.getMaxAmount() == 0)
        {
            return new ResponseEntity<>("Loan Max Amount is empty", HttpStatus.BAD_REQUEST);
        }
        if (createLoanDTO.getPayments().isEmpty())
        {
            return new ResponseEntity<>("Missing Payments", HttpStatus.BAD_REQUEST);
        }

        Loan loan = new Loan(createLoanDTO.getName(),createLoanDTO.getMaxAmount(),createLoanDTO.getPayments() );
        loanService.saveLoan(loan);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
