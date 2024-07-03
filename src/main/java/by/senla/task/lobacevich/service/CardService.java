package by.senla.task.lobacevich.service;

import by.senla.task.lobacevich.entity.Card;
import by.senla.task.lobacevich.exception.CardIsBlockedException;
import by.senla.task.lobacevich.exception.CardNotFoundException;
import by.senla.task.lobacevich.exception.DepositLimitException;
import by.senla.task.lobacevich.exception.NotEnoughtMoneyException;

public interface CardService {

    int getBalance(Card card);

    Card getCard(String cardNumber) throws CardNotFoundException, CardIsBlockedException;

    int checkPin(Card card, String pinCode) throws CardIsBlockedException;

    void createCard(String cardNumber, String pinCode);

    void withdrawMoney(Card card, int sum) throws NotEnoughtMoneyException;

    void depositMoney(Card card, int sum) throws DepositLimitException;
}
