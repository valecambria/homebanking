package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {
	@Autowired
	private PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return args -> {

			Client client1 = new Client("Melba", "Morel", "MelbaMorel@mindhub.com", passwordEncoder.encode("melba"));
			Client client2 = new Client("Valentino", "Cambria", "vale.cambria@hotmail.com", passwordEncoder.encode("vale123"));
			Client client3 = new Client("admin", "admin", "admin@mindhub.com", passwordEncoder.encode("mindhubadmin"));
			clientRepository.save(client1); // va a guardar al cliente en la tabla, donde declaramos el entity
			clientRepository.save(client2);
			clientRepository.save(client3);
			Account account1 = new Account("VIN001", LocalDateTime.now(), 5000.0, client1, AccountType.CURRENT, true);
			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.0, client1, AccountType.SAVINGS, true);
			Account account3 = new Account("VIN003", LocalDateTime.now(), 9000.0, client2, AccountType.SAVINGS, true);
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			Transaction transaction1 = new Transaction(TransactionType.DEBIT, -200.00, "Clothing", LocalDateTime.now(), account1);
			Transaction transaction2 = new Transaction(TransactionType.CREDIT, 100.00, "Videogames", LocalDateTime.now(), account1);
			Transaction transaction3 = new Transaction(TransactionType.DEBIT, -400.00, "Clothing", LocalDateTime.now(), account2);
			Transaction transaction4 = new Transaction(TransactionType.CREDIT, 200.00, "Videogames", LocalDateTime.now(), account2);
			Transaction transaction5 = new Transaction(TransactionType.CREDIT, 200.00, "Videogames", LocalDateTime.now(), account3);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			Loan loan1 = new Loan("Mortgage", 5000000.00, List.of(6, 12, 24, 36, 48, 60));
			Loan loan2 = new Loan("Personal", 1000000.00, List.of(6, 12, 24));
			Loan loan3 = new Loan("Vehicle", 300000.00, List.of(6, 12, 24, 36));
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);
			ClientLoan clientLoan1 = new ClientLoan(client1, loan1, 400000.00, 60);
			ClientLoan clientLoan2 = new ClientLoan(client1, loan2, 50000.00, 12);
			ClientLoan clientLoan3 = new ClientLoan(client2, loan2,100000.00, 24);
			ClientLoan clientLoan4 = new ClientLoan(client2, loan3,200000.00, 36);
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);
			Card card1 = new Card(client1 ,client1.getFirstName() + " " + client1.getLastName(), CardType.DEBIT, CardColor.GOLD, "1234-5678-9101-1112", 556, LocalDate.now(), LocalDate.now().plusYears(5), true);
			Card card2 = new Card(client1 ,client1.getFirstName() + " " + client1.getLastName(), CardType.CREDIT, CardColor.TITANIUM, "3535-5678-8754-5566", 575, LocalDate.now(), LocalDate.now().plusYears(5), true);
			Card card3 = new Card(client2 ,client2.getFirstName() + " " + client2.getLastName(), CardType.CREDIT, CardColor.SILVER, "1111-2222-3333-4444", 615, LocalDate.now(), LocalDate.now().plusYears(5), true);
			Card card4 = new Card(client1, client1.getFirstName() + " " + client1.getLastName(), CardType.CREDIT, CardColor.SILVER, "1111-2222-5555-6666", 888,LocalDate.now(),LocalDate.now(), true);
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
			cardRepository.save(card4);
		};
	}
}
