package by.senla.task.lobacevich.dao.impl;

import by.senla.task.lobacevich.dao.CardDao;
import by.senla.task.lobacevich.entity.Card;
import by.senla.task.lobacevich.exception.CardNotFoundException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CardDaoImpl implements CardDao {

    @Getter
    private static final CardDaoImpl INSTANCE = new CardDaoImpl();
    private final List<Card> cards = new ArrayList<>();

    private CardDaoImpl() {
    }

    @Override
    public void saveCard(Card card) {
        cards.add(card);
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    @Override
    public Card getCardByNumber(String cardNumber) throws CardNotFoundException {
        return cards.stream()
                .filter(x -> x.getNumber().equals(cardNumber))
                .findFirst()
                .orElseThrow(() -> new CardNotFoundException("Карта с номером " + cardNumber +
                        " не найдена"));
    }
}
