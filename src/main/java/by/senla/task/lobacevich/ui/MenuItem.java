package by.senla.task.lobacevich.ui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class MenuItem {

    private final String title;
    private final IAction action;
    private Menu nextMenu;

    public void doAction() {
        action.execute();
    }
}
