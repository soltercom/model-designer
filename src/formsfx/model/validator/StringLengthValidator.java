package formsfx.model.validator;

public class StringLengthValidator extends CustomValidator<String> {

    private StringLengthValidator(int min, int max, String errorMessage) {
        super(input -> input.length() >= min && input.length() <= max, errorMessage);
    }

    public static StringLengthValidator between(int min, int max, String errorMessage) {
        if (min < 0) {
            throw new IllegalArgumentException("Minimum string length cannot be negative.");
        } else if (min > max) {
            throw new IllegalArgumentException("Minimum must not be larger than maximum.");
        }

        return new StringLengthValidator(min, max, errorMessage);
    }

    public static StringLengthValidator atLeast(int min, String errorMessage) {
        if (min < 0) {
            throw new IllegalArgumentException("Minimum string length cannot be negative.");
        }

        return new StringLengthValidator(min, Integer.MAX_VALUE, errorMessage);
    }

    public static StringLengthValidator upTo(int max, String errorMessage) {
        return new StringLengthValidator(0, max, errorMessage);
    }

    public static StringLengthValidator exactly(int value, String errorMessage) {
        return new StringLengthValidator(value, value, errorMessage);
    }

}
