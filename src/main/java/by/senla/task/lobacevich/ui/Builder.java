package by.senla.task.lobacevich.ui;

import by.senla.task.lobacevich.entity.Card;
import by.senla.task.lobacevich.exception.CardDublicateException;
import by.senla.task.lobacevich.exception.CardIsBlockedException;
import by.senla.task.lobacevich.exception.CardNotFoundException;
import by.senla.task.lobacevich.exception.DepositLimitException;
import by.senla.task.lobacevich.exception.NotEnoughtMoneyException;
import by.senla.task.lobacevich.service.CardService;
import by.senla.task.lobacevich.service.impl.CardServiceImpl;
import lombok.Getter;

import java.util.ArrayList;

public class Builder {

    @Getter
    private static final Builder INSTANCE = new Builder();
    @Getter
    private final Menu rootMenu = new Menu("Выберете действие", new ArrayList<>());
    private final Menu cardMenu = new Menu("Выберете действие", new ArrayList<>());
    private final ConsoleProcessor console = ConsoleProcessor.getINSTANCE();
    private final CardService cardService = CardServiceImpl.getINSTANCE();
    private final Navigator navigator = Navigator.getINSTANCE();

    private Builder() {
    }

    public void buildRootMenu() {
        MenuItem useCard = new MenuItem("использовать карточку", () -> {
            System.out.println("Введите номер карты(формат ХХХХ-ХХХХ-ХХХХ-ХХХХ)");
            String cardNumber = console.getCardNumberInput();
            try {
                Card card = cardService.getCard(cardNumber);
                checkPinCode(card);
                buildCardMenu(card);
            } catch (CardNotFoundException | CardIsBlockedException e) {
                System.out.println(e.getMessage());
                navigator.setCurrentMenu(rootMenu);
            }
        }, cardMenu);
        rootMenu.addItem(useCard);

        MenuItem newCard = new MenuItem("Выпустить новую карточку", () -> {
            System.out.println("Придумайте номер карты(формат ХХХХ-ХХХХ-ХХХХ-ХХХХ)");
            String cardNumber = console.getCardNumberInput();
            System.out.println("Придумайте пин код(4 цифры)");
            String pinCode = console.getPinCodeInput();
            try {
                cardService.createCard(cardNumber, pinCode);
                System.out.println("Карта " + cardNumber + " успешно выпущена");
            } catch (CardDublicateException e) {
                System.out.println(e.getMessage());
            }
        }, rootMenu);
        rootMenu.addItem(newCard);
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

    public void buildCardMenu(Card card) {
        MenuItem checkBalance = new MenuItem("Проверить баланс",
                () -> System.out.println(cardService.getBalance(card)), cardMenu);
        cardMenu.addItem(checkBalance);

        MenuItem withdrawMoney = new MenuItem("Снять деньги", () -> {
            System.out.println("Введите сумму");
            int sum = console.getSumInput();
            try {
                cardService.withdrawMoney(card, sum);
                System.out.println("Средства в размере " + sum + " успешно выведены");
            } catch (NotEnoughtMoneyException e) {
                System.out.println(e.getMessage());
            }
        }, cardMenu);
        cardMenu.addItem(withdrawMoney);

        MenuItem depositMoney = new MenuItem("Зачислить деньги на карту", () -> {
            System.out.println("Введите сумму");
            int sum = console.getSumInput();
            try {
                cardService.depositMoney(card, sum);
                System.out.println("Карта успешно пополнена на сумму " + sum);
            } catch (DepositLimitException e) {
                System.out.println(e.getMessage());
            }
        }, cardMenu);
        cardMenu.addItem(depositMoney);

        MenuItem toRootMenu = new MenuItem("В главное меню", () -> {
        }, rootMenu);
        cardMenu.addItem(toRootMenu);
    }
}
