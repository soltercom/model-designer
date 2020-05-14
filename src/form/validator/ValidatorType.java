package form.validator;

public enum ValidatorType {

    STRING_REQUIRED(new StringRequiredValidator()),
    MODEL_REQUIRED(new ModelRequiredValidator()),
    NAME(new NameValidator());

    private final Validator<?> validator;
    ValidatorType(Validator<?> validator) {
        this.validator = validator;
    }

    public Validator<?> getValidator() {
        return validator;
    }
}
