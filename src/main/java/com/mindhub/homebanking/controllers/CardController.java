package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    ClientService clientService;

    @Autowired
    CardService cardService;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType type, @RequestParam CardColor color){
        Client currentClient = clientService.findByEmail(authentication.getName());
        String cardRandomNumber = CardUtils.getCardNumber();
        int cvvRandomNumber = CardUtils.getCvvNumber();
        if(type.equals(CardType.DEBIT)){
            if(currentClient.getCards().stream().filter(card -> card.getType().equals(CardType.DEBIT)).collect(Collectors.toSet()).size() >= 3){
                return new ResponseEntity<>("You've reached the maximum amount of cards per client", HttpStatus.FORBIDDEN);
            }
        }
        if(type.equals(CardType.CREDIT)){
            if(currentClient.getCards().stream().filter(card -> card.getType().equals(CardType.CREDIT)).collect(Collectors.toSet()).size() >= 3){
                return new ResponseEntity<>("You've reached the maximum amount of cards per client", HttpStatus.FORBIDDEN);
            }
        }
//        if(currentClient.getCards().stream().filter(card -> card.getColor().equals(color)).count()>=3){
//            return new ResponseEntity<>("You cannot have more than 3 cards of the same color.", HttpStatus.FORBIDDEN);
//        }

        cardService.saveCard(new Card(currentClient, currentClient.getFirstName() + " " + currentClient.getLastName(), type, color,cardRandomNumber, cvvRandomNumber, LocalDate.now(), LocalDate.now().plusYears(5), true));
        return new ResponseEntity<>("Your card has been successfully created", HttpStatus.CREATED);
    }

    public int getCvvRandomNumber() {
        int cvvRandomNumber = (getRandomNumber(000, 999));
        return cvvRandomNumber;
    }

    public String getCardRandomNumber() {
        String cardRandomNumber = (getRandomNumber(0000, 9999) + " " +  getRandomNumber(0000, 9999) + " " + getRandomNumber(0000, 9999) + " " + getRandomNumber(0000, 9999));
        return cardRandomNumber;
    }

    @PatchMapping("/clients/current/cards")
    public ResponseEntity<Object> deleteCard(@RequestParam String number ,Authentication authentication){
        Client currentClient = clientService.findByEmail(authentication.getName());
        Card selectedCard = cardService.getCard(number);

        if (currentClient != null){
            selectedCard.setActive(false);
            cardService.saveCard(selectedCard);
            return new ResponseEntity<>("Card deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
    }

    public int getRandomNumber (int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }
}
