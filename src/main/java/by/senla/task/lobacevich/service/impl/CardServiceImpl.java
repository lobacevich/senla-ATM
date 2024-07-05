package by.senla.task.lobacevich.service.impl;

import by.senla.task.lobacevich.dao.CardDao;
import by.senla.task.lobacevich.dao.impl.CardDaoImpl;
import by.senla.task.lobacevich.entity.Card;
import by.senla.task.lobacevich.exception.CSVConvertException;
import by.senla.task.lobacevich.exception.CardInsufficientFundsException;
import by.senla.task.lobacevich.exception.CardIsBlockedException;
import by.senla.task.lobacevich.exception.CardNotFoundException;
import by.senla.task.lobacevich.service.CardService;
import lombok.Getter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CardServiceImpl implements CardService {

    @Getter
    private static final CardService INSTANCE = new CardServiceImpl();
    private final CardDao cardDao = CardDaoImpl.getINSTANCE();

    private CardServiceImpl() {
    }

    @Override
    public Card getCard(String cardNumber) throws CardNotFoundException {
        return cardDao.getCardByNumber(cardNumber);
    }

    @Override
    public int checkPin(Card card, String pinCode) throws CardIsBlockedException {
        if (card.getPin().equals(pinCode)) {
            return 0;
        } else {
            card.setIncorrectPinCount(card.getIncorrectPinCount() + 1);
        }
        if (card.getIncorrectPinCount() == 3) {
            card.setBlocked(true);
            card.setTimeCardWasBlocked(LocalDateTime.now());
            throw new CardIsBlockedException("карта " + card.getNumber() +
                    " заблокирована. Автоматическая разблокировка " +
                    card.getTimeCardWasBlocked().plusDays(1L));
        }
        return card.getIncorrectPinCount();
    }

    @Override
    public void createCard(String cardNumber, String pinCode) {
        Card card = Card.builder()
                .number(cardNumber)
                .pin(pinCode)
                .balance(0)
                .incorrectPinCount(0)
                .isBlocked(false)
                .build();
        cardDao.saveCard(card);
    }

    @Override
    public boolean checkCardExists(String cardNumber) {
        Optional<Card> optCard = getCards().stream().filter(x -> x.getNumber().equals(cardNumber)).findFirst();
        return optCard.isPresent();
    }

    @Override
    public void withdrawMoney(Card card, int sum) throws CardInsufficientFundsException {
        if (card.getBalance() >= sum) {
            card.setBalance(card.getBalance() - sum);
        } else {
            throw new CardInsufficientFundsException("На карте не достаточно средств для вывода");
        }
    }

    @Override
    public void depositMoney(Card card, int sum) {
            card.setBalance(card.getBalance() + sum);
    }

    @Override
    public void loadCards() {
        try {
            cardDao.loadCards();
            System.out.println("Карточки загружены");
        } catch (IOException e) {
            System.out.println("Ошибка загрузки(банкомат)");
            throw new CSVConvertException(e.getMessage());
        }
    }

    @Override
    public void writeCards() {
        try {
            cardDao.writeCards();
            System.out.println("Карточки записаны");
        } catch (IOException e) {
            System.out.println("Ошибка записи(карточки)");
            throw new CSVConvertException("Не удалось записать карточки");
        }
    }

    @Override
    public List<Card> getCards() {
        return cardDao.getCards();
    }
}
