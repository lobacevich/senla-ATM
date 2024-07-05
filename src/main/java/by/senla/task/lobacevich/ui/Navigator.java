package by.senla.task.lobacevich.ui;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
public class Navigator {

    @Getter
    private static final Navigator INSTANCE = new Navigator();
    private Menu currentMenu;

    private Navigator() {
    }

    public void printMenu() {
        System.out.println("\n" + currentMenu.getName());
        System.out.println(0 + "\tЗакончить работу");
        List<MenuItem> menuItems = currentMenu.getMenuItems();
        for (int i = 0; i < menuItems.size(); i++) {
            System.out.println((i + 1) + "\t" + menuItems.get(i).getTitle());
        }
    }

    public boolean navigate(Integer index) {
        if (index == 0) {
            return false;
        }
        MenuItem item = currentMenu.getMenuItems().get(index - 1);
        item.doAction();
        if (item.getNextMenu() != null) {
            setCurrentMenu(item.getNextMenu());
        }
        return true;
    }
}
