package by.senla.task.lobacevich.dao;

import by.senla.task.lobacevich.entity.Card;
import by.senla.task.lobacevich.exception.CardNotFoundException;

import java.io.IOException;
import java.util.List;

public interface CardDao {

    void saveCard(Card card);

    List<Card> getCards();

    Card getCardByNumber(String cardNumber) throws CardNotFoundException;

    void loadCards() throws IOException;

    void writeCards() throws IOException;
}
