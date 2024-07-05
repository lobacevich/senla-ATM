package by.senla.task.lobacevich.ui;

import by.senla.task.lobacevich.entity.Card;
import by.senla.task.lobacevich.exception.AtmInsufficientFundsException;
import by.senla.task.lobacevich.exception.CardInsufficientFundsException;
import by.senla.task.lobacevich.exception.CardIsBlockedException;
import by.senla.task.lobacevich.exception.CardNotFoundException;
import by.senla.task.lobacevich.service.ATMService;
import by.senla.task.lobacevich.service.CardService;
import by.senla.task.lobacevich.service.impl.ATMServiceImpl;
import by.senla.task.lobacevich.service.impl.CardServiceImpl;
import lombok.Getter;

import java.time.LocalDateTime;

public class MenuService {

    @Getter
    private static final MenuService INSTANCE = new MenuService();
    private static final Integer DEPOSIT_LIMIT = 1000000;
    private final SessionHolder sessionHolder = SessionHolder.getINSTANCE();
    private final ConsoleProcessor console = ConsoleProcessor.getINSTANCE();
    private final CardService cardService = CardServiceImpl.getINSTANCE();
    private final ATMService atmService = ATMServiceImpl.getINSTANCE();

    private MenuService() {
    }

    public boolean authorise() {
        System.out.println("Введите номер карты(формат ХХХХ-ХХХХ-ХХХХ-ХХХХ)");
        String cardNumber = console.getCardNumberInput();
        try {
            Card card = cardService.getCard(cardNumber);
            if (card.isBlocked()) {
                if (checkBlockIsEnded(card)) {
                    card.setBlocked(false);
                } else {
                    throw new CardIsBlockedException("карта " + cardNumber +
                            " заблокирована. Автоматическая разблокировка " +
                            card.getTimeCardWasBlocked().plusDays(1L));
                }
            }
            checkPinCode(card);
            sessionHolder.startSession(card);
            return true;
        } catch (CardNotFoundException | CardIsBlockedException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean checkBlockIsEnded(Card card) {
        return card.getTimeCardWasBlocked().isBefore(LocalDateTime.now().minusDays(1));
    }

    private void checkPinCode(Card card) throws CardIsBlockedException {
        System.out.println("Введите пин код(4 цифры)");
        String pinCode = console.getPinCodeInput();
        int incorrectPinAmount = cardService.checkPin(card, pinCode);
        if (incorrectPinAmount != 0) {
            System.out.println("Неверный пин код. Осталось попыток " + (3 - incorrectPinAmount));
            checkPinCode(card);
        }
    }

    public void issueCard() {
        System.out.println("Придумайте номер карты(формат ХХХХ-ХХХХ-ХХХХ-ХХХХ)");
        String cardNumber = console.getCardNumberInput();
        if (!cardService.checkCardExists(cardNumber)) {
            System.out.println("Придумайте пин код(4 цифры)");
            String pinCode = console.getPinCodeInput();
            cardService.createCard(cardNumber, pinCode);
            System.out.println("Карта " + cardNumber + " успешно выпущена");
        } else {
            System.out.println("Карта c номером " + cardNumber + " уже существует");
        }
    }

    public void printCardBalance() {
        System.out.println(sessionHolder.getCard().getBalance());
    }

    public void withdrawMoney() {
        System.out.println("Введите сумму");
        int sum = console.getSumInput();
        try {
            cardService.withdrawMoney(sessionHolder.getCard(), sum);
            atmService.withdrawMoney(sum);
            System.out.println("Средства в размере " + sum + " успешно выведены");
        } catch (CardInsufficientFundsException e) {
            System.out.println(e.getMessage());
        } catch (AtmInsufficientFundsException e) {
            System.out.println(e.getMessage());
            sessionHolder.getCard().setBalance(sessionHolder.getCard().getBalance() + sum);
        }
    }

    public void depositMoney() {
        System.out.println("Введите сумму");
        int sum = console.getSumInput();
        if (sum <= DEPOSIT_LIMIT) {
            cardService.depositMoney(sessionHolder.getCard(), sum);
            atmService.depositMoney(sum);
            System.out.println("Карта успешно пополнена на сумму " + sum);
        } else {
            System.out.println("Превышен лимит пополнения карты. Максимльная сумма пополнения " + DEPOSIT_LIMIT);
        }
    }

    public void finishSession() {
        sessionHolder.clearSession();
    }
}
