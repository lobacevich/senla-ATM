package by.senla.task.lobacevich.runner;

import by.senla.task.lobacevich.service.ATMService;
import by.senla.task.lobacevich.service.CardService;
import by.senla.task.lobacevich.service.impl.ATMServiceImpl;
import by.senla.task.lobacevich.service.impl.CardServiceImpl;
import by.senla.task.lobacevich.ui.MenuController;

public class Main {

    private static final ATMService atmService = ATMServiceImpl.getINSTANCE();
    private static final CardService cardService = CardServiceImpl.getINSTANCE();


    public static void main(String[] args) {
        atmService.loadATM();
        cardService.loadCards();
        MenuController.getINSTANCE().run();
        atmService.writeATM();
        cardService.writeCards();
    }
}