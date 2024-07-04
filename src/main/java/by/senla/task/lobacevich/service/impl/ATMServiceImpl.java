package by.senla.task.lobacevich.service.impl;

import by.senla.task.lobacevich.dao.ATMDao;
import by.senla.task.lobacevich.dao.impl.ATMDaoImpl;
import by.senla.task.lobacevich.entity.ATM;
import by.senla.task.lobacevich.exception.CSVConvertException;
import by.senla.task.lobacevich.service.ATMService;
import lombok.Getter;

import java.io.IOException;

public class ATMServiceImpl implements ATMService {

    @Getter
    private static final ATMServiceImpl INSTANCE = new ATMServiceImpl();
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
            throw new CSVConvertException("Не удалось записать банкомат");
        }
    }

    @Override
    public ATM getATM() {
        return atmDao.getATM();
    }
}
