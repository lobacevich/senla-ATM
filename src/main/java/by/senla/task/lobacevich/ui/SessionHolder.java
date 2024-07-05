package by.senla.task.lobacevich.ui;

import by.senla.task.lobacevich.entity.Card;
import lombok.Getter;
import lombok.Setter;

@Setter
public class SessionHolder {

    @Getter
    private static final SessionHolder INSTANCE = new SessionHolder();
    @Getter
    private Card card;

    private SessionHolder() {
    }

    public void startSession(Card card) {
        this.card = card;
    }

    public void clearSession() {
        card = null;
    }
}
