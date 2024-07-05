package by.senla.task.lobacevich.service.impl;

import by.senla.task.lobacevich.dao.ATMDao;
import by.senla.task.lobacevich.dao.impl.ATMDaoImpl;
import by.senla.task.lobacevich.entity.ATM;
import by.senla.task.lobacevich.exception.AtmInsufficientFundsException;
import by.senla.task.lobacevich.exception.CSVConvertException;
import by.senla.task.lobacevich.service.ATMService;
import lombok.Getter;

import java.io.IOException;

public class ATMServiceImpl implements ATMService {

    @Getter
    private static final ATMService INSTANCE = new ATMServiceImpl();
    private final ATMDao atmDao = ATMDaoImpl.getINSTANCE();


    private ATMServiceImpl() {
    }

    @Override
    public void loadATM() {
        try {
            atmDao.loadATM();
            System.out.println("Банкомат загружен");
        } catch (IOException e) {
            System.out.println("Ошибка загрузки(банкомат)");
            throw new CSVConvertException(e.getMessage());
        }
    }

    @Override
    public void writeATM() {
        try {
            atmDao.writeATM();
            System.out.println("Банкомат записан");
        } catch (IOException e) {
            System.out.println("Ошибка записи(банкомат)");
            throw new CSVConvertException(e.getMessage());
        }
    }

    @Override
    public void withdrawMoney(int sum) throws AtmInsufficientFundsException {
        ATM atm = atmDao.getATM();
        if (atm.getBalance() >=sum) {
            atm.setBalance(atm.getBalance() - sum);
        } else {
            throw new AtmInsufficientFundsException("В банкомате не достаточно средств для вывода. Осталось " +
                    atm.getBalance());
        }
    }

    @Override
    public void depositMoney(int sum) {
        ATM atm = atmDao.getATM();
        atm.setBalance(atm.getBalance() + sum);
    }
}
