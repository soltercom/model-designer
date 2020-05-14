package form.validator;

public interface Validator<T> {
    boolean validate(T value);
}
