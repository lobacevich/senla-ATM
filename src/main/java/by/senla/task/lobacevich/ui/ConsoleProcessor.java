package by.senla.task.lobacevich.ui;

import by.senla.task.lobacevich.exception.ValidationException;
import by.senla.task.lobacevich.validator.Validator;
import lombok.Getter;

import java.util.Scanner;

public class ConsoleProcessor {

    @Getter
    private static final ConsoleProcessor INSTANCE = new ConsoleProcessor();

    private ConsoleProcessor() {
    }

    public int getMenuInput() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                return sc.nextInt();
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод, попробуйте еще раз");
            }
        }
    }

    public String getCardNumberInput() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                String cardNumber = sc.nextLine();
                Validator.validateCardNumber(cardNumber);
                return cardNumber;
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String getPinCodeInput() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                String pinCode = sc.nextLine();
                Validator.validatePinCode(pinCode);
                return pinCode;
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public int getSumInput() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                int sum = sc.nextInt();
                Validator.validateSum(sum);
                return sum;
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод, попробуйте еще раз");
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
