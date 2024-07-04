package by.senla.task.lobacevich.converter;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVConverter {

    @Getter
    private static final CSVConverter INSTANCE = new CSVConverter();
    private static final String ATM_PATH = "/ATM.csv";
    private static final String CARDS_PATH = "/cards.csv";
    private static final Path WRITE_PATH = Paths.get(System.getProperty("user.home"));

    private CSVConverter() {
    }

    public String loadStringATM() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(ATM_PATH);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.readLine();
        }
    }

    public void writeATMToFile(String ATMString) throws IOException {
        try (FileWriter writer = new FileWriter("./src/main/resources" + ATM_PATH)) {
            writer.write(ATMString);
        }
    }

    public List<String> loadStringCards() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(CARDS_PATH);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
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
        try (FileWriter writer = new FileWriter("./src/main/resources" + CARDS_PATH)) {
            writer.write(ATMString);
        }
    }
}
