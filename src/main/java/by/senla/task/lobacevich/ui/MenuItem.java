package by.senla.task.lobacevich.ui;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuItem {

    private final String title;
    private final IAction action;
    private final Menu nextMenu;

    public void doAction() {
        action.execute();
    }
}
