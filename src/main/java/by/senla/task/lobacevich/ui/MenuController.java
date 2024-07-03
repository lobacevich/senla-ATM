package by.senla.task.lobacevich.ui;

import lombok.Getter;

public class MenuController {

    @Getter
    private static final MenuController INSTANCE = new MenuController();
    private final Builder builder = Builder.getINSTANCE();
    private final Navigator navigator = Navigator.getINSTANCE();
    private final ConsoleProcessor console = ConsoleProcessor.getINSTANCE();

    private MenuController() {
    }

    public void run() {
        builder.buildRootMenu();
        navigator.setCurrentMenu(builder.getRootMenu());
        boolean flag = true;
        while (flag) {
            navigator.printMenu();
            int index = console.getMenuInput();
            try {
                flag = navigator.navigate(index);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Неверный ввод, попробуйте еще раз");
            }
        }
    }
}
