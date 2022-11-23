package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.Card;

public interface CardService {

    public void saveCard(Card card);

    public void deleteCard(Card card);

    public Card getCard(String number);
}
