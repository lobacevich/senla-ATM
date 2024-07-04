package by.senla.task.lobacevich.service.impl;

import by.senla.task.lobacevich.dao.CardDao;
import by.senla.task.lobacevich.dao.impl.CardDaoImpl;
import by.senla.task.lobacevich.entity.ATM;
import by.senla.task.lobacevich.entity.Card;
import by.senla.task.lobacevich.exception.CSVConvertException;
import by.senla.task.lobacevich.exception.CardDublicateException;
import by.senla.task.lobacevich.exception.CardIsBlockedException;
import by.senla.task.lobacevich.exception.CardNotFoundException;
import by.senla.task.lobacevich.exception.DepositLimitException;
import by.senla.task.lobacevich.exception.NotEnoughtMoneyException;
import by.senla.task.lobacevich.service.ATMService;
import by.senla.task.lobacevich.service.CardService;
import lombok.Getter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CardServiceImpl implements CardService {

    @Getter
    private static final CardServiceImpl INSTANCE = new CardServiceImpl();
    private static final Integer DEPOSIT_LIMIT = 1000000;
    private final CardDao cardDao = CardDaoImpl.getINSTANCE();
    private final ATMService atmService = ATMServiceImpl.getINSTANCE();

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
    public void createCard(String cardNumber, String pinCode) throws CardDublicateException {
        Optional<Card> optCard = getCards().stream().filter(x -> x.getNumber().equals(cardNumber)).findFirst();
        if (optCard.isPresent()) {
            throw new CardDublicateException("Карта с номером " + cardNumber + " уже существует");
        }
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
        ATM atm = atmService.getATM();
        if (card.getBalance() >= sum && atm.getBalance() >=sum) {
            card.setBalance(card.getBalance() - sum);
            atm.setBalance(atm.getBalance() - sum);
        } else if (card.getBalance() < sum){
            throw new NotEnoughtMoneyException("На карте недостаточно средств для вывода");
        } else {
            throw new NotEnoughtMoneyException("В банкомате недостаточно средств для вывода. Осталось " +
                    atm.getBalance());
        }
    }

    @Override
    public void depositMoney(Card card, int sum) throws DepositLimitException {
        ATM atm = atmService.getATM();
        if (sum <= DEPOSIT_LIMIT) {
            card.setBalance(card.getBalance() + sum);
            atm.setBalance(atm.getBalance() - sum);
        } else {
            throw new DepositLimitException("Превышен лимит пополнения карты. Максимльная сумма пополнения " + DEPOSIT_LIMIT);
        }
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
