package by.senla.task.lobacevich.service.impl;

import by.senla.task.lobacevich.dao.CardDao;
import by.senla.task.lobacevich.dao.impl.CardDaoImpl;
import by.senla.task.lobacevich.entity.Card;
import by.senla.task.lobacevich.exception.CardIsBlockedException;
import by.senla.task.lobacevich.exception.CardNotFoundException;
import by.senla.task.lobacevich.exception.DepositLimitException;
import by.senla.task.lobacevich.exception.NotEnoughtMoneyException;
import by.senla.task.lobacevich.service.CardService;
import lombok.Getter;

import java.time.LocalDateTime;

public class CardServiceImpl implements CardService {

    @Getter
    private static final CardServiceImpl INSTANCE = new CardServiceImpl();
    private static final Integer DEPOSIT_LIMIT = 1000000;
    private final CardDao cardDao = CardDaoImpl.getINSTANCE();

    private CardServiceImpl() {
    }

    @Override
    public int getBalance(Card card) {
        return card.getBalance();
    }

    @Override
    public Card getCard(String cardNumber) throws CardNotFoundException, CardIsBlockedException {
        Card card = cardDao.getCardByNumber(cardNumber);
        if (card.isBlocked()) {
            if (card.getTimeCardWasBlocked().isBefore(LocalDateTime.now().minusDays(1))) {
                card.setBlocked(false);
            } else {
                throw new CardIsBlockedException("карта " + cardNumber +
                        " заблокирована. Автоматическая разблокировка " +
                        card.getTimeCardWasBlocked().plusDays(1L));
            }
        }
        return card;
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
    public void withdrawMoney(Card card, int sum) throws NotEnoughtMoneyException {
        if (card.getBalance() >= sum) {
            card.setBalance(card.getBalance() - sum);
        } else {
            throw new NotEnoughtMoneyException("На карте недостаточно средств для вывода");
        }
    }

    @Override
    public void depositMoney(Card card, int sum) throws DepositLimitException {
        if (sum <= DEPOSIT_LIMIT) {
            card.setBalance(card.getBalance() + sum);
        } else {
            throw new DepositLimitException("Превышен лимит пополнения карты. Максимльная сумма пополнения " + DEPOSIT_LIMIT);
        }
    }
}
