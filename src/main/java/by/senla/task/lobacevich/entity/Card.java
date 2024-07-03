package by.senla.task.lobacevich.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Card {

    private String number;
    private String pin;
    private Integer balance;
    private boolean isBlocked;
    private int incorrectPinCount;
    private LocalDateTime timeCardWasBlocked;
}
