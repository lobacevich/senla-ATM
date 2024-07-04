package by.senla.task.lobacevich.dao.impl;

import by.senla.task.lobacevich.converter.CSVConverter;
import by.senla.task.lobacevich.dao.CardDao;
import by.senla.task.lobacevich.entity.Card;
import by.senla.task.lobacevich.exception.CardNotFoundException;
import lombok.Getter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CardDaoImpl implements CardDao {

    @Getter
    private static final CardDaoImpl INSTANCE = new CardDaoImpl();
    private final CSVConverter converter = CSVConverter.getINSTANCE();
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

    @Override
    public void loadCards() throws IOException {
        List<String> data = converter.loadStringCards();
        for (String line : data) {
            String[] cardFields = line.split(" ");
            Card card = Card.builder()
                    .number(cardFields[0])
                    .pin(cardFields[1])
                    .balance(Integer.parseInt(cardFields[2]))
                    .isBlocked(Boolean.parseBoolean(cardFields[3]))
                    .build();
            if (!cardFields[4].equals("null")) {
                card.setTimeCardWasBlocked(LocalDateTime.parse(cardFields[4]));
            }
            cards.add(card);
        }
    }

    @Override
    public void writeCards() throws IOException {
        StringBuilder data = new StringBuilder();
        for (Card card : cards) {
            data.append(card.getNumber()).append(" ");
            data.append(card.getPin()).append(" ");
            data.append(card.getBalance()).append(" ");
            data.append(card.isBlocked()).append(" ");
            data.append(card.getTimeCardWasBlocked()).append("\n");
        }
        converter.writeCardsToFile(data.toString());
    }
}
