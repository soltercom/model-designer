package formsfx.model.validator;

import java.util.function.Predicate;

public class CustomValidator<T> extends RootValidator<T> {

    private Predicate<T> callback;

    protected CustomValidator(Predicate<T> callback, String errorMessage) {
        super(errorMessage);
        this.callback = callback;
    }

    public static <E> CustomValidator<E> forPredicate(Predicate<E> callback, String errorMessage) {
        return new CustomValidator<>(callback, errorMessage);
    }

    @Override
    public ValidationResult validate(T input) {
        return createResult(callback.test(input));
    }
}
