package by.senla.task.lobacevich.service;

import by.senla.task.lobacevich.exception.AtmInsufficientFundsException;

public interface ATMService {

    void loadATM();

    void writeATM();

    void withdrawMoney(int sum) throws AtmInsufficientFundsException;

    void depositMoney(int sum);
}
