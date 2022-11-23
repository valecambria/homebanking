package com.mindhub.homebanking.utils;

public class CardUtils {
    private CardUtils() {
    }
    public static String getCardNumber(){
        String cardRandomNumber = (getRandomNumber(0000, 9999) + " " +  getRandomNumber(0000, 9999) + " " + getRandomNumber(0000, 9999) + " " + getRandomNumber(0000, 9999));
        return cardRandomNumber;
    }

    public static int getCvvNumber(){
        int cvvRandomNumber = (getRandomNumber(000, 999));
        return cvvRandomNumber;
    }
    public static int getRandomNumber (int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }
}
