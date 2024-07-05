package by.senla.task.lobacevich.service;

import by.senla.task.lobacevich.entity.Card;
import by.senla.task.lobacevich.exception.CardInsufficientFundsException;
import by.senla.task.lobacevich.exception.CardIsBlockedException;
import by.senla.task.lobacevich.exception.CardNotFoundException;

import java.util.List;

public interface CardService {

    Card getCard(String cardNumber) throws CardNotFoundException;

    int checkPin(Card card, String pinCode) throws CardIsBlockedException;

    void createCard(String cardNumber, String pinCode);

    boolean checkCardExists(String cardNumber);

    void withdrawMoney(Card card, int sum) throws CardInsufficientFundsException;

    void depositMoney(Card card, int sum);

    void loadCards();

    void writeCards();

    List<Card> getCards();
}
