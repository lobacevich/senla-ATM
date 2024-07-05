package by.senla.task.lobacevich.converter;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVConverter {

    @Getter
    private static final CSVConverter INSTANCE = new CSVConverter();
    private static final String ATM_PATH = "./src/main/resources/ATM.csv";
    private static final String CARDS_PATH = "./src/main/resources/cards.csv";

    private CSVConverter() {
    }

    public String loadStringATM() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(ATM_PATH))) {
            return reader.readLine();
        }
    }

    public void writeATMToFile(String ATMString) throws IOException {
        try (FileWriter writer = new FileWriter(ATM_PATH)) {
            writer.write(ATMString);
        }
    }

    public List<String> loadStringCards() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(CARDS_PATH))) {
            List<String> data = new ArrayList<>();
            String line = reader.readLine();
            while (line != null) {
                data.add(line);
                line = reader.readLine();
            }
            return data;
        }
    }

    public void writeCardsToFile(String ATMString) throws IOException {
        try (FileWriter writer = new FileWriter(CARDS_PATH)) {
            writer.write(ATMString);
        }
    }
}
