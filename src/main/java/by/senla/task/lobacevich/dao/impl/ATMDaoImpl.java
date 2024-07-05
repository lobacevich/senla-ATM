package by.senla.task.lobacevich.dao.impl;

import by.senla.task.lobacevich.converter.CSVConverter;
import by.senla.task.lobacevich.dao.ATMDao;
import by.senla.task.lobacevich.entity.ATM;
import lombok.Getter;

import java.io.IOException;

public class ATMDaoImpl implements ATMDao {

    @Getter
    private static final ATMDao INSTANCE = new ATMDaoImpl();
    private final CSVConverter converter = CSVConverter.getINSTANCE();
    private ATM atm;

    private ATMDaoImpl() {
    }

    @Override
    public void loadATM() throws IOException {
        atm = new ATM(Integer.parseInt(converter.loadStringATM()));
    }

    @Override
    public void writeATM() throws IOException {
        converter.writeATMToFile(atm.getBalance().toString());
    }

    @Override
    public ATM getATM() {
        return atm;
    }
}
