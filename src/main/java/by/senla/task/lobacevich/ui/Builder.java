package by.senla.task.lobacevich.ui;

import lombok.Getter;

import java.util.ArrayList;

public class Builder {

    @Getter
    private static final Builder INSTANCE = new Builder();
    @Getter
    private final Menu rootMenu = new Menu("Выберете действие", new ArrayList<>());
    private final Menu cardMenu = new Menu("Выберете действие", new ArrayList<>());
    private final MenuService menuService = MenuService.getINSTANCE();
    private final Navigator navigator = Navigator.getINSTANCE();

    private Builder() {
        buildRootMenu();
        buildCardMenu();
    }

    public void buildRootMenu() {
        MenuItem useCard = new MenuItem("использовать карточку", () ->
            navigator.setCurrentMenu(menuService.authorise() ? cardMenu : rootMenu));
        rootMenu.addItem(useCard);

        MenuItem newCard = new MenuItem("Выпустить новую карточку",
                menuService::issueCard, rootMenu);
        rootMenu.addItem(newCard);
    }

    public void buildCardMenu() {
        MenuItem checkBalance = new MenuItem("Проверить баланс",
                menuService::printCardBalance, cardMenu);
        cardMenu.addItem(checkBalance);

        MenuItem withdrawMoney = new MenuItem("Снять деньги",
                menuService::withdrawMoney, cardMenu);
        cardMenu.addItem(withdrawMoney);

        MenuItem depositMoney = new MenuItem("Зачислить деньги на карту",
                menuService::depositMoney, cardMenu);
        cardMenu.addItem(depositMoney);

        MenuItem toRootMenu = new MenuItem("В главное меню",
                menuService::finishSession, rootMenu);
        cardMenu.addItem(toRootMenu);
    }
}
