package formsfx.model.validator;

public interface Validator<T> {
    ValidationResult validate(T input);
}
