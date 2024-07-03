package by.senla.task.lobacevich.ui;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Menu {

    private final String name;
    private final List<MenuItem> menuItems;

    public void addItem(MenuItem item) {
        menuItems.add(item);
    }
}
