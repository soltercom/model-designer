package form.validator;

public class ModelRequiredValidator implements Validator<Object> {
    @Override
    public boolean validate(Object value) {
        return !value.toString().isEmpty();
    }
}
