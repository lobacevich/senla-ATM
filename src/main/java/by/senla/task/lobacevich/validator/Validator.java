package by.senla.task.lobacevich.validator;

import by.senla.task.lobacevich.exception.ValidationException;

public class Validator {

    private static final String CARD_NUMBER_PATTERN = "^\\d{4}-\\d{4}-\\d{4}-\\d{4}$";
    private static final String PIN_CODE_PATTERN = "^\\d{4}$";

    private Validator() {
    }

    public static void validateCardNumber(String cardNumber) throws ValidationException {
        if (cardNumber == null || !cardNumber.matches(CARD_NUMBER_PATTERN)) {
            throw new ValidationException("Неверный формат номера карты, попробуйте снова");
        }
    }

    public static void validatePinCode(String pinCode) throws ValidationException {
        if (pinCode == null || !pinCode.matches(PIN_CODE_PATTERN)) {
            throw new ValidationException("Неверный формат пинкода, попробуйте снова");
        }
    }

    public static void validateSum(int sum) throws ValidationException {
        if (sum <= 0) {
            throw new ValidationException("Сумма транзакции не может быть отрицательной или равной 0\n" +
                    "Повторите ввод");
        }
    }
}
