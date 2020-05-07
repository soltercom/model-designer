package formsfx.model.validator;

public abstract class RootValidator<T> implements Validator<T> {

    private String errorMessage;

    protected RootValidator(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    protected ValidationResult createResult(boolean result) {
        return new ValidationResult(result, errorMessage);
    }

}
