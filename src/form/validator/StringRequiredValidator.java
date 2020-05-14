package form.validator;

public class StringRequiredValidator implements Validator<String>{
    @Override
    public boolean validate(String value) {
        return !value.isEmpty();
    }
}
