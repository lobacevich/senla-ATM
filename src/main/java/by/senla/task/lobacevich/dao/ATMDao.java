package by.senla.task.lobacevich.dao;

import by.senla.task.lobacevich.entity.ATM;

import java.io.IOException;

public interface ATMDao {

    void loadATM() throws IOException;

    void writeATM() throws IOException;

    ATM getATM();
}
